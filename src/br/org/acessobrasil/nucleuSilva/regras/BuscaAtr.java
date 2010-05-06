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

package br.org.acessobrasil.nucleuSilva.regras;

import java.util.ArrayList;
import java.util.Iterator;

import br.org.acessobrasil.nucleuSilva.entidade.Atributo;
import br.org.acessobrasil.nucleuSilva.entidade.Regra;

/**
 * Implementação da regra de avaliação de uma tag no
 * qual ele busca:
 * <ol>
 * <li>Exist&ecirc;ncia ou não do atributo </li>
 * <li>Array de valores que podem existir no atributo. </li>
 * <li>Array de valores que não podem existir no atributo. </li>
 * <li>Tamanho mínimo do atributo. </li>
 * <li>Tamanho máximo do atributo. </li>
 * </ol>
 * 
 * @author Acessibilidade Brasil.
 */
public class BuscaAtr {

    /**
     * Avaliação do atributo sobre a visão apresentada em banco.
     * @param regra conjunto de regras para a tag.
     * @param valor conteúdo de um atributo.
     * @return verdadeiro se ele não tiver nenhum problema com os parâmetros
     * oferecidos.
     */
    private boolean confere(final Regra regra, final String valor) {
        /*
         * Valor sim.
         */
        final String sim = regra.getVs();
        /*
         *  Valor não.
         */
        final String nao = regra.getVn();
        /*
         * Valor mínimo.
         */
        final int minimo = regra.getTi();
        /*
         * Valor máximo.
         */
        final int maximo = regra.getTx();

        if (maximo != 0 && valor.length() > maximo) {
            return false;
        }

        if (minimo != 0 && valor.length() < minimo) {
            return false;
        }

        if (sim != null && !"".equals(sim)) {
            String [] array = sim.split(";");
            boolean bool = true;
            for (int i = 0; i < array.length; i++) {
                String string = array[i];
                if (valor.toUpperCase().indexOf(string.toUpperCase()) >= 0) {
                    bool = false;
                }
            }

            if (bool) {
                return false;
            }
        }

        if (nao != null && !"".equals(nao)) {
            String [] array = nao.split(";");
            for (int i = 0; i < array.length; i++) {
                String string = array[i];
                if (valor.toUpperCase().indexOf(string.toUpperCase()) >= 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Iterador de atributos para que possam ser avaliados. ele tem o objetivo
     * de afirmar se o atributo é incorreto, mas ela tenta sempre afirmar que a
     * tag é incorreta.
     * @param regra para avaliação do atributo.
     * @param atributos atributos da tag.
     * @return falso se o conjunto de tags não contiver erros.
     */
    public boolean validacao(final Regra regra, final ArrayList<Atributo> atributos) {
   	
        boolean bol = true;
     
        for (Atributo atr : atributos) {
        	if (regra.getProcurado().equalsIgnoreCase(atr.getTipo())) {
                bol = !confere(regra, atr.getConteudo());
            }
        }

        return bol;
    }

    /**
     * Iterador de atributos para que possam ser avaliados. ele tem o objetivo
     * de afirmar se o atributo é incorreto, mas ela tenta sempre afirmar que a
     * tag é correta.
     * @param regra para avaliação do atributo.
     * @param atributos atributos da tag.
     * @return falso se o conjunto de tags não contiver erros.
     */
    public boolean validacaoIncertiva(final Regra regra,
            final ArrayList<Atributo> atributos) {

        boolean bol = false;
        Iterator<Atributo> iatributo = atributos.iterator();
        Atributo atrib = null;
        while (iatributo.hasNext()) {
            atrib = iatributo.next();
            if (regra.getProcurado().equalsIgnoreCase(atrib.getTipo())) {
                bol = true;
                bol = !confere(regra, atrib.getConteudo());
            }
        }

        return bol;
    }

}
