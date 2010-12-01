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

package br.org.acessobrasil.silvinha.negocio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.org.acessobrasil.ases.entidade.ModeloSite;
import br.org.acessobrasil.ases.nucleo.InterfNucleos;
import br.org.acessobrasil.ases.nucleo.MethodFactNucleos;
import br.org.acessobrasil.ases.nucleo.adapters.entidade.ArmazenaErroOuAviso;
import br.org.acessobrasil.nucleuSilva.entidade.ArmazenaErroOuAvisoAntigo;
import br.org.acessobrasil.silvinha.entidade.PontoVerificacao;
import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.util.GeradorMapaSite;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha2.util.G_Log;

/**
 * Classe que ira gerar os erros da página. roda pelo menos uma vez para cada
 * página
 * 
 * @author Mariano Aloi Construido em 19/08/2005
 */
public class ProcessarErro2  {

	private static G_Log log = new G_Log("ProcessarErro.log");

	public boolean procErroAtivo;

	

	private long startTime;

	private InterfNucleos nucleox;

	
	/**
	 * Contagem dos Erros por Prioridade
	 */
	private int pri1 = 0, pri2 = 0, pri3 = 0;

	/**
	 * Contagem dos Avisos por Prioridade
	 */
	private int ap1 = 0, ap2 = 0, ap3 = 0;

	// A escolha de prioridades deve alterar a validação de erros, e não a
	// visualização dos mesmos
	// para melhorar a performance
	/**
	 * Definicao de quais prioridades mostrar no relatorio
	 */
	private boolean mostraP1 = true;

	private boolean mostraP2 = true;

	private boolean mostraP3 = true;

	/**
	 * Coleção de Erros de prioridade 1.
	 */
	private HashSet<PontoVerificacao> ptVerif1 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Erros de prioridade 2.
	 */
	private HashSet<PontoVerificacao> ptVerif2 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Erros de prioridade 3.
	 */
	private HashSet<PontoVerificacao> ptVerif3 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Avisos de prioridade 1.
	 */
	private HashSet<PontoVerificacao> avisosP1 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Avisos de prioridade 2.
	 */
	private HashSet<PontoVerificacao> avisosP2 = new HashSet<PontoVerificacao>();

	/**
	 * Coleção de Avisos de prioridade 3.
	 */
	private HashSet<PontoVerificacao> avisosP3 = new HashSet<PontoVerificacao>();

	/**
	 * Conjunto de propriedades a serem avaliadas pelo Processador de Erros.
	 */
	private Properties props;

	/**
	 * Conjunto de Pontos de Verifica&ccedil;&atilde; que ser&atilde;o
	 * utilizados para avalia&ccedil;&atilde;o dos erros no conte&uacute;do das
	 * p&aacute;ginas.
	 */
	private HashMap<Integer, PontoVerificacao> pontosVerificacao;

	/**
	 * Conjunto de Pontos de Verifica&ccedil;&atilde; que ser&atilde;o
	 * utilizados para avalia&ccedil;&atilde;o dos erros no conte&uacute;do das
	 * p&aacute;ginas.
	 */
	private HashMap<Integer, PontoVerificacao> avisosGenericos;



	
	
	private RelatorioDaUrl relatorio;

	private GeradorMapaSite geradorMapaSite;

	public ProcessarErro2() {
	
	}

	/**
	 * Processa os erros da urlString passada como parâmetro
	 * 
	 * @param urlString
	 * @param gerente
	 * @param p_geradorMapaSite
	 */
	
	/**
	 * obs o relatorio ja deve vir com conteudo
	 */
	public ProcessarErro2(RelatorioDaUrl relatorio) {
		
		
		
		this.props = Gerente.getProperties();
		this.relatorio = relatorio;
		

		int tipoAvaliacao = Integer.parseInt(props.getProperty("tipo_avaliacao"));
		// JOptionPane.showMessageDialog(null, "Tipo avaliação: "+
		// Integer.parseInt(props.getProperty("tipo_avaliacao")));
	
		avisosGenericos = PontosDeVerificacaoFactory.getAvisosGenericos(tipoAvaliacao);
		pontosVerificacao = PontosDeVerificacaoFactory.getPontosDeVerificacao(tipoAvaliacao);
		inicializar();
		inicializaMostraPrioridades();
	}

