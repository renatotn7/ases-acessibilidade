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
package br.org.acessobrasil.silvinha.util.versoes;

import java.net.URL;

import javax.swing.JOptionPane;

import br.org.acessobrasil.silvinha.entidade.Versao;
/**
 * Processo para verificar a Versão 
 *
 */
public class ThreadVerificaVersao extends Thread {
	String servidor;

	Versao versao;

	public ThreadVerificaVersao(String servidor, Versao versao) {
		this.servidor = servidor;
		this.versao = versao;
	}

	public void run() {
		try {
			URL url = new URL(servidor);
			// Abre a conexão para verificar se o servidor se encontra ativo
			//System.out.println("FrameSilvinha:1.4");
			url.openConnection().setConnectTimeout(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//System.out.println("FrameSilvinha:1.5");
		// Realiza a verificação da Versão
		VerificadorDeVersoes vv = new VerificadorDeVersoes(servidor);
		//System.out.println("FrameSilvinha:1.6");
		boolean realizaAtualizacao = vv.verificarVersao(versao);
		//System.out.println("FrameSilvinha:1.7");
		if (realizaAtualizacao) {
			JOptionPane.showMessageDialog(null, "Atualize o Silvinha!");
		}
	}

}
