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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import br.org.acessobrasil.nucleuSilva.entidade.Posicao;
import br.org.acessobrasil.nucleuSilva.entidade.Tag;
import br.org.acessobrasil.nucleuSilva.negocio.Automaticos;
import br.org.acessobrasil.nucleuSilva.util.Conexao;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
//import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.componentes.BotaoColorido;
import br.org.acessobrasil.silvinha.vista.componentes.ScrollPaneCorrecao;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelCorrecao;
/**
 * Painel para correção de erros 
 *
 */
public class PainelCorrecao extends SuperPainelCentral implements ActionListener {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");

	private static final long serialVersionUID = -274497847871724550L;
	
    private Font formFont = new Font("Tahoma", 1, 12);
    private Tag tags[][];
    private ScrollPaneCorrecao table;
    private int scrollHorizontal;
    private int scrollVertical;
    private JTextField parametro;
    private JLabel descricao;
    private JPanel painelCodigoFonte;
    private FrameSilvinha parentFrame;
    private Border lineBorder;
    private StringBuilder conteudo;
    
	public PainelCorrecao(FrameSilvinha frame, int tipoAvaliacao, String url, StringBuilder conteudo) {
		this.parentFrame = frame;
		this.conteudo = conteudo;
		this.tags = buscaTags(url, tipoAvaliacao, conteudo);

		scrollHorizontal = 1;
		scrollVertical = 1;
		lineBorder = BorderFactory.createLineBorder(new Color(0,0,0),1);
		
		descricao = new JLabel();
		descricao.setFont(formFont);
		descricao.setText(GERAL.DESCRICAO);
		descricao.setHorizontalAlignment(SwingConstants.CENTER);
		
		montaPainel();
	}
	
	private void montaPainel() {
		this.removeAll();
    	this.setBackground(CoresDefault.getCorPaineis());
    	GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 7, 1, 7);
        gbc.weightx = 1.0;
        this.setLayout(gridbag);
        
        gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 7.0;
		painelCodigoFonte = this.createPainelCodigoFonte(tags); 
		this.add(painelCodigoFonte, gbc);
		
        gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weighty = 2.0;
		this.add(this.createPainelDescricao(), gbc);
		
        gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.weighty = 1.0;
		this.add(this.createPainelParametro(), gbc);
		
