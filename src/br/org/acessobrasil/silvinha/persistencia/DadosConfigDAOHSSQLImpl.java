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

package br.org.acessobrasil.silvinha.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import br.org.acessobrasil.silvinha.entidade.DadosConfig;
import br.org.acessobrasil.silvinha.util.ConexaoPropriedade;
import br.org.acessobrasil.silvinha2.util.G_Cronometro;

/**
 * Implementação
 * 
 */
public class DadosConfigDAOHSSQLImpl implements DadosConfigDAO {

	private static Logger log = Logger
			.getLogger("br.org.acessobrasil.silvinha");
	private ConexaoPropriedade con;
	private Statement st;
	private String sql;

	public DadosConfigDAOHSSQLImpl() throws ClassNotFoundException,
			SQLException {
		// sql = new String();
		// con = new ConexaoPropriedade();
		// st = con.getCon().createStatement();
		// HSQLDB.setSt(st);
	}

	public boolean inserir(DadosConfig dados) throws SQLException,
			ClassNotFoundException {
		DadosConfig dc = consultar();
		if (dc == null) {
			return insert(dados);
		} else {
			return update(dados);
		}
	}

	public DadosConfig consultar() {
		DadosConfig dc = null;
		for (int i = 0; i < 7; i++) {
			try {
				ConexaoPropriedade con = new ConexaoPropriedade();
				Statement st = con.getCon().createStatement();
				String sql = "SELECT * FROM t2;";
				ResultSet rs = st.executeQuery(sql);
				st.close();
				con.getCon().close();
				dc = processaRs(rs);
				//teste de velocidade do banco
				//testBanco();
				return dc;
			} catch (SQLException e) {
				//System.out.println("Tentativa " + (i + 1) + " falhou.");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return dc;
	}

	private void testBanco() throws ClassNotFoundException, SQLException{
		G_Cronometro cro = new G_Cronometro();
		ConexaoPropriedade con = new ConexaoPropriedade();
		Statement st = con.getCon().createStatement();
		cro.start();
		for(int i=0;i<5000;i++){
			String sql = "INSERT INTO ACESSIBILIDADE_ERRO (TAG, CODIGO, LINHA) VALUES ("
					+ "'"
					+ "<img src="
					+ "',"
					+ "'"
					+ "e"+i
					+ "'," + i +
					"); "; // diretorio inicial
			//int resultado = st.executeUpdate(sql);
			sql = "INSERT INTO ACESSIBILIDADE_PAGINA (URL) VALUES ('URL" + i +" " + i + "')";
			//resultado = st.executeUpdate(sql);
		}
		cro.stop("Inserido");
		cro.start();
		String sql = "SELECT count(*) FROM ACESSIBILIDADE_PAGINA WHERE URL LIKE 'URL%1%' OR URL LIKE 'U%0%';";
		ResultSet rs = st.executeQuery(sql);
		int total=0;
		if(rs.next()){
			total=rs.getInt(1);
			System.out.println("Total="+rs.getInt(1));
		}
		cro.stop("Consultado");
		
		if(total>3000){
			cro.start();
			st.executeUpdate("DELETE FROM ACESSIBILIDADE_ERRO");
			st.executeUpdate("DELETE FROM ACESSIBILIDADE_PAGINA");
			cro.stop("Apagado");
		}
		
		st.close();
		con.close();
	}
	
	private DadosConfig processaRs(ResultSet rs) throws SQLException {
		DadosConfig dc = null;
		if (rs.next()) {
			dc = new DadosConfig();
			dc.setAdmin(rs.getString(1));
			dc.setSmtp(rs.getString(2));
			dc.setEmail(rs.getString(3));
		}

		return dc;
	}

	private boolean update(DadosConfig dados) {
		try {
			ConexaoPropriedade con = new ConexaoPropriedade();
			Statement st = con.getCon().createStatement();
			String sql = "UPDATE t2 set " + "t1  = '" + dados.getAdmin() + "',"
					+ "t2  = '" + dados.getSmtp() + "'," + "t3  = '"
					+ dados.getEmail() + "'," + "t4  = ''," + // horario
					"t5  = ''," + // tipo avaliação
					"t6  = ''," + // tipo iteração
					"t7  = ''," + // tipo resultado
					"t10 = '';"; // diretorio inicial
			int resultado = st.executeUpdate(sql);
			st.close();
			con.close();
			if (resultado > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			log.fatal(e.getMessage(), e);
			return false;
		}
	}

	private boolean insert(DadosConfig dados) throws SQLException {
		try {
			ConexaoPropriedade con = new ConexaoPropriedade();
			Statement st = con.getCon().createStatement();
			String sql = "INSERT INTO t2 (t1, t2, t3, t4, t5, t6, t7, t10) VALUES ("
					+ "'"
					+ dados.getAdmin()
					+ "',"
					+ "'"
					+ dados.getSmtp()
					+ "'," + "'" + dados.getEmail() + "'," + "''," + // horario
					"''," + // tipo de avaliação
					"''," + // tipo de iteração
					"''," + // tipo de resultado
					"''); "; // diretorio inicial
			int resultado = st.executeUpdate(sql);
			st.close();
			con.close();
			if (resultado > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			log.fatal(e.getMessage(), e);
			return false;
		}
	}

}