	/**
	 * Roda como um processo
	 */
	public void processa() {
	
	
		
		try {
			setProcErroAtivo(true);
			
			// Busca o conteudo do link na internet e coloca no relatorio;
			
			
			if (!FrameSilvinha.stopAvaliacao) {
				if (relatorio.getConteudo() != null) {
					
					// processa as regras de acessibilidade
					this.parseWAI();
				} else {
					log.addLog("Sem conteudo ");
					// System.out.println("Sem conteudo " + relatorio.getUrl());
					/*
					 * PaginasNaoAnalisadas.relatorios.add(relatorio);
					 * 
					 * PaginasNaoAnalisadas.mensagens .add("Sem Conteúdo");
					 */
					// ListaDeArquivosNaoAvaliados.add(relatorio.getUrl());
				}
				
			}
			
			setProcErroAtivo(false);
		} catch (Exception e) {
			log.addLog("Run " + e.getMessage() + "\n");
		}
	}

	
	
	public void parseWAI() {
		
		// Zera as coisas
		inicializar();
		
		
		try {
			// Marcação retorna os erros
			separarPrioridade(marcacao());
		
		} catch (Exception e) {
		
			log.debug(e.getMessage());
			return;
		}
		
	
		relatorio.setErrosPrioridade1(pri1);
		
		relatorio.setErrosPrioridade2(pri2);
		
		relatorio.setErrosPrioridade3(pri3);
		
		
		
		relatorio.setAvisosPrioridade1(ap1);
		
		relatorio.setAvisosPrioridade2(ap2);
		
		relatorio.setAvisosPrioridade3(ap3);
		System.out.println("O site possui: "+ModeloSite.getAvisos()+" Avisos, "+ModeloSite.getErros()+" Erros, "+ModeloSite.getAvisosP1()+" AvisosP1, "+ModeloSite.getAvisosP2()+" AvisosP2, "+ModeloSite.getAvisosp3()+" AvisosP3, "+ModeloSite.getErrosp1()+" ErrosP1, "+ModeloSite.getErrosp2()+" ErrosP2, "+ModeloSite.getErrosp3()+" ErrosP2");
	
		
		/*
		 * Não será necessário gravar no banco
		 */
		//gravaNoBanco();
		
		relatorio.setListaErrosP1(ptVerif1);
		relatorio.setListaErrosP2(ptVerif2);
		relatorio.setListaErrosP3(ptVerif3);
		relatorio.setListaAvisosP1(avisosP1);
		relatorio.setListaAvisosP2(avisosP2);
		relatorio.setListaAvisosP3(avisosP3);
		relatorio.setMostraP1(mostraP1);
		relatorio.setMostraP2(mostraP2);
		relatorio.setMostraP3(mostraP3);
	
	
	
	}

	
	private void inicializaMostraPrioridades() {
		String prioridade1 = props.getProperty("prioridade1");
		String prioridade2 = props.getProperty("prioridade2");
		String prioridade3 = props.getProperty("prioridade3");
		if (prioridade1.equals("false")) {
			mostraP1 = false;
		}
		if (prioridade2.equals("false")) {
			mostraP2 = false;
		}
		if (prioridade3.equals("false")) {
			mostraP3 = false;
		}
	}

	



	/**
	 * Método utilizado para zerar os contadores.
	 */
	private void inicializar() {

		this.pri1 = 0;
		this.pri2 = 0;
		this.pri3 = 0;
	/*	if (Gerente.getProperties().getProperty("tipo_avaliacao").equals(String.valueOf(PainelAvaliacao.EMAG))) {
			this.ap1 = Token.EMAG_AV_GEN_P1;
			this.ap2 = Token.EMAG_AV_GEN_P2;
			this.ap3 = Token.EMAG_AV_GEN_P3;
		} else if (Gerente.getProperties().getProperty("tipo_avaliacao").equals(String.valueOf(PainelAvaliacao.WCAG))) {
			this.ap1 = Token.WCAG_AV_GEN_P1;
			this.ap2 = Token.WCAG_AV_GEN_P2;
			this.ap3 = Token.WCAG_AV_GEN_P3;
		}//*/
		this.ptVerif1 = new HashSet<PontoVerificacao>();
		this.ptVerif2 = new HashSet<PontoVerificacao>();
		this.ptVerif3 = new HashSet<PontoVerificacao>();
		this.avisosP1 = new HashSet<PontoVerificacao>();
		this.avisosP2 = new HashSet<PontoVerificacao>();
		this.avisosP3 = new HashSet<PontoVerificacao>();
	}

