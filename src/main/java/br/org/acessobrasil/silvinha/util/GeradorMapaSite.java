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

package br.org.acessobrasil.silvinha.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.negocio.ProcessarErro;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradGeradorMapaSite;
import br.org.acessobrasil.silvinha2.util.G_Log;
/**
 * Gerencia os Threads de avaliação de múltiplas páginas
 */
public class GeradorMapaSite {

	private boolean parar;

	private Gerente gerente;

	private int profundidade;

	private String urlOriginal;
	
	private int idx_corrente;

	/**
	 * Fila de links para avaliar
	 */
	private ArrayList<String> links_para_avaliar;

	/**
	 * para verificar se o item está na fila "links_para_avaliar"
	 */
	private HashSet<String> links_avaliados;
	
	private DescobridorLinguagem descobridorLinguagem = new DescobridorLinguagem();

	/**
	 * Flag para parar por causa de versão limitada
	 */
	public static boolean versaoLimitadaStop = false;
	
	public int processos;
	int maxprocessos;
	/**
	 * Flag para pausar a avaliação
	 * O ideal é não ser estático
	 * pois se der pause vai pausar todas as instancias
	 */
	private static boolean pausar = false;

	/**
	 * boolean para indicar se é continuacao
	 */
	public static boolean continuar = false;

	public GeradorMapaSite(Gerente gerente) {
		TradGeradorMapaSite.carregaTexto(TokenLang.LANG);
		this.links_avaliados = new HashSet<String>();
		this.links_para_avaliar = new ArrayList<String>();
		this.idx_corrente = 0;
		this.gerente = gerente;
		this.processos = 0;
	}

	/**
	 * Gera um mapa do site para listar as Urls a serem avaliadas
	 * e avalia elas
	 * @param urlString url a ser avaliada
	 * @param profundidade nivel de diretorio a ser pesquisado
	 */
	public void geraMapaSite(String urlString, int profundidade) throws IOException, SAXException, ParserConfigurationException {
		this.profundidade = profundidade;
		urlOriginal=urlString;
		this.geraMapaSite(urlString);
	}

