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
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.border.Border;

import br.org.acessobrasil.ases.entidade.Espaco;
import br.org.acessobrasil.ases.entidade.Posicao;
import br.org.acessobrasil.ases.entidade.Tag;
/**
 * Visualiza uma tag
 */
public class TagTextField extends JTextField {

	private static final long serialVersionUID = -950563813283070061L;

	public boolean selected;

	private Tag tag;

	public int id = 0;

	public TagTextField() {
	}

	public Border linhabordaantiga;

	public Color corBackgroundAntigo;

	public Color corForegroundAntigo;

	public int posicaoEmLinha = 0;

	public int linha;

	public static boolean bcoinicializado;

	public static Statement st;

	public TagTextField(Tag tag) {
		if (tag.getTagInteira() != null) {
			this.setText(tag.getTagInteira());
		}
		this.tag = tag;
	}

	public Espaco getEspaco() {
		return this.tag.getEspaco();
	}

	public void setEspaco(Espaco espaco) {
		this.tag.setEspaco(espaco);
	}

	public Posicao getP() {
		return this.tag.getP();
	}

	public void setP(Posicao p) {
		this.tag.setP(p);
	}

	public Color getCorBackgroundAntigo() {
		return corBackgroundAntigo;
	}

	public void setCorBackgroundAntigo(Color corBackgroundAntigo) {
		this.corBackgroundAntigo = corBackgroundAntigo;
	}

	public Border getLinhabordaantiga() {
		return linhabordaantiga;
	}

	public void setLinhabordaantiga(Border linhabordaantiga) {
		this.linhabordaantiga = linhabordaantiga;
	}

	public Color getCorForegroundAntigo() {
		return corForegroundAntigo;
	}

	public void setCorForegroundAntigo(Color corForegroundAntigo) {
		this.corForegroundAntigo = corForegroundAntigo;
	}

	public int getPosicaoEmLinha() {
		return posicaoEmLinha;
	}

	public void setPosicaoEmLinha(int posicaoEmLinha) {
		this.posicaoEmLinha = posicaoEmLinha;
	}

	public String getTag() {

		return regexString("<.*", tag.getTagInteira()).substring(1);

	}

	private String regexString(String reg, String source) {
		// Regex
		Pattern pattern = Pattern.compile(reg);
		// Texto onde procurar o regex
		Matcher matcher = pattern.matcher(source);
		if (matcher.find())
			return matcher.group();
		else {
			System.out.println("Silvinha2.TagTextField - erro: tag sem texto");
			return null;

		}
	}

}
