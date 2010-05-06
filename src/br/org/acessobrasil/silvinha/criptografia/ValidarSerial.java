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

package br.org.acessobrasil.silvinha.criptografia;

/**
 * Classe que irá verificar a veracidade do sistema em relação ao número serial apresentado. 
 * @author Mariano Aloi Construido em 03/09/2005
 */
public class ValidarSerial {
    
    //Faixa de primos varidos para avaliação
    private final int [] PRIMOS = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37,
            41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 };
    //CNPJ apresentado
    private String       CNPJ;
    //Serial apresentado
    private String       serial;

    /**
     * Método principal para avaliação do Número serial apresentado no formulario.
     * 
     * @param CNPJ
     * @param serial
     * @return Se o número serial é valido ou não
     * @throws NumberFormatException
     */
    public boolean validar(String CNPJ, String serial)throws NumberFormatException {
    	
    	this.CNPJ = CNPJ;
        this.serial = serial;

        try{
        if (!verificarCNPJ())  return false; 
        if (!verificarPrimos()) return false;
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    /**
     * Método que retorna boolean, se baseando no fator do ultimos 4 algarismos do numero serial
     * 
     * @return boolean
     */
    private boolean verificarPrimos()throws NumberFormatException{
        int avaliado = 0;
        try {
            //pega os ultimos 4 algarismos do serial e os transforma em um inteiro valido
            avaliado = Integer.valueOf(serial.substring(serial.length() - 4));
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        int alg = 0;
        // avalia se o número é divisivel apenas por 2 numeros primos
        for (int i = 0; i < PRIMOS.length;) {
            int primo = PRIMOS[i];
            if (avaliado % primo == 0) {
                alg++;
                avaliado /= primo;
            }
            else i++;
        }
        if (alg == 2) return true;
        else return false;
    }

    /**
     * Método que retorna se o cnpj é compativel com o serial apresentado
     * 
     * @return se o serial e valido.
     */
    private boolean verificarCNPJ() {
        if ((serial.substring(0, 4) + " " + serial.substring(14, 18))
                .equals(CNPJ.substring(6, 7) + CNPJ.substring(8, 11) + " "
                        + CNPJ.substring(0, 2)
                        + CNPJ.substring(CNPJ.length() - 2))) return true;
        else return false;
    }

}
