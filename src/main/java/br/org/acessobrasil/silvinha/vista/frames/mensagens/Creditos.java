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

package br.org.acessobrasil.silvinha.vista.frames.mensagens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha2.mli.TradCreditos;
/**
 * Créditos do sistema ASES
 * @author Fabio Issamu Oshiro
 */
public class Creditos extends JFrame{
	private static final long serialVersionUID = -4035169221430816104L;

	private JPanel contentPanel;
	
	private JLabel logo;	
	private JTextPane texto;
	
	private int width=436, height=283;
	
	public Creditos(){
		initComponents();
	}
	
	private void initComponents() {
		TradCreditos.carregaTexto(TokenLang.LANG);
		
		setTitle(TradCreditos.ASES_CREDITOS);
		
		Color bgColor = new Color(120,120,120);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setBackground(bgColor);
		logo=new JLabel();
		File bg = new File("imagens/creditos.png");
		ImageIcon imageIcon=null;
		setIconImage(Toolkit.getDefaultToolkit().getImage("imagens/logo.png"));
		if(bg.exists()){
			imageIcon=new ImageIcon("imagens/creditos.png");
			logo.setIcon(imageIcon);
		}else{
			bg = new File("../imagens/creditos.png");
			if(bg.exists()){
				imageIcon = new ImageIcon("imagens/creditos.png");
				logo.setIcon(imageIcon);
			}
		}
		if(imageIcon!=null){
			width = imageIcon.getIconWidth();
			height = imageIcon.getIconHeight();
		}
		JLayeredPane layeredPane = new JLayeredPane();
		logo.setBounds(0,0,width,height);
		//layeredPane.setBounds(0,0,width,height);
		layeredPane.add(logo,1);
		contentPanel.add(layeredPane);
		contentPanel.setPreferredSize(new Dimension(width,height));
		String msg=TradCreditos.ASES_ACRO+TradCreditos.DESENVOLVIDO_POR
			
			+"\nSite: http://www.acessobrasil.org.br"
			+"\nE-mail: acesso@acessobrasil.org.br"
			+"\nTel.: 55 21 2232-1848"
			+"\n\n"
			;
		
		msg+=TradCreditos.COORDENACAO_GERAL+
			"\tGuilherme de Azambuja Lira\n"+
			
//			TradCreditos.COORDENACAO_PROJETO+
//			"\tAdele Malta\n"+
			
			TradCreditos.DESENVOLVIMENTO_SISTEMAS +
			"\tFabio Issamu Oshiro\n" +
			"\tHaroldo Veiga\n" +
			"\tRenato Tomaz Nati\n" +
			
			TradCreditos.AGRADECIMENTOS +
			"\tRuth de Moraes Barbosa Vieira\n" +
			"\tCleovande dos Santos Oliveira\n" +
			"\tFernanda Hoffmann Lobato\n" +
			
			TradCreditos.FINANCIAMENTO_PROJETO+
			"\tMinistério do Planejamento Orçamento e Gestão\n" +
			"\tSecretaria de Logística e Tecnologia da Informação\n" +
			"\tDepartamento do Governo Eletrônico\n"
			
			;
		msg=msg.replaceAll("\t","   ");
		
		
		texto = new JTextPane();
		texto.setText(msg);
		//texto.setAlignmentX(0);
		//texto.se
		//texto.setBounds(12, 110,350, 90);
		texto.setOpaque(false);
		texto.setEditable(false);
		texto.setForeground(new Color(0,0,0));
		Font fonte = new Font("Arial",1,13);
		texto.setFont(fonte);
		JScrollPane scrollTexto = new JScrollPane(texto);
		scrollTexto.setBounds(36, 103,368, 90);
		//scrollTexto.setBackground(bgColor);
		//texto.setBackground(bgColor);
		layeredPane.add(scrollTexto,0);
		layeredPane.setBounds(0, 0, width, height);
		//contentPanel.add(scrollTexto);
		scrollTexto.setOpaque(false);
		
		contentPanel.setBounds(0, 0, width, height);
		contentPane.add(contentPanel);
		
		pack();
		//setSize(width,height);
		//this.setMaximumSize(new Dimension(width,400));
		this.setResizable(false);
		setLocation((tela.width/2)-(width/2),(tela.height/2)-(height/2));
		this.setVisible(true);
	}
	public static void main(String args[]){
		new Creditos();
	}
}