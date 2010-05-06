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

package br.org.acessobrasil.ases.regras;

import java.util.ArrayList;
import java.util.HashMap;

import br.org.acessobrasil.silvinha2.mli.RegrasWcag;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;



/**
 * Contem algoritmos para as Regras WCAG 
 * @author Renato Tomaz Nati
 * @since 27/07/2007
 * @version 1.0
 */
public class RegrasHardCodedWcag extends SuperRegrasHardCoded implements InterfRegrasHardCoded{
    
	final int NUMERO_DE_GENERICOS=17;
	private String[] regrasGenericas=new String[NUMERO_DE_GENERICOS];
	
	
	private static HashMap<String,Regra> mapRegra = new HashMap<String,Regra> ();
	
	public RegrasHardCodedWcag(){
		getGenericos();
	}
	
	
	/**
	 * Método que retorna os avisos Genéricos WCAG
	 * 
	 * @author Renato Tomaz Nati
	 * @return String[] formato: gl.cp@regrax@prioridade@***@...
	 * @since 27/07/2007
	 * @version 1.0
	 */
	public String[] getGenericos(){
		
		int cont=0;
		
		//array que conterá respectivamente: gl, cp
		String glCpPrio[]=new String[3];
		
		
		//mapa que conterá respectivamente:  o glcp e a String txtRegra
		String[] regras=new String[NUMERO_DE_GENERICOS];	
		 
		
		//adiciona a primeira regra
		glCpPrio[0]="4";
		glCpPrio[1]="1";
		glCpPrio[2]="1";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_15+"@"+glCpPrio[2];
		
		cont++;
		
		glCpPrio[0]="4";
		glCpPrio[1]="2";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_16+"@"+glCpPrio[2];
		
		
		cont++;
		
		glCpPrio[0]="11";
		glCpPrio[1]="1";
		glCpPrio[2]="2";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_45+"@"+glCpPrio[2];
		
		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="9";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_47+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="11";
		glCpPrio[1]="4";
		glCpPrio[2]="1";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_48+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="12";
		glCpPrio[1]="3";
		glCpPrio[2]="2";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_51+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="2";
		glCpPrio[2]="2";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_54+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="3";
		glCpPrio[2]="2";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_55+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="4";
		glCpPrio[2]="2";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_56+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="5";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_57+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="8";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_60+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="13";
		glCpPrio[1]="10";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_62+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="14";
		glCpPrio[1]="1";
		glCpPrio[2]="1";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_63+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="14";
		glCpPrio[1]="2";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_64+"@"+glCpPrio[2];

		cont++;
		
		glCpPrio[0]="14";
		glCpPrio[1]="3";
		glCpPrio[2]="3";
		regras[cont]=glCpPrio[0]+"."+glCpPrio[1]+"@"+TokenLang.REGRA_65+"@"+glCpPrio[2];
		
cont++;
		
		glCpPrio[0] = "2";
		glCpPrio[1] = "1";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_6
				+ "@" + glCpPrio[2];
		
cont++;
		
	
		
		glCpPrio[0] = "2";
		glCpPrio[1] = "2";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_7
				+ "@" + glCpPrio[2];
		
//		+1 prioridade1 
//		+1 prioridade 3
		regrasGenericas=(String[])regras.clone();
		
		return regras;
		
		
		
	}


	/**
	 * Retorna o texto da regra
	 * Ex: 2.19 
	 * @param codigo código da regra (2.19)
	 * @return Texto da regra
	 */
	public String getTextoRegra(String codigo){
		carregaRegras();
		if(mapRegra==null){
			System.out.println("Sem regras");
			return "";
		}
		if(codigo==null){
			System.out.println("Sem codigo");
			return "";
		}
		if(mapRegra.get(codigo)==null){
			System.out.println("Sem regra " + codigo);
			return "";
		}
		return mapRegra.get(codigo).getTexto();
	}

