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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.org.acessobrasil.ases.nucleo.InterfClienteDoNucleo;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.regras.InterfRegrasHardCoded;
import br.org.acessobrasil.silvinha2.util.G_File;

/**
 * Contém informações de uma página HTML
 * @author Fabio Issamu Oshiro
 *
 */
public class Pagina implements InterfClienteDoNucleo  {
	private String path_url;
	HashSet<String> links;
	HashMap<String,ArrayList<ArmazenaErroOuAviso>> erroP1;
	HashMap<String,ArrayList<ArmazenaErroOuAviso>> erroP2;
	HashMap<String,ArrayList<ArmazenaErroOuAviso>> erroP3;
	HashMap<String,ArrayList<ArmazenaErroOuAviso>> avisoP1;
	HashMap<String,ArrayList<ArmazenaErroOuAviso>> avisoP2;
	HashMap<String,ArrayList<ArmazenaErroOuAviso>> avisoP3;
	
	G_File arqDeErros;
	
	private String path_guardarErros;
	
	private InterfNucleos nucleo;
	InterfRegrasHardCoded regras;
	
	public Pagina(){
		
	}
	
	/**
	 * Informa o path para guardar os erros
	 * @param fullPath Caminho completo
	 */
	public void setPathGuardarErros(String fullPath){
		path_guardarErros=fullPath;
	}
	
	public void avaliaPagina(){
		path_guardarErros="C:\\temp\\teste.txt";
		G_File arq = new G_File("X:\\acessobrasil.org.br\\html_exemplos_de_erros\\indexN2.htm");
		nucleo = MethodFactNucleos.mFNucleos("Estruturado");
		nucleo.setCodHTML(arq.read());
		nucleo.setWCAGEMAG(InterfNucleos.EMAG);		
		nucleo.addCliente(this);
		nucleo.avalia();
	}
	
	/**
	 * O núcleo chama este método quando acaba a avaliação 
	 * da página
	 */
	public void fimDaAvaliacao() {
		// TODO Auto-generated method stub
		arqDeErros = new G_File(path_guardarErros);
		arqDeErros.write("path_url="+path_url+"\n");
		
		arqDeErros.append("#Erros de P1\n");
		gravaMap(erroP1);
		arqDeErros.append("#Erros de P2\n");
		gravaMap(erroP2);
		arqDeErros.append("#Erros de P3\n");
		gravaMap(erroP3);
		arqDeErros.append("#Avisos de P1\n");
		gravaMap(avisoP1);
		arqDeErros.append("#Avisos de P2\n");
		gravaMap(avisoP2);
		arqDeErros.append("#Avisos de P3\n");
		gravaMap(avisoP3);
		
	}
	
	/**
	 * Grava o mapa no arquivo
	 * @param mapaErrosOuAvisos
	 */
	private void gravaMap(HashMap<String,ArrayList<ArmazenaErroOuAviso>> mapaErrosOuAvisos){
		if(mapaErrosOuAvisos==null){
			return;
		}
		if(regras==null){
			return;
		}
		Map<String, ArrayList<ArmazenaErroOuAviso>> sortedMap = new TreeMap<String, ArrayList<ArmazenaErroOuAviso>>(mapaErrosOuAvisos);
		List<String> mapKeys = new ArrayList<String>(sortedMap.keySet());
		ArrayList<ArrayList<ArmazenaErroOuAviso>> mapValues = new ArrayList<ArrayList<ArmazenaErroOuAviso>>(sortedMap.values());
		for(int i=0;i<mapKeys.size();i++){
			String codRegra=mapKeys.get(i);
			arqDeErros.append("Regra "+mapKeys.get(i)+":\n"+regras.getTextoRegra(codRegra)+"\n");
			//System.out.print("Regra "+mapKeys.get(i)+":\n"+regras.getTextoRegra(mapKeys.get(i))+"\n");
			ArrayList<ArmazenaErroOuAviso> erros = new ArrayList<ArmazenaErroOuAviso>(mapValues.get(i));
			for(int j=0;j<erros.size();j++){
				//System.out.print("cod "+erros.get(j).getIdTextoRegra()+"\n");
				//System.out.print("linha "+erros.get(j).getLinha()+" coluna "+erros.get(j).getColuna()+"\n");
				arqDeErros.append("linha "+erros.get(j).getLinha()+" coluna "+erros.get(j).getColuna()+"\n");
			}
		}
	}
	
	/**
	 * Organiza o array armazenaErroOuAviso
	 */
	private void organizaErroOuAviso(HashMap<String,ArrayList<ArmazenaErroOuAviso>> mapa,ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso){
		//Coloca no hashmap de forma ordenada
		for(int i=0;i<armazenaErroOuAviso.size();i++){
			if(!mapa.containsKey(armazenaErroOuAviso.get(i).getIdTextoRegra())){
				mapa.put(armazenaErroOuAviso.get(i).getIdTextoRegra(), new ArrayList<ArmazenaErroOuAviso>());				
			}
			mapa.get(armazenaErroOuAviso.get(i).getIdTextoRegra()).add(armazenaErroOuAviso.get(i));
		}
	}
	
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve um arrayList de avisos de P1
	 */
	public void avisosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		avisoP1 = new HashMap<String,ArrayList<ArmazenaErroOuAviso>>();
		organizaErroOuAviso(avisoP1,armazenaErroOuAviso);
	}
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve um arrayList de avisos de P2
	 */
	public void avisosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		avisoP2 = new HashMap<String,ArrayList<ArmazenaErroOuAviso>>();
		organizaErroOuAviso(avisoP2,armazenaErroOuAviso);
	}
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve um arrayList de avisos de P3
	 */
	public void avisosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		avisoP3 = new HashMap<String,ArrayList<ArmazenaErroOuAviso>>();
		organizaErroOuAviso(avisoP3,armazenaErroOuAviso);
	}
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve um arrayList de erros de P1
	 */
	public void errosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		erroP1 = new HashMap<String,ArrayList<ArmazenaErroOuAviso>>();
		organizaErroOuAviso(erroP1,armazenaErroOuAviso);
	}
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve um arrayList de erros de P2
	 */
	public void errosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		erroP2 = new HashMap<String,ArrayList<ArmazenaErroOuAviso>>();
		organizaErroOuAviso(erroP2,armazenaErroOuAviso);
	}
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve um arrayList de erros de P3
	 */
	public void errosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		erroP3 = new HashMap<String,ArrayList<ArmazenaErroOuAviso>>();
		organizaErroOuAviso(erroP3,armazenaErroOuAviso);
	}
	
	/**
	 * Método de retorno do núcleo
	 * o núcleo devolve uma lista de links encontrados
	 * durante a avaliação
	 */
	public void linksAchadosPeloNucleo(HashSet links) {
		// TODO Auto-generated method stub
		
	}

	public void setNucleo(InterfNucleos nucleo) {
		this.nucleo = nucleo;
		regras = this.nucleo.getRegras(); 
	}
}