	/**
	 * Recursão retirada
	 */
	private void geraMapaSite(String urlString) throws IOException, SAXException, ParserConfigurationException {
		G_Log mapaLog = new G_Log("GeradorMapaSite.log");
		descobridorLinguagem.limparLogs();
		/*
		 * Continua a avalição que foi salva.
		 */
		if (continuar) {
			loadIndice();
			loadLinks();
			try {
				urlString = this.links_para_avaliar.get(this.idx_corrente);
				this.idx_corrente++;
			} catch (Exception er) {

			}
			PainelStatusBar.setPaginasAvaliadas(this.idx_corrente);
		} else {
			// trata o link minimamente
			if(urlString.replace("://","").indexOf("/")==-1){
				//verifica se possui uma barra no final
				urlString+="/";
			}
			if (urlString.indexOf("http") != 0) {
				urlString = "http://" + urlString;
			}
			// Adiciona o primeiro link
			this.links_para_avaliar.add(urlString);
			this.links_avaliados.add(urlString);
		}

		boolean init = true;
		maxprocessos = 4;
		Thread t[];
		t = new Thread[maxprocessos];
		ProcessarErro pErro[];
		pErro = new ProcessarErro[maxprocessos];
		PainelStatusBar.showProgressBar();

		PegarPaginaWEB ppw[];
		ppw = new PegarPaginaWEB[maxprocessos];
		for (int i = 0; i < maxprocessos; i++) {
			ppw[i] = new PegarPaginaWEB();
		}

		// pega a data do início da avaliacao
		Date startDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		mapaLog.addLog("Processo \t Url " + dateFormat.format(startDate) + "\n");
		pausar=false;
		versaoLimitadaStop = false;
		FrameSilvinha.stopAvaliacao = false;
		while (this.processos > 0 || init || (this.links_para_avaliar.size() > this.idx_corrente && !parar)) {
			/*
			 * Pausa a avaliação
			 * tem que ser o primeiro comando do loop
			 */
			if (pausar){
				try {
					Thread.sleep(1500);
				} catch (Exception e) {

				}
				//volta para o comando while
				continue;
			}
			if(versaoLimitadaStop){
				break;
			}			
			init = false;		
			gravaIndice();
			try {
				PainelResumo.velocidade.setText(GERAL.VELOCIDADE + PainelResumo.sliderVelocidade.getValue() + "%");
				PainelResumo.valorporcAvaliacaoTotal = ((int) (((float) this.idx_corrente / (float) this.links_para_avaliar.size()) * 100));
				PainelResumo.progressBarAvTotal.setValue(PainelResumo.valorporcAvaliacaoTotal);
				PainelResumo.completo.setText(TradGeradorMapaSite.AVALIACAO_TOTAL + PainelResumo.valorporcAvaliacaoTotal + "%");
				PainelResumo.atualizaJLabelVelocidade();
			} catch (Exception e) {

			}
			if (FrameSilvinha.stopAvaliacao) {
				/*
				 * Sair do loop
				 */
				PainelStatusBar.setText("");
				for (int i = 0; i < maxprocessos; i++) {
					try {
						t[i].interrupt();
					} catch (Exception e) {

					}
				}
				ResumoDoRelatorio.reinicia();
				pErro = null;
				t = null;
				PainelStatusBar.hideProgressBar();
				this.gerente = null;
				return;
			}
			
			int nivel = contaNiveis(urlString);
			if (profundidade <= 3 && nivel > profundidade) {
				// pula esta url
				this.idx_corrente++;
				urlString = this.links_para_avaliar.get(this.idx_corrente);
			} else {
				if (this.processos < maxprocessos && this.idx_corrente < this.links_para_avaliar.size()) {
					urlString = this.links_para_avaliar.get(this.idx_corrente);
					// Exibir para o usuário a página que estamos avaliando
					PainelStatusBar.setText(TradGeradorMapaSite.AVALIANDO +" "+ urlString);
					PainelStatusBar.setTotalLinks(this.links_para_avaliar.size());
					// verifica qual processo está livre para tratar esta url
					int i;
					boolean achou = false;
					for (i = 0; i < maxprocessos; i++) {
						if (pErro[i] == null) {
							mapaLog.addLog(this.processos + " " + i + "\t\t(" + urlString + ")\n");
							pErro[i] = new ProcessarErro(urlString, gerente, this);
							t[i] = new Thread(pErro[i]);
							achou = true;
							break;
						} else {
							if (!pErro[i].isProcErroAtivo()) {
								mapaLog.addLog(this.processos + " " + i + "\t\t(" + urlString + ")\n");
								pErro[i] = null;
								pErro[i] = new ProcessarErro(urlString, gerente, this);
								t[i] = new Thread(pErro[i]);
								achou = true;
								break;
							}
						}
					}
					if (!achou) {
						// processo livre não encontrado
						this.processos = maxprocessos - 1;
					} else {
						// ok pode passar para o proximo link
						this.idx_corrente++;
						try {
							this.processos++;
							// inicia a marcação de tempo para timeout
							pErro[i].setStartTime();
							t[i].start();
						} catch (Exception e) {
							this.diminuiProcessos();
							mapaLog.addLog("Erro: " + e.getMessage() + "\n" + urlString + "\n");
							// Tenta anular o processo
							try {
								pErro[i] = null;
								t[i] = null;
							} catch (Exception e2) {

							}
						}
					}
				} else {
					// os processos estão ocupados ou estão esperando
					// verificar os processos acima do tempo permitido
					for (int i = 0; i < maxprocessos; i++) {
						if (pErro[i] != null) {
							Date agora = new Date();
							if (pErro[i].getStartTime() + 60000 < agora.getTime()) {
								mapaLog.addLog("Erro: Processo Timeout " + i + "\n");
								pErro[i] = null;
								try {
									t[i].interrupt();
									t[i] = null;
								} catch (Exception e) {

								}
								this.diminuiProcessos();
							}
						}
					}
					try {
						Thread.sleep(250);
					} catch (Exception e) {

					}
				}
			}
		}
		// pega a data final
		Date finalDate = new Date();
		mapaLog.addLog("" + dateFormat.format(finalDate) + "\n");
		mapaLog.addLog("***************** Finalizado *****************\n");
	}

