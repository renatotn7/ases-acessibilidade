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
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import br.org.acessobrasil.nucleuSilva.util.ObterPaginaLocal;
import br.org.acessobrasil.silvinha.entidade.NomeArquivoOuDiretorioLocal;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.negocio.ProcessarErro;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.PaginasNaoAnalisadas;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradGeradorMapaSite;

/**
 * Coordenador do transito do sistema, ele chama o montador do Mapa e envia o
 * resultado para a consulta dos erros.
 * 
 * @author Renato Tomaz Nati em 11/01/2007
 * @version 2.0
 */
public class GeraMapDir {
	public static File arquivo;

	//private static Properties props = new Properties();

	public static boolean parar = false;

	//private GeradorMapaSite gerador;

	public static ArrayList<RelatorioDaUrl> relatorios = new ArrayList<RelatorioDaUrl>(); // arraylist
																							// que
																							// obrigatóriamente
																							// so
																							// contera
																							// objetos
																							// RelatorioDaUrl

	public static ArrayList<RelatorioDaUrl> zerarelatorios = new ArrayList<RelatorioDaUrl>(); // arraylist
																								// que
																								// obrigatóriamente
																								// so
																								// contera
																								// objetos
																								// RelatorioDaUrl

	private ConcurrentLinkedQueue<RelatorioDaUrl> mapaConteudosAindaNaoAvaliados = new ConcurrentLinkedQueue<RelatorioDaUrl>();

	//private int contador = 0;

	private int threads = 0;

	private final int MAX_THREADS = 10;

	// private static Logger log =
	// Logger.getLogger("br.org.acessobrasil.silvinha");
	// private static boolean estouroMemoria;
	// private AvaliacaoProgressBar pb;
	public static ArrayList<File> files;

	ObterPaginaLocal ppa;

	BuscaHtmlemDiretorio buscador = new BuscaHtmlemDiretorio();

	private static boolean pausar;

	public static Gerente gerente;

	private static ArrayList<String> ListaDeArquivosNaoAvaliados = new ArrayList<String>();

	/**
	 * Método que inicia a avaliação da URL.
	 */

	public GeraMapDir(Properties p, Gerente gerente) {

		relatorios.clear();
		inicio(p, gerente);
		parar = false;
	}

