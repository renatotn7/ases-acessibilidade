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

package br.org.acessobrasil.silvinha.util.versoes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradAtualizadorDeVersoes;
/**
 * Atualizador de versões via web 
 *
 */
public class AtualizadorDeVersoes {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");
	
	private String url;
	
	public AtualizadorDeVersoes(String url) {
		
		this.url = url;
	}

	public boolean confirmarAtualizacao() {
		String msg = TradAtualizadorDeVersoes.HA_NOVA_VERSAO +
		TradAtualizadorDeVersoes.ASES_NOME +
		TradAtualizadorDeVersoes.DESEJA_ATUALIZAR;
		if (JOptionPane.showConfirmDialog(null, msg, TradAtualizadorDeVersoes.ATUALIZACAO_PROGRAMA, JOptionPane.YES_NO_OPTION) == 0) {
			return baixarVersao();
		} else {
			return false;
		}
	}
	
	public boolean baixarVersao() {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		NameValuePair param = new NameValuePair("param" , "update");
        method.setRequestBody(new NameValuePair[] {param});		
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler(3, false));
		JFrame down = new JFrame("Download");
		File downFile = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}
			Header header = method.getResponseHeader("Content-Disposition");
			String fileName = "silvinha.exe";
			if (header != null) {
				fileName = header.getValue().split("=")[1];
			}
			// Read the response body.
			is = method.getResponseBodyAsStream();
			down.pack();
			ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(
                            down,
                            TradAtualizadorDeVersoes.FAZ_DOWNLOAD_FILE + fileName,
                            is);
			pmis.getProgressMonitor().setMinimum(0);
			pmis.getProgressMonitor().setMaximum((int)method.getResponseContentLength());
			downFile = new File(fileName);
			fos = new FileOutputStream(downFile); 
			int c;
			while( ((c=pmis.read()) != -1) && (!pmis.getProgressMonitor().isCanceled())) {    
				fos.write(c);
			}  
			fos.flush();  
			fos.close();
			String msgOK = TradAtualizadorDeVersoes.DOWNLOAD_EXITO +
						TradAtualizadorDeVersoes.DESEJA_ATUALIZAR_EXECUTAR +
						TradAtualizadorDeVersoes.ARQUIVO_DE_NOME + fileName + TradAtualizadorDeVersoes.LOCALIZADO_NA +
						TradAtualizadorDeVersoes.PASTA_INSTALACAO +
						TradAtualizadorDeVersoes.APLICACAO_DEVE_SER_ENCERRADA +
						TradAtualizadorDeVersoes.DESEJA_CONTINUAR_ATUALIZACAO;
			if (JOptionPane.showConfirmDialog(null, msgOK, TradAtualizadorDeVersoes.ATUALIZACAO_DO_PROGRAMA, JOptionPane.YES_NO_OPTION) == 0){
				return true;	
			} else {
				return false;
			}
		} catch (HttpException he) {
			log.error("Fatal protocol violation: " + he.getMessage(), he);
			return false;
		} catch (InterruptedIOException iioe) {
			method.abort();
			String msg = GERAL.OP_CANCELADA_USUARIO +
			TradAtualizadorDeVersoes.DIRECIONADO_A_APLICACAO;
			JOptionPane.showMessageDialog(down, msg);
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {			
				method.releaseConnection();
				System.exit(0);
			}
			if (downFile!= null && downFile.exists()) {
				downFile.delete();
			}
			return false;
//			System.err.println("Fatal transport error: " + iioe.getMessage());
//			iioe.printStackTrace();
		} catch (IOException ioe) {
			log.error("Fatal transport error: " + ioe.getMessage(), ioe);
			return false;
		} finally {
			//Release the connection.
			method.releaseConnection();
		}
	}
	
//	public static void main(String[] args) {
//		System.out.println("baixar versao");
//		HttpClient client = new HttpClient();
//		PostMethod method = new PostMethod("http://200.201.183.180/versionador/vrschk");
//		NameValuePair param = new NameValuePair("param" , "update");
//        method.setRequestBody(new NameValuePair[] {param});		
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler(3, false));
//		JFrame down = new JFrame("Download");
//		File downFile = null;
//		InputStream is = null;
//		FileOutputStream fos = null;
//		try {
//			int statusCode = client.executeMethod(method);
//			if (statusCode != HttpStatus.SC_OK) {
//				System.err.println("Method failed: " + method.getStatusLine());
//			}
//			Header header = method.getResponseHeader("Content-Disposition");
//			String fileName = "silvinha.exe";
//			if (header != null) {
//				fileName = header.getValue().split("=")[1];
//			}
//			// Read the response body.
//			is = method.getResponseBodyAsStream();
////			InputStream is = new FileInputStream("c:\\msdelog.log");
//			down.setSize(new Dimension(700, 200));
//			down.pack();
//			//down.setVisible(true);
//			ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(
//                            down,
//                            "Fazendo Download: " + fileName,
//                            is);
//			pmis.getProgressMonitor().setMinimum(0);
//			pmis.getProgressMonitor().setMaximum((int)method.getResponseContentLength());
//			downFile = new File(fileName);
//			fos = new FileOutputStream(downFile); 
//			int c;
//			while( ((c=pmis.read()) != -1) && (!pmis.getProgressMonitor().isCanceled())) {    
//				fos.write(c);
//			}  
//			fos.flush();  
//			fos.close();
//			//down.setVisible(false);
////			String buffer = method.getResponseBodyAsString();
////			System.out.println(buffer);
//		} catch (HttpException he) {
//			System.err.println("Fatal protocol violation: " + he.getMessage());
//			he.printStackTrace();
//		} catch (InterruptedIOException iioe) {
//			method.abort();
//			String msg = "O Download foi cancelado pelo usuário. \n " +
//			             "Você será direcionado novamente à aplicação.";
//			JOptionPane.showMessageDialog(down, msg);
//			try {
//				if (fos != null) {
//					//fos.flush();  
//					fos.close();
//				}
//			} catch (IOException ioe) {			
//				method.releaseConnection();
//				System.exit(0);
//			}
//			if (downFile!= null && downFile.exists()) {
//				downFile.delete();
//			}
////			System.err.println("Fatal transport error: " + iioe.getMessage());
////			iioe.printStackTrace();
//		} catch (IOException ioe) {
//			System.err.println("Fatal transport error: " + ioe.getMessage());
//			ioe.printStackTrace();
//		} finally {
//			//Release the connection.
//			method.releaseConnection();
//			System.exit(0);
//		}
//
//	}
	
}
