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

package br.org.acessobrasil.silvinha2.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.panels.relatorio.PainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.GERAL;
/**
 * Classe para salvar as avaliações
 * @author Acessibilidade Brasil - Renato Tomaz Nati - Fabio Issamu Oshiro
 *
 */
public class SalvaAlteracoes {

	private String nomeDoArquivoEmDisco;

	private JTextPane jtextpane;
	
	private ActListener actListener;
	
	private JButton salvar;
	private ArrayList<JButton> arrSalvar;

	private JMenuItem btnSalvar;

	private boolean textoModificado;

	private FrameSilvinha parentFrame;

	private String conteudo;

	private int escolha;
	
	/**
	 * Ultimo local onde foi salvo algum arquivo
	 */
	private G_File caminhoRecente = new G_File("config/html_recente.txt");
	
	private String filtro[]={".html",".htm"};

	public SalvaAlteracoes(JTextPane jtextpane, JButton salvar, JMenuItem btnSalvar, FrameSilvinha parentFrame) {
		this.jtextpane = jtextpane;
		this.salvar = salvar;
		this.btnSalvar = btnSalvar;
		this.parentFrame = parentFrame;
		this.textoModificado = false;
		initListener();
	}
	public SalvaAlteracoes(JTextPane jtextpane, ArrayList<JButton> salvar, JMenuItem btnSalvar, FrameSilvinha parentFrame) {
		this.jtextpane = jtextpane;
		this.arrSalvar = salvar;
		this.btnSalvar = btnSalvar;
		this.parentFrame = parentFrame;
		this.textoModificado = false;
		initListener();
	}
	public SalvaAlteracoes(FrameSilvinha parentFrame, String conteudo) {
		this.conteudo = conteudo;
		this.parentFrame = parentFrame;
		this.textoModificado = false;
		initListener();
	}

	private void initListener(){
		if(this.jtextpane!=null){
			actListener = new ActListener();
			this.jtextpane.addKeyListener(actListener);
		}
	}
	/**
	 *  fazer o frame que salva deve trabalhar o foco
	 */
	public void sair() {
		Sair sair = new Sair();
		sair.start();
	}

	/**
	 * Informa que o codigo foi alterado
	 * habilita o botao de salvar e atualiza o conteudo, flags etc
	 */
	public void setAlterado() {
		if (jtextpane != null)
			conteudo = jtextpane.getText();
		if (salvar != null)
			salvar.setEnabled(true);
		if (arrSalvar!=null){
			for(JButton b: arrSalvar){
				b.setEnabled(true);
			}
		}
			
		if (btnSalvar != null){
			btnSalvar.setEnabled(true);
			System.out.println("botão '" +  btnSalvar.getText() + "' habilitado");
		}
		textoModificado = true;
	}

	/**
	 *  se for salvar e cancelar é uma coisa, sair é outra
	 */
	public void cancelar() {
		Cancelar cancelar = new Cancelar();
		cancelar.start();
	}

	public void salvarComo() {
		if (jtextpane != null){
			conteudo = jtextpane.getText();
		}
		G_File arq = new G_File(getPathProvavel());
		if (arq.openDialogSaveAs(conteudo, filtro,jtextpane)) {
			fazerDepoisDeSalvar();
			atualizaUltimoSalvo(arq);
		}
	}

	private void salvar(String texto) {
		if (nomeDoArquivoEmDisco == null) {			
			G_File arq = new G_File(getPathProvavel());
			if (arq.openDialogSaveAs(texto, filtro,parentFrame)) {
				fazerDepoisDeSalvar();
				atualizaUltimoSalvo(arq);
			}
		} else {
			G_File arq = new G_File(nomeDoArquivoEmDisco);
			arq.write(texto);
			fazerDepoisDeSalvar();
		}
	}
	
	public void setFiltro(String args[]){
		filtro=args;
	}
	
