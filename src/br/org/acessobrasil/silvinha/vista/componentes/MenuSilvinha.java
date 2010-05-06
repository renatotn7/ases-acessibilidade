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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.util.versoes.VerificadorDeVersoes;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaModel;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha.vista.listeners.SairListener;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelEscolheLinguagem;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha.vista.panels.relatorio.PainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.Silvinha;
import br.org.acessobrasil.silvinha2.mli.TradFerramentaDocType;
import br.org.acessobrasil.silvinha2.mli.XHTML_Panel;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
/**
 * Responsável pelo menu do ASES, antigo silvinha 
 *
 */
public class MenuSilvinha extends JMenuBar  implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 887238101243613278L;

	private Color corDefault = CoresDefault.getCorPaineis();
	
	public static final String MNU_ABRIR = "A";
	public static final String MNU_SALVAR = "S";
	public static final String MNU_SAIR = "R";
	public static final String MNU_EXECUTAR_AGORA = "X";
	public static final String MNU_CONFIGURAR = "C";
	public static final String MNU_ATUALIZAR = "V";
	
	public static final String MNU_LINGUAGEM = "L";
	public static final String MNU_PDF = "P";
	public static final String MNU_CSS = "CSS";
	public static final String MNU_XHTML = "XHTML";
	public static final String MNU_ROTULOS = "Rotulos";
	public static final String MNU_SILVINHA = "ASES";
	public static final String MNU_DESCRICAO = "Descricao";
	public static final String MNU_OBJETOS = "OBJETOS";
	public static final String MNU_SCRIPT = "SCRIPT";
	public static final String MNU_FRAMES = "FRAMES";
	public static final String MNU_SIMNAVEG = "SIMULADORNAVEGACAO";
	public static final String MNU_DOCTYPE = "DOCTYPE";
	public static final String MNU_CREDITOS = "CREDITOS";
	public static final String MNU_PREENCHE = "CAMPOVAZIO";
	public static final String MNU_REDUNDANTE = "LINKREDUNDANTE";
	public static final String MNU_EVENTODEPENDENTE = "EVENTODEPENDENTE";
	public static final String MNU_MANUAL = "MANUAL";
	
	
	/**
	 * Constante para baixa visão
	 */
	public static final String MNU_BV = "BAIXAVISAO";
	
	private FrameSilvinha frameSilvinha;
	private PainelAvaliacao painelAvaliacao;

	private JMenu mnArquivo;
	private JMenu mnEditar=null;
	private JMenu mnFerramenta;
	private JMenu mnSimuladores;
	
	private JMenu mnAvaliadores;
	private JMenu mnAssociadores;
	private JMenuItem miCss;
	private JMenuItem miXhtml;
	//private JMenuItem miBV;
	public static JMenuItem miSimNaveg;
	public static JMenuItem miSimBV;
	public static JMenuItem miRotulos;
	public static JMenuItem miDescricao;
	public static JMenuItem miObjetos;
	public static JMenuItem miScript;
	public static JMenuItem miFrames;
	private JMenuItem miSair;
	private JMenuItem miConf;
//	private JMenuItem miExecutar;
	private static JMenuItem miExecAgora;
	private JMenuItem miSilvinha;
	private static JMenuItem miSalvar;
	private static JMenuItem miAbrir;
	private static JMenuItem miPdf;
	JMenuItem miFerrDoctype;
	private JMenu mnOpcoes;
	private JMenuItem miAtualizar;
	private JMenuItem miManual;
	private JMenuItem miLinguagem;
	private JMenuItem miCreditos;

