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

package br.org.acessobrasil.ases.ferramentas_de_reparo.controle;

import java.util.ArrayList;
import java.util.HashSet;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.nucleo.InterfClienteDoNucleo;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.NucleoEstruturado;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.silvinha.util.AlteradorDeCsv;

/**
 * Filtra os erros e aplica a correção no código HTML
 * 
 * @author Fabio Issamu Oshiro
 */
public class ControleCorretorEventos implements InterfClienteDoNucleo {

	AlteradorDeCsv alteradorDeCsv;

	private boolean salva = false;

	ArrayList<ArmazenaErroOuAviso> arrErros;

	ArrayList<String> arrErroIni;

	private String codigo;

	InterfNucleos nucleo = new NucleoEstruturado();

	/**
	 * Avalia o código passado como parametro
	 * 
	 * @param codigo
	 */
	public void avalia(String codigo) {
		arrErros = new ArrayList<ArmazenaErroOuAviso>();
		arrErroIni = new ArrayList<String>();

		this.codigo = codigo;
		nucleo = new NucleoEstruturado();
		nucleo.addCliente(this);
		nucleo.setCodHTML(codigo);
		nucleo.setWCAGEMAG(NucleoEstruturado.EMAG);
		nucleo.avalia();
	}

	public void avalia(String conteudo, boolean salva) {

		this.codigo = conteudo;
		if (conteudo == null)
			return;

		this.salva = salva;
		if (EstadoSilvinha.conteudoEmPainelResumo) {
			alteradorDeCsv = new AlteradorDeCsv(EstadoSilvinha.hashCodeAtual);
		}
		nucleo = new NucleoEstruturado();
		nucleo.setCodHTML(conteudo);
		if (EstadoSilvinha.orgao == 2) {
			nucleo.setWCAGEMAG(InterfNucleos.EMAG);
		} else {
			nucleo.setWCAGEMAG(InterfNucleos.WCAG);
		}
		nucleo.addCliente(this);
		nucleo.avalia();

	}

	/**
	 * 
	 * @param erroAtual
	 * @return retorna um par de string com o evento que existe [0] e o que 
	 * deve ser preenchido [1]
	 */
	public String[] getEventoErro(int erroAtual) {
		RegrasHardCodedEmag regras = new RegrasHardCodedEmag();
		String oldTag = this.getTag(erroAtual);
		String onclick = regras.getAtributo(oldTag, "onclick");
		String onmousedown = regras.getAtributo(oldTag, "onmousedown");
		String onmouseup = regras.getAtributo(oldTag, "onmouseup");
		String onmouseover = regras.getAtributo(oldTag, "onmouseover");
		String onmouseout = regras.getAtributo(oldTag, "onmouseout");
		String onkeydown = regras.getAtributo(oldTag, "onkeydown");
		String onkeyup = regras.getAtributo(oldTag, "onkeyup");
		String onkeypress = regras.getAtributo(oldTag, "onkeypress");
		String onfocus = regras.getAtributo(oldTag, "onfocus");
		String onblur = regras.getAtributo(oldTag, "onblur");
		if (!onmousedown.equals("") && onkeydown.equals("")) {
			String retorno[] = { "onkeydown", "onmousedown" };
			return retorno;
		} else if (!onmouseup.equals("") && onkeyup.equals("")) {
			String retorno[] = { "onkeyup", "onmouseup" };
			return retorno;
		} else if (!onmouseover.equals("") && onfocus.equals("")) {
			String retorno[] = { "onfocus", "onmouseover" };
			return retorno;
		} else if (!onmouseout.equals("") && onblur.equals("")) {
			String retorno[] = { "onblur", "onmouseout" };
			return retorno;
		} else if (!onclick.equals("") && onkeypress.equals("")) {
			String retorno[] = { "onkeypress", "onclick" };
			return retorno;
		}
		return null;
	}

