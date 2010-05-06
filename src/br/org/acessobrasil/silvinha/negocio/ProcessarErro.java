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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.org.acessobrasil.ases.entidade.ModeloSite;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.persistencia.BancoSite;
import br.org.acessobrasil.ases.persistencia.SingBancoSite;
import br.org.acessobrasil.nucleuSilva.entidade.ArmazenaErroOuAvisoAntigo;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.entidade.PontoVerificacao;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.util.GeraMapDir;
import br.org.acessobrasil.silvinha.util.GeradorMapaSite;
import br.org.acessobrasil.silvinha.util.GravadorDeTemporarios;
import br.org.acessobrasil.silvinha.util.ThreadParaGerente;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaCtrl;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.PaginasNaoAnalisadas;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.projetodosite.ConfiguracaoDaAvaliacao;
import br.org.acessobrasil.silvinha2.util.G_Log;

/**
 * Classe que irá gerar os erros da página. roda pelo menos uma vez para cada
 * página
 */
public class ProcessarErro implements Runnable {

	private static G_Log log = new G_Log("ProcessarErro.log");

	public boolean procErroAtivo;

	private PegarPaginaWEB ppw;

	private long startTime;

	private InterfNucleos nucleox;

	/**
	 * Contagem dos Erros por Prioridade
	 */
	private int pri1 = 0, pri2 = 0, pri3 = 0;

	/**
	 * Contagem dos Avisos por Prioridade
	 */
	private int ap1 = 0, ap2 = 0, ap3 = 0;
	
	/**
	 * Definicao de quais prioridades mostrar no relatorio
	 */
	private boolean mostraP1 = true;

	private boolean mostraP2 = true;

	private boolean mostraP3 = true;

	/**
	 * Coleção de Erros de prioridade 1.
	 */
	private HashSet<PontoVerificacao> ptVerif1 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Erros de prioridade 2.
	 */
	private HashSet<PontoVerificacao> ptVerif2 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Erros de prioridade 3.
	 */
	private HashSet<PontoVerificacao> ptVerif3 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Avisos de prioridade 1.
	 */
	private HashSet<PontoVerificacao> avisosP1 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Avisos de prioridade 2.
	 */
	private HashSet<PontoVerificacao> avisosP2 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Avisos de prioridade 3.
	 */
	private HashSet<PontoVerificacao> avisosP3 = new HashSet<PontoVerificacao>();

	/**
	 * Conjunto de propriedades a serem avaliadas pelo Processador de Erros.
	 */
	private Properties props;

	/**
	 * Conjunto de Pontos de Verifica&ccedil;&atilde; que ser&atilde;o
	 * utilizados para avalia&ccedil;&atilde;o dos erros no conte&uacute;do das
	 * p&aacute;ginas.
	 */
	private HashMap<Integer, PontoVerificacao> pontosVerificacao;

	private BancoSite bancoSite;

	private RelatorioDaUrl relatorio;

	private GeradorMapaSite geradorMapaSite;

	public ProcessarErro() {
		bancoSite = SingBancoSite.getInstancia();
	}

	/**
	 * Processa os erros da urlString passada como parâmetro
	 * 
	 * @param urlString
	 * @param gerente
	 * @param p_geradorMapaSite
	 */
	public ProcessarErro(String urlString, final Gerente gerente, GeradorMapaSite parm_geradorMapaSite, PegarPaginaWEB oPPW) {
		bancoSite = SingBancoSite.getInstancia();
		this.props = Gerente.getProperties();
		relatorio = new RelatorioDaUrl();
		relatorio.setUrl(urlString);
		if (oPPW == null) {
			ppw = new PegarPaginaWEB();
		} else {
			ppw = oPPW;
		}
		this.geradorMapaSite = parm_geradorMapaSite;
		int tipoAvaliacao = Integer.parseInt(props.getProperty("tipo_avaliacao"));
		pontosVerificacao = PontosDeVerificacaoFactory.getPontosDeVerificacao(tipoAvaliacao);
		inicializar();
		inicializaMostraPrioridades();
	}

