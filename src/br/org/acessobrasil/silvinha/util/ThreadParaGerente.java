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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

import br.org.acessobrasil.ases.persistencia.BancoSite;
import br.org.acessobrasil.ases.persistencia.SingBancoSite;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.excessoes.ExceptionUrlSemConteudo;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha2.mli.TradGeradorMapaSite;
import br.org.acessobrasil.silvinha2.projetodosite.ProjetoDoSite;
/**
 * Processo para execução da avaliação
 *
 */
public class ThreadParaGerente extends Thread {

	private Properties opcoes;

	public static ArrayList<RelatorioDaUrl> relatorios;

	public static Gerente gerente; // classe provavelmente principal

	public BancoSite bancoSite;
	private FrameSilvinha frameSilvina;
	public ThreadParaGerente(Properties opcoes,FrameSilvinha frameSilvina) {
		this.frameSilvina=frameSilvina;
		initDiretorios();
		this.opcoes = opcoes;
	}

	private void initDiretorios(){
		
		String endAserAvaliado = PainelAvaliacao.getEndAvaliado()+"";
		String endSemHttp = endAserAvaliado.replaceAll("http://", "");
		File diretorio = null;
		if (endSemHttp.indexOf("/") != -1) {
			diretorio = new File("temp/"+endSemHttp.substring(0, endSemHttp.indexOf("/")));
		} else {
			diretorio = new File("temp/"+endSemHttp);
		}
		System.out.println("nome do Diretorio");
		String strDiretorio = "temp/"+diretorio.getName();
		ProjetoDoSite.setNomeDoProjeto(diretorio.getName());
		if (!diretorio.isDirectory()) {
			diretorio.mkdirs();
		}
		SingBancoSite.setBancoNome(diretorio.getName());
		bancoSite =SingBancoSite.getInstancia();
		bancoSite.insertTabelaPortal(diretorio.getName(), 0, 0, 0, 0, 0, 0, 0, 0);
		strDiretorio = strDiretorio + "/reparo";
		diretorio = new File(strDiretorio);
		if (!diretorio.isDirectory()) {
			diretorio.mkdir();
		}
		strDiretorio = strDiretorio + "/temp";
		diretorio = new File(strDiretorio);
		if (!diretorio.isDirectory()) {
			diretorio.mkdir();
		}
	}
	
	public void run() {
		boolean tentaUrl=true;
		while(tentaUrl){
			try {
				TradGeradorMapaSite.carregaTexto(TokenLang.LANG);
				Date startDate = new Date();
				gerente = new Gerente();
				relatorios = new ArrayList<RelatorioDaUrl>();
				relatorios = gerente.iniciar(opcoes);
				//pega a data final
				Date finalDate = new Date();
				PainelStatusBar.setText(TradGeradorMapaSite.AVALIACA_TERMINADA + Util.calcularTempo(startDate, finalDate));
				tentaUrl=false;
			} catch (ExceptionUrlSemConteudo e) {
				ExceptionDialog.showExceptionDialog(e.getMessage());
				Token.URL_STRING = JOptionPane.showInputDialog("Por favor, digite novamente.\nURL:","http://");
				if(Token.URL_STRING==null){
					this.frameSilvina.showPainelAvaliacao();
					return;
				}
				e.printStackTrace();
			} catch(Exception e){
				ExceptionDialog.showExceptionDialog("Class ThreadParaGerente: "+e.getMessage());
				return;
			}
		}		
	}

}
