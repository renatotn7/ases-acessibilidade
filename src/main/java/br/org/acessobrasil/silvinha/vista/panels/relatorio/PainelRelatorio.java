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

package br.org.acessobrasil.silvinha.vista.panels.relatorio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xhtmlrenderer.simple.XHTMLPanel;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.negocio.ProcessarErro;
import br.org.acessobrasil.silvinha.negocio.ProcessarErro2;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.PainelRelatorioPorPrioridade;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.XHTML_Panel;
import br.org.acessobrasil.silvinha2.projetodosite.ConfiguracaoDaAvaliacao;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.SalvaAlteracoes;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
import br.org.acessobrasil.silvinha2.util.onChange.OnChange;
import br.org.acessobrasil.silvinha2.util.onChange.OnChangeListener;
/**
 * Painel de relatório dos erros de acessibilidade 
 *
 */
public class PainelRelatorio extends SuperPainelCentral implements ActionListener, OnChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6905878798704857547L;
	
	
	private JMenuItem miBtnSalvar;
	
	private JMenuBar menuBar;
	
	private JButton voltar;

	private JButton corrigir;

	private JButton voltarOrig;

	private JButton corrigirOrig;

	private JButton atualizarOrig;

	private JButton reverter;

	private JButton verPagina;

	private JButton salvar;
	
	private JTabbedPane tPnPnRelatorio;

	private static JScrollPane scrollPane;

	private static JTabbedPane tabs;

	private static JTabbedPane tabsAlterado;
	SalvaAlteracoes salvaAlteracoes;
	/**
	 * Relatorio que está sendo exibido
	 */
	public static RelatorioDaUrl relatorio;

	/**
	 * Relatório com alterações de correção no código
	 */
	public static RelatorioDaUrl relatorioAlterado;

	public static FrameSilvinha frameSilvinha;

	/**
	 * Tela em html da ajuda
	 */

	private XHTMLPanel ajuda = null;

	private XHTMLPanel ajudaAlterado = null;

	public static G_TextAreaSourceCode boxCode;

	public static G_TextAreaSourceCode boxCodeOriginal;

	/**
	 * Inicia o painel de relatório
	 * 
	 * @param relatorio
	 */
	public PainelRelatorio(RelatorioDaUrl relatorio, FrameSilvinha parentFrame) {
		EstadoSilvinha.hashCodeAtual=relatorio.hashCodeString;
		TxtBuffer.setHashCode(EstadoSilvinha.hashCodeAtual);
		frameSilvinha = parentFrame;
		frameSilvinha.painelRelatorio = this;
		tPnPnRelatorio = new JTabbedPane();
		tPnPnRelatorio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int p = tPnPnRelatorio.getSelectedIndex();

				if (p == 0) {
					EstadoSilvinha.painel_Atual = EstadoSilvinha.PAINEL_RELATORIO_EDICAO;
				} else if (p == 1) {
					EstadoSilvinha.painel_Atual = EstadoSilvinha.PAINEL_RELATORIO_ORIGINAL;
				}

			}
		});
		TradPainelRelatorio.carregaTexto(TokenLang.LANG);

		PainelRelatorio.relatorio = relatorio;

		EstadoSilvinha.setLinkAtual(relatorio.getUrl());

		this.setLayout(new GridLayout(1, 1));
		miBtnSalvar = new JMenuItem(XHTML_Panel.BTN_SALVAR);
		constroiPainelEdicao(relatorio, parentFrame);
		constroiPainelOriginal(relatorio, parentFrame);
		this.add(tPnPnRelatorio);
		this.setVisible(true);
		
	}

	/**
	 * Código alterado
	 * 
	 * @param relatorio
	 * @param parentFrame
	 */
	private void constroiPainelEdicao(RelatorioDaUrl relatorio, FrameSilvinha parentFrame) {

		JPanel painela = new JPanel();
		painela.setLayout(new BorderLayout());
		this.setBackground(CoresDefault.getCorPaineis());
		painela.setBackground(CoresDefault.getCorPaineis());
		Border lineBorder = BorderFactory.createLineBorder(new Color(0, 0, 0), 0);
		this.setBorder(BorderFactory.createTitledBorder(lineBorder, GERAL.RELATORIO_URL + " " + relatorio.getUrl()));
		tabsAlterado = new JTabbedPane();
		loadRelatorioAlterado();
		frameSilvinha.setJMenuBar(this.criaMenuBar());
		JPanel codigoEerros = new JPanel();
		codigoEerros.setLayout(new GridLayout(2, 1));
		codigoEerros.setBackground(parentFrame.corDefault);

		boxCode = new G_TextAreaSourceCode();

		boxCode.setTipoHTML();
		boxCode.getTextPane().setEditable(true);
		new OnChange(boxCode, this);
		boxCode.setBackground(parentFrame.corDefault);
		boxCode.setTitledBorder(GERAL.COD_FONTE);
		boxCode.setDeixaSelecionadoMarcado(false);

		boxCode.setText(relatorio.getConteudo().toString());

		codigoEerros.add(boxCode);
		codigoEerros.add(tabsAlterado);

		G_File arquivo = new G_File("ajuda/" + TokenLang.LANG + "/Ajuda.html");
		ajudaAlterado = new XHTMLPanel();
		try {
			ajudaAlterado.setDocument(arquivo.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSplitPane splitPane = null;
		if (ajudaAlterado != null) {
			Dimension minimumSize = new Dimension(0, 0);
			JScrollPane ajudaScrollPane = new JScrollPane(ajudaAlterado, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			ajudaScrollPane.setMinimumSize(minimumSize);
			ajudaScrollPane.setPreferredSize(new Dimension(150, 0));
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, codigoEerros, ajudaScrollPane);
			splitPane.setOneTouchExpandable(true);
			// splitPane.set
			// splitPane.setDividerLocation(0.95);
			int w = frameSilvinha.getWidth();
			int s = w / 4;
			splitPane.setDividerLocation(w - s);
			painela.add(splitPane, BorderLayout.CENTER);
			// this.add(splitPane, BorderLayout.CENTER);
		} else {
			painela.add(codigoEerros, BorderLayout.CENTER);
			// this.add(codigoEerros, BorderLayout.CENTER);
		}
		// this.add(tabs, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(50, 60));
		panel.setBackground(CoresDefault.getCorPaineis());
		
		salvar = new JButton(GERAL.BTN_SALVAR);
		salvar.setToolTipText(GERAL.DICA_SALVAR);
		salvar.setActionCommand("Salvar");
		salvar.addActionListener(this);
		panel.add(salvar);
		salvaAlteracoes = new SalvaAlteracoes(boxCode.getTextPane(),  new JButton(), new JMenuItem(), frameSilvinha );
		voltar = new JButton(GERAL.TELA_ANTERIOR);
		voltar.setToolTipText(GERAL.DICA_TELA_ANTERIOR);
		voltar.setActionCommand("VOLTAR");
		voltar.addActionListener(this);
		panel.add(voltar);

		verPagina = new JButton(GERAL.VER_PAGINA);
		verPagina.setActionCommand("VerPágina");
		verPagina.addActionListener(this);
		// panel.add(verPagina);

		corrigir = new JButton(GERAL.CORRIGIR);
		corrigir.setActionCommand("CORRIGIR");
		corrigir.addActionListener(this);

		reverter = new JButton(TradPainelRelatorio.REVERTER);
		reverter.setToolTipText(TradPainelRelatorio.DICA_REVERTER);
		reverter.setActionCommand("Reverter");
		reverter.addActionListener(this);
		panel.add(reverter);
		painela.add(panel, BorderLayout.SOUTH);
		tPnPnRelatorio.addTab(TradPainelRelatorio.CODIGO_EDICAO, painela);

		/*
		 * if(splitPane!=null){ int w=splitPane.getBounds().width;
		 * System.out.println("w="+w); splitPane.setDividerLocation(w);
		 * this.add(splitPane, BorderLayout.CENTER); }
		 */
	}

	private void constroiPainelOriginal(RelatorioDaUrl relatorio, FrameSilvinha parentFrame) {

		JPanel painela = new JPanel();
		painela.setLayout(new BorderLayout());
		this.setBackground(CoresDefault.getCorPaineis());
		painela.setBackground(CoresDefault.getCorPaineis());
		Border lineBorder = BorderFactory.createLineBorder(new Color(0, 0, 0), 0);
		this.setBorder(BorderFactory.createTitledBorder(lineBorder, GERAL.RELATORIO_URL + " " + relatorio.getUrl()));
		tabs = new JTabbedPane();
		loadRelatorio();
		frameSilvinha.setJMenuBar(this.criaMenuBar());
		JPanel codigoEerros = new JPanel();
		codigoEerros.setLayout(new GridLayout(2, 1));
		codigoEerros.setBackground(parentFrame.corDefault);

		boxCodeOriginal = new G_TextAreaSourceCode();

		boxCodeOriginal.setTipoHTML();
		boxCodeOriginal.getTextPane().setEditable(false);
		boxCodeOriginal.setBackground(parentFrame.corDefault);
		boxCodeOriginal.setTitledBorder(GERAL.COD_FONTE);
		boxCodeOriginal.setDeixaSelecionadoMarcado(false);
		G_File file = new G_File(relatorio.pathHD + "/" + relatorio.hashCodeString);
		boxCodeOriginal.setText(file.read());

		codigoEerros.add(boxCodeOriginal);
		codigoEerros.add(tabs);

		G_File arquivo = new G_File("ajuda/" + TokenLang.LANG + "/Ajuda.html");
		ajuda = new XHTMLPanel();
		try {
			ajuda.setDocument(arquivo.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSplitPane splitPane = null;
		if (ajuda != null) {
			Dimension minimumSize = new Dimension(0, 0);
			JScrollPane ajudaScrollPane = new JScrollPane(ajuda, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			ajudaScrollPane.setMinimumSize(minimumSize);
			ajudaScrollPane.setPreferredSize(new Dimension(150, 0));
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, codigoEerros, ajudaScrollPane);
			splitPane.setOneTouchExpandable(true);
			// splitPane.set
			// splitPane.setDividerLocation(0.95);
			int w = frameSilvinha.getWidth();
			int s = w / 4;
			splitPane.setDividerLocation(w - s);
			painela.add(splitPane, BorderLayout.CENTER);
			// this.add(splitPane, BorderLayout.CENTER);
		} else {
			painela.add(codigoEerros, BorderLayout.CENTER);
			// this.add(codigoEerros, BorderLayout.CENTER);
		}
		// this.add(tabs, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(50, 60));
		panel.setBackground(CoresDefault.getCorPaineis());
		// voltarOrig = new JButton(GERAL.VOLTAR);
		voltarOrig = new JButton(GERAL.TELA_ANTERIOR);
		voltarOrig.setToolTipText(GERAL.DICA_TELA_ANTERIOR);
		voltarOrig.setActionCommand("VOLTAR_ORIG");
		voltarOrig.addActionListener(this);
		panel.add(voltarOrig);

		verPagina = new JButton(GERAL.VER_PAGINA);
		verPagina.setActionCommand("VerPágina");
		verPagina.addActionListener(this);
		// panel.add(verPagina);

		corrigir = new JButton(GERAL.CORRIGIR);
		corrigir.setActionCommand("CORRIGIR");
		corrigir.addActionListener(this);

		atualizarOrig = new JButton(GERAL.ATUALIZAR);
		atualizarOrig.setToolTipText(GERAL.DICA_ATUALIZAR);
		atualizarOrig.setActionCommand("Atualizar_ORIG");
		atualizarOrig.addActionListener(this);
		panel.add(atualizarOrig);

		// panel.add(corrigir);
		painela.add(panel, BorderLayout.SOUTH);
		tPnPnRelatorio.addTab(TradPainelRelatorio.CODIGO_ORIGINAL, painela);

		/*
		 * if(splitPane!=null){ int w=splitPane.getBounds().width;
		 * System.out.println("w="+w); splitPane.setDividerLocation(w);
		 * this.add(splitPane, BorderLayout.CENTER); }
		 */
	}

	private void loadRelatorio() {
		EstadoSilvinha.hashCodeAtual = relatorio.hashCodeString;
		if (relatorio.mostraP1()) {
			if (relatorio.getErrosPrioridade1() > 0) {
				tabs.add(new PainelRelatorioPorPrioridade(relatorio.getListaErrosP1(), "ErroP1", this), TradPainelRelatorio.ERROS_P1);
			}
			if (relatorio.getAvisosPrioridade1() > 0) {
				tabs.add(new PainelRelatorioPorPrioridade(relatorio.getListaAvisosP1(), "AvisoP1", this), TradPainelRelatorio.AVISOS_P1);
			}
		}
		if (relatorio.mostraP2()) {
			if (relatorio.getErrosPrioridade2() > 0) {
				tabs.add(new PainelRelatorioPorPrioridade(relatorio.getListaErrosP2(), "ErroP2", this), TradPainelRelatorio.ERROS_P2);
			}
			if (relatorio.getAvisosPrioridade2() > 0) {
				tabs.add(new PainelRelatorioPorPrioridade(relatorio.getListaAvisosP2(), "AvisoP2", this), TradPainelRelatorio.AVISOS_P2);
			}
		}
		if (relatorio.mostraP3()) {
			if (relatorio.getErrosPrioridade3() > 0) {
				tabs.add(new PainelRelatorioPorPrioridade(relatorio.getListaErrosP3(), "ErroP3", this), TradPainelRelatorio.ERROS_P3);
			}
			if (relatorio.getAvisosPrioridade3() > 0) {
				tabs.add(new PainelRelatorioPorPrioridade(relatorio.getListaAvisosP3(), "AvisoP3", this), TradPainelRelatorio.AVISOS_P3);
			}
		}
		// scrollPane = new JScrollPane(new
		// PainelCodigoFonte(relatorio.getConteudo()));
		// scrollPane.getVerticalScrollBar().setUnitIncrement(70);
		// tabs.add(scrollPane, TokenLang.LBL_CODIGO_FONTE);

	}

	public static void loadRelatorioAlterado() {
		relatorioAlterado = new RelatorioDaUrl();
		relatorioAlterado.setConteudo(relatorio.getConteudo());
		relatorioAlterado.setUrl("");
		ProcessarErro2 pErroAlteracao = new ProcessarErro2(relatorioAlterado);
		pErroAlteracao.parseWAI();

		tabsAlterado = new JTabbedPane();
		tabsAlterado.setVisible(false);
		if (relatorioAlterado.mostraP1()) {
			if (relatorioAlterado.getErrosPrioridade1() > 0) {
				tabsAlterado.add(new PainelRelatorioPorPrioridade(relatorioAlterado.getListaErrosP1(), "ErroP1", frameSilvinha.painelRelatorio), TradPainelRelatorio.ERROS_P1);
			}
			if (relatorioAlterado.getAvisosPrioridade1() > 0) {
				tabsAlterado.add(new PainelRelatorioPorPrioridade(relatorioAlterado.getListaAvisosP1(), "AvisoP1", frameSilvinha.painelRelatorio), TradPainelRelatorio.AVISOS_P1);
			}
		}
		if (relatorioAlterado.mostraP2()) {
			if (relatorioAlterado.getErrosPrioridade2() > 0) {
				tabsAlterado.add(new PainelRelatorioPorPrioridade(relatorioAlterado.getListaErrosP2(), "ErroP2", frameSilvinha.painelRelatorio), TradPainelRelatorio.ERROS_P2);
			}
			if (relatorioAlterado.getAvisosPrioridade2() > 0) {
				tabsAlterado.add(new PainelRelatorioPorPrioridade(relatorioAlterado.getListaAvisosP2(), "AvisoP2", frameSilvinha.painelRelatorio), TradPainelRelatorio.AVISOS_P2);
			}
		}
		if (relatorioAlterado.mostraP3()) {
			if (relatorioAlterado.getErrosPrioridade3() > 0) {
				tabsAlterado.add(new PainelRelatorioPorPrioridade(relatorioAlterado.getListaErrosP3(), "ErroP3", frameSilvinha.painelRelatorio), TradPainelRelatorio.ERROS_P3);
			}
			if (relatorioAlterado.getAvisosPrioridade3() > 0) {
				tabsAlterado.add(new PainelRelatorioPorPrioridade(relatorioAlterado.getListaAvisosP3(), "AvisoP3", frameSilvinha.painelRelatorio), TradPainelRelatorio.AVISOS_P3);
			}
		}
		tabsAlterado.repaint();
		tabsAlterado.revalidate();
		tabsAlterado.setVisible(true);
		// scrollPane = new JScrollPane(new
		// PainelCodigoFonte(relatorio.getConteudo()));
		// scrollPane.getVerticalScrollBar().setUnitIncrement(70);
		// tabs.add(scrollPane, TokenLang.LBL_CODIGO_FONTE);

	}

	/**
	 * Qual linha deve ser mostrada
	 * 
	 * @param nroLinha
	 */
	public static void mostrarLinhaTabEdic(int nroLinha) {
		boxCode.selectLine(nroLinha);
	}

	public static void mostrarLinhaTabOrig(int nroLinha) {
		boxCodeOriginal.selectLine(nroLinha);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == "Salvar") {
			salvaAlteracoes.salvar();
			System.out.println("relatorio salvar");
		} else
		if (command.equals("Reverter")) {
			
			TxtBuffer.setContent(boxCodeOriginal.getText());
			new CtrlRevert().avalia(boxCodeOriginal.getText(), true);
			frameSilvinha.showPainelRelatorio(relatorio);

			// boxCode.setText(boxCodeOriginal.getText());

			// loadRelatorioAlterado();

			setVisible(true);
		} else if (command.equals("VOLTAR")) {
			// pResumo.mostrarResumo();
			
			frameSilvinha.showPainelResumo(PainelResumo.resumo, false);
		} else if (command.equals("Atualizar_ORIG")) {
			ProcessarErro pE = new ProcessarErro();
			relatorio = pE.reavalia(relatorio);
			frameSilvinha.showPainelRelatorio(relatorio);
		} else if (command.equals("VOLTAR_ORIG")) {
			frameSilvinha.showPainelResumo(PainelResumo.resumo, false);
			// pResumo.mostrarResumo();
		} else if (command.equals("CORRIGIR")) {
			// frameSilvinha.showPainelFerramentaLabel();
			// pResumo.mostrarCorrecao(relatorio);
		} else if (command.equals("VerPágina")) {
			// pResumo.parentFrame.showBrowsePanel(relatorio.getUrl());
		} else if (command == "AumentaFonte") {
			boxCode.aumentaFontSize();
			boxCodeOriginal.aumentaFontSize();
		} else if (command == "DiminuiFonte") {
			boxCode.diminuiFontSize();
			boxCodeOriginal.diminuiFontSize();
		} else if (command == "Contraste") {
			boxCode.autoContraste();
			boxCodeOriginal.autoContraste();
			//reavalia(textAreaSourceCode.getText());
		}else if (command == "Cancelar") {
			
		}
	}

	/**
	 * Abre a ajuda da regra passada como parâmetro
	 * 
	 * @param regraCod
	 */
	public void openHelp(String regraCod) {
		/*
		 * Carrega a configuração no HD
		 */
		ConfiguracaoDaAvaliacao configDaAvaliacao = new ConfiguracaoDaAvaliacao();
		configDaAvaliacao.loadConf();
		String path = "ajuda/" + TokenLang.LANG + "/" + configDaAvaliacao.getStrRegraWcagEmag() + "/" + regraCod + ".html";
		try {
			if (EstadoSilvinha.painel_Atual == EstadoSilvinha.PAINEL_RELATORIO_ORIGINAL) {
				ajuda.setDocument(new File(path));
			} else if (EstadoSilvinha.painel_Atual == EstadoSilvinha.PAINEL_RELATORIO_EDICAO) {
				ajudaAlterado.setDocument(new File(path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna o diretório corrente
	 * 
	 * @return
	 */
	private String getHelpPath() {
		String path = System.getProperty("user.dir");
		path = path.replace(" ", "%20");
		path = path.replace("\\", "/");
		return path;
	}

	public void altTextFocusLost() {
		System.out.println("altTextFocusLost");
		reload();
	}

	public void reload() {
		String codigo = boxCode.getTextPane().getText();
		int caret = boxCode.getTextPane().getCaretPosition();
		TxtBuffer.setHashCode(EstadoSilvinha.hashCodeAtual);
		TxtBuffer.setContent(codigo);
		new CtrlRevert().avalia(codigo);
		boxCode.coloreSource();

		frameSilvinha.showPainelRelatorio(relatorio);
		boxCode.getTextPane().setCaretPosition(caret);
		// loadRelatorioAlterado();
	}
	/**
	 * Recria o menu editar do Frame Principal
	 * 
	 * @param menu
	 * @return
	 */
	private JMenu criaMenuEditar(JMenu menu) {
		// System.out.println("PanelPreenchedorFormulario.criaMenuEditar()");
		menu.removeAll();
		menu.setMnemonic('E');
		menu.setMnemonic(KeyEvent.VK_E);

		//menu.add(new JMenuItemTeclaAtalho(boxCode));

		//menu.add(new JSeparator());

		JMenuItem btnContraste = new JMenuItem(XHTML_Panel.ALTERAR_CONTRASTE);
		btnContraste.addActionListener(this);
		btnContraste.setActionCommand("Contraste");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		// btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD,
		// ActionEvent.CTRL_MASK));
		btnContraste.setToolTipText(XHTML_Panel.DICA_CONTRASTE);
		btnContraste.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_CONTRASTE);
		menu.add(btnContraste);

		JMenuItem btnAumenta = new JMenuItem(XHTML_Panel.AUMENTA_FONTE);
		btnAumenta.addActionListener(this);
		btnAumenta.setActionCommand("AumentaFonte");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));
		btnAumenta.setToolTipText(XHTML_Panel.DICA_AUMENTA_FONTE);
		btnAumenta.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_AUMENTA_FONTE);
		menu.add(btnAumenta);

		JMenuItem btnDiminui = new JMenuItem(XHTML_Panel.DIMINUI_FONTE);
		btnDiminui.addActionListener(this);
		btnDiminui.setActionCommand("DiminuiFonte");
		// btnDiminui.setMnemonic('F');
		// btnDiminui.setMnemonic(KeyEvent.VK_F);
		btnDiminui.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));
		btnDiminui.setToolTipText(XHTML_Panel.DICA_DIMINUI_FONTE);
		btnDiminui.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_DIMINUI_FONTE);
		menu.add(btnDiminui);

	//	menu.add(new JSeparator());

		JMenuItem btnProcurar = new JMenuItem(XHTML_Panel.PROCURAR);
		btnProcurar.addActionListener(this);
		btnProcurar.setActionCommand("Procurar");
		btnProcurar.setMnemonic('P');
		btnProcurar.setMnemonic(KeyEvent.VK_P);
		btnProcurar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		btnProcurar.setToolTipText(XHTML_Panel.DICA_PROCURAR);
		btnProcurar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_PROCURAR);
		//menu.add(btnProcurar);

		JMenuItem btnSelecionarTudo = new JMenuItem(XHTML_Panel.SELECIONAR_TUDO);
		btnSelecionarTudo.addActionListener(this);
		btnSelecionarTudo.setActionCommand("SelecionarTudo");
		btnSelecionarTudo.setMnemonic('T');
		btnSelecionarTudo.setMnemonic(KeyEvent.VK_T);
		btnSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		btnSelecionarTudo.setToolTipText(XHTML_Panel.DICA_SELECIONAR_TUDO);
		btnSelecionarTudo.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SELECIONAR_TUDO);
		//menu.add(btnSelecionarTudo);

		JMenuItem btnDesfazer = new JMenuItem(XHTML_Panel.DESFAZER);
		btnDesfazer.addActionListener(this);
		btnDesfazer.setActionCommand("Desfazer");
		btnDesfazer.setMnemonic('z');
		btnDesfazer.setMnemonic(KeyEvent.VK_Z);
		btnDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		btnDesfazer.setToolTipText(XHTML_Panel.DICA_DESFAZER);
		btnDesfazer.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_DESFAZER);

		//menu.add(btnDesfazer);
		return menu;
	}
	
	/**
	 * Cria o menu
	 * 
	 * @return o menu
	 */
	
	private JMenuBar criaMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBackground(frameSilvinha.corDefault);

		JMenu menuArquivo = new JMenu(XHTML_Panel.ARQUIVO);
		menuArquivo.setMnemonic('A');
		menuArquivo.setMnemonic(KeyEvent.VK_A);

		JMenu avaliadores = new JMenu();
		MenuSilvinha menuSilvinha = new MenuSilvinha(frameSilvinha, null);
		menuSilvinha.criaMenuAvaliadores(avaliadores);
		// menuArquivo.add(avaliadores);
		// menuArquivo.add(new JSeparator());

		JMenuItem btnAbrir = new JMenuItem(XHTML_Panel.BTN_ABRIR);
		btnAbrir.addActionListener(this);
		btnAbrir.setActionCommand("Abrir");
		btnAbrir.setMnemonic('A');
		btnAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		btnAbrir.setMnemonic(KeyEvent.VK_A);
		btnAbrir.setToolTipText(XHTML_Panel.DICA_ABRIR);
		btnAbrir.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_ABRIR);
		menuArquivo.add(btnAbrir);

		JMenuItem btnAbrirUrl = new JMenuItem(XHTML_Panel.BTN_ABRIR_URL);
		btnAbrirUrl.addActionListener(this);
		btnAbrirUrl.setActionCommand("AbrirURL");
		btnAbrirUrl.setMnemonic('U');
		btnAbrirUrl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		btnAbrirUrl.setMnemonic(KeyEvent.VK_U);
		btnAbrirUrl.setToolTipText(XHTML_Panel.DICA_ABRIR);
		btnAbrirUrl.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_ABRIR);
		menuArquivo.add(btnAbrirUrl);

	    
		miBtnSalvar.addActionListener(this);
		miBtnSalvar.setActionCommand("Salvar");
		miBtnSalvar.setMnemonic('S');
		miBtnSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		miBtnSalvar.setMnemonic(KeyEvent.VK_S);
		miBtnSalvar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.BTN_SALVAR);
		miBtnSalvar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SALVAR);
		menuArquivo.add(miBtnSalvar);

		JMenuItem btnSalvarAs = new JMenuItem(XHTML_Panel.BTN_SALVAR_COMO);
		btnSalvarAs.addActionListener(this);
		btnSalvarAs.setActionCommand("SaveAs");
		btnSalvarAs.setMnemonic('c');
		btnSalvarAs.setMnemonic(KeyEvent.VK_C);
		// btnSalvarAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
		// ActionEvent.CTRL_MASK));
		btnSalvarAs.setToolTipText(XHTML_Panel.DICA_SALVAR_COMO);
		btnSalvarAs.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SALVAR_COMO);
		menuArquivo.add(btnSalvarAs);

		JMenuItem btnFechar = new JMenuItem(XHTML_Panel.SAIR);
		btnFechar.addActionListener(this);
		btnFechar.setActionCommand("Sair");
		btnFechar.setMnemonic('a');
		btnFechar.setMnemonic(KeyEvent.VK_A);
		btnFechar.setToolTipText(XHTML_Panel.DICA_SAIR);
		btnFechar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SAIR);
		menuArquivo.add(btnFechar);

		menuBar.add(menuArquivo);

		JMenu menuEditar = new JMenu(XHTML_Panel.EDITAR);
		menuBar.add(criaMenuEditar(menuEditar));

		menuBar.add(avaliadores);
		JMenu menuSimuladores = new JMenu();
		menuSilvinha.criaMenuSimuladores(menuSimuladores);
		menuBar.add(menuSimuladores);

		JMenu mnFerramenta = new JMenu();
		menuSilvinha.criaMenuFerramentas(mnFerramenta);
		menuBar.add(mnFerramenta);

		JMenu menuAjuda = new JMenu(XHTML_Panel.AJUDA);
		menuSilvinha.criaMenuAjuda(menuAjuda);
		menuBar.add(menuAjuda);

		return menuBar;
	}

	public boolean showBarraUrl() {
		return false;
	}
}
