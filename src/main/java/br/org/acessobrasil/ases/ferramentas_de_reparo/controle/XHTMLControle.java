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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.org.acessobrasil.silvinha2.mli.XHTML_Traduz;
/**
 * Faz a avaliação do código HTML de acordo com as normas do W3C
 * @author Fabio Issamu Oshiro, Haroldo Veiga, Renato Tomaz Nati
 */
public class XHTMLControle {
	private static Logger logger = Logger.getLogger(XHTMLControle.class);
	ArrayList<Integer> linha = new ArrayList<Integer>();

	ArrayList<Integer> coluna = new ArrayList<Integer>();

	ArrayList<String> mensagem = new ArrayList<String>();
	private static final String tagsEmpty[] = { "BR", "AREA", "LINK", "IMG", "PARAM", "HR", "INPUT", "COL", "BASE", "META" };
	
	private String dtd = ""; 
	public static String XHTML_01Strict="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"dtd/xhtml1-strict.dtd\">";
	public static String HTML4_01Strict="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Strict//EN\" \"dtd/html4.01strict.dtd\">";
	public static String HTML4_01Transitional="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"dtd/html4.01transitional.dtd\">";
	public static String HTML4_01Frameset="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML Frameset//EN\" \"dtd/html4.01frameset.dtd\">";
	/**
	 * Avalia um arquivo
	 * @param x
	 * @deprecated
	 */
	public void avalia(File x) {
		try {
			if (x == null)
				return;
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setValidating(true); // Default is false

			DocumentBuilder b = f.newDocumentBuilder();
			ErrorHandler h = new XHTML_Traduz(this);
			b.setErrorHandler(h);
			b.parse(x);
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
	}

	/**
	 * Avalia um código 
	 * @param codHTML
	 */
	public void avalia(String codHTML){
		if(codHTML==null){
			codHTML = "";
		}
		int xhtml = codHTML.trim().indexOf("-//W3C//DTD XHTML");
		int html4Strict=codHTML.trim().indexOf("\"-//W3C//DTD HTML 4.01 Strict//EN\"");
		int html4Strict2=codHTML.trim().indexOf("\"-//W3C//DTD HTML 4.01//EN\"");		
		int html4Transitional=codHTML.trim().indexOf("\"-//W3C//DTD HTML 4.01 Transitional//EN\"");
		int html4_0Transitional=codHTML.trim().indexOf("\"-//W3C//DTD HTML 4.0 Transitional//EN\"");
		
		int html4Frameset=codHTML.trim().indexOf("\"-//W3C//DTD HTML 4.01 Frameset//EN\"");
		if((html4Strict!=-1 && html4Strict<100) ||(html4Strict2!=-1 && html4Strict2<100)){
			codHTML=tagsToUpperCase(codHTML);
			codHTML=fechaTags(codHTML);
			codHTML=fechaLI(codHTML);
			codHTML=fechaP(codHTML);
			
			//Redirecionar para o DTD local
			codHTML=replaceDTD(codHTML,HTML4_01Strict);
			//System.out.print("HTML4_01Strict\n");
			dtd=HTML4_01Strict;
		}else if(html4Transitional!=-1 && html4Transitional<100){
			codHTML=tagsToUpperCase(codHTML);
			codHTML=fechaTags(codHTML);
			codHTML=fechaLI(codHTML);
			codHTML=fechaP(codHTML);
			//Redirecionar para o DTD local
			codHTML=replaceDTD(codHTML,HTML4_01Transitional);
			//System.out.print("HTML4_01Transitional\n");
			dtd=HTML4_01Transitional;
		}else if(html4_0Transitional!=-1 && html4_0Transitional<100){
			codHTML=tagsToUpperCase(codHTML);
			codHTML=fechaTags(codHTML);
			codHTML=fechaLI(codHTML);
			codHTML=fechaP(codHTML);
			//Redirecionar para o DTD local
			codHTML=replaceDTD(codHTML,HTML4_01Transitional);
			//System.out.print("HTML4_01Transitional\n");
			dtd=HTML4_01Transitional;
		}else if(html4Frameset!=-1 && html4Frameset<100){
			codHTML=tagsToUpperCase(codHTML);
			codHTML=fechaTags(codHTML);
			codHTML=fechaLI(codHTML);
			codHTML=fechaP(codHTML);
			//Redirecionar para o DTD local
			codHTML=replaceDTD(codHTML,HTML4_01Frameset);
			//System.out.print("HTML4_01Frameset\n");
			dtd=HTML4_01Frameset;
		}else if(xhtml!=-1 && xhtml<100){
			/*
			 * Algum xhtml
			 * Não necessita de tratamento
			 */
			//Redirecionar para o DTD local
			codHTML=replaceDTD(codHTML,XHTML_01Strict);
			dtd=XHTML_01Strict;
		}else{
			codHTML=tagsToUpperCase(codHTML);
			codHTML=fechaTags(codHTML);
			codHTML=fechaLI(codHTML);
			codHTML=fechaP(codHTML);
			//Redirecionar para o DTD local
			
			
			codHTML = HTML4_01Transitional + codHTML;
			//System.out.print("default HTML4_01Transitional\n");
			dtd=HTML4_01Transitional;
		}
		//System.out.println("DTD utilizado = " + dtd);
		//Colocar num input stream para entregar ao DOM
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(codHTML));
		
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		f.setValidating(true); // Default is false

		DocumentBuilder b;
		try {
			b = f.newDocumentBuilder();
			ErrorHandler h = new XHTML_Traduz(this);
			b.setErrorHandler(h);
			b.parse(inStream);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	/**
	 * Procura pela declaração do DTD e substitui
	 * @param codHTML
	 * @param strDTD
	 * @return
	 */
	private String replaceDTD(String codHTML, String strDTD) {
		Pattern pattern = Pattern.compile("<\\!DOCTYPE.*?>",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(codHTML);
		if(matcher.find()){
			codHTML=matcher.replaceFirst(strDTD);
		}
		return codHTML;
	}
	private String fechaP(String codHTML) {
		return fechaP(codHTML, false);
	}
	private String fechaP(String codHTML,boolean toLower) {
		//nao casa <PARAM
		Pattern pat = Pattern.compile("<P>|<P\\s.*?>|</P>|</P\\s.*?>", Pattern.DOTALL  | Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(codHTML);
		String closeTag = toLower?"</p>" : "</P>";
		//int p = 0;
		//int aberto = 0;
		while (mat.find()) {
			//System.out.print(mat.group());
			//System.out.print("\n");
			String tag = mat.group();
			int ini = mat.start();
			//int fim = mat.end();
			if (tag.toUpperCase().startsWith("<P")) {
				/*
				 * Achar o pai deste P
				 */
				String pai = cataPai(codHTML.substring(0, ini));
				logger.debug("pai=" + pai + "\n");
				/*
				 * Se uma entidade Block for aberta, deve-se fechar a tag P.
				 */
				int nextStartBlock=nextBlock(codHTML, ini+2);
				if(nextStartBlock>-1){
					logger.debug("nextStartBlock=" + nextStartBlock + "\n");
					logger.debug("nextStartBlock=" + codHTML.substring(nextStartBlock,nextStartBlock+10) + "\n");
				}
				/*
				 * Se a tag pai for fechada, deve-se fechar a tag P também.
				 */
				int posPaiFecha=closeTag(codHTML, pai, ini+2);
				logger.debug("posPaiFecha=" + posPaiFecha + "\n");
				if(posPaiFecha!=-1)
					logger.debug("posPaiFecha=" + codHTML.substring(posPaiFecha,posPaiFecha+10) + "\n");
				/*
				 * Achar o próximo fecha p
				 */
				int posFechaP = closeTag(codHTML,"<P>",ini+2);
				// P|H1|H2|H3|H4|H5|H6|UL|OL|PRE|DL|DIV|NOSCRIPT|BLOCKQUOTE|FORM|HR|TABLE|FIELDSET|ADDRESS
				logger.debug("posFechaP=" + posFechaP + "\n");
				/*
				 * Ver qual é menor e diferente de -1 
				 */
				if(posFechaP!=-1 && (posFechaP<nextStartBlock || nextStartBlock==-1) && (posFechaP<posPaiFecha || posPaiFecha==-1)){
					/*
					 * Fazer nada, a tag está fechadinha
					 * pois o posFechaP e menor que o proximo bloco e menor que a posicao de fecha pai
					 */
				}else if(nextStartBlock!=-1 && nextStartBlock<posPaiFecha){
					/*
					 * um bloco começa antes que do pai fechar
					 */
					codHTML = codHTML.substring(0, nextStartBlock) + closeTag + codHTML.substring(nextStartBlock);
					mat = pat.matcher(codHTML);
				}else{
					/*
					 * o pai fecha antes de um bloco começar
					 */
					codHTML = codHTML.substring(0, posPaiFecha) + closeTag + codHTML.substring(posPaiFecha);
					mat = pat.matcher(codHTML);
				}
			}
		}
		return codHTML;
	}
	/**
	 * Retorna a posição do próximo block
	 * 
	 * @param codHTML
	 * @param aPartirDe
	 *            a partir da posição
	 * @return -1 caso não tenha ou o número/indice da posição
	 */
	private int nextBlock(String codHTML, int aPartirDe) {
		// P|H1|H2|H3|H4|H5|H6|UL|OL|PRE|DL|DIV|NOSCRIPT|BLOCKQUOTE|FORM|HR|TABLE|FIELDSET|ADDRESS
		String strPat = "";
		/*
		 * //Gerador de pattern String paraProcurar =
		 * codHTML.substring(aPartirDe); String arrPat[] =
		 * "P|H1|H2|H3|H4|H5|H6|UL|OL|PRE|DL|DIV|NOSCRIPT|BLOCKQUOTE|FORM|HR|TABLE|FIELDSET|ADDRESS".split("\\|");
		 * 
		 * for(int i=0;i<arrPat.length;i++){ if(i>0){ strPat+="|"; } strPat+="<"+arrPat[i]+">|<"+arrPat[i]+"\\\\s.*?>"; }
		 * System.out.print("strPat="+strPat+"\n");
		 */
		strPat = "<P>|<P\\s.*?>|<H1>|<H1\\s.*?>|<H2>|<H2\\s.*?>|<H3>|<H3\\s.*?>|<H4>|<H4\\s.*?>|<H5>|<H5\\s.*?>|<H6>|<H6\\s.*?>|<UL>|<UL\\s.*?>|<OL>|<OL\\s.*?>|<PRE>|<PRE\\s.*?>|<DL>|<DL\\s.*?>|<DIV>|<DIV\\s.*?>|<NOSCRIPT>|<NOSCRIPT\\s.*?>|<BLOCKQUOTE>|<BLOCKQUOTE\\s.*?>|<FORM>|<FORM\\s.*?>|<HR>|<HR\\s.*?>|<TABLE>|<TABLE\\s.*?>|<FIELDSET>|<FIELDSET\\s.*?>|<ADDRESS>|<ADDRESS\\s.*?>";
		Pattern pat = Pattern.compile(strPat,Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(codHTML.substring(aPartirDe).toUpperCase());
		if(mat.find()){
			return mat.start()+aPartirDe;
		}else{
			return -1;
		}
	}

	/**
	 * Retorna a posição em que esta tag é fechada
	 * 
	 * @param codHTML
	 * @param tag
	 * @param aPartirDe
	 * @return
	 */
	private int closeTag(String codHTML, String tag, int aPartirDe) {
		String nome=(tag.split("\\s"))[0].substring(1);
		nome=nome.replace(">","");
		//"nome"
		//System.out.print("nome="+nome+"\n");
		Pattern pat = Pattern.compile("<"+nome+">|<"+nome+"\\s.*?>|</"+nome+">|</"+nome+"\\s.*?>",Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(codHTML.substring(aPartirDe));
		int aberto=1;
		while(mat.find()){
			String aTag=mat.group();
			
			if(aTag.toUpperCase().startsWith("<"+nome.toUpperCase())){
				aberto++;
				//System.out.print("aTag++="+aTag+"\n");
			}else if(aTag.toUpperCase().startsWith("</"+nome.toUpperCase())){
				aberto--;
				//System.out.print("aTag--="+aTag+"\n");
			}
			if(aberto==0){
				//System.out.print("mat.start()="+mat.start()+"\n");
				//System.out.print("aPartirDe="+aPartirDe+"\n");
				return mat.start()+aPartirDe;
			}
		}
		return -1;
	}
	/**
	 * Transforma todo o conteudo das tags para upper case
	 * @param codHTML
	 * @return
	 */
	private String tagsToUpperCase(String codHTML){
		Pattern pat = Pattern.compile("<.*?>",Pattern.DOTALL);
		Matcher mat = pat.matcher(codHTML);
		while(mat.find()){
			codHTML = codHTML.replace(mat.group(),mat.group().toUpperCase());
		}
		codHTML = codHTML.replace("&AMP;","&amp;");
		return codHTML;
	}
	/**
	 * Retorna o pai da tag
	 * 
	 * @param codHTML
	 * @return
	 */
	private String cataPai(String codHTML) {
		/*
		 * Retorna a última tag que ficou aberta
		 */
		ArrayList<String> aberta = new ArrayList<String>();
		Pattern pat = Pattern.compile("<[a-zA-Z].*?>|</[a-zA-Z].*?>",Pattern.DOTALL);
		Matcher mat = pat.matcher(codHTML);
		while (mat.find()) {
			String tag = mat.group();
			if (tag.startsWith("</")) {
				/*
				 * Achar o com índice maior desta tag
				 */
	
				String tagAbre = "<" + tag.substring(2);
				// desempilhar
				// System.out.print("tagAbre="+tagAbre+"\n");
				int de = aberta.lastIndexOf(tagAbre);
				if (de != -1) {
					for (int i = aberta.size() - 1; i >= de; i--) {
						aberta.remove(i);
					}
				}
			} else {
				aberta.add(tag);
			}
		}
		if (aberta.size() > 0) {
			return aberta.get(aberta.size() - 1);
		} else {
			return "";
		}
	}
	/**
	 * Fecha os códigos das tags LI
	 * 
	 * @param codHTML
	 * @return código com os LIs fechados
	 */
	private String fechaLI(String codHTML){
		return fechaLI(codHTML,false);
	}
	/**
	 * Fecha os códigos das tags LI
	 * 
	 * @param codHTML
	 * @return código com os LIs fechados
	 */
	private String fechaLI(String codHTML,boolean toLower) {
		Pattern pat = Pattern.compile("(<[/]?LI>|<[/]?OL>|<[/]?UL>|<[/]?LI\\s.*?>|<[/]?OL\\s.*?>|<[/]?UL\\s.*?>)",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(codHTML);
		
		String closeTag;
		if(toLower){
			closeTag = "</li>";
		}else{
			closeTag = "</LI>";
		}
		int li = 0;
		int ol = -1;
		int aberto=0;
		while (mat.find()) {
			//System.out.print(mat.group());
			//System.out.print("\n");
			String tag = mat.group();
			int ini = mat.start();
			//int fim = mat.end();
			if (tag.toUpperCase().startsWith("<LI")) {
				li++;
				aberto=li-ol;
				if (aberto == 2) {
					// fechar o outro li==1
					
					codHTML = codHTML.substring(0, ini) + closeTag + codHTML.substring(ini);
					mat = pat.matcher(codHTML);
					li = 0;
					ol = -1;
					aberto=0;
				}
			} else if (tag.toUpperCase().startsWith("</LI")) {
				li--;
			} else if (tag.toUpperCase().startsWith("<OL") || tag.toUpperCase().startsWith("<UL")) {
				ol++;
			} else if (tag.toUpperCase().startsWith("</OL") || tag.toUpperCase().startsWith("</UL")) {
				aberto=li-ol;
				ol--;
				if (aberto == 1) {
					codHTML = codHTML.substring(0, ini) + closeTag + codHTML.substring(ini);
					mat = pat.matcher(codHTML);
					li = 0;
					ol = -1;
					aberto=0;
				}
				
			}

		}
		//System.out.print("\n");
		//System.out.print("\n");
		return codHTML;
	}
	
	/**
	 * Fecha as tags de conteudo EMPTY 
	 * como br hr, etc.<br>
	 * @param codHtml
	 * @return codigo fechado como xhtml
	 */
	public String fecharTags(String codHtml){
		codHtml=fechaTags(codHtml,true);
		codHtml=fechaLI(codHtml,true);
		codHtml=fechaP(codHtml,true);
		
		return codHtml;
	}
	/**
	 * Fecha as tags de conteudo EMPTY 
	 * como br hr, etc.
	 * @param codHTML sem tags fechadas!
	 * @return código com tags fechadas
	 */
	private String fechaTags(String codHTML) {
		return fechaTags(codHTML,false);
	}
	/**
	 * Fecha as tags de conteudo EMPTY 
	 * como br hr, etc.
	 * @param codHTML sem tags fechadas!
	 * @return código com tags fechadas
	 */
	private String fechaTags(String codHTML,boolean toLower) {
		/*
		 * Fechar tags br, area, link, img, param, hr, input, col, base, meta
		 */
		for (int i = 0; i < tagsEmpty.length; i++) {
			Pattern pattern = Pattern.compile("(<"+tagsEmpty[i]+">|<"+tagsEmpty[i]+"\\s(.*?)>)",Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			int alteracao = 0;
			int tamIni = codHTML.length();
			Matcher matcher = pattern.matcher(codHTML);
			while(matcher.find()){
				String att = matcher.group(2);
				String tag = tagsEmpty[i];
				if(toLower) tag = tag.toLowerCase();
				if(att==null){
					codHTML=codHTML.substring(0,matcher.start()+alteracao)+"<"+tag+" />"+codHTML.substring(matcher.end()+alteracao);
				}else{
					if(att.endsWith("/")){
						att=att.substring(0,att.length()-1);
					}
					codHTML=codHTML.substring(0,matcher.start()+alteracao)+"<"+tag+" "+att+" />"+codHTML.substring(matcher.end()+alteracao);
				}
				alteracao = codHTML.length() - tamIni;
			}
		}
		/*
		 * Pattern pattern = Pattern.compile("(<BR>|<BR\\s(.*?)>)",Pattern.DOTALL);
		 * Matcher matcher = pattern.matcher(codHTML); if(matcher.find()){
		 * matcher.replaceAll("<BR/>"); }
		 */
		return codHTML;
		/*
		 * Transformar em InputSource
		 */
		//InputSource inStream = new InputSource();
		//inStream.setCharacterStream(new StringReader(codHTML));
		//return inStream;
	}

	/**
	 * Metodo chamado pelo ErrorHandler
	 * 
	 * @param lineNumber
	 * @param columnNumber
	 * @param strMensagem
	 */
	public void addErro(int lineNumber, int columnNumber, String strMensagem) {
		linha.add(lineNumber);
		coluna.add(columnNumber);
		mensagem.add(strMensagem);
	}

	/**
	 * Retorna a linha do índice
	 * 
	 * @param i
	 */
	public int getColuna(int i) {
		return coluna.get(i);
	}

	/**
	 * Retorna a linha do índice
	 * 
	 * @param i
	 */
	public int getLinha(int i) {
		return linha.get(i);
	}

	/**
	 * Retorna a mensagem do índice
	 * 
	 * @param i
	 * @return a mensagem do erro
	 */
	public String getMensagem(int i) {
		return mensagem.get(i);
	}

	/**
	 * Retorna o total de erros encontrados
	 * 
	 * @return total
	 */
	public int length() {
		return linha.size();
	}

	/**
	 * Retorna o dtd que foi identificado no documento html
	 * @return dtd
	 */
	public String getDtd() {
		return dtd;
	}
}
