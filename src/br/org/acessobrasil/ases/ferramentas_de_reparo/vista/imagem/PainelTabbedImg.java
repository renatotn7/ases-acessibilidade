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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
/**
 * UI separa o código editável do não editável 
 */
public class PainelTabbedImg<PAINELFERR extends PanelDescricaoImagens> extends SuperPainelCentral implements ChangeListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTabbedPane editNaoEdit;

	public static int ultimaFerramenta;

	PAINELFERR ferrEditavel;

	PAINELFERR ferrNaoEditavel;

	FrameSilvinha frameSilvinha;

	private JMenuItem miBtnSalvar;

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if (command == "AumentaFonte") {
			ferrEditavel.getScrollPaneDescricao().aumentaFontSize();
			ferrNaoEditavel.getScrollPaneDescricao().aumentaFontSize();
		} else if (command == "DiminuiFonte") {
			ferrEditavel.getScrollPaneDescricao().diminuiFontSize();
			ferrNaoEditavel.getScrollPaneDescricao().diminuiFontSize();
		} else if (command == "Contraste") {
			ferrEditavel.getScrollPaneDescricao().autoContraste();
			ferrNaoEditavel.getScrollPaneDescricao().autoContraste();
			// reavalia(textAreaSourceCode.getText());
		}else{
			ferrEditavel.actionPerformed(e);
		}
	}

	public void init(FrameSilvinha frameSilvinha) {
		this.frameSilvinha = frameSilvinha;
		this.frameSilvinha.setTitle(GERAL.TIT_CONT_ALT_TAG_IMG);
		editNaoEdit = new JTabbedPane();

		editNaoEdit.addChangeListener(this);
		this.setBackground(this.frameSilvinha.corDefault);
	}

	public PainelTabbedImg(FrameSilvinha frameSilvinha) {
		super(new BorderLayout());
		ferrNaoEditavel = (PAINELFERR) new PanelDescricaoImagens(TxtBuffer.getContentOriginal(), PanelDescricaoImagens.CONTEUDO, frameSilvinha);
		ferrEditavel = (PAINELFERR) new PanelDescricaoImagens(TxtBuffer.getContent(), PanelDescricaoImagens.CONTEUDO, frameSilvinha);
		
		init(frameSilvinha);
		adicionaPaineis(ferrEditavel, ferrNaoEditavel);
	}

	public PainelTabbedImg(String conteudo, int conteudoOuArquivo, FrameSilvinha frameSilvinha, String endPagina) {
		super(new BorderLayout());


		ferrNaoEditavel = (PAINELFERR) new PanelDescricaoImagens(TxtBuffer.getContentOriginal(), PanelDescricaoImagens.CONTEUDO, frameSilvinha, endPagina);
		ferrEditavel = (PAINELFERR) new PanelDescricaoImagens(conteudo, PanelDescricaoImagens.CONTEUDO, frameSilvinha);
		init(frameSilvinha);

		adicionaPaineis(ferrEditavel, ferrNaoEditavel);
	}

	private void adicionaPaineis(PAINELFERR ferrEditavel, PAINELFERR ferrNaoEditavel) {
		// frameSilvinha.setJMenuBar(this.criaMenuBar());
		frameSilvinha.setJMenuBar(MenuSilvinha.criaMenuBar(frameSilvinha, this, miBtnSalvar, ferrEditavel.textAreaSourceCode));

		ferrEditavel.setPanelOriginal(ferrNaoEditavel);
		editNaoEdit.add(GERAL.CODIGO_EDICAO, ferrEditavel);
		editNaoEdit.add(GERAL.CODIGO_ORIGINAL, ferrNaoEditavel);

		this.add(editNaoEdit, BorderLayout.CENTER);
	}

	@Override
	public boolean showBarraUrl() {
		return true;
	}

	public void avaliaUrl(String url) {
		ferrEditavel.avaliaUrl(url);
	}

	public void avaliaArq(String url) {
		ferrEditavel.avaliaArq(url);
	}

	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				System.gc();
			}

		}).start();
	}
}
