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

import java.io.File;

import br.org.acessobrasil.silvinha2.util.G_File;
/**
 * Altera o arquivo CSV 
 *
 */
public class AlteradorDeCsv {
	private static String strResumoCsv;
	
	String nomeCsv;
	String hashCode;
	int posHashCode;

	public String reconstroiComBarraENovoValor(String valor, int novoValor) {
		int pos = valor.indexOf("/");
		if (pos == -1) {
			
			valor = (Integer.valueOf(valor)-Integer.valueOf(novoValor)) + " / " + valor;
		} else {
			valor = valor.substring(pos+2, valor.length());
			valor = (Integer.valueOf(valor)-Integer.valueOf(novoValor)) + " / " + valor;
		}
		return valor;
	}

	
	public AlteradorDeCsv(String hashCode) {
		posHashCode = 0;
		strResumoCsv = new String();
		this.hashCode=carregaCsvComOHashCode(hashCode);
	}

	public void setAvisosEErros(String hashCode, String avisosP1, String avisosP2, String avisosP3,  String errosP1,String errosP2,String errosP3) {
		setAvisoP1(hashCode, avisosP1);
		setAvisoP2(hashCode, avisosP2);
		setAvisoP3(hashCode, avisosP3);
		setErroP1(hashCode, errosP1);
		setErroP2(hashCode, errosP2);
		setErroP3(hashCode, errosP3);
	}
		public String getErroP1(String hashCode) {

		
		int posAvisoP1 = strResumoCsv.indexOf("\t", posHashCode) + 1;
		int endPosAvisoP1 = strResumoCsv.indexOf("\t", posAvisoP1);
		return (strResumoCsv.substring(posAvisoP1, endPosAvisoP1));

	}
	private void alteraPos(int pos,String valor){
		if(posHashCode==-1){
			carregaCsvComOHashCode(hashCode);
		}
		if(posHashCode==-1){
			System.err.println("Alterador de Csv: HashCode não encontrado nos arquivos");
			return;
		}
		int newLine = strResumoCsv.indexOf("\n",posHashCode+1);
		
		String linha;
		String novaLinha="";
		if (newLine==-1){
			//fim do arquivo pois não achou o newLine
			linha = strResumoCsv.substring(posHashCode);
		}else{
			linha = strResumoCsv.substring(posHashCode,newLine);
		}
		String dados[] = linha.split("\t");

		for(int i = 0;i<dados.length;i++){
			if(i==pos){
				dados[pos]=valor;
			}
			novaLinha+=dados[i]+"\t";
		}
		System.out.println("linha="+linha);
		System.out.println("novaLinha="+novaLinha);
		strResumoCsv = strResumoCsv.replace(linha,novaLinha);
		//return strResumoCsv;
	}
	public void setErroP1(String hashCode, String numAviso) {
		
		alteraPos(1,numAviso);
		
	}
	

	public void setErroP2(String hashCode, String numAviso) {
	
		alteraPos(2,numAviso);
		
	}

	public void setErroP3(String hashCode, String numAviso) {
	
		alteraPos(3,numAviso);
		
	}

	public void setAvisoP1(String hashCode, String numAviso) {

		alteraPos(4,numAviso);
	
	}

	public void setAvisoP2(String hashCode, String numAviso) {
	
		alteraPos(5,numAviso);
	
	}

	public void setAvisoP3(String hashCode, String numAviso) {
	
		alteraPos(6,numAviso);
		
	}

	public void escreveEmDisco(){
		new G_File("temp/" + nomeCsv).write(strResumoCsv);	
	}
	public String getErroP2(String hashCode) {

	
		int posAvisoP2 = strResumoCsv.indexOf("\t", posHashCode) + 1;
		posAvisoP2 = strResumoCsv.indexOf("\t", posAvisoP2) + 1;
		int endPosAvisoP2 = strResumoCsv.indexOf("\t", posAvisoP2);
		return(strResumoCsv.substring(posAvisoP2, endPosAvisoP2));
	}

	public String getErroP3(String hashCode) {

		int posAvisoP3 = strResumoCsv.indexOf("\t", posHashCode) + 1;
		posAvisoP3 = strResumoCsv.indexOf("\t", posAvisoP3) + 1;
		posAvisoP3 = strResumoCsv.indexOf("\t", posAvisoP3) + 1;
		int endPosAvisoP3 = strResumoCsv.indexOf("\t", posAvisoP3);
		return(strResumoCsv.substring(posAvisoP3, endPosAvisoP3));
	}

	public String getAvisoP1(String hashCode) {

	
		int posErroP1 = strResumoCsv.indexOf("\t", posHashCode) + 1;
		posErroP1 = strResumoCsv.indexOf("\t", posErroP1) + 1;
		posErroP1 = strResumoCsv.indexOf("\t", posErroP1) + 1;
		posErroP1 = strResumoCsv.indexOf("\t", posErroP1) + 1;
		int endPosErroP1 = strResumoCsv.indexOf("\t", posErroP1);
		return (strResumoCsv.substring(posErroP1, endPosErroP1));
	}

	public String getAvisoP2(String hashCode) {

		
		int posErroP2 = strResumoCsv.indexOf("\t", posHashCode) + 1;
		posErroP2 = strResumoCsv.indexOf("\t", posErroP2) + 1;
		posErroP2 = strResumoCsv.indexOf("\t", posErroP2) + 1;
		posErroP2 = strResumoCsv.indexOf("\t", posErroP2) + 1;
		posErroP2 = strResumoCsv.indexOf("\t", posErroP2) + 1;
		int endPosErroP2 = strResumoCsv.indexOf("\t", posErroP2);
		return (strResumoCsv.substring(posErroP2, endPosErroP2));
	}

	public String getAvisoP3(String hashCode) {

		
		int posErroP3 = strResumoCsv.indexOf("\t", posHashCode) + 1;
		posErroP3 = strResumoCsv.indexOf("\t", posErroP3) + 1;
		posErroP3 = strResumoCsv.indexOf("\t", posErroP3) + 1;
		posErroP3 = strResumoCsv.indexOf("\t", posErroP3) + 1;
		posErroP3 = strResumoCsv.indexOf("\t", posErroP3) + 1;
		posErroP3 = strResumoCsv.indexOf("\t", posErroP3) + 1;
		int endPosErroP3 = strResumoCsv.indexOf("\t", posErroP3);
		return (strResumoCsv.substring(posErroP3, endPosErroP3));
	}

	public String carregaCsvComOHashCode(String hashCode) {
		int i = 1;
		boolean achou = false;
		if(strResumoCsv!=null) {
				 if(strResumoCsv.indexOf(hashCode)!=-1){
					 return strResumoCsv;
				 }
		}
	
					 while ((new File("temp/resumo" + i + ".csv")).exists() && !achou) {
		
			G_File gFResumoCsv = new G_File("temp/resumo" + i + ".csv");
			nomeCsv = gFResumoCsv.getFile().getName();
			strResumoCsv = gFResumoCsv.read();

			posHashCode = strResumoCsv.indexOf(hashCode);
			if (posHashCode != -1) {
				achou = true;
			}
			i++;
		}
		if (achou == false) {
			System.out.println("alterador de csv erro");
			return "nulo";
		}
		
		return strResumoCsv;
	}

	public static void main(String[] args) {
		new AlteradorDeCsv("12343");
	}
}
