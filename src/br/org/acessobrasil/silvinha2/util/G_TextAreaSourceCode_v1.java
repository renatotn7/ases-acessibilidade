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

package br.org.acessobrasil.silvinha2.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

/**
 * Área de Texto para trabalhar código fonte
 * 
 * @author Fabio Issamu Oshiro
 */
public abstract class G_TextAreaSourceCode_v1 extends JScrollPane implements KeyListener, UndoableEditListener, ActionListener {

	/**
	 * Versão Serial
	 */
	private static final long serialVersionUID = 4918882714386500003L;

	/**
	 * Código fonte
	 */
	private String sourceCode;

	/**
	 * Estilo do documento
	 */
	private StyledDocument styledDocument;

	/**
	 * Text pane que exibe o codigo fonte
	 */
	private JTextPane textPane;

	// private JTextArea textPane = new JTextArea();

	/**
	 * Text pane que exibe o número da linha
	 */
	private JTextPane textPaneLineNumber = new JTextPane();

	/**
	 * Guarda o estilo do erro
	 */
	private MutableAttributeSet estiloDoErro;

	MutableAttributeSet estiloLineCount;

	private JPanel painel;

	private int tipoFormatacao;

	private int n_linhas = 1;

	private int fontSize = 12;

	/**
	 * Se é para deixar a marca da última seleção
	 */
	private boolean deixaSelecionadoMarcado = true;

	/**
	 * Guarda o código para dar undo
	 */
	protected UndoManager undoManager = new UndoManager();

	boolean catchUndo = true;

	private int autoContrasteTipo = 0;

	public G_TextAreaSourceCode_v1() {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// comLineCount();
		semLineCount();
		//Coloca o listener para o undo
		textPane.getDocument().addUndoableEditListener(this);
		
		this.verticalScrollBar.setUnitIncrement(18);
	}

	/**
	 * Aumenta a fonte
	 */
	public void aumentaFontSize() {
		SimpleAttributeSet fonteMaior = new SimpleAttributeSet();
		fontSize++;
		StyleConstants.setFontSize(fonteMaior, fontSize);
		this.setCharacterAttributes(0, this.textPane.getText().length(), fonteMaior, false);
	}

	/**
	 * Auto contraste
	 * 
	 * @param umOuDois
	 *            zero para nenhum
	 */
	public void autoContraste(int umOuDois) {
		autoContrasteTipo = umOuDois;
		if (autoContrasteTipo == 0) {
			loadStyleError();
		} else {
			loadStyleErrorAutoContraste();
		}
		this.coloreSource();
	}

	public void autoContraste() {
		autoContrasteTipo = (++autoContrasteTipo) % 3;
		autoContraste(autoContrasteTipo);
	}

	/**
	 * Diminui a fonte
	 */
	public void diminuiFontSize() {
		SimpleAttributeSet fonteMaior = new SimpleAttributeSet();
		fontSize--;
		StyleConstants.setFontSize(fonteMaior, fontSize);
		this.setCharacterAttributes(0, this.textPane.getText().length(), fonteMaior, false);
	}

	/**
	 * Define o estilo do erro
	 */
	private void loadStyleError() {
		estiloDoErro = new SimpleAttributeSet();
		StyleConstants.setUnderline(estiloDoErro, true);
		StyleConstants.setForeground(estiloDoErro, Color.RED);
	}

	/**
	 * Define o estilo do erro quando for auto contraste
	 */
	private void loadStyleErrorAutoContraste() {
		estiloDoErro = new SimpleAttributeSet();
		StyleConstants.setUnderline(estiloDoErro, true);
		StyleConstants.setBold(estiloDoErro, true);
	}

	/**
	 * Faz o lay out sem o contador de linha
	 * 
	 */
	private void semLineCount() {
		textPane = new JTextPane();
		//textPane.setFont(new Font("Courier New", 0, 15));
		this.add(textPane);
		this.setViewportView(textPane);
		// textPane.setBounds(20, 0, 600, 300);
		this.textPane.addKeyListener(this);
		/*
		 * Define o estilo do erro
		 */
		loadStyleError();
	}

