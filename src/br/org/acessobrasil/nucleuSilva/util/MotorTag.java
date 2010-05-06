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

import java.util.ArrayList;
import java.util.Iterator;

import br.org.acessobrasil.nucleuSilva.entidade.Espaco;
import br.org.acessobrasil.nucleuSilva.entidade.Posicao;
import br.org.acessobrasil.nucleuSilva.entidade.Tag;

/**
 * Tag HTML
 * @author Acessibilidade Brasil, em 22/08/2005.
 */
public class MotorTag extends Thread {
    /**
     * linha que a tag se encontra
     */
    private int linha;

    /**
     * numero de tab´s que foram colocadas antes da tag para formar endentação
     */
    private int tabulacaoLinha;

    /**
     * numero de espaços que foram colocadas antes da tag para formar endentação
     */
    private int espacoLinha;
    /**
     * documento parametrizado com linha e coluna
     */
    private String code;
    /**
     * Tags alinhadas dentro de um array bidimensional
     * @uml.property   name="tags"
     * @uml.associationEnd   multiplicity="(0 -1)"
     */
    private Tag [][] tags;
    /**
     * Construtor com entrada do documento em forma parametrizada
     * @param code
     */
    public MotorTag(String code) {
    	this.code = code;
    }
    /**
     * Iniciação do processo do motor de tags
     */
    @Override
    public void run() {
        quebrar();
    }
    /**
     * Roda sem o processo
     *
     */
    public void noProcess(){
    	quebrar();
    }
    /**
     * Respons&aacute;vel pela organização da classe
     */
    private void quebrar() {
        ArrayList<String> tagCrua = alinhar(code);
        ArrayList<ConcatTag> tag = popularTag(tagCrua);
        tags = popularArray(tag);
    }

    /**
     * Respons&aacute;vel por colocar as tags em um array bidimensional.
     * 
     * @param tagAL
     *            array list com todas as tags do documento já posicionadas
     * @return tags o array bidimensional
     */
    private Tag [][] popularArray(ArrayList<ConcatTag> tagAL) {
        Tag [][] tags = new Tag[linha + 1][]; // array com as tags
        for (Iterator iter = tagAL.iterator(); iter.hasNext();) {
            ConcatTag tagCont = (ConcatTag) iter.next();
            tags[tagCont.getLinha()] = new Tag[tagCont.getArray().size()];// array
            // se
            // adaptando
            // ao
            // tamanho
            // real
            // da
            // quantidade
            // de
            // tags
            for (Iterator iterator = tagCont.getArray().iterator(); iterator
                    .hasNext();) {
                Tag tag = (Tag) iterator.next();
                tags[tagCont.getLinha()][tag.getP().getColuna()] = tag;// posicioando
                // a tag
                // dentro
                // do
                // array
            }
        }
        return tags;
    }

