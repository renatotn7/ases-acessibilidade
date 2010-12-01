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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.tidy.Tidy;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import br.org.acessobrasil.silvinha2.util.G_File;

public class DTD implements ErrorHandler {

	HashMap<String, Tag> arrTag = new HashMap<String, Tag>();

	HashMap<String, ArrayList<Atributo>> arrAtt = new HashMap<String, ArrayList<Atributo>>();

	public static void main(String[] args) {

		DTD validator = new DTD();
		validator.loadDTD();
		// validator.avalia("http://192.168.0.18/testeDTD/tecassistiva.html");
	}

	/**
	 * Enviar o Fullpath do arquivo que já foi baixado pelo núcleo
	 * 
	 * @param fullPath
	 */
	public void avalia(String fullPath) {
		URL as;
		try {
			as = new URL(fullPath);
			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			tidy.setOnlyErrors(true);
			//tidy.parse(as.openStream(), System.out);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void error(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("Warning: ");
		printInfo(exception);
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("Error: ");
		printInfo(exception);
	}

	public void warning(SAXParseException exception) throws SAXException {
		// TODO Auto-generated method stub
		//System.out.println("Fattal error: ");
		printInfo(exception);
	}

	private void printInfo(SAXParseException e) {
		//System.out.println("   Public ID: " + e.getPublicId());
		//System.out.println("   System ID: " + e.getSystemId());
		//System.out.println("   Line number: " + e.getLineNumber());
		//System.out.println("   Column number: " + e.getColumnNumber());
		//System.out.println("   Message: " + e.getMessage());
		//System.out.println("   getLocalizedMessage: " + e.getLocalizedMessage());
	}

	public void loadDTD() {
		HashMap<String, ArrayList<String>> arrEntity = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<Atributo>> arrEntityAtt = new HashMap<String, ArrayList<Atributo>>();

		G_File dtd = new G_File("C:\\Temp\\testeDTD\\html4.01strict_1.dtd");
		String srcDtd = dtd.read();
		Pattern pat = Pattern.compile("<!--.*?-->", Pattern.DOTALL);
		Matcher matcher = pat.matcher(srcDtd);
		srcDtd = matcher.replaceAll("");
		Pattern pat2 = Pattern.compile("--.*?--", Pattern.DOTALL);
		matcher = pat2.matcher(srcDtd);
		srcDtd = matcher.replaceAll("");
		pat2 = Pattern.compile("\\s+");
		matcher = pat2.matcher(srcDtd);
		srcDtd = matcher.replaceAll(" ");
		//System.out.print(srcDtd);
		/*
		 * Loop pelos dados
		 */
		pat = Pattern.compile("<.*?>", Pattern.DOTALL);
		matcher = pat.matcher(srcDtd);
		while (matcher.find()) {
			String tagEleEntOuAtt = matcher.group();
			if (tagEleEntOuAtt.startsWith("<!ELEMENT")) {
				trataElement(tagEleEntOuAtt, arrEntity, arrEntityAtt);
			} else if (tagEleEntOuAtt.startsWith("<!ENTITY")) {
				trataEntity(tagEleEntOuAtt, arrEntityAtt, arrEntity);
			} else if (tagEleEntOuAtt.startsWith("<!ATTLIST")) {
				trataAtributos(tagEleEntOuAtt, arrEntity, arrEntityAtt);
			} else {
				//System.out.print("\n*************************************\n");
				//System.out.print("Erro: tag não identificada!\n" + tagEleEntOuAtt);
				//System.out.print("\n*************************************\n");
			}
		}

		atribuiPermitidas(arrEntity);

		// printArrEntity(arrEntity);

		// printArrEntityAtt(arrEntityAtt);

		printArrElement();
	}

	private void printArrElement() {
		//System.out.print("\n************************************************\n");
		//System.out.print("Imprimindo Elementos:\n");
		//System.out.print("************************************************\n");
		Iterator iter = arrTag.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Tag valores = arrTag.get(key);
			//System.out.print("key=" + key + "\n");
			//System.out.print("\ttagsPermitidas:");
			HashSet<Tag> tags = valores.getTagsPermitidasDentro();
			for (Tag tag : tags) {
				//System.out.print("'" + tag.getNome() + "',");
			}
			//System.out.print("\n");
		}
	}

