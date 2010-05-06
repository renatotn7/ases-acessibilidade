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

import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
/**
 * Classe responsável por guardar o estado do Silvinha 
 *
 */
public class EstadoSilvinha {
	

	
	
	public static final int FER_DESC_LABEL = 0;

	public static final int FER_DESC_OBJ = 1;

	public static final int FER_DESC_IMG = 2;

	public static final int FER_DESC_SCRIPT = 3;

	public static final int FER_CSS = 4;

	public static final int FER_DOCTYPE = 5;

	public static final int FER_XHTML = 6;

	public static final int FER_ANALISE_GERAL_IMG = 0;

	public static final int PAINEL_RELATORIO_ORIGINAL = 0;

	public static final int PAINEL_RELATORIO_EDICAO = 1;

	public static String hashCodeAtual = "" + 0;

	public static int ferramentaAtual = 0;

	public static int sub_ferramentaAtual = 0;

	public static int painel_Atual = 0;

	public static int orgao;

	public static int tipoAvaliacao;

	public static boolean conteudoEmPainelResumo;

	// os booleans abaixo indicam em que painel estamos atualmente
	public static boolean painelAvaliacao;

	public static boolean painelCorrecao;

	/**
	 * True caso esteja com o painel de relatório da avaliação aberto como
	 * último do módulo Silvinha
	 */
	public static boolean painelRelatorio;

	public static boolean painelConfig;

	public static boolean painelResumo;

	public static boolean ferramentaLabelPanel;
	
	public static final int PAINEL_AVALIACAO = 0;
	public static final int PAINEL_CONFIG = 1;
	public static final int PAINEL_CORRECAO = 2;
	public static final int PAINEL_RELATORIO = 3;
	public static final int PAINEL_RESUMO = 4;
	public static final int FERRAMENTA_LABELPANEL = 5;
	
	
	public static void setaPainelAtual(int painel){
		painelAvaliacao = false;
		painelConfig = false;
		painelCorrecao = false;
		painelRelatorio = false;
		painelResumo = false;
		ferramentaLabelPanel = false;
		if(PAINEL_AVALIACAO==painel){
			painelAvaliacao = true;
		}else if(PAINEL_CONFIG==painel){
			painelConfig = true;
		}else if(PAINEL_CORRECAO==painel){
			painelCorrecao = true;
		}else if(PAINEL_RELATORIO==painel){
			painelRelatorio = true;
		}else if(PAINEL_RESUMO==painel){
			painelResumo = true;
		}else if(FERRAMENTA_LABELPANEL==painel){
			ferramentaLabelPanel = true;
		}
	}
     public static int getUltimoPainelAtivo(){
    	 if(painelCorrecao){
    		 return PAINEL_AVALIACAO;
 		}else if(painelConfig){
 			 return PAINEL_CONFIG;
 		}else if(painelCorrecao){
 			 return PAINEL_CORRECAO;
 		}else if(painelRelatorio){
 			 return PAINEL_RELATORIO;
 		}else if(painelResumo){
 			 return PAINEL_RESUMO;
 		}else if(ferramentaLabelPanel){
 			 return FERRAMENTA_LABELPANEL;
 		} 
    	 return -1;
     }

	/**
	 * Guarda o link atual, http://www.algumacoisa.com ou arquivo local
	 * file:///alguma coisa/
	 */
	public static String linkAtual = "";

	/**
	 * @return o link que está sendo trabalhado
	 */
	public static String getLinkAtual() {
		return linkAtual;
	}

	/**
	 * Informar o link que está sendo trabalhado
	 * @param link
	 */
	public static void setLinkAtual(String link) {
		linkAtual = link;
		FrameSilvinha.txtUrl.setText(link);
	}
	
}
