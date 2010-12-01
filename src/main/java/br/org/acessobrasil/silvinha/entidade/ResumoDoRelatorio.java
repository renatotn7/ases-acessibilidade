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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha2.util.G_File;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
/**
 * Representação do resumo do relatório 
 *
 */
public class ResumoDoRelatorio {
	/**
	 * Guarda o índice corrente para geração da página
	 */
	private static int indicePg = 1;

	/**
	 * Guarda o total de links em todas as páginas
	 */
	private static int totalLinks = 0;

	/**
	 * Guarda o número de links por página
	 */
	public static final int results_pp = 50;

	/**
	 * Guarda o total de páginas
	 */
	private static int totPage = 1;

	public static ArrayList<RelatorioDaUrl> relatorios = new ArrayList<RelatorioDaUrl>();

	private boolean gravaCompleto;

	/**
	 * O conjunto de Op&ccedil&otilde;es selecionadas no in&iacute;cio da
	 * avalia&ccedil;&atilde;o. Cont&eacute;m as seguintes
	 * informa&ccedil;&otilde;es:
	 * <ul>
	 * <li>Tipo de avalia&ccedil;&atilde;o (WCAG / EGOV);</li>
	 * <li>Prioridades a serem avaliadas (Prioridade 1, 2 e/ou 3); e</li>
	 * <li>N&iacute;vel de aprofundamento no site (Sub-dom&iacute;nios
	 * avaliados).</li>
	 * </ul>
	 */
	private Properties opcoes;

	public ResumoDoRelatorio(ArrayList<RelatorioDaUrl> relatorios,
			Properties opcoes) {
		ResumoDoRelatorio.relatorios = relatorios;
		this.opcoes = opcoes;
		getTotPage();
	}

	public ResumoDoRelatorio(Properties opcoes) {
		this.opcoes = opcoes;
		getTotPage();
	}

	/**
	 * Reinicia as variáveis
	 */
	public static void reinicia() {
		indicePg = 1;
		totalLinks = 0;
		totPage = 1;
		// limpar os temps
		File cacheDir = new File("cache");
		if (cacheDir.isDirectory()) {
			for (File file : cacheDir.listFiles()) {
				file.delete();
			}
		}
		cacheDir = new File("temp");
		if (cacheDir.isDirectory()) {
			for (File file : cacheDir.listFiles()) {
				file.delete();
			}
		}
	}

	/**
	 * @return Returns the relatorios.
	 */
	public ArrayList<RelatorioDaUrl> getRelatorios() {
		return relatorios;
	}

	/**
	 * @param relatorios
	 *            The relatorios to set.
	 */
	public void setRelatorios(ArrayList<RelatorioDaUrl> relatorios) {
		ResumoDoRelatorio.relatorios = relatorios;
	}

	/**
	 * @return Returns the opcoes.
	 */
	public Properties getOpcoes() {
		return opcoes;
	}

	/**
	 * @param opcoes
	 *            The opcoes to set.
	 */
	public void setOpcoes(Properties opcoes) {
		this.opcoes = opcoes;
	}

	/**
	 * @return Returns the gravaCompleto.
	 */
	public boolean isGravaCompleto() {
		return gravaCompleto;
	}

	/**
	 * @param gravaCompleto
	 *            The gravaCompleto to set.
	 */
	public void setGravaCompleto(boolean gravaCompleto) {
		this.gravaCompleto = gravaCompleto;
	}

