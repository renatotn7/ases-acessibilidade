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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.nucleo.InterfClienteDoNucleo;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.persistencia.ConexaoBanco;
import br.org.acessobrasil.silvinha.util.AlteradorDeCsv;

/**
 * Controla a análise geral 
 */
class FerramentaAnaliseGeralCtrl implements InterfClienteDoNucleo {

	/**
	 * 
	 */
	private InterfNucleos nucleo;

	public ArrayList<FerramentaAnaliseGeralModel> posicaoeTag;
	
	ArrayList<ArmazenaErroOuAviso> errosP1;

	private String hashCode;

	AlteradorDeCsv alteradorDeCsv;

	public FerramentaAnaliseGeralCtrl(String conteudo, String hashCode) {

		this.hashCode = hashCode;
		if (conteudo == null)
			return;
		nucleo = MethodFactNucleos.mFNucleos("Estruturado");

		alteradorDeCsv = new AlteradorDeCsv(hashCode);
		nucleo.setCodHTML(conteudo);

		if (EstadoSilvinha.orgao == 2) {
			nucleo.setWCAGEMAG(InterfNucleos.EMAG);
		} else {
			nucleo.setWCAGEMAG(InterfNucleos.WCAG);
		}

		nucleo.addCliente(this);
		nucleo.avalia();

	}
	
	/**
	 * Inicia o arrayList de posicao e tag
	 *
	 */
	public void initPosicaoTag(){
		posicaoeTag = new ArrayList<FerramentaAnaliseGeralModel>();
		Statement stConsultaTabImg = null;
		try {
			Connection con = (DriverManager.getConnection(ConexaoBanco.bancoEmUso));

			stConsultaTabImg = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			ResultSet rs2 = stConsultaTabImg.executeQuery("SELECT * from imagem "); // "WHERE
			while (rs2.next()) {
				posicaoeTag.add(new FerramentaAnaliseGeralModel(rs2.getInt("linha"), rs2.getInt("coluna"), rs2.getString("endPagina")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * public FerramentaAnaliseGeralCtrl(PanelAnaliseGeral imagens, String
	 * conteudo) { this.imagens = imagens; if(conteudo==null) return;
	 * 
	 * this.imagens.codigo = nucleo.getCodHTMLOriginal();
	 * 
	 * }//
	 */

	private String reconstroiComBarraENovoValor(String valor, int novoValor) {
		int pos = valor.indexOf("/");
		if (pos == -1) {
			valor += " / " + novoValor;
		} else {
			valor = valor.substring(0, pos);
			valor += "/ " + novoValor;
		}
		return valor;
	}

	public void avisosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		String valor = alteradorDeCsv.getAvisoP1(hashCode);

		valor = reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

		alteradorDeCsv.setAvisoP1(hashCode, valor);

	}

	public void avisosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		String valor = alteradorDeCsv.getAvisoP2(hashCode);

		valor = reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

		alteradorDeCsv.setAvisoP2(hashCode, valor);
	}

	public void avisosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		String valor = alteradorDeCsv.getAvisoP3(hashCode);

		valor = reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

		alteradorDeCsv.setAvisoP3(hashCode, valor);
	}

	public void errosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		String valor = alteradorDeCsv.getErroP1(hashCode);
		valor = reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

		alteradorDeCsv.setErroP1(hashCode, valor);

		errosP1 = armazenaErroOuAviso;
	}

	public void errosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		String valor = alteradorDeCsv.getErroP2(hashCode);
		valor = reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

		alteradorDeCsv.setErroP2(hashCode, valor);
	}

	public void errosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		String valor = alteradorDeCsv.getErroP3(hashCode);
		valor = reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

		alteradorDeCsv.setErroP3(hashCode, valor);
	}

	public void fimDaAvaliacao() {
		alteradorDeCsv.escreveEmDisco();
	}

	public void linksAchadosPeloNucleo(HashSet links) {

	}

	/*
	 
	public ArrayList<FerramentaAnaliseGeralModel> getPosicaoeTag() {
		return this.imagens.posicaoeTag;
	}
	

	public void setPosicaoeTag(ArrayList<FerramentaAnaliseGeralModel> posiceTag) {
		this.imagens.posicaoeTag = posiceTag;
	}
	 */
}