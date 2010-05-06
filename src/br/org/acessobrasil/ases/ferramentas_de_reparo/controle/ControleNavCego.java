/*******************************************************************************
 * Copyright 2005, 2006, 2007, 2008 Acessibilidade Brasil
 * Este arquivo é parte do programa ASES - Avaliador e Simulador para AcessibilidadE de Sítios
 * O ASES é um software livre; você pode redistribui-lo e/ou modifica-lo dentro dos termos da Licença Pública Geral GNU como
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da Licença, ou (na sua opnião) qualquer versão posterior.
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer  MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU para maiores detalhes.
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU, sob o título "LICENCA.txt", junto com este programa, se não, escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *******************************************************************************/

/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007 Acessibilidade Brasil.
 * 
 * This file is part of ASES.
 *
 * ASES is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * A copy of the license can be found at 
 * http://www.gnu.org/copyleft/lesser.txt.
 *******************************************************************************/

package br.org.acessobrasil.ases.ferramentas_de_reparo.controle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.id.jericho.lib.html.Source;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
/**
 * Faz as operações para simular a navegação de cegos
 * @author Fabio Issamu Oshiro, Renato Tomaz Nati
 */
public class ControleNavCego {
	final int LETRAS_SEC = 11;
	
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	int vert = 0;
	/**
	 * Guarda o conteúdo com a página linear
	 */
	private String textoParaPainel;
	
	String enderecopagina;

	int correcaoPos;
	
	/**
	 * Conteúdo HTML
	 */
	private String conteudoPagina;
	
	/**
	 * Links resultantes
	 */
	ArrayList<Resultado> arrResultado = new ArrayList<Resultado>();
	
	public ControleNavCego() {
	
	}

	/**
	 * construtor
	 */
	public ControleNavCego(String conteudo) {
		avalia(conteudo);
	}

	/**
	 * @param s número de caracteres
	 * @return retorna o tempo na forma 00:00
	 */
	private String getTempoFormatado(int s){
		int segundosTotal = s/11;
		int minutos = segundosTotal/60;
		int segundos = segundosTotal%60;
		String m;
		if(minutos>9){
			m = String.valueOf(minutos);
		}else{
			m = "0"+minutos;
		}
		if(segundos>9){
			return m + ":" +segundos;
		}else{
			return m + ":0" +segundos;
		}
	}
	/**
	 * Trata o conteudo para cegos, 
	 * retirando as tags e deixando somente o 
	 * conteúdo que os leitores narram
	 * @param conteudo
	 * @return texto sem as tags
	 */
	private String getConteudoCego(String conteudo){
		//System.out.println("getConteudoCego("+conteudo+")");
		int lastEnd=0;
		RegrasHardCodedEmag regras = new RegrasHardCodedEmag();
		Pattern patTag = Pattern.compile("<.*?>",Pattern.DOTALL);
		Matcher matTag = patTag.matcher(conteudo.toLowerCase());
		StringBuilder textoValido2 = new StringBuilder();
		while(matTag.find()){			
			String tag = matTag.group();
			String tagLow = tag.toLowerCase();
			int posAtual=matTag.start();
			String toAdd = htmlRender(conteudo.substring(lastEnd, posAtual));
			if(!toAdd.trim().equals("")){
				textoValido2.append(toAdd+" ");
			}
			//tratar se precisa pegar algum atributo da tag
			if(tagLow.startsWith("<img")){
				textoValido2.append("[");
				textoValido2.append(htmlRender(regras.getAtributo(tag,"alt")));
				textoValido2.append("] ");
			}
			if(tagLow.startsWith("<input")){
				String type=regras.getAtributo(tagLow,"type");
				if(type.equals("text") || type.equals("") || type.equals("buttom") ||  type.equals("submit") ||  type.equals("reset")){
					textoValido2.append("[");
					textoValido2.append(htmlRender(regras.getAtributo(tag,"value")));
					textoValido2.append("] ");
				}
			}
			lastEnd=matTag.end();
		}
		String toAdd = htmlRender(conteudo.substring(lastEnd));
		if(!toAdd.trim().equals("")){
			textoValido2.append(toAdd+" ");
		}
		//System.out.println("\treturn '"+textoValido2.toString().replaceAll("  "," ")+"'");
		return textoValido2.toString().replaceAll("  "," ");
	}

