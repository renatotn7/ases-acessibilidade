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

package br.org.acessobrasil.silvinha.vista.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.util.ThreadParaGerente;
import br.org.acessobrasil.silvinha.util.Token;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaCtrl;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradExecutarAgoraListenerLocalEWeb;
import br.org.acessobrasil.silvinha2.mli.TradGerente;

/**
 * classe que começa a execução da analise local
 */
public class ExecutarAgoraListenerWeb extends Thread {

	public static PainelAvaliacao painelAvaliacao;

	private Gerente gerente;

	public static boolean podeAdicionarLinhaemTabela;

	public static ThreadParaGerente threadParaGerente;

	private FrameSilvinha frameSilvinha;
	
	public ExecutarAgoraListenerWeb(PainelAvaliacao pp) {
		painelAvaliacao = pp;
		frameSilvinha = painelAvaliacao.framePai;
		podeAdicionarLinhaemTabela = false;
	}

	public void pararExecucao() {
		if (gerente != null) {
			gerente.pararAvaliacao();
		}

	}

	public void run() {
		Properties opcoes = painelAvaliacao.verificaOpcoes();
		if (opcoes == null) {
			PainelAvaliacao.habilitarExecutar();
			return;
		} else {
			PainelStatusBar.setPaginasAvaliadas(0);
			PainelStatusBar.setText(TradGerente.INICIANDO_AVALIACAO);
			PainelStatusBar.setTotalLinks(0);
			PainelStatusBar.showProgressBar();
			try {
				new Token();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(null, TradExecutarAgoraListenerLocalEWeb.NAO_POSSIVEL_CRIAR_RELATORIO,
						GERAL.ERRO, JOptionPane.ERROR_MESSAGE);
				PainelStatusBar.setText(TradExecutarAgoraListenerLocalEWeb.AVALIACAO_INTERROMPIDA);
				return;
			}
			ArrayList<RelatorioDaUrl> relatorios = new ArrayList<RelatorioDaUrl>();
			gerente = new Gerente();
			threadParaGerente = new ThreadParaGerente(opcoes,frameSilvinha);
			new Thread(SilvinhaCtrl.processaErro, threadParaGerente);
			threadParaGerente.start();
			relatorios = ResumoDoRelatorio.relatorios;
			painelAvaliacao.getParentFrame().showPainelResumo(
					new ResumoDoRelatorio(relatorios, opcoes), false);
			podeAdicionarLinhaemTabela = true;
		}
	}

}