	/**
	 * Retorna a prioridade da regra
	 * Ex: 2.19 
	 * @param codigo código da regra (2.19)
	 * @return Prioridade da regra 1 2 ou 3
	 */
	public int getPrioridadeRegra(String codigo) {
		carregaRegras();
		Regra regra = mapRegra.get(codigo);
		if(regra==null){
			//System.out.print("Regra " +codigo +" não encontrada");
			return 0;
		}else{
			return regra.getPrioridade();
		}
	}


	@Override
	public int getCodWcagEmag() {
		return 1;
	}
	
	public void carregaRegras(){
		if(mapRegra.size()>0){
			return;
		}
		mapRegra = RegrasWcag.getHashMap(TokenLang.LANG);
	}
	
	/**
	 * Agora utiliza o MLI
	 * @deprecated
	 */
	public void carregaRegrasBkp(){
		//evita o carregamento mais de uma vez
		if(mapRegra.size()>0){
			return;
		}
		
	
		//endereço para consulta: http://www.w3.org/TR/1999/WAI-WEBCONTENT-19990505/	
				
		//Recomendação 1 - Fornecer alternativas ao conteúdo sonoro e visual
		mapRegra.put("1.1", new Regra(1,"Fornecer um equivalente textual a cada elemento não textual (por ex., por meio de “alt” ou “longdesc”, ou como parte do conteúdo do elemento). Isso abrange: imagens, representações gráficas do texto (incluindo símbolos), regiões de mapa de imagem, animações (por ex., GIF animados), applets e objetos programados, arte ASCII, frames, programas interpretáveis, imagens utilizadas como sinalizadores de pontos de enumeração, espaçadores, botões gráficos, sons (reproduzidos ou não com interação do usuário), arquivos de áudio independentes, trilhas áudio de vídeo e trechos de vídeo."));
		mapRegra.put("1.2", new Regra(1,"Fornecer links de texto redundantes relativos a cada região ativa de um mapa de imagem armazenado no servidor."));		
		mapRegra.put("1.3", new Regra(1,"Fornecer uma descrição sonora das informações importantes veiculadas em trechos visuais das apresentações multimídia."));		
		mapRegra.put("1.4", new Regra(1,"Em apresentações multimídia baseadas em tempo (filme ou animação), sincronizar, sempre que possível, os textos alternativos equivalentes (legendas ou descrições sonoras dos trechos visuais) e da apresentação."));		
		mapRegra.put("1.5", new Regra(3,"Não é suficiente fornecer descrições textuais através do atributo ALT dos links encontrados no elemento AREA, criado no mapa de imagens através de uma figura geométrica. Para assegurar que qualquer pessoa possa navegar no seu sítio, ainda é necessário colocar links redundantes em formato texto."));
		
		//Recomendação 2 - Não recorrer apenas à cor
		mapRegra.put("2.1", new Regra(1,"Assegure a acessibilidade de objetos programados, tais como programas interpretáveis e applets, garantindo que a resposta a eventos seja independente do dispositivo de entrada e que qualquer elemento dotado de interface própria possa funcionar com qualquer leitor de tela ou navegador que o usuário utilize. Evite colocar scripts que estejam vinculados a links. Se isso não for possível, forneça informações equivalentes em uma página alternativa acessível."));								
		//2.2:prioridade 2 para imagens e prioridade 3 para textos
		mapRegra.put("2.2", new Regra(2,"Assegurar que a combinação de cores entre o fundo e o primeiro plano seja suficientemente contrastante para ser vista por pessoas com cromo deficiências, bem como pelas que utilizam monitores de vídeo monocromáticos."));
		
		//Recomendação 3 -  Utilizar corretamente marcações e folhas de estilo

		mapRegra.put("3.1", new Regra(2,"Sempre que existir uma linguagem de marcação apropriada, utilizar marcações em vez de imagens para transmitir informações. Evite também usar imagens para representar texto, prefira folhas de estilo."));
		mapRegra.put("3.2", new Regra(2,"Criar documentos passíveis de validação pelos padrões formais, publicados pelo W3C. Caso seja necessário desenvolver uma página com uma gramática em particular, use a instrução DOCTYPE na primeira linha do seu arquivo HTML. Esse tipo de instrução informa aos servidores, navegadores e validadores que o código está dentro das regras da linguagem informada."));
		mapRegra.put("3.4", new Regra(2,"Utilizar unidades relativas, e não absolutas, nos valores dos atributos da linguagem de marcação e nos valores das propriedades das folhas de estilo. Em CSS não use valores absolutos como “pt” ou “cm” e sim valores relativos como o “em”, \"ex\" ou em porcentagem."));
		mapRegra.put("3.5", new Regra(2,"Use elementos de cabeçalho para veicular a lógica e hierárquica da estrutura e use-os de acordo com as especificações. Utilize os cabeçalhos de forma ordenada."));
		mapRegra.put("3.6", new Regra(2,"Use a notação para listas e use os seus itens corretamente. Use corretamente a estrutura e os itens das listas. Evitar o uso destes elementos para formatar parágrafos. Quando necessário use listas numeradas."));
		mapRegra.put("3.7", new Regra(2,"Marcar as citações. Não utilizar marcações de citação para efeitos de formatação, como, por exemplo, o avanço de texto."));
		
		//Recomendação 4 -  Indicar claramente qual o idioma utilizado
		mapRegra.put("4.1", new Regra(1,"Identificar claramente quaisquer mudanças de idioma no texto de um documento, bem como nos equivalentes textuais (por ex., legendas). Use o atributo “lang” para identificar claramente as alterações do idioma no texto."));
		mapRegra.put("4.2", new Regra(3,"Especificar por extenso cada abreviatura ou sigla quando da sua primeira ocorrência em um documento. Quando usar uma abreviatura ou uma sigla numa frase, não se esqueça de o indicar no HTML usando ABBR e ACRONYM"));		
		mapRegra.put("4.3", new Regra(3,"Identificar o principal idioma utilizado nos documentos. O idioma do documento deve ser especificado na expressão HTML."));		
		
		//Recomendação 5 - Criar tabelas passíveis de transformação harmoniosa

		mapRegra.put("5.1", new Regra(1,"Em tabelas de dados, identificar os cabeçalhos de linha e de coluna."));
		mapRegra.put("5.2", new Regra(1,"Em tabelas de dados com dois ou mais níveis lógicos de cabeçalhos de linha ou de coluna, utilizar marcações para associar as células de dados às células de cabeçalho (atributo id/headers). Organize tabelas complexas de forma que se possa identificar facilmente suas divisões."));
		mapRegra.put("5.3", new Regra(2,"Não utilizar tabelas para efeitos de disposição em página, a não ser que a tabela continue a fazer sentido depois de ser linearizada. Se não for o caso, fornecer um equivalente alternativo (que pode ser uma versão linearizada)."));
		mapRegra.put("5.4", new Regra(2,"Fornecer resumos das tabelas. Utilize o atributo SUMMARY dentro das tabelas para descrever o conteúdo de tabelas."));
		mapRegra.put("5.5", new Regra(3,"Fornecer abreviaturas para os rótulos de cabeçalho. Use o atributo ABBR dentro do elemento TH quando você tiver cabeçalhos muito longos. Os leitores de tela quando acharem o ABBR lerão apenas o seu conteúdo e não o texto do cabeçalho na integra."));
		
		//Recomendação 6 - Assegurar que as páginas dotadas de novas tecnologias sejam transformadas harmoniosamente
		mapRegra.put("6.1", new Regra(1,"Organizar os documentos de tal forma que possam ser lidos sem recurso em folhas de estilo. Por exemplo, se um documento em HTML for reproduzido sem as folhas de estilo que lhe estão associadas, deve continuar a ser possível lê-lo."));		
		mapRegra.put("6.2", new Regra(1,"Assegurar que os equivalentes de conteúdo dinâmico sejam atualizados sempre que esse conteúdo mudar. Construa o Frame sempre baseado num documento HTML."));
		mapRegra.put("6.3", new Regra(1,"Assegurar que todas as páginas possam ser utilizadas mesmo que os programas interpretáveis, os applets ou outros objetos programados tenham sido desativados ou não sejam suportados. Se isso não for possível, fornecer informações equivalentes em uma página alternativa, acessível. Evite colocar scripts que executem no navegador do usuário. Pode ser que seja um navegador que não suporte scripts. Evite a criação de links que usem “javascript” tais como URI. Se um usuário não usar scripts, então não será capaz de encontrar os links, uma vez que o navegador não consegue criar o conteúdo link."));
		mapRegra.put("6.4", new Regra(2,"Em programas interpretáveis e applets, assegurar que a resposta a eventos seja independente do dispositivo de entrada. Sempre que tiver script associe o elemento NOSCRIPT."));
		mapRegra.put("6.5", new Regra(2,"Assegurar a acessibilidade do conteúdo dinâmico ou fornecer apresentação ou página alternativa."));
		
		//Recomendação 7 - Assegurar o controle do usuário sobre as alterações temporais do conteúdo

		mapRegra.put("7.1", new Regra(1,"Evitar concepções que possam provocar intermitência da tela, até que o usuário possam acessar o controle para interromper a seqüência. Pessoas com epilepsia fotosensitiva podem desencadear um ataque epiléptico com o cintilar ou piscar numa banda de 4 a 59 intermitências por segundo (Hertz), com um pico nas 20 intermitências por segundo, bem como com alterações rápidas do escuro para a luz (como sucede com as lâmpadas das discotecas)."));		 
		mapRegra.put("7.2", new Regra(2,"Evitar situações que possam provocar o piscar do conteúdo das páginas (isto é, alterar a apresentação a intervalos regulares, como ligar e desligar), possibilitar que o software do usuário possa ter o controle desse efeito."));
		mapRegra.put("7.3", new Regra(2,"Evitar páginas contendo movimento, até que os softwares do usuário possibilitem a imobilização do conteúdo."));
		mapRegra.put("7.4", new Regra(2,"Não criar páginas de atualização automática periódica, até que os agentes do usuário possibilitem parar essa atualização."));
		mapRegra.put("7.5", new Regra(2,"Não utilizar marcações para redirecionar as páginas automaticamente, até que os softwares do usuário possibilitem parar o redirecionamento automático. Ao invés de utilizar marcações, configurar o servidor para que execute os redirecionamentos."));
	
		//Recomendação 8 - Assegurar a acessibilidade direta de interfaces do usuário integradas

		//8.1:[prioridade 1 se a funcionalidade for importante e não estiver presente em outro local; prioridade 2, se não for o caso]. 
		mapRegra.put("8.1", new Regra(1,"Criar elementos de programação, tais como programas interpretáveis e applets, diretamente acessíveis pelas tecnologias de apoio ou com elas compatíveis."));
		
		//Recomendação 9 - Projetar páginas considerando a independência de dispositivos
		mapRegra.put("9.3", new Regra(1,"Fornecer mapas de imagem armazenados no cliente ao invés de no servidor, exceto quando as regiões não puderem ser definidas por forma geométrica disponível."));
		mapRegra.put("9.2", new Regra(2,"Assegurar que qualquer elemento dotado de interface própria possa funcionar de modo independente de equipamentos, dispositivos ou qualquer hardware."));
		mapRegra.put("9.3", new Regra(2,"Em programas interpretáveis, especificar respostas a eventos, preferindo-as a rotinas dependentes de dispositivos (mouse).•	Use “onmousedown” com “onkeydown”. Use “onmouseup” com “onkeyup”, Use “onclick” com “onkeypress” , Note que não existe equivalente de teclado para duplo-click (“ondblclick”) em HTML 4.0."));
		mapRegra.put("9.4", new Regra(3,"Criar uma seqüência lógica de tabulação para percorrer links, controles de formulários e objetos. A tecla TAB é usada também, dentro do navegador para se movimentar dentro das páginas. Com isso elas devem ser dispostas para que sejam navegadas de forma linear. O comando TABINDEX permite que você estabeleça uma ordem lógica. O TABINDEX trabalha com os elementos <A>, <AREA>, <BUTTON>, <INPUT>, <OBJECT>, <SELECT> e <TEXTAREA>."));
		mapRegra.put("9.5", new Regra(3,"Fornecer atalhos por teclado que apontem para links importantes (incluindo os contidos em mapas de imagem armazenados no cliente), controles de formulários, grupo de controles de formulários, menus e conteúdo. Você pode permitir que o usuário possa saltar ou ir diretamente a campos do formulário através do comando ACCESSKEY."));
		
		
		//Recomendação 10 - Utilizar soluções de transição
		mapRegra.put("10.1", new Regra(2,"Não provocar o aparecimento de janelas de sobreposição, janelas popup ou outras quaisquer, assim como nenhuma modificação do conteúdo sem que o usuário seja informado disso. Não é recomendável para o usuário, que links abram em uma nova janela. Se o usuário utiliza um navegador com tela cheia não poderá voltar para a página anterior, também o histórico e a possibilidade de ir e voltar a páginas visitadas ficam comprometidos. Caso você tenha uma real necessidade que sua página abra uma outra janela, informe ao usuário."));
		mapRegra.put("10.2", new Regra(2,"Assegurar o correto posicionamento de todos os controles de formulários que tenham rótulos implicitamente associados, até que os agentes do usuário venham a suportar associações explícitas entre rótulos e controles de formulários. Associe legendas aos controles dos formulários de forma que a informação seja clara."));
		mapRegra.put("10.3", new Regra(3,"Proporcionar uma alternativa de texto linear (na mesma ou em outra página), em relação a todas as tabelas que apresentem o texto em colunas paralelas e com translineação, até que os agentes do usuário (incluindo as tecnologias de apoio) reproduzam corretamente texto colocado lado a lado. Ofereça uma alternativa para as pessoas que disponham de leitores de tela e que não interpretem tabelas corretamente."));
		mapRegra.put("10.4", new Regra(3,"Incluir caracteres predefinidos de preenchimento nas caixas de edição e nas áreas de texto, até que os agentes do usuário tratem corretamente os controles vazios. Alguns navegadores antigos não permitem que a tecla TAB seja usada para movimentação dentro de formulários. Para isso coloque um texto no campo do formulário para que o campo seja localizado de forma mais fácil."));
		mapRegra.put("10.5", new Regra(3,"Inserir, entre links adjacentes, caracteres que não funcionem como link e sejam passíveis de impressão (com um espaço de início e outro de fim), até que os softwares do usuário (incluindo as tecnologias de apoio) reproduzam clara e distintamente os links adjacentes. Quando existem muitos links numa mesma linha, separe-os com caracteres de forma a criar mais espaço entre eles. Isso criará uma pausa maior entre os links quando houver um leitor de tela passando entre os links."));
		
		//Recomendação 11 - Utilizar tecnologias e recomendações do W3C
		mapRegra.put("11.1", new Regra(2,"Utilizar tecnologias do W3C sempre disponíveis e adequadas a uma determinada tarefa; utilizar as versões mais recentes, desde que suportadas."));
		mapRegra.put("11.2", new Regra(2,"Não usar funcionalidades desatualizadas de tecnologias do W3C."));
		mapRegra.put("11.3", new Regra(3,"Fornecer informações que possibilitem aos usuários receber os documentos de acordo com as suas preferências (por ex., por idioma ou por tipo de conteúdo)."));
		mapRegra.put("11.4", new Regra(1,"Se, apesar de todos os esforços, não for possível criar uma página acessível, momentaneamente fornecer um link a uma página alternativa que utilize tecnologias do W3C, seja acessível, contenha informações (ou funcionalidade) equivalentes e seja atualizada tão freqüentemente quanto a página original, considerada inacessível."));
		
		
		
		//Recomendação 12 - Fornecer informações de contexto e orientações.
		mapRegra.put("12.1", new Regra(1,"Dar, a cada frame, um título que facilite a identificação dos frames e sua navegação."));
		mapRegra.put("12.2", new Regra(2,"Descrever a finalidade dos frames e o modo como se relacionam entre si, se isso não for óbvio a partir unicamente dos títulos. Caso seja necessário, explicar com mais detalhes sobre a finalidade de cada frame, use o atributo LONGDESC para complementar a informação."));
		mapRegra.put("12.3", new Regra(2,"Dividir grandes blocos de informação em grupos mais fáceis de gerenciar, sempre que for o caso. Por exemplo, em HTML, use OPTGROUP para agrupar os elementos OPTION dentro de um elemento de lista SELECT; agrupe os controles de formulário com FIELDSET e LEGEND; use listas sempre que seja apropriado; use cabeçalhos para estruturar documentos, etc."));
		mapRegra.put("12.4", new Regra(2,"Associar explicitamente os rótulos aos respectivos controles. Use o comando LABEL para associar campos nos formulários. Fazendo isso os leitores de tela associarão os elementos dos formulários de forma correta."));
		
		
		//Recomendação 13 - Fornecer mecanismos de navegação claros

		mapRegra.put("13.1", new Regra(2,"Identificar claramente o destino de cada link. O texto do link deve ser facilmente compreensível e conciso para que tenha sentido quando for lido, mesmo fora do dispositivos padrão. Como por exemplo, um leitor de tela."));
		mapRegra.put("13.2", new Regra(2,"Fornecer metadados para acrescentar informações semânticas a páginas ou sites."));
		mapRegra.put("13.3", new Regra(2,"Dar informações sobre a organização geral de um sítio (por ex., por meio de um mapa do sítio ou de um sumário). Crie um mapa do sitio de forma textual. Mas crie associando aos títulos das páginas. Isso não irá causar confusão aos usuários que utilizarem leitores de tela."));
		mapRegra.put("13.4", new Regra(2,"Utilizar os mecanismos de navegação de maneira coerente e sistemática."));		
		mapRegra.put("13.5", new Regra(3,"Fornecer barras de navegação para destacar e dar acesso ao mecanismo de navegação. Deve-se indicar ao usuário as etapas do caminho percorrido durante a navegação, oferecendo a opção de voltar a qualquer das partes."));
		mapRegra.put("13.6", new Regra(3,"Agrupar links relacionados entre si, identificar o grupo (em benefício dos agentes do usuário) e, até que os softwares do usuário se encarreguem de tal função, fornecer um modo de contornar determinado grupo."));
		mapRegra.put("13.7", new Regra(3,"Se forem oferecidas funções de pesquisa, ativar diferentes tipos de pesquisa de modo a corresponderem a diferentes níveis de competência e às preferências dos usuários."));
		mapRegra.put("13.8", new Regra(3,"Colocar informações identificativas no início de cabeçalhos, parágrafos, listas."));
		mapRegra.put("13.9", new Regra(3,"Fornecer informações sobre coleções de documentos (isto é, documentos compostos por várias páginas)."));		
		mapRegra.put("13.10", new Regra(3,"Fornecer meios para ignorar inserções de arte ASCII com várias linhas."));
		
		//Recomendação 14 - Assegurar a clareza e a simplicidade dos documentos.

		mapRegra.put("14.1", new Regra(1,"Utilizar linguagem a mais clara e simples possível, adequada ao conteúdo do síte."));		
		mapRegra.put("14.2", new Regra(3,"Complementar o texto com apresentações gráficas ou sonoras, sempre que facilitarem a compreensão da página. Incluir, por exemplo imagens, animações ou vídeos em apresentações facilita e torna especialmente útil para analfabetos, surdos que podem visualizar as apresentações visuais."));
		mapRegra.put("14.3", new Regra(3,"Criar um estilo de apresentação coerente e sistemático, ao longo das diferentes páginas."));		
		
	}


	public ArrayList<String> getArrCod() {
		carregaRegras();
		return new ArrayList<String>(mapRegra.keySet());
	}
	
}