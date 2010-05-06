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

package br.org.acessobrasil.nucleuSilva.entidade;


/**
 * @author Acessibilidade Brasil, em 22/08/2005.
 */
public final class Tag {
	
    /**
     * Posição da tag HTML na página avaliada.
     */
    private Posicao p;
    /**
     * Texto da tag HTML.
     */
    private String texto;
    /**
     * Espaços ou tabulações, utilizadas para formatar a tag dentro da
     * linha na página avaliada.
     */
    private Espaco espaco;
    /**
     * Número de identificação do Ponto de verificação utilizado.
     */
    private int pv3;

    /**
     * Construtor de Tag.
     * @param position Posição da tag HTML na página avaliada.
     * @param text Texto da tag HTML.
     * @param espacos Dados referentes a espaçamento.
     */
    public Tag(final Posicao position,
            final String text, final Espaco espacos) {
   	
    	this.p = position;
        this.texto = text;
        this.espaco = espacos;
    }

    /**
     * Construtor de Tag.
     * @param position Posição da tag HTML na página avaliada.
     * @param text Texto da tag HTML.
     * @param espacos Dados referentes a espaçamento.
     * @param chkPoint Número de identificação do
     * Ponto de verificação utilizado.
     */
    public Tag(final Posicao position, final String text, final Espaco espacos,
            final int chkPoint) {
        this.p = position;
        this.texto = text;
        this.espaco = espacos;
        this.pv3 = chkPoint;
    }

    /**
     * @return Retorna o valor de espaco.
     */
    public Espaco getEspaco() {
        return espaco;
    }

    /**
     * @param espacos Seta o valor de espaco.
     */
    public void setEspaco(final Espaco espacos) {
        this.espaco = espacos;
    }

    /**
     * @return Retorna o valor de p.
     */
    public Posicao getP() {
        return p;
    }

    /**
     * @param coords Seta o valor de p.
     */
    public void setP(final Posicao coords) {
        this.p = coords;
    }

    /**
     * @return Retorna o valor de pv3.
     */
    public int getPv3() {
        return pv3;
    }

    /**
     * @param chkPoint Seta o valor de pv3.
     */
    public void setPv3(final int chkPoint) {
        this.pv3 = chkPoint;
    }

    /**
     * @return Retorna o valor de texto.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param text Seta o valor de texto.
     */
    public void setTexto(final String text) {
        this.texto = text;
    }
}