	/**
	 * Separa as prioridades do arquivo enviado
	 * 
	 * @param po
	 */
	private void separarPrioridade(ArrayList<PontoVerificacao> po) {

		for (PontoVerificacao conjErros : po) {
			int prioridade = conjErros.getPrioridade();
			char exigencia = conjErros.getExigencia();
			switch (prioridade) {
			case 1: {
				switch (exigencia) {
				case 'e': // ERRO
					ptVerif1.add(conjErros);
					break;
				default: // AVISO OU GENERICO
					avisosP1.add(conjErros);
					break;
				}
			}
				break;
			case 2: {
				switch (exigencia) {
				case 'e': // ERRO
					ptVerif2.add(conjErros);
					break;
				default: // AVISO OU GENERICO
					avisosP2.add(conjErros);
					break;
				}
			}
				break;
			case 3: {
				switch (exigencia) {
				case 'e': // ERRO
					ptVerif3.add(conjErros);
					break;
				default: // AVISO OU GENERICO
					avisosP3.add(conjErros);
					break;
				}
			}
				break;
			default:
				break;
			}
		}

		// for (PontoVerificacao conjErros : po) {
		// switch(conjErros.getPrioridade()){
		// case 1:ptVerif1.add(conjErros); break;
		// case 2:ptVerif2.add(conjErros); break;
		// case 3:ptVerif3.add(conjErros); break;
		// default: break;
		// }
		// }
	}

