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

package br.org.acessobrasil.nucleuSilva.util;

import static br.org.acessobrasil.silvinha.entidade.NomeArquivoOuDiretorioLocal.nomeArquivoOuDiretorio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;

/**
 * Classe responsável pela captura do conteúdo de uma página local
 * @author Renato Tomaz Nati
 * Criado em 11/1/2007 
 */
public final class ObterPaginaLocal {
	
	private static Logger log = Logger.getLogger("br.org.acessobrasil.silvinha"); 
	
		
    /**
     * Construtor de PegarPaginaWEB.
     */
    public ObterPaginaLocal(String nomeArquivo) {
    	
    }

    /**
     * Método que extrai o conteúdo de uma página.
     * @param url URL da página a ter seu conteúdo extraído.
     * @return Conteúdo de uma página.
     * @throws IOException Erro ao conectar a página.
     * @deprecated Utilize o método getContent().
     */
    public static StringBuilder pegar(final URL url) throws IOException {
    	//JOptionPane.showMessageDialog(null,"oi1");
    	StringBuilder buf = new StringBuilder();
    	File file = new File(nomeArquivoOuDiretorio); 
    	FileReader reader = new FileReader(file); 
    	BufferedReader leitor = new BufferedReader(reader);
    	

        while (leitor.ready()) {
            buf.append(leitor.readLine() + "\n");
        }

        leitor.close();
   //JOptionPane.showMessageDialog(null,"oi2");
        return buf;
    }

    /**
     * Método que extraí o conteúdo de uma página web.
     * @param relatorio Página que vai ser pesquisada.
     * @throws IOException Erro ao tentar extrair o conteúdo da página html.
     */
    public void getContent(final RelatorioDaUrl relatorio) {
    	final int mb = 1024;
    	
    	try {
    		StringBuilder sbd = null;
    		sbd=new StringBuilder();
    		FileInputStream fis = null;
    		ObjectInputStream ois = null;
    		try
    		{
    			//JOptionPane.showMessageDialog(null,"arq = " + relatorio.getUrl()); 
    			File file = new File(relatorio.getUrl());
    			// JOptionPane.showMessageDialog(null,"fileexist");
    			if (file.exists())
    			{
    				
    				fis = new FileInputStream(file);
    				
    				   				
    				byte[] dados = new byte[mb];
        			int bytesLidos = 0;
        			
        			while ((bytesLidos = fis.read(dados)) > 0) {
        				        				sbd.append(new String(dados, 0, bytesLidos));
        			}
        			
        			fis.close();
        		}
    			
    		}
    		catch (Exception e)
    		{
    			log.error(e);
    		}
    		
    		finally
    		{
    			if (fis != null)
    			{
    				try {
    					fis.close();
    				} 
    				catch (Exception e){}
    			}
    			if (ois != null)
    			{
    				try
    				{
    					ois.close();
    				} 
    				catch (Exception e){}
    			}
    		}
    		
    		if (sbd != null)
    		{
    	
    			relatorio.setConteudo(sbd);
    			     		}
    	} catch (Exception e) {
    		
    		log.error(e.getMessage(), e);
    	}
    	 
    }
    
    
    
    /**
     * Método que retorna o Content-type de uma página web.
     * @param metodo Uma instância de org.apache.commons.httpclient.HttpMethod
     * inicializada pela página.
     * @return O Content-Type da página pesquisada.
     */
    private static String getContentType(final HttpMethod metodo) {
        String type = "";
        Header header = metodo.getResponseHeader("Content-Type");
        if (header != null) {
            type = header.getValue();
        }
        return type;
    }
}

