/*******************************************************************************
 * Copyright 2005, 2006, 2007, 2008 Acessibilidade Brasil
 * Este arquivo � parte do programa ASES - Avaliador e Simulador para AcessibilidadE de S�tios
 * O ASES � um software livre; voc� pode redistribui-lo e/ou modifica-lo dentro dos termos da Licen�a P�blica Geral GNU como
 * publicada pela Funda��o do Software Livre (FSF); na vers�o 2 da Licen�a, ou (na sua opni�o) qualquer vers�o posterior.
 * Este programa � distribuido na esperan�a que possa ser  util, mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUA��O a qualquer  MERCADO ou APLICA��O EM PARTICULAR. Veja a Licen�a P�blica Geral GNU para maiores detalhes.
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU, sob o t�tulo "LICENCA.txt", junto com este programa, se n�o, escreva para a Funda��o do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
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
package br.org.acessobrasil.silvinha2.bv.componente;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
/**
 * Classe que coloca um r�tulo ou label junto com o slidebar
 * @author Fabio Issamu Oshiro
 *
 */
public class SlidebarLabel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSlider slider;
	private JLabel label;
	public SlidebarLabel(String label,int CONST, int min, int max, int valorInicial){
		super(new BorderLayout());
		slider = new JSlider(CONST, min, max, valorInicial);
		this.label = new JLabel(label);
		this.label.setPreferredSize(new Dimension(110,25));
		this.label.setHorizontalAlignment(JLabel.RIGHT);
		this.add(this.label, BorderLayout.WEST);
		this.add(slider, BorderLayout.CENTER);
		//this.setBorder(BorderFactory.createTitledBorder(label));
	}
	
	public void setName(String nome){
		slider.setName(nome);
	}
	public void addChangeListener(ChangeListener obj){
		slider.addChangeListener(obj);
	}
	public void setMajorTickSpacing(int v){
		slider.setMajorTickSpacing(v);
	}
	public void setMinorTickSpacing(int v){ 
		slider.setMinorTickSpacing(v);
	}
	public void setPaintTicks(boolean v){ 
		//slider.setPaintTicks(v);
	}
	public void setPaintLabels(boolean v){ 
		//slider.setPaintLabels(v);
	}

	public int getValue() {
		return slider.getValue();
	}
}
