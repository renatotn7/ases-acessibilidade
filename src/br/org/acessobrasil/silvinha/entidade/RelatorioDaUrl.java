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

package br.org.acessobrasil.silvinha.entidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.org.acessobrasil.ases.persistencia.BancoSite;
import br.org.acessobrasil.ases.persistencia.SingBancoSite;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.util.Normalizador;
import br.org.acessobrasil.silvinha2.projetodosite.ProjetoDoSite;
import br.org.acessobrasil.silvinha2.util.G_File;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
/**
 * Representação do relatório da página 
 *
 */
public class RelatorioDaUrl {

	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");

	private String url;

	Statement statement;

	private boolean mostraP1;

	private boolean mostraP2;

	private boolean mostraP3;

	private Integer errosPrioridade1;

	private Integer errosPrioridade2;

	private Integer errosPrioridade3;

	private Integer avisosPrioridade1;

	private Integer avisosPrioridade2;

	private Integer avisosPrioridade3;

	private HashSet<PontoVerificacao> listaErrosP1;

	private HashSet<PontoVerificacao> listaErrosP2;

	private HashSet<PontoVerificacao> listaErrosP3;

	private HashSet<PontoVerificacao> listaAvisosP1;

	private HashSet<PontoVerificacao> listaAvisosP2;

	private HashSet<PontoVerificacao> listaAvisosP3;

	public int profundidade;


	public String hashCodeString;

	private int pagIdBanco;

	private int portalIdBanco;

	/**
	 * área do HD para salvar as páginas
	 */
	public final static String pathHD = "temp/";
	
	/**
	 * área do HD para salvar as páginas
	 */
	private final static String pathHD_temp = "temp/";

	public Integer hashCode;

	public RelatorioDaUrl() { // inicializa apenas
		url = "";
		mostraP1 = true;
		mostraP2 = true;
		mostraP3 = true;
		errosPrioridade1 = 0;
		errosPrioridade2 = 0;
		errosPrioridade3 = 0;
		avisosPrioridade1 = 0;
		avisosPrioridade2 = 0;
		avisosPrioridade3 = 0;
		listaErrosP1 = new HashSet<PontoVerificacao>(); // entre sinais de
		// comparação esta uma
		// classe
		// predominantemente
		listaErrosP2 = new HashSet<PontoVerificacao>(); // mantenedora de dados
		listaErrosP3 = new HashSet<PontoVerificacao>(); // 114 vezes mais rapido
		// que
		listaAvisosP1 = new HashSet<PontoVerificacao>(); // um arraylist
		listaAvisosP2 = new HashSet<PontoVerificacao>(); // ou seja, guarda
		// coleções de
		// objetos chamados
		listaAvisosP3 = new HashSet<PontoVerificacao>(); // pontos de
		// verificação
		hashCode = this.hashCode();
		hashCodeString = hashCode.toString();

	}

	// GETTERS AND SETTERS
	/**
	 * @return Returns the avisosPrioridade1.
	 */
	public Integer getAvisosPrioridade1() {
		return avisosPrioridade1;
	}

	/**
	 * @param avisosPrioridade1
	 *            The avisosPrioridade1 to set.
	 */
	public void setAvisosPrioridade1(int avisosPrioridade1) {
		this.avisosPrioridade1 = avisosPrioridade1;
	}

	/**
	 * @return Returns the avisosPrioridade2.
	 */
	public Integer getAvisosPrioridade2() {
		return avisosPrioridade2;
	}

	/**
	 * @param avisosPrioridade2
	 *            The avisosPrioridade2 to set.
	 */
	public void setAvisosPrioridade2(int avisosPrioridade2) {
		this.avisosPrioridade2 = avisosPrioridade2;
	}

	/**
	 * @return Returns the avisosPrioridade3.
	 */
	public Integer getAvisosPrioridade3() {
		return avisosPrioridade3;
	}

	/**
	 * @param avisosPrioridade3
	 *            The avisosPrioridade3 to set.
	 */
	public void setAvisosPrioridade3(int avisosPrioridade3) {
		this.avisosPrioridade3 = avisosPrioridade3;
	}

	/**
	 * @return Returns the errosPrioridade1.
	 */
	public Integer getErrosPrioridade1() {
		return errosPrioridade1;
	}

	/**
	 * @param errosPrioridade1
	 *            The errosPrioridade1 to set.
	 */
	public void setErrosPrioridade1(int errosPrioridade1) {
		//System.out.println("Relatorio da Url, errosPrioridade1:" + errosPrioridade1);
		this.errosPrioridade1 = errosPrioridade1;
	}