	public void avalia(String conteudo){
		textoParaPainel="";
		//Retira script, style e comentario 
		this.conteudoPagina = preparaTexto(conteudo);
		String conteudoTratado = this.conteudoPagina;
		conteudo = conteudo.toLowerCase();
		HashSet<String> nomeVertices = getVerticesName();
		RegrasHardCodedEmag regras = new RegrasHardCodedEmag();
		//buscar os vertices no código
		Pattern patTag = Pattern.compile("<.*?>",Pattern.DOTALL);
		Matcher matTag = patTag.matcher(conteudoTratado.toLowerCase());
		StringBuilder textoValido2 = new StringBuilder();
		int lastEnd=0;
		int tValidoIni = 0;
		Vertex origem = new Vertex();
		origem.setConteudo("");
		origem.setNomeVertice("");
		ArrayList<Vertex> arrVertice = new ArrayList<Vertex>();
		arrVertice.add(origem);
		//loop por todas as tags
		while(matTag.find()){			
			String tag = matTag.group();
			String tagLow = tag.toLowerCase();
			String strId = regras.getAtributo(tag,"id");
			String strHref = regras.getAtributo(tag,"href");
			int posAtual=matTag.start();
			String strName = regras.getAtributo(tag,"name");
			String toAdd = htmlRender(conteudoTratado.substring(lastEnd, posAtual));
			if(!toAdd.trim().equals("")){
				textoValido2.append(toAdd+" ");
			}
			//tratar se precisa pegar algum atributo da tag
			if(tagLow.startsWith("<img")){
				textoValido2.append("[");
				textoValido2.append(htmlRender(regras.getAtributo(tag,"alt")));
				textoValido2.append("] ");
			}
			if(tagLow.startsWith("<input")){
				String type=regras.getAtributo(tagLow,"type");
				if(type.equals("text") || type.equals("") || type.equals("buttom") ||  type.equals("submit") ||  type.equals("reset")){
					textoValido2.append("[");
					textoValido2.append(htmlRender(regras.getAtributo(tag,"value")));
					textoValido2.append("] ");
				}
			}
			lastEnd = matTag.end();
			if(nomeVertices.contains(strId) || nomeVertices.contains(strName) || (strHref.startsWith("#") && nomeVertices.contains(strHref.substring(1)))){
				//ver o custo		
				int charValido=textoValido2.length()-tValidoIni;
				origem.setConteudo(textoValido2.substring(tValidoIni, textoValido2.length()).replaceAll("  "," "));
				Vertex v = new Vertex("");
				v.setNomeVertice(tagLow);
				tValidoIni=textoValido2.length();
				if(strHref.startsWith("#")){
					//coloca o texto do link
					int posCloseA = conteudoTratado.toLowerCase().indexOf("</a>",matTag.end());
					if(posCloseA>-1){
						v.textoLink=getConteudoCego(conteudoTratado.substring(matTag.end(),posCloseA));
					}
				}
				Edge e = new Edge(origem,v,charValido);
				origem.insertEdge(e);
				arrVertice.add(v);
				origem=v;								
			}
		}
		{
			//Colocar o último nó
			int charValido=textoValido2.length()-tValidoIni;
			origem.setConteudo(textoValido2.substring(tValidoIni, textoValido2.length()));
			Vertex v = new Vertex("");
			v.setNomeVertice("Fim");
			Edge e = new Edge(origem,v,charValido);
			v.insertEdge(e);
			origem.insertEdge(e);
			arrVertice.add(v);
		}
		
		textoValido2.append(conteudoTratado.substring(lastEnd, conteudoTratado.length()));
		//Colocar o resto dos edges de custo "zero"
		for(Vertex v:arrVertice){
			if(v.getNomeVertice().startsWith("<a")){
				String strHref = regras.getAtributo(v.getNomeVertice(),"href");
				for(int k=0;k<arrVertice.size();k++){
					Vertex v2 = arrVertice.get(k);
					String strId = regras.getAtributo(v2.getNomeVertice(),"id");
					if(strId.equals("")){
						strId = regras.getAtributo(v2.getNomeVertice(),"name");
					}
					//strId = ao nome ou id da tag
					if(!strId.equals("") && !strHref.equals("")){
						if(strId.equals(strHref.substring(1))){
							String txtDestino="";
							int m=0;
							while(txtDestino.length()<50 && k+m<arrVertice.size()){
								Vertex tmp = arrVertice.get(k+m);
								txtDestino += tmp.getConteudo().replaceAll("  "," ").replaceAll("(\n|\r)","");
								if(txtDestino.length()>50){
									txtDestino = txtDestino.substring(0, 50)+"...";
								}
								m++;
							}
							arrResultado.add(new Resultado(v.textoLink, txtDestino,v,v2));
							Edge e = new Edge(v,v2,5);
							v.insertEdge(e);
						}
					}
				}
			}
		}
		///*
		ArrayList<Vertex> arrVerticeOriginal = new ArrayList<Vertex>();
		arrVerticeOriginal.addAll(arrVertice);
		dijkstra(arrVertice);
		textoValido2 = new StringBuilder();
		final int maxBuff = 150;
		ArrayList<String> arrStr=new ArrayList<String>();
		for(Vertex v:arrVerticeOriginal){
			arrStr.clear();
			v.setIndexOf(textoValido2.toString().length());
			if(!v.getConteudo().equals("")){
				String conteudVert=v.getConteudo().replaceAll("(\n|\r)", "");
				int posAtual=conteudVert.indexOf(" ",maxBuff);
				if(posAtual!=-1){
					arrStr.add(conteudVert.substring(0,posAtual));
				}else{
					arrStr.add(conteudVert);
				}
				while(posAtual!=-1){
					int posAnt=posAtual;
					posAtual=conteudVert.indexOf(" ",posAtual+maxBuff);
					if(posAtual!=-1){
						arrStr.add(conteudVert.substring(posAnt,posAtual));
					}else{
						arrStr.add(conteudVert.substring(posAnt,conteudVert.length()));
					}
				}
				if(!arrStr.get(0).equals("")){
					textoValido2.append(getTempoFormatado(v.getCusto()));
					textoValido2.append("\n");
					textoValido2.append(arrStr.get(0));
					textoValido2.append("\n");
					textoValido2.append("\n");
				}
				int tempoSoma=v.getCusto();
				for(int i=1;i<arrStr.size();i++){
					if(!arrStr.get(i).equals("")){
						tempoSoma+=arrStr.get(i-1).length();
						textoValido2.append(getTempoFormatado(v.getCusto()+tempoSoma));
						textoValido2.append("\n");
						textoValido2.append(arrStr.get(i));
						textoValido2.append("\n");
						textoValido2.append("\n");
					}
				}
			}
		}//*/
		textoParaPainel=textoValido2.toString();
	}