	public void salvar() {
		if (jtextpane != null){
			conteudo = jtextpane.getText();
		}
		salvar(conteudo);
	}
	/**
	 * 
	 * @return O nome do arquivo mais provavel para ser salvo
	 */
	private String getPathProvavel(){
		String retorno="";
		try{
			String recente = caminhoRecente.read();
			//tentar pegar o nome do arquivo atual
			String pathAtual = parentFrame.getUrlTextField();
			String nome="";
			int c=lastIndexOfBarra(pathAtual);
			nome = pathAtual.substring(c);			
			retorno = recente.substring(0,lastIndexOfBarra(recente))+nome;
		}catch(Exception e){
			
		}
		//System.out.println("getPathProvavel = "+retorno);
		return retorno;
	}
	/**
	 * Retorna a ultima posicao de uma barra
	 * @param texto
	 * @return
	 */
	private int lastIndexOfBarra(String texto){
		int a = texto.lastIndexOf("/");
		int b = texto.lastIndexOf("\\");
		int c=0;
		if(a>b){
			c=a;
		}else{
			c=b;
		}
		return c+1;
	}
	/**
	 * Atualiza nomeDoArquivoEmDisco<br>
	 * caminhoRecente e a <br>
	 * FrameSilvinha.urlTextField (comboBox)
	 */
	private void atualizaUltimoSalvo(G_File arq){
		nomeDoArquivoEmDisco = arq.getFile().getAbsolutePath();
		caminhoRecente.write(nomeDoArquivoEmDisco);
		parentFrame.setUrlTextField(nomeDoArquivoEmDisco);
	}
	/**
	 * desabilita os botoes de salvar
	 * e seta o flag de texto modificado
	 */
	private void fazerDepoisDeSalvar(){
		if (salvar != null)
			salvar.setEnabled(false);
		if (arrSalvar!=null){
			for(JButton b: arrSalvar){
				b.setEnabled(false);
			}
		}
		if (btnSalvar != null)
			btnSalvar.setEnabled(false);
		textoModificado = false;
	}
	private class Cancelar extends Thread {

		public void run() {
			escolha = 9999;
			if (textoModificado) {
				// System.out.println("texto modificado: "+textoModificado);
				escolha = JOptionPane.showConfirmDialog(null, GERAL.DESEJA_SALVAR_ALTERACOES);// new
																								// SalvarAlteracoesVisao();
				// System.out.println("joptionpane: "+escolha);
				// System.out.println("depois");
				if (escolha == 0) {
					// System.out.println(escolha);
					salvar(conteudo);
					PainelRelatorio.loadRelatorioAlterado();
					parentFrame.showLastActivePanel();
				
				} else if (escolha == 1) {
					// System.out.println("escolha 1");
					try{
						PainelRelatorio.loadRelatorioAlterado();
					}catch(Exception e){
						/*
						 * pode dar um null pointer
						 * e.printStackTrace();
						 */
					}
					parentFrame.showLastActivePanel();
					
				} else if (escolha == 2) {
					// System.out.println("escolha 2 ou 9999");

				}
			} else {
				if(PainelRelatorio.relatorio==null){
					parentFrame.showLastActivePanel();
				}else{
					PainelRelatorio.loadRelatorioAlterado();
					parentFrame.showLastActivePanel();
				}
			}
			// System.out.println(escolha);

		}

	}

	private class Sair extends Thread {

		public void run() {
			escolha = 9999;
			if (textoModificado) {
				escolha = JOptionPane.showConfirmDialog(null, GERAL.DESEJA_SALVAR_ALTERACOES);

				if (escolha == 0) {
					salvar(conteudo);
					System.exit(0);
				} else if (escolha == 1) {
					System.exit(0);
				}

			} else {
				System.exit(0);
			}

		}
	}

	public JMenuItem getBtnSalvar() {
		return btnSalvar;
	}

	public void setBtnSalvar(JMenuItem btnSalvar) {
		this.btnSalvar = btnSalvar;
		if (textoModificado == true) {
			setAlterado();
		}
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public JTextPane getJtextpane() {
		return jtextpane;
	}

	public void setJtextpane(JTextPane jtextpane) {
		this.jtextpane = jtextpane;
		initListener();
	}

	public String getNomeDoArquivoEmDisco() {
		return nomeDoArquivoEmDisco;
	}

	public void setNomeDoArquivoEmDisco(String nomeDoArquivoEmDisco) {
		this.nomeDoArquivoEmDisco = nomeDoArquivoEmDisco;
	}

	public FrameSilvinha getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(FrameSilvinha parentFrame) {
		this.parentFrame = parentFrame;
	}

	public JButton getSalvar() {
		return salvar;
	}

	public void setSalvar(JButton salvar) {

		this.salvar = salvar;
		if (textoModificado == true) {
			setAlterado();
		}
	}
	private class ActListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
		
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			//System.out.println("SalvaAlteracoes.keyTyped();");
			SalvaAlteracoes.this.setAlterado();
		}
	}
}