	private void printArrEntity(HashMap<String, ArrayList<String>> arrEntity) {
		//System.out.print("\n************************************************\n");
		//System.out.print("Imprimindo Entity:\n");
		//System.out.print("************************************************\n");
		Iterator iter = arrEntity.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			ArrayList<String> valores = arrEntity.get(key);
			//System.out.print("key=" + key + "\n");
			//System.out.print("\tvalores:");
			for (int j = 0; j < valores.size(); j++) {
				//System.out.print("'" + valores.get(j) + "',");
			}
			//System.out.print("\n");
		}
	}

	private void printArrEntityAtt(HashMap<String, ArrayList<Atributo>> arrEntityAtt) {
		//System.out.print("\n************************************************\n");
		//System.out.print("Imprimindo Atributos e valores permitidos:\n");
		//System.out.print("************************************************\n");
		Iterator iter = arrEntityAtt.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			ArrayList<Atributo> valores = arrEntityAtt.get(key);
			//System.out.print("key=" + key + "\n");
			for (int i = 0; i < valores.size(); i++) {
				//System.out.print("\tAtt: " + valores.get(i).getNome() + "\n");
				//System.out.print("\tvalores:");
				for (String vv : valores.get(i).getValoresPermitidos()) {
					//System.out.print(vv + ",");
				}
				//System.out.print("\n");
			}
		}
	}

	/**
	 * Trata o Element do DTD
	 * 
	 * @param tagEleEntOuAtt
	 * @param arrEntityAtt
	 * @param arrEntity
	 */
	private void trataElement(String tagEleEntOuAtt, HashMap<String, ArrayList<String>> arrEntity, HashMap<String, ArrayList<Atributo>> arrEntityAtt) {
		/*
		 * Divide a tag do DTD pelos espaços
		 */
		boolean printOut = false;
		String arr[] = tagEleEntOuAtt.split(" ");
		String nomeDaTag = arr[1];
		/*
		 * Verifica se a tag aponta para mais de um elemento
		 */
		if (nomeDaTag.startsWith("(") && nomeDaTag.endsWith("")) {
			// recursão, mais de uma tag
			String iniTag = arr[0];
			String fimTag = arr[2];
			for (int i = 3; i < arr.length; i++) {
				fimTag += " " + arr[i];
			}
			String nomes[] = nomeDaTag.replaceAll("(\\(|\\))", "").split("\\|");

			for (int i = 0; i < nomes.length; i++) {
				String recTag = iniTag + " " + nomes[i] + " " + fimTag;
				trataElement(recTag, arrEntity, arrEntityAtt);
			}
			return;
		}
		/*
		 * Verifica se o nome da tag aponta para um Entity
		 */
		if (nomeDaTag.startsWith("%") && nomeDaTag.endsWith(";")) {
			// Recursão mais de um elemento
			String tagKey = nomeDaTag.replace("%", "").replace(";", "");
			if (arrEntity.containsKey(tagKey)) {
				String iniTag = arr[0];
				String fimTag = arr[2];
				for (int i = 3; i < arr.length; i++) {
					fimTag += " " + arr[i];
				}
				for (int i = 0; i < arrEntity.get(tagKey).size(); i++) {
					String recTag = iniTag + " " + arrEntity.get(tagKey).get(i) + " " + fimTag;
					trataElement(recTag, arrEntity, arrEntityAtt);
				}
			} else {
				if (printOut){
					//System.out.print("Erro entity não encontrado");
				}
			}
			return;
		}

		/*
		 * Atribuir os valores
		 */
		Tag cTag;
		if (!arrTag.containsKey(nomeDaTag)) {
			cTag = new Tag(nomeDaTag);
			arrTag.put(nomeDaTag, cTag);
		} else {
			cTag = arrTag.get(nomeDaTag);
		}
		/*
		 * tratar os valores
		 */
		int indiceComecoValores = 0;
		if (arr.length > 3) {
			if (arr[2].equals("-") || arr[2].equals("O")) {
				/*
				 * O que são estes símbolos, quantificadores?
				 */
				indiceComecoValores = 4;
			} else {
				indiceComecoValores = 3;
			}
		}
		if (printOut)
			//System.out.println("Tag=" + nomeDaTag);
		if (indiceComecoValores != 0) {
			String valor = "";
			for (int i = indiceComecoValores; i < arr.length; i++) {
				valor += arr[i];
			}
			cTag.setStrValueDtd(valor);
		}
	}

	/**
	 * Usado para debug
	 * @param inform
	 * @param imprime
	 */
	private void printInfo(String inform,boolean imprime){
		if(imprime){
			//System.out.print(inform);
		}
	}
	/**
	 * Atribui as tags listadas no myKey na tag passada como parametro
	 * @param cTag
	 * @param myKey
	 * @param arrEntity
	 */
	private void atribuiPermitidas(Tag cTag,String myKey,HashMap<String, ArrayList<String>> arrEntity){
		boolean printOut=true;
		myKey = myKey.substring(1, myKey.length() - 1);
		printInfo("\tmyKey=" + myKey,printOut);
		if (arrEntity.containsKey(myKey)) {
			/*
			 * Pegar essas tags
			 */
			ArrayList<String> valores = arrEntity.get(myKey);
			printInfo("\n\t\tCopiando:",printOut);
			for (int j = 0; j < valores.size(); j++) {
				String valor=valores.get(j);
				printInfo("'" + valor + "'",printOut);
				if (arrTag.containsKey(valor)) {
					cTag.getTagsPermitidasDentro().add(arrTag.get(valores.get(j)));
				} else {
					if (valor.indexOf("|") == -1 && valor.indexOf(",") == -1) {
						if (valor.startsWith("%") && valor.endsWith(";")) {
							atribuiPermitidas(cTag, valor ,arrEntity);
						}
					}
					printInfo(" 404!",printOut);
				}
				printInfo(", ",printOut);
			}
			printInfo("\n",printOut);
		} else {
			printInfo(" não encontrada!",printOut);
		}
		printInfo("\n",printOut);
	}
	/**
	 * Cruzar os valores das tags para criar as hierarquias
	 */
	private void atribuiPermitidas(HashMap<String, ArrayList<String>> arrEntity) {
		boolean printOut = true;
		Iterator iter = arrTag.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Tag cTag = arrTag.get(key);

			String valor = cTag.getStrValueDtd();
			if (valor.startsWith("(")) {
				valor = getEntreParenteses(valor, 0);
				if (valor.indexOf("|") == -1 && valor.indexOf(",") == -1) {
					if (valor.startsWith("%") && valor.endsWith(";")) {
						atribuiPermitidas(cTag, valor ,arrEntity);
					}
				}else{
					/*
					 * Possui '|' ou ','
					 */
					printInfo("\tvals=" + valor,printOut);
				}
			}
		}
	}

	/**
	 * Retorna o que estiver entre parenteses Ex: para "(a(b))cdf" o retorno é
	 * "a(b)"
	 * 
	 * @param source
	 * @param pos
	 *            posição do parenteses que se quer retornar o conteúdo
	 * @return
	 */
	private String getEntreParenteses(String source, int pos) {
		int parenteses = 1;
		int fim = 0;
		for (int i = 1 + pos; i < source.length(); i++) {
			if (source.charAt(i) == '(') {
				parenteses++;
			} else if (source.charAt(i) == ')') {
				parenteses--;
			}
			if (parenteses == 0) {
				fim = i;
				break;
			}
		}
		if (pos + 1 < fim) {
			return source.substring(pos + 1, fim);
		} else {
			return "";
		}
	}

	/**
	 * Trata os atributos do DTD
	 * 
	 * @param tagEleEntOuAtt
	 * @param arrEntityAtt
	 * @param arrEntity
	 */
	private void trataAtributos(String tagEleEntOuAtt, HashMap<String, ArrayList<String>> arrEntity, HashMap<String, ArrayList<Atributo>> arrEntityAtt) {
		/*
		 * Divide a tag do DTD pelos espaços
		 */
		boolean printOut = false;
		String arr[] = tagEleEntOuAtt.split(" ");
		String nomeDaTag = arr[1];
		/*
		 * Verifica se a tag aponta para mais de um elemento
		 */
		if (nomeDaTag.startsWith("(") && nomeDaTag.endsWith("")) {
			// recursão, mais de uma tag
			String iniTag = arr[0];
			String fimTag = arr[2];
			for (int i = 3; i < arr.length; i++) {
				fimTag += " " + arr[i];
			}
			String nomes[] = nomeDaTag.replaceAll("(\\(|\\))", "").split("\\|");

			for (int i = 0; i < nomes.length; i++) {
				String recTag = iniTag + " " + nomes[i] + " " + fimTag;
				trataAtributos(recTag, arrEntity, arrEntityAtt);
			}
			return;
		}
		/*
		 * Verifica se o nome da tag aponta para um Entity
		 */
		if (nomeDaTag.startsWith("%") && nomeDaTag.endsWith(";")) {
			// Recursão mais de um elemento
			String tagKey = nomeDaTag.replace("%", "").replace(";", "");
			if (arrEntity.containsKey(tagKey)) {
				String iniTag = arr[0];
				String fimTag = arr[2];
				for (int i = 3; i < arr.length; i++) {
					fimTag += " " + arr[i];
				}
				for (int i = 0; i < arrEntity.get(tagKey).size(); i++) {
					String recTag = iniTag + " " + arrEntity.get(tagKey).get(i) + " " + fimTag;
					trataAtributos(recTag, arrEntity, arrEntityAtt);
				}
			} else {
				if (printOut){
					//System.out.print("Erro entity não encontrado");
				}
			}
			return;
		}
		if (printOut)
			//System.out.print("\nAtributos da Tag '" + nomeDaTag + "':\n");

		if (!arrTag.containsKey(nomeDaTag)) {
			arrTag.put(nomeDaTag, new Tag(nomeDaTag));
		}
		Tag tag = arrTag.get(nomeDaTag);
		/*
		 * Loop pelos atributos
		 */
		for (int i = 2; i < arr.length; i++) {
			String atKey = "";
			/*
			 * Verifica se aponta para um outro local
			 */
			if (arr[i].startsWith("%") && arr[i].endsWith(";")) {
				atKey = arr[i].replace("%", "").replace(";", "");
				if (arrEntityAtt.containsKey(atKey)) {
					// está armazenado, colocar eles nesta tag
					tag.getAtributosPermitidos().addAll(arrEntityAtt.get(atKey));
				} else {
					// Atributo não armazenado
					if (printOut){
						//System.out.print("Erro: atributo '" + atKey + "' não armazenado.\n");
					}
				}
			} else {
				// atributo não tratado
				if (i + 2 <= arr.length) {
					String atNome = arr[i++];
					String valores = arr[i++];
					String requerido = arr[i];
					Atributo att = new Atributo(atNome);
					if (valores.startsWith("%") && valores.endsWith(";")) {
						// pegar os valores do entity
						String entKey = valores.replace("%", "").replace(";", "");
						if (arrEntity.containsKey(entKey)) {
							att.getValoresPermitidos().addAll(arrEntity.get(entKey));
						} else {
							// valores não encontrados
							if (printOut){
								//System.out.print("Erro: Entity não encontrado '" + entKey + "'\n");
							}
						}

					} else if (valores.startsWith("(")) {
						/*
						 * possui parenteses tratar os valores
						 */
						valores = valores.replaceAll("(\\)|\\()", "");
						String arrVals[] = valores.split("\\|");
						for (int j = 0; j < arrVals.length; j++) {
							att.getValoresPermitidos().add(arrVals[j]);
						}
					} else {
						att.getValoresPermitidos().add(valores);
					}
					if (requerido.equals("#REQUIRED")) {
						att.setRequired(true);
					} else {
						att.setRequired(false);
					}
					tag.getAtributosPermitidos().add(att);
				}
			}

			/*
			 * 
			 * arrTag.get(nomeDaTag).getAtributosPermitidos().addAll();
			 */
		}
	}

	/**
	 * Trata os Entity do DTD
	 * 
	 * @param tagEleEntOuAtt
	 * @param arrEntityAtt
	 * @param arrEntity
	 */
	private void trataEntity(String tagEleEntOuAtt, HashMap<String, ArrayList<Atributo>> arrEntityAtt, HashMap<String, ArrayList<String>> arrEntity) {
		boolean printOut = false;
		boolean isAtt = false;
		int i;
		/*
		 * Divide a tag pelos espaços
		 */
		String att[] = tagEleEntOuAtt.split(" ");

		String entityName = att[2];
		if (printOut)
			//System.out.print("\nEntity '" + entityName + "':\n");
		if (entityName.equals("i18n")) {
			isAtt = false;
		}

		String entreAspas = getEntreAspas(tagEleEntOuAtt);
		if (entreAspas.indexOf("#") != -1) {
			if (entreAspas.indexOf("#PCDATA") != entreAspas.indexOf("#")) {
				// é um atributo
				isAtt = true;
			}
		}
		String valores[] = entreAspas.split(" ");
		for (i = 0; i < valores.length; i++) {
			String vKey = valores[i].replace(";", "").replace("%", "");
			if (arrEntityAtt.containsKey(vKey)) {
				/*
				 * é um atributo resgatar or valores e colocá-los no array de
				 * atributos deste entity
				 */
				if (!arrEntityAtt.containsKey(entityName)) {
					arrEntityAtt.put(entityName, new ArrayList<Atributo>());
				}
				arrEntityAtt.get(entityName).addAll(arrEntityAtt.get(vKey));
				isAtt = true;
			} else {
				if (isAtt) {
					/*
					 * Verificar se já existe uma lista com estes atributos
					 */
					if (!arrEntityAtt.containsKey(entityName)) {
						arrEntityAtt.put(entityName, new ArrayList<Atributo>());
					}
					Atributo atributo = new Atributo(valores[i]);
					/*
					 * Verificar se possui parenteses
					 */
					if (valores[i + 1].indexOf("(") == 0) {
						// possui parenteses, concatenar até o fecha
						// parenteses
						int parentesesCount = 0;
						String strParenteses = "";
						for (int j = i + 1; j < valores.length; j++) {
							strParenteses += valores[j];
							if (strParenteses.indexOf("(") != -1) {
								parentesesCount++;
							}
							if (strParenteses.indexOf(")") != -1) {
								parentesesCount--;
							}
							if (parentesesCount == 0) {
								i = j - 1;
								break;
							}
						}
						strParenteses = strParenteses.replaceAll("(\\)|\\()", "");
						String arrV[] = strParenteses.split("\\|");
						for (int j = 0; j < arrV.length; j++) {
							atributo.getValoresPermitidos().add(arrV[j]);
						}

					} else {
						String valor = valores[i + 1];
						if (valor.startsWith("%")) {
							/*
							 * Procurar nos entities
							 */
							String vEntKey = valor.replace("%", "").replace(";", "");
							if (arrEntity.containsKey(vEntKey)) {
								atributo.getValoresPermitidos().addAll(arrEntity.get(vEntKey));
							}
						} else {
							atributo.getValoresPermitidos().add(valor);
						}

					}
					/*
					 * Verificar se é required ou não
					 */
					if (valores[i + 2].equals("#REQUIRED")) {
						atributo.setRequired(true);
					} else {
						atributo.setRequired(false);
					}
					i = i + 2;
					arrEntityAtt.get(entityName).add(atributo);
				} else {
					/*
					 * Lista de valores
					 */
					String valor = valores[i];
					if (printOut)
						//System.out.print(valor + "\n");
					if (!arrEntity.containsKey(entityName)) {
						arrEntity.put(entityName, new ArrayList<String>());
					}
					valor = valor.replaceAll("(\\(|\\))", "");
					String valval[] = valor.split("\\|");
					for (int k = 0; k < valval.length; k++) {
						arrEntity.get(entityName).add(valval[k].trim());
					}
				}
			}
		}
		if (printOut){
			//System.out.print("isAtt=" + isAtt + "\n");
		}
	}

	private String getEntreAspas(String tag) {
		Pattern pat = Pattern.compile("\"(.*?)\"", Pattern.DOTALL);
		Matcher matcher = pat.matcher(tag);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

}