//	private JMenu mnAjuda;
//	private JMenuItem jMenuItem1;
//	private JMenuItem jMenuItem2;
	
	public MenuSilvinha(FrameSilvinha fs, PainelAvaliacao pp) {
		TradFerramentaDocType.carregaTexto(TokenLang.LANG);
		this.frameSilvinha = fs;
		this.painelAvaliacao = pp;
		criaMenuSilvina();
	}

	public MenuSilvinha(FrameSilvinha fs) {
		TradFerramentaDocType.carregaTexto(TokenLang.LANG);
		this.frameSilvinha = fs;
		criaMenuSilvina();
	}

	private void criaMenuSilvina(){
		mnArquivo = new JMenu();
		miSair = new JMenuItem();
		miConf = new JMenuItem();
//		miExecutar = new JMenuItem();
		miExecAgora = new JMenuItem();
		miSalvar = new JMenuItem();
		miPdf = new JMenuItem();
		miAbrir = new JMenuItem();
		mnEditar = new JMenu();
		
		mnSimuladores = new JMenu();
		mnAvaliadores = new JMenu();
		mnAssociadores = new JMenu();
		
		miLinguagem = new JMenuItem();
				
		mnAvaliadores.setText(Silvinha.AVALIADORES);
		mnAvaliadores.setToolTipText(GERAL.DICA_AVALIADORES);
		
		miCss = new JMenuItem();
		//miCss.setBackground(corDefault);
		miCss.setToolTipText(Silvinha.DICA_AVALIADOR_CSS);
		miCss.setText(Silvinha.AVALIADOR_CSS);
		//miCss = new JMenuItem();
		
		miCss.setActionCommand(MenuSilvinha.MNU_CSS);
		miCss.addActionListener(this);
		//mnFerramenta.add(miCss);
		mnAvaliadores.add(miCss);
		
		miXhtml = new JMenuItem();
		//miXhtml.setBackground(corDefault);
		miXhtml.setToolTipText(Silvinha.DICA_AVALIADOR_HTML);
		miXhtml.setText(Silvinha.AVALIADOR_HTML);
		
		miXhtml.setActionCommand(MenuSilvinha.MNU_XHTML);
		miXhtml.addActionListener(this);
		//mnFerramenta.add(miXhtml);
		mnAvaliadores.add(miXhtml);
		
		//miBV = new JMenuItem();
		//miBV.setActionCommand(MenuSilvinha.MNU_BV);
		//miBV.addActionListener(this);
		//miBV.setText(Silvinha.AVALIADOR_BV);
		//mnAvaliadores.add(miBV);
		
		miSilvinha = new JMenuItem();
		//miXhtml.setBackground(corDefault);
		miSilvinha.setToolTipText(Silvinha.DICA_AVALIADOR_ASES);
		miSilvinha.setText(Silvinha.AVALIADOR_ASES);
		
		miSilvinha.setActionCommand(MenuSilvinha.MNU_SILVINHA);
		miSilvinha.addActionListener(this);
		mnAvaliadores.add(miSilvinha);
		
		//mnArquivo.add(mnAvaliadores);
	
//		mnAjuda = new JMenu();
//		jMenuItem1 = new JMenuItem();
//		jMenuItem2 = new JMenuItem();

		this.setBackground(corDefault);
		this.setPreferredSize(new Dimension(0, 20));
		mnArquivo.setBackground(corDefault);
		mnArquivo.setText(Silvinha.ARQUIVO);
		mnArquivo.setToolTipText(Silvinha.DICA_ARQUIVO);
		mnArquivo.getAccessibleContext().setAccessibleName(Silvinha.ARQUIVO);
		mnArquivo.getAccessibleContext().setAccessibleDescription(Silvinha.DICA_ARQUIVO);
		//mnArquivo.setMnemonic('A');

		miSair.setText(Silvinha.SAIR);
		miSair.setToolTipText(Silvinha.DICA_SAIR);
		miSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
		miSair.setMnemonic('X');
		miSair.setActionCommand(MenuSilvinha.MNU_SAIR);
		miSair.addActionListener(new SairListener(frameSilvinha));
		miSair.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

//		miExecutar.setText(TokenLang.EXEC_MNU);
//		miExecutar.setMnemonic('E');
//		miExecutar.setActionCommand(MenuSilvinha.MNU_EXECUTAR);
//		miExecutar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
//		miExecutar.addActionListener(painelAvaliacao);

		miConf.setText(Silvinha.CONFIGURACAO);
		miConf.setToolTipText(Silvinha.DICA_CONFIGURACAO);
		miConf.setMnemonic('C');
		miConf.setActionCommand(MenuSilvinha.MNU_CONFIGURAR);
		miConf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		miConf.addActionListener(this);

		miExecAgora.setText(Silvinha.EXECUTE_AGORA);
		miExecAgora.setToolTipText(Silvinha.DICA_EXECUTE_AGORA);
		miExecAgora.setMnemonic('A');
		miExecAgora.setActionCommand(MenuSilvinha.MNU_EXECUTAR_AGORA);
		miExecAgora.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		if(painelAvaliacao==null){
			miExecAgora.setEnabled(false);
		}else{			
			miExecAgora.addActionListener(painelAvaliacao);
		}
		miSalvar.setText(Silvinha.BTN_SALVAR);
		miSalvar.setToolTipText(Silvinha.DICA_SALVAR);
		miSalvar.setMnemonic('S');
		miSalvar.setActionCommand(MenuSilvinha.MNU_SALVAR);
		miSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		miSalvar.setEnabled(false);
		miSalvar.addActionListener(this);
		
		miPdf.setText(Silvinha.EXPORTA_PDF);
		miPdf.setToolTipText(Silvinha.DICA_EXPORTA_PDF);
		miPdf.setMnemonic('P');
		miPdf.setActionCommand(MenuSilvinha.MNU_PDF);
		miPdf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		miPdf.setEnabled(false);
		miPdf.addActionListener(this);

		miAbrir.setText(Silvinha.BTN_ABRIR);
		miAbrir.setToolTipText(Silvinha.DICA_ABRIR_MENU);
		miAbrir.setMnemonic('B');
		miAbrir.setActionCommand(MenuSilvinha.MNU_ABRIR);
		miAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		miAbrir.addActionListener(this);

//		mnArquivo.add(miExecutar);
		mnArquivo.add(miExecAgora);
		mnArquivo.add(miConf);
		mnArquivo.add(new JSeparator());
		mnArquivo.add(miAbrir);
		mnArquivo.add(miSalvar);
		mnArquivo.add(miPdf);
		mnArquivo.add(new JSeparator());
		mnArquivo.add(miSair);
		
		mnOpcoes = new JMenu();
		criaMenuAjuda(mnOpcoes);
		/*
		mnOpcoes.setBackground(corDefault);
		mnOpcoes.setText(Silvinha.AJUDA);
		mnOpcoes.setToolTipText(Silvinha.OPCOES_MENU);
		mnOpcoes.setMnemonic('O');

		miAtualizar.setText(Silvinha.BUSCAR_ATLZ);
		miAtualizar.setToolTipText(Silvinha.DICA_BUSCAR_ATLZ);
		miAtualizar.setMnemonic('V');
		miAtualizar.setActionCommand(MenuSilvinha.MNU_ATUALIZAR);
		miAtualizar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
		miAtualizar.addActionListener(this);

		miCreditos.setText(GERAL.ASES_CREDITOS);
		miCreditos.setToolTipText(GERAL.DICA_CREDITOS);
		miCreditos.setMnemonic('C');
		miCreditos.setActionCommand(MenuSilvinha.MNU_CREDITOS);
		miCreditos.addActionListener(this);
		mnOpcoes.add(miAtualizar);
		mnOpcoes.add(miCreditos);
		*/
		miLinguagem.setText(Silvinha.IDIOMAS);
		miLinguagem.setToolTipText(Silvinha.DICA_IDIOMAS);
		//miLinguagem.setMnemonic('L');
		miLinguagem.setActionCommand(MenuSilvinha.MNU_LINGUAGEM);
		//miLinguagem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		miLinguagem.addActionListener(this);

		
		//mnOpcoes.add(miLinguagem);

		/*
		 * Ini - Editar
		 */
		mnEditar.setText(Silvinha.EDITAR);
		mnEditar.setEnabled(false);
		
		/*
		 * Ini - Ferramentas 
		 */
						
		mnFerramenta = new JMenu();
		
		criaMenuFerramentas(mnFerramenta);
		
		mnSimuladores = new JMenu();
		criaMenuSimuladores(mnSimuladores);
		
		/*
		mnFerramenta.setBackground(corDefault);
		mnFerramenta.setText("Ferramentas");
		mnFerramenta.setToolTipText("Ferramentas de reparo");
		mnFerramenta.setMnemonic('r');
						
		miRotulos = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miRotulos.setToolTipText("Associa rótulos em controles de formulários");
		miRotulos.setText("Associador de rótulos");
		
		miRotulos.setActionCommand(MenuSilvinha.MNU_ROTULOS);
		miRotulos.addActionListener(this);
		mnFerramenta.add(miRotulos);
		*/
		
		/*
		 * Fim - Ferramentas
		 */
			
		this.add(mnArquivo);
		this.add(mnEditar);
		this.add(mnAvaliadores);
		this.add(mnSimuladores);
		this.add(mnFerramenta);
		this.add(mnOpcoes);

//		mnAjuda.setBackground(corDefault);
//		mnAjuda.setText(TokenLang.HELP_MNU);
//		mnAjuda.setMnemonic('A');
//		jMenuItem1.setText(TokenLang.HELP_MNU);
//		jMenuItem1.setMnemonic('j');
//		jMenuItem1.setAccelerator(KeyStroke.getKeyStroke("F1"));
//		mnAjuda.add(jMenuItem1);
//
//		jMenuItem2.setText(TokenLang.ABOUT_MNU);
//		jMenuItem2.setMnemonic('S');
//		jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
//		ActionEvent.ALT_MASK));
//		mnAjuda.add(jMenuItem2);
		
		if(FrameSilvinha.VISTA_ANTIGO_SILVINHA){
			mnAvaliadores.setVisible(false);
			mnSimuladores.setVisible(false);
			mnFerramenta.setVisible(false);
			mnEditar.setVisible(false);
			miCreditos.setVisible(false);
		}
	}
	/**
	 * Cria o menu com os avaliadores
	 * @param avaliadores
	 */
	public void criaMenuAvaliadores(JMenu avaliadores){
		
		avaliadores.setText(Silvinha.AVALIADORES);
		miCss = new JMenuItem();
		//miCss.setBackground(corDefault);
		miCss.setToolTipText(Silvinha.MENU_AVALIADOR_CSS);
		miCss.setText(Silvinha.AVALIADOR_CSS);
		//miCss = new JMenuItem();
		
		miCss.setActionCommand(MenuSilvinha.MNU_CSS);
		miCss.addActionListener(this);
		//mnFerramenta.add(miCss);
		avaliadores.add(miCss);
		
		miXhtml = new JMenuItem();
		//miXhtml.setBackground(corDefault);
		miXhtml.setToolTipText(Silvinha.MENU_AVALIADOR_HTML);
		miXhtml.setText(Silvinha.AVALIADOR_HTML);
		
		miXhtml.setActionCommand(MenuSilvinha.MNU_XHTML);
		miXhtml.addActionListener(this);
		//mnFerramenta.add(miXhtml);
		avaliadores.add(miXhtml);
		
		miSilvinha = new JMenuItem();
		//miXhtml.setBackground(corDefault);
		miSilvinha.setToolTipText(Silvinha.DICA_AVALIADOR_ASES);
		miSilvinha.setText(Silvinha.AVALIADOR_ASES);
		
		miSilvinha.setActionCommand(MenuSilvinha.MNU_SILVINHA);
		miSilvinha.addActionListener(this);
		avaliadores.add(miSilvinha);
	}
	
	public void criaMenuSimuladores(JMenu mnSimuladores){
		mnSimuladores.setBackground(corDefault);
		mnSimuladores.setText(Silvinha.SIMULADORES);
		mnSimuladores.setToolTipText(Silvinha.DICA_SIMULADORES);
		miSimNaveg = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miSimNaveg.setToolTipText(Silvinha.DICA_NAVEGADOR_CEGO);
		miSimNaveg.setText(Silvinha.CEGOS);
		
		miSimNaveg.setActionCommand(MenuSilvinha.MNU_SIMNAVEG);
		miSimNaveg.addActionListener(this);
		mnSimuladores.add(miSimNaveg);
		
		JMenuItem miSimBV = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miSimBV.setToolTipText(Silvinha.DICA_NAVEGADOR_BV);
		miSimBV.setText(Silvinha.BAIXA_VISAO);
		
		miSimBV.setActionCommand(MenuSilvinha.MNU_BV);
		miSimBV.addActionListener(this);
		mnSimuladores.add(miSimBV);
		//mnSimuladores.setMnemonic('');
		//mnSimuladores.add(mnNavegadorBV);
	}
	/**
	 * Cria o menu de ajuda
	 */
	public void criaMenuAjuda(JMenu mnOpcoes){
		mnOpcoes.setBackground(corDefault);
		mnOpcoes.setText(Silvinha.AJUDA);
		//mnOpcoes.setToolTipText(Silvinha.OPCOES_MENU);
		mnOpcoes.setToolTipText(Silvinha.DICA_MENU_AJUDA);
		//mnOpcoes.setMnemonic('O');
		miAtualizar = new JMenuItem();
		miManual = new JMenuItem();
		miCreditos = new JMenuItem();
				
		miAtualizar.setText(Silvinha.BUSCAR_ATLZ);
		miAtualizar.setToolTipText(Silvinha.DICA_BUSCAR_ATLZ);
		miAtualizar.setMnemonic('V');
		miAtualizar.setActionCommand(MenuSilvinha.MNU_ATUALIZAR);
		miAtualizar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
		miAtualizar.addActionListener(this);
		
		miManual.setText(Silvinha.MANUAL);
		miManual.setToolTipText(Silvinha.DICA_MANUAL);
		miManual.setMnemonic('M');
		miManual.setActionCommand(MenuSilvinha.MNU_MANUAL);
		miManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
		miManual.addActionListener(this);

		miCreditos.setText(GERAL.ASES_CREDITOS);
		miCreditos.setToolTipText(GERAL.DICA_CREDITOS);
		miCreditos.setMnemonic('I');
		miCreditos.setActionCommand(MenuSilvinha.MNU_CREDITOS);
		miCreditos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
		miCreditos.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Creditos();
			}
		});
		
		mnOpcoes.add(miAtualizar);
		mnOpcoes.add(miManual);
		mnOpcoes.add(miCreditos);
	}
	
	/**
	 * Cria o menu de ferramentas
	 */
	public void criaMenuFerramentas(JMenu mnFerramenta){
		mnFerramenta.setBackground(corDefault);
		mnFerramenta.setText(Silvinha.FERRAMENTAS);
		mnFerramenta.setToolTipText(Silvinha.DICA_FERRAMENTAS);
		mnFerramenta.setMnemonic('r');
		mnAssociadores=new JMenu();
		mnAssociadores.setText(Silvinha.ASSOCIADORES);	
		mnFerramenta.add(mnAssociadores);
		
		miRotulos = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miRotulos.setToolTipText(Silvinha.DICA_ASSOCIADOR_ROTULO);
		miRotulos.setText(Silvinha.ASSOCIADOR_ROTULO);
		
		miRotulos.setActionCommand(MenuSilvinha.MNU_ROTULOS);
		miRotulos.addActionListener(this);
		mnFerramenta.add(miRotulos);
		
		miDescricao = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miDescricao.setToolTipText(Silvinha.DICA_ASSOCIADOR_IMAGEM);
		miDescricao.setText("IMG");
		
		miDescricao.setActionCommand(MenuSilvinha.MNU_DESCRICAO);
		miDescricao.addActionListener(this);
		
		mnAssociadores.add(miDescricao);
		miObjetos = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miObjetos.setToolTipText(Silvinha.DICA_ASSOCIADOR_OBJETOS);
		miObjetos.setText("OBJECT");
		
		miObjetos.setActionCommand(MenuSilvinha.MNU_OBJETOS);
		miObjetos.addActionListener(this);
		
		mnAssociadores.add(miObjetos);
		miScript = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miScript.setToolTipText(Silvinha.DICA_ASSOCIADOR_OBJETOS);
		miScript.setText("SCRIPT");
		
		miScript.setActionCommand(MenuSilvinha.MNU_SCRIPT);
		miScript.addActionListener(this);
				
		mnAssociadores.add(miScript);
		miFrames = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miFrames.setToolTipText(Silvinha.DICA_ASSOCIADOR_OBJETOS);
		miFrames.setText("FRAME");
		
		miFrames.setActionCommand(MenuSilvinha.MNU_FRAMES);
		miFrames.addActionListener(this);
		
		miFerrDoctype = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miFerrDoctype.setToolTipText(TradFerramentaDocType.DICA_FERRAMENTA_ESCOLHA_DTD);
		miFerrDoctype.setText(TradFerramentaDocType.EDITOR_DTD);
		
		miFerrDoctype.setActionCommand(MNU_DOCTYPE);
		miFerrDoctype.setToolTipText(GERAL.DICA_DOCTYPE);
		miFerrDoctype.addActionListener(this);
		mnFerramenta.add(miFerrDoctype);
		
		JMenuItem miFerrPreenchedor = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miFerrPreenchedor.setToolTipText(TradFerramentaDocType.DICA_PREENCHEDOR_FORM);
		miFerrPreenchedor.setText(TradFerramentaDocType.PREENCHEDOR_FORM);
		
		miFerrPreenchedor.setActionCommand(MNU_PREENCHE);
		miFerrPreenchedor.addActionListener(this);
		mnFerramenta.add(miFerrPreenchedor);
		
		JMenuItem miFerrLinkRedundante = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miFerrLinkRedundante.setToolTipText(TradFerramentaDocType.DICA_LINKS_REDUNDANTES);
		miFerrLinkRedundante.setText(TradFerramentaDocType.LINKS_REDUNDANTES);
		
		miFerrLinkRedundante.setActionCommand(MNU_REDUNDANTE);
		miFerrLinkRedundante.addActionListener(this);
		mnFerramenta.add(miFerrLinkRedundante);
		
		
		JMenuItem miFerrEventoDependente = new JMenuItem();
		//miRotulos.setBackground(corDefault);
		miFerrEventoDependente.setToolTipText(TradFerramentaDocType.DICA_EVENTODEPENDENTE);
		miFerrEventoDependente.setText(TradFerramentaDocType.EVENTODEPENDENTE);
		
		miFerrEventoDependente.setActionCommand(MNU_EVENTODEPENDENTE);
		miFerrEventoDependente.addActionListener(this);
		mnFerramenta.add(miFerrEventoDependente);
		
	}
	/**
	 * Cria o menu default para as ferramentas e avaliações
	 * @param frameSilvinha
	 * @param actionListener deve contemplar as ações Abrir, AbrirURL, Salvar, SaveAs, Sair
	 * @param miBtnSalvar
	 * @param textAreaSourceCode
	 * @return JMenuBar
	 */
	public static JMenuBar criaMenuBar(FrameSilvinha frameSilvinha,ActionListener actionListener,JMenuItem miBtnSalvar,G_TextAreaSourceCode textAreaSourceCode) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(CoresDefault.getCorPaineis());
	
		JMenu menuArquivo = new JMenu(XHTML_Panel.ARQUIVO);
		//menuArquivo.setMnemonic('A');
		//menuArquivo.setMnemonic(KeyEvent.VK_A);
	
		JMenu avaliadores = new JMenu();
		MenuSilvinha menuSilvinha = new MenuSilvinha(frameSilvinha, null);
		menuSilvinha.criaMenuAvaliadores(avaliadores);
		// menuArquivo.add(avaliadores);
		// menuArquivo.add(new JSeparator());
	
		JMenuItem btnAbrir = new JMenuItem(XHTML_Panel.BTN_ABRIR);
		btnAbrir.addActionListener(actionListener);
		btnAbrir.setActionCommand("Abrir");
		//btnAbrir.setMnemonic('A');
		btnAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		//btnAbrir.setMnemonic(KeyEvent.VK_A);
		btnAbrir.setToolTipText(XHTML_Panel.DICA_ABRIR);
		btnAbrir.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_ABRIR);
		menuArquivo.add(btnAbrir);
	
		JMenuItem btnAbrirUrl = new JMenuItem(XHTML_Panel.BTN_ABRIR_URL);
		btnAbrirUrl.addActionListener(actionListener);
		btnAbrirUrl.setActionCommand("AbrirURL");
		btnAbrirUrl.setMnemonic('U');
		btnAbrirUrl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		btnAbrirUrl.setMnemonic(KeyEvent.VK_U);
		btnAbrirUrl.setToolTipText(XHTML_Panel.DICA_ABRIR);
		btnAbrirUrl.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_ABRIR);
		menuArquivo.add(btnAbrirUrl);
	
		miBtnSalvar = new JMenuItem(XHTML_Panel.BTN_SALVAR);
		miBtnSalvar.addActionListener(actionListener);
		miBtnSalvar.setActionCommand("Salvar");
		miBtnSalvar.setMnemonic('S');
		miBtnSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		miBtnSalvar.setMnemonic(KeyEvent.VK_S);
		miBtnSalvar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.BTN_SALVAR);
		miBtnSalvar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SALVAR);
		menuArquivo.add(miBtnSalvar);
	
		JMenuItem btnSalvarAs = new JMenuItem(XHTML_Panel.BTN_SALVAR_COMO);
		btnSalvarAs.addActionListener(actionListener);
		btnSalvarAs.setActionCommand("SaveAs");
		//btnSalvarAs.setMnemonic('c');
		//btnSalvarAs.setMnemonic(KeyEvent.VK_C);
		// btnSalvarAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
		// ActionEvent.CTRL_MASK));
		btnSalvarAs.setToolTipText(XHTML_Panel.DICA_SALVAR_COMO);
		btnSalvarAs.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SALVAR_COMO);
		menuArquivo.add(btnSalvarAs);
	
		JMenuItem btnFechar = new JMenuItem(XHTML_Panel.SAIR);
		btnFechar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnFechar.setActionCommand("Sair");
		btnFechar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
		btnFechar.setMnemonic('X');
		btnFechar.setToolTipText(XHTML_Panel.DICA_SAIR);
		btnFechar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SAIR);
		menuArquivo.add(btnFechar);
		
		menuBar.add(menuArquivo);
	
		JMenu menuEditar = new JMenu(XHTML_Panel.EDITAR);
		
		menuBar.add(criaMenuEditar(menuEditar,actionListener,textAreaSourceCode));
	
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
	 * @param menu
	 * @return
	 */
	private static JMenu criaMenuEditar(JMenu menu,ActionListener actionListener,G_TextAreaSourceCode textAreaSourceCode) {
		menu.removeAll();
		menu.setMnemonic('E');
		menu.setMnemonic(KeyEvent.VK_E);
		
		/*
		 * Coloca o ítem para adicionar teclas de atalho para cegos 
		 * no código html
		 */
		if(textAreaSourceCode!=null){
			menu.add(new JMenuItemTeclaAtalho(textAreaSourceCode));
			menu.add(new JSeparator());
		}
		
		JMenuItem btnContraste = new JMenuItem(XHTML_Panel.ALTERAR_CONTRASTE);
		btnContraste.addActionListener(actionListener);
		btnContraste.setActionCommand("Contraste");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		// btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD,
		// ActionEvent.CTRL_MASK));
		btnContraste.setToolTipText(XHTML_Panel.DICA_CONTRASTE);
		btnContraste.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_CONTRASTE);
		menu.add(btnContraste);
	
		JMenuItem btnAumenta = new JMenuItem(XHTML_Panel.AUMENTA_FONTE);
		btnAumenta.addActionListener(actionListener);
		btnAumenta.setActionCommand("AumentaFonte");
		// btnAumenta.setMnemonic('F');
		// btnAumenta.setMnemonic(KeyEvent.VK_F);
		btnAumenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, ActionEvent.CTRL_MASK));
		btnAumenta.setToolTipText(XHTML_Panel.DICA_AUMENTA_FONTE);
		btnAumenta.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_AUMENTA_FONTE);
		menu.add(btnAumenta);
	
		JMenuItem btnDiminui = new JMenuItem(XHTML_Panel.DIMINUI_FONTE);
		btnDiminui.addActionListener(actionListener);
		btnDiminui.setActionCommand("DiminuiFonte");
		// btnDiminui.setMnemonic('F');
		// btnDiminui.setMnemonic(KeyEvent.VK_F);
		btnDiminui.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK));
		btnDiminui.setToolTipText(XHTML_Panel.DICA_DIMINUI_FONTE);
		btnDiminui.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_DIMINUI_FONTE);
		menu.add(btnDiminui);
	
		menu.add(new JSeparator());
		/*
		JMenuItem btnProcurar = new JMenuItem(XHTML_Panel.PROCURAR);
		btnProcurar.addActionListener(actionListener);
		btnProcurar.setActionCommand("Procurar");
		btnProcurar.setMnemonic('P');
		btnProcurar.setMnemonic(KeyEvent.VK_P);
		btnProcurar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		btnProcurar.setToolTipText(XHTML_Panel.DICA_PROCURAR);
		btnProcurar.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_PROCURAR);
		menu.add(btnProcurar);
		*/
		
		JMenuItem btnSelecionarTudo = new JMenuItem(XHTML_Panel.SELECIONAR_TUDO);
		btnSelecionarTudo.addActionListener(actionListener);
		btnSelecionarTudo.setActionCommand("SelecionarTudo");
		btnSelecionarTudo.setMnemonic('T');
		btnSelecionarTudo.setMnemonic(KeyEvent.VK_T);
		btnSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		btnSelecionarTudo.setToolTipText(XHTML_Panel.DICA_SELECIONAR_TUDO);
		btnSelecionarTudo.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_SELECIONAR_TUDO);
		menu.add(btnSelecionarTudo);
	
		JMenuItem btnDesfazer = new JMenuItem(XHTML_Panel.DESFAZER);
		btnDesfazer.addActionListener(actionListener);
		btnDesfazer.setActionCommand("Desfazer");
		btnDesfazer.setMnemonic('z');
		btnDesfazer.setMnemonic(KeyEvent.VK_Z);
		btnDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		btnDesfazer.setToolTipText(XHTML_Panel.DICA_DESFAZER);
		btnDesfazer.getAccessibleContext().setAccessibleDescription(XHTML_Panel.DICA_DESFAZER);
	
		menu.add(btnDesfazer);
		return menu;
	}
	public static void abreManual(){
		//*
		
//		Primeiro verificamos se é possível a integração com o desktop
		if (!Desktop.isDesktopSupported()){
		    throw new IllegalStateException("Desktop resources not supported!");
		}
		Desktop desktop = null;
		desktop = Desktop.getDesktop();
//		Agora vemos se é possível disparar o browser default.
		if (!desktop.isSupported(Desktop.Action.BROWSE))
		    throw new IllegalStateException("No default browser set!");

//		Pega a URI de um componente de texto.
		
		try {
			//URI uri;
			//uri = new URI("./teste/acesso.html");
			//uri = new URI("file://www.globo.com");
			//System.out.println(uri.resolve("../teste/acesso.html").toString());
			//uri = new URI(a.getUrlPath());
			//uri = new URI("file:///Documents%20and%20Settings/Administrador.ADMINISTRADOR/Desktop/Integracao15b/teste/acesso.html");
			//Dispara o browser default, que pode ser o Explorer, Firefox ou outro.
			//desktop.browse(uri);

			G_File a = new G_File("ajuda/"+TokenLang.LANG +"/manual/indice.html");
			desktop.open(a.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//*/
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals(MenuSilvinha.MNU_CONFIGURAR)) {
			try {
				frameSilvinha.showPainelConfig();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals(MenuSilvinha.MNU_MANUAL)) {
			abreManual();
		} else if (command.equals(MenuSilvinha.MNU_PREENCHE)) {
			frameSilvinha.showPainelPreencheCampo();
		} else if (command.equals(MenuSilvinha.MNU_CREDITOS)) {
			new Creditos();
		} else if (command.equals(MenuSilvinha.MNU_ATUALIZAR)) {
			JOptionPane.showMessageDialog(frameSilvinha, Silvinha.BUSCA_ATULIZACAO_SERVIDOR);
			VerificadorDeVersoes vv = new VerificadorDeVersoes(TokenLang.getProperties().getProperty("serv_versao"));
			boolean realizaAtualizacao = vv.verificarVersao(SilvinhaModel.getVersao());
			if (realizaAtualizacao) {
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(frameSilvinha, Silvinha.SEM_ATUALIZACOES);
			}
		} else if (command.equals(MenuSilvinha.MNU_SALVAR)) {
			Gerente.pausa=true;
			Object[] options = {//TokenLang.CMB_SALVAR_OPCAO_1,
						        Silvinha.SALVAR_RELATORIO_COMPLETO, 
						        Silvinha.BTN_CANCELAR};
			ResumoDoRelatorio resumo = null;
			int option = JOptionPane.showOptionDialog(frameSilvinha, Silvinha.DICA_OPCOES_RESUMO, 
													Silvinha.DICA_SALVAR_AVALIACAO,
													//TokenLang.LBL_SALVAR_AVALIACAO, 
						                              //JOptionPane.YES_NO_CANCEL_OPTION,
						                              JOptionPane.YES_NO_OPTION,
						                              JOptionPane.QUESTION_MESSAGE, null,  options, 
						                              //options[2]);
						                              options[1]);
			
			if (option == JOptionPane.YES_OPTION) {
				resumo = PainelResumo.getResumoDoRelatorio();
				//resumo.setGravaCompleto(false);
				resumo.setGravaCompleto(true);
            } else if (option == JOptionPane.NO_OPTION) {
				//resumo = PainelResumo.getResumoDoRelatorio();
				//resumo.setGravaCompleto(true);
            //} else if (option == JOptionPane.CANCEL_OPTION) {
            	JOptionPane.showMessageDialog(null, Silvinha.GRAVACAO_CANCELADA);
            } 
			if (option != JOptionPane.CANCEL_OPTION && resumo != null) {
				new SalvarArquivoFileChooser(frameSilvinha, resumo);
			}
		} else if (command.equals(MenuSilvinha.MNU_ABRIR)) {
			new AbrirArquivoFileChooser(frameSilvinha);
		} else if (command.equals(MenuSilvinha.MNU_LINGUAGEM)) {
			try {
				new PainelEscolheLinguagem(frameSilvinha);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (command.equals(MenuSilvinha.MNU_REDUNDANTE)) {
			frameSilvinha.showPainelFerramentaLinksRedundantes();
		} else if (command.equals(MenuSilvinha.MNU_EVENTODEPENDENTE)) {
			frameSilvinha.showPainelFerramentaEventoDependente();
		} else if (command.equals(MenuSilvinha.MNU_CSS)) {
			frameSilvinha.showPainelFerramentaCSS();
		} else if (command.equals(MenuSilvinha.MNU_XHTML)) {
			frameSilvinha.showPainelFerramentaXHTML();
		} else if (command.equals(MenuSilvinha.MNU_ROTULOS)){
			G_File arq = new G_File("buffer");
			if(PainelRelatorio.relatorio!=null){
				arq.write(PainelRelatorio.relatorio.getConteudo().toString());	
				//new FerramentaLabel("buffer");
			}
			frameSilvinha.showPainelFerramentaLabel();
		}else if (command.equals(MenuSilvinha.MNU_DESCRICAO)){
			G_File arq = new G_File("buffer");
			if(PainelRelatorio.relatorio!=null){
				arq.write(PainelRelatorio.relatorio.getConteudo().toString());	
				//new FerramentaLabel("buffer");
			}
			frameSilvinha.showPainelFerramentaDescricao() ;
		}else if (command.equals(MenuSilvinha.MNU_DOCTYPE)){
			G_File arq = new G_File("buffer");
			if(PainelRelatorio.relatorio!=null){
				arq.write(PainelRelatorio.relatorio.getConteudo().toString());	
				//new FerramentaLabel("buffer");
			}
			frameSilvinha.showPainelFerramentaDoctype() ;
		}else if (command.equals(MenuSilvinha.MNU_OBJETOS)){
			G_File arq = new G_File("buffer");
			if(PainelRelatorio.relatorio!=null){
				arq.write(PainelRelatorio.relatorio.getConteudo().toString());	
				//new FerramentaLabel("buffer");
			}
			frameSilvinha.showPainelFerramentaDescricaoObjetos() ;
		}else if (command.equals(MenuSilvinha.MNU_SCRIPT)){
			G_File arq = new G_File("buffer");
			if(PainelRelatorio.relatorio!=null){
				arq.write(PainelRelatorio.relatorio.getConteudo().toString());	
				//new FerramentaLabel("buffer");
			}
			frameSilvinha.showPainelFerramentaDescricaoScript() ;
		}else if(command.equals(MenuSilvinha.MNU_SIMNAVEG)){
			
			G_File arq = new G_File("buffer");
			if(PainelRelatorio.relatorio!=null){
				arq.write(PainelRelatorio.relatorio.getConteudo().toString());	
				//new FerramentaLabel("buffer");
			}
			frameSilvinha.showPainelSimuladorNavegacao() ;
			
			
		}else if (command.equals(MenuSilvinha.MNU_PDF)) {
			Object[] options = {Silvinha.SALVAR_PDF,
						        Silvinha.BTN_CANCELAR};
			ResumoDoRelatorio resumo = null;
			int option = JOptionPane.showOptionDialog(frameSilvinha, Silvinha.DICA_SALVAR_PDF, 
						                              Silvinha.SALVAR_AVALIACAO, JOptionPane.YES_NO_OPTION,
						                              JOptionPane.QUESTION_MESSAGE, null,  options, options[1]);
			if (option == JOptionPane.YES_OPTION) {
				resumo = PainelResumo.getResumoDoRelatorio();
				resumo.setGravaCompleto(true);
            } else if (option == JOptionPane.NO_OPTION) {
            	JOptionPane.showMessageDialog(null, Silvinha.GRAVACAO_CANCELADA);
            } 
			if (option != JOptionPane.NO_OPTION && resumo != null) {
				new ExportarPdfFileChooser(frameSilvinha, resumo);
			}
		}else if (command.equals(MenuSilvinha.MNU_SILVINHA)) {
			frameSilvinha.showPainelAvaliacao();
		}else if (command.equals(MenuSilvinha.MNU_BV)) {
			Runtime r = Runtime.getRuntime();
			
			String os=System.getProperty("os.name");
			//System.out.println("OS.name="+os);
			if(os.toLowerCase().equals("linux")){
				try {
					//Process p = 
					r.exec("dist_linux/start.sh");
					//Scanner sc = new Scanner(p.getInputStream());
					
					//while (sc.hasNextLine()) {
					//	String msg=sc.nextLine();
						//System.out.println(msg);
					//	if(msg.equals("dispose ok")) break;
					//}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				//System.out.print("Fim");
			}else{
				try {
					//Process p = Runtime.getRuntime().exec("java -jar bv.jar");
					//Process p = 
					Runtime.getRuntime().exec("javaw -jar dist_win/bv.jar");
					//Process p = r.exec("dir");
					/*
					Scanner sc = new Scanner(p.getInputStream());
					while (sc.hasNextLine()) {
						String msg=sc.nextLine();
						System.out.println(msg);
						if(msg.equals("dispose ok")) break;
					}
					//*/
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				//System.out.print("Fim");
			}
		}
	}
	
	public static void habilitaSalvar() {
		miSalvar.setEnabled(true);
	}
	
	public static void desabilitaSalvar() {
		miSalvar.setEnabled(false);
	}
	public static void desabilitaExecutarAgora(){
		miExecAgora.setEnabled(false);
	}
	public static void habilitaExecutarAgora(){
		miExecAgora.setEnabled(true);
	}
	public static void habilitaPdf() {
		miPdf.setEnabled(true);
	}
	
	public static void desabilitaPdf() {
		miPdf.setEnabled(false);
	}

	public static void habilitaAbrir() {
		miAbrir.setEnabled(true);
	}
	
	public static void desabilitaAbrir() {
		miAbrir.setEnabled(false);
	}

	public JMenu getMnEditar() {
		return mnEditar;
	}

	public void setMnEditar(JMenu mnEditar) {
		this.mnEditar = mnEditar;
	}

	public static JMenuItem getMiAbrir() {
		return miAbrir;
	}

	public static void setMiAbrir(JMenuItem miAbrir) {
		MenuSilvinha.miAbrir = miAbrir;
	}
}