		this.setVisible(true);
	}
	
	private JPanel createPainelCodigoFonte(Tag[][] tags) {
		JPanel panelCodigoFonte = new JPanel();
		panelCodigoFonte.setLayout(new BorderLayout());
		table = new ScrollPaneCorrecao(this, tags);
		table.getHorizontalScrollBar().setValue(scrollHorizontal);
		table.getVerticalScrollBar().setValue(scrollVertical);
		table.setBackground(CoresDefault.getCorPaineis());
		table.setBorder(new TitledBorder(lineBorder, 
										 GERAL.COD_FONTE, 
										 TitledBorder.CENTER, TitledBorder.CENTER) );
		panelCodigoFonte.add(table, "Center");
		return panelCodigoFonte;
	}
	
	private JPanel createPainelParametro() {
		GridBagLayout bag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel painelParametro = new JPanel();
		painelParametro.setBackground(CoresDefault.getCorPaineis());
		painelParametro.setBorder(new TitledBorder(lineBorder, TradPainelCorrecao.DIGITE_ABAIXO_CORRECAO));
		painelParametro.setLayout(bag);
		
		parametro = new JTextField();
		parametro.setFont(formFont);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 5.0;
		gbc.insets = new Insets(2, 10, 2, 10);
		painelParametro.add(parametro, gbc);
		
		JButton corrigir = new JButton();
		corrigir.setText(GERAL.CORRIGIR);
		corrigir.setActionCommand("CORRIGIR");
		corrigir.addActionListener(this);
        gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 0, 2, 10);
		painelParametro.add(corrigir, gbc);
		
		JButton prosseguir = new JButton();
		prosseguir.setText(GERAL.BTN_SALVAR);
		prosseguir.setActionCommand("PROSSEGUIR");
		prosseguir.addActionListener(this);
        gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 0, 2, 10);
		painelParametro.add(prosseguir, gbc);
		
		JButton voltar = new BotaoColorido(GERAL.VOLTAR, BotaoColorido.VERMELHO);
		voltar.setActionCommand("VOLTAR");
		voltar.addActionListener(this);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 0, 2, 10);
		painelParametro.add(voltar, gbc);

		return painelParametro;
	}
	
	private JPanel createPainelDescricao() {
		JPanel panelDesc = new JPanel();
		panelDesc.setBackground(CoresDefault.getCorPaineis());
		panelDesc.setBorder(new TitledBorder(lineBorder, GERAL.AJUDA));
		panelDesc.add(descricao);
		return panelDesc;
	}
	
	private Tag[][] buscaTags(String url, int tipoAvaliacao, StringBuilder conteudo) {
		Tag[][] tags = null;
		try {
			tags = (new Automaticos()).ArrayTags(url, tipoAvaliacao, conteudo);
		} catch (Exception e) {
			ExceptionDialog.showExceptionDialog(e);
		}
		return tags;
	}
	
    private void corrigir() throws ClassNotFoundException, SQLException {
    	String texto = table.getTagSelecionada().getText();
    	if(texto.equals("")) {
    		return;
    	}
    	scrollHorizontal = table.getHorizontalScrollBar().getValue();
    	scrollVertical = table.getVerticalScrollBar().getValue();
    	Conexao c = new Conexao();
    	Statement st = c.getCon().createStatement();
    	ResultSet rs = st.executeQuery("SELECT ATRIBUTO , IDATO FROM CORRIGIR WHERE pv3 = " + table.getTagSelecionada().getPv3());
    	if(rs.next() && texto.length() > 0)
    	{
    		String atributo = rs.getString(1);
    		int ato = rs.getInt(2);
    		switch(ato)
    		{
    		case 1: // '\001'
    			if (texto.endsWith("/>")) {
    				texto = texto.substring(0, texto.length() - 2) + " " + atributo + "=\"" + parametro.getText() + "\"/>";
    			} else if (texto.endsWith(">")) {
    				texto = texto.substring(0, texto.length() - 1) + " " + atributo + "=\"" + parametro.getText() + "\">";
    			}
    			break;
    		case 2: // '\002'
    			texto = texto + parametro.getText();
    			break;
    			
    		case 3: // '\003'
    			int inicio = texto.toUpperCase().indexOf(atributo);
    			int fim = texto.indexOf("\"", texto.indexOf("\"", inicio) + 1);
    			texto = (texto.substring(0, inicio) + atributo + "=\"" + parametro.getText() + texto.substring(fim));
    			break;
    		}
    	}
    	Posicao p = table.getTagSelecionada().getP();
    	int li = p.getLinha();
    	int co = p.getColuna();
    	Tag t = new Tag(p, texto, table.getTagSelecionada().getEspaco(), -1);
    	tags[li][co] = t;
    }
    
	public void actionPerformed(ActionEvent e) {
		String com = e.getActionCommand();
		if (com != null) {
			if (com.equals("CORRIGIR")) {
				try {
					corrigir();
					montaPainel();
					parentFrame.repaintCentralPane();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			} else if (com.equals("PROSSEGUIR")) {
				salvar();
			} else if (com.equals("VOLTAR")) {
				parentFrame.showLastActivePanel();
			}
		}
	}
	
	public void setDescricao(String text) {
		this.descricao.setText(text);
	}
	
	private void salvar() {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			int r = chooser.showSaveDialog(new JFrame());
			if(r == JFileChooser.APPROVE_OPTION)  {
				String url = chooser.getSelectedFile().getPath();
				FileWriter foriginal = new FileWriter(url + ".bak");
				FileWriter fcorrigido = new FileWriter(url);
				foriginal.write(conteudo.toString());
				foriginal.flush();
				fcorrigido.write(getCodigoCorrigido().toString());
				fcorrigido.flush();
				String msg = GERAL.ARQUIVO_GRAV_SUCESSO;
				JOptionPane.showMessageDialog(this, msg, "", JOptionPane.INFORMATION_MESSAGE);
			} else if (r == JFileChooser.CANCEL_OPTION) {
				String msg = GERAL.GRAV_CANCELADA_USUARIO;
				JOptionPane.showMessageDialog(this, msg, "", JOptionPane.INFORMATION_MESSAGE);
			} else if (r == JFileChooser.ERROR_OPTION) {
//				String msg = "Não foi possível realizar a gravação.\n" +
//							 "Verifique se você possui permissão de escrita\n" +
//							 "no diretório e tente novamente.";
				String msg = GERAL.FALHA_GRAV_ARQUIVO;
				JOptionPane.showMessageDialog(this, msg, "", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
	}
	
	private StringBuilder getCodigoCorrigido() {
		StringBuilder corrigido = new StringBuilder();
		for(int i = 0; i < tags.length; i++) {
			Tag tags2[] = tags[i];
			if(tags2 != null && tags2.length != 0) {
				if(tags2[0].getEspaco() != null) {
					for(int k = 0; k < tags2[0].getEspaco().getEspacoLinha(); k++) {
						corrigido.append(" ");
					}
					for(int k = 0; k < tags2[0].getEspaco().getTabulacaoLinha(); k++) {
						corrigido.append("\t");
					}
				}
				for(int j = 0; j < tags2.length; j++) {
					Tag tag23 = tags2[j];
					if(tag23 != null && tag23.getTexto() != null) { 
						corrigido.append(tag23.getTexto());
					}
				}
			}
			corrigido.append("\n");
		}
		return corrigido;
	}

	@Override
	public boolean showBarraUrl() {
		return false;
	}




	
//	public static void main(String args[]) {
//		(new HiperSonic()).start();
//		JFrame frame = new JFrame();
//		try {
//			frame.add(new PainelCorrecao());
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.pack();
//			frame.setVisible(true);
//		} catch (Exception e) {
//			System.out.println("NEM VAI: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//

}