    /**
     * M&eacute;todo para transformar uma lista de tags ainda cruas (sem
     * posicionamento e endenta&ccedil;&atilde;o) em tags que ser&atilde;o
     * posicionadas em rela&ccedil;&atilde;o com o documento avaliado.
     * 
     * @param tagCrua
     *            array de tags ainda com tags de posicionamento
     * @return tag com tags posicionadas relativa ao documento pesquisado
     */
    private ArrayList<ConcatTag> popularTag(ArrayList<String> tagCrua) {
    	//array que será retornado
        ArrayList<ConcatTag> tags = new ArrayList<ConcatTag>();
        //array temporario
        ArrayList<Tag> arrayTags = new ArrayList<Tag>();
        ConcatTag conc = new ConcatTag();
        //booleano que representa que já se passou da 1ª linha 
        boolean bool = false; 
        int coluna = 0;
        for (Iterator iter = tagCrua.iterator(); iter.hasNext();) {
            String stringCrua = (String) iter.next(); 

            /*
             * espaços relativos a tag de linha
             */
            int esp1 = stringCrua.indexOf(" "); // 1º espaço
            int esp2 = stringCrua.indexOf(" ", esp1 + 1);// 2º espaço
            int esp3 = stringCrua.indexOf(" ", esp2 + 1);// 3º espaço
            int esp4 = stringCrua.length() - 3;// 4º espaço

            String nomeTag = null; // tentar descobrir o nome da tag

            if (esp1 > 1) nomeTag = stringCrua.substring(1, esp1);// nome da
            // tag
            // convencional
            else if (stringCrua.indexOf("/") > -1) nomeTag = stringCrua // nome
                    // da
                    // tag
                    // se
                    // ela é
                    // de
                    // fechamento
                    .substring(2, stringCrua.length() - 1);
            else if (stringCrua.length() > 1) nomeTag = stringCrua.substring(1, // nome
                    // da
                    // tag
                    // se
                    // ela
                    // não
                    // contiver
                    // parametros
                    stringCrua.length() - 1);
            else nomeTag = ""; // se nada der certo damos um nome "vazio"

            if (nomeTag != null // tags de configuração
                    && (nomeTag.equals(TokenNucleo.LINHA)
                            || nomeTag.equals(TokenNucleo.COLUNA) || nomeTag
                            .equals(TokenNucleo.MAE))) {
                if (nomeTag.equals(TokenNucleo.LINHA)) {// tag de linha
                    if (bool) { // caso não seja a primeira vez
                        tags.add(conc);// adciona em tags
                        conc = new ConcatTag(); // cria um novo contenetor para
                        // as tags
                        arrayTags = new ArrayList<Tag>(); // cria um novo
                        // array list de
                        // tags
                    }

                    linha = new Integer(stringCrua
                            .substring(esp1 + 4, esp2 - 1)); // numero de
                    // linha
                    tabulacaoLinha = new Integer(stringCrua.substring(esp2 + 4,
                            esp3 - 1));// numero de tabulações
                    espacoLinha = new Integer(stringCrua.substring(esp3 + 4,
                            esp4));// numero de espaços
                    conc.setLinha(linha);
                    coluna = 0; // em uma nova linha a coluna é sempre 0
                    bool = true;// agora que já passou, não é mais a primeira
                    // vez
                }
                else if (nomeTag.equals(TokenNucleo.COLUNA)) { // contabiliza a
                    // coluna
                    coluna = new Integer(stringCrua.substring(esp1 + 4,
                            stringCrua.length() - 3));
                    conc.setColuna(coluna);
                }
                else if (nomeTag.equals(TokenNucleo.MAE)) { // tag utilizada
                    // para conter todas
                    // as outra tags
                    if (bool) tags.add(conc);
                }
            }
            else {// tags normais do documento que receberão uma
                // parametrização precisa
                arrayTags.add(new Tag(new Posicao(linha, coluna), stringCrua,
                        new Espaco(tabulacaoLinha, espacoLinha)));
                conc.setArray(arrayTags);
            }

        }
        return tags;
    }

    /**
     * @param code
     *            lista linear das tags do documento e de
     *            configura&ccedil;&atilde;o.
     * @return tags array list com as tags do documento e tags de
     *         configura&ccedil;&atilde;o.
     */
    private ArrayList<String> alinhar(String code) {
        ArrayList<String> tags = new ArrayList<String>();
        String tag;
        // array com a divisão de tags Html pela sua abertura 
        String inicioAbre[] = code.split("<"); 
        for (int x = 1; x < inicioAbre.length; x++) {
            String inicio = "<" + inicioAbre[x];
            //	array de um array, com a divisão com o seu fim
            String[] inicioFecha = inicio.split(">"); 

            for (int y = 0; y < inicioFecha.length; y++) {
                tag = y < 1 ? inicioFecha[y] + ">" : inicioFecha[y];
                tags.add(tag);
            }
        }
        return tags;
    }

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field MotorTag.java que retorna o seu valor em Tag[][]
     * @return   tags que &eacute; Tag[][].
     */
    public synchronized Tag [][] getTags() {
        return tags;
    }

}

/**
 * @author   Mariano Aloi  Construido em 22/08/2005
 */
class ConcatTag {
    /**
     * Array com as tags da linha
     */
    private ArrayList<Tag> array  = new ArrayList<Tag>();

    /**
     * posição da linha
     */
    private int            linha  = 0;

    /**
     * número máximo de colunas
     */
    private int            coluna = 0;

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field array que retorna o seu valor em ArrayList<Tag>
     * @return   array que &eacute; ArrayList<Tag>.
     * @uml.property   name="array"
     */
    public ArrayList<Tag> getArray() {
        return array;
    }

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field array que recebe o seu valor em ArrayList<Tag>
     * @param arr   &eacute; um ArrayList<Tag> para ser populado.
     * @uml.property   name="array"
     */
    public void setArray(ArrayList<Tag> arr) {
        this.array = arr;
    }

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field coluna que retorna o seu valor em int
     * @return   coluna que &eacute; int.
     * @uml.property   name="coluna"
     */
    public int getColuna() {
        return coluna;
    }

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field coluna que recebe o seu valor em int
     * @param col   &eacute; um int para ser populado.
     * @uml.property   name="coluna"
     */
    public void setColuna(final int col) {
        this.coluna = col;
    }

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field linha que retorna o seu valor em int
     * @return   linha que &eacute; int.
     * @uml.property   name="linha"
     */
    public int getLinha() {
        return linha;
    }

    /**
     * Implementa&ccedil;&atilde;o de encapsulamento da field linha que recebe o seu valor em int
     * @param line   &eacute; um int para ser populado.
     * @uml.property   name="linha"
     */
    public void setLinha(final int line) {
        this.linha = line;
    }

}
