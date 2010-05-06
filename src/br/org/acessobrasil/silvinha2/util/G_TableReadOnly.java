/*******************************************************************************
 * Copyright 2005, 2006, 2007, 2008 Acessibilidade Brasil
 * Este arquivo é parte do programa ASES - Avaliador e Simulador para AcessibilidadE de Sítios
 * O ASES é um software livre; você pode redistribui-lo e/ou modifica-lo dentro dos termos da Licença Pública Geral GNU como
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da Licença, ou (na sua opnião) qualquer versão posterior.
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer  MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU para maiores detalhes.
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU, sob o título "LICENCA.txt", junto com este programa, se não, escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *******************************************************************************/
package br.org.acessobrasil.silvinha2.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * Tabela somente para leitura
 * @author Fabio Issamu Oshiro
 */
public class G_TableReadOnly extends JTable {
	private static final long serialVersionUID = 1L;

	DefaulTableModelNotEditable dtm;

	private String colunas[];

	private int maxWidth[];

	private ArrayList<G_TableReadOnlyListener> listeners = new ArrayList<G_TableReadOnlyListener>();

	public G_TableReadOnly(String colunas[], int maxWidth[]) {
		this.colunas = colunas;
		this.maxWidth = maxWidth;
		initComponents();
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					fireEvent();
				}
			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}
		});
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					fireEvent();
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
			
		});
		
	}

	private void fireEvent() {
		int row = this.getSelectedRow();
		if (row != -1) {
			int col = this.getSelectedColumn();
			String valor = String.valueOf(this.getValueAt(row, col));
			for(int i=0;i<listeners.size();i++){
				listeners.get(i).onSelected(row, col, valor);
			}
		}
	}

	private void initComponents() {
		dtm = new DefaulTableModelNotEditable();
		dtm.setColumnIdentifiers(this.colunas);
		setModel(dtm);
		{
			TableColumnModel cm = getColumnModel();
			for (int i = 0; i < maxWidth.length; i++) {
				if(maxWidth[i]!=0){
					cm.getColumn(i).setMaxWidth(maxWidth[i]);
				}
			}
		}
	}

	public void clearTable(){
		initComponents();
	}
	
	public void addLinha(Object obj[]) {
		dtm.addRow(obj);
	}

	public void addListener(G_TableReadOnlyListener listener) {
		listeners.add(listener);
	}

	private class DefaulTableModelNotEditable extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}
