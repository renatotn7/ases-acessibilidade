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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;

import br.org.acessobrasil.ases.regras.InterfRegrasHardCoded;
import br.org.acessobrasil.ases.regras.MethodFactRegHardCod;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.entidade.PontoVerificacao;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.entidade.ResumoDoRelatorio;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.panels.PainelStatusBar;
import br.org.acessobrasil.silvinha2.mli.TradGeraRelatorioPDF;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
/**
 * Gera relatórios em PDF 
 *
 */
public class GeraRelatorioPDF {

	private static Logger log = Logger
			.getLogger("br.org.acessobrasil.silvinha");

	private static float progresso;
	public static boolean geraRelatorio(File file) {
		TradGeraRelatorioPDF.carregaTexto(TokenLang.LANG);
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(file));
			writer.setPageEvent(new HeaderAndFooter());
			document.open();
			int linha=0,j, tot_pg=ResumoDoRelatorio.getTotPage();
			//calcula o total de htmls
			float tot_link = 0.0f;
			for (j = 1; j <= tot_pg; j++) {
				String conteudo=ResumoDoRelatorio.getPage(j);
				String arr[] = conteudo.split("\n");
				tot_link+=arr.length;
			}
			PainelStatusBar.showProgTarReq();
			//loop por todas as páginas da paginacao
			for (j = 1; j <= tot_pg; j++) {
				String conteudo=ResumoDoRelatorio.getPage(j);
				String arr[] = conteudo.split("\n");
				int tot = arr.length;
				int i;
				//loop por todos os links
				for (i = 0; i < tot; i++) {
					progresso = (linha++/tot_link)*100.0f;
					PainelStatusBar.setValueProgress((int)progresso);
					String arr2[] = arr[i].split("\t");

					if (arr2.length == 8) {

						String strUrl = arr2[7];// URL
						/*
						int ap1 = Integer.parseInt(arr2[4]);// Aviso1
						int ep1 = Integer.parseInt(arr2[1]);// Erro1
						int ap2 = Integer.parseInt(arr2[5]);// Aviso2
						int ep2 = Integer.parseInt(arr2[2]);// Erro2
						int ap3 = Integer.parseInt(arr2[6]);// Aviso3
						int ep3 = Integer.parseInt(arr2[3]);// Erro3
						*/
						String myHash = arr2[0];

						RelatorioDaUrl relatorio = new RelatorioDaUrl();
						relatorio.recarregaArquivoRelatorioEmXml2(myHash);

						document.add(new Phrase("\n"));
						Font font = FontFactory.getFont(FontFactory.HELVETICA,
								Font.DEFAULTSIZE, Font.BOLD);

						Chunk url = new Chunk(strUrl, font);
						Paragraph p1 = new Paragraph(TradGeraRelatorioPDF.RELATORIO_URL);
						p1.add(url);
						p1.setAlignment(Paragraph.ALIGN_LEFT);
						document.add(p1);
						document.add(new Phrase("\n\n"));

						if (relatorio.getErrosPrioridade1() <= 0
								&& relatorio.getErrosPrioridade2() <= 0
								&& relatorio.getErrosPrioridade3() <= 0) {
							Paragraph p2 = new Paragraph(TradGeraRelatorioPDF.PAGINAS_SEM_ERROS);
							p2.setAlignment(Paragraph.ALIGN_CENTER);
							document.add(p2);
							
						}else{

							if (relatorio.getErrosPrioridade1() > 0) {
								Paragraph p = new Paragraph(TradGeraRelatorioPDF.ERROS_P1, font);
								p1.setAlignment(Paragraph.ALIGN_LEFT);
								document.add(p);
								document.add(new Chunk("\n"));
								PdfPTable table = geraLista(relatorio
										.getListaErrosP1());
								if (table != null) {
									document.add(table);
								}
								document.add(new Phrase("\n\n"));
							}
	
							if (relatorio.getErrosPrioridade2() > 0) {
								Paragraph p = new Paragraph(TradGeraRelatorioPDF.ERROS_P2, font);
								p1.setAlignment(Paragraph.ALIGN_LEFT);
								document.add(p);
								document.add(new Chunk("\n"));
								PdfPTable table = geraLista(relatorio
										.getListaErrosP2());
								if (table != null) {
									document.add(table);
								}
								document.add(new Phrase("\n\n"));
							}
	
							if (relatorio.getErrosPrioridade3() > 0) {
								Paragraph p = new Paragraph(TradGeraRelatorioPDF.ERROS_P3, font);
								p1.setAlignment(Paragraph.ALIGN_LEFT);
								document.add(p);
								document.add(new Chunk("\n"));
								PdfPTable table = geraLista(relatorio
										.getListaErrosP3());
								if (table != null) {
									document.add(table);
								}
								document.add(new Phrase("\n\n"));
							}
							document.newPage();
						}
					}
				}
			}
			writer.flush();
			document.close();
		} catch (DocumentException de) {
			log.error(de.getMessage(), de);
			PainelStatusBar.hideProgTarReq();
			return false;
		} catch (IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			PainelStatusBar.hideProgTarReq();
			return false;
		}
		PainelStatusBar.hideProgTarReq();
		return true;
	}

	public static boolean geraRelatorio_bkp(File file, ResumoDoRelatorio resumo) {

		Document document = new Document(PageSize.A4);
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(file));
			writer.setPageEvent(new HeaderAndFooter());
			document.open();

			ArrayList<RelatorioDaUrl> relatorios = resumo.getRelatorios();
			loop: for (RelatorioDaUrl relatorio : relatorios) {
				document.add(new Phrase("\n"));
				Font font = FontFactory.getFont(FontFactory.HELVETICA,
						Font.DEFAULTSIZE, Font.BOLD);

				Chunk url = new Chunk(relatorio.getUrl(), font);
				Paragraph p1 = new Paragraph(TradGeraRelatorioPDF.RELATORIO_URL);
				p1.add(url);
				p1.setAlignment(Paragraph.ALIGN_LEFT);
				document.add(p1);
				document.add(new Phrase("\n\n"));

				if (relatorio.getErrosPrioridade1() <= 0
						&& relatorio.getErrosPrioridade2() <= 0
						&& relatorio.getErrosPrioridade3() <= 0) {
					Paragraph p2 = new Paragraph(TradGeraRelatorioPDF.PAGINAS_SEM_ERROS);
					p2.setAlignment(Paragraph.ALIGN_CENTER);
					document.add(p2);
					continue loop;
				}

				if (relatorio.getErrosPrioridade1() > 0) {
					Paragraph p = new Paragraph(TradGeraRelatorioPDF.ERROS_P1_SEM_TAB, font);
					p1.setAlignment(Paragraph.ALIGN_LEFT);
					document.add(p);
					document.add(new Chunk("\n"));
					PdfPTable table = geraLista(relatorio.getListaErrosP1());
					if (table != null) {
						document.add(table);
					}
					document.add(new Phrase("\n\n"));
				}

				if (relatorio.getErrosPrioridade2() > 0) {
					Paragraph p = new Paragraph(TradGeraRelatorioPDF.ERROS_P2_SEM_TAB, font);
					p1.setAlignment(Paragraph.ALIGN_LEFT);
					document.add(p);
					document.add(new Chunk("\n"));
					PdfPTable table = geraLista(relatorio.getListaErrosP2());
					if (table != null) {
						document.add(table);
					}
					document.add(new Phrase("\n\n"));
				}

				if (relatorio.getErrosPrioridade3() > 0) {
					Paragraph p = new Paragraph(TradGeraRelatorioPDF.ERROS_P3_SEM_TAB, font);
					p1.setAlignment(Paragraph.ALIGN_LEFT);
					document.add(p);
					document.add(new Chunk("\n"));
					PdfPTable table = geraLista(relatorio.getListaErrosP3());
					if (table != null) {
						document.add(table);
					}
					document.add(new Phrase("\n\n"));
				}
				document.newPage();
			}

			writer.flush();
			document.close();
		} catch (DocumentException de) {
			log.error(de.getMessage(), de);
			return false;
		} catch (IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			return false;
		}
		return true;
	}

	private static void geraCabecalho(PdfPTable table) {
		PdfPCell cell;
		cell = new PdfPCell(new Paragraph(TradGeraRelatorioPDF.P_V));
		cell.setBorderWidth(1.5f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(TradGeraRelatorioPDF.DESCRICAO));
		cell.setColspan(3);
		cell.setBorderWidth(1.5f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(TradGeraRelatorioPDF.OCORRENCIAS));
		cell.setBorderWidth(1.5f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(TradGeraRelatorioPDF.LINHAS));
		cell.setBorderWidth(1.5f);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		table.addCell(cell);
	}

	private static PdfPTable geraLista(HashSet<PontoVerificacao> pvs) {
		// Inicializa a Tabela
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		geraCabecalho(table);
		// Preenche os dados da Tabela
		HashSet<PontoVerificacao> pontos = pvs;
		for (PontoVerificacao pv : pontos) {
			PdfPCell cell;
			// Ponto de Verificacao
			cell = new PdfPCell(new Paragraph(String.valueOf(pv.getGl()) + "."
					+ String.valueOf(pv.getCp())));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			table.addCell(cell);
			// Descrição da Regra
			String descricao;
			int tipoaval=Integer.parseInt(Gerente.getProperties().getProperty("tipo_avaliacao"));
			if(tipoaval==2){
				//pegar do EMAG
				InterfRegrasHardCoded regras = MethodFactRegHardCod.mFRegHardCod("EMAG");
				descricao = regras.getTextoRegra(String.valueOf(pv.getGl()) + "."
						+ String.valueOf(pv.getCp()));
			}else if(tipoaval==1){
				//pegar do WCAG
				InterfRegrasHardCoded regras = MethodFactRegHardCod.mFRegHardCod("WCAG");
				descricao = regras.getTextoRegra(String.valueOf(pv.getGl()) + "."
						+ String.valueOf(pv.getCp()));
			}else{
				 descricao = TokenLang.getRegra(pv.getIdRegra());
			}
			cell = new PdfPCell(new Paragraph(descricao));
			
			cell.setColspan(3);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			table.addCell(cell);
			// Total Ocorrências
			int size = pv.getLinhas().size();
			String strSize = String.valueOf(size);
			strSize = !strSize.equals("0") ? strSize : "---";
			cell = new PdfPCell(new Paragraph(strSize));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			table.addCell(cell);
			// Linhas em que ocorre o P.V.
			ArrayList<Integer> arrayLinhas = pv.getLinhas();
			StringBuilder linha = new StringBuilder();
			for (Integer nroLinha : arrayLinhas) {
				// String strLinha = Normalizador.normalizar(nroLinha);
				String strLinha = String.valueOf(nroLinha);
				linha.append(strLinha + "  ");
			}
			cell = new PdfPCell(new Paragraph(linha.toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			table.addCell(cell);
		}
		return table;
	}
}
