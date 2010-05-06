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

import org.w3c.dom.Attr;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Conversor de texto, ele transpõem o texto recebido para uma tag valida.
 * String que será retornado por essa classe
 * @author Acessibilidade Brasil.
 */

public class ConversorTexto {

    /**
     * Conversor de texto, ele transpõem o texto recebido para uma tag valida.
     * String que será retornado por essa classe
     */
    private StringBuilder texto = new StringBuilder();

    public String reEscrever(Node node) {

        if (node == null) { return ""; }

        short type = node.getNodeType();
        switch (type) {
            case Node.DOCUMENT_TYPE_NODE: { // tag de tipo de arquivo pesquisado
                DocumentType doctype = (DocumentType) node;
                texto.append("<!DOCTYPE ");
                texto.append(doctype.getName());
                String publicId = doctype.getPublicId();
                String systemId = doctype.getSystemId();
                if (publicId != null) {
                    texto.append(" PUBLIC '");
                    texto.append(publicId);
                    texto.append("' '");
                    texto.append(systemId);
                    texto.append("'");
                }
                else if (systemId != null) {
                    texto.append(" SYSTEM '");
                    texto.append(systemId);
                    texto.append("'");
                }
                String internalSubset = doctype.getInternalSubset();
                if (internalSubset != null) {
                    texto.append(" [\n");
                    texto.append(internalSubset);
                    texto.append("]");
                }
                texto.append(">\n");
                break;
            }

            case Node.ELEMENT_NODE: {// tag comum com os seus atributos
                texto.append("<");
                texto.append(node.getNodeName());
                Attr attrs[] = sortAttributes(node.getAttributes());
                for (int i = 0; i < attrs.length; i++) {
                    Attr attr = attrs[i];
                    texto.append(' ');
                    texto.append(attr.getNodeName());
                    texto.append("=\"");
                    normalizeAndPrint(attr.getNodeValue(), true);
                    texto.append('"');
                }
                texto.append('>');
                return texto.toString();
            }

            case Node.ENTITY_REFERENCE_NODE: {
                texto.append('&');
                texto.append(node.getNodeName());
                texto.append(';');
                return texto.toString();
            }

            case Node.CDATA_SECTION_NODE: { // texto que referencia um XML
                texto.append("<![CDATA[");
                texto.append(node.getNodeValue());
                texto.append("]]>");
                return texto.toString();
            }

            case Node.TEXT_NODE: { // tag de texto comum
                normalizeAndPrint(node.getNodeValue(), false);
                return texto.toString();
            }

            case Node.PROCESSING_INSTRUCTION_NODE: { // tag do PHP
                texto.append("<?");
                texto.append(node.getNodeName());
                String data = node.getNodeValue();
                if (data != null && data.length() > 0) {
                    texto.append(' ');
                    texto.append(data);
                }
                texto.append("?>");
                return texto.toString();
            }

            case Node.COMMENT_NODE: { // tag de comentário
                texto.append("<!--");
                String comment = node.getNodeValue();
                if (comment != null && comment.length() > 0) {
                    texto.append(comment);
                }
                texto.append("-->");
                return texto.toString();

            }
        }

        if (type == Node.ELEMENT_NODE) { // fechamento da tag
            texto.append("</");
            texto.append(node.getNodeName());
            texto.append('>');
            return texto.toString();
        }
        return texto.toString();

    }

    /**
     * Coletor de Atributos
     * 
     * @param attrs
     * @return array de atributos
     */
    private final Attr [] sortAttributes(NamedNodeMap attrs) {
   	 
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        return array;

    }

    /**
     * Analista de texto
     * 
     * @param s String pesquisada
     * @param isAttValue Valor dizendo se o caractere &eacute; de uma tag ou de texto comum .
     */
    private final void normalizeAndPrint(String s, boolean isAttValue) {
        if (s != null) {
        	for (int i = 0; i < s.length(); i++) {
        		char c = s.charAt(i);
        		normalizeAndPrint(c, isAttValue);
        	}
        }
    }

    /**
     * Adequa&ccedil;&atilde;o para caracteres especiais.
     * 
     * @param c
     *            Caracter pesquisado.
     * @param isAttValue
     *            Valor dizendo se o caractere &eacute; de uma tag ou de texto
     *            comum .
     */
    private final void normalizeAndPrint(char c, boolean isAttValue) {

        switch (c) {
            case '<': {
                texto.append("<");
                break;
            }
            case '>': {
                texto.append(">");
                break;
            }
            case '&': {
                texto.append("&");
                break;
            }
            case '"': {
                if (isAttValue) {
                    texto.append("&quot;");
                }
                else {
                    texto.append('"');
                }
                break;
            }
            case '\r': {
                texto.append("&#xD;");
                break;
            }
            default: {
                if (((c >= 0x01 && c <= 0x1F && c != 0x09 && c != 0x0A)
                        || (c >= 0x7F && c <= 0x9F) || c == 0x2028)
                        || isAttValue && (c == 0x09 || c == 0x0A)) {
                    texto.append("&#x");
                    texto.append(Integer.toHexString(c).toUpperCase());
                    texto.append(";");
                }
                else {
                    texto.append(c);
                }
            }
        }
    }

}
