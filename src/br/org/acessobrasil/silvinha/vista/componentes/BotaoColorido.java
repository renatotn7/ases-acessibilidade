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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
/**
 * Cria um botão de outra cor
 *
 */
public class BotaoColorido extends JButton {
	private static final long serialVersionUID = 2600240018878457641L;

	// O gradiente do botão é composto por várias cores:
	// Parte de Cima, Parte de Baixo, Cor da Fonte, Borda do Label, Botao
	// Pressionado, Borda Label Pressionado
	public static final Color[] VERMELHO = { new Color(255, 181, 197), new Color(238, 169, 184), Color.black, new Color(215, 155, 168), new Color(205, 145, 158) };

	public static final Color[] VERDE = { new Color(50, 128, 40), new Color(33, 116, 27), Color.black, new Color(10, 102, 11), new Color(0, 92, 1) };

	String text;

	GradientPaint gpParteDeBaixo, gpParteDeCima;

	Rectangle2D.Float rectFillParteBaixo, rectFillParteCima;

	Font font;

	Rectangle2D r2d;

	private Color[] color;

	boolean pressed = false;

	public BotaoColorido(String s, Color[] c) {
		super(s);

		color = c;
		text = s;
		font = new Font("Dialog", Font.BOLD, 12);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				pressed = true;
				repaint();
			}

			public void mouseReleased(MouseEvent me) {
				pressed = false;
				repaint();
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		if (!pressed) {
			// BOTAO SEM ESTAR PRESSIONADO
			gpParteDeBaixo = new GradientPaint(1.0f, (float) getHeight(), color[1], 1.0f, (float) getHeight() * 0.3f, Color.white);
			gpParteDeCima = new GradientPaint(1.0f, 0.0f, color[0], 1.0f, (float) getHeight() * 0.3f, Color.white);
			rectFillParteBaixo = new Rectangle2D.Float(0.0f, (float) getHeight() * 0.3f, (float) getWidth(), (float) getHeight());
			rectFillParteCima = new Rectangle2D.Float(0.0f, 0.0f, (float) getWidth(), (float) getHeight() * 0.3f);
			g2.setPaint(gpParteDeBaixo);
			g2.fill(rectFillParteBaixo);
			g2.setPaint(gpParteDeCima);
			g2.fill(rectFillParteCima);

			// LABEL
			g2.setPaint(color[2]);
			g2.setFont(font);
			r2d = font.getStringBounds(text, g2.getFontRenderContext());
			g2.drawString(text, (int) ((getWidth() - r2d.getWidth()) / 2), (int) (getHeight() - 3 * r2d.getHeight() / 5));

			// BORDA DO LABEL
			g2.setPaint(color[3]);
			g2.drawRect((int) ((getWidth() - r2d.getWidth()) / 2), (int) ((getHeight() - r2d.getHeight()) / 2 - 2), (int) r2d.getWidth(), (int) (r2d.getHeight() + 2));
		} else {
			// BOTAO PRESSIONADO

			// PREENCHIMENTO DO BOTAL (FILL RECT)
			g2.setPaint(color[4]);
			g2.fillRect(0, 0, getWidth(), getHeight());

			g2.setPaint(Color.black);
			g2.setFont(font);

			r2d = font.getStringBounds(text, g2.getFontRenderContext());

			// LABEL
			g2.drawString(text, (int) ((getWidth() - r2d.getWidth()) / 2), (int) (getHeight() - 3 * r2d.getHeight() / 5));
			// BORDA DO LABEL
			g2.setPaint(new Color(205, 145, 158));
			g2.drawRect((int) ((getWidth() - r2d.getWidth()) / 2), (int) ((getHeight() - r2d.getHeight()) / 2 - 2), (int) r2d.getWidth(), (int) (r2d.getHeight() + 2));
		}// FIM DO ELSE
	}// FIM DO PAINTCOMPONENT
}
