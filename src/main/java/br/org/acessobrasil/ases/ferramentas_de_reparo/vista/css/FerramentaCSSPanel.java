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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.css;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import org.apache.commons.httpclient.HttpException;
import org.w3c.css.css.StyleSheet;
import org.w3c.css.css.StyleSheetParser;
import org.w3c.css.parser.CssError;
import org.w3c.css.parser.Errors;
import org.w3c.css.util.ApplContext;
import org.w3c.css.util.Warning;
import org.w3c.css.util.Warnings;

import br.org.acessobrasil.ases.ferramentas_de_reparo.controle.ControleCss;
import br.org.acessobrasil.ases.ferramentas_de_reparo.controle.ControleCssListener;
import br.org.acessobrasil.ases.ferramentas_de_reparo.modelo.AvaliacaoCSS;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.tableComponents.DefaulTableModelNotEditable;
import br.org.acessobrasil.silvinha2.mli.CSSPanel;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.TradPainelRelatorio;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TableReadOnly;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;

/**
 * UI para avaliar o CSS
 * 
 * @author Fabio Issamu Oshiro
 */
public class FerramentaCSSPanel extends JPanel implements ActionListener, MouseListener, ControleCssListener {
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 2594256402733228097L;

	private G_File caminhoRecente = new G_File("config/css_recente.txt");

	G_TextAreaSourceCode textAreaSourceCode;

	private JPanel btnPanel;

	private G_File arquivo;

	private JPanel painel;

	private JButton btn_salvar;

	private JButton btn_abrir;

	private JButton reverter;

	private JButton btn_salvarComo;

	private boolean original = false;

	private TabelaErros tabelaDeErros;

	/**
	 * Mostra os arquivos
	 */
	private G_TableReadOnly tabelaArq;

	private JScrollPane scrollPaneTabela;

	FrameSilvinha frameSilvinha;

	private FerramentaCSSPanel ferrCSSPanelNaoEditavel;

	private static String TITULO;

	/**
	 * Guarda os erros do CSS
	 */
	private Errors erros;

	/**
	 * Guarda os avisos do CSS
	 */
	private Warnings avisos;

	/**
	 * Construtor padrão
	 * 
	 * @param silvinha
	 *            FrameSilvinha
	 */
	public FerramentaCSSPanel(FrameSilvinha silvinha) {

		TITULO = CSSPanel.TITULO;
		frameSilvinha = silvinha;

		criaInterfaceVisualEscalavel();

		carregaCodigo();
		avalia();
		atribuiActionCommand();
		// setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		CSSPanel.carregaTexto(TokenLang.LANG);
	}

	/**
	 * Coloca todos os comands e listeners
	 */
	private void atribuiActionCommand() {
		btn_salvar.setActionCommand("Salvar");
		btn_salvar.addActionListener(this);
		btn_abrir.setActionCommand("Abrir");
		btn_abrir.addActionListener(this);
		tabelaDeErros.addMouseListener(this);
		btn_salvarComo.setActionCommand("SaveAs");
		btn_salvarComo.addActionListener(this);
	}

	private void carregaCodigo() {
		if (arquivo == null)
			return;
		textAreaSourceCode.setTipoCSS();
		textAreaSourceCode.setText(arquivo.read());
	}

