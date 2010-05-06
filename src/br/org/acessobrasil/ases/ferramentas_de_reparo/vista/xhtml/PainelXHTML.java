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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.xhtml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import org.apache.commons.httpclient.HttpException;

import br.org.acessobrasil.ases.ferramentas_de_reparo.controle.XHTMLControle;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.tableComponents.DefaulTableModelNotEditable;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.TradPainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.XHTML_Panel;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.SalvaAlteracoes;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
import br.org.acessobrasil.silvinha2.util.onChange.OnChange;
import br.org.acessobrasil.silvinha2.util.onChange.OnChangeListener;
/**
 * Mostra os Erros de HTML
 * @author Fabio Issamu Oshiro, Haroldo Veiga e Renato Tomaz Nati
 *
 */
public class PainelXHTML extends JPanel implements ActionListener, OnChangeListener, MouseListener {
	/**
	 * extends JFrame
	 */
	private static final long serialVersionUID = -638404432108855839L;

	protected G_TextAreaSourceCode textAreaSourceCode;

	private G_File arquivo;

	private JButton btn_salvarComo;

	private JPanel painel;

	protected JButton btn_salvar;

	protected JButton btn_abrir;

	protected JButton btn_cancelar;

	private TabelaErros tabelaDeErros;

	private JScrollPane scrollPaneTabela;

	private FrameSilvinha frameSilvinha;

	private G_File caminhoRecente = new G_File("config/html_recente.txt");

	private JButton reverter;
	
	JPanel btnPanel;

	SalvaAlteracoes salvaAlteracoes;

	private PainelXHTML ferrXHTMLPanelNaoEditavel;

	JMenuItem miBtnSalvar = new JMenuItem(XHTML_Panel.BTN_SALVAR);

	/**
	 * Construtor
	 * 
	 * @param frameSilvinha
	 */
	public PainelXHTML(FrameSilvinha frameSilvinha, String codHtml) {
		if (codHtml == null) {
			codHtml = "";
		}
		this.frameSilvinha = frameSilvinha;

		criaInterfaceVisualEscalavel();
		atribuiActionCommand();
		arquivo = null;
		textAreaSourceCode.setText(codHtml);
		reavalia(codHtml);
	}

	/**
	 * Coloca todos os comands e listeners
	 */
	private void atribuiActionCommand() {
		btn_salvar.setActionCommand("Salvar");
		btn_salvar.addActionListener(this);
		btn_abrir.setActionCommand("Abrir");
		btn_abrir.addActionListener(this);
		btn_cancelar.setActionCommand("Cancelar");
		btn_cancelar.addActionListener(this);
		tabelaDeErros.addMouseListener(this);
		btn_salvarComo.setActionCommand("SaveAs");
		btn_salvarComo.addActionListener(this);
	}