	public Document geraResumoEmXml() { // usado para fazer gravação .sil
		try {

			Document doc = DocumentBuilderFactoryImpl.newInstance()
					.newDocumentBuilder().newDocument();
			Element root = doc.createElement("resumo");

			Element completo = doc.createElement("completo");
			if (this.isGravaCompleto()) {
				completo.setTextContent("sim");
			} else {
				completo.setTextContent("nao");
			}
			root.appendChild(completo);

			Element tipoAvaliacao = doc.createElement("tipo_avaliacao");
			tipoAvaliacao.setTextContent(opcoes.getProperty("tipo_avaliacao"));
			root.appendChild(tipoAvaliacao);

			Element niveis = doc.createElement("niveis");
			niveis.setTextContent(opcoes.getProperty("niveis"));
			root.appendChild(niveis);

			Element pri1 = doc.createElement("pri1");
			pri1.setTextContent(opcoes.getProperty("prioridade1"));
			root.appendChild(pri1);

			Element pri2 = doc.createElement("pri2");
			pri2.setTextContent(opcoes.getProperty("prioridade2"));
			root.appendChild(pri2);

			Element pri3 = doc.createElement("pri3");
			pri3.setTextContent(opcoes.getProperty("prioridade3"));
			root.appendChild(pri3);

			for (RelatorioDaUrl relatorio : relatorios) {
				Element rel = doc.createElement("relatorio");

				Element url = doc.createElement("url");
				url.setTextContent(relatorio.getUrl());
				rel.appendChild(url);

				Element ep1 = doc.createElement("errosp1");
				ep1.setTextContent(String.valueOf(relatorio
						.getErrosPrioridade1()));
				rel.appendChild(ep1);

				Element ep2 = doc.createElement("errosp2");
				ep2.setTextContent(String.valueOf(relatorio
						.getErrosPrioridade2()));
				rel.appendChild(ep2);

				Element ep3 = doc.createElement("errosp3");
				ep3.setTextContent(String.valueOf(relatorio
						.getErrosPrioridade3()));
				rel.appendChild(ep3);

				Element ap1 = doc.createElement("avisosp1");
				ap1.setTextContent(String.valueOf(relatorio
						.getAvisosPrioridade1()));
				rel.appendChild(ap1);

				Element ap2 = doc.createElement("avisosp2");
				ap2.setTextContent(String.valueOf(relatorio
						.getAvisosPrioridade2()));
				rel.appendChild(ap2);

				Element ap3 = doc.createElement("avisosp3");
				ap3.setTextContent(String.valueOf(relatorio
						.getAvisosPrioridade3()));
				rel.appendChild(ap3);

				root.appendChild(rel);
			}

			doc.appendChild(root);

			return doc;

		} catch (ParserConfigurationException pce) {
			return null;
		}
	}

	public static File gravaResumoEmXml(File outFile, Document doc) {
		try {
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
			return null;
		}
	}

	public File geraArquivoResumoEmXml() {
		try {
			File outFile = new File("resumo.xml");
			Document doc = this.geraResumoEmXml();

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
			ExceptionDialog.showExceptionDialog(e.getMessage());
			return null;
		}
	}

	public ArrayList<File> gerarArquivosRelatorios() {
		ArrayList<File> arquivos = new ArrayList<File>();
		arquivos.add(this.geraArquivoResumoEmXml());
		if (this.isGravaCompleto()) {
			for (RelatorioDaUrl relatorio : relatorios) {
				File file = relatorio.geraArquivoRelatorioEmXml(); // retorna o
				// arquivo
				// serializado
				if (file != null) {
					arquivos.add(file);
				}
			}
		}

		return arquivos;
	}

	public ArrayList<File> gerarRelatorioTemporarios(RelatorioDaUrl relatorio) {
		gravaCompleto = true;
		ArrayList<File> arquivos = new ArrayList<File>();
		arquivos.add(this.geraArquivoResumoEmXml());

		File file = relatorio.geraArquivoRelatorioEmXml();
		if (file != null) {
			arquivos.add(file);
		}

		return arquivos;
	}

