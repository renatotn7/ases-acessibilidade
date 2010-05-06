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

package br.org.acessobrasil.silvinha.util;


import java.util.*;
import java.io.*;

/**
 * Faz algo parecido com "dir /s *.html" (no caso Windows) ou
 * find . -name '*.html' -print (no caso Unix). 
 * Cuidado: se houver um arquivo "XXX.HTML" (em maiúsculas) não vai
 * reconhecer, a menos que você explicite na expressão regular.
 */
public class BuscaHtmlemDiretorio {
	/**
	 * O diretório inicial.
	 * Uma expressão regular que deve ser aplicada
	 * ao nome do arquivo.
    */
	public BuscaHtmlemDiretorio(){};
	
	public ArrayList<File> findFiles (final File startingDirectory, final String pattern) {
		
		ArrayList<File> files = new ArrayList<File>();
		 if (startingDirectory.isDirectory()) {
		      File[] sub = startingDirectory.listFiles(new FileFilter() { // listfiles lista os arquivos dentro de algo, o método implementaod é sempre chamado por esse método
			      public boolean accept (File pathname) {
				      
			    	  //faz com que so seja retornado se atender os requisitos abaixo
			    	  //a classe accept fica sempre se repetindo
			    	  return pathname.isDirectory() || pathname.getName().matches (pattern);
				  }
			  });
			  for (File fileDir: sub) {
			      if (fileDir.isDirectory()) {
				       files.addAll (findFiles (fileDir, pattern));
				  } else {
				       files.add (fileDir); // 
				  }
			  }
		 }
		 return files;
	}
    /**
	 * 
	 */
  //  public static void main(String[] args) {
	 //   ExemploDir ed = new ExemploDir();
		// Listando todos os arquivos "*.html"
	 //   System.out.println (ed.findFiles (new File("."), ".*\\.html"));
		// Listando todos os arquivos "*.java" ou "abs*.txt"
	//    System.out.println (ed.findFiles (new File("."), "(.*\\.java|abs.*\\.txt)"));
		// Listando todos os arquivos "*.htm*" (incluindo arquivos *.HTM) - útil para Windows
	 //   System.out.println (ed.findFiles (new File("."), "(?i).*\\.htm[^.]*"));
		// Listando todos os arquivos "*.htm*" e "*.jsp" 
		// (incluindo arquivos *.HTM e *.JSP) no diretório C:\INETPUB - útil para Windows
	   // System.out.println (ed.findFiles (new File("C:/INETPUB"), "(?i)(.*\\.htm[^.]*|.*\\.jsp)"));
//	}
	
}