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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.analise_geral;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import br.org.acessobrasil.ases.persistencia.ConexaoBanco;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.projetodosite.ProjetoDoSite;
import br.org.acessobrasil.silvinha2.util.G_Cronometro;
import br.org.acessobrasil.silvinha2.util.G_File;

/**
 * Tabela que mostra os erros do analise geral
 */
class TabelaAnaliseGeral extends JTable implements MouseListener {

	private static final long serialVersionUID = 1L;

	private String hashCodeAtual;

	public String paginaAtual;

	private int linhaAtual;

	private int numLinhas;

	/**
	 * 
	 */
	private final PanelAnaliseGeral panelDescricaoImagens;

	private ArrayList<String> enderecos = new ArrayList<String>();

	TabelaAnaliseGeral(PanelAnaliseGeral imagens, ArrayList<FerramentaAnaliseGeralModel> erros) {
		this.panelDescricaoImagens = imagens;
		initComponents(erros);

	}

	void initComponents(ArrayList<FerramentaAnaliseGeralModel> erros) {
		numLinhas = 0;
		paginaAtual = new String();
		this.panelDescricaoImagens.dtm = new DefaulTableModelNotEditable();
		this.panelDescricaoImagens.dtm.setColumnIdentifiers(new String[] { "Endereço", GERAL.LINHA, GERAL.COLUNA });
		this.addMouseListener(this);
		setModel(this.panelDescricaoImagens.dtm);
		{
			TableColumnModel cm = getColumnModel();
			cm.getColumn(1).setMaxWidth(50);
			cm.getColumn(2).setMaxWidth(50);
			// cm.getColumn(2).setMaxWidth(600);
		}

		String end = "";
		for (FerramentaAnaliseGeralModel flm : erros) {
			addLinha(flm.getLinha(), flm.getColuna(), flm.getEndereco());

			if (!flm.getEndereco().equals(end)) {
				enderecos.add(flm.getEndereco());
			}
			end = flm.getEndereco();
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Renato Nati
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public void clearTab() {
		for (int i = 0; i < this.panelDescricaoImagens.dtm.getRowCount(); i++)
			this.panelDescricaoImagens.dtm.removeRow(i);
	}

	public void addLinha(int linha, int coluna, String codigo) {
		numLinhas++;
		this.panelDescricaoImagens.dtm.addRow(new Object[] { codigo, linha, coluna });

	}

	public void delAtualLinha() {
		try{
			numLinhas--;
			int linhaAtual = this.getLinhaAtual();
			if(this.panelDescricaoImagens.dtm.getRowCount()>linhaAtual){
				this.panelDescricaoImagens.dtm.removeRow(linhaAtual);
			}
			if (numLinhas > 0) {
				System.err.println("numero total de linhas:" + numLinhas);
				mostraPrimLinha();
				System.err.println("numero total de linhas:" + numLinhas);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void selectTag() {

	}

	/**
	 * Mostra a primeira linha da tabela
	 * 
	 */
	public void mostraPrimLinha() {
		// this.panelDescricaoImagens.parentFrame.setCursor(new
		// Cursor(Cursor.WAIT_CURSOR));
		// this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().selectAll();
	//	this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(new Color(255, 255, 255), new Color(0, 0, 0));
		// this.panelDescricaoImagens.scrollPaneDescricao.coloreSource();

		/*
		 * Verifica se existe alguma linha na tabela
		 */
		if(this.getRowCount()==0){
			return;
		}
		this.panelDescricaoImagens.tArParticipRotulo.apagaTexto();
		this.panelDescricaoImagens.aplicar.setEnabled(true);
		this.panelDescricaoImagens.analiseSistematica.setEnabled(true);
		linhaAtual = 0;
		
		int linha = (Integer) this.panelDescricaoImagens.dtm.getValueAt(0, 1);
		int coluna = (Integer) this.panelDescricaoImagens.dtm.getValueAt(0, 2);
		String endereco = (String) this.panelDescricaoImagens.dtm.getValueAt(0, 0);

		try {

			Statement stConsultaTabPag = null;

			Connection con = (DriverManager.getConnection(ConexaoBanco.bancoEmUso));
			ResultSet rs2 = null;
			if (!paginaAtual.equals(endereco)) {
				stConsultaTabPag = con.createStatement();

				System.out.println("4");

				rs2 = stConsultaTabPag.executeQuery("SELECT * from Pagina WHERE nomePagina='" + endereco + "'"); //
				StringBuilder buffer = new StringBuilder();
				// + endImagem(tag, endPagina) + "'");
				System.out.println("5");
				if (rs2.next()) {

					String hashCodeAtual = rs2.getString("hashCode");
					this.hashCodeAtual = hashCodeAtual;
					if (endereco.equals(panelDescricaoImagens.enderecoPagina)) {
						this.panelDescricaoImagens.hashCodeInicial = this.hashCodeAtual;
					}
					System.out.println("tabela hashcodeAtualBanco:" + this.hashCodeAtual);
					buffer = trabComTemp();

					System.out.println("9");
					// System.out.println("tabela Analise geral:
					// "+buffer.toSting());
					this.panelDescricaoImagens.boxCode.getTextPane().setText(buffer.toString());
					// System.out.println("tabela Analise geral:
					// "+rs2.getClob("source").toString());

				}
			}
			System.out.println("10");
			// *
			int endTag = 0;
			int posAtual = 0;
			int posFinal = 0;
			String codHTML = this.panelDescricaoImagens.boxCode.getTextPane().getText().replace("\r", "");
			int i;
			for (i = 0; i < (linha - 1); i++) {
				posAtual = codHTML.indexOf("\n", posAtual + 1);
			}
			i = 0;
			// gambiarra provisória
			System.out.println("11");
			Statement stConsultaTabImg = con.createStatement();
			System.out.println("linha: " + linha + " coluna: " + coluna + " endereco: " + endereco);

			rs2 = stConsultaTabImg.executeQuery("SELECT * from imagem WHERE endPagina='" + endereco + "' AND linha=" + linha + " AND coluna=" + coluna); //
			if (rs2.next()) {
				System.out.println("12");
				posFinal = codHTML.indexOf(rs2.getString("tag"), posAtual + coluna);
				System.out.println("13");
			}else{
				throw new Exception("Imagem não encontrada: "+endereco);
			}
			if(posFinal==-1){
				posFinal = codHTML.indexOf(rs2.getString("tag"));
			}
			if(posFinal==-1){
				throw new Exception("Índice da tag não encontrada:"+rs2.getString("tag")+" posAtual + coluna="+(posAtual + coluna)
						+"\nCódigo:" + codHTML
				);
			}
			while (codHTML.charAt(posFinal + i) != '>') {
				i++;
			}
			System.out.println("14");
			this.panelDescricaoImagens.setPosTagRepInit(posFinal);
			this.panelDescricaoImagens.setPosTagRepEnd(posFinal + i + 1);
			System.out.println("15");
			this.panelDescricaoImagens.boxCode.goToLine(linha);
			this.panelDescricaoImagens.boxCode.getTextPane().select(this.panelDescricaoImagens.getPosTagRepInit(), this.panelDescricaoImagens.getPosTagRepEnd());
			this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(Color.decode("0xEEEEEE"), new Color(255, 0, 0));
			this.panelDescricaoImagens.arTextPainelCorrecao.setUnderline();
			System.out.println("16");
			paginaAtual = endereco;
			/*
			 * /
			 * this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().select(200,250); //
			 */
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			System.out.println("1");
			this.panelDescricaoImagens.parentFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			// this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().selectAll();
			this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(new Color(255, 255, 255), new Color(0, 0, 0));
			// this.panelDescricaoImagens.scrollPaneDescricao.coloreSource();

			this.panelDescricaoImagens.tArParticipRotulo.apagaTexto();
			if(this.panelDescricaoImagens.conteudoDoAlt.getText().length()>0){
				panelDescricaoImagens.aplicarPag.setEnabled(true);
				panelDescricaoImagens.aplicarTod.setEnabled(true);}
			this.panelDescricaoImagens.analiseSistematica.setEnabled(true);
			System.out.println("2");
			TabelaAnaliseGeral tcl = ((TabelaAnaliseGeral) e.getComponent());
			linhaAtual = tcl.getSelectedRow();
			int linha = (Integer) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 1);
			int coluna = (Integer) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 2);
			String endereco = (String) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 0);

			System.out.println("3");
			try {
				Statement stConsultaTabPag = null;

				Connection con = (DriverManager.getConnection(ConexaoBanco.bancoEmUso));

				stConsultaTabPag = con.createStatement();
				ResultSet rs2 = null;
				System.out.println("4");
				if (!paginaAtual.equals(endereco)) {
					rs2 = stConsultaTabPag.executeQuery("SELECT * from Pagina WHERE nomePagina='" + endereco + "'"); //
					StringBuilder buffer = new StringBuilder();
					// + endImagem(tag, endPagina) + "'");
					System.out.println("5");
					if (rs2.next()) {
						String hashCodeAtual = rs2.getString("hashCode");
						this.hashCodeAtual = hashCodeAtual;
						if (endereco.equals(panelDescricaoImagens.enderecoPagina)) {
							this.panelDescricaoImagens.hashCodeInicial = this.hashCodeAtual;
						}
						System.out.println("tabela hashcodeAtualBanco:" + this.hashCodeAtual);
						buffer = trabComTemp();

						System.out.println("9");
						this.panelDescricaoImagens.boxCode.setText(buffer.toString());
					}
				}
				System.out.println("10");
				// *
				int posAtual = 0;
				int posFinal = 0;
				String codHTML = this.panelDescricaoImagens.boxCode.getTextPane().getText().replace("\r", "");
				int i;
				for (i = 0; i < (linha - 1); i++) {
					posAtual = codHTML.indexOf("\n", posAtual + 1);
				}
				i = 0;

				System.out.println("11");
				Statement stConsultaTabImg = con.createStatement();
				System.out.println("linha: " + linha + " coluna: " + coluna + " endereco: " + endereco);
				rs2 = stConsultaTabImg.executeQuery("SELECT * from imagem WHERE endPagina='" + endereco + "' AND linha=" + linha + " AND coluna=" + coluna); //
				if (rs2.next()) {
					System.out.println("12");
					posFinal = codHTML.indexOf(rs2.getString("tag"), posAtual + coluna);
					System.out.println("13");
				}
				// Adaptação provisória
				while (codHTML.charAt(posFinal + i) != '>') {
					i++;
				}
				System.out.println("14");
				this.panelDescricaoImagens.setPosTagRepInit(posFinal);
				this.panelDescricaoImagens.setPosTagRepEnd(posFinal + i + 1);
				System.out.println("15");
				this.panelDescricaoImagens.boxCode.goToLine(linha);
				this.panelDescricaoImagens.boxCode.getTextPane().select(this.panelDescricaoImagens.getPosTagRepInit(), this.panelDescricaoImagens.getPosTagRepEnd());
				this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(Color.decode("0xEEEEEE"), new Color(255, 0, 0));
				this.panelDescricaoImagens.arTextPainelCorrecao.setUnderline();
				System.out.println("16");
				paginaAtual = endereco;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			this.panelDescricaoImagens.parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private StringBuilder trabComTemp() {
		StringBuilder buffer;
		if (!(new File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + this.hashCodeAtual).exists())) {
			buffer = getConteudo(RelatorioDaUrl.pathHD + this.hashCodeAtual);
			new G_File("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + this.hashCodeAtual).write(buffer.toString());
			System.err.println("arquivo não temporario em uso");
		} else {

			buffer = getConteudo("temp/"+ProjetoDoSite.getNomeDoProjeto() + "/reparo/temp/" + this.hashCodeAtual);
			System.err.println("arquivo temporario em uso");

		}
		return buffer;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public StringBuilder getConteudo(String endArq) {

		G_File file = new G_File(endArq);
		StringBuilder sbd = new StringBuilder(file.read());

		return sbd;

	}

	public String getHashCodeAtual() {
		return hashCodeAtual;
	}

	public void setHashCodeAtual(String hashCodeAtual) {
		this.hashCodeAtual = hashCodeAtual;
	}

	public ArrayList<String> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(ArrayList<String> enderecos) {
		this.enderecos = enderecos;
	}

	public int getLinhaAtual() {
		return linhaAtual;
	}

	public void setLinhaAtual(int linhaAtual) {
		this.linhaAtual = linhaAtual;
	}

	public int getNumLinhas() {
		return numLinhas;
	}

	public void setNumLinhas(int numLinhas) {
		this.numLinhas = numLinhas;
	}
}

class SetImage extends Thread {

	private PanelAnaliseGeral ferramentaDescricaoImagens;

	private String fullUrl;

	public SetImage(PanelAnaliseGeral ferramentaDescricaoImagens, String url) {
		this.ferramentaDescricaoImagens = ferramentaDescricaoImagens;
		if(url==null) url="";
		this.fullUrl = url;
		if (fullUrl.toLowerCase().indexOf("c:/") != -1) {
			this.fullUrl = "file:///" + this.fullUrl;// .substring(3,fullUrl.length());
		}
	}

	public void run() {
		G_File arq = new G_File("temp/img.html");
		String myHTML = "<html><head></head><body><img src=\"" + fullUrl + "\" /></body></html>";
		arq.write(myHTML);
		try {
			G_Cronometro cro = new G_Cronometro();
			cro.start();
			this.ferramentaDescricaoImagens.imagemSemDesc.setDocument(arq.getFile());
			// this.ferramentaDescricaoImagens.imagemSemDesc.setPage("file:///"+arq.getFile().getAbsolutePath());
			System.out.println("fullUrl " + fullUrl);
			// System.out.println("visivel " +
			// this.ferramentaDescricaoImagens.imagemSemDesc.isVisible());
			// System.out.println("isDisplayable " +
			// this.ferramentaDescricaoImagens.imagemSemDesc.isDisplayable());
			// this.ferramentaDescricaoImagens.parentFrame.setVisible(true);
			this.ferramentaDescricaoImagens.parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			cro.stop("Imagem carregada");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
