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

package br.org.acessobrasil.ases.regras;
/**
 * Classe no padrão de desenho Method Factory que retorna ao cliente um objeto 
 * do Orgão escolhido, para incluir mais um orgão basta incluir mais um else if(orgao.equals("***")){return new RegrasHardCodedorgao***();}
 * e implementar a interface InterfRegrasHardCoded. Seria interessante que sempre ao adicionar mais um orgão se utilizasse esta forma para atender ao cliente
 * @author Renato Tomaz Nati
 * @since 27/07/2007
 * @version 1.0
 */

public class MethodFactRegHardCod {

	/**
	 * @author Renato Tomaz Nati
	 * @return InterfRegrasHardCoded   
	 * @since 27/07/2007
	 * @version 1.0
	 */
	public static InterfRegrasHardCoded mFRegHardCod(String orgao){
	
		
		
		
		if(orgao.equals("EMAG")){
			
			return new RegrasHardCodedEmag();
		}else if(orgao.equals("WCAG")){
		
			return new RegrasHardCodedWcag();
     	}
		
		
		
		
		return null;
	}
	
}
