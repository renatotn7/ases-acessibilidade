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

import br.org.acessobrasil.silvinha.util.lang.TokenLang;

/**
 * Contem algoritmos para as Regras EMAG
 * 
 * @author Renato Tomaz Nati
 * @since 27/07/2007
 * @version 1.0
 */
public class RegrasHardCodedEmag extends SuperRegrasHardCoded implements InterfRegrasHardCoded {
	final int NUMERO_DE_GENERICOS=18;
	//private String[] regrasGenericas = new String[NUMERO_DE_GENERICOS];

	/**
	 * Guarda as regras e seu código
	 */
	private static HashMap<String,Regra> mapRegra = new HashMap<String,Regra> ();
	
	public RegrasHardCodedEmag() {
		//getGenericos();
	}

	/**
	 * Método que retorna os avisos Genéricos EMAG
	 * 
	 * @author Renato Tomaz Nati
	 * @return String[] formato: gl.cp@regrax@prioridade@***@...
	 * @since 27/07/2007
	 * @version 1.0
	 */

	public String[] getGenericos() {

		int cont=0;
		
		// array que conterá respectivamente: gl, cp
		String glCpPrio[] = new String[3];

		// mapa que conterá respectivamente: o glcp e a String txtRegra
		String[] regras = new String[NUMERO_DE_GENERICOS];

		// adiciona a primeira regra
		glCpPrio[0] = "1";
		glCpPrio[1] = "2";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_15
				+ "@" + glCpPrio[2];
		
		cont++;
		
		// adiciona a segunda regra

		glCpPrio[0] = "3";
		glCpPrio[1] = "2";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_16
				+ "@" + glCpPrio[2];

		cont++;

		glCpPrio[0] = "3";
		glCpPrio[1] = "5";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_47
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "24";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_48
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "2";
		glCpPrio[1] = "11";
		glCpPrio[2] = "2";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_51
				+ "@" + glCpPrio[2];

		
		cont++;
		
		glCpPrio[0] = "3";
		glCpPrio[1] = "14";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_54
				+ "@" + glCpPrio[2];

		
		cont++;
		
		
		glCpPrio[0] = "2";
		glCpPrio[1] = "17";
		glCpPrio[2] = "2";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_55
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "10";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_56
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "3";
		glCpPrio[1] = "6";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_57
				+ "@" + glCpPrio[2];

		
		cont++;
		
		glCpPrio[0] = "3";
		glCpPrio[1] = "9";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_60
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "3";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_62
				+ "@" + glCpPrio[2];

		
		cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "9";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_63
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "3";
		glCpPrio[1] = "11";
		glCpPrio[2] = "3";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_64
				+ "@" + glCpPrio[2];

		cont++;
		
		glCpPrio[0] = "2";
		glCpPrio[1] = "9";
		glCpPrio[2] = "2";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_65
				+ "@" + glCpPrio[2];
		
	cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "21";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_67
				+ "@" + glCpPrio[2];
		
cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "4";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_6
				+ "@" + glCpPrio[2];
		
cont++;
		
		glCpPrio[0] = "1";
		glCpPrio[1] = "5";
		glCpPrio[2] = "1";
		regras[cont] = glCpPrio[0] + "." + glCpPrio[1] + "@" + TokenLang.REGRA_7
				+ "@" + glCpPrio[2];
		
		//regrasGenericas = (String[]) regras.clone();
//		+2 prioridade1 
//		+1 prioridade 3
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
		return mapRegra.get(codigo).getTexto();
	}
	
	/**
	 * Retorna a prioridade da regra
	 * Ex: 2.19 
	 * @param codigo código da regra (2.19)
	 * @return Prioridade da regra 1 2 ou 3
	 */
	public int getPrioridadeRegra(String codigo){
		carregaRegras();
		Regra regra = mapRegra.get(codigo);
		if(regra==null){
			//System.out.print("Regra " +codigo +" não encontrada");
			return 0;
		}else{
			return regra.getPrioridade();
		}
	}
	
	public ArrayList<String> getArrCod(){
		carregaRegras();
		return new ArrayList<String>(mapRegra.keySet());
	}
	
