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

package br.org.acessobrasil.nucleuSilva.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.org.acessobrasil.nucleuSilva.entidade.Espaco;
import br.org.acessobrasil.nucleuSilva.entidade.Posicao;
import br.org.acessobrasil.nucleuSilva.entidade.Tag;
import br.org.acessobrasil.nucleuSilva.entidade.ArmazenaErroOuAvisoAntigo;
import br.org.acessobrasil.nucleuSilva.util.TokenNucleo;

/**
 * Adapter para o motor do silvinha
 * @author Acessibilidade Brasil, em 22/08/2005.
 */
public class Automaticos {
    /**
     * Array bidimensional de tags, que representa as tags do documento
     * @uml.property   name="tagArray"
     * @uml.associationEnd   multiplicity="(0 -1)"
     */
    private Tag [][]            tagArray;

    /**
     * lista de erros apresentado pelo arquivo
     */
    private ArrayList<ArmazenaErroOuAvisoAntigo> errados;
    
    private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");

    /**
     * @param url -
     *            pagina que será avaliada 
     * @param tipo -
     *            tipo de avaliação
     * @return o array de tags do arquivo
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IOException 
     * @throws ExceptionMariano 
     * @throws Exception 
     */
    public Tag [][] ArrayTags(String url, int tipo) 
    throws IOException, ClassNotFoundException, SQLException, 
    ParserConfigurationException, SAXException, Exception 
    {
        Geral g = new Geral();
        tagArray = g.mainTags(url, tipo); // faz o motor funcionar e recebe o array de tags
        errados = g.getErrados();// recebe os erros
        introduzir();// introduz os erros não automaticos no array.(trocar o PV3)
        corrigir();// Fazer a correção automatica
        return tagArray;
    }
    
    public Tag[][] ArrayTags(String url, int tipo, StringBuilder conteudo)
    throws IOException, ClassNotFoundException, SQLException, 
    ParserConfigurationException, SAXException, Exception
    {
   	
    	Geral g = new Geral();
    	tagArray = g.mainTags(url, tipo, conteudo);
    	errados = g.getErrados();
    	introduzir();
    	corrigir();
    	return tagArray;
    }


    /**
     * Corretor de tags que s&atilde;o complementares, exemplo script noscript
     * 
     * @param errado
     *            componente que representa a tag errada
     */
    private void correcaoTagComplementar(ArmazenaErroOuAvisoAntigo errado) {
        int termino;// termino do nome da tag
        Tag linhaTemp[] = new Tag[1];// linha temporaria
        if (errado.getTagCompleta().indexOf(" ") != -1) termino = errado
                .getTagCompleta().indexOf(" ");
        else termino = errado.getTagCompleta().length() - 1;
        String fechamento = "</"
                + errado.getTagCompleta().substring(1, termino);// simulação do
                                                                // termino da
                                                                // tag
        Tag [][] arrayTagTemp = new Tag[tagArray.length + 1][];// array
                                                                // temporario
                                                                // para
                                                                // manipulação
        for (int i = 0; i < errado.getPosicao().getLinha(); i++) {
            arrayTagTemp[i] = tagArray[i];// repetir as tags até apresentar o
                                            // erro

        }
        linha: for (int x = errado.getPosicao().getLinha(); x < tagArray.length; x++)
            if (tagArray[x] != null) for (int y = 0; y < tagArray[x].length; y++) {
                Tag tagComum = tagArray[x][y];// montar a tag que
                                                // possivelmente é a de
                                                // fechamento
                int posicao = x + 1; // posição para que seja colocada se
                                        // estiver errada
                if (tagComum != null
                        && tagComum.getTexto().length() > fechamento.length()
                        && tagComum.getTexto()
                                .substring(0, fechamento.length())
                                .equalsIgnoreCase(fechamento)) {// consulta se
                                                                // achou a tag
                                                                // de fechamento
                    arrayTagTemp[x] = tagArray[x];// sendo a de fechamento
                                                    // coloque ela

                    linhaTemp = new Tag[2];// e construa uma nova linha com a
                                            // tag complementar
                    linhaTemp[0] = new Tag(new Posicao(posicao, 0), "<"
                            + errado.getProcurado() + ">", tagArray[x][0]
                            .getEspaco());
                    linhaTemp[1] = new Tag(new Posicao(posicao, 0), "</"
                            + errado.getProcurado() + ">", tagArray[x][0]
                            .getEspaco());
                    linhaTemp[0].setPv3(errado.getPv3());// a tag ainda
                                                            // precisa de
                                                            // orientações
                    arrayTagTemp[posicao] = linhaTemp;
                    for (x++; x < tagArray.length; x++) {// continua a
                                                            // colocar as outras
                                                            // tags
                        if (tagArray[x] != null) {
                            linhaTemp = new Tag[tagArray[x].length];
                            for (int y1 = 0; tagArray[x].length > y1; y1++) {
                                Tag t = tagArray[x][y1];
                                if (t != null) {
                                    t.setP(new Posicao(x + 1, y1));// a
                                                                    // configuração
                                                                    // interna
                                                                    // necessita
                                                                    // de que a
                                                                    // linha
                                                                    // seja
                                                                    // acrescida
                                                                    // de 1
                                    linhaTemp[y1] = t;
                                }
                            }
                            arrayTagTemp[x + 1] = linhaTemp;
                        }
                    }

                    break linha;
                }
                else {
                    arrayTagTemp[x] = tagArray[x];// se não for a final vai
                                                    // colocando as mesmas
                                                    // linhas
                }
            }
        tagArray = arrayTagTemp;
    }

