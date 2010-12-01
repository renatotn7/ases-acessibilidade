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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import br.org.acessobrasil.silvinha.entidade.DadosConfig;
import br.org.acessobrasil.silvinha.persistencia.DadosConfigDAOHSSQLImpl;

/**
 * Configuração do software
 * @author Mariano Aloi
 *
 * Construido em 15/08/2005
 *
 */
public class Token {
	
    public static final Border BorderCorrigida;
    public static final Border BorderVazia = BorderFactory.createEmptyBorder();

    static 
    {
        BorderCorrigida = BorderFactory.createLineBorder(Color.red);
    }
	
    /**
     * Remover no final do projeto
     */
    public static final int EMAG_AV_GEN_P1 = 8;
    /**
     * Remover no final do projeto
     */
    public static final int EMAG_AV_GEN_P2 = 3;
    /**
     * Remover no final do projeto
     */
    public static final int EMAG_AV_GEN_P3 = 7;
    /**
     * Remover no final do projeto
     */
    public static final int WCAG_AV_GEN_P1 = 4;
    /**
     * Remover no final do projeto
     */
    public static final int WCAG_AV_GEN_P2 = 5;
    /**
     * Remover no final do projeto
     */
    public static final int WCAG_AV_GEN_P3 = 8;
    
	public static String autServer = "";
    public static String SMTP = "";
    /**
     * Guarda o email do remetente
     */
    public static final String REMETENTE = "silvinha@acessobrasil.org.br";
    
    /**
     * Guarda o email do cliente
     */
    public static String CLIENTE_EMAIL = "";
    
    public static String URL_STRING = ""; 
    public static URL URL;
    
    
    public static final String [] PROTS  = { "mailto", "news", "javascript" };
 
    public static final String [] CMS = {"calandra.nsf"};
	public static final String [] EXTS = {"htm", "html", "asp", "php", "jsp"};
	public static final String [] NAOEXTS =  { "txt", "css", "gif", "jpg", "png", "ico",
        "pdf", "doc", "xls", "rtf", "ps", "zip", "gz", "bz2", "rar", "ppt",
        "avi", "mpg", "mp3", "exe" , "pps", "js", "xsl","xsd","odp","jar" };

    public static String DATA;
    public static boolean ADMIN = true;

    public static File localArquivos;
    //public static String diretorioInicial;
    public static String ADMINISTRADOR;
    
    /**Construtor para a classe Token.java
     * @throws IOException 
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * 
     */
    public Token() throws IOException {
    	
    }

    /**
     * Método que carrega o email e o SMTP do cliente
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @return true caso consiga
     */
    public boolean buscaBanco() throws ClassNotFoundException, SQLException {
    	DadosConfigDAOHSSQLImpl dcdao = new DadosConfigDAOHSSQLImpl();
    	DadosConfig dc = dcdao.consultar();
    	if (dc == null) {
    		return false;
    	} else {
    		SMTP = dc.getSmtp();
    		CLIENTE_EMAIL = dc.getEmail();
    		return true;
    	}
    }
    
    public static void setUrl(String url) {
    	if (url.startsWith("www")) { //note que so coloca http se tiver www no inicio
    		url = "http://" + url;
    	}
    	URL_STRING = url;
    }
    
    protected void finalize() throws Throwable {
    	super.finalize();
    }
    
}