	/**
	 * Cria uma borda com título dentro dos padrões
	 * 
	 * @param titulo
	 * @return Border
	 */
	private Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}

	private void criaInterfaceVisualEscalavel() {
		painel = new JPanel();
		painel.setLayout(new GridLayout(2, 1));

		textAreaSourceCode = new G_TextAreaSourceCode();

		textAreaSourceCode.setBorder(criaBorda(CSSPanel.COD_FONTE));

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
		btn_salvar = new JButton(CSSPanel.BTN_SALVAR);
		btn_salvar.setBounds(10, 0, 100, 25);
		btnPanel.add(btn_salvar);

		btn_abrir = new JButton(CSSPanel.BTN_ABRIR);
		btn_abrir.setBounds(115, 0, 100, 25);
		btnPanel.add(btn_abrir);

		btn_salvarComo = new JButton(CSSPanel.BTN_SALVAR_COMO);
		btn_salvarComo.setBounds(220, 0, 120, 25);
		btnPanel.add(btn_salvarComo);
		btnPanel.setPreferredSize(new Dimension(760, 30));

		tabelaDeErros = new TabelaErros(erros);

		String cols[] = { "Url", "Erros", "Avisos" };
		int sizes[] = { 0, 60, 60 };
		tabelaArq = new G_TableReadOnly(cols, sizes);

		scrollPaneTabela = new JScrollPane();
		scrollPaneTabela.setBorder(criaBorda(CSSPanel.LISTA_ERROS));
		scrollPaneTabela.setViewportView(tabelaDeErros);
		panelBtnTabela.add(btnPanel, BorderLayout.NORTH);
		panelBtnTabela.add(scrollPaneTabela, BorderLayout.CENTER);

		painel.add(panelBtnTabela);
		contentPane.add(painel);
		if (!original) {
			reverter = new JButton(TradPainelRelatorio.REVERTER);
			reverter.setText(TradPainelRelatorio.REVERTER);
			reverter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					reavalia(ferrCSSPanelNaoEditavel.textAreaSourceCode.getText());
				}
			});
			// reverter.setActionCommand("Reverter");
			reverter.setToolTipText(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleDescription(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleName(TradPainelRelatorio.DICA_REVERTER);
			reverter.setBounds(345, 0, 150, 25);
			btnPanel.add(reverter);
		}
		btnPanel.setBackground(frameSilvinha.corDefault);
		panelBtnTabela.setBackground(frameSilvinha.corDefault);
		painel.setBackground(frameSilvinha.corDefault);
		contentPane.setBackground(frameSilvinha.corDefault);
		scrollPaneTabela.setBackground(frameSilvinha.corDefault);
		textAreaSourceCode.setBackground(frameSilvinha.corDefault);

		this.setVisible(true);
	}

	/**
	 * Reavalia um código CSS
	 * 
	 * @param codFonte
	 */
	private void reavalia(String codFonte) {
		/*
		 * Zerar os erros da tabela
		 */
		tabelaDeErros.initComponents(null);
		StyleSheetParser parser;
		String usermedium = "all";
		// String text = new G_File("C:\\temp\\teste.css").read();

		String fileName = "TextArea";
		fileName = "file://localhost/" + fileName;

		InputStream is = new ByteArrayInputStream(codFonte.getBytes());

		// needed by the CSS parser
		ApplContext ac = null;

		/*
		 * Configura o idioma
		 */
		if (TokenLang.LANG.equals("pt")) {
			// os PL que nos desculpem
			ac = new ApplContext("pl-PL");
		} else {
			ac = new ApplContext(TokenLang.LANG);
		}
		// All
		ac.setWarningLevel(2);
		// ac.setProfile(profile);
		ac.setCssVersion("css3");
		// ac.setCssVersion("css21");
		parser = new StyleSheetParser();
		StyleSheet css = null;
		try {
			parser.parseStyleElement(ac, is, null, usermedium, new URL(fileName), 0);
			css = parser.getStyleSheet();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		erros = css.getErrors();
		avisos = css.getWarnings();
		for (int i = 0; i < erros.getErrorCount(); i++) {
			CssError er = erros.getErrorAt(i);
			String msg = er.getException().getMessage();
			if (msg != null) {
				textAreaSourceCode.marcaErro(er.getLine());
				tabelaDeErros.addLinha(er.getLine(), CSSPanel.ERRO_DOIS_PONTOS + msg);
			}
		}
		for (int i = 0; i < avisos.getWarningCount(); i++) {
			Warning er = avisos.getWarningAt(i);
			textAreaSourceCode.marcaErro(er.getLine());
			tabelaDeErros.addLinha(er.getLine(), CSSPanel.ATENCAO_DOIS_PONTOS + er.getWarningMessage());
		}
	}

	/**
	 * Avalia o CSS referenciado na instancia arquivo de G_File
	 */
	private void avalia() {
		if (arquivo == null)
			return;
		String text = arquivo.read();
		reavalia(text);
	}

	private void abrirUrl() {
		String url;
		url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://");

		avaliaUrl(url);
	}

	/**
	 * Retorna o caminho do CSS
	 * 
	 * @param codHtml
	 * @return String com o path ou null
	 */
	private String getCssUrl(String codHtml) {
		Pattern pat = Pattern.compile("<link\\s.*?>", Pattern.DOTALL);
		Matcher mat = pat.matcher(codHtml);
		RegrasHardCodedEmag regra = new RegrasHardCodedEmag();
		while (mat.find()) {
			String tag = mat.group();
			String tipo = regra.getAtributo(tag, "type");
			String href = regra.getAtributo(tag, "href");
			if (tipo.equals("text/css") || href.toLowerCase().endsWith(".css")) {
				return href;
			}
		}
		return null;
	}

	/**
	 * Implementando
	 * @param codHtml
	 * @return lista de links de css
	 */
	@SuppressWarnings("unused")
	private List<String> getCssUrlList(String codHtml) {
		ArrayList<String> urls = new ArrayList<String>();
		Pattern pat = Pattern.compile("<link\\s.*?>", Pattern.DOTALL);
		Matcher mat = pat.matcher(codHtml);
		RegrasHardCodedEmag regra = new RegrasHardCodedEmag();
		while (mat.find()) {
			String tag = mat.group();
			String tipo = regra.getAtributo(tag, "type");
			String href = regra.getAtributo(tag, "href");
			if (tipo.equals("text/css") || href.toLowerCase().endsWith(".css")) {
				urls.add(href);
			}
		}
		return urls;
	}

	/**
	 * Salva o arquivo atual
	 */
	private void salvar() {
		if (arquivo == null) {
			salvarComo();
		} else {
			arquivo.write(textAreaSourceCode.getText());
			textAreaSourceCode.coloreSource();
			avalia();
		}
	}

	/**
	 * Faz as operações de salvar o arquivo
	 * 
	 */
	private void salvarComo() {
		if (arquivo == null) {
			arquivo = new G_File("");
		}
		String cod = "";
		cod = textAreaSourceCode.getText();
		String extensoes[] = { ".css" };
		boolean salvo = arquivo.openDialogSaveAs(cod, extensoes, this);
		if (salvo) {
			if (arquivo != null && arquivo.getFile() != null) {
				if (arquivo.getFile().getName() != null) {
					// atualiza o endereço
					frameSilvinha.setTitle(arquivo.getFile().getName() + " - " + TITULO);
					frameSilvinha.setUrlTextField(arquivo.getFile().getAbsolutePath());
				}
				textAreaSourceCode.coloreSource();
				avalia();
			}
		}
	}

	/**
	 * Faz as operações de abrir um arquivo
	 * 
	 */
	private void abrirArquivo() {

		G_File temp = new G_File(caminhoRecente.read(), ".css");
		if (temp.getFile() != null) {
			frameSilvinha.setUrlTextField(temp.getFile().getAbsolutePath());
			avaliaArq(temp);
		}

	}

	private void avaliaArq(G_File temp) {
		arquivo = temp;
		caminhoRecente.write(arquivo.getFile().getAbsolutePath());
		frameSilvinha.setTitle(arquivo.getFile().getName() + " - " + TITULO);
		frameSilvinha.setUrlTextField(arquivo.getFile().getAbsolutePath());
		carregaCodigo();
		avalia();
		this.ferrCSSPanelNaoEditavel.arquivo = temp;
		this.ferrCSSPanelNaoEditavel.carregaCodigo();
		this.ferrCSSPanelNaoEditavel.avalia();
	}

	/**
	 * Abre uma ajuda de acordo com o texto passado
	 * 
	 * @param texto
	 */
	private void openHelp(String texto) {
		//TODO implementar nas próximas versões
	}

	/**
	 * Captura o evento de mouse na tabela de erros
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			int row = this.tabelaDeErros.getSelectedRow();
			int col = this.tabelaDeErros.getSelectedColumn();
			if (row == -1) {
				return;
			}
			if (col == 2) {
				/*
				 * Abrir um help
				 */
				String texto = tabelaDeErros.getValueAt(row, 1).toString();
				openHelp(texto);
			} else {
				int linhaToGo = Integer.parseInt(tabelaDeErros.getValueAt(row, 0).toString());
				textAreaSourceCode.goToLine(linhaToGo);
				textAreaSourceCode.selectLine(linhaToGo);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == "Salvar") {
			salvar();
		} else if (cmd.equals("AbrirURL")) {
			abrirUrl();
		} else if (cmd == "Abrir" || cmd.equals(MenuSilvinha.MNU_ABRIR)) {
			abrirArquivo();
		} else if (cmd == "SaveAs") {
			salvarComo();
		} else if (cmd == "Creditos") {
			new Creditos();
		} else if (cmd == "Sair") {
			System.exit(0);
		} else if (cmd == "SelecionarTudo") {
			textAreaSourceCode.getTextPane().selectAll();
			ferrCSSPanelNaoEditavel.textAreaSourceCode.getTextPane().selectAll();
		} else if (cmd == "Desfazer") {
			textAreaSourceCode.undo();
			reavalia(textAreaSourceCode.getText());
		} else if (cmd == "AumentaFonte") {
			textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			textAreaSourceCode.autoContraste();
			reavalia(textAreaSourceCode.getText());
		}

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}

	private class TabelaErros extends JTable {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 2584151976777935682L;

		DefaulTableModelNotEditable dtm;

		private TabelaErros() {
			initComponents(null);
		}

		private TabelaErros(Errors erros) {
			initComponents(erros);
		}

		public void initComponents(Errors erros) {
			dtm = new DefaulTableModelNotEditable();
			TableColumnModel cm;
			// dtm.setColumnIdentifiers(new String[] { "Linha", "Erro", "Saiba
			// Mais" });
			dtm.setColumnIdentifiers(new String[] { CSSPanel.DESC_LINHA, CSSPanel.DESC_ERRO });
			setModel(dtm);
			{
				cm = getColumnModel();
				cm.getColumn(0).setMinWidth(50);
				cm.getColumn(0).setMaxWidth(50);
				// cm.getColumn(1).setMinWidth(730);
				// cm.getColumn(2).setMinWidth(50);
				// cm.getColumn(2).setMaxWidth(150);

			}

			// this.getColumn("Saiba Mais").setCellRenderer(centro);
			if (erros == null)
				return;

			for (int i = 0; i < erros.getErrorCount(); i++) {
				CssError er = erros.getErrorAt(i);
				addLinha(er.getLine(), er.getException().getMessage());
			}
		}

		public void addLinha(int linha, String codigo) {
			// dtm.addRow(new Object[] { linha, codigo, "Saiba Mais" });
			dtm.addRow(new Object[] { linha, codigo });
		}
	}

	/**
	 * Implementando 
	 * @param listaUrl
	 * @param url
	 */
	@SuppressWarnings("unused")
	private void avaliaTodosCss(List<String> listaUrl, String url) {
		tabelaArq.clearTable();
		/*
		 * teste
		 */
		boolean doTest = false;
		if (!doTest)
			return;
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(500, 300));
		frame.add(tabelaArq);
		frame.setVisible(true);
		System.out.println("Frame Visivel");
		for (String cssUrl : listaUrl) {
			ControleCss adapterCss = new ControleCss(this);
			adapterCss.avalia(cssUrl, url);
		}
		System.out.println("avaliaTodosCss finalizado");
	}

	/**
	 * resultados de avaliaTodosCss
	 */
	public void avaliacaoCssRealizada(AvaliacaoCSS avaliacaoCss) {
		tabelaArq.addLinha(new Object[] { avaliacaoCss.getUrl(), avaliacaoCss.getErros().getErrorCount(), avaliacaoCss.getAvisos().getWarningCount() });
	}

	/**
	 * Avalia a url passada como parâmetro
	 * 
	 * @param url
	 */
	public void avaliaUrl(String url) {
		PegarPaginaWEB ppw = new PegarPaginaWEB();

		if (url != null) {
			// arquivo = new G_File("temp/temp.css");
			arquivo = null;
			try {
				String codHtml = ppw.getContent(url);
				String cssURL = null;
				if (url.toLowerCase().endsWith(".css")) {
					cssURL = url;
				} else {
					cssURL = getCssUrl(codHtml);
					// avaliar todos os css
					// avaliaTodosCss(getCssUrlList(codHtml),url);
					// return;
				}
				if (cssURL != null) {
					if (cssURL.indexOf("http://") == -1) {
						while (cssURL.startsWith("/")) {
							cssURL = cssURL.substring(1);
						}
						if (!url.endsWith("/")) {
							cssURL = url + "/" + cssURL;
						} else {
							cssURL = url + cssURL;
						}
					}
					String codCSS = ppw.getCssContent(cssURL);
					textAreaSourceCode.setTipoCSS();
					textAreaSourceCode.setText(codCSS);
					this.ferrCSSPanelNaoEditavel.textAreaSourceCode.setTipoCSS();
					this.ferrCSSPanelNaoEditavel.textAreaSourceCode.setText(codCSS);
					this.ferrCSSPanelNaoEditavel.reavalia(codCSS);
					reavalia(codCSS);
				} else {
					/*
					 * não achou links de css
					 */
					JOptionPane.showMessageDialog(this, GERAL.CSS_N_ENCONTRADO, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				}
			} catch (HttpException e1) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * Define o painel não editável que contém o código original
	 * 
	 * @param ferrCSSPanelNaoEditavel
	 */
	public void setPanelOriginal(FerramentaCSSPanel ferrCSSPanelNaoEditavel) {
		ferrCSSPanelNaoEditavel.btnPanel.setVisible(false);
		this.ferrCSSPanelNaoEditavel = ferrCSSPanelNaoEditavel;
		this.ferrCSSPanelNaoEditavel.original = true;
	}

	/**
	 * Avalia o arquivo passado como parâmetro
	 * 
	 * @param url
	 */
	public void avaliaArq(String url) {
		G_File temp = new G_File(url);
		if (!temp.exists()) {
			JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			return;
		}
		String cssURL = null;
		if (url.toLowerCase().endsWith(".css")) {
			avaliaArq(temp);
			return;
		}
		cssURL = getCssUrl(temp.read());
		// avaliar todos os css
		// avaliaTodosCss(getCssUrlList(codHtml),url);
		// return;
		if (cssURL == null) {
			/*
			 * não achou links de css
			 */
			JOptionPane.showMessageDialog(this, GERAL.CSS_N_ENCONTRADO, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
		}
		if (cssURL.indexOf("http://") == -1) {
			try {
				cssURL = url.substring(0, url.lastIndexOf("\\")) + "\\" + cssURL.replace("/", "\\");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		temp = new G_File(cssURL);
		avaliaArq(temp);
	}
}
