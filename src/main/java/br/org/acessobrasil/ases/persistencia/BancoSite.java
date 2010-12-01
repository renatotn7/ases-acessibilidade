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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.org.acessobrasil.silvinha.util.HSQLDB;
import br.org.acessobrasil.silvinha2.util.G_File;

/**
 * Classe que faz a manipulação do banco
 * 
 * @author Renato Tomaz Nati, Fabio Issamu Oshiro, Haroldo Veiga
 * 
 */
public class BancoSite {
	String nomeSite;

	private ConexaoBanco con;

	private Statement st;

	private String sql;

	private String nomeBanco;

	private int incSite;

	private int incPagina;

	private int incErro;

	private boolean closed;
	
	private static boolean stopInsertOnSqlError=true;
	private static boolean hasSqlError=false;

	/**
	 * False = sempre apaga as tabelas
	 */
	public static boolean mantemTabelas = false;

	G_File scriptDb = new G_File("teste/script.sql");

	public BancoSite(String nomeSite) {
		incSite = 1;
		incPagina = 1;
		incErro = 1;
		int posFirstBarra;
		nomeBanco = nomeSite.replaceAll("http://", "");
		posFirstBarra = nomeBanco.indexOf("/");
		if (posFirstBarra != -1) {
			nomeBanco = nomeBanco.substring(0, posFirstBarra);
		}
		abreConexaoOuCriaBanco();
		if (!mantemTabelas) {
			criaTabelas();
		}

		// fechaConexao();
	}

