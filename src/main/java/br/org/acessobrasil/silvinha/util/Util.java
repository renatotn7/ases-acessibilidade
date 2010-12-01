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

package br.org.acessobrasil.silvinha.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que formata o tempo
 * @author André Santos.
 */
public final class Util {

	/**
	 * Calcula o tempo de fim - ini
	 * @param start
	 * @param finish
	 * @return tempo normalizado
	 */
	public static String calcularTempo(final Date start, final Date finish) {
		
		final int secInHour = 3600;
		final int secInMin = 60;

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String hora1 = sdf.format(start);
		String hora2 = sdf.format(finish);

		String horas = "00";
		String minutos = "00";
		String segundos = "00";
		
		int sub = 0;
		int subHoras = 0;
		int subMinutos = 0;

		int segundos1 = 0, segundos2 = 0;
		
		segundos1 = (Integer.parseInt(hora1.substring(0, 2)) * secInHour)
			+ (Integer.parseInt(hora1.substring(3, 5)) * secInMin)
			+ Integer.parseInt(hora1.substring(6, 8));

		segundos2 =
			(Integer.parseInt(hora2.substring(0, 2)) * secInHour)
			+ (Integer.parseInt(hora2.substring(3, 5)) * secInMin)
			+ Integer.parseInt(hora2.substring(6, 8));   

		if (segundos1 > segundos2) {
			sub = segundos1 - segundos2;
		} else if (segundos2 > segundos1) {
			sub = segundos2 - segundos1;
		} else {
			sub = 0;
		}

		if (sub >= secInHour) {
			subHoras = (sub-(sub%secInHour))/secInHour;
			sub = sub - (subHoras*secInHour);
			if (subHoras < 10) {
				horas = "0" + Integer.toString(subHoras);
			} else {
				horas = Integer.toString(subHoras);
			}
		}
		
		if (sub >= secInMin) {
			subMinutos = (sub-(sub%secInMin))/secInMin;
			sub = sub - (subMinutos*secInMin);

			if (subMinutos < 10) {
				minutos = "0"+Integer.toString(subMinutos);
			} else {
				minutos = Integer.toString(subMinutos);
			}
		}

		if (sub < 10) {
			segundos = "0"+Integer.toString(sub);
		} else {
			segundos = Integer.toString(sub);
		}

		return horas + ":" + minutos + ":" + segundos;
	}

}
