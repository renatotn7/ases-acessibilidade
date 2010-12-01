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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe responsável por simular catarata
 * 
 * @author Fabio Issamu Oshiro
 */
public class Catarata implements FiltroDeImagem {
	private Blur blur = new Blur();
	/**
	 * Cor CFCF22
	 */
	private BufferedImage amarelamento;
	private int yellow = 0;

	public Catarata() {
		try {
			amarelamento = ImageIO.read(new File("imagens/catarata.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int max(int a, int b) {
		if (a > b)
			return a;
		return b;
	}

	private BufferedImage applyAlpha(BufferedImage pb, float alpha) {
		BufferedImage img = new BufferedImage(pb.getWidth(), pb.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) img.getGraphics().create();

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.drawImage(pb, 0, 0, null);
		g2.dispose();

		return img;
	}

	

	public BufferedImage aplicaFiltro(BufferedImage bi) {
		bi = blur.aplicaFiltro(bi);
		int w = amarelamento.getWidth();
		int h = amarelamento.getHeight();
		
		Graphics2D graphics = bi.createGraphics();
		graphics.drawImage(applyAlpha(amarelamento,yellow/11.0f), 0, 0, w, h, null);
		graphics.dispose();
		return bi;
	}

	/**
	 * Aplica o filtro
	 * @param bi
	 * @return BufferedImage
	 * @deprecated
	 */
	public BufferedImage aplicaFiltro_old(BufferedImage bi) {
		bi = blur.aplicaFiltro(bi);
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

			r = oR - yellow * 25 - 5;
			g = oG - yellow * 25 - 5;
			b = oB - yellow * 35 - 10;
			r = max(0, r);
			g = max(0, g);
			b = max(0, b);
			rgb[base] = (byte) (Math.min(255f, r));
			rgb[base + 1] = (byte) (Math.min(255f, g));
			rgb[base + 2] = (byte) (Math.min(255f, b));
		}

		raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3, new int[] { 0, 1, 2 }, null);

		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		return new BufferedImage(cm, (WritableRaster) raster, true, null);
	}

	public void setBlurValue(int valor) {
		blur.setValor(valor);
	}

	public void setYellowValue(int valor) {
		yellow = valor;
	}

}
