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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.links_redundantes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import org.apache.commons.httpclient.HttpException;

import br.org.acessobrasil.ases.ferramentas_de_reparo.controle.ControleLinkRedundante;
import br.org.acessobrasil.ases.ferramentas_de_reparo.excessao.ExceptionImagemNotFound;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.nucleuSilva.util.PegarPaginaWEB;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.JMenuItemTeclaAtalho;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha.vista.tableComponents.DefaulTableModelNotEditable;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.mli.TradPainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.XHTML_Panel;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
import br.org.acessobrasil.silvinha2.util.SalvaAlteracoes;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;

/**
 * Cria a interface UI para inserir links redundantes 
 * em mapas de imagens
 * @author Fabio Issamu Oshiro
 *
 */
public class PanelLinkRedundante extends SuperPainelCentral implements ActionListener, MouseListener {
		/**
		 * extends JFrame
		 */
		private static final long serialVersionUID = -638404432108855839L;

		G_TextAreaSourceCode textAreaSourceCode;

		private G_File arquivo;

		private JButton btn_salvarComo;

		private JMenuBar menuBar;

		private JPanel painel;

		JButton btn_salvar;

		private JButton btn_abrir;

		private TabelaErros tabelaDeErros;

		private JScrollPane scrollPaneTabela;

		private FrameSilvinha frameSilvinha;
		
		private JButton btnAplicar;
		
		/**
		 * Texto para colocar no que aparece no link
		 */
		private JTextField texto;

		private JPanel panelCorretor;
		
		private ControleLinkRedundante controle;
		
		private JButton btn_cancelar;
		private boolean original=false;
		private int erroAtual;
		
		private JButton reverter;
		
		SalvaAlteracoes salvaAlteracoes;
		
		PanelLinkRedundante painelOriginal;
		
		private JMenuItem miBtnSalvar;
		
		JPanel btnPanel;
		
		G_File caminhoRecente = new G_File("config/html_recente.txt");
		
		/**
		 * Construtor
		 * 
		 * @param frameSilvinha
		 */
		public PanelLinkRedundante(FrameSilvinha frameSilvinha,String codHtml) {
			XHTML_Panel.carregaTexto(TokenLang.LANG);
			this.frameSilvinha = frameSilvinha;
			criaInterfaceVisualEscalavel();
			atribuiActionCommand();			
			arquivo = null;
			textAreaSourceCode.setText(codHtml);
			reavalia(codHtml);
		}

		/**
		 * Coloca todos os comands e listeners
		 */
		private void atribuiActionCommand() {
			btn_salvar.setActionCommand("Salvar");
			btn_salvar.addActionListener(this);
			btn_abrir.setActionCommand("Abrir");
			btn_abrir.addActionListener(this);
			tabelaDeErros.addMouseListener(this);
			btn_salvarComo.setActionCommand("SaveAs");
			btn_salvarComo.addActionListener(this);
			btnAplicar.setActionCommand("Aplicar");
			btnAplicar.addActionListener(this);
			btn_cancelar.setActionCommand("Cancelar");
			btn_cancelar.addActionListener(this);
		}

