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

package br.org.acessobrasil.nucleuSilva.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que prepara o conteúdo da página HTML para o processo de
 * localização e posicionamento das tags (tagar).
 * @author Acessibilidade Brasil.
 */
public class PrepararConteudo {
    /**
     * Construtor de PrepararConteudo.
     */
    private PrepararConteudo() { }

    /**
     * Método que substitui o conteúdo das tags SCRIPTS e STYLE, ou comentários
     * HTML por espaços, mas mantendo as quebras de linha.
     * @param content Conteúdo da página.
     * @return
     */
    public static StringBuilder ignorarConteudo(final StringBuilder content) {
        String string = ignorarComentarios(ignorarConteudoTags(content.toString()));
        return new StringBuilder(string);
    }

    /**
     * Método que substitui comentários HTML por espaços,
     * mas mantendo as quebras de linha.
     * @param content Conteúdo da página.
     * @return Conteúdo da página sem comentários.
     */
    public static String ignorarComentarios(final String content) {
        String pattern = "<!--(.*?)-->";
        final Pattern ptn = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher mtc = ptn.matcher(content);

        String aux = content.toString();

        while (mtc.find()) {
            aux = aux.replace(mtc.group(), trocarCaracteres(mtc.group()));
        }

        return aux;
    }

    /**
     * Método que atualiza o conteúdo das tags por espaços.
     * @param string Conteúdo da página.
     * @return Conteúdo da página sem conteúdo entre as tags SCRIPT e STYLE.
     */
    public static String ignorarConteudoTags(final String string) {
      	
    	String pattern = "(<SCRIPT(.*?)</SCRIPT>|<STYLE(.*?)</STYLE>)";
        final Pattern ptn = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        final Matcher mtc = ptn.matcher(string);

        String content = string;
        String aux = null;
        String startTag = "", meio = "", endTag = "";
        int pos1 = 0, pos2 = 0;

        while (mtc.find()) {
            aux = mtc.group();

            pos1 = aux.indexOf(">") + 1;
            pos2 = aux.lastIndexOf("<");

            startTag = aux.substring(0, pos1);
            meio = trocarCaracteres(aux.substring(pos1, pos2));
            endTag = aux.substring(pos2, aux.length());

            content = content.replace(mtc.group(), startTag + meio + endTag);
        }

        return content;
    }

    /**
     * Método que substituí os caracteres da string por espaço, com exceção
     * da quebra de linha.
     * @param string String a ser substituído.
     * @return String preenchida por espaços.
     */
    private static String trocarCaracteres(final String string) {
        char[] caracter = string.toCharArray();
        String aux = "";
        for (char pos : caracter) {
            aux += (pos != '\n') ? " " : pos;
        }
        return aux;
    }
}
