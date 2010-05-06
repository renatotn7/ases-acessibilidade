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

package br.org.acessobrasil.silvinha2.projetodosite;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

/**
 * Classe que guarda a configuração da avaliação. <br />
 * Ex: WCAG ou EMAG Prioridades níveis de diretório<br />
 * Serve para evitar a perda da configuração depois de iniciar a avaliação, parar e continuar depois 
 * @author Fabio Issamu Oshiro
 */
public class ConfiguracaoDaAvaliacao {
	/**
	 * Local onde o arquivo de configuração é guardado
	 */
	private String pathHD_temp = "temp/";

	private String fileName = "avaliacaoConf";

	private int wcag_emag;

	private boolean mostraPrioridade1;

	private boolean mostraPrioridade2;

	private boolean mostraPrioridade3;

	private String url;
	
	/**
	 * Guarda os níveis de diretório a serem seguidos
	 */
	private int niveis = 1;

	/**
	 * Seta como Wcag
	 */
	public void setWcag() {
		wcag_emag = 1;
	}

	/**
	 * Seta como Emag
	 */
	public void setEmag() {
		wcag_emag = 2;
	}

	/**
	 * Retorna o inteiro correspondente a regra
	 */
	public int getIntRegraWcagEmag() {
		return wcag_emag;
	}

	/**
	 * Retorna a string da regra
	 */
	public String getStrRegraWcagEmag() {
		switch (wcag_emag) {
		case 1:
			return "WCAG";
		case 2:
			return "EMAG";
		}
		return "";
	}

	/**
	 * @return true caso seja para mostrar erros e avisos de prioridade 1
	 */
	public boolean getMostraP1() {
		return mostraPrioridade1;
	}

	/**
	 * Grava se é para mostrar a prioridade 1
	 */
	public void setMostraP1(boolean mostraPrioridade1) {
		this.mostraPrioridade1 = mostraPrioridade1;
	}

	/**
	 * @return true caso seja para mostrar erros e avisos de prioridade 2
	 */
	public boolean getMostraP2() {
		return mostraPrioridade2;
	}

	/**
	 * Grava se é para mostrar erros e avisos de prioridade 2
	 */
	public void setMostraP2(boolean mostraPrioridade2) {
		this.mostraPrioridade2 = mostraPrioridade2;
	}

	/**
	 * @return true caso seja para mostrar erros e avisos de prioridade 3
	 */
	public boolean getMostraP3() {
		return mostraPrioridade3;
	}

	/**
	 * Grava se é para mostrar erros e avisos de prioridade 3
	 */
	public void setMostraP3(boolean mostraPrioridade3) {
		this.mostraPrioridade3 = mostraPrioridade3;
	}

	/**
	 * @return Retorna os níveis de diretório a serem seguidos
	 */
	public int getNiveis() {
		return niveis;
	}

	/**
	 * Guarda os níveis de diretório a serem seguidos
	 * 
	 * @param niveis
	 */
	public void setNiveis(int niveis) {
		this.niveis = niveis;
	}

	/**
	 * Método que carrega as configurações do diretório temporário
	 */
	public void loadConf() {
		File tmpFile = new File(this.pathHD_temp + fileName + ".xml");
		Document doc;
		try {
			DocumentBuilder db;
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(tmpFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return;
		} catch (SAXException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// popula os valores
		Node root = doc.getFirstChild();
		NodeList nl = root.getChildNodes();
		this.setMostraP1(false);
		this.setMostraP2(false);
		this.setMostraP3(false);
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			String nodeName = node.getNodeName();
			if (nodeName.equalsIgnoreCase("wcag_emag")) {
				this.wcag_emag = Integer.parseInt(node.getTextContent());
			} else if (nodeName.equalsIgnoreCase("mostraP1")) {
				if (node.getTextContent().equals("true")) {
					this.setMostraP1(true);
				} else {
					this.setMostraP1(false);
				}
			} else if (nodeName.equalsIgnoreCase("mostraP2")) {
				if (node.getTextContent().equals("true")) {
					this.setMostraP2(true);
				} else {
					this.setMostraP2(false);
				}
			} else if (nodeName.equalsIgnoreCase("mostraP3")) {
				if (node.getTextContent().equals("true")) {
					this.setMostraP3(true);
				} else {
					this.setMostraP3(false);
				}
			} else if (nodeName.equalsIgnoreCase("niveis")) {
				this.niveis = Integer.parseInt(node.getTextContent());
			} else if (nodeName.equalsIgnoreCase("url")) {
				this.url = node.getTextContent();
			}
		}
	}

	/**
	 * Método que grava as configurações do diretório temporário
	 */
	public void salvaConf() {

		File outFile = new File(this.pathHD_temp + fileName + ".xml");
		Document doc = this.serializarXml();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outFile);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Faz um xml com as informações da Avaliação
	 * 
	 * @return
	 */
	private Document serializarXml() {
		try {

			Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("confAvaliacao");
			Element wcag_emag = doc.createElement("wcag_emag");
			wcag_emag.setTextContent(String.valueOf(this.wcag_emag));
			root.appendChild(wcag_emag);

			Element mostraPrioridade1 = doc.createElement("mostraP1");
			mostraPrioridade1.setTextContent(String.valueOf(this.mostraPrioridade1));
			root.appendChild(mostraPrioridade1);

			Element mostraPrioridade2 = doc.createElement("mostraP2");
			mostraPrioridade2.setTextContent(String.valueOf(this.mostraPrioridade2));
			root.appendChild(mostraPrioridade2);

			Element mostraPrioridade3 = doc.createElement("mostraP3");
			mostraPrioridade3.setTextContent(String.valueOf(this.mostraPrioridade3));
			root.appendChild(mostraPrioridade3);

			Element niveis = doc.createElement("niveis");
			niveis.setTextContent(String.valueOf(this.niveis));
			root.appendChild(niveis);

			Element e_url = doc.createElement("url");
			e_url.setTextContent(String.valueOf(this.url));
			root.appendChild(e_url);
			
			doc.appendChild(root);
			return doc;
		} catch (ParserConfigurationException pce) {
			return null;
		}
	}

	/**
	 * Retorna as configurações como Properties por questões de compatibilidade
	 * 
	 * @return Properties
	 */
	public Properties getAsProperties() {
		Properties props = new Properties();
		props.setProperty("tipo_avaliacao", String.valueOf(this.wcag_emag));
		props.setProperty("niveis", String.valueOf(niveis));
		props.setProperty("prioridade1", String.valueOf(mostraPrioridade1));
		props.setProperty("prioridade2", String.valueOf(mostraPrioridade2));
		props.setProperty("prioridade3", String.valueOf(mostraPrioridade3));
		props.setProperty("url", String.valueOf(url));
		return props;
	}
	
	/**
	 * Para debug
	 */
	public void printConf(){
		System.out.print("tipo_avaliacao="+ String.valueOf(this.wcag_emag)+"\n");
		System.out.print("niveis="+ String.valueOf(this.niveis)+"\n");
		System.out.print("mostraPrioridade1="+ String.valueOf(this.mostraPrioridade1)+"\n");
		System.out.print("mostraPrioridade2="+ String.valueOf(this.mostraPrioridade2)+"\n");
		System.out.print("mostraPrioridade3="+ String.valueOf(this.mostraPrioridade3)+"\n");
	}

	/**
	 * Url que o usuário digitou para fazer a avaliação
	 * @return String
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Url que o usuário digitou para fazer a avaliação
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
