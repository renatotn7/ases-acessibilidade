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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.ases.regras.InterfRegrasHardCoded;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.ases.regras.RegrasHardCodedWcag;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;

/**
 * Núcleo que avalia uma e somente uma página durante a sua vida (instância)
 * 
 * @author Fabio Issamu Oshiro
 * 
 */
public class NucleoEstruturado implements InterfNucleos {
	/**
	 * Contém o código HTML da página
	 */
	private String codHTML;
	private String codHTMLOriginal;
	/**
	 * Guarda a regra utilizada InterfNucleos.EMAG ou InterfNucleos.WCAG
	 */
	private int codWcagEmag;

	private static final boolean AVISO = false;

	private static final boolean ERRO = true;

	/**
	 * Guarda se já procurou noscript neste documento
	 */
	private boolean procurouNoScript = false;

	/**
	 * Guarda se tem noscript no documento
	 */
	private boolean temnoscript = false;

	private ArrayList<InterfClienteDoNucleo> cliente;

	private InterfRegrasHardCoded regras;

	/**
	 * Guarda os erros e avisos desta página
	 */
	private ArrayList<ArmazenaErroOuAviso> erroOuAviso = new ArrayList<ArmazenaErroOuAviso>();

	/**
	 * Guarda os erros de mapa de imagem para revalidar Links redundantes
	 */
	private ArrayList<ArmazenaErroOuAviso> erroMapaImagem = new ArrayList<ArmazenaErroOuAviso>();

	private ArrayList<String> erroMapaImagemLink = new ArrayList<String>();

	/**
	 * Guarda os erros de frame que precisam de noframes para revalidar
	 */
	private ArrayList<ArmazenaErroOuAviso> erroPedeNoFrame = new ArrayList<ArmazenaErroOuAviso>();

	private boolean temNoFrames = false;

	/**
	 * Guarda os links desta página
	 */
	private HashSet<String> tagALinks = new HashSet<String>();

	/**
	 * Guarda o último indice do arraylist de erroOuAviso atualizado com linha e
	 * coluna
	 */
	private int ultimoComLinhaColuna = 0;

	/**
	 * Guarda se a tag atual tem erro ou aviso
	 */
	private boolean temErroOuAviso;

