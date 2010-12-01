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

import java.io.*;
import java.util.*;
import java.util.zip.*;
/**
 * Classe para fazer arquivos zip
 *
 */
public class Zipper {
	private int progresso=0;
	private boolean trabalhando = false;
	@SuppressWarnings("unchecked")
	public List<ZipEntry> listarEntradasZip(File arquivo) throws ZipException, IOException {
		List<ZipEntry> entradasDoZip = new ArrayList<ZipEntry>();
		ZipFile zip = null;
		try {
			zip = new ZipFile(arquivo);
			Enumeration e = zip.entries();
			ZipEntry entrada = null;
			while (e.hasMoreElements()) {
				entrada = (ZipEntry) e.nextElement();
				entradasDoZip.add(entrada);
			}
			setArquivoZipAtual(arquivo);
		} finally {
			if (zip != null) {
				zip.close();
			}
		}
		return entradasDoZip;
	}

	public void extrairZip(File diretorio) throws ZipException, IOException {
		extrairZip(this.getArquivoZipAtual(), diretorio);
	}

	@SuppressWarnings("unchecked")
	public void extrairZip(File arquivoZip, File diretorio) throws ZipException, IOException {
		trabalhando = true;
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[TAMANHO_BUFFER];
		try {
			// cria diretório informado, caso não exista
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			if (!diretorio.exists() || !diretorio.isDirectory()) {
				throw new IOException("Informe um diretório válido");
			}
			zip = new ZipFile(arquivoZip);
			int i=0,total = zip.size();
			Enumeration e = zip.entries();

			while (e.hasMoreElements()) {
				progresso = (++i*100)/total;
				ZipEntry entrada = (ZipEntry) e.nextElement();
				arquivo = new File(diretorio, entrada.getName());
				// se for diretório inexistente, cria a estrutura
				// e pula pra próxima entrada
				if (entrada.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
					continue;
				}
				// se a estrutura de diretórios não existe, cria
				if (!arquivo.getParentFile().exists()) {
					arquivo.getParentFile().mkdirs();
				}
				try {
					// lê o arquivo do zip e grava em disco
					is = zip.getInputStream(entrada);
					os = new FileOutputStream(arquivo);
					int bytesLidos = 0;
					if (is == null) {
						throw new ZipException("Erro ao ler a entrada do zip: " + entrada.getName());
					}
					while ((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception ex) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Exception e) {
				}
			}
			trabalhando = false;
		}
	}

	@SuppressWarnings("unchecked")
	public List criarZip(File arquivoZip, File[] arquivos) throws ZipException, IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		setArquivoZipAtual(null);
		try {
			// adiciona a extensão .zip no arquivo, caso não exista
			//if (!arquivoZip.getName().toLowerCase().endsWith(".zip")) {
			//	arquivoZip = new File(arquivoZip.getAbsolutePath() + ".zip");
			//}
			fos = new FileOutputStream(arquivoZip);
			bos = new BufferedOutputStream(fos, TAMANHO_BUFFER);
			List listaEntradasZip = criarZip(bos, arquivos);
			setArquivoZipAtual(arquivoZip);
			return listaEntradasZip;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List criarZip(OutputStream os, File[] arquivos) throws ZipException, IOException {
		if (arquivos == null || arquivos.length < 1) {
			throw new ZipException("Adicione ao menos um arquivo ou diretório");
		}
		List listaEntradasZip = new ArrayList();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(os);
			for (int i = 0; i < arquivos.length; i++) {
				String caminhoInicial = arquivos[i].getParent();
				List novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
				if (novasEntradas != null) {
					listaEntradasZip.addAll(novasEntradas);
				}
			}
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (Exception e) {
				}
			}
		}
		return listaEntradasZip;
	}

	@SuppressWarnings("unchecked")
	private List adicionarArquivoNoZip(ZipOutputStream zos, File arquivo, String caminhoInicial) throws IOException {
		List listaEntradasZip = new ArrayList();
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		byte buffer[] = new byte[TAMANHO_BUFFER];
		try {
			// diretórios não são adicionados
			if (arquivo.isDirectory()) {
				if(!arquivo.getName().equals(".svn")){
					// recursivamente adiciona os arquivos dos diretórios abaixo
					File[] arquivos = arquivo.listFiles();
					for (int i = 0; i < arquivos.length; i++) {
						List novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
						if (novasEntradas != null) {
							listaEntradasZip.addAll(novasEntradas);
						}
					}
				}
				return listaEntradasZip;
			}
			String caminhoEntradaZip = null;
			int idx = arquivo.getAbsolutePath().indexOf(caminhoInicial);
			if (idx >= 0) {
				// calcula os diretórios a partir do diretório inicial
				// isso serve para não colocar uma entrada com o caminho
				// completo
				caminhoEntradaZip = arquivo.getAbsolutePath().substring(idx + caminhoInicial.length() + 1);
			}
			ZipEntry entrada = new ZipEntry(caminhoEntradaZip);
			zos.putNextEntry(entrada);
			zos.setMethod(ZipOutputStream.DEFLATED);
			fis = new FileInputStream(arquivo);
			bis = new BufferedInputStream(fis, TAMANHO_BUFFER);
			int bytesLidos = 0;
			while ((bytesLidos = bis.read(buffer, 0, TAMANHO_BUFFER)) != -1) {
				zos.write(buffer, 0, bytesLidos);
			}
			listaEntradasZip.add(entrada);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		return listaEntradasZip;
	}

	public void fecharZip() {
		setArquivoZipAtual(null);
	}

	public File getArquivoZipAtual() {
		return arquivoZipAtual;
	}

	private void setArquivoZipAtual(File arquivoZipAtual) {
		this.arquivoZipAtual = arquivoZipAtual;
	}

	private File arquivoZipAtual;

	private static final int TAMANHO_BUFFER = 2048; // 2 Kb
	public int getProgresso() {
		return progresso;
	}

	public boolean getTrabalhando() {
		return trabalhando;
	}

	public void setTrabalhando(boolean b) {
		trabalhando=b;
	}
}