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

package br.org.acessobrasil.silvinha.negocio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.entidade.NomeArquivoOuDiretorioLocal;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.excessoes.ExceptionUrlSemConteudo;
import br.org.acessobrasil.silvinha.util.GeraMapDir;
import br.org.acessobrasil.silvinha.util.GeradorMapaSite;
import br.org.acessobrasil.silvinha.util.Token;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaCtrl;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.PaginasNaoAnalisadas;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradGerente;

/**
 * Coordenador do transito do sistema, ele chama o montador do Mapa e envia o
 * resultado para a consulta dos erros.
 * modificado por Renato Tomaz Nati em 11/01/2007
 * @author Danniel Nascimento
 * @version 2.0
 */

public final class Gerente {

	private static Properties props;

	public boolean parar;

	public static boolean pausa;

	private GeradorMapaSite gerador;

	/**
	 * arraylist que obrigatoriamente so contera objetos RelatorioDaUrl
	 */
	private static ArrayList<RelatorioDaUrl> relatorios;

	public static ArrayList<RelatorioDaUrl> errados = new ArrayList<RelatorioDaUrl>();

	private ConcurrentLinkedQueue<RelatorioDaUrl> mapaConteudosAindaNaoAvaliados;

	private static boolean ativo;

	public static int contador = 0;

	private static int threads = 0;

	private int max_threads = 5;

	public static boolean estouroMemoria;

	public static GeraMapDir gmd;

	public static int profundidade = 0;

	public static Thread t;

	public static boolean processaErroAtivo;

	private int orgao;

	/**
	 * Método que inicia a avaliação da URL.
	 * @throws Exception 
	 */
	public ArrayList<RelatorioDaUrl> iniciar(Properties p) throws ExceptionUrlSemConteudo {
		TradGerente.carregaTexto(TokenLang.LANG);
		setAtivo(true);
		GeraMapDir.parar = false;
		this.parar = false;
		pausa = false;
		File arquivo = new File("");
		try {
			arquivo = new File(NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio);
		} catch (Exception e) {

		}
		mapaConteudosAindaNaoAvaliados = new ConcurrentLinkedQueue<RelatorioDaUrl>();
		relatorios = new ArrayList<RelatorioDaUrl>();
		props = p;
		if (props != null) {
			// pega o nivel
			profundidade = Integer.parseInt(props.getProperty("niveis"));
		}
		// Monta o mapa do site
		if (profundidade == PainelAvaliacao.PAGINA) {
			PainelStatusBar.setTotalLinks(1);
			// se o nivel de avaliação for pagina executa isso
			max_threads = 5;
			RelatorioDaUrl relatorio = new RelatorioDaUrl();
			// começa aqui a leitura de paginas WEB();
			// modifiquei em 11/01/2007
			relatorio.setUrl(Token.URL_STRING); 

			PegarPaginaWEB ppw = new PegarPaginaWEB();
			ppw.getContent(relatorio);
			if (relatorio.getConteudo() != null) {
				incrementaContador();
				this.realizarAvaliacao(relatorio);
			} else {
				throw new ExceptionUrlSemConteudo(TradGerente.NAO_POSSIVEL_CONTE_PAGINA);
			}
			finalizaAvaliacao();
			try {
				PainelResumo.progressBarAvTotal.setValue(100);
				PainelResumo.completo.setText(TradGerente.AVALIACAO_TOTAL + 100 + "%");
			} catch (Exception e) {
			}
			PainelStatusBar.setPaginasAvaliadas(1);
			contador=0;
		}

		if (profundidade == PainelAvaliacao.NIVEL1 || profundidade == PainelAvaliacao.NIVEL2 || profundidade == PainelAvaliacao.NIVEL3 || profundidade == PainelAvaliacao.TODO) {
			// se não for pagina executa isto
			max_threads = 2;
			// Monta o mapa do site
			gerador = new GeradorMapaSite(this);
			estouroMemoria = false;
			try {
				gerador.geraMapaSite(Token.URL_STRING, profundidade);
			} catch (IOException e1) {
				ExceptionDialog.showExceptionDialog(TradGerente.IO);
				e1.printStackTrace();
			} catch (SAXException e1) {
				ExceptionDialog.showExceptionDialog(TradGerente.SAX);
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				ExceptionDialog.showExceptionDialog(TradGerente.PARSER);
				e1.printStackTrace();
			}
			finalizaAvaliacao();
		}
		if (profundidade == PainelAvaliacao.HTML_LOCAL) {
			max_threads = 5;
			pausa = false;
			if (arquivo.isDirectory()) {
				JOptionPane.showMessageDialog(null, TradGerente.DIGITE_END_DIR_LOCAL);
			} else {
				if (arquivo.length() > 80000) {
					RelatorioDaUrl relatorio = new RelatorioDaUrl();
					relatorio.setUrl(NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio);
					PaginasNaoAnalisadas.relatorios.add(relatorio);
					PaginasNaoAnalisadas.mensagens.add(TradGerente.TAMANHO_MAIOR_800);
					JOptionPane.showMessageDialog(null, TradGerente.ARQUIVO_2_PONTOS + arquivo.toString() + TradGerente.ARQUIVO_GRANDE_PARA_AVALIADO);
				}else {
					RelatorioDaUrl relatorio = new RelatorioDaUrl();
					relatorio.profundidade = Gerente.profundidade;
					// começa aqui a leitura de paginas WEB();
					// modifiquei em 11/01/2007
					relatorio.setUrl(NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio); 
					if (relatorio.getConteudo() != null) {
						incrementaContador();
						this.realizarAvaliacao(relatorio);
						contador = 0;
					} else {
						ExceptionDialog.showExceptionDialog(TradGerente.NAO_POSSIVEL_CONTE_PAGINA);
					}
				}
			}

			finalizaAvaliacao();
			try {
				PainelResumo.progressBarAvTotal.setValue(100);
				PainelResumo.completo.setText(TradGerente.AVALIACAO_TOTAL + 100 + "%");
			} catch (Exception e) {
			}
		}
		if (profundidade == PainelAvaliacao.DIRETORIO_LOCAL) {
			max_threads = 5;
			if (arquivo.isDirectory()) {
				relatorios.clear();
				gmd = new GeraMapDir(props, this);
			} else {
				JOptionPane.showMessageDialog(null, TradGerente.DIGITE_END_DIR_LOCAL);
			}
		}
		PainelStatusBar.setFinalizado(true);
		while (contador != 0) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
		}
		if (profundidade == PainelAvaliacao.DIRETORIO_LOCAL) {

			relatorios = GeraMapDir.getRelatorio();
		}

