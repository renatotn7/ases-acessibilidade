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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem;

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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;

import org.apache.commons.httpclient.HttpException;
import org.xhtmlrenderer.simple.XHTMLPanel;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Imagens;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.TradPainelRelatorio;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.SalvaAlteracoes;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
import br.org.acessobrasil.silvinha2.util.onChange.OnChange;
import br.org.acessobrasil.silvinha2.util.onChange.OnChangeListener;

/**
 * UI para descrever imagens
 * @author Renato Nati
 */
public class PanelDescricaoImagens extends SuperPainelCentral implements ActionListener, OnChangeListener {
	/**
	 * Serial default
	 */
	private static final long serialVersionUID = 4808373827580204059L;

	public ArrayList<FerramentaDescricaoModel> posicaoeTag;

	public String nomeArquivo;

	public String codigo;

	SalvaAlteracoes salvaAlteracoes;

	public int inicial = 0;

	public static final int ARQUIVO = 0;

	public static final int CONTEUDO = 1;

	private int posTagRepInit;

	JMenuItem btnSalvar;

	boolean original = false;

	private int posTagRepEnd;
	public JButton reverter;
	public ActionEvent buttonAction;

	private String textoSelecionado;

	/**
	 * Mostra a imagem sem descrição
	 */
	XHTMLPanel imagemSemDesc;

	// JEditorPane imagemSemDesc;

	// JLabel imagemSemDesc;

	ArTextPainelCorrecao arTextPainelCorrecao;

	public G_TextAreaSourceCode textAreaSourceCode;

	FrameSilvinha parentFrame;

	private JButton salvar;

	private JButton abrir;

	private JButton cancelar;

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

	TabelaDescricao tableLinCod;

	private JPanel pnBotoes;

	private JButton adicionar;

	public JButton analiseSistematica;

	JButton aplicar;

	private ArrayList<String> conteudoParticRotulo;

	DefaulTableModelNotEditable dtm;

	protected static String enderecoPagina;

	public static void main(String[] args) {

		Ferramenta_Imagens.carregaTexto(TokenLang.LANG);

	}

	public PanelDescricaoImagens(String codHtml, int conteudoOuArquivo, FrameSilvinha parentFrame, String endPagina) {
		enderecoPagina = endPagina;
		EstadoSilvinha.setLinkAtual(endPagina);
		//this.enderecoPagina = FrameSilvinha.getLinkAtual();
		Ferramenta_Imagens.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();
		new FerramentaDescricaoCtrl(this, codHtml, conteudoOuArquivo);
		initComponentsEscalavel(posicaoeTag);

	}

	public PanelDescricaoImagens(String codHtml, int conteudoOuArquivo, FrameSilvinha parentFrame) {
		enderecoPagina = EstadoSilvinha.getLinkAtual();
		Ferramenta_Imagens.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		// this.parentFrame.setTitle(Ferramenta_Imagens.TITULO_DESC_IMG);
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();
		new FerramentaDescricaoCtrl(this, codHtml, conteudoOuArquivo);
		initComponentsEscalavel(posicaoeTag);
	}

