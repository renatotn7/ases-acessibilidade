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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.corretor_eventos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
/**
 * UI para corrigir eventos, separa o código original do em edição
 */
public class PainelTabbedCorretorEventos extends SuperPainelCentral implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JMenuItem miBtnSalvar;

	private JTabbedPane editNaoEdit;

	FrameSilvinha frameSilvinha;

	PanelCorretorEventos panelEditavel;

	public PainelTabbedCorretorEventos(FrameSilvinha frameSilvinha) {
		super(new BorderLayout());

		editNaoEdit = new JTabbedPane();
		panelEditavel = new PanelCorretorEventos(frameSilvinha, TxtBuffer.getContent());
		PanelCorretorEventos painelOriginal = new PanelCorretorEventos(frameSilvinha, TxtBuffer.getContentOriginal());
		panelEditavel.setPanelOriginal(painelOriginal);

		this.frameSilvinha = frameSilvinha;
		frameSilvinha.setTitle(GERAL.TIT_CORR_EVT);
		this.frameSilvinha.setJMenuBar(MenuSilvinha.criaMenuBar(frameSilvinha, this, miBtnSalvar, panelEditavel.textAreaSourceCode));

		editNaoEdit.add(GERAL.CODIGO_EDICAO, panelEditavel);
		editNaoEdit.add(GERAL.CODIGO_ORIGINAL, painelOriginal);

		panelEditavel.salvaAlteracoes = TxtBuffer.getInstanciaSalvaAlteracoes(panelEditavel.textAreaSourceCode.getTextPane(), panelEditavel.btn_salvar,
				miBtnSalvar, this.frameSilvinha);

		this.setBackground(this.frameSilvinha.corDefault);
		this.add(editNaoEdit, BorderLayout.CENTER);
	}

	
	public void avaliaUrl(String url) {
		panelEditavel.avaliaUrl(url);
	}
	public void avaliaArq(String url) {
		panelEditavel.avaliaArq(url);
	}

	@Override
	public boolean showBarraUrl() {
		return true;
	}
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		PanelCorretorEventos panel = (PanelCorretorEventos)editNaoEdit.getSelectedComponent();
		if (cmd == "SelecionarTudo") {
			panel.textAreaSourceCode.getTextPane().requestFocus();
			panel.textAreaSourceCode.getTextPane().selectAll();
		} else if (cmd == "AumentaFonte") {
			panel.textAreaSourceCode.aumentaFontSize();
		} else if (cmd == "DiminuiFonte") {
			panel.textAreaSourceCode.diminuiFontSize();
		} else if (cmd == "Contraste") {
			panel.textAreaSourceCode.autoContraste();
		}else{
			panelEditavel.actionPerformed(e);
		}
	}
}
