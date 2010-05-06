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

package br.org.acessobrasil.ases.nucleo;

import java.util.ArrayList;
import java.util.Properties;

import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.regras.InterfRegrasHardCoded;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
/**
 * Interface para os adaptadores de nucleos
 * 
 * @author Renato Tomaz Nati
 * 
 *
 */
public interface InterfNucleos {
	/**
	 * Regra WCAG
	 */
	public static final int WCAG=1;
	/**
	 * Regra EMAG
	 */
	public static final int EMAG=2;
	/**
	 * retorna o ArrayList de Validados, necessário para o processar Erros  
	 */	
	public ArrayList<ArmazenaErroOuAviso> getValidados(RelatorioDaUrl relatorio, Properties props);

	/**
	 * Adiciona um cliente para retornar as ações do núcleo
	 * @param objCliente
	 */
	public void addCliente(InterfClienteDoNucleo objCliente);
	
	/**
	 * Para Wcag 
	 * Para Emag 
	 * @param ConstRegra
	 */
	public void setWCAGEMAG(int ConstRegra);
	
	/**
	 * Opção para avaliar ou não as regras do W3C sobre documentos CSS.
	 */
	public void setAvaliaCSS(boolean s);
	
	/**
	 * Passa o código do Erro a ser avaliado.
	 * Exemplo para avaliar mais de um erro:
	 * nucleo.setEscolheErro(nucleo.IMG_ALT);
	 * nucleo.setEscolheErro(nucleo.LABEL);
	 * nucleo.setEscolheErro(nucleo.FRAME_TITULO);
	 * nucleo.setEscolheErro(nucleo.TABELA_TAM_FIXO); 
	 */
	public void setEscolheErro(int cod_erro);
	
	/**
	 * Passa o código HTML a ser avaliado.
	 */
	public void setCodHTML(String codigo);

	/**
	 * Retorna as regras que o núcleo está utilizando
	 * @return
	 */
	public InterfRegrasHardCoded getRegras();

	/**
	 * Coloca as regras que o núcleo está utilizando
	 * @param regras
	 */
	public void setRegras(InterfRegrasHardCoded regras);
	
	/**
	 * Avalia a página
	 */
	public void avalia();
	
	/**
	 * Retorna os erros de forma bruta sem separar
	 * por prioridades
	 * @return ArrayList de ArmazenaErroOuAviso
	 */
	public ArrayList<ArmazenaErroOuAviso> getErroOuAviso();

	public int getCodWcagEmag();
	public String getCodHTML();
	public String getCodHTMLOriginal();
}
