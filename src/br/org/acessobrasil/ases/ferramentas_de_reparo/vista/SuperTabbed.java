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
package br.org.acessobrasil.ases.ferramentas_de_reparo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.util.G_Cronometro;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TableReadOnly;
import br.org.acessobrasil.silvinha2.util.G_TableReadOnlyListener;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.SalvaAlteracoes;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
/**
 * Classe que irá substituir a UI das ferramentas
 * @author Fabio Issamu Oshiro
 */
public abstract class SuperTabbed extends SuperPainelCentral {
		
	public int[] maxWidths;

	protected String regra;

	protected boolean showTextDesc = true;
	
	JMenuItem miBtnSalvar;
	
	FrameSilvinha frameSilvinha;

	protected PainelEditavel panelEditavel;
	
	protected PainelOriginal panelOriginal;
	
	MenuListener menuListener;
	SalvaAlteracoes salvaAlteracoes;
	
	public String[] colunas;
	
	public SuperTabbed(FrameSilvinha frameSilvinha) {
		super(new BorderLayout());
		this.frameSilvinha = frameSilvinha;
		JTabbedPane editNaoEdit = new JTabbedPane();
		panelEditavel = new PainelEditavel(this);
		panelEditavel.setCodigoFonte(TxtBuffer.getContent());
		
		panelOriginal = new PainelOriginal(this);
		panelOriginal.setCodigoFonte(TxtBuffer.getContentOriginal());
		
		editNaoEdit.add(GERAL.CODIGO_EDICAO, panelEditavel);
		editNaoEdit.add(GERAL.CODIGO_ORIGINAL, panelOriginal);
		this.setBackground(frameSilvinha.corDefault);
		frameSilvinha.setTitle(getTitulo());
		this.add(editNaoEdit,BorderLayout.CENTER);
		initMenu();
		
		this.setVisible(true);
	}
	