	private void initComponentsEscalavel(ArrayList<FerramentaDescricaoModel> erros) {
		Ferramenta_Imagens.carregaTexto(TokenLang.LANG);
		JPanel regraFonteBtn = new JPanel();
		regraFonteBtn.setLayout(new BorderLayout());

		textAreaSourceCode = new G_TextAreaSourceCode();
		textAreaSourceCode.setTipoHTML();
		new OnChange(textAreaSourceCode, this);
		

		// parentFrame.setTitle("Associador de rótulos");
		tableLinCod = new TabelaDescricao(this, erros);
		arTextPainelCorrecao = new ArTextPainelCorrecao(this);

		// scrollPaneCorrecaoLabel = new ConteudoCorrecaoLabel();
		analiseSistematica = new JButton();
		salvar = new JButton();
		abrir = new JButton();
		cancelar = new JButton();
		strConteudoalt = new String();
		// panelLegenda = new JPanel();
		btnSalvar = new JMenuItem(GERAL.BTN_SALVAR);

		pnRegra = new JPanel();
		lbRegras1 = new JLabel();
		lbRegras2 = new JLabel();
		pnSetaDescricao = new JPanel();
		spTextoDescricao = new JScrollPane();
		tArParticipRotulo = new TArParticipRotulo(this);
		conteudoDoAlt = new JTextArea();
		pnListaErros = new JPanel();
		scrollPanetabLinCod = new JScrollPane();
		/**
		 * Mostra pro usuário a imagem que está sem descrição
		 */
		imagemSemDesc = new XHTMLPanel();

		pnBotoes = new JPanel();
		salvar.setEnabled(false);
		salvaAlteracoes = TxtBuffer.getInstanciaSalvaAlteracoes(textAreaSourceCode.getTextPane(), salvar, new JMenuItem(), parentFrame);
		adicionar = new JButton();
		aplicar = new JButton();
		conteudoParticRotulo = new ArrayList<String>();
		analiseSistematica.setEnabled(false);
		// setJMenuBar(this.criaMenuBar());
		// ======== this ========
		// setTitle("Associe explicitamente os r\u00f3tulos aos respectivos
		// controles:");

		setBackground(CoresDefault.getCorPaineis());
		Container contentPane = this;// ??
		contentPane.setLayout(new GridLayout(2, 1));

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
		}

		// G_URLIcon.setIcon(lbTemp,
		// "http://pitecos.blogs.sapo.pt/arquivo/pai%20natal%20o5.%20jpg.jpg");
		JScrollPane sp = new JScrollPane();

		sp.setViewportView(imagemSemDesc);
		sp.setPreferredSize(new Dimension(500, 300));

		// ======== pnDescricao ========

