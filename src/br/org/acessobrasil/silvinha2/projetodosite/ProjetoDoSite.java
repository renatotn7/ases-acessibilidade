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

package br.org.acessobrasil.silvinha2.projetodosite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
/**
 * Possui um relatório consolidado quando o usuário escolhe por avaliar "Site"
 * @author Fabio Issamu Oshiro
 *
 */
public class ProjetoDoSite {
	/**
	 * Guarda o Path do projeto
	 * Ex: C:/meuprojeto
	 */
	private static String localDoProjetoNoHD;
	private static String nomeDoProjeto;
	private HashSet<String> arr_imagemSemAlt =  new HashSet<String>();
	private HashMap<String,Integer> erroWcagP1 =  new HashMap<String,Integer>();
	private HashMap<String,Integer> erroWcagP2 =  new HashMap<String,Integer>();
	private HashMap<String,Integer> erroWcagP3 =  new HashMap<String,Integer>();
	private HashMap<String,Integer> erroEmagP1 =  new HashMap<String,Integer>();
	private HashMap<String,Integer> erroEmagP2 =  new HashMap<String,Integer>();
	private HashMap<String,Integer> erroEmagP3 =  new HashMap<String,Integer>();
	
	private HashSet<String> totalLinks = new HashSet<String>();
	
	private ArrayList<String> arrUrlParaAvaliar = new ArrayList<String>();
	private HashSet<String> arrUrlAvaliadas = new HashSet<String>();
	
	/**
	 * Guarda em qual indice está da avaliação.
	 * Relativo ao arrUrlParaAvaliar
	 */
	private int indiceAvaliado=0;
	
	public static void main(final String[] args) {
		Pagina pg = new Pagina();
		InterfNucleos nucleo = MethodFactNucleos.mFNucleos("Estruturado");
		pg.setNucleo(nucleo);
		pg.avaliaPagina();
	}
	
	/**
	 * Adiciona uma imagem única sem alt
	 * @param imagemSemAlt Caminho inteiro da imagem
	 */
	public void addImagemSemAlt(String imagemSemAlt){
		if (!arr_imagemSemAlt.contains(imagemSemAlt)){
			arr_imagemSemAlt.add(imagemSemAlt);
		}
	}

	/**
	 * Adiciona o total de erro de uma página
	 * 
	 * @param numero_total_de_erro Passe o número total de erro esta página para este ponto de verificação
	 * @param url_ou_path Informe a url ou path do arquivo
	 */
	public void addErroWcagP1(String url_ou_path,int numero_total_de_erro){
		//tratamento de erro dos valores
		addErro(erroWcagP1,url_ou_path,numero_total_de_erro);
	}
	
	/**
	 * Adiciona o total de erro de uma página
	 * 
	 * @param numero_total_de_erro Passe o número total de erro esta página para este ponto de verificação
	 * @param url_ou_path Informe a url ou path do arquivo
	 */
	public void addErroWcagP2(String url_ou_path,int numero_total_de_erro){
		//tratamento de erro dos valores
		addErro(erroWcagP2,url_ou_path,numero_total_de_erro);
	}
	
	/**
	 * Adiciona o total de erro de uma página
	 * 
	 * @param numero_total_de_erro Passe o número total de erro esta página para este ponto de verificação
	 * @param url_ou_path Informe a url ou path do arquivo
	 */
	public void addErroWcagP3(String url_ou_path,int numero_total_de_erro){
		//tratamento de erro dos valores
		addErro(erroWcagP3,url_ou_path,numero_total_de_erro);
	}
	
	/**
	 * Adiciona o total de erro de uma página
	 * 
	 * @param numero_total_de_erro Passe o número total de erro esta página para este ponto de verificação
	 * @param url_ou_path Informe a url ou path do arquivo
	 */
	public void addErroEmagP1(int numero_total_de_erro,String url_ou_path){
		//tratamento de erro dos valores
		addErro(erroEmagP1,url_ou_path,numero_total_de_erro);
	}
	
	/**
	 * Adiciona o total de erro de uma página
	 * 
	 * @param numero_total_de_erro Passe o número total de erro esta página para este ponto de verificação
	 * @param url_ou_path Informe a url ou path do arquivo
	 */
	public void addErroEmagP2(int numero_total_de_erro,String url_ou_path){
		//tratamento de erro dos valores
		addErro(erroEmagP2,url_ou_path,numero_total_de_erro);
	}
	
	/**
	 * Adiciona o total de erro de uma página
	 * 
	 * @param numero_total_de_erro Passe o número total de erro esta página para este ponto de verificação
	 * @param url_ou_path Informe a url ou path do arquivo
	 */
	public void addErroEmagP3(int numero_total_de_erro,String url_ou_path){
		//tratamento de erro dos valores
		addErro(erroEmagP3,url_ou_path,numero_total_de_erro);
	}
	
	/**
	 * Retorna o número total de erros do Emag para prioridade 1
	 * @return int
	 */
	public int getTotalErrosEmagP1(){
		return contaErros(erroWcagP1);
	}
	
	/**
	 * Retorna o número total de erros do Emag para prioridade 2
	 * @return int
	 */
	public int getTotalErrosEmagP2(){
		return contaErros(erroWcagP2);
	}
	
	/**
	 * Retorna o número total de erros do Emag para prioridade 3
	 * @return int
	 */
	public int getTotalErrosEmagP3(){
		return contaErros(erroWcagP3);
	}
	
	/**
	 * Retorna o número total de erros do Wcag para prioridade 1
	 * @return int
	 */
	public int getTotalErrosWcagP1(){
		return contaErros(erroWcagP1);
	}
	
	/**
	 * Retorna o número total de erros do Wcag para prioridade 2
	 * @return int
	 */
	public int getTotalErrosWcagP2(){
		return contaErros(erroWcagP2);
	}
	
	/**
	 * Retorna o número total de erros do Wcag para prioridade 3
	 * @return int
	 */
	public int getTotalErrosWcagP3(){
		return contaErros(erroWcagP3);
	}
	
	/**
	 * Conta os erros do projeto, ou seja, soma os erros de todas as páginas
	 * @param mapa
	 * @return
	 */
	private int contaErros(HashMap<String,Integer> mapa){
		int total=0;
		Collection a = mapa.values();
		Iterator it = a.iterator();
		while (it.hasNext()) {
			total+=Integer.parseInt(it.next().toString());;
		}
		return total;
	}
	
	/**
	 * Adiciona um erro
	 */
	private void addErro(HashMap<String,Integer> mapa,String url_ou_path,int total){
		if(total<0){
			//Erro
		}else{
			mapa.put(url_ou_path, total);
		}
	}

	public static String getLocalDoProjetoNoHD() {
		return localDoProjetoNoHD;
	}

	public static void setLocalDoProjetoNoHD(String localDoProjetoNoHD) {
		ProjetoDoSite.localDoProjetoNoHD = localDoProjetoNoHD;
	}

	public static String getNomeDoProjeto() {
		return nomeDoProjeto;
	}

	public static void setNomeDoProjeto(String nomeDoProjeto) {
		ProjetoDoSite.nomeDoProjeto = nomeDoProjeto;
	}
	
}
