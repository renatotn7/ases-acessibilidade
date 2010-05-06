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

package br.org.acessobrasil.silvinha2.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * Classe para carregar uma imagem da internet dentro de uma aplicação desktop.
 * @author fabio
 *
 */
public class G_URLIcon {
	public static boolean hasError;
	
	/**
	 * Passar a URL e o Label para colocar a imagem dentro
	 * @param strUrl Caminho inteiro
	 * @param label
	 */
	public G_URLIcon(String strUrl,JLabel label){
		hasError=false;
		Icon icone;
		try {
			icone = getIconFromUrl(strUrl);
			label.setIcon(icone);
			label.setSize(icone.getIconWidth(), icone.getIconHeight());
			//label.setBounds(0, 0, , );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			hasError=true;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			hasError=true;
			e.printStackTrace();
		}
	}
	
	/**
	 * Passar a URL e o Label para colocar a imagem dentro
	 * @param label
	 * @param strUrl Caminho inteiro
	 */
	public static void setIcon(JLabel label,String strUrl){
		hasError=false;
		Icon icone;
		try {
			icone = getIconFromUrl(strUrl);
			label.setIcon(icone);
			label.setSize(icone.getIconWidth(), icone.getIconHeight());
			//label.setBounds(0, 0, , );
		} catch (MalformedURLException e) {
			File arq = new File(strUrl);
			Image image;
			if(!arq.exists()){
				arq = new File("C:\\Arquivos de programas\\AcessibilidadeBrasil\\ASES\\exemplo\\globo_files\\"+strUrl);
			}
			try {
				image = ImageIO.read(arq);
				icone = new ImageIcon(image);
				label.setIcon(icone);
				label.setSize(icone.getIconWidth(), icone.getIconHeight());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
	        
			// TODO Auto-generated catch block
			hasError=true;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			hasError=true;
			e.printStackTrace();
		}
	}
	
	public static Icon getIconFromUrl(String strImageUrl) throws MalformedURLException,IOException{
        URL imgUrl = null;
        imgUrl = new URL(strImageUrl);
        java.io.InputStream is = imgUrl.openStream();
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix("jpg");
        
        ImageReader reader = it.next();
        reader.setInput(iis);
        /*
        reader.addIIOReadProgressListener(new IIOReadProgressListener() {
            public void sequenceStarted(ImageReader source, int minIndex) {
            }
            
            public void sequenceComplete(ImageReader source) {
            }
            
            public void imageStarted(ImageReader source, int imageIndex) {
            }
            
            public void imageProgress(ImageReader source, float percentageDone) {
                //setProgress((int) percentageDone);
            	//System.out.print(percentageDone+"\n");
            }
            
            public void imageComplete(ImageReader source) {
            	//System.out.print(100+"\n");//setProgress(100);
            }
            
            public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) {
            }
            
            public void thumbnailProgress(ImageReader source, float percentageDone) {
            }
            
            public void thumbnailComplete(ImageReader source) {
            }
            
            public void readAborted(ImageReader source) {
            }
            
        });
        */
        Image image = reader.read(0);
        Icon icon = new ImageIcon(image);
        ////System.out.print("End\n");
        return icon;
	}
}
