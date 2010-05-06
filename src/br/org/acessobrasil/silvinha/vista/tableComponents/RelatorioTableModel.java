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
import java.util.HashSet;

import javax.swing.table.AbstractTableModel;

import br.org.acessobrasil.ases.regras.InterfRegrasHardCoded;
import br.org.acessobrasil.ases.regras.MethodFactRegHardCod;
import br.org.acessobrasil.silvinha.entidade.PontoVerificacao;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.GERAL;
/**
 * Modelo de tabela do relatório
 *
 */
public class RelatorioTableModel extends AbstractTableModel {
	int cont;

	HashSet<PontoVerificacao> HashPVRelatorio = new HashSet<PontoVerificacao>();

	/**
	 * 
	 */
	private static final long serialVersionUID = -519198052574412142L;

	private String[] columnNames = { GERAL.TBL_RELATORIO_COL_1, GERAL.TBL_RELATORIO_COL_2, GERAL.TBL_RELATORIO_COL_3, GERAL.TBL_RELATORIO_COL_4 };

	private Object[][] data;

	public RelatorioTableModel(HashSet<PontoVerificacao> relatorio, String TipoAndPrioridade) {

		cont = 0;
		HashPVRelatorio = relatorio;

		Object[] pvs = relatorio.toArray();
		int wcag_emag;
		// se nucleo antigo
		// inclusaoDeGenericos(pvs,TipoAndPrioridade,Gerente.getProperties().getProperty("tipo_avaliacao"));
		// data = new Object[relatorio.size()+numDeGenericos][4];
		// //

		// se nucleo novo
		data = new Object[relatorio.size()][4];
		// ///

		int contPV = 0;
		for (; cont < data.length; cont++) {
			PontoVerificacao pv = (PontoVerificacao) pvs[contPV];
			wcag_emag = pv.getWcagEmag();
			
			
			
			/*
			 * Achar de onde está vindo este 1.25!!
			 * String codRegra = (String) (pv.getGl() + "." + pv.getCp());
			 * if(codRegra.equals("1.25")) continue; 
			 */
			
			/*
			 * Código da Regra
			 */
			data[cont][0] = (String) (pv.getGl() + "." + pv.getCp());
			/*
			 * Texto da regra
			 */
			String texto = TokenLang.getRegra(pv.getIdRegra()).trim();
			if (texto.equals("")) {
				InterfRegrasHardCoded regra;
				if (wcag_emag == 2) {
					regra = MethodFactRegHardCod.mFRegHardCod("EMAG");
				} else {
					regra = MethodFactRegHardCod.mFRegHardCod("WCAG");
				}
				data[cont][1] = regra.getTextoRegra((String) (pv.getGl() + "." + pv.getCp())) + GERAL.SAIBA_MAIS_PARENTESES;
			} else {
				data[cont][1] = TokenLang.getRegra(pv.getIdRegra()).trim();
			}
			/*
			 * Retira as ocorrencias com o valor do numero da linha igual a 0
			 */
			ArrayList<Integer> linhas = pv.getLinhas();
			int ocorrencias = linhas.size();
			for (int j = 0; j < linhas.size(); j++) {
				if (linhas.get(j).intValue() == 0) {
					linhas.remove(j);
				}
			}
			ocorrencias = linhas.size();
			/*
			 * Ocorrencias
			 */
			data[cont][2] = ocorrencias != 0 ? String.valueOf(ocorrencias) : "---";
			/*
			 * Linhas
			 */
			data[cont][3] = linhas;
			contPV++;
			// StringBuilder linhas = new StringBuilder(" ");
			// for (int j = 0; j < pv.getLinhas().size(); j++) {
			// linhas.append(pv.getLinhas().get(j) + " ");
			// if (j != (pv.getLinhas().size() - 1)) {
			// linhas.append(", ");
			// }
			// }
			// data[i][3] = linhas.toString();

			// ArrayList<JLabel> labels = new ArrayList<JLabel>();
			// for (int j = 0; j < pv.getLinhas().size(); j++) {
			// final JLabel label = new
			// JLabel(String.valueOf(pv.getLinhas().get(j)));
			// // label.setBorder(BorderFactory.createLineBorder(new
			// Color(0,0,0), 1));
			// // label.setMaximumSize(new Dimension(16, 20));
			// label.setBackground(Color.WHITE);
			// labels.add(label);
			// }
			// data[i][3] = labels;

			// ArrayList<JButton> buttons = new ArrayList<JButton>();
			// for (int j = 0; j < pv.getLinhas().size(); j++) {
			// final JButton button = new
			// JButton(String.valueOf(pv.getLinhas().get(j)));
			// // label.setBorder(BorderFactory.createLineBorder(new
			// Color(0,0,0), 1));
			// button.setMaximumSize(new Dimension(16, 20));
			// button.setBackground(Color.WHITE);
			// buttons.add(button);
			// }
			// data[i][3] = buttons;

			// ArrayList<String> linhas = new ArrayList<String>();
			// for (int j = 0; j < pv.getLinhas().size(); j++) {
			// linhas.add(String.valueOf(pv.getLinhas().get(j)));
			// }
			// data[i][3] = linhas;
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
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		if (col == 3) {
			return true;
		} else {
			return false;
		}
	}

	private int inclusaoDeGenericos(Object[] pvs, String tipoAndPrioridade, String orgao) {

		InterfRegrasHardCoded regrasHardCoded = null;
		int tipoAndPrio = 0;
		String regras[];
		int cont2 = 0;
		int lengthParaPrio = 0;
		// String EMAG ou String WCAG
		if (tipoAndPrioridade.equals("AvisoP1") || tipoAndPrioridade.equals("AvisoP2") || tipoAndPrioridade.equals("AvisoP3")) {
			if (tipoAndPrioridade.equals("AvisoP1")) {
				tipoAndPrio = 1;
			} else if (tipoAndPrioridade.equals("AvisoP2")) {
				tipoAndPrio = 2;
			} else if (tipoAndPrioridade.equals("AvisoP3")) {
				tipoAndPrio = 3;
			}
			if (orgao.equals(String.valueOf(PainelAvaliacao.EMAG))) {
				regrasHardCoded = MethodFactRegHardCod.mFRegHardCod("EMAG");
			} else if (orgao.equals(String.valueOf(PainelAvaliacao.WCAG))) {
				regrasHardCoded = MethodFactRegHardCod.mFRegHardCod("WCAG");
			}
			regras = regrasHardCoded.getGenericos();

			for (; cont < regras.length; cont++) {

				if (regras[cont].split("@")[2].equals(String.valueOf(tipoAndPrio))) {
					lengthParaPrio++;
				}

			}

			data = new Object[HashPVRelatorio.size() + lengthParaPrio][4];
			cont = 0;

			for (; cont < regras.length; cont++) {

				if (regras[cont].split("@")[2].equals(String.valueOf(tipoAndPrio))) {
					data[cont2][0] = (regras[cont].split("@"))[0];
					// data[i][1] = pv.getRegra().trim();
					data[cont2][1] = (regras[cont].split("@"))[1];
					data[cont2][2] = "---";
					data[cont2][3] = new ArrayList<Integer>();
					cont2++;
				}
			}
			cont = cont2;
			return cont;
		} else {
			data = new Object[HashPVRelatorio.size()][4];
			return 0;
		}

	}

}