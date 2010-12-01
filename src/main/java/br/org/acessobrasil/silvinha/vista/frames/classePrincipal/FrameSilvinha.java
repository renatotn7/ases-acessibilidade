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

package br.org.acessobrasil.silvinha.vista.frames.classePrincipal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.corretor_eventos.PainelTabbedCorretorEventos;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.css.PainelTabbedCSS;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.doctype.PainelTabbedDoctype;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.PainelTabbedImg;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.PanelDescricaoImagens;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.analise_geral.PanelAnaliseGeral;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.label.PainelLabel;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.label.PainelTabbedLabel;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.links_redundantes.PainelTabbedLinkRedundante;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.navegacao_cego.PainelSimuladorNavegacao;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.objeto.PainelObjeto;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.objeto.PainelTabbedObjeto;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.preenchedor_formulario.PainelTabbedPreenchedorFormulario;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.script.PainelTabbedScript;
import br.org.acessobrasil.ases.ferramentas_de_reparo.vista.xhtml.PainelTabbedXHTML;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.util.PropertyLoader;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.listeners.SilvinhaWindowListener;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelChaveDeSeguranca;
import br.org.acessobrasil.silvinha.vista.panels.PainelConfig;
import br.org.acessobrasil.silvinha.vista.panels.PainelCorrecao;
import br.org.acessobrasil.silvinha.vista.panels.PainelLogo;
import br.org.acessobrasil.silvinha.vista.panels.PainelResumo;
import br.org.acessobrasil.silvinha.vista.panels.PainelSplash;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha.vista.panels.SuperPainelCentral;
import br.org.acessobrasil.silvinha.vista.panels.relatorio.PainelRelatorio;
import br.org.acessobrasil.silvinha2.mli.Silvinha;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.projetodosite.ConfiguracaoDaAvaliacao;
import br.org.acessobrasil.silvinha2.util.G_UrlTextField;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
import br.org.acessobrasil.silvinha2.util.G_UrlTextField.OnComboBoxEdited;

/**
 * Classe principal do sistema Silvinha.
 * 
 * @author mariano
 */
public class FrameSilvinha extends JFrame implements OnComboBoxEdited {

	private static final long serialVersionUID = -3348105187778647671L;
	
	public final Color corDefault = CoresDefault.getCorPaineis();

	/**
	 * boolean que para esta avaliacao
	 */
	public static boolean stopAvaliacao = true;

	/**
	 * Guarda a instancia do painel relatorio
	 */
	public PainelRelatorio painelRelatorio;

	/**
	 * Caso true vira versão demonstração
	 */
	public static final boolean limitado = false;

	/**
	 * Caso true coloca os logos como o do silvinha antigo
	 */
	public static final boolean VISTA_ANTIGO_SILVINHA = false;
	
	/**
	 * Pula a tela de registro com CNPJ
	 */
	public static final boolean PULA_REGISTRO = true;

	public WindowAdapter wa;

	private PainelAvaliacao pnAvaliacao;

	/**
	 * Guarda o menu principal
	 */
	private MenuSilvinha menuBar;

	private static PainelLogo pnContLogo;

	public static SuperPainelCentral pnCentral;

	private static SuperPainelCentral lastActivePanel;
	
	/**
	 * Espaço para colocar o label
	 */
	private JLabel tituloNoFrame;

	private static PainelStatusBar statusBar;

	public static PainelResumo painelresumo;

	/**
	 * URL do site
	 */
	private static JPanel painelUrl;
	public static G_UrlTextField txtUrl;
	private static JButton btnAvaliarUrl;
	
	/**
	 * Construtor para a classe silvaAcessivel.
	 * 
	 * @throws ClassNotFoundException
	 *             Driver ou classe não encontrada.
	 * @throws SQLException
	 *             Erro no banco de dados ou na instrução SQL.
	 * @throws IOException
	 *             Erro ao abrir um arquivo ou arquivo não encontrado.
	 */
	public FrameSilvinha(){
		
	}
	

	public PainelSplash showPainelSplash() {
		PainelSplash splash = new PainelSplash(this);
		splash.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return splash;
	}

	public void setaCordoBackGround() {
		this.setBackground(corDefault);
	}

