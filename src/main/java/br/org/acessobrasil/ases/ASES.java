/*******************************************************************************
 * Copyright 2005, 2006, 2007, 2008 Acessibilidade Brasil
 * Este arquivo é parte do programa ASES - Avaliador e Simulador para AcessibilidadE de Sítios
 * O ASES é um software livre; você pode redistribui-lo e/ou modifica-lo dentro dos termos da Licença Pública Geral GNU como
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da Licença, ou (na sua opnião) qualquer versão posterior.
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer  MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU para maiores detalhes.
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU, sob o título "LICENCA.txt", junto com este programa, se não, escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *******************************************************************************/
package br.org.acessobrasil.ases;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha.excessoes.ExceptionDialog;
import br.org.acessobrasil.silvinha.negocio.Gerente;
import br.org.acessobrasil.silvinha.util.HSQLDB;
import br.org.acessobrasil.silvinha.util.MemoryManagerThread;
import br.org.acessobrasil.silvinha.util.PropertyLoader;
import br.org.acessobrasil.silvinha.util.Token;
import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.util.versoes.ThreadVerificaVersao;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.SilvinhaModel;
import br.org.acessobrasil.silvinha.vista.panels.PainelAvaliacao;
import br.org.acessobrasil.silvinha.vista.panels.PainelConfig;
import br.org.acessobrasil.silvinha.vista.panels.PainelSplash;
import br.org.acessobrasil.silvinha2.mli.ErrosDoSistema;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.Silvinha;
import br.org.acessobrasil.silvinha2.util.G_Thread;
public class ASES {
	FrameSilvinha frameSilvinha;
	public static ThreadGroup processaErro = new ThreadGroup("processaErro");	
	public static void main(String args[]){
		if ((args != null) && (args.length > 0)) {
			Token.autServer = args[0];
		}
		try {
			new ASES(new FrameSilvinha());
		} catch (RuntimeException e) {
			e.printStackTrace();
			ExceptionDialog.showExceptionDialog(e.getMessage());
			System.exit(1);
		} catch (ClassNotFoundException cnfe) {
			ExceptionDialog.showExceptionDialog(cnfe.getMessage());
			System.exit(1);
		} catch (SQLException sqle) {
			ExceptionDialog.showExceptionDialog(sqle.getMessage());
			System.exit(1);
		} catch (IOException exp) {
			ExceptionDialog.showExceptionDialog(exp.getMessage());
			System.exit(1);
		} catch (IllegalAccessException iae) {
			ExceptionDialog.showExceptionDialog(iae.getMessage());
			System.exit(1);
		} catch (OutOfMemoryError oome) {
			MemoryManagerThread.setStop(true);
			Gerente.setEstouroMemoria(true);
			PainelAvaliacao.pararExecucao(); // Parar a avaliação
			ExceptionDialog.showExceptionDialog(ErrosDoSistema.ERRO_MEMORIA);
		}
	}

	public ASES(FrameSilvinha frameSilvinha) throws ClassNotFoundException, SQLException, IOException, IllegalAccessException{
		this.frameSilvinha=frameSilvinha;
		new File(RelatorioDaUrl.pathHD+"0").delete();
		new File(RelatorioDaUrl.pathHD+"0t").delete();
		frameSilvinha.inicializacaoSilvinhaWindowListener();
		inicializacaoBancoDeDados();
		carregaPropriedades();
		PainelSplash splash = frameSilvinha.showPainelSplash();
		String servidor = TokenLang.props.getProperty(PropertyLoader.SILVINHA_VERSION);
		
		File f = new File(RelatorioDaUrl.pathHD + 0);
		if (f.exists()) {
			f.delete();
		}
		
		G_Thread genericThread = new G_Thread(new ThreadVerificaVersao(servidor, SilvinhaModel.getVersao()));
		genericThread.setTimeOut(2000);
		genericThread.start();
		try {
			PainelConfig.configOK = new Token().buscaBanco();
		} catch (NullPointerException npe) {
			ExceptionDialog.showExceptionDialog(ErrosDoSistema.ERRO_BANCO_DE_DADOS);
			
			System.exit(1);
		} catch (Exception err) {
			
			ExceptionDialog.showExceptionDialog(ErrosDoSistema.ERRO_BANCO_DE_DADOS);
			System.exit(1);
		}
		frameSilvinha.setaCordoBackGround();
		

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			/*
			 * Sair do sistema
			 */
			System.exit(0);
		}
	
		splash.apaga();
		// inicializa os componentes gráficos da janela
		frameSilvinha.inicializarGraficos();

		// inicializa o monitor de memória
		new Thread(new MemoryManagerThread()).start();

		frameSilvinha.setVisible(true);
	}
	
	private void inicializacaoBancoDeDados() {

		// INICIALIZA BANCO
		HSQLDB bd = new HSQLDB();
		bd.iniciaBanco();
	}
	/**
	 * Carrega o arquivo de prop e também o idioma
	 */
	private void carregaPropriedades() throws IllegalAccessException, FileNotFoundException, IOException {

		// CARREGA AS PROPRIEDADES DO PROGRAMA
		TokenLang.props = new PropertyLoader().loadProperty(frameSilvinha);
		String language = TokenLang.props.getProperty(PropertyLoader.SILVINHA_LANGUAGE);
		if (language == null) {
			// Se não Existe linguagem definida no arquivo de properties,
			// carregar a linguagem default
			new TokenLang("pt.txt");
		} else {
			new TokenLang(language);
		}
		// System.out.println("TokenLang.LANG="+TokenLang.LANG);
		GERAL.carregaTexto(TokenLang.LANG);
		Silvinha.carregaTexto(TokenLang.LANG);
		ErrosDoSistema.carregaTexto(TokenLang.LANG);
	}
}
