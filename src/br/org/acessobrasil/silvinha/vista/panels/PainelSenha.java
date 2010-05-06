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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;
import org.apache.log4j.Logger;

import br.org.acessobrasil.silvinha.vista.listeners.ActivateNextTextFieldListener;
import br.org.acessobrasil.silvinha2.mli.GERAL;

/**
 * Painel onde o usuário informa a senha 
 *
 */
public class PainelSenha extends JPanel implements CredentialsProvider {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha");
	private static final long  serialVersionUID       = -334810518777864767L;
	
	private String user = "";
	private String password = "";
	private JLabel lblNome;
	private JLabel lblPass;
	private JTextField txtName;
	private JPasswordField txtPass;
	private JOptionPane op;
	private boolean cancelAuth = false;
	
	public PainelSenha() {
	
		lblNome = new JLabel(GERAL.USUARIO);
		lblPass = new JLabel(GERAL.SENHA);
		txtName = new JTextField(10);
		txtPass = new JPasswordField(10);
		
		GridBagLayout bag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(bag);
		
		lblNome.setHorizontalAlignment(JLabel.RIGHT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 10, 2, 10);
		this.add(lblNome, gbc);
		
		txtName.setAlignmentX(SwingConstants.WEST);
		txtName.addActionListener(new ActivateNextTextFieldListener(txtPass));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(2, 0, 2, 10);
		this.add(txtName, gbc);
		
		lblPass.setHorizontalAlignment(JLabel.RIGHT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2, 10, 2, 10);
		this.add(lblPass, gbc);
		
		txtPass.setAlignmentX(SwingConstants.WEST);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(2, 0, 2, 10);
		this.add(txtPass, gbc);
		
		setSize( 300, 130 );
		setLocation(300, 300);
//		this.setBackground(corDefault);
		
		op = new JOptionPane(this, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
//		op.setBackground(corDefault);
		
	}
	
	
	public Credentials getCredentials(final AuthScheme authscheme, final String host, 
			int port, boolean proxy)
	throws CredentialsNotAvailableException {
		if (authscheme == null) {
			return null;
		}
		try{
			if (authscheme instanceof NTLMScheme) {
				this.getPassword();
				if (cancelAuth) {
					return null;
				}
				return new NTCredentials(this.user, this.password, host, host);    
			} else if (authscheme instanceof RFC2617Scheme) {
				this.getPassword();
				if (cancelAuth) {
					return null;
				}
				return new UsernamePasswordCredentials(user, password);    
			} else {
				throw new CredentialsNotAvailableException("Unsupported authentication scheme: " +
						authscheme.getSchemeName());
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new CredentialsNotAvailableException(e.getMessage(), e);
		}
	}
	
	private void getPassword() {
		JDialog dialog = op.createDialog(this, GERAL.SENHA_SOLICITADA_SERVIDOR);
		dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dialog.setVisible(true);
		Object opcao = op.getValue();
		if (opcao != null) {
			if (opcao.equals(JOptionPane.OK_OPTION)) {
				this.user = txtName.getText();
				this.password = String.valueOf(txtPass.getPassword());
			} else if (opcao.equals(JOptionPane.CANCEL_OPTION)) {
				this.cancelAuth = true;
			}
		}
	}
	
}