	public void inicializacaoSilvinhaWindowListener() {

		// CONFIGURA O LISTENER QUE FICARÁ ENCARREGADO DO ENCERRAMENTO DO
		// PROGRAMA

		wa = new SilvinhaWindowListener(this);
		this.addWindowListener(wa);
		this.addWindowStateListener(wa);
	}
	
	public void onComboBoxEdited(String valor) {
		avaliaUrl();
	}

	public void avaliaUrl() {
		(new Thread(){
			public void run(){
				FrameSilvinha.pnCentral.avaliaUrlOuArq(txtUrl.getSelectedItem().toString());
			}
		}).start();
	}
	
	private void realizaConfig() throws ClassNotFoundException, SQLException {
		if (!limitado && !PULA_REGISTRO) {
			/*
			 *  se nao for limitado 
			 *  E
			 *  nao pula registro
			 *  pede a chave de seguranca
			 */
			pnCentral = new PainelChaveDeSeguranca(this);
			this.add(pnCentral, BorderLayout.CENTER);
			this.setVisible(true);
			while (!PainelChaveDeSeguranca.chaveOk) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
			this.getContentPane().remove(pnCentral);
		}
		pnCentral = new PainelConfig(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		while (!PainelConfig.configOK) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Método que inicializa os componentes gráficos e recupera as configurações
	 * já salvas.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void inicializarGraficos() throws ClassNotFoundException, SQLException {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("imagens/logo.png"));

		limpaFrameSilvinha();
		setaoFrameComoUltimoEstado();
		
		/* 
		 * Adiciona a parte do norte
		 */
		JPanel norte = new JPanel(new BorderLayout());
		norte.setBackground(corDefault);
		{
			pnContLogo = new PainelLogo();
			norte.add(pnContLogo, BorderLayout.NORTH);
			tituloNoFrame = new JLabel();
			tituloNoFrame.setFont(new Font("Arial",Font.BOLD,16));
			tituloNoFrame.setForeground(new Color(0,0,101));
			Border border = BorderFactory.createEmptyBorder(0,10,5, 0);
			tituloNoFrame.setBorder(border);

			norte.add(tituloNoFrame, BorderLayout.CENTER);
			
			painelUrl = new JPanel(new BorderLayout());
			JLabel lblUrl = new JLabel("Url: ");
			painelUrl.setBackground(corDefault);
			txtUrl = new G_UrlTextField("config/urls.txt");
			txtUrl.setComboEditedListener(this);
			txtUrl.setToolTipText(TradPainelAvaliacao.DICA_DIGITE_URL);
			txtUrl.getAccessibleContext().setAccessibleDescription(TradPainelAvaliacao.URL_AVALIADA);
			txtUrl.getAccessibleContext().setAccessibleName("URL");
			txtUrl.setPreferredSize(new Dimension(223, 22));
			btnAvaliarUrl = new JButton("Ok");
			btnAvaliarUrl.setPreferredSize(new Dimension(60,25));
			btnAvaliarUrl.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							avaliaUrl();
						}
					}
			);
			painelUrl.add(lblUrl, BorderLayout.WEST);
			painelUrl.add(txtUrl,BorderLayout.CENTER);
			painelUrl.add(btnAvaliarUrl,BorderLayout.EAST);
			painelUrl.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 7));
			norte.add(painelUrl,BorderLayout.SOUTH);
		}
		this.add(norte, BorderLayout.NORTH);
		
		adicionaABarraDeStatus();
		this.menuBar = new MenuSilvinha(this, pnAvaliacao);
		this.setJMenuBar(this.menuBar);
		casoConfigFeitaMostraPnAvaliacao();
		
		
		if (VISTA_ANTIGO_SILVINHA) {
			this.setTitle("Silvinha - Avaliador de Acessibilidade na Web");
		} else {
			this.setTitle(Silvinha.TITULO);
		}
		
	}
	/**
	 * Insere o título da ferramenta no frame
	 */
	public void setTitle(String titulo){
		super.setTitle(titulo);
		int aux = titulo.indexOf("-"); 
		if(aux!=-1){
			tituloNoFrame.setText(titulo.substring(aux+1).trim());
		}else{
			tituloNoFrame.setText(titulo);
		}
	}
	
	private void limpaFrameSilvinha() {
		this.getContentPane().removeAll();
	}

	private void casoConfigFeitaMostraPnAvaliacao() throws ClassNotFoundException, SQLException {
		
		if (!PainelConfig.configOK) {
		
			realizaConfig();
		} else {
			
			lastActivePanel = pnCentral;
			pnCentral = new PainelAvaliacao(this);
			EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_AVALIACAO);
		}
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	private void adicionaABarraDeStatus() {
		statusBar = new PainelStatusBar();
		this.add(statusBar, BorderLayout.SOUTH);
	}

	private void setaoFrameComoUltimoEstado() throws NumberFormatException {
		int state = Integer.parseInt(TokenLang.props.getProperty(PropertyLoader.SILVINHA_FRAME_STATE));
		double x = Double.parseDouble(TokenLang.props.getProperty(PropertyLoader.SILVINHA_FRAME_LOCATION_X));
		double y = Double.parseDouble(TokenLang.props.getProperty(PropertyLoader.SILVINHA_FRAME_LOCATION_Y));
		double w = Double.parseDouble(TokenLang.props.getProperty(PropertyLoader.SILVINHA_FRAME_WIDTH));
		double h = Double.parseDouble(TokenLang.props.getProperty(PropertyLoader.SILVINHA_FRAME_HEIGHT));
		this.setBounds((int) x, (int) y, (int) w, (int) h);
		if (state == JFrame.MAXIMIZED_BOTH) {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}

	

	/**
	 * Troca o painel central pelo painel de avaliação
	 */
	public void showPainelAvaliacao() {
		
		// MenuSilvinha.miRotulos.setEnabled(false);
		/*
		 * Verificar se está avaliando
		 */
		if (!stopAvaliacao && !(pnCentral instanceof PainelResumo)) {
			setLastActivePanel();
			ConfiguracaoDaAvaliacao configDaAvaliacao = new ConfiguracaoDaAvaliacao();
			configDaAvaliacao.loadConf();
			Properties opcoes = configDaAvaliacao.getAsProperties();
			this.setJMenuBar(new MenuSilvinha(this));
			this.showPainelResumo(new ResumoDoRelatorio(opcoes), true);
			return;
		}
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelAvaliacao(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

		EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_AVALIACAO);
		
	}

	/**
	 * Troca o painel central pelo de Configuração
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void showPainelConfig() throws ClassNotFoundException, SQLException {
		// MenuSilvinha.miRotulos.setEnabled(false);

		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelConfig(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_CONFIG);
	}

	/**
	 * Troca o painel central pelo painel de resumo dos relatorios
	 * 
	 * @param resumo
	 * @param somenteResumo
	 */
	public void showPainelResumo(ResumoDoRelatorio resumo, boolean somenteResumo) {
		

		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		painelresumo = new PainelResumo(this, resumo, somenteResumo);
		pnCentral = painelresumo;
		this.add(painelresumo, BorderLayout.CENTER);
		this.onPainelCentralChange();

		EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_RESUMO);
	}

	/**
	 * Troca o painel central pelo Avaliador de XHTML/HTML
	 * 
	 */
	public void showPainelFerramentaXHTML() {

		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		
		pnCentral = new PainelTabbedXHTML(this);
		
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		
	
	}

	/**
	 * Troca o painel central pelo Avaliador de CSS
	 * 
	 */
	public void showPainelFerramentaCSS() {

		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelTabbedCSS(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	
	}

	/**
	 * Troca o painel central pela ferramenta de label
	 * 
	 */
	public void showPainelFerramentaLabel() {
		setLastActivePanel();
		
		this.getContentPane().remove(pnCentral);
		if (EstadoSilvinha.painelRelatorio == true) {
			pnCentral = new PainelTabbedLabel(PainelRelatorio.relatorio.getConteudo().toString(), PainelLabel.CONTEUDO, this);
		} else {
			pnCentral = new PainelTabbedLabel(this);
		}

		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	}

	public void showPainelFerramentaScriptPArq(String conteudo) {
		
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);

		pnCentral = new PainelTabbedScript(conteudo, PainelLabel.CONTEUDO, this);
		

		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	
	}

	public void showPainelFerramentaImgPArq(String conteudo, String endPagina) {
		
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		//System.out.println("enderecoBase:"+endPagina );
		pnCentral = new PainelTabbedImg(conteudo, PainelLabel.CONTEUDO, this, endPagina);
	
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	
	}

	public void showPainelFerramentaLabelPArq(String conteudo) {
		// MenuSilvinha.miRotulos.setEnabled(false);
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);

		pnCentral = new PainelTabbedLabel(conteudo, PainelLabel.CONTEUDO, this);
		
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

		
	}

	public void showPainelFerramentaDoctype() {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
	
		if (EstadoSilvinha.painelRelatorio == true) {
			pnCentral = new PainelTabbedDoctype(PainelRelatorio.relatorio.getConteudo().toString(), this);
		} else {
			pnCentral = new PainelTabbedDoctype(this);
		}
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	}

	public void showPainelFerramentaDoctypePArq(String conteudo) {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelTabbedDoctype(conteudo, this);
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelFerramentaDescricaoObjetos() {
		
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		if (EstadoSilvinha.painelRelatorio == true) {
			pnCentral = new PainelTabbedObjeto(PainelRelatorio.relatorio.getConteudo().toString(), PainelLabel.CONTEUDO, this);
		} else {
			pnCentral = new PainelTabbedObjeto(this);
		}
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelFerramentaDescricaoObjetosPArq(String conteudo) {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelTabbedObjeto(conteudo, PainelObjeto.CONTEUDO, this);
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelFerramentaDescricaoAnaliseGeral(String tag, String endPag, PanelDescricaoImagens pdi) {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		setLastActivePanel();
		pnCentral = new PanelAnaliseGeral(tag, endPag, this, pdi);
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	}

	public void showLastFerramentaDescricao(PanelDescricaoImagens pdi) {

		this.getContentPane().remove(pnCentral);

		pnCentral = pdi;

		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	}

	/** 
	 * Troca o painel central pelo de descrição de imagens 
	 */
	public void showPainelFerramentaDescricao() {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		
		setLastActivePanel();
		if (EstadoSilvinha.painelRelatorio == true) {
			//System.out.println("url: "+PainelRelatorio.relatorio.getUrl());
			pnCentral = new PainelTabbedImg(PainelRelatorio.relatorio.getConteudo().toString(), PainelLabel.CONTEUDO, this, PainelRelatorio.relatorio.getUrl());
		} else {
			pnCentral = new PainelTabbedImg(this);
		}
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		
	}

	/**
	 * Abre a ferramenta de descrição de imagens
	 * 
	 * @param codHtml
	 *            Código em HTML
	 */
	public void showPainelFerramentaDescricaoPArq(String codHtml) {
		
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);

	
		pnCentral = new PanelDescricaoImagens(codHtml, PainelLabel.CONTEUDO, this);

		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();

	}
	
	/**
	 * Troca o painel central pelo de descrição de script 
	 */
	public void showPainelFerramentaDescricaoScript() {		
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		if (EstadoSilvinha.painelRelatorio == true) {
			pnCentral = new PainelTabbedScript(PainelRelatorio.relatorio.getConteudo().toString(), PainelLabel.CONTEUDO, this);
		} else {
			pnCentral = new PainelTabbedScript(this);
		}
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	/**
	 * Troca o painel central pelo simulador de navegação 
	 */
	public void showPainelSimuladorNavegacao() {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		if (EstadoSilvinha.painelRelatorio == true) {
			String conteudo = PainelRelatorio.relatorio.getConteudo().toString();
			pnCentral = new PainelSimuladorNavegacao(this,conteudo);		
		} else {
			pnCentral = new PainelSimuladorNavegacao(this);
		}
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelSimuladorNavegacaoPArq(String conteudo) {
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelSimuladorNavegacao(this, conteudo);
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelFerramentaDescricaoFramesPArq(String conteudo) {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral.setVisible(true);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelResumoDiretorio(ResumoDoRelatorio resumo, boolean somenteResumo) {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelResumo(this, resumo, somenteResumo);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_RESUMO);
	}

	public void showPainelCorrecao(ResumoDoRelatorio resumo, RelatorioDaUrl relatorio) {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		String opcao = resumo.getOpcoes().getProperty("tipo_avaliacao");
		int tipoAvaliacao = Integer.parseInt(opcao);
		pnCentral = new PainelCorrecao(this, tipoAvaliacao, relatorio.getUrl(), relatorio.getConteudo());
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_CORRECAO);
	}

	public void showPainelRelatorio(RelatorioDaUrl relatorio) {
		setLastActivePanel();
		pnCentral.setVisible(false);
		this.getContentPane().remove(pnCentral);
		painelRelatorio = new PainelRelatorio(relatorio, this);
		pnCentral = painelRelatorio;
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		EstadoSilvinha.setaPainelAtual(EstadoSilvinha.PAINEL_RELATORIO);
	}

	public void repaintCentralPane() {
		this.getContentPane().remove(pnCentral);
		pnCentral.setBackground(corDefault);
		pnCentral.repaint();
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	/**
	 * Mostra o último panel ativo 
	 */
	public void showLastActivePanel() {
		this.getContentPane().remove(pnCentral);
		SuperPainelCentral aux = pnCentral;
		if (lastActivePanel instanceof PainelAvaliacao) {
			lastActivePanel = new PainelAvaliacao(this);
			MenuSilvinha.habilitaExecutarAgora();
		} else if (lastActivePanel instanceof PainelRelatorio) {
			lastActivePanel = new PainelRelatorio(PainelRelatorio.relatorio, this);
			MenuSilvinha.desabilitaExecutarAgora();
		} else if (lastActivePanel instanceof PainelResumo) {
			this.setJMenuBar(new MenuSilvinha(this));
			MenuSilvinha.desabilitaExecutarAgora();
		}
		pnCentral = lastActivePanel;
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
		if (EstadoSilvinha.ferramentaLabelPanel || EstadoSilvinha.painelAvaliacao || EstadoSilvinha.painelConfig || EstadoSilvinha.painelCorrecao || EstadoSilvinha.painelRelatorio
				|| EstadoSilvinha.painelResumo) {
			TxtBuffer.clear();
		}
		lastActivePanel = aux;

	}

	/**
	 * seta o último painel visto
	 * sempre quando troca o painel central 
	 */
	private void setLastActivePanel() {
		if (pnCentral instanceof PainelAvaliacao || pnCentral instanceof PainelConfig
			||pnCentral instanceof PainelCorrecao || pnCentral instanceof PainelRelatorio
			|| pnCentral instanceof PainelResumo) {
			lastActivePanel = pnCentral;
		} 
	}

	
	public static boolean mostrandoAviso = false;

	public static boolean limitacao() {
		if (limitado) {
			if (PainelStatusBar.paginasAvaliadas > 5) {
				if (!mostrandoAviso) {
					mostrandoAviso = true;
					JOptionPane.showMessageDialog(null, Silvinha.AVISO_LIMITE_DEMO);
					mostrandoAviso = false;
				}
				return true;
			}
			if (PainelStatusBar.paginasAvaliadas > 5) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Coloca o layout padrão do sistema operacional do usuário
	 */
	public static void setLookAndFeel() {
		try {
			// Set cross-platform Java L&F (also called "Metal")
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			// UIManager.setLookAndFeel("Windows XP");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	public void showPainelPreencheCampo() {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelTabbedPreenchedorFormulario(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelFerramentaLinksRedundantes() {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelTabbedLinkRedundante(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}

	public void showPainelFerramentaEventoDependente() {
		setLastActivePanel();
		this.getContentPane().remove(pnCentral);
		pnCentral = new PainelTabbedCorretorEventos(this);
		this.add(pnCentral, BorderLayout.CENTER);
		this.onPainelCentralChange();
	}
	/**
	 * Define o texto da barra de URL
	 *
	 */
	public void setUrlTextField(String valor){
		txtUrl.setText(valor);
	}
	/**
	 * @return o texto da barra de URL
	 */
	public String getUrlTextField(){
		return txtUrl.getSelectedItem().toString();
	}
	
	private void onPainelCentralChange(){
		if(pnCentral.showBarraUrl()){
			painelUrl.setVisible(true);
		}else{
			painelUrl.setVisible(false);
		}
		this.setVisible(true);
	}
}