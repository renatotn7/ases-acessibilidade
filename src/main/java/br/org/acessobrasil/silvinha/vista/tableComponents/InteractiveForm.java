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

package br.org.acessobrasil.silvinha.vista.tableComponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
/**
 * Formulario interativo 
 *
 */
public class InteractiveForm extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final String[] columnNames = { "Url", "Erros/Avisos P1", "Erros/Avisos P2", "" };

	protected JTable table;

	protected JScrollPane scroller;

	protected InteractiveTableModel tableModel;

	public InteractiveForm() {

		initComponent();
	}

	public void initComponent() {
		tableModel = new InteractiveTableModel(columnNames); // chama a
																// classe que
																// chamará os
																// arquivos de
																// som
		tableModel.addTableModelListener(new InteractiveForm.InteractiveTableModelListener()); // seta
																								// o
																								// listener
		table = new JTable(); // cria uma tabela normal
		table.setModel(tableModel); // seta o modelo como table model
		table.setSurrendersFocusOnKeystroke(true);
		if (!tableModel.hasEmptyRow()) {
			tableModel.addEmptyRow();
		}

		scroller = new javax.swing.JScrollPane(table);
		table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
		TableColumn hidden = table.getColumnModel().getColumn(InteractiveTableModel.HIDDEN_INDEX);
		hidden.setMinWidth(2);
		hidden.setPreferredWidth(2);
		hidden.setMaxWidth(2);
		hidden.setCellRenderer(new InteractiveRenderer(InteractiveTableModel.HIDDEN_INDEX));

		setLayout(new BorderLayout());
		add(scroller, BorderLayout.CENTER);
	}

	public void highlightLastRow(int row) {
		int lastrow = tableModel.getRowCount();
		if (row == lastrow - 1) {
			table.setRowSelectionInterval(lastrow - 1, lastrow - 1);
		} else {
			table.setRowSelectionInterval(row + 1, row + 1);
		}

		table.setColumnSelectionInterval(0, 0);
	}

	class InteractiveRenderer extends DefaultTableCellRenderer {
		protected int interactiveColumn;

		public InteractiveRenderer(int interactiveColumn) {
			this.interactiveColumn = interactiveColumn;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column == interactiveColumn && hasFocus) {
				if ((InteractiveForm.this.tableModel.getRowCount() - 1) == row && !InteractiveForm.this.tableModel.hasEmptyRow()) {
					InteractiveForm.this.tableModel.addEmptyRow();
				}

				highlightLastRow(row);
			}

			return c;
		}
	}

	public class InteractiveTableModelListener implements TableModelListener {
		public void tableChanged(TableModelEvent evt) {
			if (evt.getType() == TableModelEvent.UPDATE) {
				int column = evt.getColumn();
				int row = evt.getFirstRow();
				// System.out.println("row: " + row + " column: " + column);
				table.setColumnSelectionInterval(column + 1, column + 1);
				table.setRowSelectionInterval(row, row);
			}
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			JFrame frame = new JFrame("Interactive Form");
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					System.exit(0);
				}
			});
			frame.getContentPane().add(new InteractiveForm());
			frame.pack(); // faz com que a janela tenha o tamanhoa inicial de
							// forma que caiba todos os seus subcomponentes
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