	/**
	 * @return Returns the errosPrioridade2.
	 */
	public Integer getErrosPrioridade2() {
		return errosPrioridade2;
	}

	/**
	 * @param errosPrioridade2
	 *            The errosPrioridade2 to set.
	 */
	public void setErrosPrioridade2(int errosPrioridade2) {
		//System.out.println("Relatorio da Url, errosPrioridade2:" + errosPrioridade2);
		this.errosPrioridade2 = errosPrioridade2;
	}

	/**
	 * @return Returns the errosPrioridade3.
	 */
	public Integer getErrosPrioridade3() {
		return errosPrioridade3;
	}

	/**
	 * @param errosPrioridade3
	 *            The errosPrioridade3 to set.
	 */
	public void setErrosPrioridade3(int errosPrioridade3) {
		this.errosPrioridade3 = errosPrioridade3;
	}

	/**
	 * @return Returns the listaAvisosP1.
	 */
	public HashSet<PontoVerificacao> getListaAvisosP1() {
		return listaAvisosP1;
	}

	/**
	 * @param listaAvisosP1
	 *            The listaAvisosP1 to set.
	 */
	public void setListaAvisosP1(HashSet<PontoVerificacao> listaAvisosP1) {
		this.listaAvisosP1 = listaAvisosP1;
	}

	/**
	 * @return Returns the listaAvisosP2.
	 */
	public HashSet<PontoVerificacao> getListaAvisosP2() {
		return listaAvisosP2;
	}

	/**
	 * @param listaAvisosP2
	 *            The listaAvisosP2 to set.
	 */
	public void setListaAvisosP2(HashSet<PontoVerificacao> listaAvisosP2) {
		this.listaAvisosP2 = listaAvisosP2;
	}

	/**
	 * @return Returns the listaAvisosP3.
	 */
	public HashSet<PontoVerificacao> getListaAvisosP3() {
		return listaAvisosP3;
	}

	/**
	 * @param listaAvisosP3
	 *            The listaAvisosP3 to set.
	 */
	public void setListaAvisosP3(HashSet<PontoVerificacao> listaAvisosP3) {
		this.listaAvisosP3 = listaAvisosP3;
	}

	/**
	 * @return Returns the listaErrosP1.
	 */
	public HashSet<PontoVerificacao> getListaErrosP1() {
		return listaErrosP1;
	}

	/**
	 * @param listaErrosP1
	 *            The listaErrosP1 to set.
	 */
	public void setListaErrosP1(HashSet<PontoVerificacao> listaErrosP1) {
		this.listaErrosP1 = listaErrosP1;
	}

	/**
	 * @return Returns the listaErrosP2.
	 */
	public HashSet<PontoVerificacao> getListaErrosP2() {
		return listaErrosP2;
	}

	/**
	 * @param listaErrosP2
	 *            The listaErrosP2 to set.
	 */
	public void setListaErrosP2(HashSet<PontoVerificacao> listaErrosP2) {
		this.listaErrosP2 = listaErrosP2;
	}

	/**
	 * @return Returns the listaErrosP3.
	 */
	public HashSet<PontoVerificacao> getListaErrosP3() {
		return listaErrosP3;
	}

