/*******************************************************************************
 * Copyright 2005, 2006, 2007, 2008 Acessibilidade Brasil
 * Este arquivo é parte do programa ASES - Avaliador e Simulador para AcessibilidadE de Sítios
 * O ASES é um software livre; você pode redistribui-lo e/ou modifica-lo dentro dos termos da Licença Pública Geral GNU como
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da Licença, ou (na sua opnião) qualquer versão posterior.
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer  MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU para maiores detalhes.
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU, sob o título "LICENCA.txt", junto com este programa, se não, escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *******************************************************************************/
package br.org.acessobrasil.silvinha2.util.onChange;


import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
/**
 * Controla quando o texto é alterado como o JavaScript 
 * @author Renato Tomaz Nati
 */
public class OnChange extends JFrame implements  FocusListener, KeyListener,OnChangeListener{
	
	private static final long serialVersionUID = 1L;
	G_TextAreaSourceCode boxCode;
	boolean keyTyped=false;
	public OnChangeListener itam;
	
	
	public OnChange(G_TextAreaSourceCode boxCode,OnChangeListener itam){
		this.boxCode=boxCode;
		this.boxCode.getTextPane().addFocusListener(this);
		this.boxCode.getTextPane().addKeyListener(this);
		this.itam=itam;
	}
	
	/**
	 * se alterou alguma tecla e perde o focus chama
	 * o método altTextFocusLost do objeto que foi repassado
	 * para esse objeto
	 */
	public void focusLost(FocusEvent focusevent) {
		if(keyTyped){
			itam.altTextFocusLost();
			keyTyped=false;
		}
	}
	
	public void keyPressed(KeyEvent arg0) {
		keyTyped=true;
	}
	
	
	public void focusGained(FocusEvent focusevent) {}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void altTextFocusLost() {}
}