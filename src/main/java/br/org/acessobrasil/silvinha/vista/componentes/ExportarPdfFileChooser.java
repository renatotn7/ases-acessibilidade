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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.util.GeraRelatorioPDF;
import br.org.acessobrasil.silvinha2.mli.GERAL;
/**
 * Caixa de diálogo para salvar o PDF 
 *
 */
public class ExportarPdfFileChooser extends JFileChooser{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6102115366294050832L;
	
	public ExportarPdfFileChooser(JFrame frameSilvinha, ResumoDoRelatorio resumo) {
		super();
	
		adicionaFiltrodeExtensoesParaCaixaSalvar();
		int salvar = mostraACaixaDeDialogoSalvar(frameSilvinha);
		
		if (salvar == JFileChooser.APPROVE_OPTION) {
			atitudeCasoClicadoBotaoSalvar(resumo);
		} else if (salvar == JFileChooser.CANCEL_OPTION) {
			atitudeCasoClicadoBotaoCancelar();
		}
	}



	private int mostraACaixaDeDialogoSalvar(JFrame frameSilvinha) {
		int salvar = showSaveDialog(frameSilvinha);
		return salvar;
	}



	private void adicionaFiltrodeExtensoesParaCaixaSalvar() {
		this.setFileFilter(new PdfFileFilter());
	}



	private void atitudeCasoClicadoBotaoCancelar() {
		JOptionPane.showMessageDialog(null, GERAL.GRAV_CANCELADA_USUARIO);
	}

	
	
	private void atitudeCasoClicadoBotaoSalvar(ResumoDoRelatorio resumo) {
		String path = corrigeNomeArquivoSeNecessario();

		File file = new File(path);
		
		if (file.exists()) {
			atitudeCasoArquivoExista(resumo, file);
		} else {
			atitudeCasoArquivoNaoExista(resumo, file);
		}
	}



	private void atitudeCasoArquivoNaoExista(ResumoDoRelatorio resumo, File file) {
		tentaGerarPDF(resumo, file);
	}



	private void atitudeCasoArquivoExista(ResumoDoRelatorio resumo, File file) {
		int confirmar = pedeDecisaoDoUsuarioSobreSobrescrever();
		
		if (confirmar == JOptionPane.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(this, GERAL.GRAV_CANCELADA_USUARIO);
		} else {
			atitudeCasoArquivoNaoExista(resumo, file);
		}
	}



	private void tentaGerarPDF(final ResumoDoRelatorio resumo, final File file) {
		Thread a = new Thread(){
			public void run(){
				if (!geraPDF(resumo, file)) {
					msgErroCasoNaoConsigaGerarPDF();
				} else {
					msgCasoConsigaGerarPDF();
				}
			}
		};
		a.start();
		/*
		if (!geraPDF(resumo, file)) {
			msgErroCasoNaoConsigaGerarPDF();
		} else {
			msgCasoConsigaGerarPDF();
		}
		*/
	}



	private void msgCasoConsigaGerarPDF() {
		JOptionPane.showMessageDialog(this, GERAL.ARQUIVO_GRAV_SUCESSO);
	}



	private void msgErroCasoNaoConsigaGerarPDF() {
		JOptionPane.showMessageDialog(this, GERAL.FALHA_GRAV_ARQUIVO, GERAL.ERRO, 
				JOptionPane.ERROR_MESSAGE);
	}



	private boolean geraPDF(ResumoDoRelatorio resumo, File file) {
		return GeraRelatorioPDF.geraRelatorio(file);
	}

	private int pedeDecisaoDoUsuarioSobreSobrescrever() {
		int confirmar = JOptionPane.showConfirmDialog(this, GERAL.ARQUIVO_EXISTENTE + GERAL.SOBRESCREVER_ARQUIVO, 
				                      GERAL.BTN_SALVAR, JOptionPane.YES_NO_OPTION);
		return confirmar;
	}

	private String corrigeNomeArquivoSeNecessario() {
		String path = getSelectedFile().getPath();
		path = path.endsWith(".pdf") ? path : path + ".pdf";
		return path;
	}

}