	public void inicio(Properties p, Gerente oGerente) {
		//pega a data do início da avaliacao
		Date startDate = new Date();
		
		//ExceptionDialogNaoModal exceptionNaoModal = new ExceptionDialogNaoModal();
		files = new ArrayList<File>();
		//props = p;
		files = buscador.findFiles(new File(
				NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio),
				".*\\.html|.*\\.htm|.*\\.HTML|.*\\.HTM");
		gerente = oGerente;

		PainelStatusBar.showProgressBar();
		PainelStatusBar.setTotalLinks(files.size());
		int total=files.size();
		int atual=0;
		pausar = false;
		for (File arquivo : files) {
			
			while(pausar){
				try{
					Thread.sleep(1500);
				}catch(Exception e){
					
				}
			}
			atual++;
			if(FrameSilvinha.limitacao()){
				break;
			}
			try{
				PainelResumo.velocidade.setText(GERAL.VELOCIDADE + PainelResumo.sliderVelocidade.getValue() + "%");
				PainelResumo.valorporcAvaliacaoTotal=((int)(((float)atual /(float)total)*100));
				PainelResumo.progressBarAvTotal.setValue(PainelResumo.valorporcAvaliacaoTotal);
				PainelResumo.completo.setText(TradGeradorMapaSite.AVALIACAO_TOTAL + PainelResumo.valorporcAvaliacaoTotal + "%");
				PainelResumo.atualizaJLabelVelocidade();
			}catch(Exception e){
				//System.out.println("catch: "+e.getMessage());
			}
			//PainelStatusBar.setText("Avaliando " + arquivo.toString());
			PainelStatusBar.setText(TradGeradorMapaSite.AVALIANDO +" "+ arquivo.toString());
			if (!parar) {
				if (arquivo.length() > 80000) {
					RelatorioDaUrl relatorio = new RelatorioDaUrl();
					relatorio.setUrl(arquivo.toString());
					PaginasNaoAnalisadas.relatorios.add(relatorio);

					PaginasNaoAnalisadas.mensagens
							.add("tamanho maior do que 800kb");
					ListaDeArquivosNaoAvaliados.add(arquivo.toString());

				} else {

					RelatorioDaUrl relatorio = new RelatorioDaUrl();
					relatorio.profundidade = Gerente.profundidade;
					relatorio.setUrl(arquivo.toString().replace('\\', '/'));
					
					//ppa = new PegarPaginaLocal(arquivo.toString().replace('\\', '/'));
					//ppa.getContent(relatorio);
					
					if (relatorio.getConteudo() != null) {
						//incrementaContador();
						ProcessarErro pErro = new ProcessarErro(relatorio, gerente);
						pErro.parseWAI();
						//this.realizarAvaliacao(relatorio);
					} else {
						ExceptionDialog
								.showExceptionDialog(GERAL.NAO_POSSIVEL_ACHAR_CONT_PAG);
					}

					// ResumoDoRelatorio.relatorios.add(relatorio);
					relatorios.add(relatorio);
					PainelStatusBar.incrementaPaginasAvaliadas();
					while (Gerente.contador != 0) {
						try {
							Thread.sleep(500);
						} catch (Exception e) {

						}
					}
				}
			} // finalizaAvaliacao(); // sendo ou não sendo executa isto
			// PainelStatusBar.setFinalizado(true);

			else {

				break;
			}

		}
		// for(String nomes:ListaDeArquivosNaoAvaliados){

		// JOptionPane.showMessageDialog(null, "Arquivo: " + nomes + " \né muito
		// grande não podendo ser avaliado");
		// }
		//System.out.print("finalizou\n");
		Date finalDate = new Date();
		//PainelStatusBar.setText("Avaliação terminada " + Util.calcularTempo(startDate, finalDate));
		PainelStatusBar.setText(TradGeradorMapaSite.AVALIACA_TERMINADA + Util.calcularTempo(startDate, finalDate));
		
		parar = false;
		while (Gerente.contador != 0) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
			}
		}

	}

	public static void pararAvaliacao() {

		parar = true;
	}

	public void realizarAvaliacao(RelatorioDaUrl relatorio) {
		if (threads < MAX_THREADS) {
			this.incrementaThreads();
			ProcessarErro pErro = new ProcessarErro(relatorio, gerente);
			pErro.parseWAI();
			//Thread t = new Thread(pErro);

			if (!parar) {

				PainelStatusBar.setText(GERAL.AVALIANDO + " "
						+ relatorio.getUrl());
				//t.start();
			}
		} else {
			mapaConteudosAindaNaoAvaliados.add(relatorio);
		}
	}

	/*
	 * private void finalizaAvaliacao() { while
	 * (mapaConteudosAindaNaoAvaliados.size() > 0) { if (threads < MAX_THREADS) {
	 * this.incrementaThreads(); //RelatorioDaUrl relatorio =
	 * mapaConteudosAindaNaoAvaliados.poll(); ProcessarErro pErro = new
	 * ProcessarErro(relatorio, this.gerente); Thread t = new Thread(pErro); if
	 * (!parar) { PainelStatusBar.setText(TokenLang.DIALOG_11 + " " +
	 * relatorio.getUrl()); t.start(); } } else { try { Thread.sleep(1000);
	 *  } catch (Exception e) { } } } }
	 */

	// public static Properties getProperties()
	// {
	// return props;
	// }
	public void incrementaContador() {
		Gerente.incrementaContador();
	}

	public void decrementaContador() {
		Gerente.decrementaContador();
	}

	public void incrementaThreads() {
		Gerente.incrementaThreads();
		// pb.incrementaTotalPaginas();

	}

	public void decrementaThreads() {
		Gerente.decrementaThreads();
		// pb.incrementaPaginasAvaliadas();
	}

	public void incluirRelatorio(RelatorioDaUrl relatorio) {
		relatorios.add(relatorio); // aqui é incluido os relatórios no
										// arraylist relatorios
	}

	public static ArrayList<RelatorioDaUrl> getRelatorio() {
		return relatorios;
	}

	/*
	 * @return Returns the houveEstouroMemoria.
	 */
	// public static boolean isEstouroMemoria() {
	// return estouroMemoria;
	// }
	/**
	 * @param houveEstouroMemoria
	 *            The houveEstouroMemoria to set.
	 */
	// public static void setEstouroMemoria(boolean houveEstouroMemoria) {
	// Gerente.estouroMemoria = houveEstouroMemoria;
	// }

}
