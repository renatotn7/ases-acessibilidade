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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.doctype;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import org.apache.commons.httpclient.HttpException;

import br.org.acessobrasil.ases.ferramentas_de_reparo.controle.DocType;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.tableComponents.DefaulTableModelNotEditable;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Imagens;
import br.org.acessobrasil.silvinha2.mli.Ferramenta_Scripts;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.LabelPanel;
import br.org.acessobrasil.silvinha2.mli.TradFerramentaDocType;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.TradPainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.TradSimuladorNavegacao;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.SalvaAlteracoes;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
import br.org.acessobrasil.silvinha2.util.onChange.OnChange;
import br.org.acessobrasil.silvinha2.util.onChange.OnChangeListener;
/**
 * UI para corrigir doctype
 */
public class ferramentaDoctype extends JPanel implements ActionListener, OnChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String codigo;

	ferramentaDoctype painelOriginal;

	private JPanel pnRegra;

	JMenuItem btnSalvar;

	private JLabel lbRegras1;

	private JPanel pnSetaDescricao;

	String strConteudoalt;

	String DoctypeText;

	private JScrollPane scrollPanetabLinCod;

	private JPanel pnBotoes;

	SalvaAlteracoes salvaAlteracoes;

	private JButton buscar;

	private G_TextAreaSourceCode textAreaSourceCode;

	private TabelaDescricao tabelaDoctypes;

	boolean original = false;

	private JButton aplicar;

	public JButton reverter;

	private JButton salvar;

	private JButton abrir;

	private JButton cancelar;

	private JPanel pnListaErros;

	FrameSilvinha parentFrame;

	DefaulTableModelNotEditable dtm;

	JPanel pnSalvarCancelar = new JPanel();

	G_File caminhoRecente = new G_File("config/html_recente.txt");

	public ferramentaDoctype(FrameSilvinha parentFrame) {
		TradFerramentaDocType.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		initComponents(TxtBuffer.getContent());
	}

	public ferramentaDoctype(String conteudo, FrameSilvinha parentFrame) {
		TradFerramentaDocType.carregaTexto(TokenLang.LANG);
		this.parentFrame = parentFrame;
		initComponents(conteudo);
	}

	/**
	 * Método chamado pelos construtores
	 * 
	 * @param conteudo
	 */
	private void initComponents(String conteudo) {
		textAreaSourceCode = new G_TextAreaSourceCode();
		tabelaDoctypes = new TabelaDescricao();
		aplicar = new JButton();

		abrir = new JButton();
		cancelar = new JButton();
		JPanel regraFonteBtn = new JPanel();
		regraFonteBtn.setLayout(new BorderLayout());

		textAreaSourceCode.setTipoHTML();
		new OnChange(textAreaSourceCode, this);
		tabelaDoctypes = new TabelaDescricao();

		salvar = new JButton();
		abrir = new JButton();
		cancelar = new JButton();
		strConteudoalt = new String();

		pnRegra = new JPanel();
		lbRegras1 = new JLabel();
		pnSetaDescricao = new JPanel();

		pnListaErros = new JPanel();
		scrollPanetabLinCod = new JScrollPane();

		pnBotoes = new JPanel();
		
		buscar = new JButton();
		salvar.setEnabled(false);
		salvaAlteracoes = TxtBuffer.getInstanciaSalvaAlteracoes(textAreaSourceCode.getTextPane(), salvar, new JMenuItem(), parentFrame);
		textAreaSourceCode.getTextPane().setEditable(true);
		aplicar.setEnabled(false);
		setBackground(CoresDefault.getCorPaineis());
		Container contentPane = this;// ??
		contentPane.setLayout(new GridLayout(2, 1));

		// ======== pnRegra ========
		{
			// pnRegra.setBorder(criaBorda(""));
			pnRegra.setBorder(criaBorda(LabelPanel.TITULO_REGRA));
			pnRegra.setLayout(new GridLayout(2, 1));
			pnRegra.add(lbRegras1);
			lbRegras1.setText(TradFerramentaDocType.UTILIZAR_ELEMENTOS_CABECALHO);
			lbRegras1.setHorizontalAlignment(SwingConstants.CENTER);
			pnRegra.add(lbRegras1);
			pnRegra.setPreferredSize(new Dimension(700, 60));
		}

		// ======== pnDescricao ========

		aplicar.setText(GERAL.APLICAR);
		aplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarActionPerformed(e);
			}
		});

		aplicar.setToolTipText(TradFerramentaDocType.DICA_APLICA_DTD);
		aplicar.getAccessibleContext().setAccessibleDescription(TradFerramentaDocType.DICA_APLICA_DTD);
		aplicar.getAccessibleContext().setAccessibleName(TradFerramentaDocType.DICA_APLICA_DTD);
		aplicar.setBounds(10, 0, 150, 25);

		salvar.setText(GERAL.BTN_SALVAR);
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarActionPerformed(e);
			}
		});
		salvar.setToolTipText(GERAL.DICA_SALVAR);
		salvar.getAccessibleContext().setAccessibleDescription(GERAL.DICA_SALVAR);
		salvar.getAccessibleContext().setAccessibleName(GERAL.DICA_SALVAR);

		salvar.setBounds(10, 0, 150, 25);

		
		
		abrir.setText(GERAL.BTN_ABRIR);
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirActionPerformed(e);
			}
		});

		abrir.setToolTipText(Ferramenta_Scripts.DICA_ABRIR);
		abrir.getAccessibleContext().setAccessibleDescription(Ferramenta_Scripts.DICA_ABRIR_HTML);
		abrir.getAccessibleContext().setAccessibleName(Ferramenta_Scripts.DICA_ABRIR_HTML);
		abrir.setBounds(165, 0, 150, 25);

		
		cancelar.setText(Ferramenta_Imagens.TELA_ANTERIOR);
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelarActionPerformed(e);
			}
		});

		cancelar.setToolTipText(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleDescription(Ferramenta_Imagens.DICA_TELA_ANTERIOR);
		cancelar.getAccessibleContext().setAccessibleName(Ferramenta_Imagens.DICA_BTN_CANCELAR);
		cancelar.setBounds(320, 0, 150, 25);

		// ======== pnParticRotulo ========

		// pnSetaDescricao.setBorder(criaBorda(""));
		GridBagConstraints cons = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		cons.fill = GridBagConstraints.BOTH;
		cons.weighty = 1;
		cons.weightx = 0.80;

		pnSetaDescricao.setLayout(layout);
		cons.anchor = GridBagConstraints.SOUTHEAST;
		cons.insets = new Insets(0, 0, 0, 10);
		// ======== spParticRotulo ========

		cons.weightx = 0.20;
		pnSetaDescricao.setPreferredSize(new Dimension(400, 60));

		// ======== pnListaErros ========
		{

			pnListaErros.setBorder(criaBorda(TradFerramentaDocType.ESCOLHA_DTD_CORRETO));
			pnListaErros.setLayout(new BorderLayout());
			// ======== scrollPanetabLinCod ========
			{
				scrollPanetabLinCod.setViewportView(tabelaDoctypes);
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
					aplicarActionPerformed(e);
				}
			});

			buscar.setToolTipText(TradSimuladorNavegacao.BUSCAR_TEXTO);
			buscar.getAccessibleContext().setAccessibleDescription(TradSimuladorNavegacao.BUSCAR_TEXTO);
			buscar.getAccessibleContext().setAccessibleName(TradSimuladorNavegacao.BUSCAR_TEXTO);
			buscar.setBounds(10, 5, 150, 25);
			// pnBotoes.add(buscar);
		}

		/*
		 * Colocar os controles
		 */
		pnRegra.setBackground(CoresDefault.getCorPaineis());
		regraFonteBtn.add(pnRegra, BorderLayout.NORTH);
		textAreaSourceCode.setBorder(criaBorda(TradFerramentaDocType.CODIGO_FONTE));
		textAreaSourceCode.setBackground(CoresDefault.getCorPaineis());

		regraFonteBtn.add(textAreaSourceCode, BorderLayout.CENTER);
		pnBotoes.setPreferredSize(new Dimension(600, 35));
		pnBotoes.setBackground(CoresDefault.getCorPaineis());

		regraFonteBtn.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(regraFonteBtn);

		JPanel textoErrosBtn = new JPanel();
		textoErrosBtn.setLayout(new BorderLayout());
		pnSetaDescricao.setBackground(CoresDefault.getCorPaineis());
		pnSetaDescricao.add(pnBotoes, cons);
		textoErrosBtn.add(pnSetaDescricao, BorderLayout.NORTH);

		textoErrosBtn.add(pnListaErros, BorderLayout.CENTER);
		pnSalvarCancelar.setLayout(null);
		pnSalvarCancelar.setPreferredSize(new Dimension(600, 35));
		pnSalvarCancelar.add(abrir);
		pnSalvarCancelar.add(salvar);
		pnSalvarCancelar.add(cancelar);

		if (!original) {
			reverter = new JButton("Reverter");
			reverter.setText(TradPainelRelatorio.REVERTER);
			reverter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					TxtBuffer.setContent(TxtBuffer.getContentOriginal());
					parentFrame.showPainelFerramentaDoctypePArq(TxtBuffer.getContentOriginal());
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

		contentPane.setBackground(CoresDefault.getCorPaineis());
		contentPane.add(textoErrosBtn);
		controla(conteudo);
		this.setVisible(true);

	}

	public void controla(String conteudo) {
		textAreaSourceCode.setText(conteudo);
		textAreaSourceCode.getTextPane().setCaretPosition(0);
		if (conteudo.length() > 0 && tabelaDoctypes.getRowCount() == 0) {
			tabelaDoctypes.addLinha("XHTML 1.1", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
			tabelaDoctypes.addLinha("XHTML Basic 1.1", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML Basic 1.1//EN\" \"http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd\">");
			tabelaDoctypes.addLinha("XHTML 1.0(strict)", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
			tabelaDoctypes.addLinha("XHTML 1.0(transitional)",
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			tabelaDoctypes.addLinha("XHTML 1.0(frameset)", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">");
			tabelaDoctypes.addLinha("XHTML Basic 1.0", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML Basic 1.0//EN\" \"http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd\">");
			tabelaDoctypes.addLinha("HTML 4.01(strict)", "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
			tabelaDoctypes.addLinha("HTML 4.01(transitional)", "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
			tabelaDoctypes.addLinha("HTML 4.01(FrameSet)", "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">");
		}
	}

	private void abrirActionPerformed(ActionEvent e) {
		G_File caminhoRecente = new G_File("config/html_recente.txt");
		String a[] = { ".html", ".htm" };
		G_File temp = new G_File(caminhoRecente.read(), a);
		if (temp.getFile() != null) {
			avaliaArq(temp);
		}
	}

	public void avaliaUrl(String url) {
		PegarPaginaWEB ppw = new PegarPaginaWEB();
		if (url != null) {
			try {
				String codHtml = ppw.getContent(url);
				TxtBuffer.setContentOriginal(codHtml, "0");
				controla(codHtml);
				this.painelOriginal.controla(codHtml);
			} catch (HttpException e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	public void avaliaArq(G_File temp){
		String codHtml = temp.read();
		TxtBuffer.setContentOriginal(codHtml, "0");
		controla(codHtml);
		if(this.painelOriginal!=null){
			this.painelOriginal.controla(codHtml);
		}
		parentFrame.setUrlTextField(temp.getFile().getAbsolutePath());
	}
	public void avaliaArq(String path) {
		G_File temp = new G_File(path);
		if (temp.exists()) {
			avaliaArq(temp);
		} else {
			JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
		}
	}

	private void cancelarActionPerformed(ActionEvent e) {

		salvaAlteracoes.cancelar();

	}

	private void aplicarActionPerformed(ActionEvent e) {
		aplicar();
	}

	private void aplicar() {
		salvaAlteracoes.setAlterado();
		String textoDoPainel = textAreaSourceCode.getTextPane().getText();
		String textoComDoctype = new String();
		int posAbre = textoDoPainel.toLowerCase().indexOf("<!doctype");
		if (posAbre != -1) {
			int posFecha = textoDoPainel.toLowerCase().indexOf(">", posAbre);
			textoComDoctype = textoDoPainel.substring(0, posAbre) + DoctypeText + textoDoPainel.substring(posFecha + 1, textoDoPainel.length());
		} else {
			textoComDoctype = DoctypeText + "\n" + textoDoPainel;
		}
		textAreaSourceCode.setText(textoComDoctype);
		if (posAbre == -1) {
			posAbre = 0;
		}
		textAreaSourceCode.getTextPane().setCaretPosition(posAbre);
		codigo = textAreaSourceCode.getTextPane().getText();

		TxtBuffer.setContent(codigo);
		new DocType(this, codigo, true);
	}

	private void salvarActionPerformed(ActionEvent e) {

		salvaAlteracoes.salvar();

	}

	private Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}

	private class TabelaDescricao extends JTable implements MouseListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private TabelaDescricao() {
			initComponents();

		}

		private void initComponents() {

			dtm = new DefaulTableModelNotEditable();
			dtm.setColumnIdentifiers(new String[] { TradFerramentaDocType.VERSAO_HTML, TradFerramentaDocType.DTD_INSERIDO });
			this.addMouseListener(this);
			setModel(dtm);
			{
				TableColumnModel cm = getColumnModel();
				cm.getColumn(0).setMinWidth(200);
				cm.getColumn(0).setMaxWidth(200);

			}

		}

		public void clearTab() {
			for (int i = 0; i < dtm.getRowCount(); i++)
				dtm.removeRow(i);
		}

		public void addLinha(String link, String codigo) {
			dtm.addRow(new Object[] { link, codigo });

		}

		public void delAtualLinha() {
			dtm.removeRow(this.getSelectedRow());
		}

		public void selectTag() {

		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && !original) {
				parentFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				System.out.println("Habilita os botoes de salvar");
				salvaAlteracoes.setAlterado();
				TabelaDescricao tcl = ((TabelaDescricao) e.getComponent());
				DoctypeText = (String) dtm.getValueAt(tcl.getSelectedRow(), 1);
				aplicar();
				parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public class PossuiDoctype extends Thread {
		public PossuiDoctype() {
		}

		public void run() {
			JOptionPane.showMessageDialog(null, TradFerramentaDocType.PAGINA_COM_DTD);
		}
	}

	private void openUrl() {
		String url;
		url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://");
		avaliaUrl(url);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == "Salvar") {
			salvaAlteracoes.salvar();
		} else if (cmd == "Abrir") {
			abrirActionPerformed(null);
		} else if (cmd.equals("SelecionarTudo")) {
			textAreaSourceCode.getTextPane().selectAll();
			textAreaSourceCode.getTextPane().requestFocus();
		} else if (cmd == "AbrirURL") {
			openUrl();
		} else if (cmd == "SaveAs") {
			salvaAlteracoes.salvarComo();
		} else if (cmd == "Sair") {
			salvaAlteracoes.sair();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd == "Desfazer") {
			textAreaSourceCode.undo();
		} else if (cmd == "AumentaFonte") {
			textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			textAreaSourceCode.autoContraste();
		}

	}

	public void altTextFocusLost() {
		reload();
	}

	public void reload() {
		codigo = textAreaSourceCode.getTextPane().getText();
		int caret = textAreaSourceCode.getTextPane().getCaretPosition();
		textAreaSourceCode.coloreSource();
		textAreaSourceCode.getTextPane().setCaretPosition(caret);
		TxtBuffer.setContent(codigo);
	}

	public void setPanelOriginal(ferramentaDoctype ferrLabelPanelNaoEditavel) {
		ferrLabelPanelNaoEditavel.textAreaSourceCode.getTextPane().setEditable(false);
		ferrLabelPanelNaoEditavel.original = true;
		ferrLabelPanelNaoEditavel.pnBotoes.setVisible(false);
		ferrLabelPanelNaoEditavel.pnSalvarCancelar.setVisible(false);
		this.painelOriginal = ferrLabelPanelNaoEditavel;
	}

	public G_TextAreaSourceCode getArea_de_texto() {
		return textAreaSourceCode;
	}

}