	public static ResumoDoRelatorio lerArquivoResumoEmXml(Document doc) {
		ArrayList<RelatorioDaUrl> relatorios = new ArrayList<RelatorioDaUrl>();
		Properties opcoes = lerOpcoesDoXml(doc);
		ResumoDoRelatorio resumo = new ResumoDoRelatorio(relatorios, opcoes);
		Node root = doc.getFirstChild();
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeName().equalsIgnoreCase("completo")) {
				String completo = node.getTextContent();
				if (completo != null && completo.equalsIgnoreCase("sim")) {
					resumo.setGravaCompleto(true);
				} else {
					resumo.setGravaCompleto(false);
				}
			} else if (node.getNodeName().equalsIgnoreCase("relatorio")) {
				RelatorioDaUrl relatorio = new RelatorioDaUrl();
				NodeList nl2 = node.getChildNodes();
				for (int j = 0; j < nl2.getLength(); j++) {
					Node child = nl2.item(j);
					String childName = child.getNodeName();
					if (childName.equalsIgnoreCase("url")) {
						relatorio.setUrl(child.getTextContent());
					} else if (childName.equalsIgnoreCase("errosp1")) {
						relatorio.setErrosPrioridade1(Integer.parseInt(child
								.getTextContent()));
					} else if (childName.equalsIgnoreCase("errosp2")) {
						relatorio.setErrosPrioridade2(Integer.parseInt(child
								.getTextContent()));
					} else if (childName.equalsIgnoreCase("errosp3")) {
						relatorio.setErrosPrioridade3(Integer.parseInt(child
								.getTextContent()));
					} else if (childName.equalsIgnoreCase("avisosp1")) {
						relatorio.setAvisosPrioridade1(Integer.parseInt(child
								.getTextContent()));
					} else if (childName.equalsIgnoreCase("avisosp2")) {
						relatorio.setAvisosPrioridade2(Integer.parseInt(child
								.getTextContent()));
					} else if (childName.equalsIgnoreCase("avisosp3")) {
						relatorio.setAvisosPrioridade3(Integer.parseInt(child
								.getTextContent()));
					}
				}
				relatorios.add(relatorio);
			}
		}
		resumo.setRelatorios(relatorios);
		return resumo;
	}

	public static Properties lerOpcoesDoXml(Document doc) {
		Properties opcoes = new Properties();
		Node root = doc.getFirstChild();
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeName().equalsIgnoreCase("tipo_avaliacao")) {
				String tipoAvaliacao = node.getTextContent();
				if (tipoAvaliacao != null) {
					opcoes.setProperty("tipo_avaliacao", tipoAvaliacao);
				} else {
					opcoes.setProperty("tipo_avaliacao", "1");
				}
			} else if (node.getNodeName().equalsIgnoreCase("niveis")) {
				String niveis = node.getTextContent();
				if (niveis != null) {
					opcoes.setProperty("niveis", niveis);
				} else {
					opcoes.setProperty("niveis", "1");
				}
			} else if (node.getNodeName().equalsIgnoreCase("pri1")) {
				String pri1 = node.getTextContent();
				opcoes.setProperty("prioridade1", pri1);
			} else if (node.getNodeName().equalsIgnoreCase("pri2")) {
				String pri2 = node.getTextContent();
				opcoes.setProperty("prioridade2", pri2);
			} else if (node.getNodeName().equalsIgnoreCase("niveis")) {
				String pri3 = node.getTextContent();
				opcoes.setProperty("prioridade3", pri3);
			}
		}
		return opcoes;
	}

	/**
	 * Atualiza uma linha
	 * @param hashCodeString
	 * @param url
	 * @param erros_p1
	 * @param erros_p2
	 * @param erros_p3
	 * @param avisos_p1
	 * @param avisos_p2
	 * @param avisos_p3
	 */
	public static void updLine(String hashCodeString, String url, int erros_p1,
			int erros_p2, int erros_p3, int avisos_p1, int avisos_p2,
			int avisos_p3) {
		int i = 1;
		File arq = new File("temp/resumo" + i + ".csv");
		while (arq.exists()) {
			G_File fileResumo = new G_File("temp/resumo" + i + ".csv");
			String resumo = fileResumo.read();
			int ini=resumo.indexOf("\n"+hashCodeString+"\t");
			if(ini!=-1){
				int fim = resumo.indexOf("\n",ini+1);
				//Atualizar a linha
				String conteudo = "\n"+hashCodeString + "\t" + erros_p1 + "\t" + erros_p2
				+ "\t" + erros_p3 + "\t" + avisos_p1 + "\t" + avisos_p2 + "\t"
				+ avisos_p3 + "\t" + url;
				if(fim!=-1){
					resumo=resumo.substring(0,ini)+conteudo+resumo.substring(fim);	
				}else{
					resumo=resumo.substring(0,ini)+conteudo;
				}
				fileResumo.write(resumo);
				break;
			}else{
				if(resumo.indexOf(hashCodeString+"\t")==0){
					int fim = resumo.indexOf("\n",ini+1);
					String conteudo = hashCodeString + "\t" + erros_p1 + "\t" + erros_p2
					+ "\t" + erros_p3 + "\t" + avisos_p1 + "\t" + avisos_p2 + "\t"
					+ avisos_p3 + "\t" + url;
					resumo=conteudo+resumo.substring(fim);
					fileResumo.write(resumo);
					break;
				}
			}
			i++;
			arq = new File("temp/resumo" + i + ".csv");
		}
	}
	/**
	 * Adiciona uma linha com hash, link, erros e avisos.
	 */
	public static void addLine(String hashCodeString, String url, int erros_p1,
			int erros_p2, int erros_p3, int avisos_p1, int avisos_p2,
			int avisos_p3) {
		Double r_pp = Double.valueOf(ResumoDoRelatorio.results_pp);
		Double tot_l = Double.valueOf(ResumoDoRelatorio.totalLinks);
		Double indx = 0.0;
		indx = (tot_l / r_pp) + 1;
		int indice = indx.intValue();
		if (Double.valueOf(indice) == indx) {
			indice--;
		}
		String conteudo = hashCodeString + "\t" + erros_p1 + "\t" + erros_p2
				+ "\t" + erros_p3 + "\t" + avisos_p1 + "\t" + avisos_p2 + "\t"
				+ avisos_p3 + "\t" + url + "\n";
		try {
			FileWriter arq = new FileWriter("temp/resumo" + indice + ".csv",true);
			arq.write(conteudo);
			arq.close();
			ResumoDoRelatorio.totalLinks++;
		} catch (IOException e) {
		}
		if (indicePg == indice) {
			// Inclui o relatório no painel
			try {
				FrameSilvinha.painelresumo.addRow(hashCodeString, url, avisos_p1, erros_p1, avisos_p2, erros_p2, avisos_p3,	erros_p3);
			} catch (Exception e) {
			}
		}
		// guarda o total de páginas
		if (totPage != indice) {
			PainelResumo.tableNumber.addItem(totPage + 1);
		}
		totPage = indice;
	}

	/**
	 * Retorna a página que ele estava vendo
	 */
	public static String getActualPage() {
		return getPage(indicePg);
	}

	/**
	 * Retorna o número da página atual
	 */
	public static int getIndicePg() {
		return indicePg;
	}

	/**
	 * Retorna o número de páginas no total
	 */
	public static int getTotPage() {
		if (totPage == 1) {
			int i = 1;
			File arq = new File("temp/resumo" + i + ".csv");
			while (arq.exists()) {
				i++;
				arq = new File("temp/resumo" + i + ".csv");
			}
			if(i>1){
				totPage = i - 1;
			}
		}
		if (totalLinks == 0) {
			totalLinks = (totPage - 1) * results_pp;
			String pg1 = getPage(totPage);			
			if (!pg1.equals("")){
				String a[] = pg1.split("\n");
				totalLinks += a.length;
			}
		}
		return totPage;
	}

	/**
	 * Retorna a página escolhida
	 */
	public static String getPage(int indice) {
		// Guarda o indice para ser usado em getActualPage
		indicePg = indice;
		// Lê o arquivo
		StringBuilder sb = new StringBuilder();
		final int mb = 1024;
		File file = new File("temp/resumo" + indice + ".csv");
		FileInputStream fis = null;
		try {
			if (file.exists()) {
				fis = new FileInputStream(file);
				byte[] dados = new byte[mb];
				int bytesLidos = 0;
				while ((bytesLidos = fis.read(dados)) > 0) {
					sb.append(new String(dados, 0, bytesLidos));
				}
				fis.close();
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		return sb.toString().trim();
	}

	public static int getTotalLinks() {
		return totalLinks;
	}

	public static void setTotalLinks(int totalLinks) {
		ResumoDoRelatorio.totalLinks = totalLinks;
	}
}
