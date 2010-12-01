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

package br.org.acessobrasil.silvinha.vista.frames.mensagens;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.vista.tableComponents.DefaulTableModelNotEditable;
import br.org.acessobrasil.silvinha2.mli.GERAL;

/**
 * Mostra as páginas não analisadas
 * @author Renato Tomaz Nati
 */
public class PaginasNaoAnalisadas extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DefaulTableModelNotEditable dtm = new DefaulTableModelNotEditable();

	private JTable tabela = null;

	private JScrollPane scrollTabela = null;

	int contaRelatorio;

	public static ArrayList<RelatorioDaUrl> relatorios = new ArrayList<RelatorioDaUrl>();

	public RelatorioDaUrl rel = new RelatorioDaUrl();

	private static ResumoDoRelatorio resumo;

	public static ArrayList<String> mensagens = new ArrayList<String>();

	/**
	 * 
	 * Inicializa a classe
	 * 
	 * 
	 */
	public PaginasNaoAnalisadas(int i) {
	}

	public PaginasNaoAnalisadas() {
		// Cria o frame com o titulo

		super("páginas não analisadas");
		// Cria o tamanho do frame
		setSize(600, 400);
		contaRelatorio = 0;
		// Seta o conteudo do frame no centro:

		// Adiciona o JScrollPane que contem a JTable
		getContentPane().add(getScrollTabela(), BorderLayout.CENTER);
		// Mostra o formulario na tela
		addLinha();
		setVisible(true);
	}

	/**
	 * Bloco principal
	 * 
	 * @param args
	 */

	/**
	 * Cria a jtable
	 * 
	 * @return
	 */
	private JTable getTabela() {
		if (tabela == null) {
			tabela = createJTable();
		}
		return tabela;
	}

	/**
	 * Cria o scroll onde irá conter a jtable
	 * 
	 * @return
	 */
	private JScrollPane getScrollTabela() {
		if (scrollTabela == null) {
			scrollTabela = new JScrollPane(getTabela());
			scrollTabela.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return scrollTabela;

	}

	/**
	 * Cria a JTable
	 * 
	 * @return JTable
	 */
	public JTable createJTable() {

		ArrayList dados = new ArrayList();

		// Alimenta as linhas de dados

		dados.add(new String[] { "dsfg", "sdfgd" });

		// SimpleTableModel modelo = new SimpleTableModel(dados, colunas,
		// edicao);
		JTable jtable = new JTable(dtm);
		dtm.addColumn(GERAL.ENDERECO);
		dtm.addColumn(GERAL.TIPO);

		jtable.getColumnModel().getColumn(0).setMinWidth(400);

		jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Adiciona uma linha pelo modelo

		// Remove pelo indice
		// dtm.removeRow(0); // Sao Paulo

		// Remove pelo valor
		// dtm.removeRow("RN", 0);
		contaRelatorio = 0;
		return jtable;

	}

	public void addLinha() {

		// for(contaRelatório=contaRelatório;contaRelatório<ThreadParaGerente.relatorios.size();contaRelatório++)

		for (contaRelatorio = contaRelatorio; contaRelatorio < relatorios.size(); contaRelatorio++) {

			rel = relatorios.get(contaRelatorio);
			dtm.addRow(new Object[] { rel.getUrl(), mensagens.get(contaRelatorio) });
		}

	}

	// public static void main(final String[] args){
	// new PaginasNaoAnalisadas();

	// }

}
