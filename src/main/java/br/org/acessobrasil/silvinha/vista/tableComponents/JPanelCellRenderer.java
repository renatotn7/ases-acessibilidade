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

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
/**
 * Panel para tabela 
 *
 */
public class JPanelCellRenderer extends JPanel implements TableCellRenderer {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 28596128033460365L;
	
	/** map from table to map of rows to map of column heights */
	private final Map cellSizes = new HashMap();
	
	public JPanelCellRenderer() {
	   
		setLayout(new FlowLayout());
	}
	
	public Component getTableCellRendererComponent(JTable table, Object obj, 
			boolean isSelected, boolean hasFocus, 
			int row, int column) {
		this.removeAll();
		
		ArrayList labels = (ArrayList)obj;
		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i) instanceof JLabel) {
				add((JLabel)labels.get(i));
			}
		}
		
		TableColumnModel columnModel = table.getColumnModel();
		setSize(columnModel.getColumn(column).getWidth(), 100000);
		int height_wanted = (int) getPreferredSize().getHeight();
		addSize(table, row, column, height_wanted);
		height_wanted = findTotalMaximumRowSize(table, row);
		if (height_wanted != table.getRowHeight(row)) {
			table.setRowHeight(row, height_wanted);
		}
		return this;
	}
	
	private void addSize(JTable table, int row, int column,
			int height) {
		Map rows = (Map) cellSizes.get(table);
		if (rows == null) {
			cellSizes.put(table, rows = new HashMap());
		}
		Map rowheights = (Map) rows.get(new Integer(row));
		if (rowheights == null) {
			rows.put(new Integer(row), rowheights = new HashMap());
		}
		rowheights.put(new Integer(column), new Integer(height));
	}
	
	/**
	 * Look through all columns and get the renderer.  If it is
	 * also a TextAreaRenderer, we look at the maximum height in
	 * its hash table for this row.
	 */
	private int findTotalMaximumRowSize(JTable table, int row) {
		int maximum_height = 0;
		Enumeration columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn tc = (TableColumn) columns.nextElement();
			TableCellRenderer cellRenderer = tc.getCellRenderer();
			if (cellRenderer instanceof JPanelCellRenderer) {
				JPanelCellRenderer pcr = (JPanelCellRenderer) cellRenderer;
				maximum_height = Math.max(maximum_height, pcr.findMaximumRowSize(table, row));
			} else if (cellRenderer instanceof RegrasCellRenderer) {
				RegrasCellRenderer tar = (RegrasCellRenderer) cellRenderer;
				maximum_height = Math.max(maximum_height, tar.findMaximumRowSize(table, row));
			}
		}
		return maximum_height;
	}
	
	public int findMaximumRowSize(JTable table, int row) {
		Map rows = (Map) cellSizes.get(table);
		if (rows == null) return 0;
		Map rowheights = (Map) rows.get(new Integer(row));
		if (rowheights == null) return 0;
		int maximum_height = 0;
		for (Iterator it = rowheights.entrySet().iterator();
		it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			int cellHeight = ((Integer) entry.getValue()).intValue();
			maximum_height = Math.max(maximum_height, cellHeight);
		}
		return maximum_height;
	}
	
	
}