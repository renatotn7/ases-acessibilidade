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
package br.org.acessobrasil.ases.ferramentas_de_reparo.controle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.css.css.StyleSheet;
import org.w3c.css.css.StyleSheetParser;
import org.w3c.css.util.ApplContext;

import br.org.acessobrasil.ases.ferramentas_de_reparo.modelo.AvaliacaoCSS;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;

/**
 * Avalia um documento CSS 
 *
 */
public class ControleCss extends Thread {
	ControleCssListener listener;
	private String cssURL,url;
	public ControleCss(ControleCssListener listener){
		this.listener = listener;
	}
	/**
	 * Avalia o arquivo CSS on-line <br />
	 * Realiza a avaliação em uma thread
	 * @param cssURL caminho do CSS
	 * @param url referencia
	 */
	public void avalia(String cssURL,String url){
		this.cssURL = cssURL;
		this.url = url;
		this.run();
	}
	public void run(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		avalia();
	}
	private void avalia(){
		/*
		 * Trata a url
		 */
		if (cssURL.indexOf("http://") == -1) {
			while (cssURL.startsWith("/")) {
				cssURL = cssURL.substring(1);
			}
			if (!url.endsWith("/")) {
				cssURL = url + "/" + cssURL;
			} else {
				cssURL = url + cssURL;
			}
		}
		/*
		 * Pega o código 
		 */
		PegarPaginaWEB ppw = new PegarPaginaWEB();
		String codCSS="";
		try {
			codCSS = ppw.getCssContent(cssURL);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		/*
		 * Avalia
		 */
		AvaliacaoCSS resultado = doAval(codCSS);
		resultado.setUrl(cssURL);
		/*
		 * Informa o resultado
		 */
		listener.avaliacaoCssRealizada(resultado);
	}
	private AvaliacaoCSS doAval(String codFonte){
		StyleSheetParser parser;
		String usermedium = "all";
		// String text = new G_File("C:\\temp\\teste.css").read();

		String fileName = "TextArea";
		fileName = "file://localhost/" + fileName;

		InputStream is = new ByteArrayInputStream(codFonte.getBytes());

		// needed by the CSS parser
		ApplContext ac = null;

		/*
		 * Configura o idioma
		 */
		if (TokenLang.LANG.equals("pt")) {
			// os PL que nos desculpem
			ac = new ApplContext("pl-PL");
		} else {
			ac = new ApplContext(TokenLang.LANG);
		}
		// All
		ac.setWarningLevel(2);
		// ac.setProfile(profile);
		ac.setCssVersion("css3");
		// ac.setCssVersion("css21");
		parser = new StyleSheetParser();
		StyleSheet css = null;
		try {
			parser.parseStyleElement(ac, is, null, usermedium, new URL(fileName), 0);
			css = parser.getStyleSheet();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		AvaliacaoCSS retorno = new AvaliacaoCSS();
		retorno.setAvisos(css.getWarnings());
		retorno.setErros(css.getErrors());
		return retorno;
	}
}