	/**
	 * Cria uma borda com título dentro dos padrões
	 * 
	 * @param titulo
	 * @return
	 */
	private Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}

	private void criaInterfaceVisualEscalavel() {
		painel = new JPanel();
		textAreaSourceCode = new G_TextAreaSourceCode();
		new OnChange(textAreaSourceCode, this);
		// textAreaSourceCode.getTextPane().addKeyListener(this);

		textAreaSourceCode.setTipoHTML();
		textAreaSourceCode.setBorder(criaBorda(XHTML_Panel.COD_FONTE));
		// frameSilvinha.setTitle(XHTMLPanel.TITULO);
		// this.frameSilvinha.setTitle(XHTMLPanel.TITULO_XHTML);

		painel.setLayout(new GridLayout(2, 1));
		setBackground(frameSilvinha.corDefault);

		Container contentPane = this;
		contentPane.setLayout(new GridLayout(1, 1));
		painel.add(textAreaSourceCode);

		JPanel panelBtnTabela = new JPanel();

		panelBtnTabela.setLayout(new BorderLayout());

		/*
		 * Barra de botões
		 */
		btnPanel = new JPanel();
		btnPanel.setLayout(null);
		btn_salvar = new JButton(XHTML_Panel.BTN_SALVAR);
		btn_salvar.setBounds(10, 0, 140, 25);
		btnPanel.add(btn_salvar);

		btn_abrir = new JButton(XHTML_Panel.BTN_ABRIR);
		btn_abrir.setBounds(165, 0, 150, 25);
		btnPanel.add(btn_abrir);

		btn_salvarComo = new JButton(XHTML_Panel.BTN_SALVAR_COMO);
		btn_salvarComo.setBounds(325, 0, 150, 25);
		btnPanel.add(btn_salvarComo);

		btnPanel.setPreferredSize(new Dimension(760, 30));

		btn_cancelar = new JButton(GERAL.TELA_ANTERIOR);
		btn_cancelar.setBounds(485, 0, 150, 25);
		btnPanel.add(btn_cancelar);

		tabelaDeErros = new TabelaErros();
		scrollPaneTabela = new JScrollPane();
		scrollPaneTabela.setViewportView(tabelaDeErros);
		panelBtnTabela.add(btnPanel, BorderLayout.NORTH);
		panelBtnTabela.add(scrollPaneTabela, BorderLayout.CENTER);
		scrollPaneTabela.setBorder(criaBorda(XHTML_Panel.LISTA_ERROS));
		painel.add(panelBtnTabela);

		btnPanel.setBackground(frameSilvinha.corDefault);
	
			reverter = new JButton("Reverter");
			reverter.setText(TradPainelRelatorio.REVERTER);
			reverter.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);
					TxtBuffer.setContent(TxtBuffer.getContentOriginal());
					frameSilvinha.showPainelFerramentaXHTML();
					setVisible(true);
				}
				
			});
			//reverter.setActionCommand("Reverter");
			reverter.setToolTipText(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleDescription(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleName(TradPainelRelatorio.DICA_REVERTER);
			reverter.setBounds(650, 0, 150, 25);
			btnPanel.add(reverter);
	
		
		panelBtnTabela.setBackground(frameSilvinha.corDefault);
		painel.setBackground(frameSilvinha.corDefault);
		contentPane.setBackground(frameSilvinha.corDefault);
		scrollPaneTabela.setBackground(frameSilvinha.corDefault);
		textAreaSourceCode.setBackground(frameSilvinha.corDefault);
		miBtnSalvar.setEnabled(false);
		btn_salvar.setEnabled(false);

		contentPane.add(painel);
		// pack();
		this.setVisible(true);
	}

	private void reavalia(String codigo) {
		try {
			tabelaDeErros.initComponents();
			// boxCode.coloreSource();
			XHTMLControle xhtmlValidator = new XHTMLControle();
			// xhtmlValidator.avalia(arquivo.getFile());

			xhtmlValidator.avalia(codigo);
			if (xhtmlValidator.length() == 0) {
				tabelaDeErros.addLinha(0, 0, XHTML_Panel.DOC_SEM_ERROS);
			}

			for (int i = 0; i < xhtmlValidator.length(); i++) {
				tabelaDeErros.addLinha(xhtmlValidator.getLinha(i), xhtmlValidator.getColuna(i), xhtmlValidator.getMensagem(i));
				textAreaSourceCode.marcaErro(xhtmlValidator.getLinha(i));
			}
			// domValidator
		} catch (Exception e) {

		}
	}

	/**
	 * Avalia o arquivo
	 */
	private void avalia() {
		String codHtml = arquivo.read();
		textAreaSourceCode.setText(codHtml);
		reavalia(codHtml);
	}

	public void avalia(String codHtml) {

		textAreaSourceCode.setText(codHtml);
		reavalia(codHtml);

	}

	public void actionPerformed(ActionEvent e) {

		String cmd = e.getActionCommand();
		if (cmd == "Salvar") {
			salvaAlteracoes.salvar();
			textAreaSourceCode.coloreSource();
			reavalia(textAreaSourceCode.getText());
		} else if (cmd == "AbrirURL") {

			String url;
			url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://");
			avaliaUrl(url);

			// JOptionPane.showMessageDialog(null, "Seu nome é " + nome);

		} else if (cmd == "Abrir") {

			String a[] = { ".html", ".htm" };
			G_File temp = new G_File(caminhoRecente.read(), a);
			if (temp.getFile() != null) {
				avaliaArq(temp);
			}
		} else if (cmd == "SaveAs") {
			// salvarComo();
			//salvaAlteracoes.setUltimoPath(caminhoRecente.read());
			salvaAlteracoes.salvarComo();
			// avalia();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd == "Sair") {
			System.exit(0);
		}else if(cmd=="SelecionarTudo"){
			textAreaSourceCode.getTextPane().selectAll();
			if(ferrXHTMLPanelNaoEditavel!=null){
				ferrXHTMLPanelNaoEditavel.textAreaSourceCode.getTextPane().selectAll();
			}
		} else if (cmd == "Desfazer") {
			textAreaSourceCode.undo();
			// boxCode.coloreSource();
			// reavalia(boxCode.getText());
		} else if (cmd == "AumentaFonte") {
			textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			textAreaSourceCode.autoContraste();
			reavalia(textAreaSourceCode.getText());
		} else if (cmd == "Cancelar") {
			salvaAlteracoes.cancelar();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			int row = this.tabelaDeErros.getSelectedRow();
			if (row == -1)
				return;
			int linhaToGo = Integer.parseInt(tabelaDeErros.getValueAt(row, 0).toString());
			if (linhaToGo == 0)
				return;
			textAreaSourceCode.goToLine(linhaToGo);
			textAreaSourceCode.selectLine(linhaToGo);
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

	private class TabelaErros extends JTable {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 2584151976777935682L;

		DefaulTableModelNotEditable dtm;

		private TabelaErros() {
			initComponents();
		}

		public void initComponents() {
			dtm = new DefaulTableModelNotEditable();
			dtm.setColumnIdentifiers(new String[] { GERAL.LINHA, GERAL.COLUNA, GERAL.ERRO });

			setModel(dtm);
			{
				TableColumnModel cm = getColumnModel();
				cm.getColumn(0).setMaxWidth(50);
				cm.getColumn(0).setMaxWidth(50);
				cm.getColumn(1).setMaxWidth(680);
			}

		}

		public void addLinha(int linha, int coluna, String mensagem) {
			dtm.addRow(new Object[] { linha, coluna, mensagem });
		}
	}

	public void setPanelOriginal(PainelXHTML ferrXHTMLPanelNaoEditavel) {
		this.ferrXHTMLPanelNaoEditavel = ferrXHTMLPanelNaoEditavel;
		ferrXHTMLPanelNaoEditavel.textAreaSourceCode.getTextPane().setEditable(false);
		ferrXHTMLPanelNaoEditavel.btnPanel.setVisible(false);

	}

	/**
	 * Chamado pelo TabbedXHTML
	 * @param url
	 */
	public void avaliaUrl(String url) {
		PegarPaginaWEB ppw = new PegarPaginaWEB();
		if (url != null) {
			arquivo = null;
			salvaAlteracoes.setNomeDoArquivoEmDisco(null);
			//System.out.println("arquivo = null");
			try {
				String codHtml = ppw.getContent(url);
				textAreaSourceCode.setText(codHtml);
				TxtBuffer.setContentOriginal(codHtml, "0");
				reavalia(codHtml);
				this.ferrXHTMLPanelNaoEditavel.avalia(codHtml);
			} catch (HttpException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void avaliaArq(String path) {
		G_File temp = new G_File(path);
		if (temp.exists()) {
			avaliaArq(temp);
		} else {
			JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
		}
	}

	private void avaliaArq(G_File temp) {
		arquivo = temp;
		caminhoRecente.write(arquivo.getFile().getAbsolutePath());
		frameSilvinha.setTitle(arquivo.getFile().getName() + " - " + XHTML_Panel.TITULO_XHTML);
		frameSilvinha.setUrlTextField(arquivo.getFile().getAbsolutePath());
		TxtBuffer.setContentOriginal(temp.read(), "0");
		this.ferrXHTMLPanelNaoEditavel.avalia(temp.read() + "");
		avalia();
		salvaAlteracoes.setNomeDoArquivoEmDisco(arquivo.getFile().getAbsolutePath());
	}

	public void altTextFocusLost() {
		TxtBuffer.setContent(textAreaSourceCode.getText());
	}
}