	/**
	 * Processa os erros da urlString passada como parâmetro
	 * 
	 * @param urlString
	 * @param gerente
	 * @param p_geradorMapaSite
	 */
	public ProcessarErro(String urlString, final Gerente gerente, GeradorMapaSite p_geradorMapaSite) {
		bancoSite = SingBancoSite.getInstancia();
		this.props = Gerente.getProperties();
		relatorio = new RelatorioDaUrl();
		relatorio.setUrl(urlString);
		if (ppw == null) {
			ppw = new PegarPaginaWEB();
		}
		this.geradorMapaSite = p_geradorMapaSite;
		int tipoAvaliacao = Integer.parseInt(props.getProperty("tipo_avaliacao"));
		pontosVerificacao = PontosDeVerificacaoFactory.getPontosDeVerificacao(tipoAvaliacao);
		inicializar();
		inicializaMostraPrioridades();
	}

	/**
	 * Chamado pelo GeraMapDir, avaliacao no HD
	 * 
	 * @param relatorio
	 * @param gerente
	 */
	public ProcessarErro(RelatorioDaUrl relatorio, final Gerente gerente) {
		bancoSite = SingBancoSite.getInstancia();
		this.props = Gerente.getProperties();
		this.relatorio = relatorio;
		int tipoAvaliacao = Integer.parseInt(props.getProperty("tipo_avaliacao"));
		pontosVerificacao = PontosDeVerificacaoFactory.getPontosDeVerificacao(tipoAvaliacao);
		inicializar();
		inicializaMostraPrioridades();
	}

	/**
	 * Roda o parseWAI como um processo
	 */
	public void run() {
		if (FrameSilvinha.limitacao()) {
			GeraMapDir.pararAvaliacao();
			Gerente gerente = new Gerente();
			if (Gerente.profundidade < 5) {
				// Indica os thread(s) para parar a avaliacao
				GeradorMapaSite.versaoLimitadaStop = true;
				if (gerente.isAtivo()) {
					ThreadParaGerente.gerente.pararAvaliacao();
					ThreadParaGerente.gerente = null;
					System.gc();
				}
				ResumoDoRelatorio.relatorios.clear();
			} else if (Gerente.profundidade == 5) {
				ResumoDoRelatorio.relatorios.clear();
				ThreadParaGerente.gerente = null;
				System.gc();
			} else if (Gerente.profundidade == 6) {
				ThreadParaGerente.relatorios.clear();
				ThreadParaGerente.gerente = null;
				System.gc();
			} else if (Gerente.profundidade == 7) {
				if (gerente.isAtivo()) {
					ThreadParaGerente.gerente.pararAvaliacao();
					ThreadParaGerente.gerente = null;
					System.gc();
				}
				while (gerente.isAtivo()) {
					try {
						Thread.sleep(300);
					} catch (Exception ex) {
					}
				}
				ThreadParaGerente.relatorios.clear();
			}
			if (PaginasNaoAnalisadas.relatorios.size() != 0) {
				PaginasNaoAnalisadas.relatorios.clear();
			}
			SilvinhaCtrl.processaErro.interrupt();
			return;
		}

		try {
			setProcErroAtivo(true);
			Gerente.incrementaContador();
			// Busca o conteudo do link na internet e coloca no relatorio;
			try {
				ppw.getContent(this.relatorio);
			} catch (Exception e) {
				log.addLog("Erro em ppw " + e.getMessage() + "\n");
			}
			if (!FrameSilvinha.stopAvaliacao) {
				if (relatorio.getConteudo() != null) {
					try {
						this.geradorMapaSite.extraiLinksFromPageSource(relatorio.getConteudo().toString(), relatorio.getUrl());
					} catch (Exception e) {
						log.addLog("geradorMapaSite " + e.getMessage() + "\n");
					}
					// processa as regras de acessibilidade
					this.parseWAI();
				} else {
					log.addLog("Sem conteudo " + relatorio.getUrl() + "\n");
				}
				this.geradorMapaSite.diminuiProcessos();
			}
			Gerente.decrementaContador();
			Gerente.decrementaThreads();
			setProcErroAtivo(false);
		} catch (Exception e) {
			log.addLog("Run " + e.getMessage() + "\n");
		}
	}