	/**
	 * Diminui os números de processos
	 */
	public void diminuiProcessos() {
		this.processos--;
		if (this.processos < 0) {
			this.processos = 0;
		}
	}

	/**
	 * Para a execução da geracao do mapa
	 */
	public void pararGeracaoDoMapa() {
		this.parar = true;
	}

	private int contaNiveis(String url) {
		String nomeSite = url.split("//")[1];
		if (nomeSite.endsWith("/")) {
			return nomeSite.split("/").length;
		} else {
			return nomeSite.split("/").length - 1;
		}

	}

	/**
	 * Extrai todos os links da página e coloca no arraylist
	 * 
	 * @param HTMLsource
	 *            código fonte da página
	 * @param Url
	 *            link da página HTML
	 */
	public void extraiLinksFromPageSource(String HTMLsource, String Url) {
		String link = "";
		String baseUrl = "";
		String dominio = "", servidor = "";
		String HTMLsourceLower = HTMLsource + " ";
		String arrUrl[];
		int i, fim, ini = fim = 0;

		HTMLsourceLower = HTMLsourceLower.toLowerCase();

		baseUrl = Url.substring(0, Url.lastIndexOf("/") + 1);
		/* 		 
		 * procurar por <base href="http://www.bcb.gov.br/pom/spb/estatistica/port/estatistica.asp">
		 */
		ini = HTMLsourceLower.indexOf("<base ", ini);
		int ini_href = -1;
		if (ini > -1) {
			fim = HTMLsourceLower.indexOf(">", ini);
			link = HTMLsource.substring(ini, fim);
			ini_href = link.toLowerCase().indexOf("href=\"");
			if (ini_href > -1) {
				ini_href += 6;
				int fim_href = link.indexOf("\"", ini_href);
				if (fim_href > ini_href) {
					link = link.substring(ini_href, fim_href);
					baseUrl = link.substring(0, link.lastIndexOf("/") + 1);
				}
			}
		}
		// buscar o nome do "servidor" www.lslslsl.gov.br
		if (Url.indexOf("?") != -1) {
			servidor = Url.substring(0, Url.indexOf("?"));
			arrUrl = servidor.split("/");
		} else {
			arrUrl = Url.split("/");
		}
		for (i = 0; i < arrUrl.length; i++) {
			if (i == 2) {
				servidor = arrUrl[i];break;
			}
		}
		//dominio = servidor.replaceAll("www", "");
		dominio = servidor;		
		RegrasHardCodedEmag regra = new RegrasHardCodedEmag();
		// Pegar os links de <a href
		ini = HTMLsourceLower.indexOf("<a ", 0);
		while (ini > -1) {
			ini_href = -1;
			if (ini > -1) {
				fim = HTMLsourceLower.indexOf(">", ini);
				String alink = HTMLsource.substring(ini, fim);
				//*
				link=regra.getAtributo(alink+ " >", "href");
				if(link.startsWith("\"") && link.endsWith("\"")){
					link=link.replaceAll("\"", "");
					regra.getAtributo(alink, "href");
				}
				this.addNewLink(link, servidor, dominio, baseUrl, Url);
			}
			ini = HTMLsourceLower.indexOf("<a ", fim);
		}
		// Fim pegar os links de <a href

		// ini pegar os links de <area
		ini = HTMLsourceLower.indexOf("<area ", 0);
		while (ini > -1) {
			ini_href = -1;
			if (ini > -1) {
				fim = HTMLsource.indexOf(">", ini);
				link = HTMLsource.substring(ini, fim);
				ini_href = link.toLowerCase().indexOf("href=\"");
				if (ini_href > -1) {
					ini_href += 6;
					int fim_href = link.indexOf("\"", ini_href);
					if (fim_href > ini_href) {
						link = link.substring(ini_href, fim_href);
						this.addNewLink(link, servidor, dominio, baseUrl, Url);
					}
				}
			}
			ini = HTMLsourceLower.indexOf("<area ", fim);
		}
		// Fim pegar os links de <area
		// ini pegar os links de <iframe
		ini = HTMLsourceLower.indexOf("<iframe ", 0);
		while (ini > -1) {
			ini_href = -1;
			if (ini > -1) {
				fim = HTMLsource.indexOf(">", ini);
				link = HTMLsource.substring(ini, fim);
				ini_href = link.toLowerCase().indexOf("src=\"");
				if (ini_href > -1) {
					ini_href += 5;
					int fim_href = link.indexOf("\"", ini_href);
					if (fim_href > ini_href) {
						link = link.substring(ini_href, fim_href);
						this.addNewLink(link, servidor, dominio, baseUrl, Url);
					}
				}
			}
			ini = HTMLsourceLower.indexOf("<iframe ", fim);
		}
		// Fim pegar os links de <iframe
		// ini pegar os links de <frame
		ini = HTMLsourceLower.indexOf("<frame ", 0);
		while (ini > -1) {
			ini_href = -1;
			if (ini > -1) {
				fim = HTMLsource.indexOf(">", ini);
				link = HTMLsource.substring(ini, fim);
				ini_href = link.toLowerCase().indexOf("src=\"");
				if (ini_href > -1) {
					ini_href += 5;
					int fim_href = link.indexOf("\"", ini_href);
					if (fim_href > ini_href) {
						link = link.substring(ini_href, fim_href);
						this.addNewLink(link, servidor, dominio, baseUrl, Url);
					}
				}
			}
			ini = HTMLsourceLower.indexOf("<frame ", fim);
		}
		// Fim pegar os links de <frame
	}

