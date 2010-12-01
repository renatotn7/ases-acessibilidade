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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
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


import br.org.acessobrasil.silvinha2.mli.TradSimuladorNavegacao;

/**
 * Área de Texto para trabalhar código fonte
 * 
 * @author Fabio Issamu Oshiro
 */
public abstract class G_TextAreaSourceCode_v2 extends JScrollPane implements KeyListener, UndoableEditListener {

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
	private JTextPane2 textPane;

	/**
	 * Guarda o estilo do erro
	 */
	private MutableAttributeSet estiloDoErro;

	MutableAttributeSet estiloLineCount;

	private PanelLineNumber painel;

	private int tipoFormatacao;

	private int fontSize = 12;
	
	private String textoUltimaBusca;
	
	private int posTextoUltimaBusca;
	
	private Color CorDoTextoUltimaBusca;
	
	/**
	 * <code>true</code> guarda as ações de undo <br>
	 * <code>false</code> não guarda ações de undo <br>
	 * Obs: false não quer dizer que zera o(s) undo(s)
	 */
	private boolean undoOn = true;
	/**
	 * Se é para deixar a marca da última seleção
	 */
	private boolean deixaSelecionadoMarcado = true;

	/**
	 * Guarda o código para dar undo
	 */
	protected UndoManager undo = new UndoManager();

	private int autoContrasteTipo = 0;

	public G_TextAreaSourceCode_v2(boolean comLineCount) {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textoUltimaBusca="";
		posTextoUltimaBusca=0;
		CorDoTextoUltimaBusca=new Color(0,0,0);
		if (comLineCount) {
			comLineCount();
		} else {
			semLineCount();
		}
		this.verticalScrollBar.setUnitIncrement(18);
	}

