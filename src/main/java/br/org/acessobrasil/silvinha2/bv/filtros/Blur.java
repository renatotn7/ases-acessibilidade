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
 *
 */
public class Blur implements FiltroDeImagem {
	private int valor=0;
	/**
	 * @return retorna o kernel para fazer o blur com 9
	 */
	private static Kernel blur2() {
		//16
		float floatB = .03f;
		//8
		float floatV = .06f;
		//1
		float floatC = 1.0f - ((floatV*8.0f)+(floatB*16.0f));
		//
		//System.out.println(((floatV*8.0f)+(floatB*16.0f)));
		return new Kernel(5, 5, new float[] { 
				floatB, floatB, floatB, floatB, floatB,
				floatB, floatV, floatV, floatV, floatB,
				floatB, floatV, floatC, floatV, floatB,
				floatB, floatV, floatV, floatV, floatB,
				floatB, floatB, floatB, floatB, floatB 
				
		});
	}
	/**
	 * @return retorna o kernel para fazer o blur com 9
	 */
	private static Kernel blur1() {
		float floatV = .12f;
		float float22 = 1.0f - (floatV * 8.0f);
		return new Kernel(3, 3, new float[] { floatV, floatV, floatV, floatV, float22, floatV, floatV, floatV, floatV });
	}
	
	/**
	 * Aplica o filtro
	 */
	public BufferedImage aplicaFiltro(BufferedImage bi) {
		Kernel k = blur2();
		ConvolveOp op = new ConvolveOp(k);		
		for(int i=0;i<valor;i++)
			bi=op.filter(bi,null);
		return bi;
	}

	/**
	 * Troca o valor do blur
	 * @param v
	 */
	public void setValor(int v){
		valor=v;
	}
}
