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

package br.org.acessobrasil.ases.nucleo.adapters.entidade;

import br.org.acessobrasil.ases.entidade.Espaco;
import br.org.acessobrasil.ases.entidade.Posicao;


/**
 * Classe que representa o resumo da avaliação de uma tag HTML.
 * @author Fabio & Renato
 * @version 1.0
 */
public final class ArmazenaErroOuAviso {

	/**
	 * Guarda a regra utilizada
	 */
	private int regraWCAGEMAG;
	
	/**
	 * Guarda o código da regra
	 */
	private String idTextoRegra;
	
	/**
	 * Guarda o número da linha com o erro ou aviso 
	 */
	private int linha=0;
	
	/**
	 * Guarda o número da coluna com o erro ou aviso 
	 */
	private int coluna=0;
	
	/**
	 * true se estiver guardando um erro
	 */
	private boolean erro=false;
	
	/**
	 * true se estiver guardando um aviso
	 */
	private boolean aviso=false;
    /**
     * Número de identificação do Ponto de verificação utilizado.
     */
    private int pv3;

    /**
     * Espaços da indentação da tag HTML avaliada.
     */
    private Espaco espaco;

    /**
     * Posição da tag dentro do arquivo pesquisado.
     */
    private Posicao posicao;

    /**
     * Nome da tag ou atributo procurado.
     */
    private String procurado;

    /**
     * String que representa a tag HTML avaliada.
     */
    private String tagCompleta;

    /**
     * Length da tag para não ter que guardar a string no xml
     */
    
    
    /**
     * Se para a sua avaliação é exigida uma outra tag HTML.
     */
    private boolean tag;

    /**
     * Construtor especifico para tags se designar tags que devem existir dentro
     * do documento.
     * @param elemento Nome da tag ou atributo que se foi utilizado
     * para avaliar a tag HTML.
     * @param ptVf Número de identificação do Ponto de verificação utilizado.
     */
    public ArmazenaErroOuAviso(final String elemento, final int ptVf) {
    	//System.out.print("Validado(elemento='"+elemento+"',ptVf="+ptVf+")\n");
    	this.pv3 = ptVf;
        this.procurado = elemento;
        this.posicao = new Posicao(0, 0);
    }

    /**
     * Construtor de Validado.
     * @param tagHtml Tag HTML avaliada.
     * @param chkPoint Número de identificação do
     * Ponto de verificação utilizado.
     * @param elemento Nome da tag ou atributo que se foi utilizado
     * para avaliar a tag HTML.
     */
    public ArmazenaErroOuAviso(final boolean tagHtml,
            final int chkPoint, final String elemento) {
    	//System.out.print("Validado(tagHtml="+tagHtml+",chkPoint="+chkPoint+",elemento='"+elemento+"')\n");
    	this.pv3 = chkPoint;
        this.tag = tagHtml;
        this.procurado = elemento;
    }

    /**
     * Construtor de Validado.
     */
    public ArmazenaErroOuAviso() { }

    /**
     * Construtor de Validado.
     */
    public ArmazenaErroOuAviso(String codigoRegra,boolean isErro, int wcag_emag,String tagInteira) {
    	if(isErro){
    		erro=true;
    		aviso=false;
    	}else{
    		aviso=true;
    		erro=false;
    	}
    	setIdTextoRegra(codigoRegra);
    	setWCAGEMAG(wcag_emag);
    	setTagCompleta(tagInteira);
    }
     
    /**
     * @return Retorna o valor de espaco.
     */
    public Espaco getEspaco() {
        return espaco;
    }

    /**
     * @param espacos Seta o valor de espaco.
     */
    public void setEspaco(final Espaco espacos) {
        this.espaco = espacos;
    }

    /**
     * @return Retorna o valor de posicao.
     */
    public Posicao getPosicao() {
        return posicao;
    }

    /**
     * @param coords Seta o valor de posicao.
     */
    public void setPosicao(final Posicao coords) {
        this.posicao = coords;
    }

    /**
     * @return Retorna o valor de procurado.
     */
    public String getProcurado() {
        return procurado;
    }

    /**
     * @param element Seta o valor de procurado.
     */
    public void setProcurado(final String element) {
    	//System.out.print("setProcurado(final String element="+element+")\n");
        this.procurado = element;
    }

    /**
     * @return Retorna o valor de pv3.
     */
    public int getPv3() {
        return pv3;
    }

    /**
     * @param chkPoint Seta o valor de pv3.
     */
    public void setPv3(final int chkPoint) {
        this.pv3 = chkPoint;
    }

    /**
     * @return Retorna o valor de tag.
     */
    public boolean isTag() {
        return tag;
    }

    /**
     * @param isRequired Seta o valor de tag.
     */
    public void setTag(final boolean isRequired) {
        this.tag = isRequired;
    }

    /**
     * @return Retorna o valor de tagCompleta.
     */
    public String getTagCompleta() {
        return tagCompleta;
    }

    /**
     * @param tagFull Seta o valor de tagCompleta.
     */
    public void setTagCompleta(final String tagFull) {
    	//System.out.print("setTagCompleta(final String tagFull="+tagFull+")\n");
        this.tagCompleta = tagFull;
    }
    
    /**
     * Informa que está guardando um erro
     */
    public void setErro(){
    	erro=true;
    	aviso=false;
    }
    
    /**
     * Informa que está guardando um aviso
     */
    public void setAviso(){
    	erro=false;
    	aviso=true;
    }
    
    /**
     * Retorna true se estiver guardando um aviso
     */
    public boolean isAviso(){
    	return aviso;
    }
    
    /**
     * Retorna true se estiver guardando um erro
     */
    public boolean isErro(){
    	return erro;
    }

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	/**
	 * Código do identificador da regra
	 * Ex: 2.19 
	 */
	public String getIdTextoRegra() {
		return idTextoRegra;
	}
	
	
	/**
	 * Atribui o código da regra
	 * Ex: 2.19 
	 * setIdTextoRegra(2,19);
	 * @param codigo
	 */
	public void setIdTextoRegra(String codigo) {
		this.idTextoRegra = codigo;
	}

	/**
	 * Retorna a regra utilizada
	 * @return InterfNucleos.EMAG ou InterfNucleos.WCAG
	 */
	public int getWCAGEMAG() {
		return regraWCAGEMAG;
	}

	/**
	 * Guarda a regra utilizada
	 * InterfNucleos.EMAG ou InterfNucleos.WCAG
	 * @param emag_wcag
	 */
	public void setWCAGEMAG(int emag_wcag) {
		this.regraWCAGEMAG = emag_wcag;
	}
    
}
