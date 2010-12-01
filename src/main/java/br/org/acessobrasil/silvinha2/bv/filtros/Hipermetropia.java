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
package br.org.acessobrasil.silvinha2.bv.filtros;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
/**
 * Classe responsável por desfocar as imagens
 * @author Fabio Issamu Oshiro
 */
public class Hipermetropia implements FiltroDeImagem {
	
	private int value=1;
	
	/**
	 * @return retorna o kernel para fazer o blur com 9
	 */
	private static Kernel blur1() {
		float floatV = .1f;
		float floatA = .35f;
		float floatO = 1.0f - (floatV * 6.0f) - (floatA * 2f);
		return new Kernel(9, 1, new float[] { 
				floatV, floatV, floatV, 
				floatA, floatO, floatA, 
				floatV, floatV, floatV });
	}
	
	/**
	 * Aplica o filtro
	 */
	public BufferedImage aplicaFiltro(BufferedImage bi) {
		Kernel k = blur1();
		ConvolveOp op = new ConvolveOp(k);
		//BufferedImage blurry = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		//BufferedImage blurry2 = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);

		for(int i=0;i<value;i++)
			bi=op.filter(bi, null);
		//blurry2=op.filter(blurry,null);
		//for(int i=0;i<10;i++){
		//	blurry2=op.filter(blurry2,null);
		//}
		return bi;
	}

	/**
	 * Altera o valor da hipermetropia
	 * @param valor
	 */
	public void setValue(int valor) {
		//System.out.print("setValue="+valor+"\n");
		value=valor;
	}
}
