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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.script;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.apache.commons.httpclient.HttpException;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.nucleo.InterfClienteDoNucleo;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.AlteradorDeCsv;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Imagens;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Scripts;
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
 * @author Renato Nati
 */
public class PainelScript extends SuperPainelCentral implements ActionListener, OnChangeListener {
	/**
	 * 
	 */

	private static final long serialVersionUID = 4808373827580204059L;

	public ArrayList<FerramentaDescricaoModel> posicaoeTag;

	public String nomeArquivo;

	public String codigo;

	public int inicial = 0;

	public static final int ARQUIVO = 0;

	public static final int CONTEUDO = 1;

	private int posTagRepInit;

	private int posTagRepEnd;

	private ArTextPainelCorrecao arTextPainelCorrecao;

	public G_TextAreaSourceCode textAreaSourceCode;

	FrameSilvinha parentFrame;

	// private ConteudoCorrecaoLabel scrollPaneCorrecaoLabel;
	private JMenuBar menuBar;

	JMenuItem btnSalvar;

	private JButton reverter;

	private JButton salvar;

	private JButton abrir;

	private JButton cancelar;

	// private JPanel panelLegenda;
	boolean original = false;

	private JPanel pnRegra;

	private JLabel lbRegras1;

	private JLabel lbRegras2;

	private JPanel pnSetaDescricao;

	private JTextArea conteudoDoAlt;

	private TArParticipRotulo tArParticipRotulo;

	String strConteudoalt;

	private JPanel pnListaErros;

	private JScrollPane scrollPanetabLinCod;

	private TabelaDescricao tableLinCod;

	private JPanel pnBotoes;

	SalvaAlteracoes salvaAlteracoes;

	private JButton aplicar;

	private ArrayList<String> conteudoParticRotulo;

	DefaulTableModelNotEditable dtm;

	public static void main(String[] args) {
		// new FerramentaReparo_Scripts("C:\\pv2.15_3.13_faltaDeLabel.html");
		Ferramenta_Scripts.carregaTexto(TokenLang.LANG);

	}

	public PainelScript(String codHtml, int conteudoOuArquivo, FrameSilvinha parentFrame) {
		Ferramenta_Scripts.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();
		new FerramentaDescricaoCtrl(codHtml, conteudoOuArquivo);
		initComponentsEscalavel(posicaoeTag);
	}

	public PainelScript(String conteudo, FrameSilvinha parentFrame) {
		Ferramenta_Scripts.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();
		new FerramentaDescricaoCtrl(TxtBuffer.getContent(original));
		initComponentsEscalavel(posicaoeTag);

	}

	public PainelScript(FrameSilvinha parentFrame) {
		Ferramenta_Scripts.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();
		new FerramentaDescricaoCtrl(TxtBuffer.getContent(original));
		initComponentsEscalavel(posicaoeTag);

	}

	public PainelScript(ArrayList<FerramentaDescricaoModel> erros) {

		initComponentsEscalavel(erros);

	}

