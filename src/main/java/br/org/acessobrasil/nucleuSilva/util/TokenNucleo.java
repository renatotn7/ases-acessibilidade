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

/**
 * Configurações do motor antigo
 * @author Acessibilidade Brasil.
 */
final public class TokenNucleo {
    /**
     * login com o banco
     */
    final public static String LOGIN           = "sa";

    /**
     * senha com o banco
     */
    final public static String SENHA           = "";

    /**
     * expresão para conexão jdbc
     */
    final public static String CONECTOR        = "jdbc:hsqldb:hsql:";

    /**
     * url para localizar o banco
     */
    final public static String URL             = "//localhost/regras";

    /**
     * drive do banco
     */
    final public static String DRIVER          = "org.hsqldb.jdbcDriver";
    /**
     * representação do nome da tag de linha.
     */
    static public final String LINHA             = "Lin";

    /**
     * representaçã da tag de coluna.
     */
    static public final String COLUNA            = "Col";

    /**
     * representaçção da tag que ira envolver todas as outras tag´s.
     */
    static public final String MAE           = "ABC";

    /*
     * Deve ser populado pelo banco com o construtor, assim evita erro de
     * desenvolvimento
     */

        

    /**
     * lista das tags automaticas.
     */
    public static final int [] automaticos       = { 10, 100, 58, 33, 94, 5,
            82, 104, 105, 106, 107, 108, 109    };

    /**
     * definição de um atributo para o corretor por parametro.
     */
   // public static final int    PARAMETROATRIBUTO = 1;

    /**
     * definição de uma tag para o corretor por parametro.
     */
   // public static final int    PARAMETROTAG      = 2;

    /**
     * definição de que quem recebera o parametro é um atributo de dimensão.
     */
   // public static final int    PARAMETRODIMENSAO = 3;

    /**
     * constante que define a busca a tag relacionada a tag avaliada.
     */
    public static final int    BUSCATAG          = 1;

    /**
     * constante que define que a tag deve existir.
     */
    public static final int    EXIST             = 2;

    /**
     * constante que define que a tag so pode ser validade de forma unica, assim
     * ele deve utilizar um codigo estruturado.
     */
    public static final int    HARDCODED         = 3;

    /**
     * constante que define a tag tem de existir.
     */
    public static final int    HAVER             = 4;

    /**
     * constante que define a busca a tag não pode existir no documento.
     */
    public static final int    NEXIT             = 5;

    /**
     * constante que define o texto estará se repetindo.
     */
    public static final int    REPET             = 6;

    /**
     * constante que define a busca na tag de um atributo especifico.
     */
    public static final int    BUSCAATR          = 7;

    /**
     * constante que define a busca de uma tag filha ao seu contexto.
     */
    public static final int    TAGINTERNA        = 9;

    /**
     * constante que define o atributo não pode existir na tag.
     */
    public static final int    TAGN              = 10;

    /**
     * constante que define a obrigatoriedade nos eventos de mão
     */
    public static final int    OBR               = 11;

    /**
     * constante que define que se o atributo existir ai sim se avalia
     */
    public static final int    ATRI              = 12;

    /**
     * constante que define que se a tag existir então ela deve ser avaliada 
     */
    public static final int    INFO              = 13;

    /**
     * verificador se a tag deve ser corrigida automaticamente ou não
     * 
     * @param atribuido
     *            ponto de verificação buscado na lista de automaticos
     * @return booleano se existe ou não
     */
    public static boolean contem(int atribuido) {
   	
    	for (int i = 0; i < automaticos.length; i++)
            if (automaticos[i] == atribuido) return true;
        return false;

    }

}
