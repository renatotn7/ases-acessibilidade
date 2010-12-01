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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem;
/**
 * Guarda informação sobre o erro 
 */
class FerramentaDescricaoModel {
	/**
	 * Linha onde a tag está localizada.
	 */
	private int linha;

	/**
	 * Coluna onde a tag está localizada.
	 */
	private int coluna;

	private String texto;

	/**
	 * Construtor de Posicao.
	 * 
	 * @param posLinha
	 *            Linha onde está a tag.
	 * @param posCol
	 *            Coluna onde está a tag.
	 */
	public FerramentaDescricaoModel(final int posLinha, final int posCol) {

		this.linha = posLinha;
		this.coluna = posCol;
	}

	public FerramentaDescricaoModel() {
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return Retorna as coordenadas da tag HTML (linha,coluna).
	 */
	@Override
	public String toString() {
		return this.linha + " | " + this.coluna;
	}

	/**
	 * @return Retorna o valor de coluna.
	 */
	public int getColuna() {
		return coluna;
	}

	/**
	 * @param numCol
	 *            Seta o valor de coluna.
	 */
	public void setColuna(final int numCol) {
		this.coluna = numCol;
	}

	/**
	 * @return Retorna o valor de linha.
	 */
	public int getLinha() {
		return linha;
	}

	/**
	 * @param numLinha
	 *            Seta o valor de linha.
	 */
	public void setLinha(final int numLinha) {
		this.linha = numLinha;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}