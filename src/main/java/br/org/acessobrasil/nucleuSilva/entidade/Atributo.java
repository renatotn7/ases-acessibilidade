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

/*
 * Created on 06/03/2005
 */
package br.org.acessobrasil.nucleuSilva.entidade;


/**
 * Classe que representa um atributo de uma tag HTML.
 * @author Acessibilidade Brasil, em 22/08/2005. Refatorado em 04/01/2006.
 * @version 1.1
 */
public final class Atributo {
    /**
     * Nome do atributo.
     */
    private String tipo;

    /**
     * Valor do atributo.
     */
    private String conteudo;

    /**
     * Construtor de Atributo.
     * @param nomeAtr Nome do Atributo
     * @param valorAtr Valor do Atributo.
     */
    public Atributo(final String nomeAtr, final String valorAtr) {
     	 
    	this.conteudo = valorAtr;
        this.tipo = nomeAtr;
    }

    /**
     * Construtor Privado de Atributo.
     */
    private Atributo() { }

    /**
     * @return Retorna o valor de conteudo.
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * @param valorAtr Seta o valor de conteudo.
     */
    public void setConteudo(final String valorAtr) {
        this.conteudo = valorAtr;
    }

    /**
     * @return Retorna o valor do nome do atributo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param nomeAtr Seta o valor do nome do atributo.
     */
    public void setTipo(final String nomeAtr) {
        this.tipo = nomeAtr;
    }
}
