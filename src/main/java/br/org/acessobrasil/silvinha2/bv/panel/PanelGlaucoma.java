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
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.org.acessobrasil.silvinha2.bv.componente.SlidebarLabel;
import br.org.acessobrasil.silvinha2.bv.filtros.Glaucoma;
import br.org.acessobrasil.silvinha2.mli.TradPaineisBV;

public class PanelGlaucoma extends JPanel  implements ChangeListener {
	/**
	 * Serial default
	 */
	private static final long serialVersionUID = 1L;

	public static SlidebarLabel sliderBlur;

	public static SlidebarLabel sliderSize;

	private Glaucoma glaucoma;
	public PanelGlaucoma(Glaucoma glaucoma,String nome) {
		//super(new GridLayout(2,1));
		super(new BorderLayout());
		LabelNome lblNome = new LabelNome(nome);
		JPanel painelSlider = new JPanel(new GridLayout(2,1));
		//System.out.print("PanelHipermetropia(Hipermetropia hipermetropia)()\n");

		sliderBlur = new SlidebarLabel(TradPaineisBV.FOCO,JSlider.HORIZONTAL, 0, 10, 2);
		sliderBlur.setName("Blur");
		
		sliderSize = new SlidebarLabel(TradPaineisBV.CAMPO_VISUAL,JSlider.HORIZONTAL, 0, 100, 4);
		sliderSize.setName("Tamanho");

		this.glaucoma = glaucoma;
		
		glaucoma.setBlurValue(sliderBlur.getValue());
		glaucoma.setSize(sliderSize.getValue());
		
		//this.add(sliderBlur, BorderLayout.NORTH);
		//this.add(sliderSize, BorderLayout.AFTER_LAST_LINE);
		painelSlider.add(sliderBlur);
		painelSlider.add(sliderSize);
		
		sliderBlur.addChangeListener(this);
		sliderBlur.setMajorTickSpacing(5);
		sliderBlur.setMinorTickSpacing(1);
		//sliderBlur.setPaintTicks(true);
		sliderBlur.setPaintLabels(true);

		sliderSize.addChangeListener(this);
		sliderSize.setMajorTickSpacing(10);
		sliderSize.setMinorTickSpacing(5);
		//sliderSize.setPaintTicks(true);
		sliderSize.setPaintLabels(true);

		
		this.add(lblNome,BorderLayout.PAGE_START);
		this.add(painelSlider,BorderLayout.CENTER);
		this.setVisible(true);
	}
	public PanelGlaucoma(Glaucoma glaucoma) {
		super(new GridLayout(2,1));
		//System.out.print("PanelHipermetropia(Hipermetropia hipermetropia)()\n");

		sliderBlur = new SlidebarLabel(TradPaineisBV.FOCO,JSlider.HORIZONTAL, 0, 10, 2);
		sliderBlur.setName("Blur");
		
		sliderSize = new SlidebarLabel(TradPaineisBV.CAMPO_VISUAL,JSlider.HORIZONTAL, 0, 100, 4);
		sliderSize.setName("Tamanho");

		this.glaucoma = glaucoma;
		
		glaucoma.setBlurValue(sliderBlur.getValue());
		glaucoma.setSize(sliderSize.getValue());
		
		//this.add(sliderBlur, BorderLayout.NORTH);
		//this.add(sliderSize, BorderLayout.AFTER_LAST_LINE);
		this.add(sliderBlur);
		this.add(sliderSize);
		
		sliderBlur.addChangeListener(this);
		sliderBlur.setMajorTickSpacing(5);
		sliderBlur.setMinorTickSpacing(1);
		//sliderBlur.setPaintTicks(true);
		sliderBlur.setPaintLabels(true);

		sliderSize.addChangeListener(this);
		sliderSize.setMajorTickSpacing(10);
		sliderSize.setMinorTickSpacing(5);
		//sliderSize.setPaintTicks(true);
		sliderSize.setPaintLabels(true);

		this.setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		//System.out.print("stateChanged()\n");
		JSlider source = (JSlider) e.getSource();
		//System.out.print("source.getName()=" + source.getName() + "\n");

		if (!source.getValueIsAdjusting()) {
			int valor = (int) source.getValue();
			//System.out.print("stateChanged=" + valor + "\n");
			if (source.getName().equals("Blur")) {
				glaucoma.setBlurValue(valor);				
			}else{
				glaucoma.setSize(valor);
			}
		}
	}
}
