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
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.LookupOp;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Aplica uma transformação nula na imagem :-)
 * 
 * @author Fabio Issamu Oshiro
 */
public class NullTransform implements FiltroDeImagem {

	public BufferedImage aplicaFiltro1(BufferedImage bi) {
		return bi;
	}

	public BufferedImage aplicaFiltro2(BufferedImage bi) {
		byte[] data = new byte[256];
		for (int i = 0; i < 256; i++) {
			data[i] = (byte) (Math.sqrt((float) i / 255.0) * 255);
		}

		ByteLookupTable table = new ByteLookupTable(0, data);
		LookupOp op = new LookupOp(table, null);
		BufferedImage brighterImage = op.filter(bi, null);
		brighterImage.setRGB(0, 255, 255);
		return brighterImage;
	}

	public BufferedImage teste(BufferedImage bi) {
		Raster raster = bi.getData();
		int w = raster.getWidth();
		int h = raster.getHeight();
		byte[] rgb = new byte[w * h * 3];
		int[] R = raster.getSamples(0, 0, w, h, 0, (int[]) null);
		int[] G = raster.getSamples(0, 0, w, h, 1, (int[]) null);
		int[] B = raster.getSamples(0, 0, w, h, 2, (int[]) null);
		int tipo=6;
		for (int i = 0, imax = R.length, base = 0; i < imax; i++, base += 3) {
			int r = 255 - R[i];
			int g = 255 - G[i];
			int b = 255 - B[i];
			switch(tipo){
			case 1:
				r+=g/3;
				b+=g/3;
				g=g/3;
				break;
			case 2:
				r+=b/3;
				g+=b/3;
				b=b/3;
				break;
			case 3:
				b+=(r/10)*5;
				g+=(r/10)*3;
				r=(r/10)*2;
				break;
			case 4:
				b+=(r/10)*6;
				g+=(r/10)*3;
				r=(r/10)*1;
				break;
			case 5:
				int gr=g+r;
				b+=(gr/10)*10;
				g=(gr/10)*5;
				r=(gr/10)*0;
				break;
			case 6:
				int oR=r,oG=g,oB=b;
				b+=r;
				r=g;
				int resto=b-255;
				if(resto>0){
					int resto2=resto-(resto/2);
					g=r+(resto/2);
					r=r+resto2;
					b=oB;
				}else{
					g=r;
					/*
					if((r-g)*(r-g)<20*20){
						//branco
						r=0;g=0;b=0;
					}
					*/
				}
				break;
			}
			
			rgb[base] = (byte) (255 - Math.min(255f, r));
			rgb[base + 1] = (byte) (255 - Math.min(255f, g));
			rgb[base + 2] = (byte) (255 - Math.min(255f, b));
		}
		
		raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3, new int[]{0, 1, 2}, null);
		 
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
	}

	Retinopatia a = new Retinopatia();
	public BufferedImage aplicaFiltro(BufferedImage bi) {
		/*
		return a.aplicaFiltro(bi);
		/*/
		return bi;
		//return teste(bi);
		//*/
		
	}
}