	/**
	 * Adiciona o novo link no arrayList de links caso não esteja
	 * 
	 * @param link
	 *            Link dentro do <a href="" por exemplo
	 * @param servidor
	 *            Ex: www.acessobrasil.org.br
	 * @param dominio
	 *            Ex: .acessobrasil.org.br
	 * @param baseUrl
	 *            Ex: http://www.acessobrasil.org.br/minhapasta/
	 * 
	 */
	private void addNewLink(String link, String servidor, String dominio, String baseUrl, String Url) {
		/**
		 * Logs de referencias
		 * gravado em
		 * log/refLinks.log
		 */
		G_Log refLinks = new G_Log("refLinks.log");
		refLinks.desliga();
		refLinks.systemPrint(false);
		
		link = link.replaceAll("&amp;", "&");
		while(link.endsWith("%20")){
			link = link.substring(0,link.length()-3);
		}
		link = link.trim();
		
		
		// verifica se é de <a name
		if (link.indexOf("#") != -1) {
			String[] splits = link.split("#");
			try {
				link = splits[0].trim();
				if (link.equals("")) {
					return;
				}
			} catch (Exception e) {
				return;
			}
		}
		// nao considerar links para arquivos ou extensoes nao aceitas
		for (int i = 0; i < Token.NAOEXTS.length; i++) {
			if (link.toLowerCase().endsWith(Token.NAOEXTS[i])) {
				return;
			}
		}
		if (link.indexOf("http://") == 0 || link.indexOf("https://") == 0) {
			// verificar se vai para outro endereço, código mais abaixo		
		} else if (link.toLowerCase().indexOf("ftp:") == 0 || link.toLowerCase().indexOf("javascript:") == 0 || link.toLowerCase().indexOf("mailto:") == 0
				|| link.toLowerCase().indexOf("file:") == 0 || link.toLowerCase().indexOf("telnet:") == 0) {
			// desconsiderar estes links
			return;
		} else if (link.indexOf("./") == 0) {
			// vai para o mesmo diretorio
			link = baseUrl + link.substring(2);
		} else if (link.indexOf("?") == 0) {
			// começa com interrogação simples
			link = baseUrl + link;
		} else if (link.indexOf("/") == 0) {
			// começa com barra simples
			if (baseUrl.indexOf("https://") != -1) {
				link = "https://" + servidor + link;
			} else {
				link = "http://" + servidor + link;
			}
		} else if (link.indexOf("../") == 0) {
			// começa com dois pontos barra
			String str2 = baseUrl;
			int pos = str2.lastIndexOf('/');
			str2 = str2.substring(0, pos + 1);
			while (link.startsWith("../")) {
				// sobe um diretório.
				pos = str2.lastIndexOf('/', pos - 1);
				// Já está na raiz.
				if (pos != -1) {
					str2 = str2.substring(0, pos + 1);
				}
				link = link.substring(3);
			}
			link = str2 + link;
		} else {
			link = baseUrl + link;
		}
		
		refLinks.addLog("link: " + link + "\n\t\tFrom: " + Url + "\n\n");
		
		// verificar se vai para outro endereço
		String tStr=link.replace("http://","");
		if(tStr.indexOf('/')!=-1){
			tStr = tStr.substring(0,tStr.indexOf('/'));
		}
		//TODO retirar a linha abaixo
		//dominio=".saude.gov.br";
		if (tStr.indexOf(dominio) == -1) {
			refLinks.addLog("link rejeitado "+link+"\n");
			return;
		}
		
		//TODO colocar o if abaixo caso queira algum ajuste
		//verifica se não está no path
		//if(link.indexOf("bvsms")==-1) return;
	
		if (!this.links_avaliados.contains(link)) {
			refLinks.desliga();
			refLinks.addLog("link: " + link + "\nFrom: " + Url + "\n\n");
			eventoLinkAdicionado(link);
			this.links_para_avaliar.add(link);
			gravaLink(link);
			this.links_avaliados.add(link);
		}
	}
	/**
	 * Evento chamado quando um link é adicionado a lista para avaliar
	 * @param link
	 */
	private void eventoLinkAdicionado(String link){
		descobridorLinguagem.descobrirLinguagem(link);
	}

	
	/**
	 * Grava os links no HD
	 */
	private void gravaLink(String link) {
		try {
			FileWriter arq = new FileWriter("temp/links.log", true);
			arq.write(link + "\n");
			arq.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Grava o indice corrente
	 */
	private void gravaIndice() {
		try {
			FileWriter arq = new FileWriter("temp/indice.log");
			arq.write(idx_corrente + "\n");
			arq.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Restaura listas de links
	 */
	public void loadLinks() {

		StringBuilder sb = new StringBuilder();
		final int mb = 1024;
		File file = new File("temp/links.log");
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
				return;
			}
		} catch (Exception e) {
			return;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		String arr[] = sb.toString().trim().split("\n");
		for (int i = 0; i < arr.length; i++) {
			this.links_para_avaliar.add(arr[i]);
			if(i<this.idx_corrente){
				this.links_avaliados.add(arr[i]);
			}
		}
	}

	/**
	 * Restaura o índice atual
	 */
	public void loadIndice() {
		// Lê o arquivo
		StringBuilder sb = new StringBuilder();
		final int mb = 1024;
		File file = new File("temp/indice.log");
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
				return;
			}
		} catch (Exception e) {
			return;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		String arr[] = sb.toString().trim().split("\n");
		for (int i = 0; i < arr.length; i++) {
			this.idx_corrente = Integer.valueOf(arr[i]);
		}
	}
	/**
	 * Flag para pausar a avaliação
	 * O ideal é que não seja estático
	 * pois se der pause vai pausar todas as instâncias
	 */
	public static void setPausar(boolean pausar) {
		GeradorMapaSite.pausar = pausar;
	}	
}