	public void parseWAI() {
		progressoPaginaAtual(1);
		// Zera as coisas
		inicializar();
		progressoPaginaAtual(12);
		try {
			// Marcação retorna os erros
			separarPrioridade(marcacao());
			progressoPaginaAtual(27);
		} catch (Exception e) {
			Gerente.decrementaContador();
			e.printStackTrace();
			log.debug(e.getMessage());
			return;
		}
		progressoPaginaAtual(29);
		ModeloSite.addNumErros(pri1 + pri2 + pri3);
		ModeloSite.addNumErrosP1(pri1);
		relatorio.setErrosPrioridade1(pri1);
		ModeloSite.addNumErrosP2(pri2);
		relatorio.setErrosPrioridade2(pri2);
		ModeloSite.addNumErrosP3(pri3);
		relatorio.setErrosPrioridade3(pri3);
		progressoPaginaAtual(31);
		ModeloSite.addNumAvisos(ap1 + ap2 + ap3);
		ModeloSite.addNumAvisosP1(ap1);
		relatorio.setAvisosPrioridade1(ap1);
		ModeloSite.addNumAvisosP2(ap2);
		progressoPaginaAtual(33);
		relatorio.setAvisosPrioridade2(ap2);
		ModeloSite.addNumAvisosP3(ap3);
		relatorio.setAvisosPrioridade3(ap3);
		progressoPaginaAtual(34);
		relatorio.setListaErrosP1(ptVerif1);
		relatorio.setListaErrosP2(ptVerif2);
		relatorio.setListaErrosP3(ptVerif3);
		relatorio.setListaAvisosP1(avisosP1);
		relatorio.setListaAvisosP2(avisosP2);
		relatorio.setListaAvisosP3(avisosP3);
		relatorio.setMostraP1(mostraP1);
		relatorio.setMostraP2(mostraP2);
		relatorio.setMostraP3(mostraP3);
		progressoPaginaAtual(47);
		if (relatorio.profundidade > 5) {
			GravadorDeTemporarios.gravarTemp(relatorio);
		}
		progressoPaginaAtual(55);
		if (relatorio.profundidade <= 5) {
			if (!FrameSilvinha.stopAvaliacao) {
				relatorio.geraArquivoRelatorioEmXml2();
			}
		}
		progressoPaginaAtual(62);
		if (relatorio.profundidade > 5){
			ThreadParaGerente.relatorios.add(relatorio);
		}
		progressoPaginaAtual(75);
		ResumoDoRelatorio.addLine(relatorio.hashCodeString, relatorio.getUrl(), relatorio.getErrosPrioridade1(), relatorio.getErrosPrioridade2(), relatorio.getErrosPrioridade3(), relatorio.getAvisosPrioridade1(), relatorio.getAvisosPrioridade2(), relatorio.getAvisosPrioridade3());
		progressoPaginaAtual(83);
		relatorio.setListaErrosP1(null);
		relatorio.setListaErrosP2(null);
		relatorio.setListaErrosP3(null);
		progressoPaginaAtual(96);
		relatorio.setListaAvisosP1(null);
		relatorio.setListaAvisosP2(null);
		relatorio.setListaAvisosP3(null);
		relatorio = null;
		progressoPaginaAtual(100);
	}