	/**
	 * Avaliador estruturado
	 */
	public void avalia() {
		if (regras == null) {
			setWCAGEMAG(InterfNucleos.EMAG);
		}
		// Retirar comentarios de HTML
		retiraComentario();
		int podeH1aH6 = 1;
		int tagCount = 0;
		boolean temDocType = false;
		// Fazer um loop pelas tags
		Pattern pattern = Pattern.compile("<[!?]*?(\\w*?)(\\s.*?>|>)", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(codHTML.toLowerCase());
		trace("codHTML=" + codHTML + "\n");
		int conta = 0;
		while (matcher.find()) {
			temErroOuAviso = false;
			int tagIndex = matcher.start();
			String tagInteira = codHTML.substring(matcher.start(), matcher.end());
			String tagName = matcher.group(1);
			trace("conta=" + (conta++) + "\n");
			trace("tagInteira=" + tagInteira + "\n");
			trace("tagName=" + tagName + "\n");
			// pegar as regras desta tag, colocar as mais frequentes para cima
			String inputType = "";
			if (tagName.equals("p")) {
			} else if (tagName.equals("doctype") && tagCount < 3) {
				temDocType = true;
			} else if (tagName.equals("br")) {
			} else if (tagName.equals("div")) {
			} else if (tagName.equals("td")) {
			} else if (tagName.equals("b")) {
			} else if (tagName.equals("i")) {
			} else if (tagName.equals("a")) {
				String href = regras.getAtributo(tagInteira, "href");
				if (!href.equals("")) {
					if (codWcagEmag == WCAG) {
						// regra de tabindex
						erroOuAviso.add(new ArmazenaErroOuAviso("9.4", AVISO, codWcagEmag, tagInteira));
						// Identificar claramente o destino do link
						erroOuAviso.add(new ArmazenaErroOuAviso("13.1", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						// regra de tabindex
						erroOuAviso.add(new ArmazenaErroOuAviso("1.8", AVISO, codWcagEmag, tagInteira));
						// Identificar claramente o destino do link
						erroOuAviso.add(new ArmazenaErroOuAviso("3.12", AVISO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
					// Guarda o link para bater com mapas de imagens e devolver
					// aos gerentes
					boolean inserido = tagALinks.add(href);
					trace("a href='" + href + "' " + inserido + "\n");
					if (href.toLowerCase().indexOf("javascript:") == 0) {
						// evitar o uso de javascript em links
						if (codWcagEmag == WCAG) {
							if (temNoScript()) {
								// Avisar que pode não funcionar
								erroOuAviso.add(new ArmazenaErroOuAviso("6.4", AVISO, codWcagEmag, tagInteira));
							} else {
								// Erro não funciona sem o noscript
								erroOuAviso.add(new ArmazenaErroOuAviso("6.4", ERRO, codWcagEmag, tagInteira));
							}
						} else if (codWcagEmag == EMAG) {
							if (temNoScript()) {
								// Avisar que pode não funcionar
								erroOuAviso.add(new ArmazenaErroOuAviso("1.19", AVISO, codWcagEmag, tagInteira));
								// Erro duplicado
								erroOuAviso.add(new ArmazenaErroOuAviso("2.18", AVISO, codWcagEmag, tagInteira));
							} else {
								// Erro não funciona sem o noscript
								erroOuAviso.add(new ArmazenaErroOuAviso("1.19", ERRO, codWcagEmag, tagInteira));
							}
						}
					}
					String target = regras.getAtributo(tagInteira, "target");
					if (target.toLowerCase().equals("_blank")) {
						// Outra janela
						if (codWcagEmag == WCAG) {
							erroOuAviso.add(new ArmazenaErroOuAviso("10.1", AVISO, codWcagEmag, tagInteira));
						} else if (codWcagEmag == EMAG) {
							erroOuAviso.add(new ArmazenaErroOuAviso("2.6", AVISO, codWcagEmag, tagInteira));

						}
					}
					/*
					 * Documentos compostos por mais de uma página
					 */
					String hrefLow = href.toLowerCase();
					if (hrefLow.endsWith(".zip") || hrefLow.endsWith(".tar") || hrefLow.endsWith(".gzip") || hrefLow.endsWith(".arj")) {
						if (codWcagEmag == WCAG) {
							// erroOuAviso.add(new ArmazenaErroOuAviso("3.10",
							// AVISO, codWcagEmag, tagInteira));
						} else if (codWcagEmag == EMAG) {
							erroOuAviso.add(new ArmazenaErroOuAviso("3.10", AVISO, codWcagEmag, tagInteira));
						}
					}
					/*
					 * Somente para links
					 */
					if (codHTML.toLowerCase().subSequence(tagIndex - 4, tagIndex).equals("</a>")) {
						// link sem espaço adjacente
						if (codWcagEmag == WCAG) {
							// erroOuAviso.add(new ArmazenaErroOuAviso("3.4",
							// ERRO, codWcagEmag, tagInteira));
						} else if (codWcagEmag == EMAG) {
							erroOuAviso.add(new ArmazenaErroOuAviso("3.4", ERRO, codWcagEmag, tagInteira));
						}

					}
				}
			} else if (tagName.equals("img")) {
				// Ulizar linguagens de marcação ao invés de imagens 2.7
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("3.1", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.7", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
				if (regras.verificaIsGif(tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("7.1", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.7", AVISO, codWcagEmag, tagInteira));
					}
				}
				if (regras.getAtributo(tagInteira, "alt").equals("")) {
					// colocar texto alternativo
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.1", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.11", ERRO, codWcagEmag, tagInteira));
					}
				}
			} else if (tagName.equals("span")) {
			} else if (tagName.equals("table")) {
				if (regras.getAtributo(tagInteira, "summary").equals("")) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.5", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.13", AVISO, codWcagEmag, tagInteira));
					}
				}
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("5.2", AVISO, codWcagEmag, tagInteira));
					erroOuAviso.add(new ArmazenaErroOuAviso("5.3", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.14", AVISO, codWcagEmag, tagInteira));
					erroOuAviso.add(new ArmazenaErroOuAviso("2.12", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("strong")) {
			} else if (tagName.equals("em")) {
			} else if (tagName.equals("ul")) {
			} else if (tagName.equals("ol")) {
				// Evite o uso incorreto desta tag
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("3.6", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.3", AVISO, codWcagEmag, tagInteira));
				}
			} else if (tagName.equals("li")) {
			} else if (tagName.equals("dt")) {
				// Citações “blockquote”, “ul”, “dl” & “dt”, “table”
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("3.7", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.8", AVISO, codWcagEmag, tagInteira));
				}
			} else if (tagName.equals("dd")) {
			} else if (tagName.equals("dl")) {
				// Citações “blockquote”, “ul”, “dl” & “dt”, “table”
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("3.7", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.8", AVISO, codWcagEmag, tagInteira));
				}
			} else if (tagName.equals("th")) {
				// Não usar th para formatar texto em bold
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("5.4", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.13", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("tt")) {
			} else if (tagName.equals("tr")) {
			} else if (tagName.equals("input")) {
				// Verifica a regra de Label
				inputType = regras.getAtributo(tagInteira, "type").toLowerCase();
				if (!regras.verificaLabelTag(this.codHTML, tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("12.4", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.15", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				// Verifica regra de texto predefinido em text e password
				if (!regras.verificaTextoPredefinidoInput(tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("10.4", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.14", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				// verifica se não é do tipo hidden
				if (!inputType.equals("hidden")) {
					if (codWcagEmag == WCAG) {
						// regra de tabindex
						erroOuAviso.add(new ArmazenaErroOuAviso("9.4", AVISO, codWcagEmag, tagInteira));
						// regra de posicionamento de rótulo
						erroOuAviso.add(new ArmazenaErroOuAviso("10.2", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						// regra de tabindex
						erroOuAviso.add(new ArmazenaErroOuAviso("1.8", AVISO, codWcagEmag, tagInteira));
						// regra de posicionamento de rótulo
						erroOuAviso.add(new ArmazenaErroOuAviso("2.16", AVISO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
			} else if (tagName.equals("select")) {
				// Verifica a regra de Label
				if (!regras.verificaLabelTag(this.codHTML, tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("12.4", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.15", ERRO, codWcagEmag, tagInteira));
					}
				}
				if (codWcagEmag == WCAG) {
					// regra de tabindex
					erroOuAviso.add(new ArmazenaErroOuAviso("9.4", AVISO, codWcagEmag, tagInteira));
					// regra de posicionamento de rótulo
					erroOuAviso.add(new ArmazenaErroOuAviso("10.2", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					// regra de tabindex
					erroOuAviso.add(new ArmazenaErroOuAviso("1.8", AVISO, codWcagEmag, tagInteira));
					// regra de posicionamento de rótulo
					erroOuAviso.add(new ArmazenaErroOuAviso("2.16", AVISO, codWcagEmag, tagInteira));
					// Evitar o envio automático
					erroOuAviso.add(new ArmazenaErroOuAviso("3.13", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("option")) {
			} else if (tagName.equals("optgroup")) {
			} else if (tagName.equals("area")) {
				if (codWcagEmag == WCAG) {
					// regra de tabindex
					erroOuAviso.add(new ArmazenaErroOuAviso("9.4", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.8", AVISO, codWcagEmag, tagInteira));
				}
				// Links redundantes
				String href = regras.getAtributo(tagInteira, "href");
				if (!href.equals("")) {
					ArmazenaErroOuAviso erro = new ArmazenaErroOuAviso();
					if (codWcagEmag == WCAG) {
						erro = new ArmazenaErroOuAviso("1.2", ERRO, codWcagEmag, tagInteira);
					} else if (codWcagEmag == EMAG) {
						erro = new ArmazenaErroOuAviso("1.12", ERRO, codWcagEmag, tagInteira);
					}
					erroMapaImagem.add(erro);
					erroOuAviso.add(erro);
					this.erroMapaImagemLink.add(href);
				}
				temErroOuAviso = true;
			} else if (tagName.equals("label")) {
			} else if (tagName.equals("legend")) {
			} else if (tagName.equals("button")) {
				/*
				 * HTML 4
				 */
				// regra de tabindex
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("9.4", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.8", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("map")) {
				/*
				 * HTML 3.2
				 */
			} else if (tagName.equals("hr")) {
			} else if (tagName.equals("meta")) {
				if (regras.getAtributo(tagInteira, "http-equiv").toLowerCase().equals("refresh")) {
					// não dar refresh nem redirect
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("7.4", ERRO, codWcagEmag, tagInteira));
						erroOuAviso.add(new ArmazenaErroOuAviso("7.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.4", ERRO, codWcagEmag, tagInteira));
						erroOuAviso.add(new ArmazenaErroOuAviso("2.5", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
			} else if (tagName.equals("title")) {
			} else if (tagName.equals("link")) {
				/*
				 * Verificar se é de CSS
				 */
				if (!regras.verificaIsCSS(tagInteira)) {
					if (codWcagEmag == WCAG) {
						// Pagina legivel sem style
						erroOuAviso.add(new ArmazenaErroOuAviso("6.2", AVISO, codWcagEmag, tagInteira));
						// Contraste
						erroOuAviso.add(new ArmazenaErroOuAviso("2.2", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						// Pagina legivel sem style
						erroOuAviso.add(new ArmazenaErroOuAviso("1.6", AVISO, codWcagEmag, tagInteira));
						// Contraste
						erroOuAviso.add(new ArmazenaErroOuAviso("1.5", AVISO, codWcagEmag, tagInteira));
						// Info com cor também sem cor
						erroOuAviso.add(new ArmazenaErroOuAviso("1.4", AVISO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
			} else if (tagName.equals("style")) {
				if (codWcagEmag == WCAG) {
					// Pagina legivel sem style
					erroOuAviso.add(new ArmazenaErroOuAviso("6.2", AVISO, codWcagEmag, tagInteira));
					// Contraste
					erroOuAviso.add(new ArmazenaErroOuAviso("2.2", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					// Info com cor também sem cor
					erroOuAviso.add(new ArmazenaErroOuAviso("1.4", AVISO, codWcagEmag, tagInteira));
					// Contraste
					erroOuAviso.add(new ArmazenaErroOuAviso("1.5", AVISO, codWcagEmag, tagInteira));
					// Pagina legivel sem style
					erroOuAviso.add(new ArmazenaErroOuAviso("1.6", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("script")) {
				// verificar se possui noscript
				if (!temNoScript()) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.1", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						//erroOuAviso.add(new ArmazenaErroOuAviso("1.11", ERRO, codWcagEmag, tagInteira));
						erroOuAviso.add(new ArmazenaErroOuAviso("1.20", ERRO, codWcagEmag, tagInteira));
					}
				}
				// Evitar páginas contendo movimento
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("7.3", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.23", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("noscript")) {
			} else if (tagName.equals("caption")) {
			} else if (tagName.equals("form")) {
			} else if (tagName.equals("textarea")) {
				// Verifica a regra de Label
				if (!regras.verificaLabelTag(this.codHTML, tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("12.4", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.15", ERRO, codWcagEmag, tagInteira));
					}
				}
				// Verifica regra de texto predefinido
				if (!regras.verificaTextoPredefinidoTextarea(this.codHTML, tagInteira, tagIndex)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("10.4", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.14", ERRO, codWcagEmag, tagInteira));
					}
				}
				if (codWcagEmag == WCAG) {
					// regra de tabindex
					erroOuAviso.add(new ArmazenaErroOuAviso("9.4", AVISO, codWcagEmag, tagInteira));
					// regra de posicionamento de rótulo
					erroOuAviso.add(new ArmazenaErroOuAviso("10.2", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					// regra de tabindex
					erroOuAviso.add(new ArmazenaErroOuAviso("1.8", AVISO, codWcagEmag, tagInteira));
					// regra de posicionamento de rótulo
					erroOuAviso.add(new ArmazenaErroOuAviso("2.16", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			} else if (tagName.equals("blockquote")) {
				if (codWcagEmag == WCAG) {
					// Citações “blockquote”, “ul”, “dl” & “dt”, “table”
					erroOuAviso.add(new ArmazenaErroOuAviso("3.7", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					// Citações “blockquote”, “ul”, “dl” & “dt”, “table”
					erroOuAviso.add(new ArmazenaErroOuAviso("2.8", AVISO, codWcagEmag, tagInteira));
				}
			} else if (tagName.equals("head")) {
			} else if (tagName.equals("pre")) {
			} else if (tagName.equals("h1")) {
				podeH1aH6 = 2;
			} else if (tagName.equals("h2")) {
				if (2 > podeH1aH6) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.10", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				podeH1aH6 = 3;
			} else if (tagName.equals("h3")) {
				if (3 > podeH1aH6) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.10", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				podeH1aH6 = 4;
			} else if (tagName.equals("h4")) {
				if (4 > podeH1aH6) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.10", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				podeH1aH6 = 4;
			} else if (tagName.equals("h5")) {
				if (5 > podeH1aH6) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.10", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				podeH1aH6 = 5;
			} else if (tagName.equals("h6")) {
				if (6 > podeH1aH6) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.10", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				podeH1aH6 = 6;
			} else if (tagName.equals("body")) {
			} else if (tagName.equals("html")) {
				// Verificar se possui o atributo lang
				if (!regras.verificaLang(tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("4.3", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.1", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
			} else if (tagName.equals("sub")) {
				/*
				 * HTML 3.2
				 */
			} else if (tagName.equals("sup")) {
				/*
				 * HTML 3.2
				 */
			} else if (tagName.equals("frameset")) {
			} else if (tagName.equals("frame")) {
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("6.2", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.15", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
				if (!regras.verificaLongDesc(tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("12.2", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.18", AVISO, codWcagEmag, tagInteira));
					}
				}
				ArmazenaErroOuAviso erro = new ArmazenaErroOuAviso();
				if (codWcagEmag == WCAG) {
					erro = new ArmazenaErroOuAviso("6.5", ERRO, codWcagEmag, tagInteira);

				} else if (codWcagEmag == EMAG) {
					erro = new ArmazenaErroOuAviso("1.16", ERRO, codWcagEmag, tagInteira);
				}
				erroOuAviso.add(erro);
				erroPedeNoFrame.add(erro);
				if (regras.getAtributo(tagInteira, "title").equals("")) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("12.1", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.17", ERRO, codWcagEmag, tagInteira));
					}
				}
			} else if (tagName.equals("noframes")) {
				/*
				 * Guarda que possui noframe
				 */
				this.temNoFrames = true;
			} else if (tagName.equals("iframe")) {
				/*
				 * DTD transitional e frameset
				 */
				if (codWcagEmag == WCAG) {
					// equivalentes atualizados
					erroOuAviso.add(new ArmazenaErroOuAviso("6.2", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					// equivalentes atualizados
					erroOuAviso.add(new ArmazenaErroOuAviso("1.15", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
				if (!regras.verificaLongDesc(tagInteira)) {
					if (codWcagEmag == WCAG) {
						// Descrever a finalidade dos frames
						erroOuAviso.add(new ArmazenaErroOuAviso("12.2", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						// Descrever a finalidade dos frames
						erroOuAviso.add(new ArmazenaErroOuAviso("1.18", AVISO, codWcagEmag, tagInteira));
					}
				}
				if (regras.getAtributo(tagInteira, "title").equals("")) {
					if (codWcagEmag == WCAG) {
						// Dar a cada frame um título
						erroOuAviso.add(new ArmazenaErroOuAviso("12.1", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						// Dar a cada frame um título
						erroOuAviso.add(new ArmazenaErroOuAviso("1.17", ERRO, codWcagEmag, tagInteira));
					}
				}
				if (!regras.verificaTextoIframe(codHTML, tagInteira, tagIndex)) {
					if (codWcagEmag == WCAG) {
						// Texto dentro do iframe
						erroOuAviso.add(new ArmazenaErroOuAviso("6.5", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						// Texto dentro do iframe
						erroOuAviso.add(new ArmazenaErroOuAviso("1.16", ERRO, codWcagEmag, tagInteira));
					}
				}
			} else if (tagName.equals("fieldset")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("colgroup")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("thead")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("tfoot")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("tbody")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("q")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("address")) {
			} else if (tagName.equals("object")) {
				/*
				 * Flash e outros
				 */
				// Piscar
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("7.1", AVISO, codWcagEmag, tagInteira));
					// Avisar que pode não funcionar
					erroOuAviso.add(new ArmazenaErroOuAviso("6.4", AVISO, codWcagEmag, tagInteira));
					// Legendas em movies
					erroOuAviso.add(new ArmazenaErroOuAviso("1.4", AVISO, codWcagEmag, tagInteira));
					// Movimento sem controle
					erroOuAviso.add(new ArmazenaErroOuAviso("7.3", AVISO, codWcagEmag, tagInteira));
					if (!regras.verificaTextoEmObject(codHTML, tagInteira, tagIndex)) {
						// Texto dentro da tag object
						erroOuAviso.add(new ArmazenaErroOuAviso("1.1", ERRO, codWcagEmag, tagInteira));
					}
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.7", AVISO, codWcagEmag, tagInteira));
					// Avisar que pode não funcionar
					erroOuAviso.add(new ArmazenaErroOuAviso("1.19", AVISO, codWcagEmag, tagInteira));
					// Erro ou aviso duplicado da 1.19
					erroOuAviso.add(new ArmazenaErroOuAviso("2.18", AVISO, codWcagEmag, tagInteira));
					// Legendas em movies
					erroOuAviso.add(new ArmazenaErroOuAviso("1.22", AVISO, codWcagEmag, tagInteira));
					// Movimento sem controle
					erroOuAviso.add(new ArmazenaErroOuAviso("1.23", AVISO, codWcagEmag, tagInteira));
					if (!regras.verificaTextoEmObject(codHTML, tagInteira, tagIndex)) {
						// Texto dentro da tag object
						erroOuAviso.add(new ArmazenaErroOuAviso("1.11", ERRO, codWcagEmag, tagInteira));
					}
				}
				temErroOuAviso = true;
			} else if (tagName.equals("base")) {
			} else if (tagName.equals("param")) {
				/*
				 * HTML 3.2
				 */
			} else if (tagName.equals("layer")) {
				/*
				 * Non standard
				 */
			} else if (tagName.equals("small")) {
				/*
				 * Renders as smaller text é relativo
				 */
			} else if (tagName.equals("big")) {
				/*
				 * Renders as bigger text é relativo
				 */
			} else if (tagName.equals("col")) {
				/*
				 * Defines the attribute values for one or more columns in a
				 * table. You can only use this element inside a table or a
				 * colgroup.
				 */
			} else if (tagName.equals("ins")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("del")) {
				/*
				 * HTML4
				 */
			} else if (tagName.equals("dfn")) {
				/*
				 * Indicates that this is the defining instance of the enclosed
				 * term.
				 */
			} else if (tagName.equals("code")) {
				/*
				 * Designates a fragment of computer code.
				 */
			} else if (tagName.equals("samp")) {
				/*
				 * Designates sample output from programs, scripts, etc.
				 */
			} else if (tagName.equals("kbd")) {
				/*
				 * Indicates text to be entered by the user.
				 */
			} else if (tagName.equals("var")) {
				/*
				 * Indicates an instance of a variable or program argument.
				 */
			} else if (tagName.equals("cite")) {
				/*
				 * Contains a citation or a reference to other sources.
				 */
			} else if (tagName.equals("abbr")) {
				/*
				 * Indicates an abbreviated form (e.g., WWW, HTTP, URI, Mass.,
				 * etc.).
				 */
			} else if (tagName.equals("acronym")) {
				/*
				 * Indicates an acronym (e.g., WAC, radar, etc.).
				 */
			} else if (tagName.equals("bdo")) {
				/*
				 * HTML 4 http://www.w3.org/TR/html4/struct/dirlang.html#h-8.2.4
				 */
			} else {
				/*
				 * Tag desconhecida verificar se é Deprecated ou Non standard
				 */
				if (!regras.verificaDeprecatedNonStandard(tagName)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("11.2", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.1", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
				if (tagName.equals("applet")) {
					// tela piscando
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("7.1", AVISO, codWcagEmag, tagInteira));
						// Avisar que pode não funcionar
						erroOuAviso.add(new ArmazenaErroOuAviso("6.4", AVISO, codWcagEmag, tagInteira));
						temErroOuAviso = true;
						// Movimento sem controle
						erroOuAviso.add(new ArmazenaErroOuAviso("7.3", AVISO, codWcagEmag, ""));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.7", AVISO, codWcagEmag, tagInteira));
						// Avisar que pode não funcionar
						erroOuAviso.add(new ArmazenaErroOuAviso("1.19", AVISO, codWcagEmag, tagInteira));
						// Erro ou aviso duplicado da 1.19
						erroOuAviso.add(new ArmazenaErroOuAviso("2.18", AVISO, codWcagEmag, tagInteira));
						temErroOuAviso = true;
						// Movimento sem controle
						erroOuAviso.add(new ArmazenaErroOuAviso("1.23", AVISO, codWcagEmag, ""));
					}
				}
			}
			/*
			 * Verificação geral para todas as tags
			 */
			if (regras.verificaPossuiStyle(tagInteira)) {
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("6.1", AVISO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("1.6", AVISO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
				if (!tagName.equals("applets") && !tagName.equals("object") && !tagName.equals("img")) {
					// erro de tamanho fixo as imagens, objetos, applets podem
					// ter tamanho fixo
					if (!regras.verificaTamFixoEmAtributoStyle(tagInteira)) {
						if (codWcagEmag == WCAG) {
							erroOuAviso.add(new ArmazenaErroOuAviso("3.4", ERRO, codWcagEmag, tagInteira));
						} else if (codWcagEmag == EMAG) {
							erroOuAviso.add(new ArmazenaErroOuAviso("2.2", ERRO, codWcagEmag, tagInteira));
						}
					}
				}
				if (regras.verificaCorStyleInline(tagInteira)) {
					// possui bg ou color no estilo
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.1", AVISO, codWcagEmag, tagInteira));
						erroOuAviso.add(new ArmazenaErroOuAviso("2.2", AVISO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("1.4", AVISO, codWcagEmag, tagInteira));
						erroOuAviso.add(new ArmazenaErroOuAviso("1.5", AVISO, codWcagEmag, tagInteira));
					}
				}
			}
			if(tagInteira.contains("accesskey=\"f\"")){
				if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("4.1",ERRO,codWcagEmag,tagInteira));
				}
			}
			if (!tagName.equals("applets") && !tagName.equals("object") && !tagName.equals("img")) {
				// erro de tamanho fixo as imagens, objetos, applets podem
				// ter tamanho fixo
				if (regras.verificaTamFixoAtributoWidhtHeight(tagInteira)) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("3.4", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.2", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
			}
			/*
			 * resposta a eventos
			 */
			String onclick = regras.getAtributo(tagInteira, "onclick");
			String onmousedown = regras.getAtributo(tagInteira, "onmousedown");
			String onmouseup = regras.getAtributo(tagInteira, "onmouseup");
			String onmouseover = regras.getAtributo(tagInteira, "onmouseover");
			String onmouseout = regras.getAtributo(tagInteira, "onmouseout");
			String onkeydown = regras.getAtributo(tagInteira, "onkeydown");
			String onkeyup = regras.getAtributo(tagInteira, "onkeyup");
			String onkeypress = regras.getAtributo(tagInteira, "onkeypress");
			String onfocus = regras.getAtributo(tagInteira, "onfocus");
			String onblur = regras.getAtributo(tagInteira, "onblur");
			if (!(tagName.equals("a") || inputType.equals("buttom") || inputType.equals("reset") || inputType.equals("submit"))) {
				// verificar onclick menos nestes elementos
				if (!onclick.equals("") && onkeypress.equals("")) {
					if (codWcagEmag == WCAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("9.3", ERRO, codWcagEmag, tagInteira));
					} else if (codWcagEmag == EMAG) {
						erroOuAviso.add(new ArmazenaErroOuAviso("2.19", ERRO, codWcagEmag, tagInteira));
					}
					temErroOuAviso = true;
				}
			}
			if (!onmousedown.equals("") && onkeydown.equals("")) {
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("9.3", ERRO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.19", ERRO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			}
			if (!onmouseup.equals("") && onkeyup.equals("")) {
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("9.3", ERRO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.19", ERRO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			}
			if (!onmouseover.equals("") && onfocus.equals("")) {
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("9.3", ERRO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.19", ERRO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			}
			if (!onmouseout.equals("") && onblur.equals("")) {
				if (codWcagEmag == WCAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("9.3", ERRO, codWcagEmag, tagInteira));
				} else if (codWcagEmag == EMAG) {
					erroOuAviso.add(new ArmazenaErroOuAviso("2.19", ERRO, codWcagEmag, tagInteira));
				}
				temErroOuAviso = true;
			}
			/*
			 * caso tenha erro(s) retornar a linha e a coluna. O cod do erro é
			 * colocado antes
			 */
			if (temErroOuAviso) {
				// pegar a linha e a coluna da tag
				int linha = 1, coluna = 0;
				int aux = codHTML.indexOf("\n", 0);
				int laux = aux;
				while (aux != -1 && aux < tagIndex) {
					laux = aux;
					aux = codHTML.indexOf("\n", aux + 1);
					linha++;
				}
				// calcula a coluna
				coluna = tagIndex - laux;
				// Dar um loop pelos erros guardados e colocar a linha e a
				// coluna
				for (int i = ultimoComLinhaColuna; i < erroOuAviso.size(); i++) {
					erroOuAviso.get(i).setLinha(linha);
					erroOuAviso.get(i).setColuna(coluna);
				}
				ultimoComLinhaColuna = erroOuAviso.size();
				trace("Erro na linha " + linha + " coluna " + coluna + "\n");
				temErroOuAviso = false;
			}
			tagCount++;
			trace("\n");
		}

		/*
		 * Verificação depois de ter os dados da página
		 */
		trace("Verificação depois de ter os dados da página\n");
		for (int i = 0; i < this.erroMapaImagem.size(); i++) {
			// Retira os que possuirem links redundantes
			trace("href='" + this.erroMapaImagemLink.get(i) + "' ");
			if (tagALinks.contains(this.erroMapaImagemLink.get(i))) {
				trace("removido");
				erroOuAviso.remove(erroMapaImagem.get(i));
			}
			trace("\n");
		}
		if (this.temNoFrames) {
			// retira os erros que pedem noframe
			for (int i = 0; i < this.erroPedeNoFrame.size(); i++) {
				erroOuAviso.remove(erroPedeNoFrame.get(i));
			}
		}
		if (!temDocType) {
			// Coloca o erro de doctype quanto este não existe
			if (codWcagEmag == WCAG) {
				erroOuAviso.add(new ArmazenaErroOuAviso("3.2", ERRO, codWcagEmag, ""));
			} else if (codWcagEmag == EMAG) {
				erroOuAviso.add(new ArmazenaErroOuAviso("2.1", ERRO, codWcagEmag, ""));
			}

		}
		if (tagALinks.size() > 3) {
			// Agrupar links
			if (codWcagEmag == WCAG) {
				erroOuAviso.add(new ArmazenaErroOuAviso("13.6", AVISO, codWcagEmag, ""));
			} else if (codWcagEmag == EMAG) {
				erroOuAviso.add(new ArmazenaErroOuAviso("3.7", AVISO, codWcagEmag, ""));
			}

		}
		/*
		 * Colocar os avisos gerais
		 */
		carregaAvisosGerais();

		/*
		 * Fim da avaliação desta página
		 */

		/*
		 * Verificar os clientes
		 */
		if (this.cliente != null) {
			/*
			 * separar os erros
			 */
			ArrayList<ArmazenaErroOuAviso> erroP1 = new ArrayList<ArmazenaErroOuAviso>();
			ArrayList<ArmazenaErroOuAviso> erroP2 = new ArrayList<ArmazenaErroOuAviso>();
			ArrayList<ArmazenaErroOuAviso> erroP3 = new ArrayList<ArmazenaErroOuAviso>();
			ArrayList<ArmazenaErroOuAviso> avisoP1 = new ArrayList<ArmazenaErroOuAviso>();
			ArrayList<ArmazenaErroOuAviso> avisoP2 = new ArrayList<ArmazenaErroOuAviso>();
			ArrayList<ArmazenaErroOuAviso> avisoP3 = new ArrayList<ArmazenaErroOuAviso>();
			for (int j = 0; j < erroOuAviso.size(); j++) {
				int priori = regras.getPrioridadeRegra(erroOuAviso.get(j).getIdTextoRegra());
				switch (priori) {
				case 1:
					if (erroOuAviso.get(j).isAviso()) {
						avisoP1.add(erroOuAviso.get(j));
					} else {
						erroP1.add(erroOuAviso.get(j));
					}
					break;
				case 2:
					if (erroOuAviso.get(j).isAviso()) {
						avisoP2.add(erroOuAviso.get(j));
					} else {
						erroP2.add(erroOuAviso.get(j));
					}
					break;
				case 3:
					if (erroOuAviso.get(j).isAviso()) {
						avisoP3.add(erroOuAviso.get(j));
					} else {
						erroP3.add(erroOuAviso.get(j));
					}
					break;
				}
			}
			/*
			 * Mandar os erros para os clientes
			 */
			int tot = this.cliente.size();
			for (int i = 0; i < tot; i++) {
				this.cliente.get(i).linksAchadosPeloNucleo(tagALinks);
				this.cliente.get(i).errosP1(erroP1);
				this.cliente.get(i).errosP2(erroP2);
				this.cliente.get(i).errosP3(erroP3);
				this.cliente.get(i).avisosP1(avisoP1);
				this.cliente.get(i).avisosP2(avisoP2);
				this.cliente.get(i).avisosP3(avisoP3);
			}
			/*
			 * Mandar sinal de termino para os clientes
			 */
			for (int i = 0; i < tot; i++) {
				this.cliente.get(i).fimDaAvaliacao();
			}
		}
	}

	/**
	 * Carrega os avisos gerais no arraylist erroOuAviso
	 */
	private void carregaAvisosGerais() {
		if (codWcagEmag == WCAG) {
			/*
			 * Mudanças de idioma
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("4.1", AVISO, codWcagEmag, ""));
			/*
			 * Ignorar arte ascii
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.10", AVISO, codWcagEmag, ""));
			/*
			 * Utilizar a linguagem mais clara e simples possível
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("14.1", AVISO, codWcagEmag, ""));
			/*
			 * navegação de maneira coerente
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.4", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("14.2", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("11.4", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("14.3", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("12.3", AVISO, codWcagEmag, ""));
			/*
			 * Fornecer mapa
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.3", AVISO, codWcagEmag, ""));
			/*
			 * Abreviaturas
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("4.2", AVISO, codWcagEmag, ""));
			/*
			 * Fornecer atalho
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("9.5", AVISO, codWcagEmag, ""));
			/*
			 * Preferências (por ex., por idioma ou por tipo de conteúdo).
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("11.3", AVISO, codWcagEmag, ""));
			/*
			 * BreadCrumb
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.5", AVISO, codWcagEmag, ""));
			/*
			 * funções de pesquisa
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.7", AVISO, codWcagEmag, ""));
			/*
			 * front-loading
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.8", AVISO, codWcagEmag, ""));
			/*
			 * Documentos compostos por mais de uma página
			 */
			// comentado por não ter achado equi
			// erroOuAviso.add(new ArmazenaErroOuAviso("3.10", AVISO,
			// codWcagEmag, ""));
			/*
			 * Complementar o texto com imagens, etc.
			 */
			// erroOuAviso.add(new ArmazenaErroOuAviso("3.11", AVISO,
			// codWcagEmag, ""));
			/*
			 * Forneça metadados.
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("13.2", AVISO, codWcagEmag, ""));
		} else if (codWcagEmag == EMAG) {
			/*
			 * Mudanças de idioma
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("1.2", AVISO, codWcagEmag, ""));
			/*
			 * Ignorar arte ascii
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("1.3", AVISO, codWcagEmag, ""));
			/*
			 * Utilizar a linguagem mais clara e simples possível
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("1.9", AVISO, codWcagEmag, ""));
			/*
			 * navegação de maneira coerente
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("1.10", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("1.21", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("1.24", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("2.9", AVISO, codWcagEmag, ""));
			erroOuAviso.add(new ArmazenaErroOuAviso("2.11", AVISO, codWcagEmag, ""));
			/*
			 * Fornecer mapa
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("2.17", AVISO, codWcagEmag, ""));
			/*
			 * Abreviaturas
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.2", AVISO, codWcagEmag, ""));
			/*
			 * Fornecer atalho
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.3", AVISO, codWcagEmag, ""));
			/*
			 * Preferências (por ex., por idioma ou por tipo de conteúdo).
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.5", AVISO, codWcagEmag, ""));
			/*
			 * BreadCrumb
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.6", AVISO, codWcagEmag, ""));
			/*
			 * funções de pesquisa
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.8", AVISO, codWcagEmag, ""));
			/*
			 * front-loading
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.9", AVISO, codWcagEmag, ""));
			/*
			 * Documentos compostos por mais de uma página
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.10", AVISO, codWcagEmag, ""));
			/*
			 * Complementar o texto com imagens, etc.
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.11", AVISO, codWcagEmag, ""));
			/*
			 * Forneça metadados.
			 */
			erroOuAviso.add(new ArmazenaErroOuAviso("3.14", AVISO, codWcagEmag, ""));
		}

	}

	/**
	 * Retira os comentários de HTML, porém manter as linhas e colunas
	 */
	private void retiraComentario() {
		int ini = codHTML.indexOf("<!--");
		while (ini != -1) {
			// procurar o "-->"
			String enters = "";
			int fim = codHTML.indexOf("-->", ini);
			// contar os enters
			for (int i = ini; i < fim; i++) {
				char c = codHTML.charAt(i);
				if (c == '\n' || c == '\t' || c == '\r') {
					// mantém os enters, tabs, etc.
					enters += c;
				} else {
					// mantém a coluna
					enters += " ";
				}
			}
			// Os referente a -->
			enters += "   ";
			codHTML = codHTML.substring(0, ini) + enters + codHTML.substring(fim + 3);
			ini = codHTML.indexOf("<!--", ini);
		}
	}

	public void addCliente(InterfClienteDoNucleo objCliente) {
		if (this.cliente == null) {
			this.cliente = new ArrayList<InterfClienteDoNucleo>();
		}
		this.cliente.add(objCliente);
	}

	public ArrayList<ArmazenaErroOuAviso> getValidados(RelatorioDaUrl relatorio, Properties props) {
		return null;
	}

	public void setWCAGEMAG(int ConstRegra) {
		codWcagEmag = ConstRegra;
		switch (ConstRegra) {
		case InterfNucleos.EMAG:
			regras = new RegrasHardCodedEmag();
			break;
		case InterfNucleos.WCAG:
			regras = new RegrasHardCodedWcag();
			break;
		default:
			regras = new RegrasHardCodedEmag();
			break;
		}
	}

	public void setAvaliaCSS(boolean s) {
	}

	public void setEscolheErro(int cod_erro) {
	}

	public void setCodHTML(String codigo) {
		this.codHTMLOriginal = codigo;
		this.codHTML = codigo;
	}

	public String getCodHTML() {
		return codHTML;
	}

	public String getCodHTMLOriginal() {
		return codHTMLOriginal;
	}

	/**
	 * Verifica se existe a tag noscript no documento
	 * 
	 * @return true caso exista
	 */
	private boolean temNoScript() {
		if (!procurouNoScript) {
			temnoscript = regras.verificaNoscript(codHTML);
			procurouNoScript = true;
		}
		return temnoscript;
	}

	public InterfRegrasHardCoded getRegras() {
		return regras;
	}

	public void setRegras(InterfRegrasHardCoded regras) {
		this.regras = regras;
	}

	/**
	 * Retorna os erros de forma bruta (Sem tratamento)
	 * 
	 * @return
	 */
	public ArrayList<ArmazenaErroOuAviso> getErroOuAviso() {
		return erroOuAviso;
	}

	/**
	 * Utilizado para debug 
	 * @param t
	 */
	private void trace(String t) {
	}

	public int getCodWcagEmag() {
		return codWcagEmag;
	}
}
