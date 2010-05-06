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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.analise_geral;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ColorModel;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

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
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;

import org.apache.commons.httpclient.HttpException;
import org.xhtmlrenderer.simple.XHTMLPanel;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.PanelDescricaoImagens;
import br.org.acessobrasil.ases.persistencia.ConexaoBanco;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha.vista.panels.relatorio.PainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Imagens;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.projetodosite.ProjetoDoSite;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;

/**
 * UI para corrigir os erros de imagem
 * @author Renato Nati
 */
public class PanelAnaliseGeral extends SuperPainelCentral implements ActionListener {
	/**
	 * Serial default
	 */
	private static final long serialVersionUID = 4808373827580204059L;

	public ArrayList<FerramentaAnaliseGeralModel> posicaoeTag;

	public String nomeArquivo;

	public String codigo;

	private String codigoAnterior;

	private String hashCodAnterior;

	private String enderecoAnterior = "";

	SalvaAlteracoes salvaAlteracoes;

	public int inicial = 0;

	public static final int ARQUIVO = 0;

	public static final int CONTEUDO = 1;

	private int posTagRepInit;

	JMenuItem btnSalvar;

	private int posTagRepEnd;

	private String textoSelecionado;

	PanelDescricaoImagens pdi;

	/**
	 * Mostra a imagem sem descrição
	 */
	XHTMLPanel imagemSemDesc;

	// JEditorPane imagemSemDesc;

	// JLabel imagemSemDesc;

	ArTextPainelCorrecao arTextPainelCorrecao;

	G_TextAreaSourceCode boxCode;

	FrameSilvinha parentFrame;

	private JMenuBar menuBar;

	private JButton salvar;

	private JButton cancelar;

	private JButton salvarMod;

	private JButton salvarPag;

	public AnaliseGeral analiseGeral;

	public JButton aplicarPag;

	public JButton aplicarTod;

	private JPanel pnRegra;

	private JLabel lbRegras1;

	private JLabel lbRegras2;

	private JPanel pnSetaDescricao;

	public JTextArea conteudoDoAlt;

	private JScrollPane spTextoDescricao;

	TArParticipRotulo tArParticipRotulo;

	String strConteudoalt;

	private JPanel pnListaErros;

	private JScrollPane scrollPanetabLinCod;

	TabelaAnaliseGeral tableLinCod;

	private JPanel pnBotoes;

	private JButton adicionar;

	public JButton analiseSistematica;

	JButton aplicar;

	private ArrayList<String> conteudoParticRotulo;

	private String enderecoImagem;

	DefaulTableModelNotEditable dtm;

	String enderecoPagina, hashCodeInicial, tagInicial;

	public String paginaAtual;

	private ArrayList<String> endModfcd = new ArrayList<String>();

	private ArrayList<String> hashCodeModfcd = new ArrayList<String>();

	public static void main(String[] args) {
		Ferramenta_Imagens.carregaTexto(TokenLang.LANG);
	}