	private void initComponentsEscalavel(ArrayList<FerramentaDescricaoModel> erros) {
		Ferramenta_Scripts.carregaTexto(TokenLang.LANG);
		JPanel regraFonteBtn = new JPanel();
		regraFonteBtn.setLayout(new BorderLayout());

		textAreaSourceCode = new G_TextAreaSourceCode();
		textAreaSourceCode.setTipoHTML();
		new OnChange(textAreaSourceCode, this);
		tableLinCod = new TabelaDescricao(erros);
		arTextPainelCorrecao = new ArTextPainelCorrecao();
		salvar = new JButton();
		abrir = new JButton();
		cancelar = new JButton();
		strConteudoalt = new String();
		btnSalvar = new JMenuItem("Salvar");
		salvar.setEnabled(false);
		salvaAlteracoes = TxtBuffer.getInstanciaSalvaAlteracoes(textAreaSourceCode.getTextPane(), salvar, new JMenuItem(), parentFrame);

		pnRegra = new JPanel();
		lbRegras1 = new JLabel();
		lbRegras2 = new JLabel();
		pnSetaDescricao = new JPanel();
		new JScrollPane();
		tArParticipRotulo = new TArParticipRotulo();
		conteudoDoAlt = new JTextArea();
		pnListaErros = new JPanel();
		scrollPanetabLinCod = new JScrollPane();
		pnBotoes = new JPanel();
		new JButton();
		aplicar = new JButton();
		conteudoParticRotulo = new ArrayList<String>();
		setBackground(CoresDefault.getCorPaineis());
		Container contentPane = this;// ??
		contentPane.setLayout(new GridLayout(2, 1));
		{
			pnRegra.setBorder(criaBorda(Ferramenta_Scripts.TITULO_REGRA));
			pnRegra.setLayout(new GridLayout(2, 1));
			pnRegra.add(lbRegras1);
			lbRegras1.setText(Ferramenta_Scripts.REGRAP1);
			lbRegras2.setText(Ferramenta_Scripts.REGRAP2);
			lbRegras1.setHorizontalAlignment(SwingConstants.CENTER);

			lbRegras2.setHorizontalAlignment(SwingConstants.CENTER);
			pnRegra.add(lbRegras1);
			pnRegra.add(lbRegras2);
			pnRegra.setPreferredSize(new Dimension(700, 60));
		}
		// ---- Salvar ----
		salvar.setText(Ferramenta_Scripts.BTN_SALVAR);
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarActionPerformed(e);
			}
		});

		salvar.setToolTipText(Ferramenta_Scripts.DICA_SALVAR);
		salvar.getAccessibleContext().setAccessibleDescription(Ferramenta_Scripts.DICA_SALVAR);
		salvar.getAccessibleContext().setAccessibleName(Ferramenta_Scripts.DICA_SALVAR);
		salvar.setBounds(10, 0, 150, 25);

		abrir.setText(Ferramenta_Scripts.BTN_ABRIR);
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbrirActionPerformed(e);
			}
		});

		abrir.setToolTipText(Ferramenta_Scripts.DICA_ABRIR);
		abrir.getAccessibleContext().setAccessibleDescription(Ferramenta_Scripts.DICA_ABRIR_HTML);
		abrir.getAccessibleContext().setAccessibleName(Ferramenta_Scripts.DICA_ABRIR_HTML);
		abrir.setBounds(165, 0, 150, 25);

		cancelar.setText(Ferramenta_Imagens.TELA_ANTERIOR);
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelarActionPerformed(e);
			}
		});

		cancelar.setToolTipText(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.setBounds(320, 0, 150, 25);
		GridBagConstraints cons = new GridBagConstraints();
		
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;

		pnSetaDescricao.setLayout(new BorderLayout());
		cons.anchor = GridBagConstraints.WEST;
		cons.insets = new Insets(0, 0, 0, 0);
		JScrollPane sp = new JScrollPane();
		sp.setBorder(criaBorda(Ferramenta_Scripts.CONTEUDO_ALTER));
		sp.setViewportView(conteudoDoAlt);
		sp.setPreferredSize(new Dimension(500, 300));
		pnSetaDescricao.setPreferredSize(new Dimension(400, 60));
		pnListaErros.setBorder(criaBorda(Ferramenta_Scripts.LISTA_ERROS));
		pnListaErros.setLayout(new BorderLayout());
		scrollPanetabLinCod.setViewportView(tableLinCod);
		pnListaErros.add(scrollPanetabLinCod, BorderLayout.CENTER);
		{
			pnBotoes.setLayout(null);
			aplicar.setEnabled(false);
			aplicar.setText(Ferramenta_Scripts.BTN_APLICAR);
			aplicar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					aplicarRotuloActionPerformed(e);
				}
			});

			aplicar.setToolTipText(Ferramenta_Scripts.DICA_APLICAR);
			aplicar.getAccessibleContext().setAccessibleDescription(Ferramenta_Scripts.DICA_APLICAR);
			aplicar.getAccessibleContext().setAccessibleName(Ferramenta_Scripts.DICA_APLICAR);
			aplicar.setBounds(3, 3, 120, 30);
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
		sp.setMinimumSize(minimumSize);
		sp.setPreferredSize(new Dimension(150, 90));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, textAreaSourceCode);
		splitPane.setOneTouchExpandable(true);
		int w = parentFrame.getWidth();
		int s = w / 4;
		splitPane.setDividerLocation(s);
		regraFonteBtn.add(splitPane, BorderLayout.CENTER);
		pnBotoes.setPreferredSize(new Dimension(600, 35));
		pnBotoes.setBackground(CoresDefault.getCorPaineis());
		regraFonteBtn.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(regraFonteBtn);

		JPanel textoErrosBtn = new JPanel();
		textoErrosBtn.setLayout(new BorderLayout());
		pnSetaDescricao.setBackground(CoresDefault.getCorPaineis());
		pnSetaDescricao.add(pnBotoes, BorderLayout.WEST);
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
			reverter.setText(TradPainelRelatorio.REVERTER);
			reverter.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					TxtBuffer.setContent(TxtBuffer.getContentOriginal());
					parentFrame.showPainelFerramentaScriptPArq(TxtBuffer.getContentOriginal());
					setVisible(true);
				}
				
			});
			reverter.setToolTipText(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleDescription(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleName(TradPainelRelatorio.DICA_REVERTER);
			reverter.setBounds(485, 0, 150, 25);
			pnSalvarCancelar.add(reverter);
		}
		pnSalvarCancelar.setBackground(CoresDefault.getCorPaineis());
		textoErrosBtn.add(pnSalvarCancelar, BorderLayout.SOUTH);
		pnListaErros.setBackground(CoresDefault.getCorPaineis());
		if (tableLinCod.getRowCount() == 0)
			tableLinCod.addLinha(0, 0, GERAL.DOC_SEM_ERROS);
		contentPane.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(textoErrosBtn);
		this.setVisible(true);
	}

	private void AbrirActionPerformed(ActionEvent e) {

		G_File caminhoRecente = new G_File("config/html_recente.txt");
		String a[] = { ".html", ".htm" };
		G_File temp = new G_File(caminhoRecente.read(), a);
		if (temp.getFile() != null) {
			caminhoRecente.write(temp.getFile().getAbsolutePath());
			TxtBuffer.setContentOriginal(temp.read(),"0");
			parentFrame.showPainelFerramentaScriptPArq(temp.read());
		}
	}

	private void Abrir() {
		G_File caminhoRecente = new G_File("config/html_recente.txt");
		String a[] = { ".html", ".htm" };
		G_File temp = new G_File(caminhoRecente.read(), a);
		if (temp.getFile() != null) {
			caminhoRecente.write(temp.getFile().getAbsolutePath());
			TxtBuffer.setContentOriginal(temp.read(),"0");
			parentFrame.showPainelFerramentaScriptPArq(temp.read());
		}
	}

	private void CancelarActionPerformed(ActionEvent e) {
		salvaAlteracoes.cancelar();
	}

	private void salvarActionPerformed(ActionEvent e) {
		System.out.println("PainelScript salvando... original = " + original);
		if(original) return;
		salvaAlteracoes.salvar();
	}

	/**
	 * Aplica a descricao ao script
	 * @param e
	 */
	private void aplicarRotuloActionPerformed(ActionEvent e) {

		aplicar.setEnabled(false);
		strConteudoalt = conteudoDoAlt.getText().replace("\n", " ");
		salvaAlteracoes.setAlterado();
		int corretordePosicoesdoControle = 0;
		int caret = 0;
		caret = getPosTagRepInit();
		conteudoParticRotulo = null;
		conteudoParticRotulo = tArParticipRotulo.getTextoEPos();

		int posBeginBody = textAreaSourceCode.getTextPane().getText().indexOf("<body");
		if(posBeginBody!=-1){
			posBeginBody = textAreaSourceCode.getTextPane().getText().indexOf(">",posBeginBody);
		}
		int posFechaScript = textAreaSourceCode.getTextPane().getText().indexOf("</script>", getPosTagRepEnd() + corretordePosicoesdoControle);
		posFechaScript = textAreaSourceCode.getTextPane().getText().indexOf(">", posFechaScript);		
		//O noscript deve ser colocado sempre dentro do body
		if(posBeginBody<posFechaScript && posBeginBody!=-1){
			textAreaSourceCode.getTextPane().select(posFechaScript + 1, posFechaScript + 1);
		}else{
			textAreaSourceCode.getTextPane().select(posBeginBody + 1, posBeginBody + 1);
		}
		arTextPainelCorrecao.setTextoParaSelecionado("<noscript><p>" + strConteudoalt + "</p></noscript>");
		tArParticipRotulo.apagaTexto();
		codigo = textAreaSourceCode.getTextPane().getText();
		TxtBuffer.setContent(textAreaSourceCode.getTextPane().getText());
		new FerramentaDescricaoCtrl(codigo, true);
		textAreaSourceCode.coloreSource();
		textAreaSourceCode.getTextPane().setCaretPosition(caret);
		tableLinCod.delAtualLinha();
	}

	private class FerramentaDescricaoCtrl implements InterfClienteDoNucleo {
		AlteradorDeCsv alteradorDeCsv;
		private boolean salva = false;
		private InterfNucleos nucleo;
		ArrayList<ArmazenaErroOuAviso> errosP1;
		public FerramentaDescricaoCtrl() {
		}
		public FerramentaDescricaoCtrl(String string, int nomeArquivoouConteudo) {
			if (string == null)
				return;
			nucleo = MethodFactNucleos.mFNucleos("Estruturado");
			if (nomeArquivoouConteudo == 0) {
				G_File arq = new G_File(string);
				nucleo.setCodHTML(arq.read());
			}
			if (nomeArquivoouConteudo == 1) {
				nucleo.setCodHTML(string);
			}
			if (EstadoSilvinha.orgao == 2) {

				nucleo.setWCAGEMAG(InterfNucleos.EMAG);
			} else {

				nucleo.setWCAGEMAG(InterfNucleos.WCAG);
			}

			nucleo.addCliente(this);
			nucleo.avalia();
			codigo = nucleo.getCodHTMLOriginal();

		}

		public FerramentaDescricaoCtrl(String conteudo) {
			if (conteudo == null)
				return;

			nucleo = MethodFactNucleos.mFNucleos("Estruturado");
			nucleo.setCodHTML(conteudo);
			if (EstadoSilvinha.orgao == 2) {

				nucleo.setWCAGEMAG(InterfNucleos.EMAG);
			} else {

				nucleo.setWCAGEMAG(InterfNucleos.WCAG);
			}

			nucleo.addCliente(this);
			nucleo.avalia();
			codigo = nucleo.getCodHTMLOriginal();

		}

		public FerramentaDescricaoCtrl(String conteudo, boolean salva) {

			if (conteudo == null)
				return;
			this.salva = salva;
			if (EstadoSilvinha.conteudoEmPainelResumo) {
				alteradorDeCsv = new AlteradorDeCsv(EstadoSilvinha.hashCodeAtual);
			}
			nucleo = MethodFactNucleos.mFNucleos("Estruturado");
			nucleo.setCodHTML(conteudo);
			if (EstadoSilvinha.orgao == 2) {

				nucleo.setWCAGEMAG(InterfNucleos.EMAG);
			} else {

				nucleo.setWCAGEMAG(InterfNucleos.WCAG);
			}
			nucleo.addCliente(this);
			nucleo.avalia();
			codigo = nucleo.getCodHTMLOriginal();

		}

		private void reload_bkp() {
			//G_File arq = new G_File("C:\\ buffer");
			G_File arq = new G_File("");

			// errosP2.clear();
			posicaoeTag.clear();
			nucleo = MethodFactNucleos.mFNucleos("Estruturado");
			nucleo.setCodHTML(arq.read());
			if (EstadoSilvinha.orgao == 2) {

				nucleo.setWCAGEMAG(InterfNucleos.EMAG);
			} else {

				nucleo.setWCAGEMAG(InterfNucleos.WCAG);
			}

			nucleo.addCliente(this);
			nucleo.avalia();
			codigo = nucleo.getCodHTMLOriginal();

			tableLinCod.clearTab();
			tableLinCod.initComponents(posicaoeTag);

		}

		public void avisosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				String valor = alteradorDeCsv.getAvisoP1(EstadoSilvinha.hashCodeAtual);

				valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

				alteradorDeCsv.setAvisoP1(EstadoSilvinha.hashCodeAtual, valor);
			}
		}

		public void avisosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				String valor = alteradorDeCsv.getAvisoP2(EstadoSilvinha.hashCodeAtual);

				valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

				alteradorDeCsv.setAvisoP2(EstadoSilvinha.hashCodeAtual, valor);
			}
		}

		public void avisosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				String valor = alteradorDeCsv.getAvisoP3(EstadoSilvinha.hashCodeAtual);

				valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

				alteradorDeCsv.setAvisoP3(EstadoSilvinha.hashCodeAtual, valor);
			}
		}

		public void errosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				String valor = alteradorDeCsv.getErroP1(EstadoSilvinha.hashCodeAtual);
				valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

				alteradorDeCsv.setErroP1(EstadoSilvinha.hashCodeAtual, valor);
			}
			errosP1 = armazenaErroOuAviso;
		}

		public void errosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				String valor = alteradorDeCsv.getErroP2(EstadoSilvinha.hashCodeAtual);
				valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

				alteradorDeCsv.setErroP2(EstadoSilvinha.hashCodeAtual, valor);
			}
		}

		public void errosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				String valor = alteradorDeCsv.getErroP3(EstadoSilvinha.hashCodeAtual);
				valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

				alteradorDeCsv.setErroP3(EstadoSilvinha.hashCodeAtual, valor);
			}
		}

		public void fimDaAvaliacao() {
			if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
				alteradorDeCsv.escreveEmDisco();
			}
			this.salva = false;
			if (nucleo.getCodWcagEmag() == InterfNucleos.EMAG) {
				for (ArmazenaErroOuAviso aEA : errosP1) {

					if (aEA.getIdTextoRegra().equals("1.11")) {

						FerramentaDescricaoModel errado = new FerramentaDescricaoModel(aEA.getLinha(), aEA.getColuna());
						errado.setTexto(aEA.getTagCompleta());
						if (aEA.getTagCompleta().toLowerCase().indexOf("<script") != -1) {
							posicaoeTag.add(errado);
						}
					}
				}
			} else if (nucleo.getCodWcagEmag() == InterfNucleos.WCAG) {
				for (ArmazenaErroOuAviso aEA : errosP1) {
					if (aEA.getIdTextoRegra().equals("1.1")) {
						FerramentaDescricaoModel errado = new FerramentaDescricaoModel(aEA.getLinha(), aEA.getColuna());
						errado.setTexto(aEA.getTagCompleta());
						if (aEA.getTagCompleta().toLowerCase().indexOf("<script") != -1) {
							posicaoeTag.add(errado);
						}

					}
				}
			}
		}

		public void linksAchadosPeloNucleo(HashSet<String> links) {

		}

		public ArrayList<FerramentaDescricaoModel> getPosicaoeTag() {
			return posicaoeTag;
		}

		public void setPosicaoeTag(ArrayList<FerramentaDescricaoModel> posiceTag) {
			posicaoeTag = posiceTag;
		}

	}

	private class FerramentaDescricaoModel {
		/**
		 * Linha onde a tag está localizada.
		 */
		private int linha;

		/**
		 * Coluna onde a tag está localizada.
		 */
		private int coluna;

		private String texto;

		/**
		 * Construtor de Posicao.
		 * 
		 * @param posLinha
		 *            Linha onde está a tag.
		 * @param posCol
		 *            Coluna onde está a tag.
		 */
		public FerramentaDescricaoModel(final int posLinha, final int posCol) {

			this.linha = posLinha;
			this.coluna = posCol;
		}

		public FerramentaDescricaoModel() {
		}

		/**
		 * @see java.lang.Object#toString()
		 * @return Retorna as coordenadas da tag HTML (linha,coluna).
		 */
		@Override
		public String toString() {
			return this.linha + " | " + this.coluna;
		}

		/**
		 * @return Retorna o valor de coluna.
		 */
		public int getColuna() {
			return coluna;
		}

		/**
		 * @param numCol
		 *            Seta o valor de coluna.
		 */
		public void setColuna(final int numCol) {
			this.coluna = numCol;
		}

		/**
		 * @return Retorna o valor de linha.
		 */
		public int getLinha() {
			return linha;
		}

		/**
		 * @param numLinha
		 *            Seta o valor de linha.
		 */
		public void setLinha(final int numLinha) {
			this.linha = numLinha;
		}

		public String getTexto() {
			return texto;
		}

		public void setTexto(String texto) {
			this.texto = texto;
		}

	}

	private class TabelaDescricao extends JTable implements MouseListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private TabelaDescricao(ArrayList<FerramentaDescricaoModel> erros) {
			initComponents(erros);

		}

		private void initComponents(ArrayList<FerramentaDescricaoModel> erros) {
			// JFormDesigner - Component initialization - DO NOT MODIFY
			// //GEN-BEGIN:initComponents
			// Generated using JFormDesigner Evaluation license - Renato Nati

			dtm = new DefaulTableModelNotEditable();
			dtm.setColumnIdentifiers(new String[] { GERAL.LINHA, GERAL.COLUNA, GERAL.TAG });
			this.addMouseListener(this);
			setModel(dtm);
			{
				TableColumnModel cm = getColumnModel();
				cm.getColumn(0).setMaxWidth(50);
				cm.getColumn(1).setMaxWidth(50);
				// cm.getColumn(2).setMaxWidth(600);
			}
			// JFormDesigner - End of component initialization
			// //GEN-END:initComponents

			for (FerramentaDescricaoModel flm : erros) {
				addLinha(flm.getLinha(), flm.getColuna(), flm.getTexto());
			}
		}

		// JFormDesigner - Variables declaration - DO NOT MODIFY
		// //GEN-BEGIN:variables
		// Generated using JFormDesigner Evaluation license - Renato Nati
		// JFormDesigner - End of variables declaration //GEN-END:variables

		public void clearTab() {
			for (int i = 0; i < dtm.getRowCount(); i++)
				dtm.removeRow(i);
		}

		public void addLinha(int linha, int coluna, String codigo) {
			try {
				if (dtm.getRowCount() > 0)
					if (Integer.parseInt((String) dtm.getValueAt(0, 0)) == 0 && Integer.parseInt((String) dtm.getValueAt(0, 1)) == 0)
						dtm.removeRow(0);
			} catch (Exception e) {

			}
			try {
				dtm.addRow(new Object[] { linha, coluna, codigo });
			} catch (Exception e) {

			}
		}

		public void delAtualLinha() {
			dtm.removeRow(this.getSelectedRow());
		}

		public void mouseClickedbkp(MouseEvent e) {

			arTextPainelCorrecao.selectAll();
			arTextPainelCorrecao.setColorForSelectedText(new Color(255, 255, 255), new Color(0, 0, 0));
			arTextPainelCorrecao.formataHTML();
			tArParticipRotulo.apagaTexto();
			aplicar.setEnabled(false);
			TabelaDescricao tcl = ((TabelaDescricao) e.getComponent());
			int linha = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 0);
			int coluna = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 1);
			int posAtual = 0;
			int posFinal = 0;
			String codHTML = arTextPainelCorrecao.getText().replace("\r", "");
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

			arTextPainelCorrecao.select(getPosTagRepInit(), getPosTagRepEnd());

			arTextPainelCorrecao.setColorForSelectedText(new Color(255, 255, 255), new Color(255, 0, 0));
			arTextPainelCorrecao.setUnderline();
		}

		public void selectTag() {

		}

		public void mouseClicked(MouseEvent e) {
			new Thread(new Runnable() {
				public void run() {
					System.gc();
				}

			}).start();
			textAreaSourceCode.getTextPane().selectAll();
			arTextPainelCorrecao.setColorForSelectedText(new Color(255, 255, 255), new Color(0, 0, 0));
			textAreaSourceCode.coloreSource();
			// arTextPainelCorrecao.formataHTML();
			tArParticipRotulo.apagaTexto();
			aplicar.setEnabled(true);
			TabelaDescricao tcl = ((TabelaDescricao) e.getComponent());
			int linha = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 0);
			int coluna = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 1);
			int posAtual = 0;
			int posFinal = 0;
			String codHTML = textAreaSourceCode.getTextPane().getText().replace("\r", "");
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

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}
	}

	private class ArTextPainelCorrecao extends JTextPane implements MouseListener {
		private StyledDocument styledDocument;

		private DefaultStyledDocument doc = null;

		private ArrayList<String> modificados = new ArrayList<String>();

		private Style defautStyle = null;

		private Style mainStyle = null;

		private JTextPane texto;

		private AttributeSet aSet;

		private StyleContext sc;

		private ArTextPainelCorrecao() {
			initComponents();

		}

		private void initComponents() {
			textAreaSourceCode.getTextPane().setEditable(true);
			testaRTF();
		}

		/**
		 * Troca o texto da área seleciona pelo parametro
		 * 
		 * @param texto
		 */
		public void setTextoParaSelecionado(String texto) {

			int intTemp = 0;
			int startSelect = textAreaSourceCode.getTextPane().getSelectionStart();
			int endSelect = textAreaSourceCode.getTextPane().getSelectionEnd();
			String codHTML = textAreaSourceCode.getTextPane().getText();

			// observação: o JtextArea não identifica \r
			// apenas \n, de modo que tem que compensar a falta de \r
			// o getSelectionStart() não conta o \r
			intTemp = 0;
			while (codHTML.indexOf("\r", intTemp + 1) != -1) {
				intTemp = codHTML.indexOf("\r", intTemp + 1);

				if (intTemp < startSelect) {
					startSelect++;
					endSelect++;

				}
				if (intTemp > startSelect) {
					endSelect++;

				}
				if (intTemp > endSelect) {
					break;
				}

			}
			intTemp = 0;
			textAreaSourceCode.getTextPane().setText(codHTML.substring(0, startSelect) + texto + codHTML.substring(endSelect - 0, codHTML.length()));

			sc = StyleContext.getDefaultStyleContext();

			defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

			mainStyle = sc.addStyle("MainStyle", defautStyle);
			StyleConstants.setForeground(mainStyle, new Color(0, 0, 255));
			aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);
			textAreaSourceCode.getTextPane().select(startSelect, endSelect - 1);
			textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);

		}

		/**
		 * Troca o texto da área seleciona pelo parametro
		 * 
		 * @param texto
		 * @deprecated
		 */
		public void setTextoParaSelecionadoBKP(String texto) {

			int intTemp = 0;
			int startSelect = getSelectionStart();
			int endSelect = getSelectionEnd();
			int contEnter = 0;
			String codHTML = getText();

			// observação: o JtextArea não identifica \r
			// apenas \n, de modo que tem que compensar a falta de \r
			// o getSelectionStart() não conta o \r
			intTemp = 0;
			while (codHTML.indexOf("\r", intTemp + 1) != -1) {
				intTemp = codHTML.indexOf("\r", intTemp + 1);

				if (intTemp < startSelect) {
					startSelect++;
					endSelect++;

				}
				if (intTemp > startSelect) {
					endSelect++;

				}
				if (intTemp > endSelect) {
					break;
				}

			}
			intTemp = 0;
			setText(codHTML.substring(0, startSelect) + texto + codHTML.substring(endSelect - 0, codHTML.length()));

			sc = StyleContext.getDefaultStyleContext();

			defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

			mainStyle = sc.addStyle("MainStyle", defautStyle);
			StyleConstants.setForeground(mainStyle, new Color(0, 0, 255));
			aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);

			this.select(startSelect, endSelect - 1);
			this.setCharacterAttributes(aSet, false);

		}

		public void testaRTF() {

			sc = StyleContext.getDefaultStyleContext();

			defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
			mainStyle = sc.addStyle("MainStyle", defautStyle);

			aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);
			// codigo=codigo.replace("\r","\t");

			textAreaSourceCode.getTextPane().setText(codigo);
			textAreaSourceCode.getTextPane().setText(textAreaSourceCode.getTextPane().getText().replaceAll("\r", ""));

			textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);

			textAreaSourceCode.coloreSource();
			textAreaSourceCode.getTextPane().addMouseListener(this);

		}

		public void setUnderline() {
			sc = StyleContext.getDefaultStyleContext();

			defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
			mainStyle = sc.addStyle("MainStyle", defautStyle);

			StyleConstants.setUnderline(mainStyle, true);

			aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);

			textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);

		}

		public void setColorForSelectedText(Color background, Color foreground) {
			sc = StyleContext.getDefaultStyleContext();

			defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
			mainStyle = sc.addStyle("MainStyle", defautStyle);

			if (textAreaSourceCode.getAutoContrasteTipo() == 0) {
				StyleConstants.setBold(mainStyle, true);
				StyleConstants.setBackground(mainStyle, background);
				StyleConstants.setForeground(mainStyle, foreground);

			} else if (textAreaSourceCode.getAutoContrasteTipo() == 1) {
				StyleConstants.setBold(mainStyle, true);
				StyleConstants.setBackground(mainStyle, Color.BLACK);
				StyleConstants.setForeground(mainStyle, Color.WHITE);

			} else if (textAreaSourceCode.getAutoContrasteTipo() == 2) {
				StyleConstants.setBold(mainStyle, true);
				StyleConstants.setBackground(mainStyle, Color.WHITE);
				StyleConstants.setForeground(mainStyle, Color.BLACK);
			}

			aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);

			textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);
			addModificados(textAreaSourceCode.getTextPane().getSelectionStart(), textAreaSourceCode.getTextPane().getSelectionEnd(), background.getRed(), background.getGreen(),
					background.getBlue());
		}

		public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() == 2) {
				JTextPane txtSource = ((JTextPane) e.getComponent());
				int posClick = txtSource.getCaretPosition();
				String codHTML = txtSource.getText().replace("\r", "");

				int abreTag = codHTML.indexOf("<", posClick);
				int fechaTag = codHTML.indexOf(">", posClick);
				if (!(fechaTag < abreTag && fechaTag != -1)) {
					// não está dentro de tag
					return;
				}
				boolean achou = false;
				for (int i = fechaTag; i >= 0; i--) {
					if (codHTML.charAt(i) == '<') {
						abreTag = i;
						achou = true;
						break;
					}
				}
				if (achou) {
					// Seleciona a tag
					// System.out.print("abreTag=" + abreTag + "\n");
					// System.out.print("fechaTag=" + fechaTag + "\n");
					textAreaSourceCode.getTextPane().select(abreTag, fechaTag + 1);
				}
			}
		}

		public void mouseClickedBKP(MouseEvent e) {
			if (e.getClickCount() == 1) {
				StyleContext sc = StyleContext.getDefaultStyleContext();
				// AttributeSet aSet = sc.addAttribute(SimpleAttributeSet.EMPTY,
				// StyleConstants.Bold, true);
				AttributeSet aSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Underline, true);
				// AttributeSet aSet = sc.addAttribute(SimpleAttributeSet.EMPTY,
				// StyleConstants.Foreground, Color.BLUE);
				JTextPane txtSource = ((JTextPane) e.getComponent());
				int posClick = txtSource.getCaretPosition();
				// System.out.println("init" + posClick + "\n");
				int countEnter = 0, countTab = 0, ocorrencia1 = 0, intTemp = 0;

				while (txtSource.getText().indexOf("\t", intTemp + 1) < posClick && txtSource.getText().indexOf("\t", intTemp + 1) != -1) {
					intTemp = ((JTextPane) e.getComponent()).getText().indexOf("\t", intTemp + 1);
					posClick++;
					if (intTemp < posClick) {
						countEnter++;
					}
				}
				intTemp = 0;
				while (((JTextPane) e.getComponent()).getText().indexOf("\n", intTemp + 1) < posClick && ((JTextPane) e.getComponent()).getText().indexOf("\n", intTemp + 1) != -1) {
					intTemp = ((JTextPane) e.getComponent()).getText().indexOf("\n", intTemp + 1);
					posClick++;
					if (intTemp < posClick) {
						countEnter++;
					}
				}

				String conteudo = ((JTextPane) e.getComponent()).getText();
				// System.out.println(posClick);
				int firstPosFechaTag = 0;
				int firstPosAbreTag = 0;

				boolean isTag = false;
				firstPosFechaTag = conteudo.indexOf(">", posClick);
				firstPosAbreTag = conteudo.indexOf("<", posClick);

				// posClick+=countEnter;

				firstPosFechaTag = firstPosFechaTag - countEnter;
				firstPosAbreTag = firstPosAbreTag - countEnter;

				if (firstPosFechaTag < firstPosAbreTag && firstPosFechaTag > -1) {

					isTag = true; // *
				} else {
					isTag = false;
				}

				if (firstPosAbreTag == -1) {

					isTag = true; // *
				}

				if (firstPosFechaTag == -1) {
					isTag = false;
				}

				if (isTag) {
					ocorrencia1 = 0;
					intTemp = 0;
					while (conteudo.indexOf("<", intTemp + 1) < posClick && conteudo.indexOf("<", intTemp + 1) != -1) {
						intTemp = conteudo.indexOf("<", intTemp + 1);
						if (intTemp < (posClick)) {
							ocorrencia1 = intTemp;
						}

					}

					ocorrencia1 = ocorrencia1 - countEnter;
					// System.out.println("CalcEnter" + countEnter);
					// System.out.println("posClick" + posClick);
					// System.out.println("Ocorrencia1" + ocorrencia1 + "\n");
					// System.out.println("FechaTag" + firstPosFechaTag + "\n");
					textAreaSourceCode.getTextPane().select(ocorrencia1, firstPosFechaTag + 1);

				}

			}

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

			// this.setSelectionColor(new Color(123,123,132));

		}

		public void mouseReleased(MouseEvent e) {

		}

		public AttributeSet getASet() {
			return aSet;
		}

		public void setASet(AttributeSet set) {
			aSet = set;
		}

		public Style getDefautStyle() {
			return defautStyle;
		}

		public void setDefautStyle(Style defautStyle) {
			this.defautStyle = defautStyle;
		}

		public DefaultStyledDocument getDoc() {
			return doc;
		}

		public void setDoc(DefaultStyledDocument doc) {
			this.doc = doc;
		}

		public Style getMainStyle() {
			return mainStyle;
		}

		public void setMainStyle(Style mainStyle) {
			this.mainStyle = mainStyle;
		}

		public StyleContext getSc() {
			return sc;
		}

		public void setSc(StyleContext sc) {
			this.sc = sc;
		}

		public JTextPane getTexto() {
			return texto;
		}

		public void setTexto(JTextPane texto) {
			this.texto = texto;
		}

		public ArrayList<String> getModificados() {
			return modificados;
		}

		public void setModificados(ArrayList<String> modificados) {
			this.modificados = modificados;
		}

		public void addModificados(int inicio, int fim, int r, int g, int b) {

			modificados.add(inicio + "@" + fim + "@" + r + "@" + g + "@" + b);
		}

		/**
		 * Formata o fonte em HTML
		 */
		private void formataHTML() {
			setText(getText().replaceAll("\r", ""));
			Pattern pattern = Pattern.compile("<.*?>", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(getText());
			while (matcher.find()) {
				String tag = matcher.group();
				int iniTag = matcher.start();
				Pattern ptnPriEsp = Pattern.compile("\\s");
				Matcher matPriEsp = ptnPriEsp.matcher(tag);
				setColorRange(iniTag, matcher.end(), Color.BLUE);
				if (matPriEsp.find()) {
					int iniEsp = matPriEsp.start();
					iniTag += iniEsp;
					Pattern att = Pattern.compile("\\b\\w+\\b");
					Matcher matAtt = att.matcher(tag.substring(iniEsp));
					while (matAtt.find()) {
						String achado = matAtt.group();
						setColorRange(iniTag + matAtt.start(), iniTag + matAtt.end(), Color.decode("0xF07700"));
					}
					Pattern ptnAspas = Pattern.compile("\".*?\"");
					Matcher matAspas = ptnAspas.matcher(tag.substring(iniEsp));
					while (matAspas.find()) {
						String achado = matAspas.group();
						setColorRange(iniTag + matAspas.start(), iniTag + matAspas.end(),/*
																							 * new
																							 * Color(255,0,255)
																							 */
						Color.decode("0xFF00FF"));
					}
				}

			}
			Pattern patternCom = Pattern.compile("<!\\-\\-.*?\\-\\->", Pattern.DOTALL);
			matcher = patternCom.matcher(getText());
			while (matcher.find()) {
				int ini = matcher.start();
				int fim = matcher.end();
				setColorRange(ini, fim, Color.decode("0x007700"));
			}

		}

		private void setColorRange(int ini, int fim, Color cor) {

			select(ini, fim);
			// select(startSelect,endSelect);
			sc = StyleContext.getDefaultStyleContext();

			defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
			mainStyle = sc.addStyle("MainStyle", defautStyle);
			StyleConstants.setForeground(mainStyle, cor);
			// StyleConstants.setBackground(mainStyle, cor);

			aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);

			this.setCharacterAttributes(aSet, false);

		}

	}

	private class TArParticipRotulo extends JTextArea {

		ArrayList<TArParticipRotulo> ant;

		ArrayList<String> conteudo = new ArrayList<String>();

		ArrayList<String> posicoes = new ArrayList<String>();

		ArrayList<String> conteudoComPosicoes = new ArrayList<String>();

		public TArParticipRotulo() {
			this.setEditable(false);
		}

		public void addTexto(String texto, int posicaoInicial, int posicaoFinal) {
			// ant.add(this);

			// if(tableLinCod.getRowCount()>0 &&
			// tableLinCod.getSelectedRow()!=-1){
			aplicar.setEnabled(true);// }

			conteudo.add(texto);
			posicoes.add(String.valueOf(posicaoInicial) + "@" + String.valueOf(posicaoFinal));

			super.setText(conteudo.toString().substring(1, conteudo.toString().length() - 1));

		}

		public void apagaTexto() {
			// ant.add(this);
			conteudo = new ArrayList<String>();
			posicoes = new ArrayList<String>();
			conteudoComPosicoes = new ArrayList<String>();
			super.setText("");

		}

		public ArrayList<String> getTextoEPos() {
			conteudoComPosicoes = new ArrayList<String>();
			for (int n = 0; n < conteudo.size(); n++) {
				conteudoComPosicoes.add(conteudo.get(n) + "@" + posicoes.get(n));

			}
			return conteudoComPosicoes;
		}

	}

	private static class Desfazer {
		ArrayList<TArParticipRotulo> ant;

		public static void run() {

		}

	}

	private static class Refazer {

		public static void run() {

		}

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

	private class DefaulTableModelNotEditable extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {

			return false;

		}
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
		btnSalvar.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SALVA_REAVALIA);
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

	private void abreUrl() {
		String url;
		url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://");
		PegarPaginaWEB ppw = new PegarPaginaWEB();
		if (url != null) {
			try {
				String codHtml = ppw.getContent(url);
				TxtBuffer.setContentOriginal(codHtml,"0");
				parentFrame.showPainelFerramentaScriptPArq(codHtml);
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
		
		
	   if (cmd == "SaveAs") {
			salvaAlteracoes.salvarComo();
			// salvarComo();
		} else if (cmd == "AbrirURL") {
			abreUrl();
		} else if (cmd == "Sair") {
			salvaAlteracoes.sair();
		} 
		
		else if (cmd == "Salvar") {
			salvaAlteracoes.salvar();
		} else if (cmd == "Abrir") {
			Abrir();
		} else if (cmd == "AbrirURL") {
			abreUrl();
		} else if (cmd.equals("SelecionarTudo")) {
			textAreaSourceCode.getTextPane().selectAll();
			textAreaSourceCode.getTextPane().requestFocus();
		} else if (cmd == "SaveAs") {
			salvaAlteracoes.salvarComo();
			// salvarComo();
		} else if (cmd == "Sair") {
			salvaAlteracoes.sair();
		} else if (cmd == "Desfazer") {
			// boxCode.undo();
			// boxCode.coloreSource();
			// reavalia(boxCode.getText());
		} else if (cmd == "AumentaFonte") {
			textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd == "DiminuiFonte") {
			textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			textAreaSourceCode.autoContraste();

			int selectedStart = 0;
			int selectedEnd = 0;
			int corretordePosicoesdoLabel = 0;
			int corretordePosicoesdoControle = 0;
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
				textAreaSourceCode.getTextPane().select(selectedStart, selectedEnd);
				arTextPainelCorrecao.setColorForSelectedText(new Color(255, 204, 102), new Color(0, 0, 0));
				textAreaSourceCode.getTextPane().setCharacterAttributes(arTextPainelCorrecao.getASet(), false);

			}

			// arTextPainelCorrecao.formataHTML();
			// tArParticipRotulo.apagaTexto();

			TabelaDescricao tcl = tableLinCod;
			int linha = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 0);
			int coluna = (Integer) dtm.getValueAt(tcl.getSelectedRow(), 1);
			int endTag = 0;
			int posAtual = 0;
			int posFinal = 0;
			codHTML = textAreaSourceCode.getTextPane().getText().replace("\r", "");
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

			textAreaSourceCode.goToLine(linha);
			textAreaSourceCode.getTextPane().select(getPosTagRepInit(), getPosTagRepEnd());

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

	public void altTextFocusLost() {
		System.out.println("altTextFocusLost");
		reload();
	}

	private void reload() {
		salvaAlteracoes.setAlterado();
		posicaoeTag = new ArrayList<FerramentaDescricaoModel>();

		codigo = textAreaSourceCode.getTextPane().getText();
		int caret = textAreaSourceCode.getTextPane().getCaretPosition();
		new FerramentaDescricaoCtrl(codigo);
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

	public void setPanelOriginal(PainelScript ferrLabelPanelNaoEditavel) {

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
		System.out.println("avaliaArq painelObjeto");
		G_File temp = new G_File(path);
		if (temp.getFile() != null) {
			
			TxtBuffer.setContentOriginal(temp.read(),"0");
			parentFrame.showPainelFerramentaScriptPArq(temp.read());
		}
	}
	public void avaliaUrl(String url) {
		PegarPaginaWEB ppw = new PegarPaginaWEB();
		if (url != null) {
			salvaAlteracoes.setNomeDoArquivoEmDisco(null);
			try {
				String codHtml = ppw.getContent(url);
				TxtBuffer.setContentOriginal(codHtml, "0");
				parentFrame.showPainelFerramentaScriptPArq(codHtml);
				
			} catch (HttpException e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public G_TextAreaSourceCode getScrollPaneDescricao() {
		return textAreaSourceCode;
	}
}
// 