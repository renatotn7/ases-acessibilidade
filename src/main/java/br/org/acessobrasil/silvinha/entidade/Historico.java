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

package br.org.acessobrasil.silvinha.entidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;
/**
 * Histórico de avaliações 
 *
 */
public class Historico {

	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");
	
	private static ArrayList<String> listaLinks = new ArrayList<String>();
	
	static 
	{
		if (!getHistorico())
		{
			listaLinks.add("");
		}
	}
	
	public static void addLink(String link) {
		if (!listaLinks.contains(link))
		{
			if (listaLinks.size() > 6)
			{
				listaLinks.remove(1);	
			}
			listaLinks.add(link);
			Collections.sort(listaLinks);
		}
	}

	public static Object[] getLinks() {
		return listaLinks.toArray(); 
	}
	
	public static void gravaHistorico() 
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("config/historico");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(listaLinks);
			oos.flush();
			fos.flush();
			fos.close();
			oos.close();
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}
	
	public static boolean getHistorico()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{
			File file = new File("config/historico");
			if (file.exists())
			{
				fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				listaLinks = ( (ArrayList<String>)ois.readObject() );
				return true;
			}
			else 
			{
				listaLinks = new ArrayList<String>();
				return false;
			}
		}
		catch (Exception e)
		{
			log.error("getHistorico: " + e);
			listaLinks = new ArrayList<String>();
			return false;
		}
		finally
		{
			if (fis != null)
			{
				try {
					fis.close();
				} 
				catch (Exception e){}
			}
			if (ois != null)
			{
				try
				{
					ois.close();
				} 
				catch (Exception e){}
			}
		}
	}

}
