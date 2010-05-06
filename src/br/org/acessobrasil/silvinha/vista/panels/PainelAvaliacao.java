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

package br.org.acessobrasil.silvinha.vista.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.silvinha.entidade.Historico;
import br.org.acessobrasil.silvinha.entidade.HistoricoDaAvaliacao;
import br.org.acessobrasil.silvinha.entidade.NomeArquivoOuDiretorioLocal;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.util.GeraMapDir;
import br.org.acessobrasil.silvinha.util.IsHtmlLocalOrNot;
import br.org.acessobrasil.silvinha.util.Token;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.HTMLLocalDirectoryChooser;
import br.org.acessobrasil.silvinha.vista.componentes.HTMLLocalFileChooser;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaCtrl;
import br.org.acessobrasil.silvinha.vista.listeners.ExecutarAgoraListenerWeb;
import br.org.acessobrasil.silvinha2.mli.ErrosDoSistema;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAvaliacao;
import br.org.acessobrasil.silvinha2.projetodosite.ConfiguracaoDaAvaliacao;
import br.org.acessobrasil.silvinha2.util.G_File;
/**
 * UI para configuração da avaliação
 *
 */
public class PainelAvaliacao extends SuperPainelCentral implements ActionListener {

	private static final long serialVersionUID = 3162585771098841906L;

	private final Color corDefault = CoresDefault.getCorPaineis();

	public FrameSilvinha framePai;

	private ExecutarAgoraListenerWeb threadExecutarAgora;

	public static final int WCAG = 1;

	public static final int EMAG = 2;

	public static final int NIVEL1 = 1;

	private static String endAvaliado;
	
	public static final int NIVEL2 = 2;

	public static final int NIVEL3 = 3;

	public static final int TODO = 4;

	public static final int PAGINA = 5;

	public static final int HTML_LOCAL = 6;

	public static final int DIRETORIO_LOCAL = 7;

	private String opcoes[];

	private JPanel pnTipoAval;

	private JPanel pnPrioridades;

	private JPanel pnProfundidade;

	private JPanel pnExecutar;

	private static JRadioButton rdEMAG;

	private static JRadioButton rdWCAG;

	private ButtonGroup bgTipoAvaliacao;

	private static JCheckBox chkPri1;

	private static JCheckBox chkPri2;

	private static JCheckBox chkPri3;

	private static JComboBox cbProfundidade;

	private static JLabel lblProfundidade;

	/**
	 * URL do site
	 */
	private static JComboBox txtUrl;

	private static JLabel lblUrl;

	private static JButton btnExecutar;

	private static JButton btnPararExecucao;

	private static JButton btnAbreArquivo;

	private static JPanel endereco = new JPanel();

	private static boolean isLocal = false;

	private static boolean isWeb = false;

	private static JPanel urls = new JPanel();