		// ---- Salvar ----
		salvar.setText(Ferramenta_Imagens.BTN_SALVAR);
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarActionPerformed(e);
			}
		});

		salvar.setToolTipText(Ferramenta_Imagens.DICA_SALVAR);
		salvar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_SALVAR);
		salvar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_SALVAR);
		salvar.setBounds(10, 0, 150, 25);

		abrir.setText(Ferramenta_Imagens.BTN_ABRIR);
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbrirActionPerformed(e);
			}
		});

		abrir.setToolTipText(Ferramenta_Imagens.DICA_ABRIR_HTML);
		abrir.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_ABRIR_HTML);
		abrir.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_ABRIR_HTML);
		abrir.setBounds(165, 0, 150, 25);

		cancelar.setText(Ferramenta_Imagens.TELA_ANTERIOR);
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelarActionPerformed(e);
			}
		});

		cancelar.setToolTipText(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.TELA_ANTERIOR);
		cancelar.setBounds(320, 0, 150, 25);

		analiseSistematica.setText(GERAL.ANALISE_SISTEMATICA);
		analiseSistematica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonAction = e;
				Thread t = new Thread(new Runnable() {
					public void run() {
						PainelStatusBar.showProgTarReq();
						parentFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
						analiseSistematicaActionPerformed(buttonAction);
						parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						PainelStatusBar.hideProgTarReq();
					}
				});
				t.setPriority(9);
				t.start();

			}
		});

		analiseSistematica.setToolTipText(GERAL.DICA_ANALISE_SISTEMATICA);
		analiseSistematica.getAccessibleContext().setAccessibleDescription(GERAL.DICA_ANALISE_SISTEMATICA);
		analiseSistematica.getAccessibleContext().setAccessibleName(GERAL.DICA_ANALISE_SISTEMATICA);
		analiseSistematica.setBounds(480, 0, 150, 25);
		// ======== pnParticRotulo ========

		pnSetaDescricao.setBorder(criaBorda(Ferramenta_Imagens.TITULO_DIGITE_O_ALT));
		GridBagConstraints cons = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;
		cons.weightx = 0.80;

		pnSetaDescricao.setLayout(layout);
		cons.anchor = GridBagConstraints.SOUTHEAST;
		cons.insets = new Insets(0, 0, 0, 10);
		conteudoDoAlt.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
				if (conteudoDoAlt.getText().length() == 0) {
					System.out.println("conteudo vazio");
					aplicar.setEnabled(false);

				} else if (tableLinCod.getSelectedRow() != -1) {
					System.out.println("com conteudo");
					aplicar.setEnabled(true);

				}
			}
		});
		// ======== spParticRotulo ========
		{
			spTextoDescricao.setViewportView(conteudoDoAlt);
		}

		// lbRegras1.setText(Reparo_Imagens.REGRAP2);
		// lbRegras1.setHorizontalAlignment(SwingConstants.CENTER);

		// pnRegra.add(lbRegras1);

		pnSetaDescricao.add(spTextoDescricao, cons);
		cons.weightx = 0.20;
		pnSetaDescricao.setPreferredSize(new Dimension(400, 60));

		// ======== pnListaErros ========
		{

			pnListaErros.setBorder(criaBorda(Ferramenta_Imagens.LISTA_ERROS));
			pnListaErros.setLayout(new BorderLayout());
			// ======== scrollPanetabLinCod ========
			{
				scrollPanetabLinCod.setViewportView(tableLinCod);
			}
			pnListaErros.add(scrollPanetabLinCod, BorderLayout.CENTER);
		}
		// ======== pnBotoes ========
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

			adicionar.setToolTipText(Ferramenta_Imagens.DICA_ADICIONAR);
			adicionar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_ADICIONAR);
			adicionar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_ADICIONAR);
			adicionar.setBounds(10, 5, 150, 25);
			// pnBotoes.add(adicionar);

			// ---- aplicarRotulo ----
			aplicar.setEnabled(false);
			aplicar.setText(Ferramenta_Imagens.BTN_APLICAR);
			aplicar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					aplicarRotuloActionPerformed(e);
				}
			});

			aplicar.setToolTipText(Ferramenta_Imagens.DICA_APLICAR);
			aplicar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_APLICAR);
			aplicar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_APLICAR);
			aplicar.setBounds(10, 5, 150, 25);
			pnBotoes.add(aplicar);
		}

		/*
		 * Colocar os controles
		 */
		pnRegra.setBackground(CoresDefault.getCorPaineis());
		regraFonteBtn.add(pnRegra, BorderLayout.NORTH);
		textAreaSourceCode.setBorder(criaBorda(""));
		textAreaSourceCode.setBackground(CoresDefault.getCorPaineis());

		JSplitPane splitPane = null;

		Dimension minimumSize = new Dimension(0, 0);
		// JScrollPane ajudaScrollPane = new
		// JScrollPane(ajuda,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		sp.setMinimumSize(minimumSize);
		sp.setPreferredSize(new Dimension(150, 90));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, textAreaSourceCode);
		splitPane.setOneTouchExpandable(true);
		// splitPane.set
		// splitPane.setDividerLocation(0.95);
		int w = parentFrame.getWidth();
		int s = w / 4;
		splitPane.setDividerLocation(s);

		// regraFonteBtn.add(scrollPaneCorrecaoLabel, BorderLayout.CENTER);
		regraFonteBtn.add(splitPane, BorderLayout.CENTER);
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
		pnSalvarCancelar.add(salvar);
		pnSalvarCancelar.add(abrir);
		pnSalvarCancelar.add(cancelar);
		if (!original) {
			reverter = new JButton("Reverter");
			reverter.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					TxtBuffer.setContent(TxtBuffer.getContentOriginal());
					parentFrame.showPainelFerramentaImgPArq(TxtBuffer.getContentOriginal(),enderecoPagina);
					setVisible(true);
				}
			});
			//reverter.setActionCommand("Reverter");
			reverter.setText(TradPainelRelatorio.REVERTER);
			reverter.setToolTipText(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleDescription(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleName(TradPainelRelatorio.DICA_REVERTER);
			if (EstadoSilvinha.conteudoEmPainelResumo) {
				reverter.setBounds(640, 0, 150, 25);
			}else{
				reverter.setBounds(480, 0, 150, 25);
			}
			pnSalvarCancelar.add(reverter);
		}
		
		if (EstadoSilvinha.conteudoEmPainelResumo) {
			pnSalvarCancelar.add(analiseSistematica);
		}
		pnSalvarCancelar.setBackground(CoresDefault.getCorPaineis());
		textoErrosBtn.add(pnSalvarCancelar, BorderLayout.SOUTH);
		pnListaErros.setBackground(CoresDefault.getCorPaineis());
		textoErrosBtn.add(pnListaErros, BorderLayout.CENTER);
		if (tableLinCod.getRowCount() == 0)
			tableLinCod.addLinha(0, 0, GERAL.DOC_SEM_ERROS);
		contentPane.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(textoErrosBtn);

		this.setVisible(true);

	}

	private void AbrirActionPerformed(ActionEvent e) {
		abrirArquivoLocal();
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
			// frameSilvinha.setTitle(arquivo.getFile().getName() + " - " +
			// XHTMLPanel.TITULO);

			enderecoPagina = temp.getFile().getAbsolutePath();

			TxtBuffer.setContentOriginal(temp.read(),"0");
			parentFrame.showPainelFerramentaImgPArq(temp.read(), enderecoPagina);
			String path = temp.getUrlPath();
			// System.out.print(path);
			EstadoSilvinha.setLinkAtual(path);
		}
	}

	private void CancelarActionPerformed(ActionEvent e) {

		salvaAlteracoes.cancelar();
	}

	private void salvarActionPerformed(ActionEvent e) {
		salvaAlteracoes.salvar();
	}

	private void adicionarActionPerformed(ActionEvent e) {
		textoSelecionado = textAreaSourceCode.getTextPane().getSelectedText();
		if (textoSelecionado == null) {
			// System.out.println(GERAL.SEM_TEXTO_SELECIONADO);
		} else {
			if (tableLinCod.getRowCount() > 0 && tableLinCod.getSelectedRow() != -1) {
				arTextPainelCorrecao.setColorForSelectedText(new Color(255, 204, 102), new Color(0, 0, 0));
				// scrollPaneCorrecaoLabel.setColorForSelectedText(new
				// Color(255, 204, 102), new Color(0, 0, 0));
				tArParticipRotulo.addTexto(textoSelecionado, textAreaSourceCode.getTextPane().getSelectionStart(), textAreaSourceCode.getTextPane().getSelectionEnd());
			}
		}
	}

	private void analiseSistematicaActionPerformed(ActionEvent e) {
		parentFrame.showPainelFerramentaDescricaoAnaliseGeral((String) dtm.getValueAt(tableLinCod.getSelectedRow(), 2), enderecoPagina, this);

		// new AnaliseGeral
	}

	private void aplicarRotuloActionPerformed(ActionEvent e) {
		aplicar.setEnabled(false);
		strConteudoalt = conteudoDoAlt.getText().replace("\n", " ");
		salvaAlteracoes.setAlterado();
		
		conteudoParticRotulo = null;
		conteudoParticRotulo = tArParticipRotulo.getTextoEPos();

		String codHTML = textAreaSourceCode.getTextPane().getText().replace("\r", "");
		//*
		String tag = codHTML.substring(getPosTagRepInit(),getPosTagRepEnd());
		textAreaSourceCode.getTextPane().select(getPosTagRepInit(), getPosTagRepEnd());
		tag=tag.replaceAll("alt=\".*?\"","");
		tag=tag.replaceAll("  "," ");
		if(tag.endsWith("/>")){
			tag = tag.substring(0,tag.length()-2) + " alt=\"" +strConteudoalt + "\" />";
		}else{
			tag = tag.substring(0,tag.length()-1) + " alt=\"" +strConteudoalt + "\" >";
		}
		arTextPainelCorrecao.setTextoParaSelecionado(tag);
		//System.out.println("tag="+tag);
		/*/
		if (codHTML.charAt(getPosTagRepEnd() + corretordePosicoesdoControle - 2) == '/') {
			textAreaSourceCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 2, getPosTagRepEnd() + corretordePosicoesdoControle);
			arTextPainelCorrecao.setTextoParaSelecionado(" alt=\"" + strConteudoalt + "\"/>");
		} else {
			textAreaSourceCode.getTextPane().select(getPosTagRepEnd() + corretordePosicoesdoControle - 1, getPosTagRepEnd() + corretordePosicoesdoControle);
			arTextPainelCorrecao.setTextoParaSelecionado(" alt=\"" + strConteudoalt + "\">");
		}
		//*/
		tArParticipRotulo.apagaTexto();
		codigo = textAreaSourceCode.getTextPane().getText();

		//G_File arq = new G_File("C:\\ buffer");
		//arq.write(textAreaSourceCode.getTextPane().getText());
		new FerramentaDescricaoCtrl(this, codigo, true);
		textAreaSourceCode.coloreSource();
		tableLinCod.delAtualLinha();
		TxtBuffer.setContent(textAreaSourceCode.getTextPane().getText());
	}

	public int getPosTagRepEnd() {
		return posTagRepEnd;
	}

	public void setPosTagRepEnd(int posTagRepEnd) {
		this.posTagRepEnd = posTagRepEnd;
	}

	public int getPosTagRepInit() {
		return posTagRepInit;
	}

	public void setPosTagRepInit(int posTagRepInit) {
		this.posTagRepInit = posTagRepInit;
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
			try {
				String codHtml = ppw.getContent(url);
				TxtBuffer.setContentOriginal(codHtml,"0");
				parentFrame.showPainelFerramentaImgPArq(codHtml, url);
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
			salvaAlteracoes.salvar();

		} else if (cmd == "Abrir") {
			abrirArquivoLocal();
		} else if (cmd.equals("SelecionarTudo")) {
			textAreaSourceCode.getTextPane().selectAll();
			textAreaSourceCode.getTextPane().requestFocus();
		} else if (cmd == "SaveAs") {
			salvaAlteracoes.salvarComo();
			// salvarComo();
		} else if (cmd == "AbrirURL") {
			abreUrl();
		} else if (cmd == "Sair") {
			salvaAlteracoes.sair();
		} else if (cmd == "Desfazer") {
			textAreaSourceCode.undo();
		} else if (cmd == "AumentaFonte") {
			textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd == "Contraste") {
			textAreaSourceCode.autoContraste();

			int selectedStart = 0;
			int selectedEnd = 0;
			int corretordePosicoesdoLabel = 0;
			ArrayList<Integer> ordenador = new ArrayList<Integer>();
			ArrayList<String> conteudoParticRotuloOrdenado = new ArrayList<String>();
			conteudoParticRotulo = null;
			conteudoParticRotulo = tArParticipRotulo.getTextoEPos();
			String[] conteudo = new String[3];
			String codHTML = textAreaSourceCode.getTextPane().getText().replace("\r", "");
			// System.out.println(codHTML.substring((Integer) (getPosTagRepEnd()
			// + corretordePosicoesdoControle - 1), (getPosTagRepEnd() +
			// corretordePosicoesdoControle - 1) + 36));

			while (codHTML.indexOf("SIL" + inicial) != -1) {
				inicial++;
			}

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
				textAreaSourceCode.getTextPane().select(selectedStart, selectedEnd);
				arTextPainelCorrecao.setColorForSelectedText(new Color(255, 204, 102), new Color(0, 0, 0));
				textAreaSourceCode.getTextPane().setCharacterAttributes(arTextPainelCorrecao.getASet(), false);

			}

			// arTextPainelCorrecao.formataHTML();
			// tArParticipRotulo.apagaTexto();

			TabelaDescricao tcl = tableLinCod;
			int linha = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 0);
			int coluna = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 1);
			// int endTag = 0;
			int posAtual = 0;
			int posFinal = 0;
			codHTML = textAreaSourceCode.getTextPane().getText().replace("\r", "");
			int i;
			for (i = 0; i < (linha - 1); i++) {
				posAtual = codHTML.indexOf("\n", posAtual + 1);
			}
			i = 0;
			// Adaptação provisória
			posFinal = codHTML.indexOf((String) dtm.getValueAt(tcl.getSelectedRow(), 2), posAtual + coluna);
			while (codHTML.charAt(posFinal + i) != '>') {
				i++;
			}

			setPosTagRepInit(posFinal);
			setPosTagRepEnd(posFinal + i + 1);

			textAreaSourceCode.goToLine(linha);
			textAreaSourceCode.getTextPane().select(getPosTagRepInit(), getPosTagRepEnd());

			arTextPainelCorrecao.setColorForSelectedText(Color.decode("0xEEEEEE"), new Color(255, 0, 0));
			arTextPainelCorrecao.setUnderline();
		}
	}

	private Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}

	public void altTextFocusLost() {
		System.out.println("altTextFocusLost");
		reload();
	}

	public void reload() {
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();

		codigo = textAreaSourceCode.getTextPane().getText();
		int caret = textAreaSourceCode.getTextPane().getCaretPosition();
		new FerramentaDescricaoCtrl(this, codigo);
		textAreaSourceCode.coloreSource();
		textAreaSourceCode.getTextPane().setCaretPosition(caret);
		TxtBuffer.setContent(codigo);
		tableLinCod.clearTab();
		for (FerramentaDescricaoModel flm : posicaoeTag) {
			tableLinCod.addLinha(flm.getLinha(), flm.getColuna(), flm.getTexto());
		}
	}

	@Override
	public boolean showBarraUrl() {
		return false;
	}

	public void setPanelOriginal(PanelDescricaoImagens ferrLabelPanelNaoEditavel) {

		ferrLabelPanelNaoEditavel.textAreaSourceCode.getTextPane().setEditable(false);
		ferrLabelPanelNaoEditavel.original = true;
		ferrLabelPanelNaoEditavel.pnBotoes.setVisible(false);
	}




	/**
		 * Avalia o arquivo
		 * 
		 * @param path
		 *            caminho
		 */
		public void avaliaArq(String path) {
			//System.out.println("avaliaArq painelObjeto");
			G_File temp = new G_File(path);
			if (temp.getFile() != null) {
				salvaAlteracoes.setNomeDoArquivoEmDisco(null);
				TxtBuffer.setContentOriginal(temp.read(),"0");
				parentFrame.showPainelFerramentaImgPArq(temp.read(),path);
			}
		}
		public void avaliaUrl(String url) {
			PegarPaginaWEB ppw = new PegarPaginaWEB();
			if (url != null) {
				salvaAlteracoes.setNomeDoArquivoEmDisco(null);
				try {
					String codHtml = ppw.getContent(url);
					
					TxtBuffer.setContentOriginal(codHtml, "0");
					parentFrame.showPainelFerramentaImgPArq(codHtml,url);
					
				} catch (HttpException e1) {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		public static String getEnderecoPagina() {
			return enderecoPagina;
		}

		public static void setEnderecoPagina(String enderecoPagina) {
			PanelDescricaoImagens.enderecoPagina = enderecoPagina;
		}

		public G_TextAreaSourceCode getScrollPaneDescricao() {
			return textAreaSourceCode;
		}
}
// 