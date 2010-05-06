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

package br.org.acessobrasil.ases.entidade;
/**
 * Calcula o total de erros do site 
 *
 */
public class ModeloSite {
	private static String nome;

	private static int avisos;

	private static int avisosP1;

	private static int avisosP2;

	private static int avisosP3;

	private static int erros;

	private static int errosP1;

	private static int errosP2;

	private static int errosP3;

	private static boolean inicializado = false;

	public static void addNumAvisos(int avisos) {
		ModeloSite.avisos += avisos;
		if (!inicializado) {
			inicializa();
		}
	}
	public static void clean(){
		avisos = 0;
		avisosP1 = 0;
		avisosP2 = 0;
		avisosP3 = 0;
		erros = 0;
		errosP1 = 0;
		errosP2 = 0;
		errosP3 = 0;
		inicializado = true;
	}
	
	private static void inicializa() {
		avisos = 0;
		avisosP1 = 0;
		avisosP2 = 0;
		avisosP3 = 0;
		erros = 0;
		errosP1 = 0;
		errosP2 = 0;
		errosP3 = 0;
		inicializado = true;
	}

	public static void addNumAvisosP1(int avisosP1) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.avisosP1 += avisosP1;
	}

	public static void addNumAvisosP2(int avisosP2) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.avisosP2 += avisosP2;
	}

	public static void addNumAvisosP3(int avisosP3) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.avisosP3 += avisosP3;
	}

	public static void addNumErros(int erros) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.erros += erros;
	}

	public static void addNumErrosP1(int errosP1) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.errosP1 += errosP1;
	}

	public static void addNumErrosP2(int errosP2) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.errosP2 += errosP2;
	}

	public static void addNumErrosP3(int errosP3) {

		if (!inicializado) {
			inicializa();
		}
		ModeloSite.errosP3 += errosP3;
	}

	public static int getAvisos() {
		return avisos;

	}

	public static int getAvisosP1() {
		return avisosP1;
	}

	public static int getAvisosP2() {
		return avisosP2;
	}

	public static int getAvisosp3() {
		return avisosP3;
	}

	public static int getErros() {
		return erros;
	}

	public static int getErrosp1() {
		return errosP1;
	}

	public static int getErrosp2() {
		return errosP2;
	}

	public static int getErrosp3() {
		return errosP3;
	}

	public static String getNome() {
		return nome;
	}

	public static void setNome(String nome) {
		ModeloSite.nome = nome;
	}

}
