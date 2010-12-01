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

package br.org.acessobrasil.ases.regras;

/**
 * Interface para as regras HardCoded de variados orgãos
 * 
 * @author Renato Tomaz Nati & Fabio Issamu Oshiro
 * @since 27/07/2007
 * @version 1.0
 */
public interface InterfRegrasHardCoded {
	/**
	 * Verifica se a extensão é .gif
	 * @param tagInteira
	 * @return true caso no final seja gif
	 */
	public boolean verificaIsGif(String tagInteira);
	
	/**
	 * Método que retorna os avisos Genéricos de determinado orgão(ex.:
	 * EMAG,WCAG)
	 * 
	 * @return String[] deve seguir este formato: 
	 *         gl.cp@regrax@prioridade@***@...
	 * @since 27/07/2007
	 */
	public String[] getGenericos();
	public boolean verificaLabel(String codHTML,String idField);
	public int verificaH1aH6(String codHTML);
	
	/**
	 * Verifica caracteres predefinidos
	 * @param tagInput
	 * @return true
	 */
	public boolean verificaTextoPredefinidoInput(String tagInput);
	
	/**
	 * Verifica se a tag tem algum parametro indicando tamanho fixo
	 * Hoje não vai até um arquivo CSS separado
	 * @param tag
	 * @return true caso esteja ok
	 */
	public boolean verificaTamFixoEmAtributoStyle(String tag);
	
	/**
	 * Verifica se a tag html tem um label associado Input text Input (text
	 * default) Input password Input checkbox Input radio Input file textarea
	 * (sem type) select (sem type)
	 * 
	 * @param codHTML
	 * @param tag
	 * @return true caso esteja com label associado ou não precise
	 */
	public boolean verificaLabelTag(String codHTML,String tag);
	
	/**
	 * Verifica se existe texto predefinido para a tag area
	 * @param codHTML
	 * @param tag
	 * @param tagIndex início de onde começa a tag no código html
	 * @return true caso exista texto predefinido
	 */
	public boolean verificaTextoPredefinidoTextarea(String codHTML,String tag,int tagIndex);
	
	/**
	 * Verifica se existe texto predefinido para a tag area
	 * @param codHTML
	 * @param tag
	 * @param tagIndex início de onde começa a tag no código html
	 * @return true caso exista texto predefinido
	 *  @deprecated
	 */
	public boolean verificaTextoPredefinidoTextarea(String codHTML,String tag);
	
	public boolean verificaSeparadorEmLink(String codHTML);
	/**
	 * Verifica se existe uma tag noscript no código html
	 * @param codHTML
	 * @return true caso exista alguma
	 */
	public boolean verificaNoscript(String codHTML);
	
	/**
	 * Verifica se está na lista de Deprecated ou Non Standard
	 * @param tagName
	 * @return true
	 */
	public boolean verificaDeprecatedNonStandard(String tagName);
	
	/**
	 * Verifica se a tag possui o stributo lang
	 * @param tag
	 * @return true
	 */
	public boolean verificaLang(String tag);
	
	/**
	 * Verifica se a tag link é de css
	 * @param tagInteira
	 * @return true caso seja link de CSS
	 */
	public boolean verificaIsCSS(String tagInteira);
	
	/**
	 * Verifica se há algum parametro de cor
	 * @param tagInteira
	 * @return true caso haja parametro de cor
	 */
	public boolean verificaCorStyleInline(String tagInteira);
	
	/**
	 * Verifica se na tag há um longdesc
	 * @param tagInteira
	 * @return true caso tenha um long desc
	 */
	public boolean verificaLongDesc(String tagInteira);
	
	/**
	 * Retorna o texto da regra
	 * Ex: 2.19 
	 * @param codigo código da regra (2.19)
	 * @return Texto da regra
	 */
	public String getTextoRegra(String codigo);
	
	/**
	 * Verifica se possui um atributo style
	 * @param tagInteira
	 * @return true caso tenha algun style
	 */
	public boolean verificaPossuiStyle(String tagInteira);
	
	/**
	 * Retorna o atributo especificado caso não tenha retorna ""
	 * @param tag
	 * @param att
	 * @return valor
	 */
	public String getAtributo(String tagInteira, String att);
	
	/**
	 * Retorna a prioridade da regra
	 * Ex: 2.19 
	 * @param codigo código da regra (2.19)
	 * @return Prioridade da regra
	 */
	public int getPrioridadeRegra(String codigo);
	
	/**
	 * Verifica se existe height ou width com tamanho fixo]
	 * @return true caso haja tamanho fixo
	 */
	public boolean verificaTamFixoAtributoWidhtHeight(String tagInteira);
	
	/**
	 * As classes de regras devem informar qual é o código da regra 
	 * @return 1 para Wcag
	 * 2 para Emag 
	 */
	public abstract int getCodWcagEmag();
	
	/**
	 * Verifica se existe texto dentro das tags object
	 * @param codHTML
	 * @param tag
	 * @param tagIndex início de onde começa a tag no código html
	 * @return true caso exista texto predefinido
	 */
	public boolean verificaTextoEmObject(String codHTML,String tag,int tagIndex);
	
	/**
	 * Verifica se existe texto na tag iframe
	 * 
	 * @param codHTML
	 * @param tag
	 * @param tagIndex
	 *            início de onde começa a tag no código html
	 * @return true caso exista texto dentro de iframe
	 */
	public boolean verificaTextoIframe(String codHTML, String tag, int tagIndex);
}