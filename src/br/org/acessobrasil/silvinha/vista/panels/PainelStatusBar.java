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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.configs.UsoAutorizado;
import br.org.acessobrasil.silvinha2.mli.GERAL;

/**
 * Painel amarelo de status que aparece em todas as paginas
 * Change Log:<br>
 * Retirei o campo que mostra o cnpj
 * @author Acessibilidade Brasil
 */
public class PainelStatusBar extends JPanel {
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = -3348105187778647664L;

	private static JProgressBar progressBarProcessoAtual;

	private JPanel panelTexto;

	private static JLabel lblProgTarReq;

	private static JPanel panelProgressBar;

	private static JLabel lblTexto;

	private static JLabel lblTexto2;

	private Color corDefault = new Color(200, 200, 200);

	/**
	 * Páginas mapeadas
	 */
	private static int paginasMapeadas;

	public static int paginasAvaliadas;

	private static boolean finalizado;

	private static boolean progressBarVisible;

	private static JLabel lblTxtPaginasMapeadas;

	private static JLabel lblQtddPaginasMapeadas;

	private static JLabel lblTxtPaginasAvaliadas;

	private static JLabel lblQtddPaginasAvaliadas;
	
	private static boolean pago = false;

	/**
	 * inicializa as paginas mapeadas e avaliadas, zeradas e a visibilidade e
	 * finalização como falsos
	 */
	public PainelStatusBar() {
		init_v3();
	}
	private void init_v3() {

		paginasMapeadas = 0;
		paginasAvaliadas = 0;
		finalizado = false;
		progressBarVisible = false;

		this.setLayout(new BorderLayout());

		panelTexto = new JPanel();
		panelTexto.setLayout(new GridLayout(1, 1));
		

		panelTexto.setPreferredSize(new Dimension(10, 25));
		//panelTexto.getInsets().set(0, 10, 0, 0);

		lblTexto = new JLabel();
		lblTexto.setHorizontalAlignment(JLabel.LEFT);
		lblTexto.getInsets().set(0, 10, 0, 0);
		panelProgressBar = new JPanel(new BorderLayout());
		panelProgressBar.setAlignmentY(JPanel.LEFT_ALIGNMENT);

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel progressoAtual = new JPanel();
		//lblTxtPaginasMapeadas = new JLabel(GERAL.TOTAL_LINKS);
		lblTxtPaginasMapeadas = new JLabel(GERAL.DE);
		lblTxtPaginasMapeadas.setHorizontalAlignment(JLabel.LEFT);
		lblQtddPaginasMapeadas = new JLabel(String.valueOf(paginasMapeadas));
		//lblTxtPaginasAvaliadas = new JLabel(GERAL.TOTAL_PAGINAS_AVALIADAS);
		lblTxtPaginasAvaliadas = new JLabel(GERAL.PAGINA_STATUS);
		lblTxtPaginasAvaliadas.setHorizontalAlignment(JLabel.LEFT);
		lblQtddPaginasAvaliadas = new JLabel(String.valueOf(paginasAvaliadas));
		//panel1.setSize(new Dimension(12, 12));
		//panel1.setOpaque(false);
		lblTexto.setMaximumSize(new Dimension(350,25));
		panel1.add(lblTxtPaginasAvaliadas);
		panel1.add(lblQtddPaginasAvaliadas);
		panel1.add(lblTxtPaginasMapeadas);
		panel1.add(lblQtddPaginasMapeadas);
		panel1.add(new JLabel("- "));
		panel1.add(lblTexto);
		//panel1.add(progressoAtual);
		
		lblProgTarReq = new JLabel(GERAL.PROGRESSO_TAREFA);
		progressBarProcessoAtual = new JProgressBar();
		progressBarProcessoAtual.setPreferredSize(new Dimension(350, 10));
		progressBarProcessoAtual.setValue(0);

		progressoAtual.add(lblProgTarReq);
		progressoAtual.add(progressBarProcessoAtual);
		panelProgressBar.add(panel1, BorderLayout.WEST);
		panelProgressBar.add(panel2, BorderLayout.CENTER);
		//panelProgressBar.add(lblTexto);
		//panelProgressBar.add(panel2);
		panelProgressBar.setVisible(progressBarVisible);
		JPanel panel4 = new JPanel(new BorderLayout());
		panel4.add(panelProgressBar, BorderLayout.NORTH);
		//panel4.add(progressoAtual, BorderLayout.CENTER);
		
		{
			panel1.setBackground(CoresDefault.getCorStatusBar());
			panel2.setBackground(CoresDefault.getCorStatusBar());
			progressoAtual.setBackground(CoresDefault.getCorStatusBar());
			panel4.setBackground(CoresDefault.getCorStatusBar());
			this.setBackground(CoresDefault.getCorStatusBar());
			panelTexto.setBackground(CoresDefault.getCorStatusBar());
		}

		panelTexto.add(panel4);
		if (pago) {
			JPanel PTexto2 = new JPanel();
			// ///////modificando 4/12/2006

			// adicionamento do cnpj a todas os frames do silvinha

			lblTexto2 = new JLabel(GERAL.USO_AUTORIZADO + UsoAutorizado.nomeDoEstabelecimento);
			lblTexto2.setHorizontalAlignment(JLabel.CENTER);
			lblTexto2.getInsets().set(0, 100, 0, 0);
			Font formatacao = new Font("Verdana", Font.BOLD, 13);
			lblTexto2.setFont(formatacao);

			//PTexto2.setBackground(new Color(200, 200, 200));
			PTexto2.setLayout(new GridLayout());
			PTexto2.add(lblTexto2, BorderLayout.SOUTH);
			Border border = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
			PTexto2.setBorder(BorderFactory.createTitledBorder(border));
			this.add(PTexto2, BorderLayout.NORTH);
		}
		PainelStatusBar.hideProgTarReq();
		this.add(panelTexto, BorderLayout.CENTER);
		this.add(progressoAtual, BorderLayout.EAST);
		Border borderLinha = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		Border borderFora = BorderFactory.createEmptyBorder(0,7, 2, 7);
		this.setBorder(BorderFactory.createCompoundBorder(borderFora,borderLinha));
		this.setVisible(true);
	}
	/**
	 * Versão antiga
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private void init_v2() {

		paginasMapeadas = 0;
		paginasAvaliadas = 0;
		finalizado = false;
		progressBarVisible = false;

		this.setLayout(new BorderLayout());
		this.setBackground(corDefault);

		panelTexto = new JPanel();
		panelTexto.setLayout(new GridLayout(1, 1));
		panelTexto.setBackground(CoresDefault.getCorPaineis());

		panelTexto.setPreferredSize(new Dimension(10, 100));
		panelTexto.getInsets().set(0, 10, 0, 0);

		lblTexto = new JLabel();
		lblTexto.setHorizontalAlignment(JLabel.CENTER);
		lblTexto.getInsets().set(0, 10, 0, 0);
		panelProgressBar = new JPanel(new GridLayout(3, 1));
		panelProgressBar.setAlignmentY(JPanel.LEFT_ALIGNMENT);

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		lblTxtPaginasMapeadas = new JLabel(GERAL.TOTAL_LINKS);
		lblTxtPaginasMapeadas.setHorizontalAlignment(JLabel.CENTER);
		lblQtddPaginasMapeadas = new JLabel(String.valueOf(paginasMapeadas));
		lblTxtPaginasAvaliadas = new JLabel(GERAL.TOTAL_PAGINAS_AVALIADAS);
		lblTxtPaginasAvaliadas.setHorizontalAlignment(JLabel.CENTER);
		lblQtddPaginasAvaliadas = new JLabel(String.valueOf(paginasAvaliadas));
		panel1.setSize(new Dimension(12, 12));
		panel1.add(lblTxtPaginasMapeadas);
		panel1.add(lblQtddPaginasMapeadas);
		panel2.add(lblTxtPaginasAvaliadas);
		panel2.add(lblQtddPaginasAvaliadas);

		lblProgTarReq = new JLabel(GERAL.PROGRESSO_TAREFA);
		progressBarProcessoAtual = new JProgressBar();
		progressBarProcessoAtual.setPreferredSize(new Dimension(350, 10));
		progressBarProcessoAtual.setValue(0);

		panel3.add(lblProgTarReq);

		panel3.add(progressBarProcessoAtual);

		panelProgressBar.add(lblTexto);
		panelProgressBar.add(panel1);
		panelProgressBar.add(panel2);
		panelProgressBar.setVisible(progressBarVisible);
		JPanel panel4 = new JPanel(new BorderLayout());
		panel4.add(panelProgressBar, BorderLayout.NORTH);
		panel4.add(panel3, BorderLayout.CENTER);

		panelTexto.add(panel4);
		if (pago) {
			JPanel PTexto2 = new JPanel();
			// ///////modificando 4/12/2006

			// adicionamento do cnpj a todas os frames do silvinha

			lblTexto2 = new JLabel(GERAL.USO_AUTORIZADO + UsoAutorizado.nomeDoEstabelecimento);
			lblTexto2.setHorizontalAlignment(JLabel.CENTER);
			lblTexto2.getInsets().set(0, 100, 0, 0);
			Font formatacao = new Font("Verdana", Font.BOLD, 13);
			lblTexto2.setFont(formatacao);

			PTexto2.setBackground(new Color(200, 200, 200));
			PTexto2.setLayout(new GridLayout());
			PTexto2.add(lblTexto2, BorderLayout.SOUTH);
			Border border = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
			PTexto2.setBorder(BorderFactory.createTitledBorder(border));
			PainelStatusBar.hideProgTarReq();
			this.add(PTexto2, BorderLayout.CENTER);
		}

		this.add(panelTexto, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	/**
	 * Versão antiga
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private void init_v1() {

		paginasMapeadas = 0;
		paginasAvaliadas = 0;
		finalizado = false;
		progressBarVisible = false;

		this.setLayout(new BorderLayout());
		this.setBackground(corDefault);

		// jSeparator1 = new JSeparator();
		// jSeparator1.setPreferredSize(new Dimension(1, 2));
		// this.add(jSeparator1, BorderLayout.NORTH);

		// panelCantoEsquerdo = new JPanel();
		// panelCantoEsquerdo.setBackground(new Color(255, 204, 51));
		// panelCantoEsquerdo.setPreferredSize(new Dimension(40, 10));
		// this.add(panelCantoEsquerdo, BorderLayout.WEST);

		panelTexto = new JPanel();
		panelTexto.setLayout(new GridLayout(1, 1));
		// panelTexto.setBackground(new Color(255, 204, 51));
		panelTexto.setBackground(CoresDefault.getCorStatusBar());
		// panelTexto.setBackground(CoresDefault.getCorPaineis());

		panelTexto.setPreferredSize(new Dimension(10, 100));
		panelTexto.getInsets().set(0, 10, 0, 0);

		lblTexto = new JLabel();
		lblTexto.setHorizontalAlignment(JLabel.CENTER);
		lblTexto.getInsets().set(0, 10, 0, 0); // 2/100
		// panelTexto.add(lblTexto);

		//

		panelProgressBar = new JPanel(new GridLayout(3, 1));
		panelProgressBar.setAlignmentY(JPanel.LEFT_ALIGNMENT);
		// panelProgressBar.setBackground(new Color(255, 204, 51));

		JPanel panel1 = new JPanel();
		// panel1.setBackground(new Color(255, 204, 51));
		JPanel panel2 = new JPanel();
		// panel2.setBackground(new Color(255, 204, 51));
		JPanel panel3 = new JPanel();
		// panel3.setBackground(new Color(255, 204, 51));
		lblTxtPaginasMapeadas = new JLabel(GERAL.TOTAL_LINKS);
		lblTxtPaginasMapeadas.setHorizontalAlignment(JLabel.CENTER);
		// panel1.setLayout(new GridLayout());

		lblQtddPaginasMapeadas = new JLabel(String.valueOf(paginasMapeadas));

		// panel2.setLayout(new GridLayout());
		lblTxtPaginasAvaliadas = new JLabel(GERAL.TOTAL_PAGINAS_AVALIADAS);
		lblTxtPaginasAvaliadas.setHorizontalAlignment(JLabel.CENTER);
		lblQtddPaginasAvaliadas = new JLabel(String.valueOf(paginasAvaliadas));
		panel1.setSize(new Dimension(12, 12));
		panel1.add(lblTxtPaginasMapeadas);
		panel1.add(lblQtddPaginasMapeadas);
		panel2.add(lblTxtPaginasAvaliadas);
		panel2.add(lblQtddPaginasAvaliadas);

		lblProgTarReq = new JLabel(GERAL.PROGRESSO_TAREFA);
		progressBarProcessoAtual = new JProgressBar();
		progressBarProcessoAtual.setPreferredSize(new Dimension(350, 10));
		progressBarProcessoAtual.setValue(0);

		panel3.add(lblProgTarReq);

		panel3.add(progressBarProcessoAtual);

		panelProgressBar.add(lblTexto);
		panelProgressBar.add(panel1);
		panelProgressBar.add(panel2);
		// panelProgressBar.add(panel3);
		panelProgressBar.setVisible(progressBarVisible);
		JPanel panel4 = new JPanel(new BorderLayout());
		panel4.add(panelProgressBar, BorderLayout.NORTH);
		panel4.add(panel3, BorderLayout.CENTER);

		panelTexto.add(panel4);

		// //////this.add(panelTexto, BorderLayout.CENTER);
		JPanel PTexto2 = new JPanel();
		// ///////modificando 4/12/2006

		// adicionamento do cnpj a todas os frames do silvinha

		// lblTexto2 = new JLabel("Uso autorizado para o " +
		// UsoAutorizado.nomeDoEstabelecimento + " - CNPJ: " +
		// UsoAutorizado.cnpj);
		lblTexto2 = new JLabel(GERAL.USO_AUTORIZADO + UsoAutorizado.nomeDoEstabelecimento);
		lblTexto2.setHorizontalAlignment(JLabel.CENTER);
		lblTexto2.getInsets().set(0, 100, 0, 0);
		Font formatacao = new Font("Verdana", Font.BOLD, 13);
		lblTexto2.setFont(formatacao);

		PTexto2.setBackground(new Color(200, 200, 200));
		PTexto2.setLayout(new GridLayout());
		PTexto2.add(lblTexto2, BorderLayout.SOUTH);
		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0), 1);
		PTexto2.setBorder(BorderFactory.createTitledBorder(border));
		PainelStatusBar.hideProgTarReq();
		// this.add(PTexto2, BorderLayout.CENTER);

		this.add(panelTexto, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public static void setValueProgress(int valor) {
		progressBarProcessoAtual.setValue(valor);
	}

	public static void setText(String text) {
		lblTexto.setText(text);
	}

	/**
	 * @return Returns the finalizado.
	 */
	public boolean isFinalizado() {
		return finalizado;
	}