	/**
	 * @param listaErrosP3
	 *            The listaErrosP3 to set.
	 */
	public void setListaErrosP3(HashSet<PontoVerificacao> listaErrosP3) {
		this.listaErrosP3 = listaErrosP3;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            The url to set.
	 */

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return Returns the conteudo.
	 */
	public StringBuilder getConteudo() {
		G_File arqT = new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto()+ "/reparo/temp/" + this.hashCodeString);
		if (arqT.exists()){
			return new StringBuilder(arqT.read());
		}else{
			G_File arq = new G_File(pathHD + this.hashCodeString);
			if (arq.exists()) {
				//System.err.println("arquivo não temporario em uso:"+ this.hashCodeString);
				return new StringBuilder(arq.read());
			}
		}
		//não deve chegar aqui
		StringBuilder sb = new StringBuilder();
		StringBuilder sbd = new StringBuilder();

		final int mb = 1024;

		if (profundidade > 5) {

			File file = new File(getUrl());

			FileInputStream fis = null;

			try {
				// JOptionPane.showMessageDialog(null,"arq = " +
				// relatorio.getUrl());

				// JOptionPane.showMessageDialog(null,"fileexist");
				if (file.exists()) {

					fis = new FileInputStream(file);

					byte[] dados = new byte[mb];
					int bytesLidos = 0;

					while ((bytesLidos = fis.read(dados)) > 0) {
						sb.append(new String(dados, 0, bytesLidos));
					}

					fis.close();
				} else {
					return null;
				}

			} catch (Exception e) {
				log.error(e);
				return null;
			}

			finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception e) {
					}
				}

			}

			sbd.append(sb.toString().trim());
		}

		else {

			File file = new File(pathHD + this.hashCode());

			FileInputStream fis = null;
			ObjectInputStream ois = null;
			try {

				if (file.exists()) {
					fis = new FileInputStream(file);
					ois = new ObjectInputStream(fis);
					sbd = new StringBuilder((String) ois.readObject());
				} else {
					return null;
				}
			} catch (Exception f) {
				log.error(f);
				return null;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception f) {
					}
				}
				if (ois != null) {
					try {
						ois.close();
					} catch (Exception f) {
					}
				}
			}
		}

		return sbd;

	}
	

	/**
	 * @param conteudo
	 *            The conteudo to set.
	 */
	public void setConteudo(StringBuilder conteudo) {
		try {
			statement.execute("UPDATE Pagina SET hashCode='"+ hashCodeString + "' WHERE idPagina=" + (pagIdBanco));
		} catch(NullPointerException e){
			//Normalmente o statment não está ok
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			G_File arq = new G_File(pathHD + this.hashCodeString);
			arq.write(conteudo.toString());
		}
	}

	/**
	 * @return Returns the mostraP1.
	 */
	public boolean mostraP1() {
		return mostraP1;
	}

	/**
	 * @param mostraP1
	 *            The mostraP1 to set.
	 */
	public void setMostraP1(boolean mostraP1) {
		this.mostraP1 = mostraP1;
	}

	/**
	 * @return Returns the mostraP2.
	 */
	public boolean mostraP2() {
		return mostraP2;
	}

	/**
	 * @param mostraP2
	 *            The mostraP2 to set.
	 */
	public void setMostraP2(boolean mostraP2) {
		this.mostraP2 = mostraP2;
	}

	/**
	 * @return Returns the mostraP3.
	 */
	public boolean mostraP3() {
		return mostraP3;
	}

	/**
	 * @param mostraP3
	 *            The mostraP3 to set.
	 */
	public void setMostraP3(boolean mostraP3) {
		this.mostraP3 = mostraP3;
	}

	/**
	 * Retorna um documento serializado em XML
	 * 
	 * @return documento
	 */
	public Document serializarXml() {
		try {

			Document doc = DocumentBuilderFactoryImpl.newInstance()
					.newDocumentBuilder().newDocument();
			Element root = doc.createElement("relatorio");

			Element url = doc.createElement("url");
			url.setTextContent(this.getUrl());
			root.appendChild(url);

			Element ep1 = doc.createElement("errosp1");
			ep1.setTextContent(String.valueOf(this.getErrosPrioridade1()));
			root.appendChild(ep1);

			Element ep2 = doc.createElement("errosp2");
			ep2.setTextContent(String.valueOf(this.getErrosPrioridade2()));
			root.appendChild(ep2);

			Element ep3 = doc.createElement("errosp3");
			ep3.setTextContent(String.valueOf(this.getErrosPrioridade3()));
			root.appendChild(ep3);

			Element ap1 = doc.createElement("avisosp1");
			ap1.setTextContent(String.valueOf(this.getAvisosPrioridade1()));
			root.appendChild(ap1);

			Element ap2 = doc.createElement("avisosp2");
			ap2.setTextContent(String.valueOf(this.getAvisosPrioridade2()));
			root.appendChild(ap2);

			Element ap3 = doc.createElement("avisosp3");
			ap3.setTextContent(String.valueOf(this.getAvisosPrioridade3()));
			root.appendChild(ap3);

			Element conteudo = doc.createElement("conteudo");
			String[] cLinhas = this.getConteudo().toString().split("\n");
			Element clinha;
			for (int i = 0; i < cLinhas.length; i++) {
				String linha = Normalizador.normalizar(i + 1);
				clinha = doc.createElement("clinha");
				clinha.setAttribute("nro", linha);
				clinha.setTextContent(cLinhas[i] + "\n");
				conteudo.appendChild(clinha);
			}
			root.appendChild(conteudo);

			if (this.mostraP1) {
				root.appendChild(doc.createElement("mostrap1"));
				if (this.errosPrioridade1 > 0) {
					root.appendChild(geraLista("listaep1", this
							.getListaErrosP1(), doc));
				}
				if (this.avisosPrioridade1 > 0) {
					root.appendChild(geraLista("listaap1", this
							.getListaAvisosP1(), doc));
				}
			}

			if (this.mostraP2) {
				root.appendChild(doc.createElement("mostrap2"));
				if (this.errosPrioridade2 > 0) {
					root.appendChild(geraLista("listaep2", this
							.getListaErrosP2(), doc));
				}
				if (this.avisosPrioridade2 > 0) {
					root.appendChild(geraLista("listaap2", this
							.getListaAvisosP2(), doc));
				}
			}

			if (this.mostraP3) {
				root.appendChild(doc.createElement("mostrap3"));
				if (this.errosPrioridade3 > 0) {
					root.appendChild(geraLista("listaep3", this
							.getListaErrosP3(), doc));
				}
				if (this.avisosPrioridade3 > 0) {
					root.appendChild(geraLista("listaap3", this
							.getListaAvisosP3(), doc));
				}
			}

			doc.appendChild(root);

			return doc;

		} catch (ParserConfigurationException pce) {
			return null;
		}
	}

	/**
	 * Gera um xml com os pvs
	 * 
	 * @param nomeElemento
	 * @param pvs
	 * @param doc
	 * @return
	 */
	private Element geraLista(String nomeElemento,
			HashSet<PontoVerificacao> pvs, Document doc) {

		Element lista = doc.createElement(nomeElemento);

		HashSet<PontoVerificacao> pontos = pvs;
		for (PontoVerificacao pv : pontos) {
			Element pontoVerificacao = doc.createElement("pontoverificacao");

			pontoVerificacao.setAttribute("priori", String.valueOf(pv
					.getPrioridade()));
			pontoVerificacao.setAttribute("wcagemag", String.valueOf(pv
					.getWcagEmag()));

			Element gl = doc.createElement("gl");
			gl.setTextContent(String.valueOf(pv.getGl()));
			pontoVerificacao.appendChild(gl);

			Element cp = doc.createElement("cp");
			cp.setTextContent(String.valueOf(pv.getCp()));
			pontoVerificacao.appendChild(cp);

			Element regra = doc.createElement("id_regra");
			regra.setTextContent(String.valueOf(pv.getIdRegra()));
			pontoVerificacao.appendChild(regra);

			Element total = doc.createElement("total");
			int size = pv.getLinhas().size();
			String strSize = String.valueOf(size);
			total.setTextContent(!strSize.equals("0") ? strSize : "---");
			pontoVerificacao.appendChild(total);

			Element linhas = doc.createElement("linhas");
			ArrayList<Integer> arrayLinhas = pv.getLinhas();
			ArrayList<Integer> arrayColunas = pv.getColunas();
			ArrayList<Integer> arrayTagLength = pv.getTagLength();
			ArrayList<String> arrayAvOuErr = pv.getAvisoOuErro();
			Element linha;
			for (int i = 0; i < arrayLinhas.size(); i++) {
				linha = doc.createElement("linha");
				linha.setAttribute("col", arrayColunas.get(i).toString());
				linha.setAttribute("taglen", arrayTagLength.get(i).toString());
				linha.setAttribute("avisoerro", arrayAvOuErr.get(i).toString());
				String strLinha = Normalizador.normalizar(arrayLinhas.get(i));
				linha.setTextContent(strLinha);
				linhas.appendChild(linha);
			}
			pontoVerificacao.appendChild(linhas);
			lista.appendChild(pontoVerificacao);
		}
		return lista;
	}

	public static void gravaRelatorioEmHtml(File outFile, Document doc) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);

			File style = new File("relatorio.xsl");
			StreamSource styleSource = new StreamSource(style);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(styleSource);

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outFile);
			transformer.transform(source, result);

		} catch (Exception e) {
		}
	}

	public File geraArquivoRelatorioEmXml() {
		try {
			String fileName = String.valueOf(this.hashCode());
			File outFile = new File(pathHD_temp + fileName + ".xml"); // o
																		// gravador
																		// de
			// arquivos no diretorio
			// que não deve ser
			// gravado está aqui?
			Document doc = this.serializarXml();
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outFile);
			transformer.transform(source, result);
			return outFile;
		} catch (Exception e) {
			ExceptionDialog.showExceptionDialog("[RelatorioDaURL]: "
					+ e.getMessage());
			return null;
		}
	}

	public static RelatorioDaUrl lerArquivoRelatorioEmXml(Document doc) {
		RelatorioDaUrl relatorio = new RelatorioDaUrl();
		Node root = doc.getFirstChild();
		NodeList nl = root.getChildNodes();
		relatorio.setMostraP1(false);
		relatorio.setMostraP2(false);
		relatorio.setMostraP3(false);
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			String nodeName = node.getNodeName();
			if (nodeName.equalsIgnoreCase("url")) {
				relatorio.setUrl(node.getTextContent());
			} else if (nodeName.equalsIgnoreCase("errosp1")) {
				relatorio.setErrosPrioridade1(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("errosp2")) {
				relatorio.setErrosPrioridade2(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("errosp3")) {
				relatorio.setErrosPrioridade3(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("avisosp1")) {
				relatorio.setAvisosPrioridade1(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("avisosp2")) {
				relatorio.setAvisosPrioridade2(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("avisosp3")) {
				relatorio.setAvisosPrioridade3(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("mostrap1")) {
				relatorio.setMostraP1(true);
			} else if (nodeName.equalsIgnoreCase("mostrap2")) {
				relatorio.setMostraP2(true);
			} else if (nodeName.equalsIgnoreCase("mostrap3")) {
				relatorio.setMostraP3(true);
			} else if (nodeName.equalsIgnoreCase("conteudo")) {
				StringBuilder conteudo = new StringBuilder();
				NodeList nl2 = node.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					String linhaCodigo = nl2.item(j).getTextContent();
					linhaCodigo = linhaCodigo.endsWith("\n") ? linhaCodigo
							: linhaCodigo + "\n";
					conteudo.append(linhaCodigo);
				}
				relatorio.setConteudo(conteudo);
			} else if (nodeName.equalsIgnoreCase("listaep1")) {
				relatorio.setListaErrosP1(relatorio.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaep2")) {
				relatorio.setListaErrosP2(relatorio.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaep3")) {
				relatorio.setListaErrosP3(relatorio.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaap1")) {
				relatorio.setListaAvisosP1(relatorio.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaap2")) {
				relatorio.setListaAvisosP2(relatorio.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaap3")) {
				relatorio.setListaAvisosP3(relatorio.geraListaDeErros(node));
			}
		}
		return relatorio;
	}

	/**
	 * Popula um hashSet com os pontos de verificação passado em node
	 * 
	 * @param node
	 *            nó onde estão gravados os pontos de verificação
	 * @return
	 */
	private HashSet<PontoVerificacao> geraListaDeErros(Node node) {
		HashSet<PontoVerificacao> lista = new HashSet<PontoVerificacao>();
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			PontoVerificacao pv = new PontoVerificacao();
			Node pontoVerificacao = nl.item(i);
			NamedNodeMap nodePVatt = pontoVerificacao.getAttributes();
			int wcagemag = Integer.parseInt(nodePVatt.getNamedItem("wcagemag")
					.getTextContent());
			pv.setWcagEmag(wcagemag);
			NodeList children = pontoVerificacao.getChildNodes();
			for (int j = 0; j < children.getLength(); j++) {
				Node child = children.item(j);
				String childName = child.getNodeName();
				if (childName.equalsIgnoreCase("gl")) {
					pv.setGl(Integer.parseInt(child.getTextContent()));
				} else if (childName.equalsIgnoreCase("cp")) {
					pv.setCp(Integer.parseInt(child.getTextContent()));
				} else if (childName.equalsIgnoreCase("id_regra")) {
					pv.setIdRegra(Integer.parseInt(child.getTextContent()));
				} else if (childName.equalsIgnoreCase("linhas")) {
					pv.setLinhas(new ArrayList<Integer>());
					pv.setAvisoOuErro(new ArrayList<String>());
					pv.setColunas(new ArrayList<Integer>());
					pv.setTagLength(new ArrayList<Integer>());
					NodeList nodeLinhas = child.getChildNodes();
					for (int k = 0; k < nodeLinhas.getLength(); k++) {
						NamedNodeMap att = nodeLinhas.item(k).getAttributes();
						String strErrOuAv = att.getNamedItem("avisoerro")
								.getTextContent();
						int col = Integer.parseInt(att.getNamedItem("col")
								.getTextContent());
						int tagLen = Integer.parseInt(att
								.getNamedItem("taglen").getTextContent());
						int linha = Integer.parseInt(nodeLinhas.item(k)
								.getTextContent());
						pv.getLinhas().add(linha);
						pv.getAvisoOuErro().add(strErrOuAv);
						pv.getColunas().add(col);
						pv.getTagLength().add(tagLen);
					}
				}
			}
			lista.add(pv);
		}
		return lista;
	}

	/**
	 * Grava o relatório no HD em Xml na pasta cache
	 */
	public File geraArquivoRelatorioEmXml2() {
		try {
			File outFile = gravaXmlNoHD();
			limpaListasErrosAvisos();
			return outFile;
		} catch (Exception e) {
			ExceptionDialog.showExceptionDialog("[RelatorioDaURL]: "
					+ e.getMessage());
			return null;
		}
	}

	/**
	 * Limpa a lista de erros ou avisos
	 * 
	 */
	private void limpaListasErrosAvisos() {
		setListaErrosP1(null);
		setListaErrosP2(null);
		setListaErrosP3(null);
		setListaAvisosP1(null);
		setListaAvisosP2(null);
		setListaAvisosP3(null);
	}

	/**
	 * Grava o Xml deste relatório no HD
	 */
	public File gravaXmlNoHD() {
		try {
			String fileName = String.valueOf(this.hashCodeString);
			File outFile = new File(pathHD_temp + fileName + ".xml");
			Document doc = this.serializarXml();
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outFile);
			transformer.transform(source, result);

			return outFile;
		} catch (Exception e) {
			ExceptionDialog.showExceptionDialog("[RelatorioDaURL]: "
					+ e.getMessage());
			return null;
		}
	}

	/**
	 * Recupera o relatório do HD em Xml salvo na pasta cache
	 */
	public void recarregaArquivoRelatorioEmXml2() {
		this.recarregaArquivoRelatorioEmXml2(String.valueOf(this.hashCode()));
	}

	/**
	 * Recarrega um relatório do HD.
	 * 
	 * @param myHash
	 *            é o nome do arquivo
	 */
	public void recarregaArquivoRelatorioEmXml2(String myHash) {
		hashCodeString = myHash;
		File tmpFile = new File(pathHD_temp + hashCodeString + ".xml");
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
			if (nodeName.equalsIgnoreCase("url")) {
				this.setUrl(node.getTextContent());
			} else if (nodeName.equalsIgnoreCase("errosp1")) {
				this.setErrosPrioridade1(Integer
						.parseInt(node.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("errosp2")) {
				this.setErrosPrioridade2(Integer
						.parseInt(node.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("errosp3")) {
				this.setErrosPrioridade3(Integer
						.parseInt(node.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("avisosp1")) {
				this.setAvisosPrioridade1(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("avisosp2")) {
				this.setAvisosPrioridade2(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("avisosp3")) {
				this.setAvisosPrioridade3(Integer.parseInt(node
						.getTextContent()));
			} else if (nodeName.equalsIgnoreCase("mostrap1")) {
				this.setMostraP1(true);
			} else if (nodeName.equalsIgnoreCase("mostrap2")) {
				this.setMostraP2(true);
			} else if (nodeName.equalsIgnoreCase("mostrap3")) {
				this.setMostraP3(true);
			} else if (nodeName.equalsIgnoreCase("conteudo")) {
				StringBuilder conteudo = new StringBuilder();
				NodeList nl2 = node.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					String linhaCodigo = nl2.item(j).getTextContent();
					linhaCodigo = linhaCodigo.endsWith("\n") ? linhaCodigo
							: linhaCodigo + "\n";
					conteudo.append(linhaCodigo);
				}
				this.setConteudo(conteudo);
			} else if (nodeName.equalsIgnoreCase("listaep1")) {
				this.setListaErrosP1(this.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaep2")) {
				this.setListaErrosP2(this.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaep3")) {
				this.setListaErrosP3(this.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaap1")) {
				this.setListaAvisosP1(this.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaap2")) {
				this.setListaAvisosP2(this.geraListaDeErros(node));
			} else if (nodeName.equalsIgnoreCase("listaap3")) {
				this.setListaAvisosP3(this.geraListaDeErros(node));
			}
		}
	}

	public int getPagIdBanco() {
		return pagIdBanco;
	}

	public int getPortalIdBanco() {
		return portalIdBanco;
	}

}
