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

//classe adicionada por Renato Tomaz Nati em 10/1/2007
//é o frame onde escolhemos o arquivo local a ser analisado
// é chamado apartir do painel avaliação
package br.org.acessobrasil.silvinha.vista.componentes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.org.acessobrasil.silvinha.entidade.NomeArquivoOuDiretorioLocal;
import br.org.acessobrasil.silvinha.util.BuscaHtmlemDiretorio;
import br.org.acessobrasil.silvinha.vista.listeners.ExecutarAgoraListenerLocal;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;

/**
 * Caixa de diálogo para selecionar um diretório 
 *
 */
public class HTMLLocalDirectoryChooser extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	static public ArrayList<File> files;

	BuscaHtmlemDiretorio buscador;

	JFileChooser chooser;

	String choosertitle;

	private ExecutarAgoraListenerLocal threadExecutarAgora;

	public HTMLLocalDirectoryChooser(PainelAvaliacao pp) {

		files = new ArrayList<File>();

		chooser = new JFileChooser();

		// adcionado em 10/01/2007
		// HtmlLocalFileFilter filter = new HtmlLocalFileFilter();
		// chooser.setFileFilter(filter);
		//

		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(choosertitle);

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			PainelResumo.valorVelocidade = 100;
			PainelResumo.setDesabilitarBtnContinuar(true);

			// alterei aqui
			NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio = chooser.getCurrentDirectory().toString() + "\\" + chooser.getName(chooser.getSelectedFile());

			NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio = NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio.replace('\\', '/');

			PainelAvaliacao.habilitarParar();
			// files=buscador.findFiles(new
			// File(NomeArquivoOuDiretórioLocal.nomeArquivoOuDiretório),
			// ".*\\.html");
			try {
				threadExecutarAgora = new ExecutarAgoraListenerLocal(pp);
				threadExecutarAgora.start();
			} catch (Exception erro) {
				JOptionPane.showMessageDialog(null, "erro na thread");
			}

			// System.out.println("getCurrentDirectory(): "
			// + chooser.getCurrentDirectory());
			// System.out.println("getSelectedFile() : "
			// + chooser.getSelectedFile());
		} else {
			// System.out.println("No Selection ");
		}

	}

	public void actionPerformed(ActionEvent e) {
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}
}
/*
 * public static void main(String s[]) {
 * 
 * HTMLFileChooser panel = new HTMLFileChooser(); frame.addWindowListener( new
 * WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } } );
 * frame.getContentPane().add(panel,"Center");
 * frame.setSize(panel.getPreferredSize()); frame.setVisible(true); } }
 */