		setAtivo(false);
		return relatorios;

	}

	public void pararAvaliacao() {
		if (gerador != null) {
			gerador.pararGeracaoDoMapa();
		}
		this.parar = true;
	}

	public void realizarAvaliacao(RelatorioDaUrl relatorio) {
		if (threads < max_threads) {
			incrementaThreads();
			if (!pausa) {
				if (!parar) {
					ProcessarErro pErro = new ProcessarErro(relatorio, this);
					new Thread(SilvinhaCtrl.processaErro, pErro);
					t = new Thread(pErro);
					/*
					 * Método aplicável a relatórios de apenas uma página. Para
					 * relatório de nível de site, GeradorMapaSite.
					 */
					PainelStatusBar.setText(GERAL.AVALIANDO + " " + relatorio.getUrl());
					try {
						t.start();
					} catch (Exception e) {

					}
				}
			}
		} else {
			mapaConteudosAindaNaoAvaliados.add(relatorio);
		}
	}

	private void finalizaAvaliacao() {
	
	}

	public static Properties getProperties() {
		return props;
	}

	public static void setProperties(Properties props) {
		Gerente.props = props;
	}

	public static void incrementaContador() {
		contador++;
	}

	public static void decrementaContador() {
		contador--;
	}

	public static void incrementaThreads() {
		threads++;
		PainelStatusBar.incrementaTotalPaginas();
	}

	public static void decrementaThreads() {
		threads--;
		PainelStatusBar.incrementaPaginasAvaliadas();
	}
	/**
	 * Aqui é incluido os relatórios no arraylist relatorios
	 */
	public void incluirRelatorio(RelatorioDaUrl relatorio) {
		relatorios.add(relatorio); 
	}

	public static ArrayList<RelatorioDaUrl> getRelatorio() {
		return relatorios;
	}

	/**
	 * @return Returns the houveEstouroMemoria.
	 */
	public static boolean isEstouroMemoria() {
		return estouroMemoria;
	}

	/**
	 * @param houveEstouroMemoria
	 *            The houveEstouroMemoria to set.
	 */
	public static void setEstouroMemoria(boolean houveEstouroMemoria) {
		Gerente.estouroMemoria = houveEstouroMemoria;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		Gerente.ativo = ativo;
	}

	public static boolean isProcessaErroAtivo() {
		return processaErroAtivo;
	}

	public static void setProcessaErroAtivo(boolean processaErroAtivo) {
		Gerente.processaErroAtivo = processaErroAtivo;
	}

	public static void interrompe() {
		t.interrupt();
	}

	public int getOrgao() {
		return orgao;
	}

	public void setOrgao(int orgao) {
		this.orgao = orgao;
	}
	
}