	public PainelAvaliacao(FrameSilvinha framePai) {
		TradPainelAvaliacao.carregaTexto(TokenLang.LANG);
		String opcoes[] = { TradPainelAvaliacao.CMB_PROFUNDIDADE_OPCAO_1, TradPainelAvaliacao.CMB_PROFUNDIDADE_OPCAO_2, TradPainelAvaliacao.CMB_PROFUNDIDADE_OPCAO_3,
				TradPainelAvaliacao.CMB_PROFUNDIDADE_OPCAO_4, TradPainelAvaliacao.CMB_PROFUNDIDADE_OPCAO_5, TradPainelAvaliacao.CMB_PROFUNDIDADE_LOCAL_OPCAO_1,
				TradPainelAvaliacao.CMB_PROFUNDIDADE_LOCAL_OPCAO_2 };
		this.opcoes=opcoes;		
		this.framePai = framePai;
		this.framePai.setJMenuBar(new MenuSilvinha(this.framePai, this));
		isLocal = false;
		isWeb = true;
		framePai.setTitle(TradPainelAvaliacao.TIT_AVAL_ACS);
		this.setBackground(corDefault);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 7, 1, 7);
		this.setLayout(gridbag);

		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);

		Dimension dLabels = new Dimension(150, 20);

		rdEMAG = new JRadioButton();
		rdWCAG = new JRadioButton();
		bgTipoAvaliacao = new ButtonGroup();

		chkPri1 = new JCheckBox();
		chkPri2 = new JCheckBox();
		chkPri3 = new JCheckBox();

		rdWCAG.setBackground(corDefault);
		rdWCAG.setText("WCAG");
		rdWCAG.setActionCommand("WCAG");
		rdWCAG.setPreferredSize(dLabels);
		rdWCAG.setHorizontalAlignment(SwingConstants.CENTER);
		rdWCAG.setHorizontalTextPosition(SwingConstants.LEADING);
		rdWCAG.setToolTipText(TradPainelAvaliacao.DICA_WCAG);

		rdEMAG.setBackground(corDefault);
		rdEMAG.setText("E-MAG");
		rdEMAG.setActionCommand("E-MAG");
		rdEMAG.setPreferredSize(dLabels);
		rdEMAG.setHorizontalAlignment(SwingConstants.CENTER);
		rdEMAG.setHorizontalTextPosition(SwingConstants.LEADING);
		rdEMAG.setToolTipText(TradPainelAvaliacao.DICA_EMAG);
		
		bgTipoAvaliacao.add(rdEMAG);
		bgTipoAvaliacao.add(rdWCAG);
		bgTipoAvaliacao.setSelected(rdEMAG.getModel(), true);

		/*
		 * Emag retirado por enquanto 
		 */
		if(!TokenLang.LANG.equals("pt")){
			bgTipoAvaliacao.setSelected(rdWCAG.getModel(), true);
			rdEMAG.setEnabled(false);
		}
		
		pnTipoAval = new JPanel();
		pnTipoAval.setBackground(corDefault);
		pnTipoAval.add(rdWCAG);
		pnTipoAval.add(rdEMAG);
		pnTipoAval.setBorder(BorderFactory.createTitledBorder(border, TradPainelAvaliacao.BORDER_LBL_TIPOAVAL));
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		gridbag.setConstraints(pnTipoAval, c);
		this.add(pnTipoAval);

		lblProfundidade = new JLabel(TradPainelAvaliacao.NIVEL);
		lblProfundidade.setBackground(corDefault);
		lblProfundidade.setAlignmentX(Component.RIGHT_ALIGNMENT);

		cbProfundidade = new JComboBox(this.opcoes);
		cbProfundidade.setSelectedIndex(4);
		cbProfundidade.setBackground(corDefault);
		cbProfundidade.setToolTipText(TradPainelAvaliacao.DICA_NIVEIS_AVALIACAO);
		cbProfundidade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaintPainelExecutar();
			}
		});
		
		pnProfundidade = new JPanel();
		pnProfundidade.setBackground(corDefault);
		pnProfundidade.add(lblProfundidade);
		pnProfundidade.add(cbProfundidade);
		pnProfundidade.setBorder(BorderFactory.createTitledBorder(border, TradPainelAvaliacao.BORDER_LBL_PROFUNDIDADE));
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(pnProfundidade, c);
		this.add(pnProfundidade);

		chkPri1.setBackground(corDefault);
		chkPri1.setText(TradPainelAvaliacao.LBL_PRIORIDADE1);
		chkPri1.setActionCommand("PRI1");
		chkPri1.setSelected(true);
		chkPri1.setToolTipText(TradPainelAvaliacao.DICA_PRIORIDADE1);
		chkPri1.setPreferredSize(dLabels);
		chkPri1.setHorizontalAlignment(SwingConstants.CENTER);
		chkPri1.setHorizontalTextPosition(SwingConstants.LEADING);

		chkPri2.setBackground(corDefault);
		chkPri2.setText(TradPainelAvaliacao.LBL_PRIORIDADE2);
		chkPri2.setActionCommand("PRI2");
		chkPri2.setSelected(true);
		chkPri2.setToolTipText(TradPainelAvaliacao.DICA_PRIORIDADE2);
		chkPri2.setPreferredSize(dLabels);
		chkPri2.setHorizontalAlignment(SwingConstants.CENTER);
		chkPri2.setHorizontalTextPosition(SwingConstants.LEADING);

		chkPri3.setBackground(corDefault);
		chkPri3.setText(TradPainelAvaliacao.LBL_PRIORIDADE3);
		chkPri3.setActionCommand("PRI3");
		chkPri3.setSelected(true);
		chkPri3.setToolTipText(TradPainelAvaliacao.DICA_PRIORIDADE3);
		chkPri3.setPreferredSize(dLabels);
		chkPri3.setHorizontalAlignment(SwingConstants.CENTER);
		chkPri3.setHorizontalTextPosition(SwingConstants.LEADING);

		pnPrioridades = new JPanel();
		pnPrioridades.setBackground(corDefault);
		pnPrioridades.add(chkPri1);
		pnPrioridades.add(chkPri2);
		pnPrioridades.add(chkPri3);
		pnPrioridades.setBorder(BorderFactory.createTitledBorder(border, TradPainelAvaliacao.BORDER_LBL_PRIORIDADE));
		c.weightx = 0.0;
		gridbag.setConstraints(pnPrioridades, c);
		this.add(pnPrioridades);

		pnExecutar = new JPanel();
		pnExecutar.setBackground(corDefault);
		pnExecutar.setBorder(BorderFactory.createTitledBorder(border, null));

		lblUrl = new JLabel(TradPainelAvaliacao.LBL_URL);
		lblUrl.setLabelFor(txtUrl);

		txtUrl = new JComboBox(Historico.getLinks());
		txtUrl.setEditable(true);
		txtUrl.setToolTipText(TradPainelAvaliacao.DICA_DIGITE_URL);
		txtUrl.getAccessibleContext().setAccessibleDescription(TradPainelAvaliacao.URL_AVALIADA);
		txtUrl.getAccessibleContext().setAccessibleName("URL");
		txtUrl.setPreferredSize(new Dimension(223, 22));

		// linha adicionada em 9 de janeiro de 2006 depois tem que adicinar as
		// possibilidades de tradução

		btnExecutar = new JButton(TradPainelAvaliacao.BTN_EXECUTAR_AGORA);
		btnExecutar.setActionCommand(MenuSilvinha.MNU_EXECUTAR_AGORA);
		btnExecutar.setToolTipText(TradPainelAvaliacao.DICA_AVALIACAO);
		btnExecutar.getAccessibleContext().setAccessibleDescription(TradPainelAvaliacao.DICA_AVALIACAO);
		btnExecutar.getAccessibleContext().setAccessibleName(TradPainelAvaliacao.DICA_AVALIACAO);
		btnExecutar.addActionListener(this);
		btnPararExecucao = new JButton(TradPainelAvaliacao.BTN_PARAR_EXECUCAO);
		btnPararExecucao.setActionCommand("PARAR");
		btnPararExecucao.setToolTipText(TradPainelAvaliacao.DICA_INTERROMPER);
		btnPararExecucao.addActionListener(this);
		btnPararExecucao.setVisible(false);

		GridBagLayout bag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		pnExecutar.setLayout(bag);

		urls.setLayout(new FlowLayout());
		urls.setBackground(corDefault);
		urls.add(lblUrl);
		urls.add(txtUrl);

		// linha adicionada em 9 de janeiro de 2006
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 10, 2, 10);
		pnExecutar.add(urls, gbc);

		JPanel btns = new JPanel();
		btns.setLayout(new FlowLayout());
		btns.setBackground(corDefault);
		btns.add(btnExecutar);
		btns.add(btnPararExecucao);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 10, 2, 10);
		pnExecutar.add(btns, gbc);
		
		

		btnPararExecucao.setEnabled(false);

		// c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 2;
		c.weighty = 1.0;
		gridbag.setConstraints(pnExecutar, c);

		this.add(pnExecutar);

		repaintParaWeb();
		this.setVisible(true);
		txtUrl.requestFocusInWindow();
		this.framePai.getRootPane().setDefaultButton(btnExecutar);
		MenuSilvinha.habilitaExecutarAgora();
	}

	public int getTipoAvaliacao() {
		String selecao = bgTipoAvaliacao.getSelection().getActionCommand();
		if (selecao.equals("WCAG")) {
			return WCAG;
		} else {
			return EMAG;
		}
	}

	public boolean verificaPrioridade1() {
		return chkPri1.isSelected();
	}

	public boolean verificaPrioridade2() {
		return chkPri2.isSelected();
	}

	public boolean verificaPrioridade3() {
		return chkPri3.isSelected();
	}

	public int getProfundidade() {
		return (cbProfundidade.getSelectedIndex() + 1);
	}

	public void actionPerformed(ActionEvent e) {
		FrameSilvinha.stopAvaliacao = false;
		ResumoDoRelatorio.reinicia();

		String command = e.getActionCommand();
		if (command.equals(MenuSilvinha.MNU_EXECUTAR_AGORA)) {
			G_File.clearDir("temp");
			EstadoSilvinha.orgao=getTipoAvaliacao();
			String url = ((String) txtUrl.getSelectedItem()).trim();
			setEndAvaliado(url);
			PainelResumo.valorVelocidade = 100;
			PainelResumo.setDesabilitarBtnContinuar(true);
			/*
			 * Salva a configuração no HD
			 */
			ConfiguracaoDaAvaliacao configDaAvaliacao = new ConfiguracaoDaAvaliacao();
			configDaAvaliacao.setNiveis(getProfundidade());
			configDaAvaliacao.setMostraP1(verificaPrioridade1());
			configDaAvaliacao.setMostraP2(verificaPrioridade2());
			configDaAvaliacao.setMostraP3(verificaPrioridade3());
			configDaAvaliacao.setUrl(url);
			if (getTipoAvaliacao() == EMAG) {
				configDaAvaliacao.setEmag();
			} else {
				configDaAvaliacao.setWcag();
			}
			configDaAvaliacao.salvaConf();
			if ((getProfundidade()) > PAGINA) {
				if (IsHtmlLocalOrNot.testIsHtmlLocalOrNot(url)) {
					NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio = url;
					NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio = NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio.replace('\\', '/');
					Historico.addLink(Token.URL_STRING);
					txtUrl = new JComboBox(Historico.getLinks());
					habilitarParar();
					try {
						threadExecutarAgora = new ExecutarAgoraListenerWeb(this);
						threadExecutarAgora.start();
					} catch (Exception ex) {
						repaintPainelExecutar();
					}
				}
			}
			if ((getProfundidade()) <= PAGINA) {
				if (confereUrl() && conferePrioridades()) { // caso
					NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio = url;
					Historico.addLink(Token.URL_STRING);
					txtUrl = new JComboBox(Historico.getLinks());
					habilitarParar();
					try {
						threadExecutarAgora = new ExecutarAgoraListenerWeb(this);
						repaintParaLocal(); // faço isso porque caso não faça
						// está sinalizando um null point em
						// algum lugar
						repaintParaWeb();
						new Thread(SilvinhaCtrl.processaErro, threadExecutarAgora);
						threadExecutarAgora.start();
					} catch (Exception erro) {
						JOptionPane.showMessageDialog(null, ErrosDoSistema.ERRO_PROCESSO);
					}
				} else {
					return;
				}
			}
		} else if (command.equals(btnPararExecucao.getActionCommand())) {
			try {
				if ((getProfundidade()) > PAGINA) {
					GeraMapDir.pararAvaliacao();
				}
				threadExecutarAgora.pararExecucao();
				threadExecutarAgora.interrupt();
				Thread.sleep(2500);
				repaintPainelExecutar();
			} catch (Exception ex) {
			}
			PainelStatusBar.setFinalizado(true);
			PainelStatusBar.setText(TradPainelAvaliacao.AVALIACAO_INTERROMPIDA);
			if (Gerente.getRelatorio() != null && Gerente.getRelatorio().size() > 0) {
				ResumoDoRelatorio resumo = new ResumoDoRelatorio(Gerente.getRelatorio(), this.verificaOpcoes());
				// verifica a necessidade de gravar as urls avaliadas e
				// instancia, caso necessario,
				// um objeto do tipo HistoricoDaAvaliacao
				if (Gerente.isEstouroMemoria()) {
					HistoricoDaAvaliacao.setPaginasAvaliadas(resumo);
				}
				this.getParentFrame().showPainelResumo(resumo, false);
			}

			else {
				habilitarExecutar();
			}
		}
		// adicionado em 9/1/2006
		if (command.equals(btnAbreArquivo.getActionCommand())) {
			if (getProfundidade() == HTML_LOCAL)
				new HTMLLocalFileChooser(this);
			if (getProfundidade() == DIRETORIO_LOCAL)
				new HTMLLocalDirectoryChooser(this);
		}
	}

	public static void habilitarExecutar() {
		btnPararExecucao.setEnabled(false);
		btnExecutar.setEnabled(true);
	}

	public static void habilitarParar() {
		btnExecutar.setEnabled(false);
		btnPararExecucao.setEnabled(true);
	}

	public Properties verificaOpcoes() {
		Properties props = new Properties();

		String tipoAvaliacao = String.valueOf(getTipoAvaliacao());
		props.setProperty("tipo_avaliacao", tipoAvaliacao);

		Boolean pri1 = verificaPrioridade1();
		Boolean pri2 = verificaPrioridade2();
		Boolean pri3 = verificaPrioridade3();
		if (pri1) {
			props.setProperty("prioridade1", "true");
		} else {
			props.setProperty("prioridade1", "false");
		}
		if (pri2) {
			props.setProperty("prioridade2", "true");
		} else {
			props.setProperty("prioridade2", "false");
		}
		if (pri3) {
			props.setProperty("prioridade3", "true");
		} else {
			props.setProperty("prioridade3", "false");
		}

		String profundidade = String.valueOf(getProfundidade());
		props.setProperty("niveis", profundidade);

		return props;
	}

	private boolean conferePrioridades() {
		Boolean pri1 = verificaPrioridade1();
		Boolean pri2 = verificaPrioridade2();
		Boolean pri3 = verificaPrioridade3();
		if (!pri1 && !pri2 && !pri3) {
			JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_SELECIONE_PRIORIDADE, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean confereUrl() {
		Token.URL_STRING = "";
		String urlString = ((String) txtUrl.getSelectedItem()).trim();
		if (urlString.trim().equals("")) {
			JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_PREENCHA_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			txtUrl.requestFocusInWindow();
			return false;
		}
		if (urlString.startsWith("www")) {
			urlString = "http://" + urlString;
		}
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException mue) {
			JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
			txtUrl.requestFocusInWindow();
			return false;
		}
		if (url != null) {
			try {
				url.openConnection().connect();				
			} catch (IOException ioe) {
				if (url.getProtocol().equals("https")) {
					// Melhorar verificacao, testar conexao se for https
				} else {
					JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_NAO_CONECTOU, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
					txtUrl.requestFocusInWindow();
					return false;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, TradPainelAvaliacao.AVISO_VERIFIQUE_URL, TradPainelAvaliacao.AVISO, JOptionPane.WARNING_MESSAGE);
				txtUrl.requestFocusInWindow();
				return false;
			}
		}
		if (urlString != null && urlString.length() > 0) {
			urlString = urlString.startsWith("/") ? urlString.substring(1) : urlString;
			Token.URL_STRING = urlString;
			Token.URL = url;
		}
		return true;
	}

	public FrameSilvinha getParentFrame() {
		return framePai;
	}

	public static void pararExecucao() {
		btnPararExecucao.doClick();
	}

	public static void continuarAvaliacaoAnterior() {
		txtUrl.setSelectedItem(Token.URL_STRING);
		cbProfundidade.setSelectedIndex(Integer.parseInt(Gerente.getProperties().getProperty("niveis")) - 1);
		btnExecutar.doClick();
	}

	public void repaintPainelExecutar() {
		if ((getProfundidade()) <= 5) {
			if (!isWeb) {
				repaintParaWeb();
				isLocal = false;
				isWeb = true;
			}
		}
		if ((getProfundidade()) > 5) {
			if (!isLocal) {
				repaintParaLocal();
				isLocal = true;
				isWeb = false;
			}
		}
	}

	public void repaintParaWeb() {
		pnExecutar.setVisible(false);
		pnExecutar.remove(endereco);
		urls.removeAll();
		lblUrl = new JLabel("URL");
		lblUrl.setLabelFor(txtUrl);
		urls.add(lblUrl);
		urls.add(txtUrl);
		this.repaint();
		pnExecutar.setVisible(true);
		this.setVisible(false);
		this.setVisible(true);
	}

	private void repaintParaLocal() {
		pnExecutar.setVisible(false);
		btnAbreArquivo = new JButton(TradPainelAvaliacao.LOCALIZAR);
		urls.removeAll();
		lblUrl = new JLabel(GERAL.ENDERECO);
		lblUrl.setLabelFor(txtUrl);
		urls.add(lblUrl);
		txtUrl.setEditable(true);
		txtUrl.setPreferredSize(new Dimension(223, 22));
		urls.add(txtUrl);
		urls.add(btnAbreArquivo);
		btnAbreArquivo.addActionListener(this);
		pnExecutar.setVisible(true);
		this.setVisible(false);
		this.setVisible(true);
	}

	public static String getEndAvaliado() {
		return endAvaliado;
	}

	public static void setEndAvaliado(String endAvaliado) {
		PainelAvaliacao.endAvaliado = endAvaliado;
	}

	@Override
	public boolean showBarraUrl() {
		return false;
	}
}
