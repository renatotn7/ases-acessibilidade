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

package br.org.acessobrasil.silvinha2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Leitor genérico de arquivos XML
 * @author fabio
 *
 */
public class G_Xml {
	public Document dom;

	private Node currentNode;
	
	/**
	 * Informa o caminho do arquivo Xml
	 * @param fullPath
	 */
	public G_Xml(String fullPath){
		this.open(fullPath);
	}
	
	public G_Xml(){
		
	}
	public void open(String fullPath) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder domBuilder;
		domFactory.setValidating(false);
		// domFactory.setSchema(null);
		try {
			domBuilder = domFactory.newDocumentBuilder();
			InputSource myXML = this.getXML(fullPath);
			if (myXML!=null) this.dom = domBuilder.parse(myXML);			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.print("ParserConfigurationException\n");
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InputSource getXML(String fullpath) {
		StringBuilder buf = new StringBuilder();
		File file = new File(fullpath);
		FileReader reader;
		if (!file.exists()){
			return null;
		}
		try {
			reader = new FileReader(file);
			BufferedReader leitor = new BufferedReader(reader);
			while (leitor.ready()) {
				buf.append(leitor.readLine() + "\n");
			}
			leitor.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String retorno = buf.toString();
		String retAux = retorno.toLowerCase();
		int ini = retAux.indexOf("<!doctype");
		if (ini != -1) {
			int fim = retorno.indexOf(">", ini);
			retorno = retorno.substring(fim + 1);
		}
		//
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(retorno)); 
		return inStream;
	}

	public void gotoFirstChild() {

		NodeList c = currentNode.getChildNodes();
		int tot = c.getLength();
		if (tot > 0) {
			currentNode = c.item(0);
			if (currentNode.getNodeName() == "#text") {
				currentNode = c.item(1);
			}
		}
		// System.out.print("gotoFirstChild\ntot="+tot+"\n"+currentNode.getNodeName());
	}

	public void gotoNext() {
		currentNode = currentNode.getNextSibling();
		if (currentNode.getNodeName() == "#text") {
			gotoNext();
		}
	}

	public String getCurrentTagName() {
		return currentNode.getNodeName();
	}

	public String getAtributo(String v) {
		NamedNodeMap nm = currentNode.getAttributes();
		if (nm == null)
			return "";
		int j, tot2 = nm.getLength();
		for (j = 0; j < tot2; j++) {
			if (nm.item(j).getNodeName() == v) {
				return nm.item(j).getNodeValue();
			}
		}
		return "";
	}

	public void gotoTagId(String tagId) {
		System.out.print("gotoTagId('" + tagId + "')\n");
		currentNode = buscaTagId(this.dom.getFirstChild(), tagId);
	}

	/*
	 * 
	 */
	private Node buscaTagId(Node xml_node, String tagId) {
		System.out.print("buscaTagId('" + xml_node.getNodeName() + "','"
				+ tagId + "')\n");
		NodeList nl = xml_node.getChildNodes();
		int i, tot = nl.getLength();
		//System.out.print("tot=" + tot + "\n");
		for (i = 0; i < tot; i++) {
			Node cn = nl.item(i);
			//System.out.print("cn.name=" + cn.getNodeName() + "\n");
			if (cn.hasAttributes()) {
				NamedNodeMap at = cn.getAttributes();
				int j, tot2 = at.getLength();
				for (j = 0; j < tot2; j++) {

					if (at.item(j).getNodeName().toLowerCase() == "id") {
						String node_id=at.item(j).getNodeValue().toString();
						//Ini - Só para não acharem que sou louco
						//System.out.print("---- at " + at.item(j).getNodeName()
						//		+ "='"+node_id+"'=='"+tagId+"'?");
						//if (node_id == tagId) {
						//Fim - Só para não acharem que sou louco
						if(node_id.equals(tagId)){
							//System.out.print(" S\n<--RETORNO\n");
							return cn;
						}else{
							//System.out.print(" N\n");
						}
					}
				}
			}
			// busca nos Childs
			//System.out.print("--" + cn.getNodeName() + " hasChilds="
			//		+ cn.hasChildNodes() + "\n");
			if (cn.hasChildNodes()) {
				Node res = buscaTagId(cn, tagId);
				if (res != null) {
					return res;
				}
			}

		}
		return null;
	}
	
	public String getText(){
		return currentNode.getTextContent();
	}
	
	// */
	public void gotoTagName(String tagname, int pos) {
		NodeList b = this.dom.getElementsByTagName(tagname);
		currentNode = b.item(pos);
		/*
		 * NodeList c=currentNode.getChildNodes(); int tot=c.getLength(); if
		 * (tot>0){ currentChildNode=c.item(Child_index); currentChildNode. }
		 * 
		 * int i; for (i = 0; i < tot; i++) { Node inNode=c.item(i);
		 * System.out.print("'"+inNode.getNodeName()+"'\n\n"); } /* NamedNodeMap
		 * nm = b.item(pos).getAttributes();
		 * 
		 * int j,tot2 = nm.getLength(); for (j = 0; j < tot2; j++) { String val =
		 * nm.item(j).getNodeValue(); System.out.print(val + "\n"); }
		 */
	}

	public void teste() {
		NodeList b = this.dom.getElementsByTagName("meta");
		int tot = b.getLength();
		int i, j;
		for (i = 0; i < tot; i++) {
			NamedNodeMap nm = b.item(i).getAttributes();
			int tot2 = nm.getLength();
			for (j = 0; j < tot2; j++) {
				String val = nm.item(j).getNodeValue();
				System.out.print(val + "\n");
			}
		}
	}
	
	/**
	 * Gera um Xml do objeto
	 * @param obj
	 */
	public void geraXml(Object obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			String tipo=field.getType().toString();
			String nome=field.getName().toString();
			System.out.print("nome="+nome+"\n");
			System.out.print("tipo="+field.getType()+"\n");
			if(tipo.equals("class java.lang.String")){
				try {
					System.out.print("valor="+field.get(obj)+"\n");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}