	/**
	 * Faz o lay out com o contador de linhas
	 */
	private void comLineCount() {
		textPane = new JTextPane() {
			/**
			 * Serial
			 */
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				super.paint(g);
				G_TextAreaSourceCode_v1.this.repaint();
			}

			public void setSize(Dimension d) {
				if (d.width < getParent().getSize().width)
					d.width = getParent().getSize().width;

				super.setSize(d);
			}

			public boolean getScrollableTracksViewportWidth() {
				return false;
			}
		};
		textPane.setBorder(BorderFactory.createEmptyBorder(0, 43, 0, 0));
		this.add(textPane);
		this.setViewportView(textPane);
		// textPane.setBounds(20, 0, 600, 300);
		this.textPane.addKeyListener(this);
		/*
		 * Define o estilo do erro
		 */
		loadStyleError();
	}

	public void paint2(Graphics g) {
		/*
		 * Coloca o scroll bar em outro local
		 */
		// We need to properly convert the points to match the viewport
		// Read docs for viewport
		Point viewPosition = this.getViewport().getViewPosition();
		Rectangle rec = this.getViewportBorderBounds();// VisibleRect();
		// super.horizontalScrollBar.setBounds(41,super.horizontalScrollBar.getY(),super.getWidth()-42,super.horizontalScrollBar.getHeight());
		super.paint(g);

		// viewPosition.x = rec.x;
		// viewPosition.y = rec.y;
		int start = textPane.viewToModel(viewPosition);
		// starting pos in document
		int end = textPane.viewToModel(new Point(viewPosition.x + textPane.getWidth(), viewPosition.y + textPane.getHeight()));
		// end pos in doc

		// translate offsets to lines
		Document doc = textPane.getDocument();
		int startline = doc.getDefaultRootElement().getElementIndex(start) + 1;
		int endline = doc.getDefaultRootElement().getElementIndex(end) + 1;

		int fontHeight = g.getFontMetrics(new Font("Courier New", 0, fontSize)).getHeight();
		// int fontHeight = fontSize+2;
		// System.out.println("startLine = " + startline);
		int fontDesc = g.getFontMetrics(textPane.getFont()).getDescent();
		// int fontDesc = 0;
		int starting_y = -1;

		try {
			starting_y = textPane.modelToView(start).y - viewPosition.y + fontHeight - fontDesc;
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		final int X = 3 + rec.x;
		int line = startline, y = starting_y + rec.y;
		/*
		 * Escolhe a cor do line number
		 */
		g.setFont(new Font("Courier New", 0, 13));
		g.setColor(new Color(255, 255, 240));
		g.fillRect(rec.x, rec.y, 40, rec.height);
		g.setColor(new Color(0, 0, 0));
		g.drawLine(rec.x + 40, rec.y, rec.x + 40, rec.y + rec.height);
		int mw = rec.y + rec.height;
		for (; line <= endline && line < 10 && y < mw; y += fontHeight, line++) {
			g.drawString("000" + Integer.toString(line), X, y);
		}
		for (; line <= endline && line < 100 && y < mw; y += fontHeight, line++) {
			g.drawString("00" + Integer.toString(line), X, y);
		}
		for (; line <= endline && line < 1000 && y < mw; y += fontHeight, line++) {
			g.drawString("0" + Integer.toString(line), X, y);
		}
		for (; line <= endline && y < mw; y += fontHeight, line++) {
			g.drawString(Integer.toString(line), X, y);
		}
	}

	/**
	 * Atribui um texto a este componente
	 * 
	 * @param texto
	 */
	public void setText(String texto) {
		//System.out.println(texto);
		sourceCode = texto.replaceAll("\\r", "");
		
		//sourceCode = texto;
		this.textPane.setText(sourceCode);
		coloreSource();
	}

	/**
	 * Retorna o código
	 */
	public String getText() {
		sourceCode = this.textPane.getText().toString();
		return sourceCode;
	}

	/**
	 * Colore o fonte
	 */
	public void coloreSource() {
		// Colore em um processo separado
		/*
		 * (new Thread(){ public void run(){ coloreTexto(); } }).start(); /
		 */
		catchUndo=false;
		coloreTexto();
		catchUndo=true;
		// */
	}

	/**
	 * SubProcesso para evitar a espera de outras aplicações
	 */
	private void coloreTexto() {
		sourceCode = this.textPane.getText().replaceAll("\\r", "");
		if (styledDocument == null) {
			styledDocument = textPane.getStyledDocument();
		}
		MutableAttributeSet standard = new SimpleAttributeSet();
		StyleConstants.setFontFamily(standard, "Courier New");
		StyleConstants.setUnderline(standard, false);
		setCharacterAttributes(0, sourceCode.length(), standard, true);
		if (autoContrasteTipo != 0) {
			if (autoContrasteTipo == 1) {
				setColorRange(0, sourceCode.length(), Color.BLACK);
				this.textPane.setBackground(Color.WHITE);
			} else if (autoContrasteTipo == 2) {
				setColorRange(0, sourceCode.length(), Color.WHITE);
				this.textPane.setBackground(Color.BLACK);
			}
		} else {
			setColorRange(0, sourceCode.length(), Color.BLACK);
			this.textPane.setBackground(Color.WHITE);
			if (tipoFormatacao == 1) {
				formataCSS();
			} else if (tipoFormatacao == 2) {
				formataHTML();
			}
		}
	}

	/**
	 * Formata o fonte em HTML
	 */
	private void formataHTML() {
		// System.out.println("formataHTML() - ini");
		Pattern patTag = Pattern.compile("<.*?>", Pattern.DOTALL);
		Pattern patAtt = Pattern.compile("\\b\\w+\\b");
		Pattern ptnPriEsp = Pattern.compile("\\s");
		Pattern ptnAspas = Pattern.compile("\".*?\"");
		Matcher matcher = patTag.matcher(sourceCode);
		final Color corAtt = Color.decode("0xF07700");
		final Color corAttVal = Color.decode("0xFF00FF");
		final Color corComentario = Color.decode("0x007700");
		while (matcher.find()) {
			String tag = matcher.group();
			int iniTag = matcher.start();

			Matcher matPriEsp = ptnPriEsp.matcher(tag);
			setColorRange(iniTag, matcher.end(), Color.BLUE);
			if (matPriEsp.find()) {
				int iniEsp = matPriEsp.start();
				iniTag += iniEsp;

				Matcher matAtt = patAtt.matcher(tag.substring(iniEsp));
				while (matAtt.find()) {
					setColorRange(iniTag + matAtt.start(), iniTag + matAtt.end(), corAtt);
				}

				Matcher matAspas = ptnAspas.matcher(tag.substring(iniEsp));
				while (matAspas.find()) {
					setColorRange(iniTag + matAspas.start(), iniTag + matAspas.end(), corAttVal);
				}
			}
			// System.out.print(".");
		}
		Pattern patternCom = Pattern.compile("<!\\-\\-.*?\\-\\->", Pattern.DOTALL);
		matcher = patternCom.matcher(sourceCode);
		while (matcher.find()) {
			int ini = matcher.start();
			int fim = matcher.end();
			setColorRange(ini, fim, corComentario);
		}
		// System.out.println("formataHTML() - fim");
	}

	/**
	 * Formata o fonte em CSS
	 */
	private void formataCSS() {
		// Pattern pattern =
		// Pattern.compile("\\{.*?\\b(.*?):.*?\\}",Pattern.DOTALL);
		Pattern pattern = Pattern.compile("\\{.*?\\}", Pattern.DOTALL);
		Pattern ptBlue = Pattern.compile("([\\w-]*?):");
		Matcher matcher = pattern.matcher(sourceCode);
		while (matcher.find()) {
			int iniBase = matcher.start();
			Matcher matBlue = ptBlue.matcher(matcher.group());
			while (matBlue.find()) {
				int fim = iniBase + matBlue.end(1) + 1;
				int ini = iniBase + matBlue.start(1);
				setColorRange(ini, fim, Color.BLUE);
			}
		}
		Pattern patternCom = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
		matcher = patternCom.matcher(sourceCode);
		while (matcher.find()) {
			int ini = matcher.start();
			int fim = matcher.end();
			setColorRange(ini, fim, Color.decode("0x007700"));
		}
	}

	/**
	 * Qual o tipo de source?
	 */
	public void setTipoCSS() {
		tipoFormatacao = 1;
	}

	/**
	 * Qual o tipo de source?
	 */
	public void setTipoHTML() {
		tipoFormatacao = 2;
	}

	/**
	 * Aplica a formatação de erro
	 * 
	 * @param ini
	 *            início do index
	 * @param fim
	 *            fim do index
	 */
	public void marcaErro(int ini, int fim) {
		setCharacterAttributes(ini, fim - ini, estiloDoErro, false);
	}

	/**
	 * Evita erros
	 * 
	 * @param ini
	 * @param fim
	 * @param estilo
	 * @param sobrescreve
	 */
	private void setCharacterAttributes(int ini, int fim, MutableAttributeSet estilo, boolean sobrescreve) {
		if (fim > 0) {
			try{
				styledDocument.setCharacterAttributes(ini, fim, estilo, sobrescreve);
			}catch(Exception e){
				//Ignorar
			}
		}
	}
	/**
	 * Aplica a formatação de erro
	 * 
	 * @param linha
	 *            linha do erro
	 * @param coluna coluna do erro
	 * @param length tamanho a string errada 
	 * 	
	 */
	public void marcaErro(int linha,int coluna, int length) {
		int contaLinha = 1, ini = 0;
		while (contaLinha < linha) {
			ini = sourceCode.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return;
			}
		}
		ini+=coluna;
		if (ini + 1 >= sourceCode.length())
			return;
		//int fim = sourceCode.indexOf('\n', ini + 1);
		setCharacterAttributes(ini, length, this.estiloDoErro, false);
	}
	/**
	 * Aplica a formatação de erro
	 * 
	 * @param linha
	 *            linha do erro
	 */
	public void marcaErro(int linha) {
		int contaLinha = 1, ini = 0;
		while (contaLinha < linha) {
			ini = sourceCode.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return;
			}
		}
		if (ini + 1 >= sourceCode.length())
			return;
		char c = sourceCode.charAt(++ini);
		while (c == '\t' || c == ' ' || c == ' ') {
			c = sourceCode.charAt(++ini);
		}
		int fim = sourceCode.indexOf('\n', ini + 1);
		setCharacterAttributes(ini, fim - ini, this.estiloDoErro, false);
	}

	/**
	 * Aplica uma cor em uma determinada área do texto
	 * 
	 * @param ini
	 * @param fim
	 * @param cor
	 */
	public void setColorRange(int ini, int fim, Color cor) {
		MutableAttributeSet estilo = new SimpleAttributeSet();
		StyleConstants.setForeground(estilo, cor);
		setCharacterAttributes(ini, fim - ini, estilo, false);
	}

	/**
	 * @return the textPane
	 */
	public JTextPane getTextPane() {
		return textPane;
	}

	/**
	 * Seleciona a linha
	 */
	public void selectLine(int linha) {
		int contaLinha = 1, ini = 0;
		while (contaLinha < linha) {
			ini = sourceCode.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return;
			}
		}
		char c = sourceCode.charAt(++ini);
		while (c == '\t' || c == ' ' || c == ' ') {
			c = sourceCode.charAt(++ini);
		}
		int fim = sourceCode.indexOf('\n', ini + 1);
		this.textPane.select(ini, fim);
		// System.out.print("selectLine()\n");
		if (deixaSelecionadoMarcado || true) {
			MutableAttributeSet backGround1 = new SimpleAttributeSet();
			MutableAttributeSet backGround = new SimpleAttributeSet();
			if (autoContrasteTipo != 2) {
				StyleConstants.setBackground(backGround1, Color.WHITE);
				// setCharacterAttributes(0, sourceCode.length(), backGround1,
				// false);
				setCharacterAttributes(lastSelectedIni, lastSelectedFim, backGround1, false);
				StyleConstants.setBackground(backGround, Color.decode("0xEEEEEE"));
			} else {
				StyleConstants.setBackground(backGround1, Color.BLACK);
				// setCharacterAttributes(0, sourceCode.length(), backGround1,
				// false);
				setCharacterAttributes(lastSelectedIni, lastSelectedFim, backGround1, false);
				StyleConstants.setBackground(backGround, Color.decode("0x444444"));
			}
			lastSelectedIni = ini;
			lastSelectedFim = fim - ini;
			setCharacterAttributes(ini, fim - ini, backGround, false);
		}
		this.textPane.requestFocus();
	}

	private int lastSelectedIni = 0;

	private int lastSelectedFim = 0;

	/**
	 * Cria uma borda com título dentro dos padrões
	 * 
	 * @param titulo
	 */
	public void setTitledBorder(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		// return bordaFinal;
		this.setBorder(bordaFinal);
	}

	/**
	 * Posiciona o cursor na linha
	 */
	public void goToLine(int linha) {
		int contaLinha = 1, ini = 0;
		while (contaLinha < linha) {
			ini = sourceCode.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return;
			}
		}
		char c = sourceCode.charAt(++ini);
		while (c == '\t' || c == ' ' || c == ' ') {
			c = sourceCode.charAt(++ini);
		}
		this.textPane.setCaretPosition(ini);
	}

	

	/**
	 * Volta um texto antes
	 */
	public void undo() {
		try{
			undoManager.undo();
		}catch(Exception e){
			//ignorar
		}
	}

	/**
	 * Sincronizar as linhas
	 */
	private void sincLine() {
		if (estiloLineCount == null)
			return;
		String linhas = "", lin[] = this.textPane.getText().split("\n");
		int i;
		if (n_linhas == lin.length)
			return;
		for (i = 1; i < 18 || i <= lin.length + 1; i++) {
			linhas += i + "\n";
		}
		linhas += i;
		n_linhas = lin.length;
		// linhas="";
		textPaneLineNumber.setText(linhas);
		textPaneLineNumber.getStyledDocument().setCharacterAttributes(0, linhas.length(), estiloLineCount, true);
	}

	public void keyPressed(KeyEvent arg0) {
		
	}

	public void keyReleased(KeyEvent arg0) {
		/*
		 * TLE = Time Limit Exceeded
		 * 
		 */
		// coloreSource();

	}

	public void keyTyped(KeyEvent arg0) {

	}

	public void setColorForSelectedText(Color background, Color foreground) {

		MutableAttributeSet backGround1 = new SimpleAttributeSet();
		MutableAttributeSet backGround = new SimpleAttributeSet();

		if (autoContrasteTipo != 2) {
			StyleConstants.setBackground(backGround1, background);
			StyleConstants.setForeground(backGround1, foreground);
			setCharacterAttributes(0, sourceCode.length(), backGround1, false);
			StyleConstants.setBackground(backGround, background);
			StyleConstants.setForeground(backGround, foreground);
		} else {
			StyleConstants.setBackground(backGround1, Color.BLACK);
			StyleConstants.setForeground(backGround1, Color.WHITE);
			setCharacterAttributes(0, sourceCode.length(), backGround1, false);
			StyleConstants.setBackground(backGround, Color.BLACK);
			StyleConstants.setForeground(backGround, Color.WHITE);

		}

	}

	public int getAutoContrasteTipo() {
		return autoContrasteTipo;
	}

	public boolean isDeixaSelecionadoMarcado() {
		return deixaSelecionadoMarcado;
	}

	public void setDeixaSelecionadoMarcado(boolean deixaSelecionadoMarcado) {
		this.deixaSelecionadoMarcado = deixaSelecionadoMarcado;
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		if(catchUndo){
			undoManager.addEdit(e.getEdit());
		}
	}

	public void addUndoMenuItem(JMenuItem btnDesfazer) {
		btnDesfazer.setActionCommand("UNDO");
		btnDesfazer.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("UNDO")){
			undo();
		}
	}

}

