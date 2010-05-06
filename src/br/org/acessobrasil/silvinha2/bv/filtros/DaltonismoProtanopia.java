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

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Classe que implementa um filtro que simula a ausencia de cones vermelhos
 * 
 * @author Fabio Issamu Oshiro
 * 
 */
public class DaltonismoProtanopia implements FiltroDeImagem {
	
	private BufferedImage filtro1(BufferedImage bi) {
		Raster raster = bi.getData();
		int w = raster.getWidth();
		int h = raster.getHeight();
		byte[] rgb = new byte[w * h * 3];
		int[] R = raster.getSamples(0, 0, w, h, 0, (int[]) null);
		int[] G = raster.getSamples(0, 0, w, h, 1, (int[]) null);
		int[] B = raster.getSamples(0, 0, w, h, 2, (int[]) null);
		for (int i = 0, imax = R.length, base = 0; i < imax; i++, base += 3) {
			int r = R[i];
			int g = G[i];
			int b = B[i];
			int oR = r, oG = g, oB = b;
			r=(oR+oG+oB)/3;
			g=(oG+oR)/2;
			//b=(oR+oB)/2;
			b=oB;
			rgb[base] = (byte) (Math.min(255f, r));
			rgb[base + 1] = (byte) (Math.min(255f, g));
			rgb[base + 2] = (byte) (Math.min(255f, b));
		}

		raster = Raster.createInterleavedRaster(new DataBufferByte(rgb,
				rgb.length), w, h, w * 3, 3, new int[] { 0, 1, 2 }, null);

		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, true,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		return new BufferedImage(cm, (WritableRaster) raster, true, null);
	}

	public BufferedImage aplicaFiltro(BufferedImage bi) {
		Raster raster = bi.getData();
		int w = raster.getWidth();
		int h = raster.getHeight();
		byte[] rgb = new byte[w * h * 3];
		int[] R = raster.getSamples(0, 0, w, h, 0, (int[]) null);
		int[] G = raster.getSamples(0, 0, w, h, 1, (int[]) null);
		int[] B = raster.getSamples(0, 0, w, h, 2, (int[]) null);
		for (int i = 0, imax = R.length, base = 0; i < imax; i++, base += 3) {
			int r = 255 - R[i];
			int g = 255 - G[i];
			int b = 255 - B[i];
			int oR = r, oG = g, oB = b;
			r=(oR+oB+oG)/3;
			g=(oG+oG+oG+oR)/4;
			b=(oB+oB+oB+oR)/4;
			
			rgb[base] = (byte) (255 - Math.min(255f, r));
			rgb[base + 1] = (byte) (255 - Math.min(255f, g));
			rgb[base + 2] = (byte) (255 - Math.min(255f, b));
			
		}

		raster = Raster.createInterleavedRaster(new DataBufferByte(rgb,
				rgb.length), w, h, w * 3, 3, new int[] { 0, 1, 2 }, null);

		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, true,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		return new BufferedImage(cm, (WritableRaster) raster, true, null);
	}
}
/*
b += r;
r = g;
int resto = b - 255;
if (resto > 0) {
	int resto2 = resto - (resto / 2);
	g = r + (resto / 2);
	r = r + resto2;
	b = oB;
} else {
	g = r;
}*/