		/**
		 * Cria uma borda com título dentro dos padrões
		 * 
		 * @param titulo
		 * @return
		 */
		private Border criaBorda(String titulo) {
			Border bordaLinhaPreta = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
			Border borda = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5), new TitledBorder(bordaLinhaPreta, titulo));
			Border bordaFinal = BorderFactory.createCompoundBorder(borda, BorderFactory.createEmptyBorder(0, 4, 4, 5));
			return bordaFinal;
		}

		private void criaInterfaceVisualEscalavel() {
			miBtnSalvar = new JMenuItem(XHTML_Panel.BTN_SALVAR);
			painel = new JPanel();
			textAreaSourceCode = new G_TextAreaSourceCode();
			//frameSilvinha.setJMenuBar(this.criaMenuBar());			
			textAreaSourceCode.setTipoHTML();
			textAreaSourceCode.setBorder(criaBorda(XHTML_Panel.COD_FONTE));
			//frameSilvinha.setTitle(XHTMLPanel.TIT_LINK_RED);

			painel.setLayout(new GridLayout(2, 1));
			setBackground(frameSilvinha.corDefault);

			Container contentPane = this;
			contentPane.setLayout(new GridLayout(1, 1));
			painel.add(textAreaSourceCode);

			JPanel panelBtnTabela = new JPanel();

			panelBtnTabela.setLayout(new BorderLayout());

			
			
			/*
			 * Barra de botões
			 */
			btnPanel = new JPanel();
			btnPanel.setLayout(null);
			btn_salvar = new JButton(XHTML_Panel.BTN_SALVAR);
			btn_salvar.setToolTipText(XHTML_Panel.DICA_SALVAR);
			btn_salvar.setBounds(10, 0, 150, 25);
			btnPanel.add(btn_salvar);

			btn_abrir = new JButton(XHTML_Panel.BTN_ABRIR);
			btn_abrir.setToolTipText(XHTML_Panel.DICA_ABRIR);
			btn_abrir.setBounds(165, 0, 150, 25);
			btnPanel.add(btn_abrir);

			btn_salvarComo = new JButton(XHTML_Panel.BTN_SALVAR_COMO);
			btn_salvarComo.setToolTipText(XHTML_Panel.DICA_SALVAR_COMO);
			btn_salvarComo.setBounds(320, 0, 150, 25);
			btnPanel.add(btn_salvarComo);

			btn_cancelar = new JButton(XHTML_Panel.TELA_ANTERIOR);
			btn_cancelar.setToolTipText(XHTML_Panel.DICA_TELA_ANTERIOR);
			btn_cancelar.setBounds(480, 0, 150, 25);
			btnPanel.add(btn_cancelar);
			
			btnPanel.setPreferredSize(new Dimension(430, 30));

			/*
			 * Barra de correcao
			 */
			btnAplicar = new JButton(XHTML_Panel.BTN_APLICAR);
			btnAplicar.setToolTipText(XHTML_Panel.DICA_BTN_APLICAR);
			btnAplicar.setEnabled(false);
			
			texto = new JTextField();
			
			texto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			JPanel borda = new JPanel(new BorderLayout());
			JLabel lbl_texto = new JLabel(XHTML_Panel.ROTULO_TEXTO);
			lbl_texto.setToolTipText(XHTML_Panel.DICA_ROTULO_TEXTO);
			borda.add(lbl_texto,BorderLayout.WEST);
			borda.add(texto,BorderLayout.CENTER);
			borda.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 5));
			borda.setOpaque(false);
			panelCorretor = new JPanel(new BorderLayout());
			panelCorretor.add(borda,BorderLayout.CENTER);
			panelCorretor.add(btnAplicar,BorderLayout.EAST);
			//panelCorretor.add(btnPanel,BorderLayout.WEST);
			panelCorretor.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 10));
			panelCorretor.setOpaque(false);
			
			/*
			 * Tabela de erros
			 */
			tabelaDeErros = new TabelaErros();
			scrollPaneTabela = new JScrollPane();
			scrollPaneTabela.setViewportView(tabelaDeErros);
			panelBtnTabela.add(panelCorretor, BorderLayout.NORTH);
			panelBtnTabela.add(scrollPaneTabela, BorderLayout.CENTER);
			panelBtnTabela.add(btnPanel, BorderLayout.SOUTH);
			scrollPaneTabela.setBorder(criaBorda(XHTML_Panel.LISTA_ERROS));
			painel.add(panelBtnTabela);

			btnPanel.setBackground(frameSilvinha.corDefault);
			if (!original) {
			reverter = new JButton("Reverter");
			reverter.setText(TradPainelRelatorio.REVERTER);
			reverter.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);
					TxtBuffer.setContent(TxtBuffer.getContentOriginal());
					frameSilvinha.showPainelFerramentaLinksRedundantes();
					setVisible(true);
				}
				
			});
			//reverter.setActionCommand("Reverter");
			reverter.setToolTipText(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleDescription(TradPainelRelatorio.DICA_REVERTER);
			reverter.getAccessibleContext().setAccessibleName(TradPainelRelatorio.DICA_REVERTER);
			reverter.setBounds(640, 0, 150, 25);
			btnPanel.add(reverter);}

			
			panelBtnTabela.setBackground(frameSilvinha.corDefault);
			painel.setBackground(frameSilvinha.corDefault);
			contentPane.setBackground(frameSilvinha.corDefault);
			scrollPaneTabela.setBackground(frameSilvinha.corDefault);
			textAreaSourceCode.setBackground(frameSilvinha.corDefault);
			miBtnSalvar.setEnabled(false);
			btn_salvar.setEnabled(false);
			salvaAlteracoes=TxtBuffer.getInstanciaSalvaAlteracoes(textAreaSourceCode.getTextPane(), btn_salvar, miBtnSalvar,  frameSilvinha);
			String fil[]={ ".html", ".htm" };
			salvaAlteracoes.setFiltro(fil);
			contentPane.add(painel);
			// pack();
			this.setVisible(true);
		}

		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "Salvar") {
				salvaAlteracoes.salvar();
				/*	if (arquivo == null) {
					salvarComo();
					// avalia();
					return;
				}
				arquivo.write(boxCode.getText());//*/
				//avalia();
				reavalia(textAreaSourceCode.getText());
			} else if (cmd == "Aplicar") {
			
				if(controle!=null){
					
					int pos = controle.getIniIndex(erroAtual);
					String newCod;
					try {
						newCod = controle.corrige(texto.getText(),erroAtual);
						textAreaSourceCode.setText(newCod);
						salvaAlteracoes.setAlterado();
						TxtBuffer.setContent(textAreaSourceCode.getText());
						reavalia(newCod);
						textAreaSourceCode.getTextPane().setCaretPosition(pos);
					} catch (ExceptionImagemNotFound e1) {
						JOptionPane.showMessageDialog(this,e1.getMessage());
					}
					controle.avalia(textAreaSourceCode.getText(),
							true);
				}
			} else if (cmd == "AbrirURL") {
				String url;
				url = JOptionPane.showInputDialog(this, GERAL.DIGITE_ENDERECO, "http://");
				avaliaUrl(url);
				// JOptionPane.showMessageDialog(null, "Seu nome é " + nome);

			} else if (cmd == "Abrir") {

				
				String a[] = { ".html", ".htm" };
				G_File temp = new G_File(caminhoRecente.read(), a);
				if (temp.getFile() != null) {
					avaliaArq(temp);
					
				}
			} else if (cmd == "SaveAs") {
				//salvarComo();
				salvaAlteracoes.salvarComo();
				// avalia();
			} else if (cmd == "Creditos") {
				new Creditos();
			} else if (cmd == "Sair") {
				System.exit(0);
			} else if (cmd == "Desfazer") {
				textAreaSourceCode.undo();
				//boxCode.coloreSource();
				//reavalia(boxCode.getText());
			} else if (cmd == "AumentaFonte") {
				textAreaSourceCode.aumentaFontSize();
			} else if (cmd == "DiminuiFonte") {
				textAreaSourceCode.diminuiFontSize();
			} else if (cmd == "Contraste") {
				textAreaSourceCode.autoContraste();
				reavalia(textAreaSourceCode.getText());
			}else if(cmd=="SelecionarTudo"){
				textAreaSourceCode.getTextPane().selectAll();
				if(painelOriginal!=null){
					painelOriginal.textAreaSourceCode.getTextPane().selectAll();
				}
			}else if (cmd == "Cancelar") {
				salvaAlteracoes.cancelar();
			}
		}

		private void reavalia(String codigo) {

			tabelaDeErros.initComponents();
			// boxCode.coloreSource();
			controle = new ControleLinkRedundante();
			// xhtmlValidator.avalia(arquivo.getFile());

			controle.avalia(codigo);
			if (controle.length() == 0) {
				tabelaDeErros.addLinha(0, 0, XHTML_Panel.DOC_SEM_ERROS);
			}

			for (int i = 0; i < controle.length(); i++) {
				tabelaDeErros.addLinha(controle.getLinha(i), controle.getColuna(i), controle.getTag(i));
				//boxCode.marcaErro(ini, fim);
				textAreaSourceCode.marcaErro(controle.getLinha(i),controle.getColuna(i),controle.getTag(i).length());
			}
			btnAplicar.setEnabled(false);
			// domValidator
		}

		/**
		 * Avalia o arquivo
		 */
		private void avalia() {
			String codHtml = arquivo.read();
			avalia(codHtml);
		}
		/**
		 * Avalia o código
		 * @param codHtml codigo HTML
		 */
		private void avalia(String codHtml){
			textAreaSourceCode.setText(codHtml);
			reavalia(codHtml);
		}

		/**
		 * Cria o menu
		 * 
		 * @return o menu
		 */
		private JMenuBar criaMenuBar() {
			menuBar = new JMenuBar();
			menuBar.setBackground(frameSilvinha.corDefault);

			JMenu menuArquivo = new JMenu(XHTML_Panel.ARQUIVO);
			menuArquivo.setMnemonic('A');
			menuArquivo.setMnemonic(KeyEvent.VK_A);

			JMenu avaliadores = new JMenu();
			MenuSilvinha menuSilvinha = new MenuSilvinha(frameSilvinha, null);
			menuSilvinha.criaMenuAvaliadores(avaliadores);
			// menuArquivo.add(avaliadores);
			// menuArquivo.add(new JSeparator());

			JMenuItem btnAbrir = new JMenuItem(XHTML_Panel.BTN_ABRIR);
			btnAbrir.addActionListener(this);
			btnAbrir.setActionCommand("Abrir");
			btnAbrir.setMnemonic('A');
			btnAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, ActionEvent.CTRL_MASK));
			btnAbrir.setMnemonic(KeyEvent.VK_A);
			btnAbrir.setToolTipText(XHTML_Panel.DICA_ABRIR);
			btnAbrir.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_ABRIR);
			menuArquivo.add(btnAbrir);

			JMenuItem btnAbrirUrl = new JMenuItem(XHTML_Panel.BTN_ABRIR_URL);
			btnAbrirUrl.addActionListener(this);
			btnAbrirUrl.setActionCommand("AbrirURL");
			btnAbrirUrl.setMnemonic('U');
			btnAbrirUrl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, ActionEvent.CTRL_MASK));
			btnAbrirUrl.setMnemonic(KeyEvent.VK_U);
			btnAbrirUrl.setToolTipText(XHTML_Panel.DICA_ABRIR);
			btnAbrirUrl.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_ABRIR);
			menuArquivo.add(btnAbrirUrl);

		    
			miBtnSalvar.addActionListener(this);
			miBtnSalvar.setActionCommand("Salvar");
			miBtnSalvar.setMnemonic('S');
			miBtnSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK));
			miBtnSalvar.setMnemonic(KeyEvent.VK_S);
			miBtnSalvar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.BTN_SALVAR);
			miBtnSalvar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SALVAR);
			menuArquivo.add(miBtnSalvar);

			JMenuItem btnSalvarAs = new JMenuItem(XHTML_Panel.BTN_SALVAR_COMO);
			btnSalvarAs.addActionListener(this);
			btnSalvarAs.setActionCommand("SaveAs");
			btnSalvarAs.setMnemonic('c');
			btnSalvarAs.setMnemonic(KeyEvent.VK_C);
			// btnSalvarAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
			// ActionEvent.CTRL_MASK));
			btnSalvarAs.setToolTipText(XHTML_Panel.DICA_SALVAR_COMO);
			btnSalvarAs.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SALVAR_COMO);
			menuArquivo.add(btnSalvarAs);

			JMenuItem btnFechar = new JMenuItem(XHTML_Panel.SAIR);
			btnFechar.addActionListener(this);
			btnFechar.setActionCommand("Sair");
			btnFechar.setMnemonic('a');
			btnFechar.setMnemonic(KeyEvent.VK_A);
			btnFechar.setToolTipText(XHTML_Panel.DICA_SAIR);
			btnFechar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SAIR);
			menuArquivo.add(btnFechar);

			menuBar.add(menuArquivo);

			JMenu menuEditar = new JMenu(XHTML_Panel.EDITAR);
			menuBar.add(criaMenuEditar(menuEditar));

			menuBar.add(avaliadores);
			JMenu menuSimuladores = new JMenu();
			menuSilvinha.criaMenuSimuladores(menuSimuladores);
			menuBar.add(menuSimuladores);

			JMenu mnFerramenta = new JMenu();
			menuSilvinha.criaMenuFerramentas(mnFerramenta);
			menuBar.add(mnFerramenta);

			JMenu menuAjuda = new JMenu(XHTML_Panel.AJUDA);
			menuSilvinha.criaMenuAjuda(menuAjuda);
			menuBar.add(menuAjuda);

			return menuBar;
		}

		/**
		 * Recria o menu editar do Frame Principal
		 * 
		 * @param menu
		 * @return
		 */
		private JMenu criaMenuEditar(JMenu menu) {
			menu.removeAll();
			menu.setMnemonic('E');
			menu.setMnemonic(KeyEvent.VK_E);

			menu.add(new JMenuItemTeclaAtalho(textAreaSourceCode));
			
			menu.add(new JSeparator());
			
			JMenuItem btnContraste = new JMenuItem(XHTML_Panel.ALTERAR_CONTRASTE);
			btnContraste.addActionListener(this);
			btnContraste.setActionCommand("Contraste");
			// btnAumenta.setMnemonic('F');
			// btnAumenta.setMnemonic(KeyEvent.VK_F);
			// btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD,
			// ActionEvent.CTRL_MASK));
			btnContraste.setToolTipText(XHTML_Panel.DICA_CONTRASTE);
			btnContraste.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_CONTRASTE);
			menu.add(btnContraste);

			JMenuItem btnAumenta = new JMenuItem(XHTML_Panel.AUMENTA_FONTE);
			btnAumenta.addActionListener(this);
			btnAumenta.setActionCommand("AumentaFonte");
			// btnAumenta.setMnemonic('F');
			// btnAumenta.setMnemonic(KeyEvent.VK_F);
			btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));
			btnAumenta.setToolTipText(XHTML_Panel.DICA_AUMENTA_FONTE);
			btnAumenta.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_AUMENTA_FONTE);
			menu.add(btnAumenta);

			JMenuItem btnDiminui = new JMenuItem(XHTML_Panel.DIMINUI_FONTE);
			btnDiminui.addActionListener(this);
			btnDiminui.setActionCommand("DiminuiFonte");
			// btnDiminui.setMnemonic('F');
			// btnDiminui.setMnemonic(KeyEvent.VK_F);
			btnDiminui.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));
			btnDiminui.setToolTipText(XHTML_Panel.DICA_DIMINUI_FONTE);
			btnDiminui.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_DIMINUI_FONTE);
			menu.add(btnDiminui);

			menu.add(new JSeparator());

			JMenuItem btnProcurar = new JMenuItem(XHTML_Panel.PROCURAR);
			btnProcurar.addActionListener(this);
			btnProcurar.setActionCommand("Procurar");
			btnProcurar.setMnemonic('P');
			btnProcurar.setMnemonic(KeyEvent.VK_P);
			btnProcurar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, ActionEvent.CTRL_MASK));
			btnProcurar.setToolTipText(XHTML_Panel.DICA_PROCURAR);
			btnProcurar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_PROCURAR);
			menu.add(btnProcurar);

			JMenuItem btnSelecionarTudo = new JMenuItem(XHTML_Panel.SELECIONAR_TUDO);
			btnSelecionarTudo.addActionListener(this);
			btnSelecionarTudo.setActionCommand("SelecionarTudo");
			btnSelecionarTudo.setMnemonic('T');
			btnSelecionarTudo.setMnemonic(KeyEvent.VK_T);
			btnSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, ActionEvent.CTRL_MASK));
			btnSelecionarTudo.setToolTipText(XHTML_Panel.DICA_SELECIONAR_TUDO);
			btnSelecionarTudo.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SELECIONAR_TUDO);
			menu.add(btnSelecionarTudo);

			JMenuItem btnDesfazer = new JMenuItem(XHTML_Panel.DESFAZER);
			btnDesfazer.addActionListener(this);
			btnDesfazer.setActionCommand("Desfazer");
			btnDesfazer.setMnemonic('z');
			btnDesfazer.setMnemonic(KeyEvent.VK_Z);
			btnDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
			btnDesfazer.setToolTipText(XHTML_Panel.DICA_DESFAZER);
			btnDesfazer.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_DESFAZER);

			menu.add(btnDesfazer);
			return menu;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				int row = this.tabelaDeErros.getSelectedRow();
				if (row == -1)
					return;
				int linhaToGo = Integer.parseInt(tabelaDeErros.getValueAt(row, 0).toString());
				if(linhaToGo==0) return;
				textAreaSourceCode.goToLine(linhaToGo);
				textAreaSourceCode.selectLine(linhaToGo);
				erroAtual = row;
				btnAplicar.setEnabled(true);
				RegrasHardCodedEmag regra = new RegrasHardCodedEmag();
				texto.setText(regra.getAtributo(controle.getTag(erroAtual),"alt"));
			}
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		private class TabelaErros extends JTable {
			/**
			 * serialVersionUID
			 */
			private static final long serialVersionUID = 2584151976777935682L;

			DefaulTableModelNotEditable dtm;

			private TabelaErros() {
				initComponents();
			}

			public void initComponents() {
				dtm = new DefaulTableModelNotEditable();
				dtm.setColumnIdentifiers(new String[] { GERAL.LINHA, GERAL.COLUNA, GERAL.ERRO });

				setModel(dtm);
				{
					TableColumnModel cm = getColumnModel();
					cm.getColumn(0).setMaxWidth(50);
					cm.getColumn(0).setMaxWidth(50);
					cm.getColumn(1).setMaxWidth(680);
				}

			}

			public void addLinha(int linha, int coluna, String mensagem) {
				dtm.addRow(new Object[] { linha, coluna, mensagem });
			}
		}

		@Override
		public boolean showBarraUrl() {
			return false;
		}

		public void avaliaUrl(String url){
			PegarPaginaWEB ppw = new PegarPaginaWEB();
			if (url != null) {
				arquivo = null;
				try {
					String codHtml = ppw.getContent(url);
					textAreaSourceCode.setText(codHtml);
					TxtBuffer.setContentOriginal(codHtml,"0");
					reavalia(codHtml);
					this.painelOriginal.avalia(codHtml);
				} catch (HttpException e1) {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		public void setPanelOriginal(PanelLinkRedundante painelOriginal) {
		
			this.painelOriginal = painelOriginal;
			this.painelOriginal.btnPanel.setVisible(false);
			this.painelOriginal.panelCorretor.setVisible(false);
			this.painelOriginal.original=true;
		}

		private void avaliaArq(G_File temp){
			arquivo = temp;
			salvaAlteracoes.setNomeDoArquivoEmDisco(arquivo.getFile().getAbsolutePath());
			TxtBuffer.setContentOriginal(arquivo.read(),"0");
			caminhoRecente.write(arquivo.getFile().getAbsolutePath());
			frameSilvinha.setTitle(arquivo.getFile().getName() + " - " + XHTML_Panel.TIT_LINK_RED);
			avalia();
			frameSilvinha.setUrlTextField(arquivo.getFile().getAbsolutePath());
			this.painelOriginal.avalia(arquivo.read());
		}
		public void avaliaArq(String path) {
			G_File temp = new G_File(path);
			if(temp.exists()){
				avaliaArq(temp);
			}else{
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			}
		}
}



