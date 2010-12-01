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

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import br.org.acessobrasil.silvinha.entidade.Versao;
/**
 * Verifica a versão local com a on-line 
 *
 */
public class VerificadorDeVersoes {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");
	private String url;
	
	public VerificadorDeVersoes(String url) {
		
		this.url = url;
	}

	public boolean verificarVersao(Versao versaoCliente) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		NameValuePair param = new NameValuePair("param" , "check");
        method.setRequestBody(new NameValuePair[] {param});
        DefaultHttpMethodRetryHandler retryHandler = null;
        retryHandler = new DefaultHttpMethodRetryHandler(0, false);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryHandler);
		try {
			//System.out.println(Silvinha.VERIFICADOR_VERSOES+client.getState().toString());
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}
			// Read the response body.
			byte[] responseBody = method.getResponseBody();
			String rb = new String(responseBody).trim();
			Versao versaoServidor = null;
			try {
				versaoServidor = new Versao(rb);
			} catch (NumberFormatException nfe) {
				return false;
			}
			if (versaoCliente.compareTo(versaoServidor) < 0) {
				AtualizadorDeVersoes av = new AtualizadorDeVersoes(url);
				return av.confirmarAtualizacao();
			} else {
				return false;
			}
		} catch (HttpException he) {
			log.error("Fatal protocol violation: " + he.getMessage(), he);
			return false;
		} catch (IOException ioe) {
			log.error("Fatal transport error: " + ioe.getMessage(), ioe);
			return false;
		} catch (Exception e) {
        	return false;
        } finally {
			//Release the connection.
			method.releaseConnection();
		}

	}


}