	public void initMenu(){
		menuListener = new MenuListener(this);
		this.frameSilvinha.setJMenuBar(MenuSilvinha.criaMenuBar(frameSilvinha, menuListener, miBtnSalvar, panelEditavel.textAreaSourceCode));
	}
	protected Border criaBorda(String titulo) {
		Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(), new TitledBorder(bordaLinhaPreta, titulo));
		Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
		return bordaFinal;
	}
	public void setImagemPainelOriginal(String fullUrl){
		SetImage setImage = new SetImage(this.panelOriginal, fullUrl);
		setImage.start();
	}
	public void setImagemPainelEditavel(String fullUrl){
		SetImage setImage = new SetImage(this.panelEditavel, fullUrl);
		setImage.start();
	}
	
	public abstract void onTableOriginalSelected(int row, int col, String valor, G_TextAreaSourceCode textAreaSourceCode);
	public abstract void onTableEditableSelected(int row, int col, String valor, G_TextAreaSourceCode textAreaSourceCode);

	/**
	 * Retornar o nome das colunas na tabela de erros
	 * @return
	 */
	public abstract String[] getColunas();
	/**
	 * Retornar o tamanho máximo das colunas da esquerda para a direita
	 * @return
	 */
	public abstract int[] getMaxWidths();
	/**
	 * Retorne true para que seja mostrado o painel de imagem
	 * @return
	 */
	public abstract boolean isShowPainelImg();
	/**
	 * Avaliar o arquivo passado como parametro
	 */
	public abstract void avaliaArq(String path);
	/**
	 * Avaliar a url passada como parâmetro
	 */
	public abstract void avaliaUrl(String url);
	/**
	 * Avaliar o arquivo passado como parâmetro
	 */
	public abstract void avaliaArq(G_File temp);
	/**
	 * Reavaliar o código passado como parâmetro
	 */
	public abstract void reavalia(String codHtml);

	/**
	 * Informar o título da ferramenta
	 */
	public abstract String getTitulo();
	
	@Override
	public boolean showBarraUrl() {
		return true;
	}
	
	/**
	 * Painel Original que por default não é editável
	 * @author Fabio Issamu Oshiro
	 *
	 */
	protected class PainelOriginal extends JPanel implements G_TableReadOnlyListener {
		private static final long serialVersionUID = 1L;
	
		/**
		 * Regra
		 */
		protected JPanel painelRegra;
	
		/**
		 * Mostra a imagem sem descrição
		 */
		protected org.xhtmlrenderer.simple.XHTMLPanel imagemSemDesc;
	
		/**
		 * Código fonte do site
		 */
		protected G_TextAreaSourceCode textAreaSourceCode;
	
		/**
		 * Tabela de Erros
		 */
		protected G_TableReadOnly tableReadOnly;
		
		SuperTabbed superTabbed;
	
		public PainelOriginal(SuperTabbed superTabbed) {
			super(new BorderLayout());
			this.superTabbed = superTabbed;
			initComp();
			setNonEditable();
		}
		public void setNonEditable(){
			textAreaSourceCode.getTextPane().setEditable(false);
		}
		public void initComp() {
			
			painelRegra = new JPanel(new BorderLayout());
			
			textAreaSourceCode = new G_TextAreaSourceCode();
			tableReadOnly = new G_TableReadOnly(superTabbed.getColunas(),superTabbed.getMaxWidths());
			tableReadOnly.addListener(this);
			/*
			 * Verifica se é necessário incluir a regra
			 */
			if(superTabbed.regra!=null && !superTabbed.regra.equals("")){
				JTextArea txtRegra = new JTextArea();
				txtRegra.setText(superTabbed.regra);
				painelRegra.add(txtRegra,BorderLayout.CENTER);
			}
			
			/*
			 * Verifica se mostra o painel de imagens
			 */
			JPanel panelNorth = new JPanel(new BorderLayout());
			if(superTabbed.isShowPainelImg()){
				imagemSemDesc = new org.xhtmlrenderer.simple.XHTMLPanel();
				JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(imagemSemDesc) , textAreaSourceCode);
				splitPane.setOneTouchExpandable(true);
				splitPane.setDividerLocation(superTabbed.frameSilvinha.getWidth()/4);
				panelNorth.add(splitPane,BorderLayout.CENTER);
			}else{
				panelNorth.add(textAreaSourceCode,BorderLayout.CENTER);
			}
			/*
			 * método para ser criada a parte de descrição, inserção de texto, etc 
			 */
			criaDescricao(panelNorth);
			/*
			 * Colocar a tabela de erros ou outra coisa 
			 */
			JPanel panelSouth = new JPanel(new BorderLayout());
			JScrollPane scrollPane = new JScrollPane(tableReadOnly);
			panelSouth.add(scrollPane, BorderLayout.CENTER);
			criaBtns(panelSouth);
			
			JSplitPane splitPaneBase = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelNorth , panelSouth);
			splitPaneBase.setDividerLocation(superTabbed.frameSilvinha.getHeight()/3);
			this.add(splitPaneBase,BorderLayout.CENTER);
		}
		/**
		 * Sobreescrito pelo painel Edição
		 *
		 */
		public void criaBtns(JPanel panelSouth){
			
		}
		/**
		 * Sobreescrito pelo painel Edição
		 *
		 */
		public void criaDescricao(JPanel panelNorth){
			
		}
		public void setCodigoFonte(String texto){
			textAreaSourceCode.setText(texto);
		}
		public void onSelected(int row, int col, String valor) {
			this.superTabbed.onTableOriginalSelected(row, col, valor,textAreaSourceCode);
		}
	}
	protected class PainelEditavel extends PainelOriginal {
		public PainelEditavel(SuperTabbed superTabbed) {
			super(superTabbed);
		}

		private static final long serialVersionUID = 1L;
	
		/**
		 * Label da descrição
		 */
		JLabel labelTxtDesc;
		
		/**
		 * Texto da Descrição
		 */
		JTextArea txtDesc;
		/**
		 * Botão de aplicar
		 */
		JButton btnAplicar;
	
		/**
		 * Botão de salvar
		 */
		JButton btnSalvar;
	
		/**
		 * Botão de Abrir
		 */
		JButton btnAbrir;
	
		/**
		 * Botão de Tela Anterior
		 */
		JButton btnTelaAnterior;
	
		/**
		 * Botão de Reverter
		 */
		JButton btnReverter;
		/**
		 * Cria o painel de botões
		 */
		public void criaBtns(JPanel panelSouth){
			JPanel panelBtn = new JPanel(new GridLayout(1,4));
			btnSalvar = new JButton("Salvar");
			btnAbrir = new JButton("Abrir");
			btnTelaAnterior = new JButton("Tela Anterior");
			btnReverter = new JButton("Reverter");
			
			panelBtn.add(btnSalvar);
			panelBtn.add(btnAbrir);
			panelBtn.add(btnTelaAnterior);
			panelBtn.add(btnReverter);
			
			panelSouth.add(panelBtn,BorderLayout.SOUTH);
		}
		/**
		 * Sobreescrito pelo painel Edição
		 *
		 */
		public void criaDescricao(JPanel panelNorth){
			JPanel panelDesc = new JPanel(new BorderLayout());
			labelTxtDesc = new JLabel("Descrição:");
			txtDesc = new JTextArea();
			btnAplicar = new JButton("Aplicar");
			
			txtDesc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			panelDesc.add(labelTxtDesc,BorderLayout.WEST);
			panelDesc.add(txtDesc,BorderLayout.CENTER);
			panelDesc.add(btnAplicar,BorderLayout.EAST);
			
			panelNorth.add(panelDesc,BorderLayout.SOUTH);
		}
		public void setNonEditable(){
		
		}
		public void onSelected(int row, int col, String valor) {
			this.superTabbed.onTableEditableSelected(row, col, valor,textAreaSourceCode);
		}
	}

	/**
	 * Listener do menu silvinha 
	 * @author Fabio Issamu Oshiro
	 */
	private class MenuListener implements ActionListener{
		private SuperTabbed superTabbed;
		private G_File caminhoRecente = new G_File("config/html_recente.txt");
		public MenuListener(SuperTabbed superTabbed){
			this.superTabbed = superTabbed;
		}
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "Salvar") {
				this.superTabbed.salvaAlteracoes.salvar();
				this.superTabbed.panelEditavel.textAreaSourceCode.coloreSource();
				this.superTabbed.reavalia(this.superTabbed.panelEditavel.textAreaSourceCode.getText());
			} else if (cmd == "AbrirURL") {

				String url;
				url = JOptionPane.showInputDialog(this.superTabbed, GERAL.DIGITE_ENDERECO, "http://");
				avaliaUrl(url);
			} else if (cmd == "Abrir") {

				String a[] = { ".html", ".htm" };
				G_File temp = new G_File(caminhoRecente.read(), a);
				if (temp.getFile() != null) {
					this.superTabbed.avaliaArq(temp);
				}
			} else if (cmd == "SaveAs") {
				// salvarComo();
				salvaAlteracoes.salvarComo();
				// avalia();
			} else if (cmd == "Creditos") {
				new Creditos();
			} else if (cmd == "Sair") {
				System.exit(0);
			} else if (cmd == "Desfazer") {
				this.superTabbed.panelEditavel.textAreaSourceCode.undo();
				// boxCode.coloreSource();
				// reavalia(boxCode.getText());
			} else if (cmd == "AumentaFonte") {
				this.superTabbed.panelEditavel.textAreaSourceCode.aumentaFontSize();
			} else if (cmd == "DiminuiFonte") {
				this.superTabbed.panelEditavel.textAreaSourceCode.diminuiFontSize();
			} else if (cmd == "Contraste") {
				this.superTabbed.panelEditavel.textAreaSourceCode.autoContraste();
				this.superTabbed.reavalia(this.superTabbed.panelEditavel.textAreaSourceCode.getText());
			} else if (cmd == "Cancelar") {
				salvaAlteracoes.cancelar();
			}
		}
	}

	private class SetImage extends Thread {

		private PainelOriginal painelOriginal;

		private String fullUrl;

		public SetImage(PainelOriginal painelOriginal, String url) {
			this.painelOriginal = painelOriginal;
			this.fullUrl = url;
		}

		public void run() {
			try {
				G_File arq = new G_File("temp/img.html");
				String myHTML = "<html><head></head><body><img src=\"" + fullUrl + "\" /></body></html>";
				arq.write(myHTML);
				this.painelOriginal.superTabbed.frameSilvinha.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				G_Cronometro cro = new G_Cronometro();
				cro.start();
				this.painelOriginal.imagemSemDesc.setDocument(arq.getFile());
				// this.ferramentaDescricaoImagens.imagemSemDesc.setPage("file:///"+arq.getFile().getAbsolutePath());
				System.out.println("fullUrl " + fullUrl);
				// System.out.println("visivel " +
				// this.ferramentaDescricaoImagens.imagemSemDesc.isVisible());
				// System.out.println("isDisplayable " +
				// this.ferramentaDescricaoImagens.imagemSemDesc.isDisplayable());
				// this.ferramentaDescricaoImagens.parentFrame.setVisible(true);
				this.painelOriginal.superTabbed.frameSilvinha.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				cro.stop("Imagem carregada");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