	/**
	 * Insere uma ocorrência na tabela Portal
	 * 
	 * @param nomePortal
	 * @param numErros
	 * @param numAvisos
	 * @param numErrosP1
	 * @param numErrosP2
	 * @param numErrosP3
	 * @param numAvisosP1
	 * @param numAvisosP2
	 * @param numAvisosP3
	 * @return boolean
	 */
	public synchronized boolean insertTabelaPortal(String nomePortal, int numErros, int numAvisos, int numErrosP1, int numErrosP2, int numErrosP3, int numAvisosP1,
			int numAvisosP2, int numAvisosP3) {
		if(stopInsertOnSqlError && hasSqlError) return false;
		String sql = "";
		try {
			HSQLDB.setSt(st);
			sql = "INSERT INTO Portal (nomePortal, numErros, numAvisos, numErrosP1, numErrosP2, numErrosP3, numAvisosP1, numAvisosP2, numAvisosP3) VALUES (" + "'" + nomePortal
					+ "'," + "" + numErros + "," + "" + numAvisos + "," + "" + numErrosP1 + "," + "" + numErrosP2 + "," + "" + numErrosP3 + "," + "" + numAvisosP1 + "," + ""
					+ numAvisosP2 + "," + "" + numAvisosP3 + ") ";
			int resultado = st.executeUpdate(sql);
			ResultSet rs = st.executeQuery("SELECT idPortal, nomePortal from Portal order by idPortal desc");
			if (rs.next()) {
				// System.out.println("idPortal: " + rs.getString("idPortal") +
				// "\n nomePortal: " + rs.getString("nomePortal"));
				incSite = rs.getInt("idPortal");
			}

			if (resultado > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			System.err.println("4erro: " + e.getMessage() + " causa:" + e.getCause());
			System.err.println("\tsql: " + sql);
			hasSqlError = true;
			return false;
		}
	}

	/**
	 * Insere uma ocorrência na tabela pagina
	 * 
	 * @param idPagina
	 * @param nomePagina
	 * @param numErros
	 * @param numAvisos
	 * @param numErrosP1
	 * @param numErrosP2
	 * @param numErrosP3
	 * @param numAvisosP1
	 * @param numAvisosP2
	 * @param numAvisosP3
	 * @return int
	 */
	public synchronized int insertTabelaPagina(int idPagina, String nomePagina, int numErros, int numAvisos, int numErrosP1, int numErrosP2, int numErrosP3, int numAvisosP1,
			int numAvisosP2, int numAvisosP3) {
		String sql = "";
		if(stopInsertOnSqlError && hasSqlError) return incPagina++;
		try {
			HSQLDB.setSt(st);
			sql = "INSERT INTO Pagina ( Portal_idPortal, nomePagina, numErros, numAvisos, numErrosP1, numErrosP2, numErrosP3, numAvisosP1, numAvisosP2, numAvisosP3) VALUES("
					+ incSite + "," + "'" + nomePagina.replace("'", "''") + "'," + "" + numErros + "," + "" + numAvisos + "," + "" + numErrosP1 + "," + "" + numErrosP2 + "," + ""
					+ numErrosP3 + "," + "" + numAvisosP1 + "," + "" + numAvisosP2 + "," + "" + numAvisosP3 + ") ";
			st.executeUpdate(sql);
			return incPagina++;
		} catch (Exception e) {
			System.err.println("3erro insert Página: " + e.getMessage() + " causa:'" + e.getCause() + "'");
			System.err.println("\tsql:" + sql);
			e.printStackTrace();
			hasSqlError = true;
			// System.exit(1);
			return -1;
		}
	}

	/**
	 * Chamado no Processa erro<br>
	 * Insere um erro na tabela de erros
	 */
	public synchronized void insertTabelaErro(String codRegra, int idPagina, int prioridade, String tag, int linha, int coluna, String tagName, char erroOuAviso) {
		if(stopInsertOnSqlError && hasSqlError) return;
		StringBuffer sb = new StringBuffer();
		try {
			// con = new ConexaoBanco(nomeBanco);
			HSQLDB.setSt(st);
			// st = con.getCon().createStatement();
			// *

			sb.append("INSERT INTO Erro (Pagina_Portal_idPortal ,Pagina_idPagina ,codRegra, prioridade, tag, linha, coluna, tagName, erroOuAviso) VALUES(");
			sb.append(incSite);
			sb.append(",");
			sb.append(idPagina);
			sb.append(",'");
			sb.append(codRegra);
			sb.append("',");
			sb.append(prioridade);
			sb.append(",'");
			sb.append(tag);
			sb.append("',");
			sb.append(linha);
			sb.append(",");
			sb.append(coluna);
			sb.append(",'");
			sb.append(tagName.replaceAll("'","''"));
			sb.append("','");
			sb.append(erroOuAviso);
			sb.append("')");
			st.executeUpdate(sb.toString());
			incErro++;
		} catch (NullPointerException e) {
		} catch (Exception e) {
			System.err.println("2erro: " + e.getMessage() + " causa:" + e.getCause());
			System.err.println("\tsql: " + sb.toString());
			e.printStackTrace();
			hasSqlError = true;
			// System.exit(1);
			// return false;
		}// */

	}

	/**
	 * Abre a conexão com o banco caso ele não exista o banco é criado
	 */
	protected void abreConexaoOuCriaBanco() {
		try {
			System.out.println("abreConexao");
			sql = new String();
			con = new ConexaoBanco(nomeBanco);
			st = con.getCon().createStatement();
			HSQLDB.setSt(st);
			// st.executeUpdate("SET WRITE_DELAY FALSE");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Drop tabela Erro, Pagina e Portal
	 */
	private void destroiTabelas() {
		try {
			st.execute("DROP TABLE Erro");
			st.execute("DROP TABLE Pagina");
			st.execute("DROP TABLE Portal");
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Cria as tabelas Portal, Pagina e Erro
	 */
	private void criaTabelas() {
		try {
			destroiTabelas();
			System.out.println("criaTabelas()");
			// sql = "CREATE SCHEMA ASES";
			// st.executeUpdate(sql);
			// st.executeUpdate("SET SCHEMA ASES");
			sql = "CREATE TABLE Portal (idPortal INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),nomePortal VARCHAR(2000) NOT NULL,numErros INTEGER NOT NULL,numAvisos INTEGER NOT NULL,numErrosP1 INTEGER NOT NULL,numErrosP2 INTEGER NOT NULL,numErrosP3 INTEGER NOT NULL,numAvisosP1 INTEGER NOT NULL, numAvisosP2 INTEGER NOT NULL, numAvisosP3 INTEGER NOT NULL, PRIMARY KEY(idPortal))";
			// "CREATE TABLE Portal (idPortal INTEGER NOT NULL ,nomePortal
			// VARCHAR(255) NULL,numErros INTEGER NULL,numAvisos INTEGER
			// NULL,numErrosP1 INTEGER NULL,numErrosP2 INTEGER NULL,numErrosP3
			// INTEGER NULL,numAvisosP1 INTEGER NULL, numAvisosP2 INTEGER NULL,
			// numAvisosP3 INTEGER NULL, PRIMARY KEY(idPortal))";
			st.executeUpdate(sql);

			// sql = "CREATE TABLE Pagina (idPagina INTEGER NOT
			// NULL,Portal_idPortal INTEGER NOT NULL,nomePagina VARCHAR(255)
			// NULL,numErros INTEGER NULL,numAvisos INTEGER NULL,numErrosP1
			// INTEGER NULL,numErrosP2 INTEGER NULL,numErrosP3 INTEGER
			// NULL,numAvisosP1 INTEGER NULL,numAvisosP2 INTEGER
			// NULL,numAvisosP3 INTEGER NULL,PRIMARY KEY(idPagina),FOREIGN
			// KEY(Portal_idPortal) REFERENCES Portal(idPortal));";
			// melhor guardar o nome do arquivo ao inves do arquivo.
			// sql = "CREATE TABLE Pagina (idPagina INTEGER NOT NULL
			// ,Portal_idPortal INTEGER NOT NULL,nomePagina VARCHAR(2000) NOT
			// NULL,numErros INTEGER NOT NULL,numAvisos INTEGER NOT
			// NULL,numErrosP1 INTEGER NOT NULL,numErrosP2 INTEGER NOT
			// NULL,numErrosP3 INTEGER NOT NULL,numAvisosP1 INTEGER NOT
			// NULL,numAvisosP2 INTEGER NOT NULL,numAvisosP3 INTEGER NOT
			// NULL,hashCode VARCHAR(30),PRIMARY KEY(idPagina),FOREIGN
			// KEY(Portal_idPortal) REFERENCES Portal(idPortal))";
			sql = "CREATE TABLE Pagina (idPagina INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),Portal_idPortal INTEGER  NOT NULL,nomePagina VARCHAR(2000) NOT NULL,numErros INTEGER  NOT NULL,numAvisos INTEGER  NOT NULL,numErrosP1 INTEGER  NOT NULL,numErrosP2 INTEGER  NOT NULL,numErrosP3 INTEGER  NOT NULL,numAvisosP1 INTEGER  NOT NULL,numAvisosP2 INTEGER  NOT NULL,numAvisosP3 INTEGER  NOT NULL,hashCode VARCHAR(30),PRIMARY KEY(idPagina),FOREIGN KEY(Portal_idPortal) REFERENCES Portal(idPortal))";

			st.executeUpdate(sql);

			sql = "CREATE TABLE Erro (idErro INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),Pagina_Portal_idPortal INTEGER NOT NULL,Pagina_idPagina INTEGER NOT NULL,codRegra VARCHAR(255) NOT NULL,prioridade INTEGER NOT NULL,tag VARCHAR(2000) NOT NULL,linha INTEGER NOT NULL,coluna INTEGER NOT NULL,tagName VARCHAR(2000) NOT NULL,erroOuAviso CHAR(1),PRIMARY KEY(idErro),FOREIGN KEY(Pagina_idPagina) REFERENCES Pagina(idPagina),FOREIGN KEY(Pagina_Portal_idPortal) REFERENCES Portal(idPortal))";
			// sql = "CREATE TABLE Erro (idErro INTEGER NOT NULL GENERATED
			// ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY
			// 1),Pagina_Portal_idPortal INTEGER NOT NULL,Pagina_idPagina
			// INTEGER NOT NULL,codRegra VARCHAR(255) NOT NULL,prioridade
			// INTEGER NOT NULL,tag VARCHAR(2000) NOT NULL,linha INTEGER NOT
			// NULL,coluna INTEGER NOT NULL,tagName VARCHAR(2000) NOT
			// NULL,erroOuAviso CHAR(1),PRIMARY KEY(idErro))";
			// sql = "CREATE TABLE Erro (idErro INTEGER NOT NULL GENERATED
			// ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY
			// 1),Pagina_Portal_idPortal INTEGER NOT NULL,Pagina_idPagina
			// INTEGER NOT NULL,codRegra VARCHAR(255) NOT NULL,prioridade
			// INTEGER NOT NULL,tag VARCHAR(2000) NOT NULL,linha INTEGER NOT
			// NULL,coluna INTEGER NOT NULL,tagName VARCHAR(2000) NOT
			// NULL,erroOuAviso CHAR(1))";
			// sql = "CREATE TABLE Erro (idErro INTEGER,Pagina_Portal_idPortal
			// INTEGER NOT NULL,Pagina_idPagina INTEGER NOT NULL,codRegra
			// VARCHAR(255) NOT NULL,prioridade INTEGER NOT NULL,tag
			// VARCHAR(2000) NOT NULL,linha INTEGER NOT NULL,coluna INTEGER NOT
			// NULL,tagName VARCHAR(2000) NOT NULL,erroOuAviso CHAR(1))";
			st.executeUpdate(sql);// */
			// teste

		} catch (Exception e) {

			System.err.println("erro: " + e.getMessage() + " causa:" + e.getCause());
		}
	}

	/**
	 * Retorna o total de erros de um site
	 * 
	 * @param idPortal
	 * @param prioridade
	 * @param erroOuAviso
	 *            <code>e</code> para erro e <code>a</code> para aviso
	 * @return
	 * @throws SQLException
	 */
	public int getErrosSite(int idPortal, int prioridade, char erroOuAviso) throws SQLException {
		String sql;
		sql = "Select count(idErro) as total From Erro where Pagina_Portal_idPortal=" + idPortal + " and prioridade=" + prioridade + " and erroOuAviso = '" + erroOuAviso + "'";
		st.execute(sql);
		ResultSet rs = st.getResultSet();
		rs.next();
		return rs.getInt("total");
	}

	/**
	 * Retorna o último indice da tabela site
	 * 
	 * @return
	 */
	public int getIncSite() {
		return incSite;
	}

	/**
	 * Verifica se a conexão está fechada ou não
	 * 
	 * @return boolean
	 */
	public boolean isClosed() {
		try {
			closed = con.getCon().isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return closed;
	}

	/**
	 * Retorna a conexão corrente
	 * 
	 * @return ConexaoBanco
	 */
	public ConexaoBanco getCon() {
		return con;
	}

	/**
	 * Fecha a conexão com o banco
	 */
	public void closeConn() {
		try {
			HSQLDB.setSt(st);
			if(st!=null) st.close();
			con.getCon().commit();
			con.getCon().close();
			st = null;
			con = null;
			HSQLDB.setSt(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Destroi a tabela imagem e cria uma nova
	 */
	public void criaTabelaImagem() {
		// destroi a tabela imagem anterior e cria uma nova
		Statement stTabImg;
		try {
			stTabImg = con.getCon().createStatement();

			try {
				stTabImg.execute("DROP TABLE imagem");
			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			try {
				stTabImg
						.execute("CREATE TABLE imagem(idImagem INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),idPagina INTEGER NOT NULL, endPagina VARCHAR(2000),tag VARCHAR(2000) NOT NULL,endTag VARCHAR(2000), linha INTEGER, coluna INTEGER)");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna o total de imagem com este nome
	 * 
	 * @param nomeDaImagem
	 * @return int
	 * @throws SQLException
	 */
	public int getTotalImagem(String nomeDaImagem) throws SQLException {
		Statement stConsultTabErrEPag = con.getCon().createStatement();
		ResultSet rs = stConsultTabErrEPag.executeQuery("SELECT count(p.idPagina) as total from Pagina p INNER JOIN Erro e ON "
				+ "(p.idPagina=e.Pagina_idPagina) WHERE tagName = 'img' AND ((codRegra = '1.11') OR (codRegra = '1.1'))" + "  and tag like '%" + nomeDaImagem.replaceAll("'", "''")
				+ "%'");
		rs.next();
		int total = rs.getInt("total");
		return total;
	}

	/**
	 * Retorna as ocorrências de erro com esta imagem
	 * 
	 * @param nomeDaImagem
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getImagensErro(String nomeDaImagem) throws SQLException {
		Statement stConsultTabErrEPag = con.getCon().createStatement();
		return stConsultTabErrEPag.executeQuery("SELECT * from Pagina p INNER JOIN Erro e ON "
				+ "(p.idPagina=e.Pagina_idPagina) WHERE tagName = 'img' AND ((codRegra = '1.11') OR (codRegra = '1.1'))" + "  and tag like '%" + nomeDaImagem.replaceAll("'", "''")
				+ "%' order by p.nomePagina");
	}

	/**
	 * Apaga os registros da tabela imagem
	 * 
	 * @throws SQLException
	 */
	public void delTabelaImagem() throws SQLException {
		Statement stDel = con.getCon().createStatement();
		stDel.executeUpdate("Delete from imagem");
	}

	/**
	 * Insere uma ocorrência de erro em imagem
	 * 
	 * @param endPagina
	 * @param idPagina
	 * @param tag
	 * @param endTag
	 * @param linha
	 * @param coluna
	 * @throws SQLException
	 */
	public void insertTabImagem(String endPagina, int idPagina, String tag, String endTag, int linha, int coluna) throws SQLException {
		Statement stInsereEmTabImg = con.getCon().createStatement();
		stInsereEmTabImg.executeUpdate("INSERT INTO imagem(endPagina,idPagina,tag,endTag,linha,coluna) VALUES('" + endPagina.replaceAll("'", "''") + "', " + idPagina + ", '"
				+ tag.replaceAll("'", "''") + "', '" + endTag.replaceAll("'", "''") + "', " + linha + ", " + coluna + ")");
	}

	/**
	 * Retorna os portais
	 * 
	 * @return resultset com todos os portais
	 * @throws SQLException
	 */
	public ResultSet getPortais() throws SQLException {
		Statement st = con.getCon().createStatement();
		return st.executeQuery("Select * From Portal");
	}
}
