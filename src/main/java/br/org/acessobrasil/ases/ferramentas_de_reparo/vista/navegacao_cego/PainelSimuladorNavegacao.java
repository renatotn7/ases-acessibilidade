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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.navegacao_cego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.commons.httpclient.HttpException;

import br.org.acessobrasil.ases.ferramentas_de_reparo.controle.ControleNavCego;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.listeners.SairListener;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Imagens;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Scripts;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.Silvinha;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.TradSimuladorNavegacao;
import br.org.acessobrasil.silvinha2.mli.XHTML_Panel;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
/**
 * Simula a navegação do leitor de tela
 * @author Fabio Issamu Oshiro, Haroldo Veiga e Renato Tomaz Nati
 *
 */
public class PainelSimuladorNavegacao extends SuperPainelCentral implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea conteudoDoAlt;

	String textoSearched = "";

	protected G_TextAreaSourceCode textAreaSourceCode;

	protected ControleNavCego controle = new ControleNavCego();

	FrameSilvinha parentFrame;

	// private ConteudoCorrecaoLabel scrollPaneCorrecaoLabel;
	private JMenuBar menuBar;

	JMenuItem btnSalvar;

	private JButton abrir;

	private JButton cancelar;

	Color corAchadoAntigo = new Color(0, 0, 0);

	Color corSelecionadoAntigo = new Color(0, 0, 0);

	// private JPanel panelLegenda;

	private JPanel pnRegra;

	private JLabel lbRegras1;

	private JLabel lbRegras2;

	private JPanel pnSetaDescricao;

	private JScrollPane spTextoDescricao;

	private JPanel pnListaErros;

	private JScrollPane scrollPanetabLinCod;

	private TabelaDescricao tabelaDescricao;

	private JPanel pnBotoes;

	private JButton buscar;

	/**
	 * ùltima posição do texto encontrado
	 */
	int posSearched = 0;

	public PainelSimuladorNavegacao(FrameSilvinha parentFrame, String codHtml) {
		this.parentFrame = parentFrame;
		initComponentsEscalavel();
		controle.avalia(codHtml);
		textAreaSourceCode.getTextPane().setText(controle.getTextoParaPainel());
		for (ControleNavCego.Resultado o : controle.getArrResultado()) {
			tabelaDescricao.addLinha(o.getIni_pagina(), o.getConteudo());
		}
	}

	public PainelSimuladorNavegacao(FrameSilvinha silvinha) {
		this.parentFrame = silvinha;
		initComponentsEscalavel();
	}

	private Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}

	private void AbrirActionPerformed(ActionEvent e) {
		G_File caminhoRecente = new G_File("config/html_recente.txt");
		String a[] = { ".html", ".htm" };
		G_File temp = new G_File(caminhoRecente.read(), a);
		if (temp.getFile() != null) {
			caminhoRecente.write(temp.getFile().getAbsolutePath());
			
			// frameSilvinha.setTitle(arquivo.getFile().getName() + " - " +
			// XHTMLPanel.TITULO);

			parentFrame.showPainelSimuladorNavegacaoPArq(temp.read());
			parentFrame.setTitle(temp.getFile().getName() + " - " + TradSimuladorNavegacao.TITULO_SIMULADOR_CEGO);
			// parentFrame.showPainelSimuladorNavegacaoPArq(temp.read());
		}
	}

	private void CancelarActionPerformed(ActionEvent e) {

		parentFrame.showLastActivePanel();
	}

	private void BuscarActionPerformed(ActionEvent e) {
		int pos = 0;
		Color corNovoAchado = new Color(255, 100, 0);
		String textSearch = conteudoDoAlt.getText();
		if (textSearch.equals(textoSearched)) {
			pos = textAreaSourceCode.getTextPane().getText().indexOf(textSearch, posSearched + 1);
		} else {
			pos = textAreaSourceCode.getTextPane().getText().indexOf(textSearch);
		}
		if (pos == -1) {
			SemMaisBusca semMaisBusca = new SemMaisBusca(this);
			semMaisBusca.start();
			return;
		}
		// textAreaSourceCode.getTextPane().setRequestFocusEnabled(true);
		textAreaSourceCode.setColorRange(posSearched, posSearched + textoSearched.length(), corAchadoAntigo);
		// scrollPaneDescricao.getTextPane().setText(scrollPaneDescricao.getTextPane().getText());
		// textAreaSourceCode.getTextPane().setCaretPosition(pos);

		textAreaSourceCode.getTextPane().select(pos, pos + textSearch.length());
		textAreaSourceCode.requestFocus();
		// corAchadoAntigo =
		// textAreaSourceCode.getTextPane().getSelectedTextColor();
		textAreaSourceCode.setColorRange(pos, pos + textSearch.length(), corNovoAchado);
		// corAchadoAntigo=
		posSearched = pos;
		textoSearched = textSearch;
	}

	/**
	 * Cria os componentes
	 * 
	 */
	private void initComponentsEscalavel() {
		TradSimuladorNavegacao.carregaTexto(TokenLang.LANG);
		JPanel regraFonteBtn = new JPanel();
		regraFonteBtn.setLayout(new BorderLayout());

		textAreaSourceCode = new G_TextAreaSourceCode();
		textAreaSourceCode.getTextPane().setEditable(false);

		// scrollPaneDescricao.getTextPane().setContentType("text/html;");
		conteudoDoAlt = new JTextArea();
		parentFrame.setTitle(TradSimuladorNavegacao.TITULO_SIMULADOR_CEGO);
		parentFrame.setJMenuBar(this.criaMenuBar());
		tabelaDescricao = new TabelaDescricao(this);
		tabelaDescricao.addMouseListener(this);
		conteudoDoAlt = new JTextArea();
		abrir = new JButton();
		cancelar = new JButton();
		btnSalvar = new JMenuItem(GERAL.BTN_SALVAR);

		pnRegra = new JPanel();
		lbRegras1 = new JLabel();
		lbRegras2 = new JLabel();
		pnSetaDescricao = new JPanel();
		spTextoDescricao = new JScrollPane();

		pnListaErros = new JPanel();
		scrollPanetabLinCod = new JScrollPane();

		pnBotoes = new JPanel();

		buscar = new JButton();

		this.setBackground(CoresDefault.getCorPaineis());
		Container contentPane = this;// ??
		contentPane.setLayout(new GridLayout(2, 1));

		// ======== pnRegra ========
		{
			pnRegra.setBorder(criaBorda(""));
			pnRegra.setLayout(new GridLayout(2, 1));
			pnRegra.add(lbRegras1);
			lbRegras1.setText(TradSimuladorNavegacao.SELECIONE_ITEM_TABELA);
			lbRegras2.setText(TradSimuladorNavegacao.DE_UMA_PAGINA);
			lbRegras1.setHorizontalAlignment(SwingConstants.CENTER);

			lbRegras2.setHorizontalAlignment(SwingConstants.CENTER);
			pnRegra.add(lbRegras1);
			pnRegra.add(lbRegras2);
			pnRegra.setPreferredSize(new Dimension(700, 60));
		}

		// G_URLIcon.setIcon(lbTemp,
		// "http://pitecos.blogs.sapo.pt/arquivo/pai%20natal%20o5.%20jpg.jpg");

		// ======== pnDescricao ========

		abrir.setText(Ferramenta_Scripts.BTN_ABRIR);
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbrirActionPerformed(e);
			}
		});

		abrir.setToolTipText(Ferramenta_Scripts.DICA_ABRIR);
		abrir.getAccessibleContext().setAccessibleDescription(Ferramenta_Scripts.DICA_ABRIR_HTML);
		abrir.getAccessibleContext().setAccessibleName(Ferramenta_Scripts.DICA_ABRIR_HTML);
		abrir.setBounds(10, 0, 150, 25);

		cancelar.setText(Ferramenta_Imagens.TELA_ANTERIOR);
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelarActionPerformed(e);
			}
		});

		cancelar.setToolTipText(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.TELA_ANTERIOR);
		cancelar.setBounds(165, 0, 150, 25);

		// ======== pnParticRotulo ========

		pnSetaDescricao.setBorder(criaBorda(TradSimuladorNavegacao.DIGITE_TEXTO_BUSCADO));
		GridBagConstraints cons = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;
		cons.weightx = 0.80;

		pnSetaDescricao.setLayout(layout);
		cons.anchor = GridBagConstraints.SOUTHEAST;
		cons.insets = new Insets(0, 0, 0, 10);
		// ======== spParticRotulo ========
		{
			spTextoDescricao.setViewportView(conteudoDoAlt);
		}

		pnSetaDescricao.add(spTextoDescricao, cons);
		cons.weightx = 0.20;
		pnSetaDescricao.setPreferredSize(new Dimension(400, 60));

		// ======== pnListaErros ========
		{

			pnListaErros.setBorder(criaBorda(TradSimuladorNavegacao.LINKS_INTERNOS));
			pnListaErros.setLayout(new BorderLayout());
			// ======== scrollPanetabLinCod ========
			{
				scrollPanetabLinCod.setViewportView(tabelaDescricao);
			}
			pnListaErros.add(scrollPanetabLinCod, BorderLayout.CENTER);
		}
		// ======== pnBotoes ========
		{

			// pnBotoes.setBorder(criaBorda(""));

			pnBotoes.setLayout(null);
			// ---- adicionar ----

			// ---- aplicarRotulo ----

			buscar.setText(TradSimuladorNavegacao.BUSCAR_PROXIMA);
			buscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					BuscarActionPerformed(e);
				}
			});

			buscar.setToolTipText(TradSimuladorNavegacao.BUSCAR_TEXTO);
			buscar.getAccessibleContext().setAccessibleDescription(TradSimuladorNavegacao.BUSCAR_TEXTO);
			buscar.getAccessibleContext().setAccessibleName(TradSimuladorNavegacao.BUSCAR_TEXTO);
			buscar.setBounds(10, 5, 150, 25);
			pnBotoes.add(buscar);
		}

		/*
		 * Colocar os controles
		 */
		pnRegra.setBackground(CoresDefault.getCorPaineis());
		regraFonteBtn.add(pnRegra, BorderLayout.NORTH);
		textAreaSourceCode.setBorder(criaBorda(""));
		textAreaSourceCode.setBackground(CoresDefault.getCorPaineis());

		// JScrollPane ajudaScrollPane = new
		// JScrollPane(ajuda,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// regraFonteBtn.add(scrollPaneCorrecaoLabel, BorderLayout.CENTER);
		regraFonteBtn.add(textAreaSourceCode, BorderLayout.CENTER);
		pnBotoes.setPreferredSize(new Dimension(600, 35));
		pnBotoes.setBackground(CoresDefault.getCorPaineis());
		// regraFonteBtn.add(pnBotoes, BorderLayout.SOUTH);
		regraFonteBtn.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(regraFonteBtn);

		JPanel textoErrosBtn = new JPanel();
		textoErrosBtn.setLayout(new BorderLayout());
		pnSetaDescricao.setBackground(CoresDefault.getCorPaineis());
		pnSetaDescricao.add(pnBotoes, cons);
		textoErrosBtn.add(pnSetaDescricao, BorderLayout.NORTH);

		textoErrosBtn.add(pnListaErros, BorderLayout.CENTER);
		JPanel pnSalvarCancelar = new JPanel();
		pnSalvarCancelar.setLayout(null);
		pnSalvarCancelar.setPreferredSize(new Dimension(600, 35));

		pnSalvarCancelar.add(abrir);
		pnSalvarCancelar.add(cancelar);
		pnSalvarCancelar.setBackground(CoresDefault.getCorPaineis());
		textoErrosBtn.add(pnSalvarCancelar, BorderLayout.SOUTH);
		pnListaErros.setBackground(CoresDefault.getCorPaineis());

		contentPane.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(textoErrosBtn);

		this.setVisible(true);

	}

	private JMenuBar criaMenuBar() {
		menuBar = new JMenuBar();

		menuBar.setBackground(parentFrame.corDefault);

		JMenu menuArquivo = new JMenu(GERAL.ARQUIVO);
		menuArquivo.setMnemonic('A');
		menuArquivo.setMnemonic(KeyEvent.VK_A);
		menuArquivo.setBackground(parentFrame.corDefault);

		JMenu avaliadores = new JMenu();
		MenuSilvinha menuSilvinha = new MenuSilvinha(parentFrame, null);
		menuSilvinha.criaMenuAvaliadores(avaliadores);
		// menuArquivo.add(avaliadores);
		// menuArquivo.add(new JSeparator());

		JMenuItem btnAbrir = new JMenuItem(GERAL.BTN_ABRIR);
		btnAbrir.addActionListener(this);
		btnAbrir.setActionCommand("Abrir");
		btnAbrir.setMnemonic('A');
		btnAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		btnAbrir.setMnemonic(KeyEvent.VK_A);
		btnAbrir.setToolTipText(TradSimuladorNavegacao.DICA_ABRE_E_AVALIA);
		btnAbrir.getAccessibleContext().setAccessibleDescription(TradSimuladorNavegacao.DICA_ABRE_E_AVALIA);
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

		btnSalvar = new JMenuItem(GERAL.BTN_SALVAR);
		btnSalvar.addActionListener(this);
		btnSalvar.setActionCommand("Salvar");
		btnSalvar.setMnemonic('S');
		btnSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		btnSalvar.setMnemonic(KeyEvent.VK_S);
		btnSalvar.getAccessibleContext().setAccessibleDescription(TradSimuladorNavegacao.DICA_GRAVA_E_REAVALIA);
		// menuArquivo.add(btnSalvar);

		JMenuItem btnSalvarAs = new JMenuItem(GERAL.BTN_SALVAR_COMO);
		btnSalvarAs.addActionListener(this);
		btnSalvarAs.setActionCommand("SaveAs");
		btnSalvarAs.setMnemonic('c');
		btnSalvarAs.setMnemonic(KeyEvent.VK_C);
		// btnSalvarAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
		// ActionEvent.CTRL_MASK));
		btnSalvarAs.setToolTipText(GERAL.DICA_SALVAR_COMO);
		btnSalvarAs.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SALVAR_COMO);
		// menuArquivo.add(btnSalvarAs);

		menuArquivo.add(new JSeparator());

		JMenuItem btnFechar = new JMenuItem(GERAL.SAIR);
		//btnFechar.addActionListener(this);
		btnFechar.setActionCommand("Sair");
		//btnFechar.setMnemonic(KeyEvent.VK_X);
		btnFechar.setToolTipText(GERAL.DICA_SAIR);
		//btnFechar.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SAIR);
		menuArquivo.add(btnFechar);
		btnFechar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
		btnFechar.setMnemonic('X');
		btnFechar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		menuBar.add(menuArquivo);

		menuBar.add(this.criaMenuEditar());

		menuBar.add(avaliadores);

		JMenu menuSimuladores = new JMenu();
		menuSilvinha.criaMenuSimuladores(menuSimuladores);
		menuBar.add(menuSimuladores);

		JMenu mnFerramenta = new JMenu();
		menuSilvinha.criaMenuFerramentas(mnFerramenta);
		menuBar.add(mnFerramenta);

		JMenu menuAjuda = new JMenu(GERAL.AJUDA);
		menuSilvinha.criaMenuAjuda(menuAjuda);
		menuBar.add(menuAjuda);

		return menuBar;
	}

	/**
	 * Cria o menu editar do Frame Principal
	 * 
	 * @param menu
	 */
	private JMenu criaMenuEditar() {
		JMenu menu = new JMenu(GERAL.EDITAR);
		menu.setBackground(parentFrame.corDefault);
		menu.setMnemonic('E');
		menu.setMnemonic(KeyEvent.VK_E);

		JMenuItem btnContraste = new JMenuItem(GERAL.BTN_ALTERAR_CONTRASTE);
		btnContraste.addActionListener(this);
		btnContraste.setActionCommand("Contraste");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		// btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD,
		// ActionEvent.CTRL_MASK));
		btnContraste.setToolTipText(GERAL.ALTERAR_CONTRASTE);
		btnContraste.getAccessibleContext().setAccessibleDescription(GERAL.ALTERAR_CONTRASTE);
		menu.add(btnContraste);

		JMenuItem btnAumenta = new JMenuItem(GERAL.AUMENTA_FONTE);
		btnAumenta.addActionListener(this);
		btnAumenta.setActionCommand("AumentaFonte");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));
		btnAumenta.setToolTipText(GERAL.DICA_AUMENTA_FONTE);
		btnAumenta.getAccessibleContext().setAccessibleDescription(GERAL.DICA_AUMENTA_FONTE);
		menu.add(btnAumenta);

		JMenuItem btnDiminui = new JMenuItem(GERAL.DIMINUI_FONTE);
		btnDiminui.addActionListener(this);
		btnDiminui.setActionCommand("DiminuiFonte");
		// btnDiminui.setMnemonic('F');
		// btnDiminui.setMnemonic(KeyEvent.VK_F);
		btnDiminui.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));
		btnDiminui.setToolTipText(GERAL.DICA_DIMINUI_FONTE);
		btnDiminui.getAccessibleContext().setAccessibleDescription(GERAL.DICA_DIMINUI_FONTE);
		menu.add(btnDiminui);

		JMenuItem btnSelecionarTudo = new JMenuItem(GERAL.SELECIONAR_TUDO);
		btnSelecionarTudo.addActionListener(this);
		btnSelecionarTudo.setActionCommand("SelecionarTudo");
		btnSelecionarTudo.setMnemonic('T');
		btnSelecionarTudo.setMnemonic(KeyEvent.VK_T);
		btnSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		btnSelecionarTudo.setToolTipText(GERAL.DICA_SELECIONAR_TUDO);
		btnSelecionarTudo.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SELECIONAR_TUDO);
		menu.add(btnSelecionarTudo);
		menu.add(new JSeparator());
		/*
		 * JMenuItem btnProcurar = new JMenuItem(GERAL.PROCURAR);
		 * btnProcurar.addActionListener(this);
		 * btnProcurar.setActionCommand("Procurar");
		 * btnProcurar.setMnemonic('P'); btnProcurar.setMnemonic(KeyEvent.VK_P);
		 * btnProcurar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,
		 * ActionEvent.CTRL_MASK));
		 * btnProcurar.setToolTipText(GERAL.DICA_PROCURAR);
		 * btnProcurar.getAccessibleContext().setAccessibleDescription(GERAL.DICA_PROCURAR);
		 * menu.add(btnProcurar);
		 * 
		 * JMenuItem btnSelecionarTudo = new JMenuItem(GERAL.SELECIONAR_TUDO);
		 * btnSelecionarTudo.addActionListener(this);
		 * btnSelecionarTudo.setActionCommand("SelecionarTudo");
		 * btnSelecionarTudo.setMnemonic('T');
		 * btnSelecionarTudo.setMnemonic(KeyEvent.VK_T);
		 * btnSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T,
		 * ActionEvent.CTRL_MASK));
		 * btnSelecionarTudo.setToolTipText(GERAL.DICA_SELECIONAR_TUDO);
		 * btnSelecionarTudo.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SELECIONAR_TUDO);
		 * menu.add(btnSelecionarTudo);
		 * 
		 * JMenuItem btnDesfazer = new JMenuItem(GERAL.DESFAZER);
		 * btnDesfazer.addActionListener(this);
		 * btnDesfazer.setActionCommand("Desfazer");
		 * btnDesfazer.setMnemonic('z'); btnDesfazer.setMnemonic(KeyEvent.VK_Z);
		 * btnDesfazer.getAccessibleContext().setAccessibleDescription(GERAL.DICA_DESFAZER);
		 * btnDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
		 * ActionEvent.CTRL_MASK)); menu.add(btnDesfazer);
		 * menu.setEnabled(true);
		 */
		return menu;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Abrir")) {
			AbrirActionPerformed(null);
		} else if (cmd == "AbrirURL") {
			openUrl();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd.equals("SelecionarTudo")) {
			textAreaSourceCode.getTextPane().selectAll();
			textAreaSourceCode.getTextPane().requestFocus();
		} else if (cmd == "AumentaFonte") {
			textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			textAreaSourceCode.autoContraste();
		}
	}

	private void openUrl() {
		String url;
		url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://www.acessobrasil.org.br");
		avaliaUrl(url);
	}

	private class SemMaisBusca extends Thread {
		PainelSimuladorNavegacao parent;

		public SemMaisBusca(PainelSimuladorNavegacao parent) {
			this.parent = parent;
		}

		public void run() {

			JOptionPane.showMessageDialog(parent, TradSimuladorNavegacao.NAO_HA_OCORRENCIAS);

		}
	}

	int linhaAnteriorTabela = -1;

	int selectAntigo = 0;

	int fimLinhaAntiga = 0;

	public void mouseClicked(MouseEvent e) {
		try {
			if (e.getClickCount() == 2) {
				TabelaDescricao tcl = ((TabelaDescricao) e.getComponent());
				int caretPosition = 0;
				int linhaSelecionada = tcl.getSelectedRow();
				int colSelecionada = tcl.getSelectedColumn();
				// System.out.println("resultados="+controle.getArrResultado().size());
				this.textAreaSourceCode.getTextPane().setCaretPosition(0);
				if (colSelecionada == 0) {
					caretPosition = controle.getArrResultado().get(linhaSelecionada).getOrigem().getIndexOf();
				} else {
					caretPosition = controle.getArrResultado().get(linhaSelecionada).getDestino().getIndexOf();
				}
				// System.out.println("caretPosition="+caretPosition);
				String txt = this.textAreaSourceCode.getTextPane().getText().substring(caretPosition);
				if (txt.length() > 5) {
					txt = txt.substring(0, 5);
				}
				// System.out.println("\ttxt="+txt);
				this.textAreaSourceCode.getTextPane().setCaretPosition(caretPosition);
				this.textAreaSourceCode.getTextPane().select(caretPosition, txt.length() + caretPosition);
				this.textAreaSourceCode.getTextPane().requestFocus();
			}
		} catch (Exception e1) {

		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void avaliaUrl(String url) {
		if (url.startsWith("http")) {
			PegarPaginaWEB ppw = new PegarPaginaWEB();
			if (url != null) {
				try {
					String codHtml = ppw.getContent(url);
					System.out.println(codHtml);
					parentFrame.showPainelSimuladorNavegacaoPArq(codHtml);
					parentFrame.setTitle(url + " - " + TradSimuladorNavegacao.TITULO_SIMULADOR_CEGO);
				} catch (HttpException e1) {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage(), e1.getMessage(), JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}
			}
		} else {
			G_File temp = new G_File(url);
			if(temp.exists()){
				parentFrame.showPainelSimuladorNavegacaoPArq(temp.read());
				parentFrame.setTitle(temp.getFile().getName() + " - " + TradSimuladorNavegacao.TITULO_SIMULADOR_CEGO);
			}else{
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@Override
	public boolean showBarraUrl() {
		return true;
	}
}
