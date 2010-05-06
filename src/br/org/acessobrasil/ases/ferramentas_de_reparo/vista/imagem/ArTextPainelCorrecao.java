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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
/**
 * Mostra o texto para o usuário inserir a correção 
 *
 */
class ArTextPainelCorrecao extends JTextPane implements MouseListener {
	/**
	 * 
	 */
	private final PanelDescricaoImagens imagens;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultStyledDocument doc = null;

	private ArrayList<String> modificados = new ArrayList<String>();

	private Style defautStyle = null;

	private Style mainStyle = null;

	private JTextPane texto;

	private AttributeSet aSet;

	private StyleContext sc;

	ArTextPainelCorrecao(PanelDescricaoImagens imagens) {
		this.imagens = imagens;
		initComponents();

	}

	private void initComponents() {
		this.imagens.textAreaSourceCode.getTextPane().setEditable(true);
		testaRTF();
	}

	/**
	 * Troca o texto da área seleciona pelo parametro
	 * 
	 * @param texto
	 */
	public void setTextoParaSelecionado(String texto) {

		int intTemp = 0;
		int startSelect = this.imagens.textAreaSourceCode.getTextPane().getSelectionStart();
		int endSelect = this.imagens.textAreaSourceCode.getTextPane().getSelectionEnd();
		String codHTML = this.imagens.textAreaSourceCode.getTextPane().getText();

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
		this.imagens.textAreaSourceCode.getTextPane().setText(codHTML.substring(0, startSelect) + texto + codHTML.substring(endSelect - 0, codHTML.length()));

		sc = StyleContext.getDefaultStyleContext();

		defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

		mainStyle = sc.addStyle("MainStyle", defautStyle);
		StyleConstants.setForeground(mainStyle, new Color(0, 0, 255));
		aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);
		this.imagens.textAreaSourceCode.getTextPane().select(startSelect, endSelect - 1);
		this.imagens.textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);

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

		this.imagens.textAreaSourceCode.getTextPane().setText(this.imagens.codigo);
		this.imagens.textAreaSourceCode.getTextPane().setText(this.imagens.textAreaSourceCode.getTextPane().getText().replaceAll("\r", ""));

		this.imagens.textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);

		this.imagens.textAreaSourceCode.coloreSource();
		this.imagens.textAreaSourceCode.getTextPane().addMouseListener(this);

	}

	public void setUnderline() {
		sc = StyleContext.getDefaultStyleContext();

		defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
		mainStyle = sc.addStyle("MainStyle", defautStyle);

		StyleConstants.setUnderline(mainStyle, true);

		aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);

		this.imagens.textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);

	}

	public void setColorForSelectedText(Color background, Color foreground) {
		sc = StyleContext.getDefaultStyleContext();

		defautStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
		mainStyle = sc.addStyle("MainStyle", defautStyle);
		
		
		if(this.imagens.textAreaSourceCode.getAutoContrasteTipo()==0){
			StyleConstants.setBold(mainStyle, true);
			StyleConstants.setBackground(mainStyle, background);
			StyleConstants.setForeground(mainStyle, foreground);
						
						
			
		}else if(this.imagens.textAreaSourceCode.getAutoContrasteTipo()==1){
			StyleConstants.setBold(mainStyle, true);
			StyleConstants.setBackground(mainStyle, Color.BLACK);
			StyleConstants.setForeground(mainStyle, Color.WHITE);
			
							
		}else if(this.imagens.textAreaSourceCode.getAutoContrasteTipo()==2){
			StyleConstants.setBold(mainStyle, true);
			StyleConstants.setBackground(mainStyle, Color.WHITE);
			StyleConstants.setForeground(mainStyle, Color.BLACK);
		}
		
		
		aSet = sc.addAttributes(SimpleAttributeSet.EMPTY, mainStyle);

		this.imagens.textAreaSourceCode.getTextPane().setCharacterAttributes(aSet, false);
		addModificados(this.imagens.textAreaSourceCode.getTextPane().getSelectionStart(), this.imagens.textAreaSourceCode.getTextPane().getSelectionEnd(), background.getRed(), background
				.getGreen(), background.getBlue());
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
				System.out.print("abreTag=" + abreTag + "\n");
				System.out.print("fechaTag=" + fechaTag + "\n");
				this.imagens.textAreaSourceCode.getTextPane().select(abreTag, fechaTag + 1);
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
			System.out.println("init" + posClick + "\n");
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
			System.out.println(posClick);
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
				System.out.println("CalcEnter" + countEnter);
				System.out.println("posClick" + posClick);
				System.out.println("Ocorrencia1" + ocorrencia1 + "\n");
				System.out.println("FechaTag" + firstPosFechaTag + "\n");
				this.imagens.textAreaSourceCode.getTextPane().select(ocorrencia1, firstPosFechaTag + 1);

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
	void formataHTML() {
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