	private ArrayList<Vertex> dijkstra(ArrayList<Vertex> vertices) {
		final int infinito = 9999999;
		int menorCustoAtual = infinito;
		int pesoAnterior = infinito;

		Edge menorEdge = null;
		Vertex menorVertice = null;
		/**
		 * com o custo já computados
		 */
		/*
		System.out.println("\n\n\n\n\tantes do dijkstra: ");
		for (Vertex vertice : vertices) {
			System.out.println("vertice: " + vertice.getNomeVertice() + "- custo: " + vertice.getCusto());
			for (Edge edge : vertice.getEdges()) {
				System.out.println("\tedge: " + edge.getOrigem().getNomeVertice() + " - " + edge.getDestino().getNomeVertice() + " - peso: " + edge.peso);
			}
		}*/
		ArrayList<Vertex> perm = new ArrayList<Vertex>();
		// custo infinito para todos os vertices
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).custo = infinito;
		}
		// for (Vertex vertice : vertices) {
		// vertice.peso = infinito;
		// }
		// seta o primeiro commo 0 e coloca no conjunto
		vertices.get(0).custo = 0;
		perm.add(vertices.get(0));

		// elegendo o menor vertice

		menorVertice = vertices.get(0);
		vertices.remove(0);

		while (vertices.size() > 0) {
			menorCustoAtual = infinito;
			pesoAnterior = infinito;

			for (Edge edge : menorVertice.getEdges()) {

				if (edge.peso + menorVertice.custo < edge.destino.custo) {
					edge.destino.custo = edge.peso + menorVertice.custo;

				}

				if (edge.peso < menorCustoAtual) {
					menorEdge = edge;
					edge.destino.edgeLigaPai = menorEdge;// ???
					menorCustoAtual = edge.destino.custo;
				}
			}

			vertices.remove(menorVertice);
			for (Vertex vertice : vertices) {

				if (vertice.custo < pesoAnterior) {
					menorVertice = vertice;
					pesoAnterior = vertice.custo;
				}
			}

			// System.out.print("Custo="+menorVertice.custo+"\n");

			perm.add(menorVertice);
		}
		/*
		System.out.println("\n\n\n\n\tdepois do dijkstra: ");
		for (Vertex vertice : perm) {
			System.out.println("vertice: " + vertice.getNomeVertice() + "- custo: " + vertice.getCusto());
			for (Edge edge : vertice.getEdges()) {
				System.out.println("\tedge: " + edge.getOrigem().getNomeVertice() + " - " + edge.getDestino().getNomeVertice() + " - peso: " + edge.peso);
			}
		}
		System.out.println("tam de perm: " + perm.size());
		*/
		if (perm.size() > 1)
			if (perm.get(perm.size() - 1).compareTo(perm.get(perm.size() - 2))) {
				perm.remove(perm.size() - 1);
			}
		return perm;
	}

	/**
	 * acha os edges ainda falta os edges de letura direta Coloca os custos e
	 * liga um vertice a outro.
	 */
	public ArrayList<Vertex> tracaEdges(ArrayList<Vertex> vertices) {

		int iniPosNomeAncora = 0;
		int fimPosNomeAncora = 0;
		String ancora;

		for (Vertex vertice : vertices) {
			String conteudoVertice = ((String) vertice.getConteudo()).toLowerCase();
			//System.out.println("nomeVertice: " + vertice.getNomeVertice());

			// System.out.println("ini vertice content------------");
			// System.out.println(conteudoVertice);
			// System.out.println("end vertice content------------");
			int ultHref = 0;

			while (ultHref != -1) {

				int posRelHref = conteudoVertice.indexOf("href=\"#", ultHref + 1);

				ultHref = posRelHref;
				/*
				 * if(vertice.getNomeVertice().equals("_note-0")){
				 * System.out.println("ultHref: "+ultHref);
				 * System.out.println(conteudoVertice); }
				 * if(vertice.getNomeVertice().equals("column-one")){
				 * System.out.println("ultHref: "+ultHref);
				 * System.out.println(conteudoVertice); }//
				 */
				// String VertContent=conteudoVertice.substring(0);
				if (ultHref == -1) {
					break;
				}
				int initTagdoHref = posRelHref;
				for (; conteudoVertice.charAt(initTagdoHref) != '<'; initTagdoHref--)
					;

				int medidaAresta = contadorDeLetrasValidas(0, initTagdoHref - 1, conteudoVertice);
				// Vertex origem, Vertex destino, int custo
				iniPosNomeAncora = posRelHref + "href=\"#".length();
				fimPosNomeAncora = conteudoVertice.indexOf("\"", iniPosNomeAncora);
				// guarda o nome de saida numa variavel
				ancora = conteudoVertice.substring(iniPosNomeAncora, fimPosNomeAncora);
				//System.out.println("nome de saida(tracaEdges): " + ancora);

				for (int i = 0; i < vertices.size(); i++) {
					//System.out.println(vertices.get(i).getNomeVertice() + "," + ancora);
					if (vertices.get(i).getNomeVertice().equals(ancora)) {
						Edge edge = new Edge(vertice, vertices.get(i), medidaAresta / LETRAS_SEC);
						edge.posOrigem = vertice.inicioRegiao;
						edge.posDestino = vertices.get(i).inicioRegiao;
						vertice.insertEdge(edge);
						//System.out.println("achou o vertice");
						break;
					} else if (i == vertices.size() - 1) {

						//System.out.println("erro: saida sem destino: " + ancora);
					}
				}
			}
		}
		for (int vertice = 0; vertice < vertices.size() - 1; vertice++) {// demora
			// pouco
			// para
			// chegar
			// aqui

			Edge edge = new Edge(vertices.get(vertice), vertices.get(vertice + 1), 0);
			edge.posOrigem = vertices.get(vertice).inicioRegiao;
			edge.posDestino = vertices.get(vertice + 1).inicioRegiao;
			edge.peso = contadorDeLetrasValidas(edge.posOrigem, edge.posDestino, this.conteudoPagina) / LETRAS_SEC;
			vertices.get(vertice).insertEdge(edge);

		}

		/*
		 * for (int vertice = 0; vertice < vertices.size() - 1; vertice++) {//
		 * demora // pouco // para // chegar // aqui
		 * 
		 * for (int verticeMaisAbaixo = vertice + 1; verticeMaisAbaixo <
		 * vertices.size(); verticeMaisAbaixo++) {
		 * 
		 * Edge edge = new Edge(vertices.get(vertice),
		 * vertices.get(verticeMaisAbaixo), 0); edge.posOrigem =
		 * vertices.get(vertice).inicioRegiao; edge.posDestino =
		 * vertices.get(verticeMaisAbaixo).inicioRegiao; edge.peso =
		 * contadorDeLetrasValidas(edge.posOrigem, edge.posDestino,
		 * this.conteudoPagina) / 14; vertices.get(vertice).insertEdge(edge); }
		 * }//
		 */

		return vertices;

	}


	/**
	 * Retorna os nomes das ancoras
	 * @return
	 */
	private HashSet<String> getVerticesName(){
		
		HashSet<String> retorno = new HashSet<String>();
		
		String conteudo = conteudoPagina.toLowerCase();
		int ultPosHref = conteudo.indexOf("href=\"#", 0);
		if(ultPosHref==-1){
			//não há ancoras abortar
			return retorno;
		}
		int comecoAncora = ultPosHref + "href=\"#".length();
		int fimAncora = conteudo.indexOf("\"", comecoAncora);
		String ancora = conteudo.substring(comecoAncora, fimAncora);
		//System.out.println("\t" + cont + ": '" + ancora + "'");
		int posDestino=0;
		while (ultPosHref != -1) { // 0,5 sec
			//Procura o destino
			posDestino = conteudo.indexOf("id=" + ancora + " ");
			if (posDestino == -1) {
				posDestino = conteudo.indexOf("id='" + ancora + "'");
			}
			if (posDestino == -1) {
				posDestino = conteudo.indexOf("id=\"" + ancora + "\"");
			}
			if (posDestino == -1) {
				posDestino = conteudo.indexOf("name=" + ancora + " ");
			}
			if (posDestino == -1) {
				posDestino = conteudo.indexOf("name='" + ancora + "'");
			}
			if (posDestino == -1) {
				posDestino = conteudo.indexOf("name=\"" + ancora + "\"");
			}

			if (posDestino == -1) {
				//System.out.println(TradSimuladorNavegacao.PROBLEMAS_HTML);
				ultPosHref = conteudo.indexOf("href=\"#", ultPosHref + 1);
				comecoAncora = ultPosHref + "href=\"#".length();
				// pode dar problema aqui por falta de tolerancia a erros...
				fimAncora = conteudo.indexOf("\"", comecoAncora);
				ancora = conteudo.substring(comecoAncora, fimAncora);
				continue;
			}
			// nomeVertice.add(ancora);
			
			//posicoes.add(ultPosHref);
			
			retorno.add(ancora);

			// procura o proximo
			ultPosHref = conteudo.indexOf("href=\"#", ultPosHref + 1);

			comecoAncora = ultPosHref + "href=\"#".length();
			//System.out.println("comecoNome" + comecoAncora);
			fimAncora = conteudo.indexOf("\"", comecoAncora);
			//System.out.println("fimNome" + fimAncora);
			ancora = conteudo.substring(comecoAncora, fimAncora);
		}
		return retorno;
	}
	
	
	/**
	 * Retira os espaços e os tabs
	 * @param conteudo
	 * @return o conteúdo sem espaços
	 */
	public String retiraExcessodeEspacos(String conteudo) {
		conteudo = conteudo.toString().replaceAll("  *", " ");
		conteudo = conteudo.replaceAll("\t", "");
		return conteudo;
	}

	public String textoComSegundos(String conteudo, int segundos) {

		String textoPronto = new String();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbTempo = new StringBuilder();
		String partes[] = new String[conteudo.split("\n").length];
		partes = conteudo.split("\n");

		partes[0] = partes[0].toString().replaceAll("  ", " ");
		while (!partes[0].equals(partes[0].replaceAll("  ", " "))) {
			partes[0] = partes[0].replaceAll("  ", " ");
		}
		partes[0] = partes[0].replaceAll("\t", "");

		if (!partes[0].equals("")) {
			sb.append((((sbTempo.toString().length()) / LETRAS_SEC) + segundos) / 60 + " min e " + ((sbTempo.toString().length() / LETRAS_SEC) + segundos) % 60 + " seg \n" + partes[0] + "\n\n\n");
			sbTempo.append(partes[0] + "\n");
		}

		for (int i = 1; i < partes.length; i++) {
			// System.out.println("PARTE "+1+partes[i]);
			partes[i] = partes[i].replaceAll("  ", " ");
			while (!partes[i].equals(partes[i].replaceAll("  ", " "))) {
				partes[i] = partes[i].replaceAll("  ", " ");
			}
			partes[i] = partes[i].replaceAll("\t", "");

			if (!partes[i].equals("") && !partes[i].equals("\" ") && !partes[i].equals("\"") && !partes[i].equals(" ")) {
				sb.append(((sbTempo.toString().length()) / LETRAS_SEC + segundos) / 60 + " min e " + ((sbTempo.toString().length() / LETRAS_SEC) + segundos) % 60 + " seg \n" + partes[i] + "\n\n\n");
				sbTempo.append(partes[i] + "\n");
			}
		}
		textoPronto = sb.toString().replaceAll("  ", " ");
		while (!textoPronto.equals(sb.toString().replaceAll("  ", " "))) {
			textoPronto = textoPronto.replaceAll("  ", " ");
		}
		textoPronto = textoPronto.replaceAll("\t", "");

		return textoPronto;
	}

	
	public String preparaTexto(String conteudo) {
		Pattern pat = Pattern.compile("<!\\-\\-.*?\\-\\->",Pattern.DOTALL);
		Matcher mat = pat.matcher(conteudo);
		conteudo=mat.replaceAll("");
		Pattern patS = Pattern.compile("<script.*?</script>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher matS = patS.matcher(conteudo);
		conteudo=matS.replaceAll("");
		Pattern patStyle = Pattern.compile("<style.*?</style>",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher matStyle = patStyle.matcher(conteudo);
		conteudo=matStyle.replaceAll("");
		return conteudo;
	}
	
	private String htmlRender(String or){
		//or = or.replaceAll("\\n\\s*\\n*","");
		or = or.replaceAll("\r","");
		or = or.replaceAll("\n","");
		//or = or.replace("\n","");
		Source source = new Source(or);
		return source.getRenderer().toString();
	}

	public int contadorDeLetrasValidas(int origem, int destino, String conteudo) {
		if (destino == -1) {
			return 0;
		}
		if (origem == -1) {
			//System.out.println("contadorDeLetrasValidas:erro: origem -1");
			return 0;
		}

		// tratando o texto
		int posA = 0;
		StringBuilder sb = new StringBuilder();
		int iniTexto = 0;
		int fimTexto = origem;
		// tratando o texto
		String parte = conteudo.substring(origem, destino);

		posA = parte.indexOf("<a ", posA);
		int quantA = 0;

		while (posA != -1) {
			posA = parte.indexOf("<a ", posA + 1);
			// System.out.println("quantA" + quantA);
			quantA++;
		}
		fimTexto = origem;
		int posImgAbreTag = 0;
		int posImgFechaTag = 0;
		int posInputAbreTag = 0;
		int posInputFechaTag = 0;
		int cont = 0;
		while ((parte.indexOf(">", fimTexto) != -1) || (parte.indexOf("<img", posImgFechaTag) != -1) || (parte.indexOf("<input", posInputFechaTag) != -1)) {

			posImgAbreTag = parte.indexOf("<img", posImgFechaTag);
			posInputAbreTag = parte.indexOf("<input", posInputFechaTag);

			if (cont == 0 && parte.charAt(0) != '<') {
				int fechaTag = parte.indexOf(">", origem);
				int abreTag = parte.indexOf("<", origem);

				if (fechaTag > abreTag && abreTag != -1) {
					iniTexto = origem;
				} else if (abreTag > fechaTag && fechaTag != -1) {
					iniTexto = parte.indexOf(">", fimTexto);
				} else if (abreTag == -1 || fechaTag == -1) {
					iniTexto = parte.indexOf(">", fimTexto);
				}
			} else {// */
				iniTexto = parte.indexOf(">", fimTexto);
			}
			cont++;
			if (iniTexto == -1) {
				iniTexto = 99999;
			}
			if (posImgAbreTag == -1) {
				posImgAbreTag = 99999;
			}
			if (posInputAbreTag == -1) {
				posInputAbreTag = 99999;
			}

			if (posImgAbreTag > iniTexto && posInputAbreTag > iniTexto) {

				fimTexto = parte.indexOf("<", iniTexto);

				if (iniTexto == -1 || fimTexto == -1) {
					break;
				}
				String texto = parte.substring(iniTexto + 1, fimTexto);
				if (!texto.equals("") && !texto.equals("\n")) {
					Source source = new Source(parte.substring(iniTexto + 1, fimTexto));
					String renderedText = source.getRenderer().toString();
					sb.append(renderedText);
					sb.append(" ");
				}

			} else if (posImgAbreTag < iniTexto && posImgAbreTag < posInputAbreTag) {

				posImgFechaTag = parte.indexOf(">", posImgAbreTag);

				String conteudoAlt = getLetrasEmAlt(posImgAbreTag, posImgFechaTag, parte);
				if (!conteudoAlt.equals("") && !conteudoAlt.equals("\n")) {
					Source source = new Source(conteudoAlt + "\n");
					String renderedText = source.getRenderer().toString();
					sb.append(renderedText);
					sb.append(" ");
				}
			} else if (posInputAbreTag < iniTexto && posInputAbreTag < posImgAbreTag) {
				posInputFechaTag = parte.indexOf(">", posInputAbreTag);
				String conteudoValue = getLetrasEmValue(posInputAbreTag, posInputFechaTag, parte);
				if (!conteudoValue.equals("") && !conteudoValue.equals("\n")) {

					Source source = new Source(conteudoValue + "\n");
					String renderedText = source.getRenderer().toString();
					sb.append(renderedText);
					sb.append(" ");
				}
			}

		}

		return sb.toString().length() + quantA * LETRAS_SEC;

	}

	private String getLetrasEmAlt(int abreTag, int fechaTag, String conteudo) {
		return G_pega_LetrasDeAlt(abreTag, fechaTag, conteudo);
	}

	private String G_pega_LetrasDeAlt(int abreTag, int fechaTag, String conteudo) {
		StringBuilder sb = new StringBuilder();
		String tagInteira;
		String conteudoSrc = "";
		String conteudoAlt = "";
		int posSrc;
		int posAlt;
		int iniTexto;
		int fimTexto = 0;

		tagInteira = conteudo.substring(abreTag, fechaTag);
		posAlt = tagInteira.indexOf("alt=");

		if (posAlt != -1) {
			// System.out.println("bloco 1");
			iniTexto = "alt=".length() + posAlt;
			if (tagInteira.charAt(iniTexto) == '\"') {
				fimTexto = tagInteira.indexOf("\"", iniTexto + 1);
			} else if (tagInteira.charAt(iniTexto) == '\'') {
				fimTexto = tagInteira.indexOf("\'", iniTexto + 1);
			} else {
				int posFechaTag = tagInteira.indexOf(">", iniTexto + 1);
				int posProxEqual = tagInteira.indexOf("=", iniTexto + 1);

				if (posFechaTag == -1) {
					posFechaTag = 9999;
				}
				if (posProxEqual == -1) {
					posProxEqual = 9999;
				}
				if (posFechaTag > posProxEqual) {

					for (fimTexto = posProxEqual; tagInteira.charAt(fimTexto) != ' '; fimTexto--) {
					}
					conteudoAlt = tagInteira.substring(iniTexto + 1, fimTexto);

				} else {
					fimTexto = posFechaTag - 1;
					conteudoAlt = tagInteira.substring(iniTexto + 1, fimTexto);

				}

			}

			if (iniTexto != -1 && fimTexto != -1) {

				conteudoAlt = tagInteira.substring(iniTexto + 1, fimTexto);

				if (!conteudoAlt.equals("")) {
					sb.append("[");
					sb.append(conteudoAlt);
					sb.append("]");
				}
			}

		}

		{
			posSrc = tagInteira.indexOf("src=");

			if (posSrc != -1 && (posAlt == -1 || conteudoAlt.equals(""))) {
				// System.out.println("bloco 1");

				iniTexto = "src=".length() + posSrc;
				if (tagInteira.charAt(iniTexto) == '\"') {
					fimTexto = tagInteira.indexOf("\"", iniTexto + 1);
				} else if (tagInteira.charAt(iniTexto) == '\'') {
					fimTexto = tagInteira.indexOf("\'", iniTexto + 1);
				} else {
					int posFechaTag = tagInteira.indexOf(">", iniTexto + 1);
					int posProxEqual = tagInteira.indexOf("=", iniTexto + 1);

					if (posFechaTag == -1) {
						posFechaTag = 9999;
					}
					if (posProxEqual == -1) {
						posProxEqual = 9999;
					}
					if (posFechaTag > posProxEqual) {

						for (fimTexto = posProxEqual; tagInteira.charAt(fimTexto) != ' '; fimTexto--) {
						}
						// fimTexto = tagInteira.indexOf("\"",
						// "src=".length() + posSrc + 1)-1;
						conteudoSrc = tagInteira.substring(iniTexto, fimTexto);

					} else {
						fimTexto = posFechaTag - 1;
					}

				}

				if (iniTexto != -1 && fimTexto != -1) {
					// System.out.println("bloco 2");

					conteudoSrc = tagInteira.substring(iniTexto, fimTexto);
					// System.out.println("src:"+conteudoSrc);
					// System.out.println("src:"+conteudoSrc);
					sb.append("[");
					sb.append(conteudoSrc.substring(conteudoSrc.lastIndexOf("/") + 1, conteudoSrc.length()));
					sb.append("]");

				}
				// System.out.println("total em alt: " + sb.length());

			}
		}

		return sb.toString();
	}

	private String getLetrasEmValue(int abreTag, int fechaTag, String conteudo) {
		return G_pega_letrasEmValue(abreTag, fechaTag, conteudo);
	}

	private String G_pega_letrasEmValue(int abreTag, int fechaTag, String conteudo) {
		if (abreTag < 0 || fechaTag < 0) {
			return "";
		}
		// System.out.println(abreTag + " " + fechaTag);
		String tagInteira = conteudo.substring(abreTag, fechaTag);
		if (tagInteira.indexOf("text") != -1 || tagInteira.indexOf("buttom") != -1 || tagInteira.indexOf("submit") != -1 || tagInteira.indexOf("reset") != -1) {
			StringBuilder sb = new StringBuilder();
			int posValue;
			int iniTexto;
			int fimTexto = 0;

			// System.out.println("Tag em value: "
			// + conteudo.substring(abreTag, fechaTag));
			posValue = conteudo.substring(abreTag, fechaTag).indexOf("value=");

			iniTexto = "value=".length() + posValue;
			if (tagInteira.charAt(iniTexto) == '\"') {
				fimTexto = tagInteira.indexOf("\"", iniTexto + 1);
				iniTexto++;
			} else if (tagInteira.charAt(iniTexto) == '\'') {
				fimTexto = tagInteira.indexOf("\'", iniTexto + 1);
				iniTexto++;
			} else {
				int posFechaTag = tagInteira.indexOf(">", iniTexto + 1);
				int posProxEqual = tagInteira.indexOf("=", iniTexto + 1);

				if (posFechaTag == -1) {
					posFechaTag = 9999;
				}
				if (posProxEqual == -1) {
					posProxEqual = 9999;
				}
				if (posFechaTag > posProxEqual) {

					for (fimTexto = posProxEqual; tagInteira.charAt(fimTexto) != ' '; fimTexto--) {
					}

					// fimTexto = tagInteira.indexOf("\"", "value=".length()
					// + posValue + 1)-1;

					//String strValue = tagInteira.substring(iniTexto, fimTexto);
					//System.out.println("value:" + strValue);
				} else {
					fimTexto = posFechaTag - 2;
				}

			}

			// iniTexto = conteudo.substring(abreTag,
			// fechaTag).indexOf("\"", posValue);
			// fimTexto = conteudo.substring(abreTag,
			// fechaTag).indexOf("\"", "value=".length() + posValue + 1);
			if (iniTexto != -1 && fimTexto != -1) {

				String strValue = tagInteira.substring(iniTexto, fimTexto);
				// System.out.println("value:"+strValue);
				sb.append(strValue);

			}
			//System.out.println("total em value: " + sb.length());

			return "[" + sb.toString() + "]";
		} else {
			return "";
		}
	}

	public class Edge {
		public int peso;

		int posOrigem;

		int posDestino;

		int posSaida;

		Vertex origem;

		Vertex destino;

		public Edge(Vertex origem, Vertex destino, int custo) {
			this.origem = origem;
			this.destino = destino;
			this.peso = custo;
		}

		public Vertex getOrigem() {
			return origem;
		}

		public Vertex getDestino() {
			return destino;
		}
	}

	public class Vertex {
		String textoLink;
		
		int idVertice = 0;

		int tipoVertice = 0;

		Edge edgeLigaPai;

		int custo;

		String nomeVertice;

		int inicioRegiao;

		String textoSemTag = new String();

		String conteudo;

		ArrayList<Integer> saidas = new ArrayList<Integer>();

		private int indexOf;

		ArrayList<Edge> edges = new ArrayList<Edge>();

		public boolean compareTo(Vertex vertice) {
			if (idVertice == vertice.idVertice)
				if (tipoVertice == vertice.tipoVertice)
					if (custo == vertice.custo)
						if (nomeVertice.equals(vertice.nomeVertice))
							if (inicioRegiao == vertice.inicioRegiao)
								if (textoSemTag.equals(vertice.textoSemTag))
									if (((String) conteudo).equals(((String) vertice.conteudo))) {
										return true;
									}

			return false;
		}

		/**
		 * Guarda o indice dentro do texto com o tempo
		 * @param i posição dentro do texto do painel
		 */
		public void setIndexOf(int i) {
			this.indexOf = i;
		}
		
		/**
		 * @return o indice dentro do texto com o tempo
		 */
		public int getIndexOf(){
			return this.indexOf;
		}

		public Vertex() {
		}

		public Vertex(String conteudo) {
			this.conteudo = conteudo;
		}

		public void insertEdge(Edge edge) {
			edges.add(edge);

		}

		public void removeEdge(Edge edge) {
			edges.remove(edge);

		}

		public ArrayList<Edge> getEdges() {
			return edges;
		}

		public ArrayList<Integer> getSaidas() {
			return saidas;
		}

		public int getIdVertice() {
			return idVertice;
		}

		public String getConteudo() {
			return conteudo;
		}

		public void setConteudo(String conteudo) {
			this.conteudo = conteudo;
		}

		public String getNomeVertice() {
			return nomeVertice;
		}

		public void setNomeVertice(String nomeVertice) {
			this.nomeVertice = nomeVertice;
		}

		public String getTextoSemTag() {
			return textoSemTag;
		}

		public void setTextoSemTag(String textoSemTag) {
			this.textoSemTag = textoSemTag;
		}

		public int getCusto() {
			return custo;
		}

		public void setCusto(int custo) {
			this.custo = custo;
		}

		public int getTipoVertice() {
			return tipoVertice;
		}

		public void setTipoVertice(int tipoVertice) {
			this.tipoVertice = tipoVertice;
		}
	}

	public String getConteudoPagina() {
		return conteudoPagina;
	}

	public void setConteudoPagina(String conteudoPagina) {
		this.conteudoPagina = conteudoPagina;
	}

	public String getTextoParaPainel() {
		return textoParaPainel;
	}

	public ArrayList<Resultado> getArrResultado() {
		return arrResultado;
	}
	public class Resultado {
		private Vertex origem;
		private Vertex destino;
		private String ini_pagina;
		private String conteudo;
		public Resultado(String ini_pagina2, String conteudo,Vertex origem,Vertex destino) {
			this.ini_pagina = ini_pagina2;
			this.conteudo = conteudo;
			this.origem = origem;
			this.destino = destino;
		}
		public Vertex getDestino(){
			return this.destino; 
		}
		public Vertex getOrigem(){
			return this.origem;
		}
		public String getIni_pagina() {
			return ini_pagina;
		}
		public void setIni_pagina(String ini_pagina) {
			this.ini_pagina = ini_pagina;
		}
		public String getConteudo() {
			return conteudo;
		}
		public void setConteudo(String conteudo) {
			this.conteudo = conteudo;
		}
	}
}