	/**
	 * @param finalizado
	 *            The finalizado to set.
	 */
	public static void setFinalizado(boolean finalizado) {
		PainelStatusBar.finalizado = finalizado;
		paginasMapeadas = 0;
		paginasAvaliadas = 0;
	}

	/**
	 * Esconde a barra de progresso da área amarela
	 */
	public static void hideProgTarReq() {
		lblProgTarReq.setVisible(false);
		progressBarProcessoAtual.setVisible(false);

	}

	/**
	 * Mostra uma barra de progresso na área amarela
	 */
	public static void showProgTarReq() {
		lblProgTarReq.setVisible(true);
		progressBarProcessoAtual.setVisible(true);
	}

	/**
	 * Esconde todo o texto da parte amarela incluindo o link que está
	 * avaliando, etc
	 */
	public static void hideProgressBar() {
		if (progressBarVisible) {
			progressBarVisible = false;
			panelProgressBar.setVisible(progressBarVisible);
		}
	}

	/**
	 * Mostra todo o texto da área amarela
	 */
	public static void showProgressBar() {
		if (!progressBarVisible) {
			progressBarVisible = true;
			
			panelProgressBar.setVisible(progressBarVisible);
		}
	}

	/**
	 * @return Returns the paginasAvaliadas.
	 */
	public int getPaginasAvaliadas() {
		return paginasAvaliadas;
	}