    /**
     * M&eacute;todo que diretamente ira corrigir automaticamente as tag&acute;s ,
     * ele baseie-se nos PV3 (ponto de verifica&ccedil;&atilde;o) para cada PV3
     * h&aacute; uma forma de corre&ccedil;&atilde;o quando a
     * corre&ccedil;&atilde;o segue alguma igualdade &eacute; repassado para um
     * m&eacute;todo que generaliza a corre&ccedil;&atilde;o.
     * 
     * @throws SQLException
     */
    private void corrigir() throws SQLException {
        for (Iterator iter = errados.iterator(); iter.hasNext();) {
            ArmazenaErroOuAvisoAntigo errado = (ArmazenaErroOuAvisoAntigo) iter.next();
            Tag tagComum;
            Tag linhaTemp[] = new Tag[1];
            Tag [][] arrayTagTemp = new Tag[tagArray.length + 1][];
            switch (errado.getPv3()) {

                case 34:
                // FONT
                case 5:
                    // OBJECT
                    break;

                case 82:// Frameset
                case 10:// script
                    correcaoTagComplementar(errado);
                    break;
                case 58:// HTML
                    tagComum = tagArray[errado.getPosicao().getLinha()][errado.getPosicao().getColuna()];
                    tagComum.setTexto(tagArray[errado.getPosicao().getLinha()]
                                              [errado.getPosicao().getColuna()].getTexto().substring(0,
                                            		  tagArray[errado.getPosicao().getLinha()]
                                            		          [errado.getPosicao().getColuna()].getTexto().length() - 1)
                                    + " lang=pt >");
                    tagComum.setPv3(-1);
                    break;
                case 100:// INPUT IMAGE
                    tagComum = tagArray[errado.getPosicao().getLinha()][errado
                            .getPosicao().getColuna()];
                    int pontoLocalizacao = errado.getTagCompleta()
                            .toUpperCase().indexOf("ISMAP");

                    tagComum.setTexto(errado.getTagCompleta().substring(0,
                            pontoLocalizacao)
                            + "U"
                            + errado.getTagCompleta().substring(
                                    pontoLocalizacao + 1));
                    tagComum.setPv3(-1);
                    tagArray[errado.getPosicao().getLinha()][errado
                            .getPosicao().getColuna()] = tagComum;
                    break;
                case 33:// DOCTYPE
                    linhaTemp[0] = new Tag(
                            new Posicao(0, 0),
                            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
                            new Espaco(0, 0));
                    linhaTemp[0].setPv3(-1);
                    arrayTagTemp[0] = linhaTemp;
                    for (int x = 0; x < tagArray.length; x++) {
                        arrayTagTemp[x + 1] = tagArray[x];
                        if (arrayTagTemp[x + 1] != null) for (int i = 0; i < arrayTagTemp[x + 1].length; i++) {
                            tagComum = arrayTagTemp[x + 1][i];
                            if (tagComum != null) tagComum.setP(new Posicao(
                                    x + 1, i));
                        }
                    }
                    tagArray = arrayTagTemp;
                    break;
                case 94:// REFRESH
                    tagComum = tagArray[errado.getPosicao().getLinha()][errado
                            .getPosicao().getColuna()];
                    tagComum.setTexto("<!-- " + tagComum.getTexto() + " --> ");
                    tagComum.setPv3(-1);
                    tagArray[errado.getPosicao().getLinha()][errado
                            .getPosicao().getColuna()] = tagComum;
                    break;

                // EVENTOS DE MOUSE
                case 104:
                    eventosTags(errado, "ONKEYPRESS");
                    break;
                case 105:
                    eventosTags(errado, "ONKEYDOWN");
                    break;
                case 107:
                    eventosTags(errado, "ONBLUR");
                    break;
                case 108:
                    eventosTags(errado, "ONFOCUS");
                    break;
                case 109:
                    eventosTags(errado, "ONKEYUP");
                    break;

            }
        }

    }

