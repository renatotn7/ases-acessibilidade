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

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
/**
 * Modelo da tabela do resumo
 *
 */
public class ResumoTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6894915087976741302L;
	
//	private String[] columnNames = {"URL",
//	"ERROS\n PRIORIDADE\n 1",
//	"ERROS PRIORIDADE 2",
//	"ERROS PRIORIDADE 3",
//	"AVISOS PRIORIDADE 1",
//	"AVISOS PRIORIDADE 2",
//	"AVISOS PRIORIDADE 3"};
//	private Object[][] data;
//	
//	
//	public RelatorioTableModel(ArrayList<RelatorioDaURL> relatorios) {
//	data = new Object[relatorios.size()][7];
//	for (int i = 0; i < relatorios.size(); i++) {
//	RelatorioDaURL relatorio = relatorios.get(i);
//	data[i][0] = relatorio.getUrl();
//	data[i][1] = relatorio.getErrosPrioridade1();
//	data[i][2] = relatorio.getErrosPrioridade2();
//	data[i][3] = relatorio.getErrosPrioridade3();
//	data[i][4] = relatorio.getAvisosPrioridade1();
//	data[i][5] = relatorio.getAvisosPrioridade2();
//	data[i][6] = relatorio.getAvisosPrioridade3();
//	}
//	}
	
	private String[] columnNames = {"URL","P1","P2","P3"};
	private Object[][] data;
	
	public ResumoTableModel(ArrayList<RelatorioDaUrl> relatorios) {
	
			data = new Object[relatorios.size()][7];
		for (int i = 0; i < relatorios.size(); i++) {
			RelatorioDaUrl relatorio = relatorios.get(i);
			data[i][0] = relatorio.getUrl();
			data[i][1] = relatorio.getErrosPrioridade1() + " / " + relatorio.getAvisosPrioridade1();
			data[i][2] = relatorio.getErrosPrioridade2() + " / " + relatorio.getAvisosPrioridade2();
			data[i][3] = relatorio.getErrosPrioridade3() + " / " + relatorio.getAvisosPrioridade3();
		}
	}
	
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {
		return data.length;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}


}
 