	public G_TextAreaSourceCode_v2() {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textoUltimaBusca="";
		posTextoUltimaBusca=0;
		CorDoTextoUltimaBusca=new Color(0,0,0);
		comLineCount();
		// semLineCount();
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
		 * busca texto no painel
		 */
	
	public void buscar() {
		int posTextoBuscaAtual = 0;
		Color corNovoAchado = new Color(255, 100, 0);
		String textoABuscar = JOptionPane.showInputDialog("Buscar");
		if (textoABuscar.equals(textoUltimaBusca)) {
			posTextoBuscaAtual = getTextPane().getText().indexOf(textoABuscar, posTextoUltimaBusca + 1);
		} else {
			posTextoBuscaAtual = getTextPane().getText().indexOf(textoABuscar);
		}
		if (posTextoBuscaAtual == -1) {
			JOptionPane.showMessageDialog(null,TradSimuladorNavegacao.NAO_HA_OCORRENCIAS);
			
			return;
		}
		// textAreaSourceCode.getTextPane().setRequestFocusEnabled(true);
		setColorRange(posTextoUltimaBusca, posTextoUltimaBusca + textoUltimaBusca.length(), CorDoTextoUltimaBusca);
		// scrollPaneDescricao.getTextPane().setText(scrollPaneDescricao.getTextPane().getText());
		// textAreaSourceCode.getTextPane().setCaretPosition(pos);

		getTextPane().select(posTextoBuscaAtual, posTextoBuscaAtual + textoABuscar.length());
		requestFocus();
		// corAchadoAntigo =
		// textAreaSourceCode.getTextPane().getSelectedTextColor();
		setColorRange(posTextoBuscaAtual, posTextoBuscaAtual + textoABuscar.length(), corNovoAchado);
		// corAchadoAntigo=
		posTextoUltimaBusca = posTextoBuscaAtual;
		textoUltimaBusca = textoABuscar;
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
		textPane = new JTextPane2();
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

		// painel = new JPanel(new BorderLayout());
		painel = new PanelLineNumber(this);
		textPane = painel.getTextPane();
		textPane.setFont(new Font("Courier New", 0, fontSize));
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(painel, BorderLayout.WEST);
		panel.add(textPane, BorderLayout.CENTER);
		// painel.setBounds(0, 0, 40, 400);
		// textPane.setBounds(40, 0, 400, 800);
		// textPaneLineNumber.setBounds(0, 0, 800, 800);
		// this.add(panel);
		this.setViewportView(panel);
		// textPane.setBounds(20, 0, 600, 300);
		this.textPane.addKeyListener(this);
		/*
		 * Define o estilo do erro
		 */
		loadStyleError();
	}

	/**
	 * Atribui um texto a este componente
	 * 
	 * @param texto
	 */
	public void setText(String texto) {
		sourceCode = texto.replaceAll("\\r", "");
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
		this.coloreTexto();
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
		// StyleConstants.setFontFamily(standard, "Courier New");
		StyleConstants.setUnderline(standard, false);
		setCharacterAttributes(0, sourceCode.length(), standard, true);
		if (autoContrasteTipo != 0) {
			if (autoContrasteTipo == 1) {
				setColorRange(0, sourceCode.length(), Color.BLACK);
				this.textPane.setBackground(Color.WHITE);
				this.textPane.setForeground(Color.BLACK);
				this.textPane.setCaretColor(Color.BLACK);
			} else if (autoContrasteTipo == 2) {
				setColorRange(0, sourceCode.length(), Color.WHITE);
				this.textPane.setBackground(Color.BLACK);
				this.textPane.setForeground(Color.WHITE);
				this.textPane.setCaretColor(Color.WHITE);
			}
		} else {
			setColorRange(0, sourceCode.length(), Color.BLACK);
			this.textPane.setBackground(Color.WHITE);
			this.textPane.setForeground(Color.BLACK);
			this.textPane.setCaretColor(Color.BLACK);
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
		/*
		 * Pattern patTab = Pattern.compile("\t\t*"); matcher =
		 * patTab.matcher(sourceCode); while(matcher.find()){
		 * MutableAttributeSet estilo = new SimpleAttributeSet();
		 * StyleConstants.setFontSize(estilo, this.fontSize-1 );
		 * setCharacterAttributes(matcher.start(),matcher.end() -
		 * matcher.start(), estilo, false); }
		 */
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
	 * Aplica a formatação de erro
	 * 
	 * @param linha
	 *            linha do erro
	 * @param coluna
	 *            coluna do erro
	 * @param length
	 *            tamanho a string errada
	 * 
	 */
	public void marcaErro(int linha, int coluna, int length) {
		int contaLinha = 1, ini = -1;
		String sourceCode = this.textPane.getText().replaceAll("\r","");
		while (contaLinha < linha) {
			ini = sourceCode.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return;
			}
		}
		ini += coluna;
		if (ini + 1 >= sourceCode.length())
			return;
		// int fim = sourceCode.indexOf('\n', ini + 1);
		setCharacterAttributes(ini, length, this.estiloDoErro, false);
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
		undoOn=false;
		if (fim > 0) {
			if (styledDocument == null) {
				styledDocument = textPane.getStyledDocument();
			}
			styledDocument.setCharacterAttributes(ini, fim, estilo, sobrescreve);
		}
		undoOn=true;
	}

	/**
	 * Aplica a formatação de erro
	 * 
	 * @param linha
	 *            linha do erro
	 */
	public void marcaErro(int linha) {
		int contaLinha = 1, ini = -1;
		String sourceCode = this.textPane.getText().replaceAll("\r","");
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
		undoOn=false;
		MutableAttributeSet estilo = new SimpleAttributeSet();
		StyleConstants.setForeground(estilo, cor);
		setCharacterAttributes(ini, fim - ini, estilo, false);
		undoOn=true;
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
		int contaLinha = 1, ini = -1;
		String sourceCode = this.textPane.getText().replaceAll("\r","");
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

	public int getAutoContrasteTipo() {
		return autoContrasteTipo;
	}

	public boolean isDeixaSelecionadoMarcado() {
		return deixaSelecionadoMarcado;
	}

	public void setDeixaSelecionadoMarcado(boolean deixaSelecionadoMarcado) {
		this.deixaSelecionadoMarcado = deixaSelecionadoMarcado;
	}

	/**
	 * Retorna o tamanho da fonte utilizada
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * Usuário quer voltar um passo
	 */
	public void undo() {
		try{
			undo.undo();
		}catch(Exception e){
			//don't worry be happy!
		}
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		if(undoOn){
			undo.addEdit(e.getEdit());
		}
	}
	class JTextPane2 extends JTextPane{
		private static final long serialVersionUID = 1L;

		public void selectAll(){
			super.requestFocus();
			super.selectAll();
		}
	}
	class PanelLineNumber extends JPanel {
		/**
		 * Serial
		 */
		private static final long serialVersionUID = 1L;

		private JTextPane2 textPane;

		private G_TextAreaSourceCode_v2 scrollPane;

		public JTextPane2 getTextPane() {
			return textPane;
		}

		public void setTextPane(JTextPane2 textPane) {
			this.textPane = textPane;
		}

		public PanelLineNumber(G_TextAreaSourceCode_v2 scrollPane) {
			super();
			this.textPane = new TextPane(this);
			this.scrollPane = scrollPane;
			this.setBackground(new Color(255, 255, 222));
			this.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		}

		/**
		 * Imprime a linha relativa a posicao y
		 * @param g
		 * @param y
		 * @throws BadLocationException
		 */
		private int printLine(Graphics g,int y,int ultimaImpressa) throws BadLocationException{
			JViewport viewPort = this.scrollPane.getViewport();
			Point point = new Point(viewPort.getViewPosition().x,y);
			int start = this.textPane.viewToModel(point);
			Document doc = this.textPane.getDocument();
			int numeroDaLinha = doc.getDefaultRootElement().getElementIndex(start) + 1;
			if(ultimaImpressa==numeroDaLinha) return numeroDaLinha;
			int starting_y = this.textPane.modelToView(start).y;
			int fontHeight = g.getFontMetrics(this.textPane.getFont()).getHeight();
			int fontDesc = g.getFontMetrics(this.textPane.getFont()).getDescent();
			g.setFont(new Font("Courier New", 0, 12));
			g.setColor(new Color(0, 0, 0));
			g.drawString(normalize(numeroDaLinha), 0, starting_y+fontHeight-fontDesc);
			return numeroDaLinha;
		}
		
		public void paint(Graphics g) {
			try {
				super.paint(g);
				int iniY = this.scrollPane.getViewport().getViewPosition().y;
				int fimY = this.scrollPane.getViewport().getViewPosition().y+this.scrollPane.getViewport().getHeight();
				int ultimaImpressa = 0;
				for(int i=iniY;i<fimY;i++){
					ultimaImpressa=printLine(g,i,ultimaImpressa);
				}
			} catch (BadLocationException e1) {
				//e1.printStackTrace();
			} catch(Exception e){
				//e.printStackTrace();
			}
		}

		private String normalize(int i) {
			if (i < 10)
				return "000" + i;
			if (i < 100)
				return "00" + i;
			if (i < 1000)
				return "0" + i;
			return i + "";
		}
	}

	class TextPane extends JTextPane2 {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Component comp;

		public TextPane(Component comp) {
			this.comp = comp;
		}

		public void paint(Graphics g) {
			super.paint(g);
			comp.repaint();
		}

		/**
		 * Evita o wordWrap
		 */
		public void setSize(Dimension d) {
			if (d.width < getParent().getSize().width)
				d.width = getParent().getSize().width;

			super.setSize(d);
		}

		/**
		 * Evita o wordWrap
		 */
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}
		
	}
}