	/**
	 * Principal método para a avaliação do sistema, ele apresenta os erros que
	 * o sistema advem Método que retorna uma instancia de ArrayList<PontoVerificacao>
	 * 
	 * para cada ponto de verificação ele agrupa todos as ocorrencias de erros
	 * neste mesmo ponto adicionando n linhas onde ocorre este erro
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ExceptionMariano
	 */
	private ArrayList<PontoVerificacao> marcacao() throws ClassNotFoundException, SQLException, Exception {

		ArrayList<PontoVerificacao> erros = new ArrayList<PontoVerificacao>();
		String nucleo = "Antigo";
		nucleo = "Estruturado";
		ArrayList<Object> validados = new ArrayList<Object>();

		validados = (ArrayList<Object>) escolheNucleo(nucleo);
	
		PontoVerificacao pv = null;
		ArmazenaErroOuAviso erroOuAviso = null;
		//bancoSite.abreConexaoOuCriaBanco();
		for (Object erro : validados) {
			// modo de compatibilidade
		
			char flagErroOuAviso = ' ';
			if (nucleo.equals("Antigo")) {
				pv = new PontoVerificacao(pontosVerificacao.get(((ArmazenaErroOuAvisoAntigo) erro).getPv3()));
			} else {
				// PARA QUALQUER OUTRO NUCLEO
				/*
				 * pv = PontoVerificacao(nuxleox.getTextoRegra(String
				 * codigo),int idRegra, int prioridade,
				 * 
				 * String pontoVerificacaoReal, final ArrayList<Integer>
				 * nlinhas, final String exige)
				 */
				//3
				erroOuAviso = (ArmazenaErroOuAviso) erro;
				
				String tagCompleta=erroOuAviso.getTagCompleta();
				
				/*
				if(tagCompleta.indexOf(" ")!=-1){
				bancoSite.insertTabelaErro("1",relatorio.getPagIdBanco(), 0, tagCompleta.replaceAll("'",""), erroOuAviso.getLinha() , erroOuAviso.getColuna(), tagCompleta.substring(1,tagCompleta.indexOf(" ")));
				}else if(tagCompleta.indexOf(">")!=-1){
					bancoSite.insertTabelaErro("1",relatorio.getPagIdBanco(), 0, tagCompleta.replaceAll("'",""), erroOuAviso.getLinha() , erroOuAviso.getColuna(), tagCompleta.substring(1,tagCompleta.indexOf(">")));
				}
				if(tagCompleta.indexOf(">")==-1){
					bancoSite.insertTabelaErro("1",relatorio.getPagIdBanco(), 0, tagCompleta.replaceAll("'",""), erroOuAviso.getLinha() , erroOuAviso.getColuna(), tagCompleta);
				}//*/
				
				
				//3
				String codigoRegra = erroOuAviso.getIdTextoRegra();
				//3
				String texto = nucleox.getRegras().getTextoRegra(codigoRegra);
				int idRegra = 0;
				int prioridade = nucleox.getRegras().getPrioridadeRegra(codigoRegra);

				
				
				//2
				ArrayList<Integer> nlinhas = new ArrayList<Integer>();
				nlinhas.add(erroOuAviso.getLinha());
//2
				if (((ArmazenaErroOuAviso) erro).isAviso()) {
					flagErroOuAviso = 'a';
				} else {
					flagErroOuAviso = 'e';
				}
					
			//	2
				pv = new PontoVerificacao(texto, idRegra, prioridade, codigoRegra, nlinhas, String.valueOf(flagErroOuAviso));
				//2
				pv.setWcagEmag(nucleox.getRegras().getCodWcagEmag());
			}
			if (pv != null) {
				int exigencia = pv.getExigencia();
				int prioridade = pv.getPrioridade();
				switch (exigencia) {
				case 'e':
					switch (prioridade) {
					case 1:
						pri1++;
						break;
					case 2:
						pri2++;
						break;
					case 3:
						pri3++;
						break;
					default:
						new Throwable("Erro na contagem de prioridades");
					}
					break;
				case 'a':
					switch (prioridade) {
					case 1:
						ap1++;
						break;
					case 2:
						ap2++;
						break;
					case 3:
						ap3++;
						break;
					default:
						new Throwable("Erro na contagem de prioridades");
					}
					break;
				}
				int linha, coluna, tagLen;
				//2
				String umaTagInteira="";
				if (nucleo.equals("Antigo")) {
					linha = ((ArmazenaErroOuAvisoAntigo) erro).getPosicao().getLinha() + 1;
					coluna = 0;
					tagLen = 0;
					 
				} else {
					linha = erroOuAviso.getLinha();
					coluna = erroOuAviso.getColuna();
					//2
					tagLen = erroOuAviso.getTagCompleta().length();
					umaTagInteira = erroOuAviso.getTagCompleta();
				}
				/*
				 * Agrupa os pontos
				 */
				if (erros.contains(pv)) {
					/*
					 * o ponto já existe, agrupa a ocorrencia nele
					 */
					//
					erros.get(erros.indexOf(pv)).getLinhas().add(linha);
					erros.get(erros.indexOf(pv)).getColunas().add(coluna);
					erros.get(erros.indexOf(pv)).getTagLength().add(tagLen);
					//ver se não ocupa muita memoria
					erros.get(erros.indexOf(pv)).getTagInteira().add(umaTagInteira);
					if (flagErroOuAviso == 'e') {
						erros.get(erros.indexOf(pv)).getAvisoOuErro().add("e");
					} else {
						erros.get(erros.indexOf(pv)).getAvisoOuErro().add("a");
					}
				} else {
					/*
					 * O ponto não existe, criamos um novo
					 */
					ArrayList<Integer> linhas = new ArrayList<Integer>();
					linhas.add(linha);
					ArrayList<Integer> colunas = new ArrayList<Integer>();
					colunas.add(linha);
					ArrayList<Integer> tagLengths = new ArrayList<Integer>();
					tagLengths.add(linha);
//					ver se não ocupa muita memoria
					ArrayList<String> tagInteira = new ArrayList<String>();
					tagInteira.add(umaTagInteira);
					ArrayList<String> avOuErr = new ArrayList<String>();
					if (flagErroOuAviso == 'e') {
						avOuErr.add("e");
					} else {
						avOuErr.add("a");
					}
					pv.setLinhas(linhas);
					pv.setColunas(colunas);
					pv.setTagLength(tagLengths);
					pv.setAvisoOuErro(avOuErr);
					pv.setTagInteira(tagInteira);
					erros.add(pv);
				}
			}
		}

		// ADICAO DOS AVISOS GENERICOS (QUE NÃO CONTAM OCORRÊNCIA EM LINHAS DO
		// CÓDIGO)
		//erros.addAll(avisosGenericos.values());
		//testArrayErros(erros);
		return erros;
	}