	/**
	 * Preenche o evento de javascript
	 * @param text código javascript
	 * @param erroAtual índice do erro atual
	 * @return código HTML da página corrigido
	 */
	public String corrige(String text, int erroAtual) {
		String oldTag = this.getTag(erroAtual), newTag = oldTag;
		RegrasHardCodedEmag regras = new RegrasHardCodedEmag();
		String onclick = regras.getAtributo(oldTag, "onclick");
		String onmousedown = regras.getAtributo(oldTag, "onmousedown");
		String onmouseup = regras.getAtributo(oldTag, "onmouseup");
		String onmouseover = regras.getAtributo(oldTag, "onmouseover");
		String onmouseout = regras.getAtributo(oldTag, "onmouseout");
		String onkeydown = regras.getAtributo(oldTag, "onkeydown");
		String onkeyup = regras.getAtributo(oldTag, "onkeyup");
		String onkeypress = regras.getAtributo(oldTag, "onkeypress");
		String onfocus = regras.getAtributo(oldTag, "onfocus");
		String onblur = regras.getAtributo(oldTag, "onblur");
		String endTag;
		if (newTag.endsWith("/>")) {
			endTag = "/>";
			newTag = newTag.substring(0, newTag.length() - 2);
		} else {
			endTag = ">";
			newTag = newTag.substring(0, newTag.length() - 1);
		}
		if (!onmousedown.equals("") && onkeydown.equals("")) {
			newTag = newTag + " onkeydown=\"" + text + "\" ";
		} else if (!onmouseup.equals("") && onkeyup.equals("")) {
			newTag = newTag + " onkeyup=\"" + text + "\" ";
		} else if (!onmouseover.equals("") && onfocus.equals("")) {
			newTag = newTag + " onfocus=\"" + text + "\" ";
		} else if (!onmouseout.equals("") && onblur.equals("")) {
			newTag = newTag + " onblur=\"" + text + "\" ";
		} else if (!onclick.equals("") && onkeypress.equals("")) {
			newTag = newTag + " onkeypress=\"" + text + "\" ";
		}
		newTag = newTag + endTag;
		this.codigo = this.codigo.replace(oldTag, newTag);
		return this.codigo;
	}

	public void avisosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv.getAvisoP1(EstadoSilvinha.hashCodeAtual);

			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

			alteradorDeCsv.setAvisoP1(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void avisosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv.getAvisoP2(EstadoSilvinha.hashCodeAtual);

			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

			alteradorDeCsv.setAvisoP2(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void avisosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv.getAvisoP3(EstadoSilvinha.hashCodeAtual);

			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

			alteradorDeCsv.setAvisoP3(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void errosP1(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {

		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv.getErroP1(EstadoSilvinha.hashCodeAtual);
			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

			alteradorDeCsv.setErroP1(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void errosP2(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv.getErroP2(EstadoSilvinha.hashCodeAtual);
			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

			alteradorDeCsv.setErroP2(EstadoSilvinha.hashCodeAtual, valor);
		}
		// 2.19
		for (ArmazenaErroOuAviso erro : armazenaErroOuAviso) {
			if (erro.getIdTextoRegra().equals("2.19")) {
				arrErros.add(erro);
				arrErroIni.add(getErroIni(erro) + "");
			}
		}
	}

	public void errosP3(ArrayList<ArmazenaErroOuAviso> armazenaErroOuAviso) {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			String valor = alteradorDeCsv.getErroP3(EstadoSilvinha.hashCodeAtual);
			valor = alteradorDeCsv.reconstroiComBarraENovoValor(valor, armazenaErroOuAviso.size());

			alteradorDeCsv.setErroP3(EstadoSilvinha.hashCodeAtual, valor);
		}
	}

	public void fimDaAvaliacao() {
		if (salva && EstadoSilvinha.conteudoEmPainelResumo) {
			alteradorDeCsv.escreveEmDisco();
		}
		this.salva = false;
	}

	public void linksAchadosPeloNucleo(HashSet<String> links) {
	}

	/**
	 * @return Retorna o total de erros 
	 */
	public int length() {
		return arrErros.size();
	}

	/**
	 * @param i
	 * @return retorna a coluna em que ocorreu o erro
	 */
	public int getColuna(int i) {
		return arrErros.get(i).getColuna();
	}

	/**
	 * @param i
	 * @return retorna a tag completa do erro
	 */
	public String getTag(int i) {
		return arrErros.get(i).getTagCompleta();
	}

	/**
	 * @param i
	 * @return retorna a linha do erro
	 */
	public int getLinha(int i) {
		return arrErros.get(i).getLinha();
	}

	/**
	 * @param i índice o erro
	 * @return retorna o índice do erro
	 */
	public int getIniIndex(int i) {
		return Integer.parseInt(arrErroIni.get(i).toString());
	}

	/**
	 * 
	 * @param erro
	 * @return retorna o indice o erro
	 */
	private int getErroIni(ArmazenaErroOuAviso erro) {
		int linha = erro.getLinha();
		int contaLinha = 1, ini = 0;
		while (contaLinha < linha) {
			ini = this.codigo.indexOf('\n', ini + 1);
			if (ini != -1) {
				contaLinha++;
			} else {
				// linha não encontrada
				return -1;
			}
		}
		ini += erro.getColuna();
		if (ini + 1 >= this.codigo.length())
			return -1;
		return ini;
	}

}