	/**
	 * @param paginasAvaliadas
	 *            The paginasAvaliadas to set.
	 */
	public static void setPaginasAvaliadas(int paginasAvaliadas) {
		PainelStatusBar.paginasAvaliadas = paginasAvaliadas;
		lblQtddPaginasAvaliadas.setText(String.valueOf(paginasAvaliadas));
	}

	/**
	 * @return Returns the totalPaginas.
	 */
	public int getPaginasMapeadas() {
		return paginasMapeadas;
	}

	/**
	 * Coloca no status bar o número total de links
	 */
	public static void setTotalLinks(int total) {
		lblQtddPaginasMapeadas.setText(String.valueOf(total));
	}

	public static void incrementaTotalPaginas() {
		paginasMapeadas++;
		// progressBar.setMaximum(paginasMapeadas);
		// lblQtddPaginasMapeadas.setText(String.valueOf(paginasMapeadas));
		if (!progressBarVisible) {
			progressBarVisible = true;
			panelProgressBar.setVisible(progressBarVisible);
		}
	}

	public static void incrementaPaginasAvaliadas() {
		paginasAvaliadas++;
		// progressBar.setValue(paginasAvaliadas);
		lblQtddPaginasAvaliadas.setText(String.valueOf(paginasAvaliadas));
		if (finalizado) {
			// Toolkit.getDefaultToolkit().beep();
			// progressBar.setValue(progressBar.getMinimum());
		}
	}

}
