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

package br.org.acessobrasil.ases.nucleo;

import br.org.acessobrasil.ases.nucleo.adapters.AdapterNovoNucleo1;
import br.org.acessobrasil.ases.nucleo.adapters.AdapterNovoNucleo2;
import br.org.acessobrasil.ases.nucleo.adapters.AdapterNucleoAntigo;

/**
 * Classe no padrão de desenho Method Factory que retorna ao cliente um objeto 
 * do nucleo escolhido, para incluir mais um núcleo basta incluir mais um else if(nucleo.equals("***")){return new AdapterNovoNucleo***();}
 * e implementar a interface InterfNucleos. Seria interessante que sempre ao adicionar mais um orgão se utilizasse esta forma para atender ao cliente
 * @author Renato Tomaz Nati
 * @since 30/07/2007
 * @version 1.0
 */

public class MethodFactNucleos {

	/**
	 * Método onde escolhemos qual nucleo se quer utilizar
	 * @return InterfNucleos 
	 * @since 30/07/2007
	 */
	
	public static InterfNucleos mFNucleos(String nucleo){
		if(nucleo.equals("Antigo")){
			return new AdapterNucleoAntigo();
		}else if(nucleo.equals("Estruturado")){
			return new NucleoEstruturado();
		}else if(nucleo.equals("Novo1")){
			return new AdapterNovoNucleo1();
     	}else if(nucleo.equals("Novo2")){
			return new AdapterNovoNucleo2();
     	}
		return null;
	}
}
