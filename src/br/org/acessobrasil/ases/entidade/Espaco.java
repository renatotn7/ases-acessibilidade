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

package br.org.acessobrasil.ases.entidade;


/**
 * Classe reponsável por informar a quantidade de espaços e tabulações
 * no início de cada linha da página avaliada. Ela é importante para reconstrução da
 * página nos relatórios, já que os espaços e tabulações são perdidos
 * no processo de avaliação.
 * @author Mariano Aloi, em 22/08/2005. Refatorado por André em 04/01/2006.
 * @version 1.1
 */
public final class Espaco {
    /**
     * Quantidade de tabulações na linha avaliada.
     */
    private int tabulacaoLinha;
    /**
     * Quantidade de espaços na linha avaliada.
     */
    private int espacoLinha;

    /**
     * Construtor de Espaço.
     * @param qtdTab Quantidade de tabulações na linha avaliada.
     * @param qtdEspaco Quantidade de espaços na linha avaliada.
     */
    public Espaco(final int qtdTab, final int qtdEspaco) {
   	
    	this.tabulacaoLinha = qtdTab;
        this.espacoLinha = qtdEspaco;
    }

    /**
     * @return Retorna o valor de espacoLinha.
     */
    public int getEspacoLinha() {
        return espacoLinha;
    }

    /**
     * @param qtdEspaco Seta o valor de espacoLinha.
     */
    public void setEspacoLinha(final int qtdEspaco) {
        this.espacoLinha = qtdEspaco;
    }

    /**
     * @return Retorna o valor de tabulacaoLinha.
     */
    public int getTabulacaoLinha() {
        return tabulacaoLinha;
    }

    /**
     * @param qtdTab Seta o valor de tabulacaoLinha.
     */
    public void setTabulacaoLinha(final int qtdTab) {
        this.tabulacaoLinha = qtdTab;
    }

}
