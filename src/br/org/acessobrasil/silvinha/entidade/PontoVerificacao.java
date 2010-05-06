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

package br.org.acessobrasil.silvinha.entidade;

import java.util.ArrayList;


/**
 * Representação de um ponto de verificação.
 * Um ponto de verificação é a abstração de uma recomendação da WCAG e/ou eGov,
 * que recebe várias linhas e tem prioridades.<br>
 * Revisão de 21/12/2005: O atributo 'exigencia' define se a recomendação é
 * obrigatória (erro) ou não (aviso).
 * @author Mariano Aloi, 17/10/2005.
 * @version 1.1, 21/12/2005
 */
public final class PontoVerificacao {
    /**
     * Número de identificação da recomendação na base de dados.
     */
    private int idRegra;

    /**
     * Guarda o código do documento 
     * 1 = WCAG
     * 2 = EMAG
     */
    private int wcag_emag;
    
    /**
     * Exigência da Recomendação.
     */
    private char exigencia;

    /**
     * Guia (Guideline) da recomendação.
     */
    private int gl;

    /**
     * Pontos de verificação (checkpoint) da recomendação.
     */
    private int cp;

    /**
     * Prioridade da recomendação.
     */
    private int prioridade;

    /**
     * Linhas onde foram encontrados erros.
     */
    private ArrayList<Integer> linhas;
    
    /**
     * Colunas onde foram encontrados os erros
     */
    private ArrayList<Integer> colunas;

    /**
     * Tamanho da tag onde foi encontrado o erro
     */
    private ArrayList<Integer> tagLength;
    
    /**
     * Nome da tag onde foi encontrado o erro<br>
     * Ex.: img<br>
     * verificar peso em memoria
     */
    private ArrayList<String> tagName;
    
    /**
     * Tag onde foi encontrado o erro<br>
     * Ex.: &lt;img src=""> <br>
     * verificar peso em memoria
     */
    private ArrayList<String> tagInteira;
    
    /**
     * Se a ocorrencia foi um aviso ou erro 
     */
    private ArrayList<String> avisoOuErro;
    
    /**
     * Construtor de PontoVerificacao.
     */
    public PontoVerificacao() {
       	
    	this.idRegra = 0;
        this.prioridade = 0;
        this.gl = 0;
        this.cp = 0;
        this.exigencia = 'g';
        this.linhas = new ArrayList<Integer>();
    }

    /**
     * Construtor de PontoVerificacao.
     * @param texto Texto explicativo da recomendação.
     * @param guideline Guia (Guideline) da recomendação.
     * @param checkpoint Pontos de verificação (checkpoint) da recomendação.
     * @param nlinhas Linhas onde foram encontrados erros.
     * @param exige Exigência da Recomendação.
     * @deprecated
     */
    
    public PontoVerificacao(final String texto, final int guideline,
        	
    		final int checkpoint, final ArrayList<Integer> nlinhas,
        final String exige) {
     
    	this.gl = guideline;
        this.cp = checkpoint;
        this.linhas = nlinhas;
        this.exigencia = exige.charAt(0);
    }
    
    public PontoVerificacao(final String texto,int idRegra, int prioridade,
    	
    		String pontoVerificacaoReal, final ArrayList<Integer> nlinhas,
        final String exige) {
     
    	this.idRegra = idRegra;
    	this.prioridade= prioridade;
    	this.gl = Integer.parseInt(pontoVerificacaoReal.split("\\.")[0]);
        this.cp = Integer.parseInt(pontoVerificacaoReal.split("\\.")[1]);
        this.linhas = nlinhas;
        this.exigencia = exige.charAt(0);
    }
    
    public PontoVerificacao(PontoVerificacao pv) {
    
    	this.idRegra = pv.getIdRegra();
        this.prioridade = pv.getPrioridade();
        this.gl = pv.getGl();
        this.cp = pv.getCp();
        this.exigencia = pv.getExigencia();
        this.linhas = pv.getLinhas();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.gl + "." + this.cp + " = " + this.prioridade;
    }

    /**
     * @return Retorna o valor de cp.
     */
    public int getCp() {
        return cp;
    }

    /**
     * @param vChkPoint para se colocar cp no campo.
     */
    public void setCp(final int vChkPoint) {
        this.cp = vChkPoint;
    }

    /**
     * @return Retorna o valor de exigencia.
     */
    public char getExigencia() {
        return exigencia;
    }

    /**
     * @param vExige para se colocar exigencia no campo.
     */
    public void setExigencia(final char vExige) {
        this.exigencia = vExige;
    }

    /**
     * @return Retorna o valor de gl.
     */
    public int getGl() {
        return gl;
    }

    /**
     * @param vGuideline para se colocar gl no campo.
     */
    public void setGl(final int vGuideline) {
        this.gl = vGuideline;
    }

    /**
     * @return Retorna o valor de linhas.
     */
    public ArrayList<Integer> getLinhas() {
        return linhas;
    }

    /**
     * @param vLinhas para se colocar linhas no campo.
     */
    public void setLinhas(final ArrayList<Integer> vLinhas) {
        this.linhas = vLinhas;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {   ///HASH CODE!!
        final int num = 1000;
        return (num * this.cp) + this.gl;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        boolean resultado = false;

        if (obj instanceof PontoVerificacao) {
            PontoVerificacao chk = (PontoVerificacao) obj;

            if ((chk.getCp() == this.cp)
                    && (chk.getGl() == this.gl)
                    && (chk.getPrioridade() == this.prioridade)
                    && (chk.getExigencia() == this.exigencia)) {
                resultado = true;
            }
        }

        return resultado;
    }

    /**
     * @return Retorna o valor de prioridade.
     */
    public int getPrioridade() {
        return prioridade;
    }

    /**
     * @param vPrior valor a ser setado em prioridade.
     */
    public void setPrioridade(final int vPrior) {
        this.prioridade = vPrior;
    }

    /**
     * @return Retorna o valor de idRegra.
     */
    public int getIdRegra() {
        return idRegra;
    }

    /**
     * @param vRegraID valor a ser setado em idRegra.
     */
    public void setIdRegra(final int vRegraID) {
        this.idRegra = vRegraID;
    }

    /**
     * Retorna se é wcag ou emag
     * @return
     */
	public int getWcagEmag() {
		return wcag_emag;
	}

	/**
	 * Define se é wcag ou emag
	 * @param wcag_emag
	 */
	public void setWcagEmag(int wcag_emag) {
		this.wcag_emag = wcag_emag;
	}
	
	/**
	 * Retorna as colunas dos erros encontrados
	 * @return
	 */
	public ArrayList<Integer> getColunas() {
		return colunas;
	}
	
	/**
	 * Atribui a lista de colunas referentes ao nº de linhas encontradas
	 * @param cols
	 */
	public void setColunas(ArrayList<Integer> cols) {
		colunas = cols;
	}
	
	/**
	 * Retorna o tamanho da tag
	 * @return
	 */
	public ArrayList<Integer> getTagLength() {
		return tagLength;
	}

	/**
	 * Informa o tamanho da tag
	 * @param tagLength
	 */
	public void setTagLength(ArrayList<Integer> tagLength) {
		this.tagLength = tagLength;
	}

	public ArrayList<String> getAvisoOuErro() {
		return avisoOuErro;
	}

	public void setAvisoOuErro(ArrayList<String> avisoOuErro) {
		this.avisoOuErro = avisoOuErro;
	}

	public ArrayList<String> getTagInteira() {
		return tagInteira;
	}

	public void setTagInteira(ArrayList<String> tagInteira) {
		this.tagInteira = tagInteira;
	}


}
