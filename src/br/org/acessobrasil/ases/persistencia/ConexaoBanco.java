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

package br.org.acessobrasil.ases.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * conexão para links
 *
 * Construido em 20/08/2005
 *
 */
public class ConexaoBanco{
    
    Connection con;
    private String nomeSite;
    public static String bancoEmUso="jdbc:derby:temp/mydb;create=true";
  
    /**
     * Construtor para a classe Conexao.java
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * 
     */
    public ConexaoBanco(String nomeSite) throws ClassNotFoundException, SQLException {        
    	Class.forName("org.hsqldb.jdbcDriver");// instancia o driver
    	//Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    	
    	bancoEmUso="jdbc:derby:temp/db_"+nomeSite+";create=true";
    	//con =DriverManager.getConnection(bancoEmUso);
    
    	con=DriverManager.getConnection("jdbc:hsqldb:file:cache/"+nomeSite);
    }
    
    /**
     * Retorna a conexão
     * @return Connection
     */
    public Connection getCon(){        
        return con;
       
    }

	public String getNomeSite() {
		return nomeSite;
	}

}
