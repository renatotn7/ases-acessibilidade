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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;

import br.org.acessobrasil.silvinha.entidade.DadosEmpresa;
import br.org.acessobrasil.silvinha.persistencia.DadosEmpresaDAOHSSQLImpl;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.configs.CoresDefault;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradPainelAdministrativo;
/**
 * Tela de configuração que não está sendo utilizado
 * @deprecated
 */
public class PainelAdministrativo extends JPanel {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");
    
	private static final long  serialVersionUID       = -3348105187778647673L;
    private DadosEmpresa de = new DadosEmpresa();
    
    private final Color corDefault = CoresDefault.getCorPaineis();
    private static JTextField tfCNPJ;
    private static JTextField tfInstituicao;
    private static JTextField tfTelefone;
    private static JTextField tfURL;
    private static JTextField tfEmail;
    private static JTextField tfSerial;
    private static JTextField tfEndereco;
    private static JLabel instituicao;
    private static JLabel jLabel10;
    private static JLabel jLabel5;
    private static JLabel jLabel6;
    private static JLabel jLabel7;
    private static JLabel jLabel8;
    private static JLabel jLabel9;


    public PainelAdministrativo () {
        // Inicialização dos atributos
    	
    	tfCNPJ = new JTextField();
        tfInstituicao = new JTextField();
        tfTelefone = new JTextField();
        tfURL = new JTextField("http://");
        tfEmail = new JTextField();
        tfSerial = new JTextField();
        tfEndereco = new JTextField();
        instituicao = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();

        MaskFormatter format_textField;
        try {
            format_textField = new MaskFormatter("***.***.*** / **** - **");
            format_textField.setValidCharacters("0123456789");
            tfCNPJ = new JFormattedTextField(format_textField);
            format_textField = new MaskFormatter("(** **) **** - ****");
            format_textField.setValidCharacters("0123456789");
            tfTelefone = new JFormattedTextField(format_textField);
            format_textField = new MaskFormatter("**** - **** - **** - ****");
            format_textField.setValidCharacters("0123456789");
            tfSerial = new JFormattedTextField(format_textField);
        }
        catch (ParseException exp) 
        {
        	log.error(exp.getMessage(), exp);
        }
        
        this.setLayout(new GridLayout(4, 4, 2, 2));
        this.setBackground(corDefault);
        this.setPreferredSize(new Dimension(10, 90));
        
        instituicao.setHorizontalAlignment(SwingConstants.LEFT);
        //instituicao.setText(TokenLang.INSTITU_LBL);
        instituicao.setText(TradPainelAdministrativo.NOME_INSTITUICAO);
        instituicao.setHorizontalTextPosition(SwingConstants.RIGHT);
        instituicao.setPreferredSize(new Dimension(41, 20));
        instituicao.setVerifyInputWhenFocusTarget(false);
        this.add(instituicao);

        tfInstituicao.setPreferredSize(new Dimension(69, 20));
        this.add(tfInstituicao);

        jLabel5.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel5.setText("          CNPJ");
        jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel5.setMaximumSize(new Dimension(10, 20));
        jLabel5.setMinimumSize(new Dimension(10, 20));
        jLabel5.setPreferredSize(new Dimension(20, 20));
        jLabel5.setVerifyInputWhenFocusTarget(false);
        this.add(jLabel5);

        tfCNPJ.setPreferredSize(new Dimension(69, 20));

        this.add(tfCNPJ);

        jLabel6.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel6.setText(TokenLang.ENDERE_LBL);
        jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel6.setPreferredSize(new Dimension(41, 20));
        jLabel6.setVerifyInputWhenFocusTarget(false);
        this.add(jLabel6);

        tfEndereco.setPreferredSize(new Dimension(69, 20));
        this.add(tfEndereco);

        jLabel7.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel7.setText("          "+GERAL.TELEFONE);
        jLabel7.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel7.setMaximumSize(new Dimension(10, 20));
        jLabel7.setMinimumSize(new Dimension(10, 20));
        jLabel7.setPreferredSize(new Dimension(20, 20));
        jLabel7.setVerifyInputWhenFocusTarget(false);
        this.add(jLabel7);

        tfTelefone.setPreferredSize(new Dimension(69, 20));
        this.add(tfTelefone);

        jLabel8.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel8.setText("Site/URL");
        jLabel8.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel8.setPreferredSize(new Dimension(41, 20));
        jLabel8.setVerifyInputWhenFocusTarget(false);
        this.add(jLabel8);

        tfURL.setPreferredSize(new Dimension(69, 20));
        this.add(tfURL);

        jLabel9.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel9.setText(GERAL.EMAIL);
        jLabel9.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel9.setMaximumSize(new Dimension(10, 20));
        jLabel9.setMinimumSize(new Dimension(10, 20));
        jLabel9.setPreferredSize(new Dimension(20, 20));
        jLabel9.setVerifyInputWhenFocusTarget(false);
        this.add(jLabel9);

        tfEmail.setPreferredSize(new Dimension(69, 20));
        this.add(tfEmail);

        jLabel10.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel10.setText(GERAL.SERIAL_EXECUCAO);
        jLabel10.setHorizontalTextPosition(SwingConstants.RIGHT);
        jLabel10.setVerifyInputWhenFocusTarget(false);
        this.add(jLabel10);

        tfSerial.setPreferredSize(new Dimension(69, 20));
        this.add(tfSerial);
        
        verificaDadosEmpresa();
        mostraDadosEmpresa();
        this.setVisible(true);
    }
    
    public void verificaDadosEmpresa() {
        DadosEmpresaDAOHSSQLImpl ded = new DadosEmpresaDAOHSSQLImpl();
        de = ded.consultar();
    }

    public DadosEmpresa leDadosEmpresa() {
        de.setCnpj(tfCNPJ.getText());
        de.setNome(tfInstituicao.getText());
        de.setTelefone(tfTelefone.getText());
        de.setUrl(tfURL.getText());
        de.setEmail(tfEmail.getText());
        de.setSerial(tfSerial.getText());
        de.setEndereco(tfEndereco.getText());
        return de;
    }
    
    public void mostraDadosEmpresa() {
        tfCNPJ.setText(de.getCnpj());
        tfInstituicao.setText(de.getNome());
        tfTelefone.setText(de.getTelefone());
        tfURL.setText(de.getUrl());
        tfEmail.setText(de.getEmail());
        tfSerial.setText(de.getSerial());
        tfEndereco.setText(de.getEndereco());
    }
}

