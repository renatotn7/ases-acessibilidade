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

package br.org.acessobrasil.silvinha.vista.listeners;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import br.org.acessobrasil.ases.persistencia.SingBancoSite;
import br.org.acessobrasil.silvinha.entidade.Historico;
import br.org.acessobrasil.silvinha.util.HSQLDB;
import br.org.acessobrasil.silvinha.util.PropertyLoader;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha2.util.G_File;
/**
 * responsável por encerrar o programa 
 *
 */
public class SilvinhaWindowListener extends WindowAdapter {

	private FrameSilvinha frame;

	private int state;

	public SilvinhaWindowListener(FrameSilvinha frame) {
		//System.out.println("br.org.acessobrasil.silvinha.vista.listeners.SilvinhaWindowListener.java");
		this.frame = frame;
	}

	public void windowStateChanged(WindowEvent e) {
		this.state = e.getNewState();

	}

	public void windowClosing(WindowEvent e) {
		try{
			TokenLang.setProperty(PropertyLoader.SILVINHA_FRAME_STATE, String.valueOf(state), frame);
			if (state == Frame.NORMAL) {
				Dimension d = frame.getSize();
				double h = d.getHeight() >= 565 ? d.getHeight() : 565;
				double w = d.getWidth() >= 757 ? d.getWidth() : 757;
				TokenLang.setProperty(PropertyLoader.SILVINHA_FRAME_HEIGHT, String.valueOf(h), frame);
				TokenLang.setProperty(PropertyLoader.SILVINHA_FRAME_WIDTH, String.valueOf(w), frame);
				Point p = frame.getLocation();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				double x = p.getX() <= ((screenSize.width - 757) / 2) ? p.getX() : ((screenSize.width - 757) / 2);
				double y = p.getY() <= ((screenSize.height - 565) / 2) ? p.getY() : ((screenSize.height - 565) / 2);
				TokenLang.setProperty(PropertyLoader.SILVINHA_FRAME_LOCATION_X, String.valueOf(x), frame);
				TokenLang.setProperty(PropertyLoader.SILVINHA_FRAME_LOCATION_Y, String.valueOf(y), frame);
			}
			try{
				HSQLDB.finalizaBanco();
				Historico.gravaHistorico();
				System.runFinalization();
				if(SingBancoSite.getInstancia()!=null){
					SingBancoSite.getInstancia().closeConn();
				}
			}catch (Exception ex){
				ex.printStackTrace();
			}
			File cacheDir = new File("cache");
			if (cacheDir.isDirectory()) {
				for (File file : cacheDir.listFiles()) {
					file.delete();
				}
			}
			G_File.clearDir("temp");
		}catch(Exception err){
			err.printStackTrace();
		}
		System.exit(0);
	}
	
}