    /**
     * Coreção dos eventos de mão
     * 
     * @param errado
     *            representação da tag errada
     * @param procurado
     *            nome do atributo faltando
     */
    private void eventosTags(ArmazenaErroOuAvisoAntigo errado, String procurado) {
    	Tag tag = null;
    	try 
    	{
    		tag = tagArray[errado.getPosicao().getLinha()][errado.getPosicao().getColuna()];// tag errada
    	} catch (ArrayIndexOutOfBoundsException e) 
    	{
    		log.info("ArrayIndexOutOfBoundsException:" + e.getMessage());
    	}
        if (tag == null) 
        {
        	return;
        }
        String texto = tag.getTexto().toUpperCase();// tag em texto
        //localiza o inicio do  evento
        int localizacaoInicio = 1 + texto.indexOf("=",texto.indexOf(errado.getProcurado()));
        char ini = texto.substring(localizacaoInicio).trim().charAt(0);
        localizacaoInicio = 1 + texto.indexOf(ini, localizacaoInicio);
        int localizacaoFim;
        if(ini == '\"')     // localiza o final do evento   
         localizacaoFim = texto.indexOf("\"",  localizacaoInicio+1);
        else if(ini == '\'')
            localizacaoFim = texto.indexOf("\'",  localizacaoInicio+1);
        else{
        	log.info("eventosTags: " + texto);
            localizacaoInicio--;
            localizacaoFim = texto.indexOf(")", localizacaoInicio)+1;
        }
        texto = tag.getTexto();//volta com o texto original
        texto = texto.substring(0, texto.length() - 1) + " " + procurado
                + "=\"" + texto.substring(localizacaoInicio, localizacaoFim)
                + "\">";// pega a ação e atribui o evento correspondente com a
                        // mesma ação
//        tag.setPv3(-1);// muda a relação de erro
        tag.setTexto(texto); // repassa o texto
    }

    /**
     * Faz um loop com os erros e muda o pv3 daqueles que n&atilde;o forem
     * autom&aacute;ticos, guardando novamente os aqueles que s&atilde;o.
     * 
     * @throws ArrayIndexOutOfBoundsException
     */
    private void introduzir() throws ArrayIndexOutOfBoundsException {
    	//Array com os erros automaticos
        ArrayList<ArmazenaErroOuAvisoAntigo> erroAutomatico = new ArrayList<ArmazenaErroOuAvisoAntigo>();
        for (Iterator iter = errados.iterator(); iter.hasNext();) {
            ArmazenaErroOuAvisoAntigo tagInvalida = (ArmazenaErroOuAvisoAntigo) iter.next();
            if (!TokenNucleo.contem(tagInvalida.getPv3())) {
                tagArray[tagInvalida.getPosicao().getLinha()][tagInvalida
                        .getPosicao().getColuna()].setPv3(tagInvalida.getPv3());// mudança
                                                                                // de
                                                                                // PV3
            }
            else erroAutomatico.add(tagInvalida);
        }
        errados = erroAutomatico;

    }

}