class Tag {
	private String nome;

	private String strValueDtd;

	HashSet<Tag> tagsPermitidasDentro;

	HashSet<Atributo> atributosPermitidos;

	public Tag(String nomeDaTag) {
		atributosPermitidos = new HashSet<Atributo>();
		tagsPermitidasDentro = new HashSet<Tag>();
		nome = nomeDaTag;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the tagsPermitidasDentro
	 */
	public HashSet<Tag> getTagsPermitidasDentro() {
		return tagsPermitidasDentro;
	}

	/**
	 * @param tagsPermitidasDentro
	 *            the tagsPermitidasDentro to set
	 */
	public void setTagsPermitidasDentro(HashSet<Tag> tagsPermitidasDentro) {
		this.tagsPermitidasDentro = tagsPermitidasDentro;
	}

	/**
	 * @return the atributosPermitidos
	 */
	public HashSet<Atributo> getAtributosPermitidos() {
		return atributosPermitidos;
	}

	/**
	 * @param atributosPermitidos
	 *            the atributosPermitidos to set
	 */
	public void setAtributosPermitidos(HashSet<Atributo> atributosPermitidos) {
		this.atributosPermitidos = atributosPermitidos;
	}

	public String getStrValueDtd() {
		return strValueDtd;
	}

	public void setStrValueDtd(String strValueDtd) {
		this.strValueDtd = strValueDtd;
	}
}

class Atributo {
	private String nome;

	private String type;

	private HashSet<String> valoresPermitidos;

	private boolean required;

	public Atributo(String atNome) {
		nome = atNome;
		valoresPermitidos = new HashSet<String>();
		required = false;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the valoresPermitidos
	 */
	public HashSet<String> getValoresPermitidos() {
		return valoresPermitidos;
	}

	/**
	 * @param valoresPermitidos
	 *            the valoresPermitidos to set
	 */
	public void setValoresPermitidos(HashSet<String> valoresPermitidos) {
		this.valoresPermitidos = valoresPermitidos;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
}
