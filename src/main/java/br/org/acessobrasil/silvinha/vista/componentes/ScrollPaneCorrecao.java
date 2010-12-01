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

package br.org.acessobrasil.silvinha.vista.componentes;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import br.org.acessobrasil.nucleuSilva.entidade.Tag;
import br.org.acessobrasil.nucleuSilva.util.Conexao;
import br.org.acessobrasil.silvinha.util.Token;
import br.org.acessobrasil.silvinha.vista.panels.PainelCorrecao;
/**
 * Usado em PainelCorrecao
 * @author Acessibilidade Brasil
 *
 */
public class ScrollPaneCorrecao extends JScrollPane implements MouseListener {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");
	private static final long serialVersionUID = -7124095009214954198L;
	
	private Tag tags[][];
	Container panelGeral;
	Color cor;
	Color corErro;
	private TagTextFieldA tagSelecionada;
	private HashMap<String, String> listaDescricoes;
	private PainelCorrecao parentPanel;
	private ArrayList<Integer> listaPv3;
	
	public ScrollPaneCorrecao(PainelCorrecao panel, Tag[][] tags) {
		
		this.parentPanel = panel;
		panelGeral = new Container();
		cor = Color.WHITE;
		corErro = new Color(245, 228, 155);
		this.tags = tags;
		tagSelecionada = new TagTextFieldA();
		listaDescricoes = inicializaDescricoes();
		listaPv3 = inicializaListaPv3();
		if (tags != null)
		{
			jbInit();
			getFields();
		}
	}
	
	/**
	 * Retorna os campos
	 *
	 */
	private void getFields() {
		for(int i = 0; i < tags.length; i++) {
			Tag tagLinha[] = tags[i];
			JPanel panel = new JPanel(new FlowLayout(0, 0, 0));
			panel.setBackground(cor);
			DecimalFormat formatador = new DecimalFormat();
			formatador.applyPattern("0000");
			panel.add(new JLabel(formatador.format(i + 1) + " "));
			panelGeral.add(panel);
			if(tagLinha != null && tagLinha.length != 0) {
				for(int j = 0; j < tagLinha.length; j++) {
					Tag tag = tagLinha[j];
					if(tag != null) {
						TagTextFieldA text = new TagTextFieldA(tag);
						text.setBorder(Token.BorderVazia);
						if (text.getPv3() > 0 && listaPv3.contains(new Integer(text.getPv3()))) {
							text.setBackground(corErro);
							text.addMouseListener(this);
						} else if(text.getPv3() < 0) {
								text.setBorder(Token.BorderCorrigida);
								text.setEditable(false);
							} else {
								text.setBackground(cor);
								text.setEditable(false);
							}
						panel.add(text);
					}
				}
			}
		}
	}
	
	private void jbInit()
	{
		panelGeral.setLayout(new GridLayout((tags != null ? tags.length : 1) , 0));
		getViewport().add(panelGeral);
	}
	
	public TagTextFieldA getTagSelecionada()
	{
		return tagSelecionada;
	}
	
	private HashMap<String, String> inicializaDescricoes() {
		HashMap<String, String> descricoes = new HashMap<String, String>();
		Conexao c = null;
		try {
			c = new Conexao();
			Statement st = c.getCon().createStatement();
			String sql = "Select PV3, DESCRICAO from CORRIGIR;";
			ResultSet rs = st.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					descricoes.put(rs.getString("PV3"), rs.getString("DESCRICAO"));
				}
			}				
		} catch(ClassNotFoundException cnfe) {
			log.error(cnfe.getMessage(), cnfe);
		} catch(SQLException sqle) {
			log.error(sqle.getMessage(), sqle);
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException sqle2) {
					log.error(sqle2.getMessage(), sqle2);
				}
			}
		}
		return descricoes;
	}
	
	private ArrayList<Integer> inicializaListaPv3() {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Conexao c = null;
		try {
			c = new Conexao();
			Statement st = c.getCon().createStatement();
			ResultSet rs = st.executeQuery(" SELECT pv3 FROM CORRIGIR; ");
			if(rs != null) {
				while (rs.next()) {
					lista.add(rs.getInt(1));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ArrayList<Integer>();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException sqle) {
					log.error(sqle.getMessage(), sqle);
					c = null;
				}
			}
		}
		return lista;
	}

	public void mousePressed(MouseEvent e) {
		TagTextFieldA m = (TagTextFieldA)e.getSource();
		tagSelecionada = m;
		String pv3 = String.valueOf(m.getPv3());
		if (listaDescricoes.containsKey(pv3)) {
			parentPanel.setDescricao(listaDescricoes.get(pv3));
		} else {
			parentPanel.setDescricao("Ajuda não disponível para este item.");
		}
	}
	
	//MÉTODOS DE MOUSELISTENER NÃO IMPLEMENTADOS
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
