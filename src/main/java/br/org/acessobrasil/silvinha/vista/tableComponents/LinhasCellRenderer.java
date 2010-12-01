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
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import br.org.acessobrasil.silvinha.vista.panels.PainelRelatorioPorPrioridade;
/**
 * Controla como as linhas serão desenhadas 
 *
 */
public class LinhasCellRenderer extends JPanel implements TableCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1943337394026730059L;

	public LinhasCellRenderer() {
		super();
	 	
		setLayout(new GridLayout(0, 8));
	}
	
	public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected,
			boolean hasFocus, int row, int column) {
		this.removeAll();
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setFont(table.getFont());
		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				setForeground(UIManager.getColor("Table.focusCellForeground"));
				setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		} else {
			setBorder(new EmptyBorder(1, 2, 1, 2));
		}
		TableColumnModel columnModel = table.getColumnModel();
		setSize(columnModel.getColumn(column).getWidth(), 100000);
		
		
		ArrayList<Integer> linhas = (ArrayList<Integer>)obj;
		PainelRelatorioPorPrioridade.lineMap.put(new Integer(row), linhas);
		for (Integer linha : linhas) {
			add(new JLabel(String.valueOf(linha)));
		}
		
		
		
//		ArrayList labels = (ArrayList)obj;
//		for (int i = 0; i < labels.size(); i++) {
//			if (labels.get(i) instanceof JLabel) {
//				add((JLabel)labels.get(i));
//			}
//		}
		
//		ArrayList buttons = (ArrayList)obj;
//		for (int i = 0; i < buttons.size(); i++) {
//			if (buttons.get(i) instanceof JButton) {
//				add((JButton)buttons.get(i));
//			}
//		}
		
//		JComboBox combo = new JComboBox();
//		ArrayList<String> linhas = (ArrayList<String>)obj;
//		for (String linha : linhas) {
//			combo.addItem(linha);
//		}
//		columnModel.getColumn(column).setCellEditor(new DefaultCellEditor(combo));		


		int height_wanted = (int) this.getPreferredSize().getHeight();
		int avaiableHeight = PainelRelatorioPorPrioridade.ss.getSize(row);
		if ( (avaiableHeight == 0) || (height_wanted > avaiableHeight) ) {
			PainelRelatorioPorPrioridade.ss.setSize(row, height_wanted);
		} else { 
			height_wanted = avaiableHeight;
		}
		if ( (height_wanted != table.getRowHeight(row)) && (height_wanted > table.getRowHeight(row)) ) {
			table.setRowHeight(row, height_wanted);
		}
		return this;
	}
	
	
	
//	public JLabel getJLabelAt(Point p) {
//		JLabel label = null;
//		Object o = this.findComponentAt(p);
//		if (o instanceof JLabel) {
//			label = (JLabel)o;
//		}
//		return label;
//	}
	
}