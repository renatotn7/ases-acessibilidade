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

package br.org.acessobrasil.silvinha.vista.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.util.GeraMapDir;
import br.org.acessobrasil.silvinha.util.GeradorMapaSite;
import br.org.acessobrasil.silvinha.util.LeitorDeTemporarios;
import br.org.acessobrasil.silvinha.util.ThreadParaGerente;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.BotaoColorido;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.componentes.SalvarArquivoFileChooser;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaCtrl;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.PaginasNaoAnalisadas;
import br.org.acessobrasil.silvinha.vista.listeners.PainelResumoMouseListener;
import br.org.acessobrasil.silvinha.vista.tableComponents.DefaulTableModelNotEditable;
import br.org.acessobrasil.silvinha.vista.tableComponents.ResumoTableModel;
import br.org.acessobrasil.silvinha2.mli.ResumoPanel;
import br.org.acessobrasil.silvinha2.mli.Silvinha;
import br.org.acessobrasil.silvinha2.projetodosite.ConfiguracaoDaAvaliacao;
/**
 * Exibe o resumo da avaliação de várias páginas 
 *
 */
public class PainelResumo extends SuperPainelCentral implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3273032414748667618L;

	public static int valorVelocidade;

	public static int valorporcAvaliacaoTotal;

	public static int valorProgressBarPaginaAtual;

	private JButton finalizar;

	// private JButton visualizar;

	private JButton pausar;

	public static JButton continuar;

	private JButton nao_avaliados;

	// private JButton btn_prox;

	// private JButton btn_ant;

	private JButton btn_play;

	private JButton btn_back;

	private JButton btn_last;

	public static JComboBox tableNumber;

	private JButton btn_first;

	public FrameSilvinha frameSilvinha;

	public static boolean desabilitarBtnContinuar;

	public static ResumoDoRelatorio resumo;

	public static JSlider sliderVelocidade;

	public static JProgressBar progressBarAvTotal;

	public static JProgressBar progressBarPaginaAtual;

	public static JTable table;

	// private Color cor2 = new Color(12, 34, 34);

	public JScrollPane scrollPane;

	public RelatorioDaUrl rel = new RelatorioDaUrl();

	DefaulTableModelNotEditable dtm = new DefaulTableModelNotEditable();

	public static JLabel completo;

	public static JLabel paginaAtual;

	public static JLabel velocidade;

	public static int contaRelatorio;

	public static boolean podeAdicionarLinhaemTabela;

	/**
	 * Lista para guardar o código do relatório para recuperar do HD referente
	 * ao HashCode
	 */
	private ArrayList<String> relatorio_Cod;

	private boolean pausado;

	public PainelResumo(final FrameSilvinha parentFrame, ResumoDoRelatorio pResumo, boolean somenteResumo) {
		super(new BorderLayout());
		EstadoSilvinha.conteudoEmPainelResumo = true;
		ResumoPanel.carregaTexto(TokenLang.LANG);

		this.relatorio_Cod = new ArrayList<String>();
		podeAdicionarLinhaemTabela = false;
		contaRelatorio = 0;

		resumo = pResumo;
		this.frameSilvinha = parentFrame;
		this.frameSilvinha.setTitle(ResumoPanel.TITULO);
		configuraMenuSilvinha();

		this.setBackground(CoresDefault.getCorPaineis());
		adicionaBorda();

		inicializaTabelaEScrollpane();

		JPanel panelAcao = inicializaPainelBotoes();
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1, 10));
		panel2.setBackground(CoresDefault.getCorPaineis());
		JPanel panel3 = new JPanel();
		JPanel espacadorHorizontal = new JPanel();
		espacadorHorizontal.setBackground(CoresDefault.getCorPaineis());
		panel3.setLayout(new BorderLayout());
		panel3.setBackground(CoresDefault.getCorPaineis());

		criaBotaoContinuar(somenteResumo, panelAcao);
		criaBotaoPausar(somenteResumo, panelAcao);

		criaEspacoEntreBotoes(panelAcao);

		criaPainelControle(panelAcao);

		criaEspacoEntreBotoes(panelAcao);

		criaBotaoNaoAvaliados(panelAcao);

		criaBotaoFinalizar(panelAcao);

		inicializaDropDown();
		completo = new JLabel(ResumoPanel.AVALIACAO_TOTAL + valorporcAvaliacaoTotal + "%");
		completo.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(completo);

		progressBarAvTotal = new JProgressBar();
		progressBarAvTotal.setValue(valorporcAvaliacaoTotal);

		progressBarAvTotal.setBackground(CoresDefault.getCorPaineis());

		panel2.add(progressBarAvTotal);
		progressBarPaginaAtual = new JProgressBar();
		progressBarPaginaAtual.setBackground(CoresDefault.getCorPaineis());
		progressBarPaginaAtual.setValue(valorProgressBarPaginaAtual);
		paginaAtual = new JLabel(ResumoPanel.PAGINA_ATUAL + valorProgressBarPaginaAtual + "%");
		paginaAtual.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(paginaAtual);
		panel2.add(progressBarPaginaAtual);

		sliderVelocidade = new JSlider();
		sliderVelocidade.setBackground(CoresDefault.getCorPaineis());
		sliderVelocidade.setValue(valorVelocidade);

		velocidade = new JLabel(ResumoPanel.VELOCIDADE + sliderVelocidade.getValue() + "%");
		velocidade.setText(ResumoPanel.VELOCIDADE + sliderVelocidade.getValue() + "%");
		velocidade.setHorizontalAlignment(SwingConstants.CENTER);

		panel2.add(velocidade);
		panel2.add(sliderVelocidade);

		panel3.add(panelAcao, BorderLayout.NORTH);

		panel3.add(espacadorHorizontal, BorderLayout.CENTER);

		panel3.add(panel2, BorderLayout.SOUTH);

		this.add(panel3, BorderLayout.SOUTH);
		
		this.setVisible(true);
		
		tabelafinal();
		/*
		 * Não funciona!
		 * pausar.requestFocusInWindow(); 
		 * pausar.requestFocus();
		 * frameSilvinha.setVisible(true);
		 * this.frameSilvinha.getOwner()
		 * pausar.setFocusCycleRoot(true);
		 * pausar.requestDefaultFocus();
		 */
		//Não funciona, mas ficamos com este que é o do tutorial da Sun
		pausar.requestFocusInWindow();
	}

	private JPanel inicializaPainelBotoes() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 10));
		panel.setBackground(CoresDefault.getCorPaineis());
		return panel;
	}

	private void criaEspacoEntreBotoes(JPanel panel) {

		panel.add(new JLabel());

	}

	private void criaBotaoContinuar(boolean somenteResumo, JPanel panel) {
		ResumoPanel.carregaTexto(TokenLang.LANG);
		continuar = new JButton(ResumoPanel.BTN_CONTINUAR);
		continuar.setActionCommand("CONTINUAR");
		continuar.setToolTipText(ResumoPanel.DICA_BTN_CONTINUAR);
		continuar.addActionListener(this);
		if (isDesabilitarBtnContinuar())
			desabilitaBtnContinuar();

		if (somenteResumo) {

		}
		panel.add(continuar);
	}

	public static void habilitaBtnContinuar() {
		try {
			continuar.setEnabled(true);
		} catch (Exception e) {

		}
	}

	public static void desabilitaBtnContinuar() {
		try {
			continuar.setEnabled(false);
		} catch (Exception e) {

		}
	}

	private void inicializaDropDown() {
		tableNumber.addItem(1);
		for (int pagina = 2; pagina <= ResumoDoRelatorio.getTotPage(); pagina++) {
			tableNumber.addItem(pagina);

		}
	}

	private void criaBotaoFinalizar(JPanel panel) {
		ResumoPanel.carregaTexto(TokenLang.LANG);
		finalizar = new BotaoColorido(ResumoPanel.BTN_FINALIZAR, BotaoColorido.VERMELHO);
		finalizar.setToolTipText(ResumoPanel.DICA_BTN_FINALIZAR);
		finalizar.setActionCommand("FINALIZAR");
		finalizar.addActionListener(this);
		panel.add(finalizar);
	}

	private void criaBotaoNaoAvaliados(JPanel panel) {
		ResumoPanel.carregaTexto(TokenLang.LANG);
		nao_avaliados = new BotaoColorido(ResumoPanel.BTN_PROBLEMAS, BotaoColorido.VERMELHO);
		nao_avaliados.setToolTipText(ResumoPanel.DICA_BTN_PROBLEMAS);
		nao_avaliados.setActionCommand("NAO_AVALIADOS");
		nao_avaliados.addActionListener(this);
		panel.add(nao_avaliados);
	}

	private void criaBotaoPausar(boolean somenteResumo, JPanel panel) {
		ResumoPanel.carregaTexto(TokenLang.LANG);
		pausar = new JButton(ResumoPanel.BTN_PAUSAR);
		pausar.setToolTipText(ResumoPanel.DICA_PAUSAR);
		pausar.setActionCommand("PAUSAR");
		pausar.addActionListener(this);
		// if (somenteResumo) {
		// pausar.setEnabled(false);
		// }
		panel.add(pausar);
	}

	private void configuraMenuSilvinha() {
		MenuSilvinha.habilitaSalvar();
		MenuSilvinha.habilitaPdf();
		MenuSilvinha.desabilitaAbrir();
		MenuSilvinha.desabilitaExecutarAgora();
	}

	private void adicionaBorda() {
		ResumoPanel.carregaTexto(TokenLang.LANG);
		Border lineBorder = BorderFactory.createLineBorder(new Color(0, 0, 0), 0);
		// this.setBorder(BorderFactory.createTitledBorder(lineBorder,
		// TokenLang.BORDER_LBL_RESUMO));
		this.setBorder(BorderFactory.createTitledBorder(lineBorder, ResumoPanel.BORDA_ROTULO_RESUMO));
	}

	private void inicializaTabelaEScrollpane() {
		ResumoTableModel rtb = new ResumoTableModel(resumo.getRelatorios());
		table = new JTable(rtb);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(757, 565));
		table.setBackground(CoresDefault.getCorPaineis());
		table.addMouseListener(new PainelResumoMouseListener(this));

		table.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					ActionEvent ae = new ActionEvent(this, 0, "VISUALIZAR");
					actionPerformed(ae);
				}
			}
			public void keyReleased(KeyEvent arg0) {
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});
		TableColumn column = null;
		for (int i = 0; i < 4; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setResizable(false);

			if (i == 0) {
				column.setPreferredWidth(500); // coluna da url

			} else {
				column.setPreferredWidth(80); // colunas das prioridades

			}
		}

		// Create the scroll pane and add the table to it.
		scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		this.add(scrollPane, BorderLayout.CENTER);
	}

	private void criaPainelControle(JPanel panel) {
		JPanel pcontroles1 = new JPanel();
		JPanel pcontroles2 = new JPanel();
		JPanel pcontroles3 = new JPanel();

		pcontroles1.setLayout(new GridLayout(1, 5));
		pcontroles1.setBackground(CoresDefault.getCorPaineis());
		pcontroles2.setLayout(new GridLayout(1, 5));
		pcontroles2.setBackground(CoresDefault.getCorPaineis());
		pcontroles3.setLayout(new GridLayout(1, 5));
		pcontroles3.setBackground(CoresDefault.getCorPaineis());

		btn_play = new JButton();
		btn_back = new JButton();
		btn_last = new JButton();
		tableNumber = new JComboBox();
		btn_first = new JButton();

		ImageIcon icon = new ImageIcon("imagens/btn_play.jpg");
		btn_play.setBorder(null);
		btn_play.setMargin(null);

		btn_play.setBackground(CoresDefault.getCorPaineis());
		btn_play.setIcon(icon);
		btn_play.setText("");

		icon = new ImageIcon("imagens/btn_back.jpg");

		btn_back.setBorder(null);
		btn_back.setMargin(null);
		btn_back.setIcon(icon);
		btn_back.setText("");
		btn_back.setBackground(CoresDefault.getCorPaineis());
		icon = new ImageIcon("imagens/btn_last.jpg");

		btn_last.setBorder(null);
		btn_last.setBackground(CoresDefault.getCorPaineis());
		btn_last.setMargin(null);
		btn_last.setIcon(icon);
		btn_last.setText("");

		icon = new ImageIcon("imagens/btn_first.jpg");
		btn_first.setBorder(null);
		btn_first.setBackground(CoresDefault.getCorPaineis());
		btn_first.setMargin(null);
		btn_first.setIcon(icon);

		btn_first.setText("");

		btn_play.setActionCommand("ProxPage");
		btn_play.setToolTipText(ResumoPanel.DICA_BTN_PLAY);
		btn_back.setActionCommand("AntPage");
		btn_back.setToolTipText(ResumoPanel.DICA_BTN_BACK);
		btn_last.setActionCommand("UltPage");
		btn_last.setToolTipText(ResumoPanel.DICA_BTN_LAST);
		btn_first.setActionCommand("PrimPage");
		btn_first.setToolTipText(ResumoPanel.DICA_BTN_FIRST);

		btn_play.addActionListener(this);
		btn_back.addActionListener(this);
		btn_last.addActionListener(this);
		btn_first.addActionListener(this);

		pcontroles1.add(btn_first);

		tableNumber.setEditable(true);
		tableNumber.setActionCommand("jcombobox");
		tableNumber.setToolTipText(ResumoPanel.DICA_NUM_PAG);
		tableNumber.addActionListener(this);

		pcontroles1.add(btn_back);
		pcontroles2.add(tableNumber);
		pcontroles3.add(btn_play);
		pcontroles3.add(btn_last);

		panel.add(pcontroles1);
		panel.add(pcontroles2);
		panel.add(pcontroles3);
	}

	/**
	 * Ações
	 */
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		if (command.equals("FINALIZAR")) {
			/*
			 * Pergunta se salva
			 */
			if (!continuar.isEnabled()) {
				if (!salvaRelatorio()) {
					return;
				}
			}

			/*
			 * Indica os thread(s) e a outros para parar a avaliacao
			 */
			FrameSilvinha.stopAvaliacao = true;

			valorVelocidade = 0;
			valorporcAvaliacaoTotal = 0;
			valorProgressBarPaginaAtual = 0;
			// no caso de finalizar
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
				podeAdicionarLinhaemTabela = false;
				GeraMapDir.pararAvaliacao();
				// Gerente.parar=true;
				Gerente gerente = new Gerente();
				if (Gerente.profundidade < 5) {
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

				this.setVisible(false);
				this.removeAll();
				EstadoSilvinha.conteudoEmPainelResumo = false;
				frameSilvinha.showPainelAvaliacao();
				ResumoPanel.carregaTexto(TokenLang.LANG);
				if (Gerente.isEstouroMemoria()) {
					int i = JOptionPane.showConfirmDialog(this, ResumoPanel.CONTINUAR_AVALIACAO_ANTERIOR);
					if (i == JOptionPane.YES_OPTION) {
						PainelAvaliacao.continuarAvaliacaoAnterior();
						return;
					}
				}
				PainelStatusBar.hideProgressBar();
				MenuSilvinha.desabilitaSalvar();
				MenuSilvinha.desabilitaPdf();
				MenuSilvinha.habilitaAbrir();
				// visualizar.setEnabled(true);
				pausar.setEnabled(true);
				PainelStatusBar.setFinalizado(true);
				// ResumoPanel.carregaTexto(TokenLang.LANG);
				PainelStatusBar.setText(ResumoPanel.AVALICAO_INTER_USUARIO);
			} catch (Exception ex) {
				// Erro de AWT
				ex.printStackTrace();
				// Ir para o painel de avaliacao assim mesmo
				frameSilvinha.showPainelAvaliacao();
				PainelStatusBar.hideProgressBar();
				MenuSilvinha.desabilitaSalvar();
				MenuSilvinha.desabilitaPdf();
				MenuSilvinha.habilitaAbrir();
				pausar.setEnabled(true);
				PainelStatusBar.setFinalizado(true);
				ResumoPanel.carregaTexto(TokenLang.LANG);
				PainelStatusBar.setText(ResumoPanel.AVALICAO_INTER_USUARIO);
			}
			// ThreadParaGerente.gerente.parar=false;
		} else if (command.equals("VISUALIZAR")) {
			/*
			 * O Usuário vai visualizar os erros em detalhes FAVOR NÃO APAGAR
			 * ESTA ACTION!!!
			 */
			frameSilvinha.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			valorVelocidade = sliderVelocidade.getValue();
			int row = table.getSelectedRow();
			if (row != -1) {
				// Recuperar os objetos aqui somente
				if (Gerente.profundidade > 5) {
					rel = ThreadParaGerente.relatorios.get(row);
					LeitorDeTemporarios leitorDeTemporarios = new LeitorDeTemporarios();
					rel = leitorDeTemporarios.leiaTemporario(this.relatorio_Cod.get(row));
				} else {
					rel = new RelatorioDaUrl();
					rel.recarregaArquivoRelatorioEmXml2(this.relatorio_Cod.get(row));
				}
				if (rel.hashCodeString == null) {
					rel.hashCodeString = this.relatorio_Cod.get(row);
				}
				this.removeAll();
				frameSilvinha.showPainelRelatorio(rel);
				frameSilvinha.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		} else if (command.equals("PAUSAR")) {
			// pausa a avalição
			pausado = true;
			habilitaBtnContinuar();
			GeradorMapaSite.setPausar(true);

		} else if (command.equals("ProxPage")) {
			// ir para a proxima página
			if (ResumoDoRelatorio.getIndicePg() + 1 <= ResumoDoRelatorio.getTotPage()) {
				parseTable(ResumoDoRelatorio.getPage(ResumoDoRelatorio.getIndicePg() + 1));
				tableNumber.setSelectedItem(ResumoDoRelatorio.getIndicePg());
			}
		} else if (command.equals("AntPage")) {
			// ir para a pagina anterior
			if (ResumoDoRelatorio.getIndicePg() - 1 > 0) {
				parseTable(ResumoDoRelatorio.getPage(ResumoDoRelatorio.getIndicePg() - 1));
				tableNumber.setSelectedItem(ResumoDoRelatorio.getIndicePg());
			}
		} else if (command.equals("UltPage")) {
			// ir para a ultima pagina
			parseTable(ResumoDoRelatorio.getPage(ResumoDoRelatorio.getTotPage()));
			tableNumber.setSelectedItem(ResumoDoRelatorio.getTotPage());
		} else if (command.equals("PrimPage")) {
			// ir para a primeira pagina
			parseTable(ResumoDoRelatorio.getPage(1));
			tableNumber.setSelectedItem(1);
		} else if (command.equals("jcombobox")) {
			if (Integer.parseInt(tableNumber.getSelectedItem().toString()) <= ResumoDoRelatorio.getTotPage() && Integer.parseInt(tableNumber.getSelectedItem().toString()) >= 1) {
				parseTable(ResumoDoRelatorio.getPage(Integer.parseInt(tableNumber.getSelectedItem().toString())));
			}
		} else if (command.equals("CONTINUAR")) {
			desabilitaBtnContinuar();
			if (pausado) {
				pausado = false;
				GeradorMapaSite.setPausar(false);
			} else {
				Properties opcoes = new Properties();
				ConfiguracaoDaAvaliacao configDaAvaliacao = new ConfiguracaoDaAvaliacao();
				configDaAvaliacao.loadConf();
				opcoes = configDaAvaliacao.getAsProperties();

				FrameSilvinha.stopAvaliacao = false;
				Gerente.profundidade = configDaAvaliacao.getNiveis();
				GeradorMapaSite.continuar = true;
				ThreadParaGerente threadParaGerente = new ThreadParaGerente(opcoes,frameSilvinha);
				new Thread(SilvinhaCtrl.processaErro, threadParaGerente);
				threadParaGerente.start();
			}
		} else if (command.equals("NAO_AVALIADOS")) {
			ResumoPanel.carregaTexto(TokenLang.LANG);
			if (PaginasNaoAnalisadas.relatorios.size() != 0) {
				new PaginasNaoAnalisadas();
			} else
				JOptionPane.showMessageDialog(this, ResumoPanel.SEM_CONTEUDO_EXIBIR);
		}
	}

	public void mostrarResumo() {
		frameSilvinha.showPainelResumo(resumo , false);
	}

	public void mostrarCorrecao(RelatorioDaUrl relatorio) {
		frameSilvinha.showPainelCorrecao(resumo, relatorio);
	}

	public static ResumoDoRelatorio getResumoDoRelatorio() {
		return resumo;
	}

	public JTable getTable() {
		return table;
	}

	/**
	 * Monta a tabela com os resumos
	 */
	private void tabelafinal() {
		parseTable(ResumoDoRelatorio.getActualPage());
	}

	/**
	 * Pega os dados em conteudo e coloca eles na tabela
	 */
	private void parseTable(String conteudo) {

		limpaTabela();

		adicionaAsColunas();
		this.relatorio_Cod.clear();
		String arr[] = conteudo.split("\n");
		int tot = arr.length;

		// if (ultPagAdic<=ResumoDoRelatorio.getTotPage()){

		// tableNumber.addItem(ResumoDoRelatorio.getTotPage());
		// ultPagAdic=ResumoDoRelatorio.getTotPage();}

		adicionaLinhasDaTabelaRequisitada(arr, tot);
		podeAdicionarLinhaemTabela = true;
		// */

	}

	private void adicionaLinhasDaTabelaRequisitada(String[] arr, int tot) {
		int i;
		for (i = 0; i < tot; i++) {

			String arr2[] = arr[i].split("\t");

			if (arr2.length == 8) {
				this.relatorio_Cod.add(arr2[0]);
				dtm.addRow(new Object[] { i + (ResumoDoRelatorio.getIndicePg() - 1) * ResumoDoRelatorio.results_pp + 1 + ": " + arr2[7], arr2[4],// Aviso1
						arr2[1],// Erro1
						arr2[5],// Aviso2
						arr2[2],// Erro2
						arr2[6],// Aviso3
						arr2[3] });// Erro3
				// if (ultPagAdic<=ResumoDoRelatorio.getTotPage()){
				// tableNumber.addItem(ResumoDoRelatorio.getTotPage());
				// ultPagAdic=ResumoDoRelatorio.getTotPage();
				// }
			}

			// contaRelatorio=i +
			// (ResumoDoRelatorio.getIndicePg()-1)*ResumoDoRelatorio.results_pp
			// + 1;
		}

	}

	private void adicionaAsColunas() {
		dtm.addColumn(ResumoPanel.URL);
		dtm.addColumn(ResumoPanel.AVISOS_P1);
		dtm.addColumn(ResumoPanel.ERROS_P1);
		dtm.addColumn(ResumoPanel.AVISOS_P2);
		dtm.addColumn(ResumoPanel.ERROS_P2);
		dtm.addColumn(ResumoPanel.AVISOS_P3);
		dtm.addColumn(ResumoPanel.ERROS_P3);
		table.getColumnModel().getColumn(0).setMinWidth(600);
	}

	private void limpaTabela() {
		dtm = null;
		dtm = new DefaulTableModelNotEditable();
		table.removeAll();
		table.setVisible(false);
		table.setVisible(true);
		table.setModel(dtm);
	}

	/*
	 * public void tabelafinal_bkp() {
	 * 
	 * table.setModel(dtm);
	 * 
	 * adicionaAsColunas();
	 * 
	 * if (Gerente.profundidade <= 5) { for (contaRelatorio = 0; contaRelatorio <
	 * resumo.relatorios.size(); contaRelatorio++) { rel =
	 * resumo.relatorios.get(contaRelatorio);
	 * this.relatorio_Cod.add(rel.hashCodeString); dtm.addRow(new Object[] {
	 * contaRelatorio + 1 + ": " + rel.getUrl(),
	 * rel.getAvisosPrioridade1().toString(),
	 * rel.getErrosPrioridade1().toString(),
	 * rel.getAvisosPrioridade2().toString(),
	 * rel.getErrosPrioridade2().toString(),
	 * rel.getAvisosPrioridade3().toString(),
	 * rel.getErrosPrioridade3().toString() }); } }
	 * 
	 * if (Gerente.profundidade == 6) { if (ThreadParaGerente.relatorios.size() !=
	 * 0) { rel = ThreadParaGerente.relatorios.get(0);
	 * this.relatorio_Cod.add(rel.hashCodeString); dtm.addRow(new Object[] { 1 + ": " +
	 * rel.getUrl(), rel.getAvisosPrioridade1().toString(),
	 * rel.getErrosPrioridade1().toString(),
	 * rel.getAvisosPrioridade2().toString(),
	 * rel.getErrosPrioridade2().toString(),
	 * rel.getAvisosPrioridade3().toString(),
	 * rel.getErrosPrioridade3().toString() }); } else {
	 * JOptionPane.showMessageDialog(this, ResumoPanel.NENHUM_RELATORIO_EXIBIR); }
	 * contaRelatorio = 0; }
	 * 
	 * if (Gerente.profundidade == 7) { for (contaRelatorio = 0; contaRelatorio <
	 * ThreadParaGerente.relatorios.size(); contaRelatorio++) { rel =
	 * ThreadParaGerente.relatorios.get(contaRelatorio);
	 * this.relatorio_Cod.add(rel.hashCodeString); dtm.addRow(new Object[] {
	 * contaRelatorio + 1 + ": " + rel.getUrl(),
	 * rel.getAvisosPrioridade1().toString(),
	 * rel.getErrosPrioridade1().toString(),
	 * rel.getAvisosPrioridade2().toString(),
	 * rel.getErrosPrioridade2().toString(),
	 * rel.getAvisosPrioridade3().toString(),
	 * rel.getErrosPrioridade3().toString() }); } }
	 * 
	 * podeAdicionarLinhaemTabela = true;
	 * 
	 * }//
	 */

	/**
	 * Adiciona uma linha no resumo do relatório, independente do objeto
	 * relatório
	 */
	public void addRow(String hashCodeString, String url, int avisos_p1, int erros_p1, int avisos_p2, int erros_p2, int avisos_p3, int erros_p3) {
		this.relatorio_Cod.add(hashCodeString);

		dtm.addRow(new Object[] { ResumoDoRelatorio.getTotalLinks() + ": " + url, String.valueOf(avisos_p1), String.valueOf(erros_p1), String.valueOf(avisos_p2),
				String.valueOf(erros_p2), String.valueOf(avisos_p3), String.valueOf(erros_p3) });

	}

	/*
	 * Adiciona uma linha no resumo do relatório
	 */
	/*
	 * public void addLinha() { if (!FrameSilvinha.painelResumo) return; if
	 * (Gerente.profundidade <= 5) { for (contaRelatorio = contaRelatorio;
	 * contaRelatorio < resumo.relatorios.size(); contaRelatorio++) { //
	 * rel=ThreadParaGerente.relatorios.get(contaRelatório); rel =
	 * ResumoDoRelatorio.relatorios.get(contaRelatorio);
	 * this.relatorio_Cod.add(rel.hashCodeString); dtm.addRow(new Object[] {
	 * contaRelatorio + 1 + ": " + rel.getUrl(),
	 * rel.getAvisosPrioridade1().toString(),
	 * rel.getErrosPrioridade1().toString(),
	 * rel.getAvisosPrioridade2().toString(),
	 * rel.getErrosPrioridade2().toString(),
	 * rel.getAvisosPrioridade3().toString(),
	 * rel.getErrosPrioridade3().toString() }); // limpa o relatório //
	 * System.out.print("Limpando o relatorio\n"); rel = null; } } if
	 * (Gerente.profundidade > 5) { for (contaRelatorio = contaRelatorio;
	 * contaRelatorio < ThreadParaGerente.relatorios.size(); contaRelatorio++) {
	 * rel = ThreadParaGerente.relatorios.get(contaRelatorio);
	 * this.relatorio_Cod.add(rel.hashCodeString); dtm.addRow(new Object[] {
	 * contaRelatorio + 1 + ": " + rel.getUrl(),
	 * rel.getAvisosPrioridade1().toString(),
	 * rel.getErrosPrioridade1().toString(),
	 * rel.getAvisosPrioridade2().toString(),
	 * rel.getErrosPrioridade2().toString(),
	 * rel.getAvisosPrioridade3().toString(),
	 * rel.getErrosPrioridade3().toString() }); podeAdicionarLinhaemTabela =
	 * true; } } }//
	 */

	public static boolean isDesabilitarBtnContinuar() {
		return desabilitarBtnContinuar;
	}

	public static void setDesabilitarBtnContinuar(boolean desabilitarBtnContinuar) {
		PainelResumo.desabilitarBtnContinuar = desabilitarBtnContinuar;
	}

	public static void atualizaJLabelVelocidade() {
		PainelResumo.velocidade.setText(ResumoPanel.VELOCIDADE + PainelResumo.sliderVelocidade.getValue() + "%");
	}

	/**
	 * Abre confirmação para salvar o relatório
	 * 
	 * @return true caso é para sair, false caso seja para ficar neste panel
	 */
	private boolean salvaRelatorio() {
		Gerente.pausa = true;
		Object[] options = { Silvinha.BTN_SIM, Silvinha.BTN_NAO, Silvinha.BTN_CANCELAR };
		ResumoDoRelatorio resumo = null;
		int option = JOptionPane.showOptionDialog(frameSilvinha, Silvinha.DESEJA_SALVAR, Silvinha.DICA_SALVAR_AVALIACAO, JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (option == 0) {
			resumo = PainelResumo.getResumoDoRelatorio();
			resumo.setGravaCompleto(true);
		} else if (option == 1) {
			JOptionPane.showMessageDialog(null, Silvinha.GRAVACAO_CANCELADA);
		} else if (option == 2) {
			return false;
		}
		if (option == JOptionPane.YES_OPTION && resumo != null) {
			new SalvarArquivoFileChooser(frameSilvinha, resumo);
		}
		return true;
	}

	@Override
	public boolean showBarraUrl() {
		return false;
	}

}