	/**
	 * Imprime os erros para testar
	 * @param erros
	 */
	private void testArrayErros(ArrayList<PontoVerificacao> erros){
		//System.out.print("pri1="+pri1+"\n");
		//System.out.print("pri2="+pri2+"\n");
		//System.out.print("pri3="+pri3+"\n");
		//System.out.print("ap1="+ap1+"\n");
		//System.out.print("ap2="+ap2+"\n");
		//System.out.print("ap3="+ap3+"\n");
		
		for(int i=0;i<erros.size();i++){
			
			//System.out.print("Regra: "+erros.get(i).getGl()+"."+erros.get(i).getCp()+"\n");
			//System.out.print("Linhas:");
			for(int j=0;j<erros.get(i).getLinhas().size();j++){
				//System.out.print(" "+erros.get(i).getLinhas().get(j));
			}
			//System.out.print("\n");
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws Exception
	 * @deprecated
	 */
	private ArrayList<PontoVerificacao> marcacaobkp() throws ClassNotFoundException, SQLException, Exception {

		ArrayList<PontoVerificacao> erros = new ArrayList<PontoVerificacao>();

		ArrayList<ArmazenaErroOuAvisoAntigo> validados = new ArrayList<ArmazenaErroOuAvisoAntigo>();

		validados = (ArrayList<ArmazenaErroOuAvisoAntigo>) escolheNucleo("Antigo");

		PontoVerificacao pv = null;
		for (ArmazenaErroOuAvisoAntigo erro : validados) {
			// pv = new PontoVerificacao(pontosVerificacao.get(erro.getPv3()));
			if (pv != null) {
				int exigencia = pv.getExigencia();
				int prioridade = pv.getPrioridade();
				switch (exigencia) {
				case 'e':
					switch (prioridade) {
					case 1:
						pri1++;
						break;
					case 2:
						pri2++;
						break;
					case 3:
						pri3++;
						break;
					default:
						new Throwable("Erro na contagem de prioridades");
					}
					break;
				case 'a':
					switch (prioridade) {
					case 1:
						ap1++;
						break;
					case 2:
						ap2++;
						break;
					case 3:
						ap3++;
						break;
					default:
						new Throwable("Erro na contagem de prioridades");
					}
					break;
				}

				int linha = erro.getPosicao().getLinha() + 1;
				if (erros.contains(pv)) {
					erros.get(erros.indexOf(pv)).getLinhas().add(linha);
				} else {
					ArrayList<Integer> linhas = new ArrayList<Integer>();
					linhas.add(linha);
					pv.setLinhas(linhas);
					erros.add(pv);
				}
			}
		}

		// ADICAO DOS AVISOS GENERICOS (QUE NÃO CONTAM OCORRÊNCIA EM LINHAS DO
		// CÓDIGO)
		erros.addAll(avisosGenericos.values());

		return erros;
	}

	private Object escolheNucleo(String nucleo) throws ClassNotFoundException, SQLException, Exception {
		nucleox = MethodFactNucleos.mFNucleos(nucleo);
		if (nucleo.equals("Antigo")) {
			return nucleox.getValidados(relatorio, props);
		} else {
			nucleox.setCodHTML(relatorio.getConteudo().toString());
			if(props.getProperty("tipo_avaliacao").equals("1")){
				nucleox.setWCAGEMAG(InterfNucleos.WCAG);
			}else{
				nucleox.setWCAGEMAG(InterfNucleos.EMAG);
			}
			nucleox.avalia();
			// precisa atender o preenchimento da interface validado e este
			// estar num array
			return nucleox.getErroOuAviso();
		}

	}

	public boolean isProcErroAtivo() {
		return procErroAtivo;
	}

	public void setProcErroAtivo(boolean procErroAtivo) {
		this.procErroAtivo = procErroAtivo;
	}

	public void setStartTime() {
		Date startDate = new Date();
		startTime = startDate.getTime();
	}

	public long getStartTime() {
		return startTime;
	}
	public static byte[] convert(byte[] data, String srcEncoding, String targetEncoding) {
	      // First, decode the data using the source encoding.
	      // The String constructor does this (Javadoc).
	      // Next, encode the data using the target encoding.
			      // The String.getBytes() method does this.
		byte[] result=null;
		try {
			String str = new String(data, srcEncoding);
			  result = str.getBytes(targetEncoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      return result;
	  }
}
