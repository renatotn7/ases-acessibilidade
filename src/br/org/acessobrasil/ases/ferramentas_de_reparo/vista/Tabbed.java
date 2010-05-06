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
package br.org.acessobrasil.ases.ferramentas_de_reparo.vista;

import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_TextAreaSourceCode;
/**
 * Exemplo de classe concreta
 * @author Fabio Issamu Oshiro
 *
 */
public class Tabbed extends SuperTabbed{

	public Tabbed(FrameSilvinha frameSilvinha) {
		super(frameSilvinha);
		for (int i = 0; i < 30; i++) {
			this.panelEditavel.tableReadOnly.addLinha(new String[]{"L"+i,"c"+i,"D"+i});	
			this.panelOriginal.tableReadOnly.addLinha(new String[]{"L"+i,"c"+i,"D"+i});
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void avaliaArq(G_File temp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitulo() {
		return "Meu título";
	}

	@Override
	public void reavalia(String codHtml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getColunas() {
		String a[]={"Linha","Coluna","Descrição"};
		return a;
	}

	@Override
	public int[] getMaxWidths() {
		int b[]={60,60};
		return b;
	}

	@Override
	public boolean isShowPainelImg() {
		return true;
	}

	@Override
	public void avaliaArq(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void avaliaUrl(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTableEditableSelected(int row, int col, String valor, G_TextAreaSourceCode textAreaSourceCode) {
		System.out.println("onTableEditableSelected(row="+row+",col="+col+",valor='"+valor+"')");
		this.setImagemPainelEditavel("http://www.sun.com/images/l2/l2_nblogo_vert.gif");
	}

	@Override
	public void onTableOriginalSelected(int row, int col, String valor, G_TextAreaSourceCode textAreaSourceCode) {
		System.out.println("onTableOriginalSelected(row="+row+",col="+col+",valor='"+valor+"')");
		this.setImagemPainelOriginal("http://www.sun.com/images/l2/l2_nblogo_vert.gif");
	}

}
