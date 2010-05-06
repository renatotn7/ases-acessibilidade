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

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.preenchedor_formulario.PanelPreenchedorFormulario;
import br.org.acessobrasil.ases.nucleo.InterfClienteDoNucleo;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
import br.org.acessobrasil.ases.nucleo.NucleoEstruturado;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.silvinha.util.AlteradorDeCsv;
/**
 * Faz as operações para corrigir o código HTML
 * colocando conteúdo em campos de formulário
 * @author Fabio Issamu Oshiro, Renato Tomaz Nati
 */
public class ControlePreenchedorFormulario implements InterfClienteDoNucleo{
	AlteradorDeCsv alteradorDeCsv;

	private boolean salva = false;

	ArrayList<ArmazenaErroOuAviso> arrErros;
	ArrayList<Integer> arrErroIni;
	private String codigo;
	InterfNucleos nucleo = new NucleoEstruturado();
	private PanelPreenchedorFormulario painel;
	
	public void avalia(String conteudo, boolean salva) {
		
		if (conteudo == null)
			return;
		this.salva = salva;
		alteradorDeCsv = new AlteradorDeCsv(EstadoSilvinha.hashCodeAtual);
		nucleo = (NucleoEstruturado) MethodFactNucleos.mFNucleos("Estruturado");
		nucleo.setCodHTML(conteudo);
		nucleo.setWCAGEMAG(InterfNucleos.WCAG);
		nucleo.addCliente(this);
		nucleo.avalia();
		codigo = nucleo.getCodHTMLOriginal();

	}
public void avalia(PanelPreenchedorFormulario painel,String conteudo, boolean salva) {
		
	this.painel = painel;
	if (conteudo == null)
		return;
	
	this.salva = salva;
	if(EstadoSilvinha.conteudoEmPainelResumo){
	alteradorDeCsv = new AlteradorDeCsv(EstadoSilvinha.hashCodeAtual);
	}
	nucleo = new NucleoEstruturado();
	nucleo.setCodHTML(conteudo);
	if (EstadoSilvinha.orgao == 2) {
		
		nucleo.setWCAGEMAG(InterfNucleos.EMAG);
	} else {
	
		nucleo.setWCAGEMAG(InterfNucleos.WCAG);
	}
	nucleo.addCliente(this);
	nucleo.avalia();
	this.painel.codigo = nucleo.getCodHTMLOriginal();

	}

	
	public void avalia(String codigo){
		arrErros = new ArrayList<ArmazenaErroOuAviso>();
		arrErroIni = new ArrayList<Integer>();
		
		
		this.codigo=codigo;
		nucleo = new NucleoEstruturado();
		nucleo.addCliente(this);
		nucleo.setCodHTML(codigo);
		nucleo.setWCAGEMAG(NucleoEstruturado.EMAG);
		nucleo.avalia();
	}




	public void avisosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv
					.getAvisoP1(EstadoSilvinha.hashCodeAtual);

			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso
					.size());

			alteradorDeCsv.setAvisoP1(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void avisosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv
					.getAvisoP2(EstadoSilvinha.hashCodeAtual);

			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso
					.size());

			alteradorDeCsv.setAvisoP2(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void avisosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv
					.getAvisoP3(EstadoSilvinha.hashCodeAtual);

			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso
					.size());

			alteradorDeCsv.setAvisoP3(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void errosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo){
			String valor = alteradorDeCsv
					.getErroP1(EstadoSilvinha.hashCodeAtual);
			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso
					.size());

			alteradorDeCsv.setErroP1(EstadoSilvinha.hashCodeAtual, valor);
		}
	
	}
	public void errosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		//2.14
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv
					.getErroP2(EstadoSilvinha.hashCodeAtual);
			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso
					.size());

			alteradorDeCsv.setErroP2(EstadoSilvinha.hashCodeAtual, valor);
		}
		for(ArmazenaErroOuAviso erro:armazenaErroOuAviso){
			if(erro.getIdTextoRegra().equals("2.14")){
				arrErros.add(erro);
				arrErroIni.add(getErroIni(erro));
			}
		}
	}

	public void errosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv
					.getErroP3(EstadoSilvinha.hashCodeAtual);
			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso
					.size());

			alteradorDeCsv.setErroP3(EstadoSilvinha.hashCodeAtual, valor);
		}
	}
	public void fimDaAvaliacao() {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			alteradorDeCsv.escreveEmDisco();
			}
			this.salva = false;
		
	}

	public void linksAchadosPeloNucleo(HashSet<String> links) {
		// TODO Auto-generated method stub
		
	}
	public int length() {
		return arrErros.size();
	}
	
	public int getColuna(int i) {
		return arrErros.get(i).getColuna();
	}
	
	public String getTag(int i) {
		return arrErros.get(i).getTagCompleta();
	}
	public int getLinha(int i) {
		return arrErros.get(i).getLinha();
	}
	
	public int getIniIndex(int i){
		return Integer.parseInt(arrErroIni.get(i).toString());
	}

	private int getErroIni(ArmazenaErroOuAviso erro){
		int linha = erro.getLinha();
		int contaLinha = 1, ini = 0;
		while (contaLinha < linha) {
			ini = this.codigo.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return -1;
			}
		}
		ini+=erro.getColuna();
		if (ini + 1 >= this.codigo.length())
			return -1;
		return ini;
	}

	public String corrige(String text, int erroAtual) {
		
		String newTag,tag = arrErros.get(erroAtual).getTagCompleta();
		if(tag.toLowerCase().startsWith("<textarea")){
			text = text.replace("<","&lt;");
			newTag=tag+text;
		}else{
			newTag=corrigeTagInput(tag,text);
		}
		this.codigo=this.codigo.replace(tag,newTag);
		return this.codigo;
	}
	private String corrigeTagInput(String tag, String valor){
		valor = valor.replace("\"","&quot;");
		valor = valor.replace("\n","");
		String newTag;
		Pattern patVal = Pattern.compile("value=(([\"']).*?\\2|\\w\\w*?)");
		Matcher matVal = patVal.matcher(tag);
		if(matVal.find()){
			newTag=matVal.replaceAll("value=\"" + valor + "\"");
		}else{
			newTag=tag.substring(0,tag.length()-1);
			if(newTag.endsWith("/")){
				newTag=newTag.substring(0,newTag.length()-1);
				newTag+=" value=\""+valor + "\" />";
			}else{
				newTag+=" value=\""+valor + "\" >";
			}
		}
		return newTag;
	}
	
}
