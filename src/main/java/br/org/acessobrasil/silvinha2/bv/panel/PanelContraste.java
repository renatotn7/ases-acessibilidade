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
package br.org.acessobrasil.silvinha2.bv.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelContraste extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panelBg;

	private JPanel panelFg;

	private JPanel corBg;

	private JPanel corFg;

	private JLabel resultado;
	
	private JPanel panelResultado;

	int bgR, bgG, bgB;

	int fgR, fgG, fgB;

	protected static int selecionado = 0;

	public PanelContraste() {
		this.setLayout(new GridLayout(1, 3));
		panelBg = new JPanel();
		panelFg = new JPanel();
		panelResultado = new JPanel();
		corBg = new JPanel();
		corFg = new JPanel();
		resultado = new JLabel();
		//resultado.setHorizontalTextPosition(JLabel.CENTER);
		resultado.setHorizontalAlignment(JLabel.CENTER);
		panelBg.setBorder(BorderFactory.createTitledBorder("Cor de fundo"));
		panelFg.setBorder(BorderFactory.createTitledBorder("Cor do texto"));
		panelResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
		panelBg.setLayout(new BorderLayout());
		panelFg.setLayout(new BorderLayout());
		panelResultado.setLayout(new BorderLayout());
		panelBg.add(corBg, BorderLayout.CENTER);
		panelFg.add(corFg, BorderLayout.CENTER);
		panelResultado.add(resultado,BorderLayout.CENTER);
		corBg.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				//System.out.println("Bg");
				selecionado = 0;
				corBg.setBorder(BorderFactory.createBevelBorder(1));
				corFg.setBorder(BorderFactory.createEmptyBorder());
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		corFg.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				//System.out.println("Fg");
				selecionado = 1;
				corBg.setBorder(BorderFactory.createEmptyBorder());
				corFg.setBorder(BorderFactory.createBevelBorder(1));
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		
		this.add(panelBg);
		this.add(panelFg);
		this.add(panelResultado);
	}

	private void calcResultado() {
		int diff = getBrilho(fgR, fgG, fgB) - getBrilho(bgR, bgG, bgB);
		if (abs(diff) < 125) {
			resultado.setText("Ruim");
		} else {
			resultado.setText("Bom");
		}
	}

	private int abs(int v) {
		return v > 0 ? v : -v;
	}

	private int getBrilho(int r, int g, int b) {
		return ((r * 299) + (g * 587) + (b * 114)) / 1000;
	}

	public void setCor(int r, int g, int b) {
		if (selecionado == 0) {
			setBg(r, g, b);
		} else {
			setFg(r, g, b);
		}
	}

	private void setFg(int r, int g, int b) {
		fgR = r;
		fgG = g;
		fgB = b;
		corFg.setBackground(new Color(r, g, b));
		calcResultado();
	}

	private void setBg(int r, int g, int b) {
		bgR = r;
		bgG = g;
		bgB = b;
		corBg.setBackground(new Color(r, g, b));
		calcResultado();
	}
}
