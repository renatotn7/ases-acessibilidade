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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.CSSPanel;
import br.org.acessobrasil.silvinha2.mli.GERAL;
/**
 * UI separa o código original do em edição
 */
public class PainelTabbedCSS  extends SuperPainelCentral implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JMenuItem miBtnSalvar;

	private JTabbedPane editNaoEdit;

	FrameSilvinha frameSilvinha;

	FerramentaCSSPanel ferrCSSPanelEditavel;
	
	public PainelTabbedCSS(FrameSilvinha frameSilvinha) {
		super(new BorderLayout());

		CSSPanel.carregaTexto(TokenLang.LANG);

		editNaoEdit = new JTabbedPane();
		ferrCSSPanelEditavel = new FerramentaCSSPanel(frameSilvinha);
		FerramentaCSSPanel ferrCSSPanelNaoEditavel = new FerramentaCSSPanel(frameSilvinha);
		ferrCSSPanelEditavel.setPanelOriginal(ferrCSSPanelNaoEditavel);

		this.frameSilvinha = frameSilvinha;
		this.frameSilvinha.setTitle(CSSPanel.TITULO);
		this.frameSilvinha.setJMenuBar(MenuSilvinha.criaMenuBar(frameSilvinha,this, miBtnSalvar, null));

		editNaoEdit.add(GERAL.CODIGO_EDICAO, ferrCSSPanelEditavel);
		editNaoEdit.add(GERAL.CODIGO_ORIGINAL, ferrCSSPanelNaoEditavel);

		this.setBackground(this.frameSilvinha.corDefault);
		this.add(editNaoEdit, BorderLayout.CENTER);
		
	}
	public void avaliaUrl(String url) {
		ferrCSSPanelEditavel.avaliaUrl(url);
	}
	public void avaliaArq(String url) {
		System.out.println("PainelTabbedCSS.avaliaArq(url='"+url+"')");
		ferrCSSPanelEditavel.avaliaArq(url);
	}

	
	@Override
	public boolean showBarraUrl() {
		return true;
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		FerramentaCSSPanel ferrCSS = (FerramentaCSSPanel)editNaoEdit.getSelectedComponent();
		if (cmd == "SelecionarTudo") {
			ferrCSS.textAreaSourceCode.getTextPane().selectAll();
		} else if (cmd == "AumentaFonte") {
			ferrCSS.textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			ferrCSS.textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			ferrCSS.textAreaSourceCode.autoContraste();
		}else{
			ferrCSSPanelEditavel.actionPerformed(e);
		}
	}
	
}
