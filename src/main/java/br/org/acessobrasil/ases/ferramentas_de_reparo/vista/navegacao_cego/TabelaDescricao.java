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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.navegacao_cego;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import br.org.acessobrasil.silvinha2.mli.TradSimuladorNavegacao;
/**
 * Mostra os links internos
 * @author Fabio Issamu Oshiro, Haroldo Veiga e Renato Tomaz Nati
 *
 */
public class TabelaDescricao extends JTable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	
	DefaulTableModelNotEditable dtm;

	private PainelSimuladorNavegacao sim;

	public TabelaDescricao(PainelSimuladorNavegacao sim) {
		this.sim = sim;
		initComponents();
	}

	private void initComponents() {
		dtm = new DefaulTableModelNotEditable();
		dtm.setColumnIdentifiers(new String[] { TradSimuladorNavegacao.LINK_ORIGEM, TradSimuladorNavegacao.DESTINO });
		setModel(dtm);
		{
			TableColumnModel cm = getColumnModel();
			cm.getColumn(0).setMinWidth(200);
			cm.getColumn(0).setMaxWidth(200);
		}
	}

	public void clearTab() {
		for (int i = 0; i < dtm.getRowCount(); i++)
			dtm.removeRow(i);
	}

	public void addLinha(String link, String codigo) {
		dtm.addRow(new Object[] { link, codigo });

	}

	public void delAtualLinha() {
		dtm.removeRow(this.getSelectedRow());
	}

	public void selectTag() {

	}

	

	private class DefaulTableModelNotEditable extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {

			return false;

		}
	}

}