	public PanelAnaliseGeral(String tag, String endPag, FrameSilvinha parent, PanelDescricaoImagens pdi) {
		this.enderecoPagina = endPag;
		this.pdi = pdi;
		this.tagInicial = tag;
		this.parentFrame = parent;
		analiseGeral = new AnaliseGeral(tag, endPag);
		posicaoeTag = new ArrayList<FerramentaAnaliseGeralModel>();
		Statement stConsultaTabImg = null;
		try {
			Connection con = (DriverManager.getConnection(ConexaoBanco.bancoEmUso));

			stConsultaTabImg = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			ResultSet rs2 = stConsultaTabImg.executeQuery("SELECT * from imagem "); // "WHERE
			// endTag='"
			// + endImagem(tag, endPagina) + "'");
			boolean passou = false;
			while (rs2.next()) {
				posicaoeTag.add(new FerramentaAnaliseGeralModel(rs2.getInt("linha"), rs2.getInt("coluna"), rs2.getString("endPagina")));
				if (!passou) {
					enderecoImagem = rs2.getString("endTag");
					passou = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		initComponentsEscalavel(posicaoeTag);
		tableLinCod.mostraPrimLinha();
	}

	public void incValueProgress() {
		analiseGeral.incrementoExtra += 5;
		for (int i = 1;; i++) {
			if (analiseGeral.incrementoExtra < 75 * (10 ^ i)) {
				if (i == 1) {

				}
				PainelStatusBar.setValueProgress(25 + (analiseGeral.incrementoExtra / (10 ^ (i - 1))));
				break;
			}
		}

	}

	/**
	 * Inicia os componentes
	 * 
	 * @param erros
	 */

	private void initComponentsEscalavel(ArrayList<FerramentaAnaliseGeralModel> erros) {
		incValueProgress();
		hashCodeInicial = null;
		PainelStatusBar.hideProgTarReq();
		Ferramenta_Imagens.carregaTexto(TokenLang.LANG);
		JPanel regraFonteBtn = new JPanel();
		regraFonteBtn.setLayout(new BorderLayout());
		PainelStatusBar.setValueProgress(3);
		boxCode = new G_TextAreaSourceCode();
		boxCode.setTipoHTML();
		incValueProgress();
		parentFrame.setJMenuBar(this.criaMenuBar());
		PainelStatusBar.setValueProgress(6);
		// parentFrame.setTitle("Associador de rótulos");
		tableLinCod = new TabelaAnaliseGeral(this, erros);
		arTextPainelCorrecao = new ArTextPainelCorrecao(this);
		incValueProgress();
		// scrollPaneCorrecaoLabel = new ConteudoCorrecaoLabel();
		analiseSistematica = new JButton();
		salvar = new JButton();

		cancelar = new JButton();

		salvarMod = new JButton();

		salvarPag = new JButton();

		aplicarPag = new JButton();

		aplicarTod = new JButton();

		strConteudoalt = new String();
		incValueProgress();
		btnSalvar = new JMenuItem(GERAL.BTN_SALVAR);
		PainelStatusBar.setValueProgress(10);
		pnRegra = new JPanel();
		lbRegras1 = new JLabel();
		lbRegras2 = new JLabel();
		pnSetaDescricao = new JPanel();
		spTextoDescricao = new JScrollPane();
		tArParticipRotulo = new TArParticipRotulo(this);
		conteudoDoAlt = new JTextArea();
		pnListaErros = new JPanel();
		scrollPanetabLinCod = new JScrollPane();
		incValueProgress();
		/**
		 * Mostra pro usuário a imagem que está sem descrição
		 */
		imagemSemDesc = new XHTMLPanel();

		pnBotoes = new JPanel();
		salvar.setEnabled(false);
		// salvaAlteracoes = new SalvaAlteracoes(boxCode.getTextPane(), salvar,
		// btnSalvar, parentFrame);
		adicionar = new JButton();
		aplicar = new JButton();
		conteudoParticRotulo = new ArrayList<String>();
		analiseSistematica.setEnabled(false);
		boxCode.getTextPane().setText(TxtBuffer.getContent());
		PainelStatusBar.setValueProgress(20);
		String fullUrl = this.enderecoImagem;
		System.out.println("\t\t\t\t\tendereço da imagem: " + fullUrl);
		SetImage setImage = new SetImage(this, fullUrl);
		setImage.start();
		incValueProgress();
		setBackground(CoresDefault.getCorPaineis());
		Container contentPane = this;// ??
		contentPane.setLayout(new GridLayout(2, 1));
		incValueProgress();
		// ======== pnRegra ========
		{
			pnRegra.setBorder(criaBorda(Ferramenta_Imagens.TITULO_REGRA));
			pnRegra.setLayout(new GridLayout(2, 1));
			pnRegra.add(lbRegras1);
			lbRegras1.setText(Ferramenta_Imagens.REGRAP1);
			lbRegras2.setText(Ferramenta_Imagens.REGRAP2);
			lbRegras1.setHorizontalAlignment(SwingConstants.CENTER);
			lbRegras2.setHorizontalAlignment(SwingConstants.CENTER);
			pnRegra.add(lbRegras1);
			pnRegra.add(lbRegras2);
			pnRegra.setPreferredSize(new Dimension(700, 60));
			incValueProgress();
		}
		PainelStatusBar.setValueProgress(30);
		// G_URLIcon.setIcon(lbTemp,
		// "http://pitecos.blogs.sapo.pt/arquivo/pai%20natal%20o5.%20jpg.jpg");
		JScrollPane sp = new JScrollPane();

		sp.setViewportView(imagemSemDesc);
		sp.setPreferredSize(new Dimension(500, 300));

		// ======== pnDescricao ========
		incValueProgress();
		// ---- Salvar ----
		salvarMod.setText(GERAL.SALVAR_MODIFICADAS);
		salvarMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarModificadosActionPerformed(e);
			}
		});
		incValueProgress();
		salvarMod.setToolTipText(GERAL.DICA_SALVAR_MODIFICADOS);
		salvarMod.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SALVAR_MODIFICADOS);
		salvarMod.getAccessibleContext().setAccessibleName(GERAL.DICA_SALVAR_MODIFICADOS);
		salvarMod.setBounds(10, 0, 150, 25);
		PainelStatusBar.setValueProgress(40);
		salvarPag.setText(GERAL.SALVAR_ULTIMA);
		salvarPag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarPaginaActionPerformed(e);
			}
		});
		incValueProgress();
		salvarPag.setToolTipText(GERAL.DICA_SALVAR_ULTIMA_MODIFICADA);
		salvarPag.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SALVAR_ULTIMA_MODIFICADA);
		salvarPag.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_ABRIR_HTML);
		salvarPag.setBounds(165, 0, 150, 25);
		incValueProgress();
		salvarMod.setEnabled(false);
		salvarPag.setEnabled(false);
		ArrayList<JButton> btnsSalvar = new ArrayList<JButton>();
		btnsSalvar.add(salvarMod);
		btnsSalvar.add(salvarPag);
		btnsSalvar.add(salvar);
		salvaAlteracoes = new SalvaAlteracoes(boxCode.getTextPane(), btnsSalvar, btnSalvar, parentFrame);
		aplicarPag.setText(GERAL.APLICAR_PAGINA);
		aplicarPag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarPaginaActionPerformed(e);
			}
		});
		incValueProgress();
		PainelStatusBar.setValueProgress(50);
		aplicarPag.setToolTipText(GERAL.DICA_APLICA_PAGINA);
		aplicarPag.getAccessibleContext().setAccessibleDescription(GERAL.DICA_APLICA_PAGINA);
		aplicarPag.getAccessibleContext().setAccessibleName(GERAL.DICA_APLICA_PAGINA);
		aplicarPag.setBounds(320, 0, 150, 25);
		incValueProgress();
		aplicarTod.setText(GERAL.APLICA_TODOS);
		aplicarTod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						aplicarATodosActionPerformed();
					}
				});
				t.start();
			}
		});
		incValueProgress();
		aplicarTod.setToolTipText(GERAL.DICA_APLICA_ULTIMA_TODOS);
		aplicarTod.getAccessibleContext().setAccessibleDescription(GERAL.DICA_APLICA_ULTIMA_TODOS);
		aplicarTod.getAccessibleContext().setAccessibleName(GERAL.DICA_APLICA_ULTIMA_TODOS);
		aplicarTod.setBounds(475, 0, 150, 25);
		aplicarPag.setEnabled(false);
		aplicarTod.setEnabled(false);
		PainelStatusBar.setValueProgress(60);
		incValueProgress();
		cancelar.setText(GERAL.TELA_ANTERIOR);
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelarActionPerformed(e);
			}
		});

		cancelar.setToolTipText(Ferramenta_Imagens.DICA_BTN_CANCELAR);
		cancelar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_BTN_CANCELAR);
		cancelar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_BTN_CANCELAR);
		cancelar.setBounds(630, 0, 150, 25);
		incValueProgress();
		pnSetaDescricao.setBorder(criaBorda(Ferramenta_Imagens.TITULO_DIGITE_O_ALT));
		GridBagConstraints cons = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;
		cons.weightx = 0.80;
		PainelStatusBar.setValueProgress(70);
		incValueProgress();
		pnSetaDescricao.setLayout(layout);
		cons.anchor = GridBagConstraints.SOUTHEAST;
		cons.insets = new Insets(0, 0, 0, 10);
		// ======== spParticRotulo ========
		conteudoDoAlt.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
				if (conteudoDoAlt.getText().length() == 0 || tableLinCod.getNumLinhas() < 1) {
					System.out.println("conteudo vazio");
					aplicarPag.setEnabled(false);
					aplicarTod.setEnabled(false);

				} else if (tableLinCod.getSelectedRow() != -1) {
					System.out.println("com conteudo");
					aplicarPag.setEnabled(true);
					aplicarTod.setEnabled(true);

				} else {
					aplicarTod.setEnabled(true);
				}
			}
		});

		{
			spTextoDescricao.setViewportView(conteudoDoAlt);
		}
		incValueProgress();
		// lbRegras1.setText(Reparo_Imagens.REGRAP2);
		// lbRegras1.setHorizontalAlignment(SwingConstants.CENTER);

		// pnRegra.add(lbRegras1);

		pnSetaDescricao.add(spTextoDescricao, cons);
		cons.weightx = 0.20;
		pnSetaDescricao.setPreferredSize(new Dimension(400, 60));

		// ======== pnListaErros ========
		{
			PainelStatusBar.setValueProgress(80);
			pnListaErros.setBorder(criaBorda(Ferramenta_Imagens.LISTA_ERROS));
			pnListaErros.setLayout(new BorderLayout());
			// ======== scrollPanetabLinCod ========
			{
				scrollPanetabLinCod.setViewportView(tableLinCod);
			}
			pnListaErros.add(scrollPanetabLinCod, BorderLayout.CENTER);
		}
		// ======== pnBotoes ========
		incValueProgress();
		{

			// pnBotoes.setBorder(criaBorda(""));

			pnBotoes.setLayout(null);
			// ---- adicionar ----
			adicionar.setText(Ferramenta_Imagens.BTN_ADICIONAR);
			adicionar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					adicionarActionPerformed(e);
				}
			});
			PainelStatusBar.setValueProgress(90);
			adicionar.setToolTipText(Ferramenta_Imagens.DICA_ADICIONAR);
			adicionar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_ADICIONAR);
			adicionar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_ADICIONAR);
			adicionar.setBounds(10, 5, 150, 25);
			// pnBotoes.add(adicionar);
			incValueProgress();
			// ---- aplicarRotulo ----
			aplicar.setEnabled(false);
			aplicar.setText(Ferramenta_Imagens.BTN_APLICAR);
			aplicar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					aplicarRotuloActionPerformed(e);
				}
			});
			incValueProgress();
			aplicar.setToolTipText(Ferramenta_Imagens.DICA_APLICAR);
			aplicar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_APLICAR);
			aplicar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_APLICAR);
			aplicar.setBounds(10, 5, 150, 25);
			// pnBotoes.add(aplicar);
		}

		/*
		 * Colocar os controles
		 */
		pnRegra.setBackground(CoresDefault.getCorPaineis());
		regraFonteBtn.add(pnRegra, BorderLayout.NORTH);
		boxCode.setBorder(criaBorda(""));
		boxCode.setBackground(CoresDefault.getCorPaineis());
		incValueProgress();
		JSplitPane splitPane = null;

		Dimension minimumSize = new Dimension(0, 0);
		// JScrollPane ajudaScrollPane = new
		// JScrollPane(ajuda,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		PainelStatusBar.setValueProgress(93);
		sp.setMinimumSize(minimumSize);
		sp.setPreferredSize(new Dimension(150, 90));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, boxCode);
		splitPane.setOneTouchExpandable(true);
		// splitPane.set
		// splitPane.setDividerLocation(0.95);
		int w = parentFrame.getWidth();
		int s = w / 4;
		splitPane.setDividerLocation(s);
		incValueProgress();
		// regraFonteBtn.add(scrollPaneCorrecaoLabel, BorderLayout.CENTER);
		regraFonteBtn.add(splitPane, BorderLayout.CENTER);
		pnBotoes.setPreferredSize(new Dimension(600, 35));
		pnBotoes.setBackground(CoresDefault.getCorPaineis());
		// regraFonteBtn.add(pnBotoes, BorderLayout.SOUTH);
		regraFonteBtn.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(regraFonteBtn);
		PainelStatusBar.setValueProgress(96);
		JPanel textoErrosBtn = new JPanel();
		textoErrosBtn.setLayout(new BorderLayout());
		pnSetaDescricao.setBackground(CoresDefault.getCorPaineis());
		pnSetaDescricao.add(pnBotoes, cons);
		textoErrosBtn.add(pnSetaDescricao, BorderLayout.NORTH);

		textoErrosBtn.add(pnListaErros, BorderLayout.CENTER);
		JPanel pnSalvarCancelar = new JPanel();
		pnSalvarCancelar.setLayout(null);
		pnSalvarCancelar.setPreferredSize(new Dimension(600, 35));
		incValueProgress();
		pnSalvarCancelar.add(salvarMod);
		pnSalvarCancelar.add(salvarPag);
		pnSalvarCancelar.add(aplicarPag);
		pnSalvarCancelar.add(aplicarTod);
		pnSalvarCancelar.add(cancelar);
		pnSalvarCancelar.setBackground(CoresDefault.getCorPaineis());

		incValueProgress();

		textoErrosBtn.add(pnSalvarCancelar, BorderLayout.SOUTH);
		PainelStatusBar.setValueProgress(100);
		pnListaErros.setBackground(CoresDefault.getCorPaineis());
		textoErrosBtn.add(pnListaErros, BorderLayout.CENTER);
		contentPane.setBackground(CoresDefault.getCorPaineis());

		incValueProgress();

		contentPane.add(textoErrosBtn);
		System.out.println("\t\t\t" + TxtBuffer.getContent());
	

		incValueProgress();

		this.setVisible(true);

	}

	/**
	 * Abre um arquivo Local
	 * 
	 */
	private void abrirArquivoLocal() {
		G_File caminhoRecente = new G_File("config/html_recente.txt");
		String a[] = { ".html", ".htm" };
		G_File temp = new G_File(caminhoRecente.read(), a);
		if (temp.getFile() != null) {
			caminhoRecente.write(temp.getFile().getAbsolutePath());
			TxtBuffer.setContentOriginal(temp.read(),"0");
			parentFrame.showPainelFerramentaImgPArq(temp.read(), enderecoPagina);
			String path = temp.getUrlPath();
			EstadoSilvinha.setLinkAtual(path);
		}
	}

	private void CancelarActionPerformed(ActionEvent e) {
		new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + hashCodeInicial);

		parentFrame.showPainelFerramentaImgPArq(TxtBuffer.getContent(), enderecoPagina);
		PainelRelatorio.boxCode.setText(TxtBuffer.getContent());
		// new File(ProjetoDoSite.getNomeDoProjeto()+"/reparo/temp/").delete();
		// salvaAlteracoes.cancelar();
	}

	private void salvarModificadosActionPerformed(ActionEvent e) {

		salvaAlteracoes.salvar(endModfcd, hashCodeModfcd);
		hashCodeModfcd.clear();
		endModfcd.clear();
	}

	private void salvarPaginaActionPerformed(ActionEvent e) {

		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();
		temp2.add(hashCodeModfcd.get(hashCodeModfcd.size() - 1));
		hashCodeModfcd.remove(hashCodeModfcd.size() - 1);
		temp.add(endModfcd.get(endModfcd.size() - 1));
		endModfcd.remove(endModfcd.size() - 1);

		salvaAlteracoes.salvar(temp, temp2);

	}

	private void aplicarATodosActionPerformed() {
		PainelStatusBar.showProgTarReq();
		parentFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		aplicarTod.setEnabled(false);
		int progresso = 0;
		int numLinhas = tableLinCod.getNumLinhas();
		while (tableLinCod.getNumLinhas() > 0) {
			progresso = tableLinCod.getNumLinhas() * 100 / numLinhas;
			PainelStatusBar.setValueProgress(100 - progresso);
			aplicando();
		}
		hashCodeModfcd.add(hashCodAnterior);
		endModfcd.add(enderecoAnterior);
		G_File arq = new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + hashCodAnterior);
		arq.write(codigoAnterior);
		// FerramentaAnaliseGeralCtrl flc =
		new FerramentaAnaliseGeralCtrl(codigoAnterior, hashCodAnterior);
		boxCode.getTextPane().setText("");
		aplicarTod.setEnabled(true);
		parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		salvarPag.setEnabled(false);
		aplicarPag.setEnabled(false);
		aplicarTod.setEnabled(false);
		PainelStatusBar.hideProgTarReq();
	}

	private void aplicando() {
		try {
			strConteudoalt = conteudoDoAlt.getText().replace("\n", " ");
			salvaAlteracoes.setAlterado();
			int corretordePosicoesdoControle = 0;

			conteudoParticRotulo = null;
			conteudoParticRotulo = tArParticipRotulo.getTextoEPos();

			String codHTML = boxCode.getTextPane().getText().replace("\r", "");

			if (codHTML.charAt(getPosTagRepEnd() + corretordePosicoesdoControle - 2) == '/') {
				boxCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 2, getPosTagRepEnd() + corretordePosicoesdoControle);
				arTextPainelCorrecao.setTextoParaSelecionadoSCor(" alt=\"" + strConteudoalt + "\"/>");
			} else {
				boxCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 1, getPosTagRepEnd() + corretordePosicoesdoControle);
				arTextPainelCorrecao.setTextoParaSelecionadoSCor(" alt=\"" + strConteudoalt + "\">");
			}

			codigo = boxCode.getTextPane().getText();

			if (enderecoAnterior != null && codigoAnterior != null && hashCodAnterior != null) {
				if (!enderecoAnterior.equals(tableLinCod.paginaAtual)) {
					hashCodeModfcd.add(hashCodAnterior);
					endModfcd.add(enderecoAnterior);
					G_File arq = new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + hashCodAnterior);
					arq.write(codigoAnterior);
					// FerramentaAnaliseGeralCtrl flc =
					new FerramentaAnaliseGeralCtrl(codigoAnterior, hashCodAnterior);
				}
			}
			codigoAnterior = codigo;
			hashCodAnterior = tableLinCod.getHashCodeAtual();
			enderecoAnterior = tableLinCod.paginaAtual;
			// TxtBuffer.setContent(scrollPaneDescricao.getTextPane().getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		tableLinCod.delAtualLinha();
	}

	/**
	 * Colocar na classe de controle
	 * 
	 */
	public void getProxPagina() {
		// this.panelDescricaoImagens.parentFrame.setCursor(new
		// Cursor(Cursor.WAIT_CURSOR));
		// this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().selectAll();
		// this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(new
		// Color(255, 255, 255), new Color(0, 0, 0));
		// this.panelDescricaoImagens.scrollPaneDescricao.coloreSource();

		int linha = (Integer) this.dtm.getValueAt(0, 1);
		int coluna = (Integer) this.dtm.getValueAt(0, 2);
		String endereco = (String) this.dtm.getValueAt(0, 0);
		try {

			Statement stConsultaTabPag = null;

			Connection con = (DriverManager.getConnection(ConexaoBanco.bancoEmUso));
			ResultSet rs2 = null;
			if (!paginaAtual.equals(endereco)) {
				stConsultaTabPag = con.createStatement();

				System.out.println("4");

				rs2 = stConsultaTabPag.executeQuery("SELECT * from Pagina WHERE nomePagina='" + endereco + "'"); //
				StringBuilder buffer = new StringBuilder();
				System.out.println("5");
				if (rs2.next()) {

					String hashCodeAtual = rs2.getString("hashCode");
					// this.hashCodeAtual = hashCodeAtual;
					// if
					// (endereco.equals(panelDescricaoImagens.enderecoPagina)) {
					// this.hashCodeInicial = this.hashCodeAtual;
					// }
					// System.out.println("tabela hashcodeAtualBanco:" +
					// this.hashCodeAtual);
					buffer = trabComTemp(hashCodeAtual);

					//System.out.println("9");
					this.boxCode.setText(buffer.toString());
				}
			}
			//System.out.println("10");
			int posAtual = 0;
			int posFinal = 0;
			String codHTML = this.boxCode.getTextPane().getText().replace("\r", "");
			int i;
			for (i = 0; i < (linha - 1); i++) {
				posAtual = codHTML.indexOf("\n", posAtual + 1);
			}
			i = 0;

			//System.out.println("11");
			Statement stConsultaTabImg = con.createStatement();
			//System.out.println("linha: " + linha + " coluna: " + coluna + " endereco: " + endereco);

			rs2 = stConsultaTabImg.executeQuery("SELECT * from imagem WHERE endPagina='" + endereco + "' AND linha=" + linha + " AND coluna=" + coluna); //
			if (rs2.next()) {
				//System.out.println("12");
				posFinal = codHTML.indexOf(rs2.getString("tag"), posAtual + coluna);
				//System.out.println("13");
			}
			// Adaptação provisória
			while (codHTML.charAt(posFinal + i) != '>') {
				i++;
			}
			//System.out.println("14");
			this.setPosTagRepInit(posFinal);
			this.setPosTagRepEnd(posFinal + i + 1);
			//System.out.println("15");
			this.boxCode.goToLine(linha);
			this.boxCode.getTextPane().select(this.getPosTagRepInit(), this.getPosTagRepEnd());
			this.arTextPainelCorrecao.setColorForSelectedText(Color.decode("0xEEEEEE"), new Color(255, 0, 0));
			this.arTextPainelCorrecao.setUnderline();
			//System.out.println("16");
			paginaAtual = endereco;
			/*
			 * /
			 * this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().select(200,250); //
			 */
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * Colocar na classe de controle
	 * 
	 * @param hashCodeAtual
	 * @return
	 */
	private StringBuilder trabComTemp(String hashCodeAtual) {
		StringBuilder buffer;
		if (!(new File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + hashCodeAtual).exists())) {
			buffer = getConteudo(RelatorioDaUrl.pathHD + hashCodeAtual);
			new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + hashCodeAtual).write(buffer.toString());
			System.err.println("arquivo não temporario em uso");
		} else {

			buffer = getConteudo("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + hashCodeAtual);
			System.err.println("arquivo temporario em uso");

		}
		return buffer;
	}

	/**
	 * Colocar na classe de controle
	 * 
	 * @param endArq
	 * @return retorna o conteúdo do arquivo
	 */
	public StringBuilder getConteudo(String endArq) {

		G_File file = new G_File(endArq);
		StringBuilder sbd = new StringBuilder(file.read());

		return sbd;

	}

	private void aplicarPaginaActionPerformed(ActionEvent e) {
		salvarMod.setEnabled(true);
		salvarPag.setEnabled(true);
		aplicar.setEnabled(false);
		strConteudoalt = conteudoDoAlt.getText().replace("\n", " ");
		salvaAlteracoes.setAlterado();
		int corretordePosicoesdoControle = 0;

		conteudoParticRotulo = null;
		conteudoParticRotulo = tArParticipRotulo.getTextoEPos();

		String codHTML = boxCode.getTextPane().getText().replace("\r", "");

		if (codHTML.charAt(getPosTagRepEnd() + corretordePosicoesdoControle - 2) == '/') {
			boxCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 2, getPosTagRepEnd() + corretordePosicoesdoControle);
			arTextPainelCorrecao.setTextoParaSelecionado(" alt=\"" + strConteudoalt + "\"/>");
		} else {
			boxCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 1, getPosTagRepEnd() + corretordePosicoesdoControle);
			arTextPainelCorrecao.setTextoParaSelecionado(" alt=\"" + strConteudoalt + "\">");
		}

		tArParticipRotulo.apagaTexto();
		codigo = boxCode.getTextPane().getText();
		hashCodeModfcd.add(tableLinCod.getHashCodeAtual());
		endModfcd.add((String) this.dtm.getValueAt(tableLinCod.getLinhaAtual(), 0));
		G_File arq = new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + tableLinCod.getHashCodeAtual());
		arq.write(codigo);
		FerramentaAnaliseGeralCtrl flc = new FerramentaAnaliseGeralCtrl(codigo, tableLinCod.getHashCodeAtual());
		boxCode.coloreSource();
		tableLinCod.delAtualLinha();

		// TxtBuffer.setContent(scrollPaneDescricao.getTextPane().getText());

	}

	private void sair() {
		salvaAlteracoes.sair();
	}

	private void salvarActionPerformed(ActionEvent e) {
		// salvaAlteracoes.salvar();
	}

	private void adicionarActionPerformed(ActionEvent e) {
		textoSelecionado = boxCode.getTextPane().getSelectedText();
		if (textoSelecionado == null) {
			// System.out.println(GERAL.SEM_TEXTO_SELECIONADO);
		} else {
			if (tableLinCod.getRowCount() > 0 && tableLinCod.getSelectedRow() != -1) {
				arTextPainelCorrecao.setColorForSelectedText(new Color(255, 204, 102), new Color(0, 0, 0));
				// scrollPaneCorrecaoLabel.setColorForSelectedText(new
				// Color(255, 204, 102), new Color(0, 0, 0));
				tArParticipRotulo.addTexto(textoSelecionado, boxCode.getTextPane().getSelectionStart(), boxCode.getTextPane().getSelectionEnd());
			}
		}
	}

	private void analiseSistematicaActionPerformed(ActionEvent e) {
		new AnaliseGeral((String) dtm.getValueAt(tableLinCod.getSelectedRow(), 2), enderecoPagina);

		// new AnaliseGeral
	}

	private void aplicarRotuloActionPerformed(ActionEvent e) {

		aplicar.setEnabled(false);
		strConteudoalt = conteudoDoAlt.getText().replace("\n", " ");
		salvaAlteracoes.setAlterado();
		int corretordePosicoesdoControle = 0;

		conteudoParticRotulo = null;
		conteudoParticRotulo = tArParticipRotulo.getTextoEPos();

		String codHTML = boxCode.getTextPane().getText().replace("\r", "");

		if (codHTML.charAt(getPosTagRepEnd() + corretordePosicoesdoControle - 2) == '/') {
			boxCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 2, getPosTagRepEnd() + corretordePosicoesdoControle);
			arTextPainelCorrecao.setTextoParaSelecionado(" alt=\"" + strConteudoalt + "\"/>");
		} else {
			boxCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 1, getPosTagRepEnd() + corretordePosicoesdoControle);
			arTextPainelCorrecao.setTextoParaSelecionado(" alt=\"" + strConteudoalt + "\">");
		}

		tArParticipRotulo.apagaTexto();
		codigo = boxCode.getTextPane().getText();

		//G_File arq = new G_File("C:\\ buffer");
		//arq.write(boxCode.getTextPane().getText());

		boxCode.coloreSource();
		tableLinCod.delAtualLinha();
		TxtBuffer.setContent(boxCode.getTextPane().getText());
	}

	public int getPosTagRepEnd() {
		return posTagRepEnd;
	}

	/**
	 * Seta a posição da tag
	 * 
	 * @param posTagRepEnd
	 */
	public void setPosTagRepEnd(int posTagRepEnd) {
		this.posTagRepEnd = posTagRepEnd;
	}

	public int getPosTagRepInit() {
		return posTagRepInit;
	}

	public void setPosTagRepInit(int posTagRepInit) {
		this.posTagRepInit = posTagRepInit;
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
		btnAbrir.setToolTipText(GERAL.DICA_ABRIR);
		btnAbrir.getAccessibleContext().setAccessibleDescription(GERAL.DICA_ABRIR);
		menuArquivo.add(btnAbrir);

		JMenuItem btnAbrirUrl = new JMenuItem(br.org.acessobrasil.silvinha2.mli.XHTML_Panel.BTN_ABRIR_URL);
		btnAbrirUrl.addActionListener(this);
		btnAbrirUrl.setActionCommand("AbrirURL");
		btnAbrirUrl.setMnemonic('U');
		btnAbrirUrl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		btnAbrirUrl.setMnemonic(KeyEvent.VK_U);
		btnAbrirUrl.setToolTipText(br.org.acessobrasil.silvinha2.mli.XHTML_Panel.DICA_ABRIR);
		btnAbrirUrl.getAccessibleContext().setAccessibleDescription(br.org.acessobrasil.silvinha2.mli.XHTML_Panel.DICA_ABRIR);
		menuArquivo.add(btnAbrirUrl);

		btnSalvar = new JMenuItem(GERAL.BTN_SALVAR);
		btnSalvar.addActionListener(this);
		btnSalvar.setActionCommand("Salvar");
		btnSalvar.setMnemonic('S');
		btnSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		btnSalvar.setMnemonic(KeyEvent.VK_S);
		btnSalvar.getAccessibleContext().setAccessibleDescription(GERAL.SALVA_REAVALIA);
		menuArquivo.add(btnSalvar);

		JMenuItem btnSalvarAs = new JMenuItem(GERAL.BTN_SALVAR_COMO);
		btnSalvarAs.addActionListener(this);
		btnSalvarAs.setActionCommand("SaveAs");
		btnSalvarAs.setMnemonic('c');
		btnSalvarAs.setMnemonic(KeyEvent.VK_C);
		// btnSalvarAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
		// ActionEvent.CTRL_MASK));
		btnSalvarAs.setToolTipText(GERAL.DICA_SALVAR_COMO);
		btnSalvarAs.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SALVAR_COMO);
		menuArquivo.add(btnSalvarAs);

		menuArquivo.add(new JSeparator());

		JMenuItem btnFechar = new JMenuItem(GERAL.SAIR);
		btnFechar.addActionListener(this);
		btnFechar.setActionCommand("Sair");
		btnFechar.setMnemonic('a');
		btnFechar.setMnemonic(KeyEvent.VK_A);
		btnFechar.setToolTipText(GERAL.DICA_SAIR);
		btnFechar.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SAIR);
		menuArquivo.add(btnFechar);

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

		JMenuItem btnContraste = new JMenuItem(GERAL.ALTERAR_CONTRASTE);
		btnContraste.addActionListener(this);
		btnContraste.setActionCommand("Contraste");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		// btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD,
		// ActionEvent.CTRL_MASK));
		btnContraste.setToolTipText(GERAL.DICA_CONTRASTE);
		btnContraste.getAccessibleContext().setAccessibleDescription(GERAL.DICA_CONTRASTE);
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

		menu.add(new JSeparator());

		JMenuItem btnProcurar = new JMenuItem(GERAL.PROCURAR);
		btnProcurar.addActionListener(this);
		btnProcurar.setActionCommand("Procurar");
		btnProcurar.setMnemonic('P');
		btnProcurar.setMnemonic(KeyEvent.VK_P);
		btnProcurar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		btnProcurar.setToolTipText(GERAL.DICA_PROCURAR);
		btnProcurar.getAccessibleContext().setAccessibleDescription(GERAL.DICA_PROCURAR);
		menu.add(btnProcurar);

		JMenuItem btnSelecionarTudo = new JMenuItem(GERAL.SELECIONAR_TUDO);
		btnSelecionarTudo.addActionListener(this);
		btnSelecionarTudo.setActionCommand("SelecionarTudo");
		btnSelecionarTudo.setMnemonic('T');
		btnSelecionarTudo.setMnemonic(KeyEvent.VK_T);
		btnSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		btnSelecionarTudo.setToolTipText(GERAL.DICA_SELECIONAR_TUDO);
		btnSelecionarTudo.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SELECIONAR_TUDO);
		menu.add(btnSelecionarTudo);

		JMenuItem btnDesfazer = new JMenuItem(GERAL.DESFAZER);
		btnDesfazer.addActionListener(this);
		btnDesfazer.setActionCommand("Desfazer");
		btnDesfazer.setMnemonic('z');
		btnDesfazer.setMnemonic(KeyEvent.VK_Z);
		btnDesfazer.getAccessibleContext().setAccessibleDescription(GERAL.DICA_DESFAZER);
		btnDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menu.add(btnDesfazer);
		menu.setEnabled(true);
		return menu;
	}

	/**
	 * Abre uma URL
	 * 
	 */
	private void abreUrl() {
		String url;
		url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://");
		PegarPaginaWEB ppw = new PegarPaginaWEB();
		if (url != null) {
			// arquivo=null;
			try {
				String codHtml = ppw.getContent(url);
				// boxCode.setText(codHtml);
				// reavalia(codHtml);
				parentFrame.showPainelFerramentaDescricaoPArq(codHtml);
				EstadoSilvinha.setLinkAtual(url);
			} catch (HttpException e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == "Salvar") {
			// salvaAlteracoes.salvar();

		}else if (cmd.equals("SelecionarTudo")){
			boxCode.getTextPane().selectAll();
			boxCode.getTextPane().requestFocus();
		}  else if (cmd == "Abrir") {
			abrirArquivoLocal();
		} else if (cmd == "SaveAs") {
			salvaAlteracoes.salvarComo();
			// salvarComo();
		} else if (cmd == "AbrirURL") {
			abreUrl();
		} else if (cmd == "Sair") {
			salvaAlteracoes.sair();
		} else if (cmd == "Desfazer") {
			// boxCode.undo();
			// boxCode.coloreSource();
			// reavalia(boxCode.getText());
		} else if (cmd == "AumentaFonte") {
			boxCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			boxCode.diminuiFontSize();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd == "Contraste") {
			boxCode.autoContraste();

			int selectedStart = 0;
			int selectedEnd = 0;
			int corretordePosicoesdoLabel = 0;
			int corretordePosicoesdoControle = 0;
			ArrayList<Integer> ordenador = new ArrayList<Integer>();
			ArrayList<String> conteudoParticRotuloOrdenado = new ArrayList<String>();
			conteudoParticRotulo = null;
			conteudoParticRotulo = tArParticipRotulo.getTextoEPos();
			String[] conteudo = new String[3];
			String codHTML = boxCode.getTextPane().getText().replace("\r", "");
			// System.out.println(codHTML.substring((Integer) (getPosTagRepEnd()
			// + corretordePosicoesdoControle - 1), (getPosTagRepEnd() +
			// corretordePosicoesdoControle - 1) + 36));

			while (codHTML.indexOf("SIL" + inicial) != -1) {
				inicial++;
			}

			ColorModel cm = tArParticipRotulo.getColorModel();

			for (String conteudoPR : conteudoParticRotulo) {
				conteudo = conteudoPR.split("@");
				ordenador.add(Integer.parseInt(conteudo[1]));
			}

			int[] ordem = new int[ordenador.size()];
			for (int i = 0; i < ordem.length; i++) {
				ordem[i] = ordenador.get(i);
			}

			Arrays.sort(ordem);

			for (int i = 0; i < ordem.length; i++) {
				for (String conteudoPR : conteudoParticRotulo) {
					conteudo = conteudoPR.split("@");

					if (Integer.parseInt(conteudo[1]) == ordem[i]) {
						conteudoParticRotuloOrdenado.add(conteudoPR);
					}

				}
			}
			for (String conteudoPR : conteudoParticRotuloOrdenado) {
				conteudo = conteudoPR.split("@");

				// System.out.println("posicão: " +
				// Integer.parseInt(conteudo[1]));

			}

			for (String conteudoPR : conteudoParticRotuloOrdenado) {

				conteudo = conteudoPR.split("@");
				conteudo[0] = "<label for=\"SIL" + inicial + "\">" + conteudo[0] + "</label>";
				selectedStart = Integer.parseInt(conteudo[1]) + corretordePosicoesdoLabel;
				selectedEnd = Integer.parseInt(conteudo[2]) + corretordePosicoesdoLabel;
				// corretordePosicoesdoLabel += ("<label for=\"SIL" + inicial +
				// "\"></label>").length();

				if ((selectedStart < getPosTagRepInit() + corretordePosicoesdoLabel)) {
					corretordePosicoesdoControle = corretordePosicoesdoLabel;

				}
				/*
				 * if((selectedStart>getPosTagRepInit()+corretordePosicoesdoLabel)){
				 * //arTextPainelCorrecao.select(selectedStart+("id=x").length(),
				 * selectedEnd+("id=x").length());
				 * 
				 * }else{ }
				 */
				// scrollPaneCorrecaoLabel.getTextPane().select(selectedStart,
				// selectedEnd);
				// arTextPainelCorrecao.setTextoParaSelecionado(conteudo[0]);
				arTextPainelCorrecao.setASet(arTextPainelCorrecao.getSc().addAttributes(SimpleAttributeSet.EMPTY, SimpleAttributeSet.EMPTY));
				boxCode.getTextPane().select(selectedStart, selectedEnd);
				arTextPainelCorrecao.setColorForSelectedText(new Color(255, 204, 102), new Color(0, 0, 0));
				boxCode.getTextPane().setCharacterAttributes(arTextPainelCorrecao.getASet(), false);

			}

			// arTextPainelCorrecao.formataHTML();
			// tArParticipRotulo.apagaTexto();

			TabelaAnaliseGeral tcl = tableLinCod;
			int linha = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 0);
			int coluna = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 1);
			int endTag = 0;
			int posAtual = 0;
			int posFinal = 0;
			codHTML = boxCode.getTextPane().getText().replace("\r", "");
			int i;
			for (i = 0; i < (linha - 1); i++) {
				posAtual = codHTML.indexOf("\n", posAtual + 1);
			}
			i = 0;
			// gambiarra provisória
			posFinal = codHTML.indexOf((String) dtm.getValueAt(tcl.getSelectedRow(), 2), posAtual + coluna);
			while (codHTML.charAt(posFinal + i) != '>') {
				i++;
			}

			setPosTagRepInit(posFinal);
			setPosTagRepEnd(posFinal + i + 1);

			boxCode.goToLine(linha);
			boxCode.getTextPane().select(getPosTagRepInit(), getPosTagRepEnd());

			arTextPainelCorrecao.setColorForSelectedText(Color.decode("0xEEEEEE"), new Color(255, 0, 0));
			arTextPainelCorrecao.setUnderline();
			// TODO Auto-generated method stub
			// tArParticipRotulo.apagaTexto();

		}

	}

	private Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}

	public String getConteudoSrc(String tag) {
		int pos2;
		int pos = tag.indexOf("src");
		pos = tag.indexOf("=", pos + 1);
		if (tag.indexOf("\"", pos + 1) != -1) {
			pos = tag.indexOf("\"", pos + 1);
			pos2 = tag.indexOf("\"", pos + 1);
			return tag.substring(pos + 1, pos2);
		} else if (tag.indexOf("\'", pos + 1) != -1) {
			pos = tag.indexOf("\'", pos + 1);
			pos2 = tag.indexOf("\'", pos + 1);
			return tag.substring(pos + 1, pos2);
		}

		return tag;
	}

	@Override
	public boolean showBarraUrl() {
		return false;
	}

}
// 