	/**
	 * Carrega as regras
	 *
	 */
	public void carregaRegras(){
		//evita o carregamento mais de uma vez
		if(mapRegra.size()>0){
			return;
		}
		//2.15 label
		
		mapRegra.put("1.1", new Regra(1,"Identificar o principal idioma utilizado nos documentos. O idioma do documento deve ser especificado na expressão HTML."));
		mapRegra.put("1.2", new Regra(1,"Identificar claramente quaisquer mudanças de idioma no texto de um documento, bem como nos equivalentes textuais (por ex., legendas de imagens). Use o atributo \"lang\" para identificar claramente as alterações do idioma no texto."));
		mapRegra.put("1.3", new Regra(1,"Fornecer meios para ignorar e explicar inserções de arte ASCII com várias linhas, tais como links de âncora ou páginas alternativas."));
		mapRegra.put("1.4", new Regra(1,"Assegurar que todas as informações veiculadas com cor estejam também disponíveis sem cor."));
		//Geral por enquanto
		//Contraste
		mapRegra.put("1.5", new Regra(1,"Assegurar que a combinação de cores entre o fundo e o primeiro plano seja suficientemente contrastante para poder ser vista por pessoas com cromodeficiências, bem como pelas que utilizam monitores de vídeo monocromáticos."));
		
		mapRegra.put("1.6", new Regra(1,"Organizar os documentos de tal forma que possam ser lidos sem recurso à folhas de estilo. Por exemplo, se um documento em HTML for reproduzido sem as folhas de estilo que lhe estão associadas, deve continuar a ser possível lê-lo."));
		mapRegra.put("1.7", new Regra(1,"Não usar concepções que possam provocar intermitência da tela, até que os leitores de tela ou navegadores do usuário possibilitem o seu controle."));
		mapRegra.put("1.8", new Regra(1,"Criar uma seqüência lógica de tabulação para percorrer links, controles de formulários e objetos."));
		mapRegra.put("1.9", new Regra(1,"Utilizar a linguagem mais clara e simples possível, logicamente, adequada ao conteúdo do sítio."));
		mapRegra.put("1.10", new Regra(1,"Utilizar os mecanismos de navegação de maneira coerente, consistente e sistemática. Por exemplo, organizar itens do menu por tema, seções ou classes, etc."));
		mapRegra.put("1.11", new Regra(1,"Fornecer um equivalente textual a cada imagem (isso abrange: representações gráficas do texto, incluindo símbolos, GIFs animados, imagens utilizadas como sinalizadores de pontos de enumeração, espaçadores e botões gráficos), para tanto, utiliza-se o atributo \"alt\" ou \"longdesc\" em cada imagem."));
		mapRegra.put("1.12", new Regra(1,"Fornecer links de texto redundantes relativos a cada região ativa de um mapa de imagem armazenado tanto no cliente quanto no servidor. Não esquecendo de adicionar texto equivalente à imagem mostrada, no caso o \"alt\" ou \"longdesc\"."));
		mapRegra.put("1.13", new Regra(1,"Fornecer resumos das tabelas utilizando o atributo \"summary\", caso seja criada uma tabela para dados. Se a tabela foi criada para efeito de design, deixar o \"summary\" em branco."));
		mapRegra.put("1.14", new Regra(1,"Em tabelas de dados com dois ou mais níveis lógicos de cabeçalhos, sejam de linha ou de coluna, utilizar marcações para associar as células de dados às células de cabeçalho. Organize tabelas complexas de forma que possa identificar facilmente suas divisões."));
		mapRegra.put("1.15", new Regra(1,"Assegurar que os equivalentes de conteúdo dos frames (dinâmico ou não) sejam atualizados sempre que esse conteúdo mudar. A origem do frame sempre deve estar ligada a um arquivo HTML."));
		mapRegra.put("1.16", new Regra(1,"Assegurar a acessibilidade do conteúdo de frames, fornecendo uma página alternativa através do elemento \"noframes\"."));
		mapRegra.put("1.17", new Regra(1,"Dar a cada frame um título que facilite a identificação dos frames e sua navegação."));
		mapRegra.put("1.18", new Regra(1,"Descrever a finalidade dos frames e o modo como se relacionam entre si, se isso não for óbvio a partir unicamente dos títulos, forneça uma descrição mais detalhada."));
		mapRegra.put("1.19", new Regra(1,"Assegure a acessibilidade de objetos programados, tais como programas interpretáveis e applets, garantindo que a resposta a eventos seja independente do dispositivo de entrada e que qualquer elemento dotado de interface própria possa funcionar com qualquer leitor de tela ou navegador que o usuário utilize. Evite colocar scripts que estejam vinculados a links, se isso não for possível, fornecer informações equivalentes em uma página alternativa acessível."));
		mapRegra.put("1.20", new Regra(1,"Assegurar que todas as páginas possam ser utilizadas mesmo que os programas interpretáveis, os applets ou outros objetos programados tenham sido desativados ou não sejam suportados. Sempre que tiver script associe logo a seguir o elemento \"noscript\"."));
		mapRegra.put("1.21", new Regra(1,"Fornecer equivalentes textuais para sons (reproduzidos ou não com interação do usuário), arquivos de áudio independentes, trilhas áudio de vídeo e trechos de vídeo."));
		mapRegra.put("1.22", new Regra(1,"Em apresentações multimídia baseadas em tempo (filme ou animação), fornecer ou sincronizar alternativas textuais equivalentes (legendas ou descrições sonoras dos trechos visuais)."));
		mapRegra.put("1.23", new Regra(1,"Evitar páginas contendo movimento, até que os agentes do usuário possibilitem o controle e a imobilização do conteúdo."));
		mapRegra.put("1.24", new Regra(1,"Não sendo possível criar uma página acessível, crie uma página alternativa, juntamente com uma justificativa apropriada, que utilize tecnologias em conformidade com este documento - acessível, que contenha informações (ou funcionalidade) equivalentes e seja atualizada tão freqüentemente quanto a página original, considerada inacessível."));
		mapRegra.put("2.1", new Regra(2,"Criar documentos passíveis de validação por gramáticas formais publicadas. Declarando o tipo de documento (atributo \"doctype\") no topo do código fonte de cada página do sítio. Assim seu sítio informará aos servidores, navegadores e validadores que o código está dentro das regras da linguagem utilizada."));
		mapRegra.put("2.2", new Regra(2,"Utilizar unidades relativas, e não absolutas, nos valores dos atributos de tabelas, textos, etc. Em CSS não use valores absolutos como \"pt\" ou \"px\" e sim valores relativos como o \"em\", \"ex\" ou em porcentagem."));
		mapRegra.put("2.3", new Regra(2,"Marcar corretamente listas e pontos de enumeração em listas ordenadas. Use corretamente a estrutura e os itens das listas. Evite o uso destes elementos para formatar parágrafos."));
		mapRegra.put("2.4", new Regra(2,"Não criar páginas com atualização automática periódica, até que os leitores de tela ou navegadores possibilitem o controle da atualização para o usuário. Não utilize a tag meta \"refresh\" ou dispositivos semelhantes para atualização da página. Caso a página seja continuamente atualizada, informe ao usuário que ele deve \"recarregar\" a página de tempos em tempos."));
		mapRegra.put("2.5", new Regra(2,"Não utilizar marcações para redirecionar as páginas automaticamente, até que os leitores de tela ou navegadores do usuário possibilitem interromper o processo."));
		mapRegra.put("2.6", new Regra(2,"Não provocar o aparecimento de janelas de sobreposição, janelas popup ou outras quaisquer, assim como nenhuma modificação do conteúdo sem que o usuário seja informado disso. Não é recomendável para o usuário, que links abram em uma nova janela. Se o usuário utiliza um navegador com tela cheia não poderá voltar para a página anterior, também o histórico e a possibilidade de ir e voltar a páginas visitadas ficam comprometidos. Caso você tenha uma real necessidade que sua página abra uma outra janela, informe ao usuário."));
		mapRegra.put("2.7", new Regra(2,"Sempre que existir uma linguagem de marcação apropriada, utilizar marcações em vez de imagens para transmitir informações, um exemplo é a linguagem MathML que permite a criação de fórmulas matemáticas somente utilizando-se das tags apropriadas."));
		mapRegra.put("2.8", new Regra(2,"Utilizar o elemento \"blockquote\" para marcar citações quando existentes. Não use \"blockquote\", \"ul\", \"dl\" & \"dt\", \"table\" e outros elementos para criar efeitos visuais nos parágrafos. Caso a intenção seja organizar a estrutura ou a disposição de textos no sítio, utilize folhas de estilo."));
		mapRegra.put("2.9", new Regra(2,"Criar um estilo de apresentação coerente e sistemático, ao longo das diferentes páginas, como exemplo, mantendo um padrão de desenho, agrupando os itens do menu de forma coerente. Mantenha os botões principais de navegação no mesmo local em cada página. Isso ajudará ao usuário a localizar-se rapidamente, e saber o destino de cada botão levará. Mantenha para o sítio uma paleta de cores, estilos de texto e diagramação consistente. Identifique as regiões da página, navegação e fim de página de forma clara."));
		mapRegra.put("2.10", new Regra(2,"Utilize elementos de cabeçalho de forma lógica, organizando o conteúdo de acordo com uma hierarquia."));
		mapRegra.put("2.11", new Regra(2,"Sempre que necessário, divida grandes blocos de informação em grupos mais fáceis de gerenciar. As opções de menu devem ser dispostas de forma consistente na mesma ordem relativa no grupo de opções. Se as opções num painel de menu estão ordenadas \"arquivo, editar, inserir, imprimir\", essas opções devem aparecer naquela mesma ordem quando aquele grupo for apresentado novamente (ou quando um outro painel contendo aquele mesmo grupo de opções seja apresentado)."));
		mapRegra.put("2.12", new Regra(2,"Não utilizar tabelas para efeitos de disposição em página, prefira o uso de folhas de estilo para a diagramação das páginas. Sendo utilizadas tabelas construa de forma que a disposição continue a fazer sentido depois de ser linearizada. Em último caso, forneça um equivalente alternativo (que pode ser uma versão linearizada)."));
		mapRegra.put("2.13", new Regra(2,"Se for utilizada uma tabela para efeitos de disposição em página, não utilizar qualquer marcação estrutural para efeitos de formatação visual. Não use comandos destinados a indicar cabeçalhos de tabela como o \"th\" para formatar parágrafos ou fazer títulos em \"bold\"."));
		mapRegra.put("2.14", new Regra(2,"Incluir caracteres pré-definidos de preenchimento nas caixas de edição e nas áreas de texto, até que os navegadores tratem corretamente os controles vazios."));
		mapRegra.put("2.15", new Regra(2,"Usar o elemento \"label\" juntamente com o atributo \"id\" para associar os rótulos aos respectivos controles dos formulários. Assim, os leitores de tela associarão os elementos do formulário de forma correta. Usando o comando \"label\" as pessoas que usam leitores de tela não terão problemas ao ler o formulário. Caso haja grupos de informação, controles, etc, a estes devem estar devidamente diferenciados, seja por meio de espaçamento, localização ou elementos gráficos."));
		mapRegra.put("2.16", new Regra(2,"Assegurar o correto posicionamento de todos os controles de formulários que tenham rótulos implicitamente associados, até que os leitores de tela ou navegadores do suportem associações explícitas entre rótulos e controles de formulários."));
		mapRegra.put("2.17", new Regra(2,"Forneça informações sobre como o sítio está estruturado, através de um mapa ou de sumário. Crie o mapa de forma textual, associando aos títulos das páginas para não causar confusão aos usuários que utilizarem leitores de tela."));
		mapRegra.put("2.18", new Regra(2,"Assegure a acessibilidade de objetos programados, tais como programas interpretáveis e applets, garantindo que a resposta a eventos seja independente do dispositivo de entrada e que qualquer elemento dotado de interface própria possa funcionar com qualquer leitor de tela ou navegador que o usuário utilize. Evite colocar scripts que estejam vinculados a links. Se isso não for possível, forneça informações equivalentes em uma página alternativa acessível."));
		mapRegra.put("2.19", new Regra(2,"Em programas interpretáveis, especificar respostas a eventos, preferindo as rotinas dependentes de dispositivos (mouse, teclado, etc)."));
		mapRegra.put("3.1", new Regra(3,"Não usar elementos considerados ultrapassados pelo W3C."));
		mapRegra.put("3.2", new Regra(3,"Especificar por extenso cada abreviatura ou sigla, quando da sua primeira ocorrência em um documento, utilizando os atributos \"abbr\" e \"acronym\". Utilize o atributo \"abbr\" dentro de um elemento \"th\" quando você tiver cabeçalhos muito longos, para que os leitores de tela lerem apenas o seu conteúdo e não o texto do cabeçalho na íntegra."));
		mapRegra.put("3.3", new Regra(3,"Fornecer atalhos por teclado que apontem para links importantes (incluindo os contidos em mapas de imagem armazenados no cliente), para início da área principal de conteúdo da página, controles de formulários, e grupo de controles de formulários."));
		mapRegra.put("3.4", new Regra(3,"Inserir, entre links adjacentes, caracteres que não funcionem como link e sejam passíveis de impressão (como um espaço), até que os leitores de tela ou navegadores (incluindo as tecnologias de apoio) reproduzam clara e distintamente os links adjacentes."));
		mapRegra.put("3.5", new Regra(3,"Sempre que possível, fornecer informações que possibilitem aos usuários receber os documentos de acordo com as suas preferências (por ex., por idioma ou por tipo de conteúdo)."));
		mapRegra.put("3.6", new Regra(3,"Fornecer barras de navegação para auxiliar os menus de navegação. Utilizar elemento que contextualizem a localização do usuário, como barras de caminho e \"Sua Localização\" nas páginas do documento."));
		mapRegra.put("3.7", new Regra(3,"Agrupar links relacionados entre si, identificando o grupo (em benefício do navegador ou leitor de tela do usuário) e, até que o navegador ou leitor de tela do usuário se encarregue de tal função, fornecer um modo de contornar determinado grupo."));
		mapRegra.put("3.8", new Regra(3,"Se forem oferecidas funções de pesquisa, ativar diferentes tipos de pesquisa de modo a corresponderem a diferentes níveis de competência e às preferências dos usuários. Sendo possível, quando a pesquisa não encontrar a palavra, sugerir palavras semelhantes."));
		mapRegra.put("3.9", new Regra(3,"Use palavras relevantes no início de cabeçalhos, parágrafos, e listas para identificar o assunto tratado."));
		mapRegra.put("3.10", new Regra(3,"Fornecer informações sobre documentos compostos por várias páginas (isto é, coleções de documentos). Caso seja necessário, utilize ferramentas de compactação de arquivo, tais como ZIP, TAR, GZIP ou ARJ. Informe o tamanho do arquivo e o tempo estimado para baixar por meio de um modem comum. Forneça documentos em formatos alternativos, passíveis de leitura pelos leitores de tela."));
		mapRegra.put("3.11", new Regra(3,"Complementar o texto com apresentações gráficas ou sonoras, sempre que puderem facilitar a compreensão da página."));
		mapRegra.put("3.12", new Regra(3,"Identificar claramente o destino de cada link, botão ou elemento que submeta uma ação. Prefira utilizar textos mais claros e objetivos, mostrando o verdadeiro sentido e o destino do link. Evite usar frases como \"Clique aqui\"."));
		mapRegra.put("3.13", new Regra(3,"Informar previamente ao usuário o destino e resultado da ação, quando houver campos e elementos do formulário, como, por exemplo, caixas de seleção, que submetem automaticamente o conteúdo ao se efetuar uma determinada seleção. Nestes casos, ao invés da seleção submeter automaticamente o formulário, é recomendável que se vincule ao elemento um botão para efetuar a ação."));
		mapRegra.put("3.14", new Regra(3,"Forneça metadados para acrescentar informações semânticas e descritivas do sítio, que sejam úteis para os mecanismos de busca."));
		
		//Regras especificas da ACBR
		mapRegra.put("4.1", new Regra(3,"Não faça atalhos \"accesskey\" que entrem em conflito com o Internet Explorer."));
	}

	@Override
	public int getCodWcagEmag() {
		return 2;
	}
}

