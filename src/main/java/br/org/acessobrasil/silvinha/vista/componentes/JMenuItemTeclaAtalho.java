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

package br.org.acessobrasil.silvinha.vista.componentes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import br.org.acessobrasil.silvinha2.mli.XHTML_Panel;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;

/**
 * Classe responsável por colocar as teclas de atalho
 * de html
 * @author Fabio Issamu Oshiro
 *
 */
public class JMenuItemTeclaAtalho extends JMenuItem implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ControleTeclasAtalho controle = new ControleTeclasAtalho();
	G_TextAreaSourceCode boxCode;
	public JMenuItemTeclaAtalho(G_TextAreaSourceCode boxCode){
		this.boxCode = boxCode;
		this.setText(XHTML_Panel.COLOCAR_ATALHOS);
		this.addActionListener(this);
		this.setActionCommand("TeclasAtalho");
		this.setToolTipText(XHTML_Panel.DICA_COLOCAR_ATALHOS);
		this.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_CONTRASTE);
	}
	public void actionPerformed(ActionEvent e) {
		try {
			this.boxCode.setText(controle.corrige(this.boxCode.getText()));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(boxCode,e1.getMessage());
			e1.printStackTrace();
		}
	}
	
}
class ControleTeclasAtalho{
	private String codHTMLlow;
	private int iniBody,fimBody;
	public String corrige(String codHTML) throws Exception{
		StringBuilder sb = new StringBuilder();
		codHTMLlow = codHTML.toLowerCase();
		
		//calcula o início e fim do body
		calcIniFimBody();
		
		//coloca as teclas de início
		String teclasIni = teclasInicio();
		String teclasFim = teclasFim();
		sb.append(codHTML.substring(0, iniBody));
		if(codHTML.indexOf(teclasIni)==-1){
			sb.append(teclasIni);
		}
		sb.append(codHTML.substring(iniBody,fimBody));
		if(codHTML.indexOf(teclasFim)==-1){
			sb.append(teclasFim);
		}
		sb.append(codHTML.substring(fimBody));
		return sb.toString();
	}
	private String teclasInicio(){
		return "\n<!-- ini teclas de atalho -->" +
		"\n<div id=\"teclasAtalho\" class=\"teclasAtalho\" >" +
		"\n\t<a href=\"#Aconteudo\" accesskey=\"c\">conte&uacute;do</a> | " +
		"\n\t<a href=\"#Amenu\" accesskey=\"m\">menu principal</a> | " +
		"\n\t<a href=\"#Abusca\" accesskey=\"b\">busca</a> | " +
		"\n\t<a href=\"#Afim\" accesskey=\"f\">fim da p&aacute;gina</a>" +
		"\n</div>"+
		"\n<!-- fim teclas de atalho -->\n"
		;
	}
	private String teclasFim(){
		return "\n<!-- ini teclas de atalho -->" +
		"\n<div style=\"display:none\" >" +
		"\n\t<a href=\"#Aconteudo\" accesskey=\"c\">conte&uacute;do</a> | " +
		"\n\t<a href=\"#Amenu\" accesskey=\"m\">menu principal</a> | " +
		"\n\t<a href=\"#Abusca\" accesskey=\"b\">busca</a> | " +
		"\n\t<a href=\"#Ainicio\" accesskey=\"i\">in&iacute;cio da p&aacute;gina</a>" +
		"\n</div>" +
		"\n<!-- fim teclas de atalho -->\n"
		;
	}
	private void calcIniFimBody() throws Exception{
		iniBody = codHTMLlow.indexOf("<body");
		fimBody = codHTMLlow.lastIndexOf("</body");
		if(iniBody==-1){
			throw new Exception("Não há início do body");
		}
		if(fimBody==-1){
			throw new Exception("Não há fim do body");
		}
		iniBody = codHTMLlow.indexOf('>',iniBody)+1;
	}
}

