	/**
	 * Reavalia uma página
	 */
	public RelatorioDaUrl reavalia(RelatorioDaUrl oRelatorio) {
		oRelatorio.setListaErrosP1(new HashSet<PontoVerificacao>());
		oRelatorio.setListaErrosP2(new HashSet<PontoVerificacao>());
		oRelatorio.setListaErrosP3(new HashSet<PontoVerificacao>());
		relatorio = new RelatorioDaUrl();
		relatorio.setUrl(oRelatorio.getUrl());
		relatorio.hashCodeString = oRelatorio.hashCodeString;
		ppw = new PegarPaginaWEB();
		/*
		 * Carrega as configurações do HD
		 */
		ConfiguracaoDaAvaliacao configDaAvaliacao = new ConfiguracaoDaAvaliacao();
		configDaAvaliacao.loadConf();
		props = configDaAvaliacao.getAsProperties();
		int tipoAvaliacao = Integer.parseInt(props.getProperty("tipo_avaliacao"));
		pontosVerificacao = PontosDeVerificacaoFactory.getPontosDeVerificacao(tipoAvaliacao);
		inicializar();
		inicializaMostraPrioridades();
		// Busca o conteudo do link na internet e coloca no relatorio;
		try {
			ppw.getContent(this.relatorio);
		} catch (Exception e) {
			log.addLog("Erro em ppw " + e.getMessage() + "\n");
			e.printStackTrace();
			return null;
		}
		if (this.relatorio.getConteudo() != null) {
			try {
				separarPrioridade(marcacao());
				relatorio.setErrosPrioridade1(pri1);
				relatorio.setErrosPrioridade2(pri2);
				relatorio.setErrosPrioridade3(pri3);
				relatorio.setAvisosPrioridade1(ap1);
				relatorio.setAvisosPrioridade2(ap2);
				relatorio.setAvisosPrioridade3(ap3);
				relatorio.setListaErrosP1(ptVerif1);
				relatorio.setListaErrosP2(ptVerif2);
				relatorio.setListaErrosP3(ptVerif3);
				relatorio.setListaAvisosP1(avisosP1);
				relatorio.setListaAvisosP2(avisosP2);
				relatorio.setListaAvisosP3(avisosP3);
				relatorio.setMostraP1(mostraP1);
				relatorio.setMostraP2(mostraP2);
				relatorio.setMostraP3(mostraP3);
				// Atualiza o XML
				relatorio.gravaXmlNoHD();
				// Atualiza o indice no CSV
				ResumoDoRelatorio.updLine(relatorio.hashCodeString, relatorio.getUrl(), pri1, pri2, pri3, ap1, ap2, ap3);
			} catch (Exception e) {
				log.debug(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return this.relatorio;
	}

	private void inicializaMostraPrioridades() {
		String prioridade1 = props.getProperty("prioridade1");
		String prioridade2 = props.getProperty("prioridade2");
		String prioridade3 = props.getProperty("prioridade3");
		if (prioridade1.equals("false")) {
			mostraP1 = false;
		}
		if (prioridade2.equals("false")) {
			mostraP2 = false;
		}
		if (prioridade3.equals("false")) {
			mostraP3 = false;
		}
	}

	private void progressoPaginaAtual(int progresso) {
		try {
			PainelResumo.progressBarPaginaAtual.setValue(progresso);
			PainelResumo.paginaAtual.setText(GERAL.PAGINA_ATUAL + progresso + "%");
			PainelResumo.valorProgressBarPaginaAtual = progresso;
		} catch (Exception e) {

		}
		sleepForVelocity();
	}

	/**
	 * Utlizado para liberar o processador
	 */
	private void sleepForVelocity() {
		try {
			Thread.sleep(10 * (100 - PainelResumo.sliderVelocidade.getValue()));
			PainelResumo.atualizaJLabelVelocidade();
		} catch (Exception e) {

		}
	}

	/**
	 * Método utilizado para zerar os contadores.
	 */
	private void inicializar() {
		this.pri1 = 0;
		this.pri2 = 0;
		this.pri3 = 0;
		this.ptVerif1 = new HashSet<PontoVerificacao>();
		this.ptVerif2 = new HashSet<PontoVerificacao>();
		this.ptVerif3 = new HashSet<PontoVerificacao>();
		this.avisosP1 = new HashSet<PontoVerificacao>();
		this.avisosP2 = new HashSet<PontoVerificacao>();
		this.avisosP3 = new HashSet<PontoVerificacao>();
	}

	/**
	 * Separa as prioridades do arquivo enviado
	 * 
	 * @param po
	 */
	private void separarPrioridade(ArrayList<PontoVerificacao> po) {

		for (PontoVerificacao conjErros : po) {
			int prioridade = conjErros.getPrioridade();
			char exigencia = conjErros.getExigencia();
			switch (prioridade) {
			case 1: {
				switch (exigencia) {
				case 'e': // ERRO
					ptVerif1.add(conjErros);
					break;
				default: // AVISO OU GENERICO
					avisosP1.add(conjErros);
					break;
				}
			}
				break;
			case 2: {
				switch (exigencia) {
				case 'e': // ERRO
					ptVerif2.add(conjErros);
					break;
				default: // AVISO OU GENERICO
					avisosP2.add(conjErros);
					break;
				}
			}
				break;
			case 3: {
				switch (exigencia) {
				case 'e': // ERRO
					ptVerif3.add(conjErros);
					break;
				default: // AVISO OU GENERICO
					avisosP3.add(conjErros);
					break;
				}
			}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Principal método para a avaliação do sistema, ele apresenta os erros que
	 * o sistema advem Método que retorna uma instancia de ArrayList<PontoVerificacao>
	 * 
	 * para cada ponto de verificação ele agrupa todos as ocorrencias de erros
	 * neste mesmo ponto adicionando n linhas onde ocorre este erro
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ExceptionMariano
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<PontoVerificacao> marcacao() throws ClassNotFoundException, SQLException, Exception {
		ArrayList<PontoVerificacao> erros = new ArrayList<PontoVerificacao>();
		String nucleo = "Antigo";
		nucleo = "Estruturado";
		ArrayList<Object> validados = new ArrayList<Object>();
		validados = (ArrayList<Object>) escolheNucleo(nucleo);
		progressoPaginaAtual(28);
		PontoVerificacao pv = null;
		ArmazenaErroOuAviso erroOuAviso = null;
		for (Object erro : validados) {			
			char flagErroOuAviso = ' ';
			if (nucleo.equals("Antigo")) {
				pv = new PontoVerificacao(pontosVerificacao.get(((ArmazenaErroOuAvisoAntigo) erro).getPv3()));
			} else {
				// PARA QUALQUER OUTRO NUCLEO
				erroOuAviso = (ArmazenaErroOuAviso) erro;
				String tagCompleta = erroOuAviso.getTagCompleta();
				String codigoRegra = erroOuAviso.getIdTextoRegra();
				String texto = nucleox.getRegras().getTextoRegra(codigoRegra);
				int idRegra = 0;
				int prioridade = nucleox.getRegras().getPrioridadeRegra(codigoRegra);
				String tagName="";
				/*
				 * Pega o nome da tag
				 */
				if (tagCompleta.indexOf(" ") != -1) {
					tagName = tagCompleta.substring(1, tagCompleta.indexOf(" "));
				} else if (tagCompleta.indexOf(">") != -1) {
					tagName = tagCompleta.substring(1, tagCompleta.indexOf(">"));
				} else if (tagCompleta.indexOf(">") == -1) {
					//Aviso sem tag ou algum erro
					tagName = tagCompleta;
				}
				ArrayList<Integer> nlinhas = new ArrayList<Integer>();
				nlinhas.add(erroOuAviso.getLinha());
				String tagTratada =  tagCompleta.replaceAll("'", "''");
				int idPagBanco = relatorio.getPagIdBanco(),linha=erroOuAviso.getLinha(), coluna = erroOuAviso.getColuna();
				if (((ArmazenaErroOuAviso) erro).isAviso()) {
					flagErroOuAviso = 'a';
				} else {
					flagErroOuAviso = 'e';
					//evitar guardar os avisos por uma questao de demora
					bancoSite.insertTabelaErro(codigoRegra, idPagBanco, prioridade,tagTratada, linha, coluna, tagName,flagErroOuAviso);
				}
				pv = new PontoVerificacao(texto, idRegra, prioridade, codigoRegra, nlinhas, String.valueOf(flagErroOuAviso));
				pv.setWcagEmag(nucleox.getRegras().getCodWcagEmag());
			}
			if (pv != null) {
				int exigencia = pv.getExigencia();
				int prioridade = pv.getPrioridade();
				switch (exigencia) {
				case 'e':
					switch (prioridade) {
					case 1:
						pri1++;
						break;
					case 2:
						pri2++;
						break;
					case 3:
						pri3++;
						break;
					default:
						new Throwable("Erro na contagem de prioridades");
					}
					break;
				case 'a':
					switch (prioridade) {
					case 1:
						ap1++;
						break;
					case 2:
						ap2++;
						break;
					case 3:
						ap3++;
						break;
					default:
						new Throwable("Erro na contagem de prioridades");
					}
					break;
				}
				int linha, coluna, tagLen;
				String umaTagInteira = "";
				if (nucleo.equals("Antigo")) {
					linha = ((ArmazenaErroOuAvisoAntigo) erro).getPosicao().getLinha() + 1;
					coluna = 0;
					tagLen = 0;

				} else {
					linha = erroOuAviso.getLinha();
					coluna = erroOuAviso.getColuna();
					tagLen = erroOuAviso.getTagCompleta().length();
					umaTagInteira = erroOuAviso.getTagCompleta();
				}
				/*
				 * Agrupa os pontos
				 */
				if (erros.contains(pv)) {
					/*
					 * o ponto já existe, agrupa a ocorrencia nele
					 */
					erros.get(erros.indexOf(pv)).getLinhas().add(linha);
					erros.get(erros.indexOf(pv)).getColunas().add(coluna);
					erros.get(erros.indexOf(pv)).getTagLength().add(tagLen);
					// ver se não ocupa muita memoria
					erros.get(erros.indexOf(pv)).getTagInteira().add(umaTagInteira);
					if (flagErroOuAviso == 'e') {
						erros.get(erros.indexOf(pv)).getAvisoOuErro().add("e");
					} else {
						erros.get(erros.indexOf(pv)).getAvisoOuErro().add("a");
					}
				} else {
					/*
					 * O ponto não existe, criamos um novo
					 */
					ArrayList<Integer> linhas = new ArrayList<Integer>();
					linhas.add(linha);
					ArrayList<Integer> colunas = new ArrayList<Integer>();
					colunas.add(linha);
					ArrayList<Integer> tagLengths = new ArrayList<Integer>();
					tagLengths.add(linha);
					// ver se não ocupa muita memoria
					ArrayList<String> tagInteira = new ArrayList<String>();
					tagInteira.add(umaTagInteira);
					ArrayList<String> avOuErr = new ArrayList<String>();
					if (flagErroOuAviso == 'e') {
						avOuErr.add("e");
					} else {
						avOuErr.add("a");
					}
					pv.setLinhas(linhas);
					pv.setColunas(colunas);
					pv.setTagLength(tagLengths);
					pv.setAvisoOuErro(avOuErr);
					pv.setTagInteira(tagInteira);
					erros.add(pv);
				}
			}
		}
		return erros;
	}

	private Object escolheNucleo(String nucleo) throws ClassNotFoundException, SQLException, Exception {
		nucleox = MethodFactNucleos.mFNucleos(nucleo);
		if (nucleo.equals("Antigo")) {
			return nucleox.getValidados(relatorio, props);
		} else {
			nucleox.setCodHTML(relatorio.getConteudo().toString());
			if (props.getProperty("tipo_avaliacao").equals("1")) {
				nucleox.setWCAGEMAG(InterfNucleos.WCAG);
			} else {
				nucleox.setWCAGEMAG(InterfNucleos.EMAG);
			}
			nucleox.avalia();
			/*
			 * precisa atender o preenchimento da interface validado 
			 * e este estar num array
			 */
			return nucleox.getErroOuAviso();
		}
	}

	public boolean isProcErroAtivo() {
		return procErroAtivo;
	}

	public void setProcErroAtivo(boolean procErroAtivo) {
		this.procErroAtivo = procErroAtivo;
	}

	public void setStartTime() {
		Date startDate = new Date();
		startTime = startDate.getTime();
	}

	public long getStartTime() {
		return startTime;
	}
}
