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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.PaginasNaoAnalisadas;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.Zipper;
/**
 * Grava o relatório completo 
 *
 */
public class GravadorDeRelatorio {
	/**
	 * Não deve ser mais utilizado.
	 * Método antigo que não gravava as subpastas
	 * @param fileName
	 * @return true caso consiga
	 * @deprecated
	 */
	public static boolean gravarRelatoriosSemSubDir(String fileName) {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		try {
			// Create the ZIP file
			fileName = fileName.endsWith("."+Extensions.silvinha) ? fileName : fileName + "."+Extensions.silvinha;
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileName));
			// Compress the files
			File cacheDir = new File("temp");
			if (cacheDir.isDirectory()) {
				for (File arquivo : cacheDir.listFiles()) {
					if (!arquivo.isDirectory()) {
						FileInputStream in = new FileInputStream(arquivo);
						// Add ZIP entry to output stream.
						out.putNextEntry(new ZipEntry(arquivo.getName()));
						// Transfer bytes from the file to the ZIP file
						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						// Complete the entry
						out.closeEntry();
						in.close();
						// arquivo.deleteOnExit();
					}
				}
			}
			// Complete the ZIP file
			out.close();
			return true;
		} catch (IOException e) {
			ExceptionDialog.showExceptionDialog("[Zipper]: " + e.getMessage());
			return false;
		}
	}
	/**
	 * Recupera as paginas nao analisadas
	 * @return true caso consiga
	 */
	public static boolean recuperarPaginasNaoAnalisadas(){
		try{
			PaginasNaoAnalisadas.mensagens.clear();
			PaginasNaoAnalisadas.relatorios.clear();
			String fName = new File("temp").getAbsolutePath();
			fName=fName.endsWith("/")|| fName.endsWith("\\")?fName:fName+"/";
			fName+="paginasNaoAnalisadas.txt";
			G_File naoAnalisadas = new G_File(fName);
			String linhas[]  = naoAnalisadas.read().split("\n");
			for (int i = 0; i < linhas.length; i++) {
				String cols[]=linhas[i].split("\t");
				RelatorioDaUrl relatorio = new RelatorioDaUrl();
				relatorio.setUrl(cols[0]);
				PaginasNaoAnalisadas.mensagens.add(cols[1]);
				PaginasNaoAnalisadas.relatorios.add(relatorio);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * Grava as páginas não analizadas
	 * @return true caso consiga
	 */
	private static boolean gravarPaginasNaoAnalisadas(){
		try{
			String fName = new File("temp").getAbsolutePath();
			fName=fName.endsWith("/")|| fName.endsWith("\\")?fName:fName+"/";
			fName+="paginasNaoAnalisadas.txt";
			G_File naoAnalisadas = new G_File(fName);
			naoAnalisadas.write("");
			int tot = PaginasNaoAnalisadas.mensagens.size();
			for(int i=0;i<tot;i++){
				String mensagem = PaginasNaoAnalisadas.mensagens.get(i);
				String url = PaginasNaoAnalisadas.relatorios.get(i).getUrl();
				naoAnalisadas.append(url+"\t"+mensagem+"\n");
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * Grava a avaliação atual no arquivo passado como parametro
	 * @param fileName
	 * @return true caso consiga fazer
	 */
	public static boolean gravarRelatorios(String fileName) {
		try {
			gravarPaginasNaoAnalisadas();
			gravarRelatoriosComSubDir(fileName);
			return true;
		} catch (ZipException e) {
			ExceptionDialog.showExceptionDialog("[Zipper]: " + e.getMessage());
			return false;
		} catch (IOException e) {
			ExceptionDialog.showExceptionDialog("[Zipper]: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Não deve ser mais utilizado
	 * @param fileName
	 * @param arquivos
	 * @return true caso consiga
	 * @deprecated
	 */
	public static boolean gravarRelatorios(String fileName,
			ArrayList<File> arquivos) {

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		try {
			// Create the ZIP file
			fileName = fileName.endsWith("."+Extensions.silvinha) ? fileName : fileName + "."+Extensions.silvinha;
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					fileName));
			// Compress the files
			for (File arquivo : arquivos) {
				FileInputStream in = new FileInputStream(arquivo);
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(arquivo.getName()));
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
				arquivo.deleteOnExit();
			}
			// Complete the ZIP file
			out.close();
			return true;
		} catch (IOException e) {
			ExceptionDialog.showExceptionDialog("[Zipper]: " + e.getMessage());
			return false;
		}
	}
	/**
	 * Método novo o qual grava as subpastas
	 * @param fileName
	 * @return true caso consiga gravar
	 * @throws ZipException
	 * @throws IOException
	 */
	private static boolean gravarRelatoriosComSubDir(String fileName) throws ZipException, IOException {
		Zipper zipper = new Zipper();
		fileName = fileName.endsWith("."+Extensions.silvinha) ? fileName : fileName + "."+Extensions.silvinha;
		zipper.criarZip(new File(fileName),new File("temp").listFiles());
		return true;
	}

}
