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

package br.org.acessobrasil.ases.nucleo.adapters;

import java.util.ArrayList;
import java.util.Properties;

import br.org.acessobrasil.ases.nucleo.InterfClienteDoNucleo;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.regras.InterfRegrasHardCoded;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;

/**
 * retorna o ArrayList de Validados, necessário para o processar Erros
 * 
 * @author Renato Tomaz Nati
 * 
 * 
 */
public class AdapterNovoNucleo1 implements InterfNucleos {

	public ArrayList<ArmazenaErroOuAviso> getValidados(RelatorioDaUrl relatorio, Properties props) {

		return null;

	}

	public void addCliente(InterfClienteDoNucleo obj) {
		// TODO Auto-generated method stub

	}

	public void setAvaliaCSS(boolean s) {
		// TODO Auto-generated method stub
		
	}

	public void setEscolheErro(int cod_erro) {
		// TODO Auto-generated method stub
		
	}

	public void setWCAGEMAG(int ConstRegra) {
		// TODO Auto-generated method stub
		
	}

	public void setCodHTML(String codigo) {
		// TODO Auto-generated method stub
		
	}

	public void avalia() {
		// TODO Auto-generated method stub
		
	}

	public InterfRegrasHardCoded getRegras() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRegras(InterfRegrasHardCoded regras) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<ArmazenaErroOuAviso> getErroOuAviso() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getCodWcagEmag(){
		return 0;
	}
	public String getCodHTML() {
		// TODO Auto-generated method stub
		 return null;
	}
public String getCodHTMLOriginal(){
		
		return null;
	}
}
