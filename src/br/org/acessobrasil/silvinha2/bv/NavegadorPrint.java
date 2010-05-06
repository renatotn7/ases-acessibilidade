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
package br.org.acessobrasil.silvinha2.bv;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import br.org.acessobrasil.silvinha.util.lang.TokenLang;
import br.org.acessobrasil.silvinha.vista.frames.mensagens.Creditos;
import br.org.acessobrasil.silvinha2.bv.filtros.Catarata;
import br.org.acessobrasil.silvinha2.bv.filtros.DaltonismoDeuteranopia;
import br.org.acessobrasil.silvinha2.bv.filtros.DaltonismoProtanopia;
import br.org.acessobrasil.silvinha2.bv.filtros.DaltonismoTritanopia;
import br.org.acessobrasil.silvinha2.bv.filtros.FiltroDeImagem;
import br.org.acessobrasil.silvinha2.bv.filtros.Glaucoma;
import br.org.acessobrasil.silvinha2.bv.filtros.Hipermetropia;
import br.org.acessobrasil.silvinha2.bv.filtros.Miopia;
import br.org.acessobrasil.silvinha2.bv.filtros.NullTransform;
import br.org.acessobrasil.silvinha2.bv.filtros.PretoEBranco;
import br.org.acessobrasil.silvinha2.bv.filtros.Retinopatia;
import br.org.acessobrasil.silvinha2.bv.panel.PanelCatarata;
import br.org.acessobrasil.silvinha2.bv.panel.PanelContraste;
import br.org.acessobrasil.silvinha2.bv.panel.PanelGlaucoma;
import br.org.acessobrasil.silvinha2.bv.panel.PanelHipermetropia;
import br.org.acessobrasil.silvinha2.bv.panel.PanelMiopia;
import br.org.acessobrasil.silvinha2.bv.panel.PanelRetinopatia;
import br.org.acessobrasil.silvinha2.bv.panel.PanelVazio;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.mli.TradCreditos;
import br.org.acessobrasil.silvinha2.mli.TradPaineisBV;
import br.org.acessobrasil.silvinha.vista.componentes.MenuSilvinha;

/**
 * Interface gráfica que gera o navegador e uma tela com a deficiência simulada
 * 
 * @author Fabio Issamu Oshiro
 */
public class NavegadorPrint implements MouseListener {
	/**
	 * Display da aplicação
	 */
	private static Display display;

	/**
	 * Flag para saber se pausa a atualização da imagem
	 */
	private static boolean pause = false;

	/**
	 * Componente onde a imagem é mostrada
	 */
	private static JLabel imagemFile;

	/**
	 * Processo que sincroniza a imagem do navegador
	 */
	private TempoParaAtualizarImagem tempoParaBlur;

	/**
	 * Navegador padrão do usuário
	 */
	public static Browser browser;

	/**
	 * Panel onde fica a imagem com a simulação
	 */
	private static JPanel panelBlur;

	/**
	 * Panel onde é mostrada as opções de configuração
	 */
	private static JPanel panelConfig;
	
	/**
	 * Panel onde é feita as opções de contraste
	 * Escolhe a cor de fundo e do texto
	 */
	private static PanelContraste panelContraste;

	/**
	 * Panel que está sendo exibido para o usuário
	 */
	private static JPanel panelConfigAtual;

	/**
	 * Guarda o filtro atual
	 */
	private static FiltroDeImagem filtro = new NullTransform();

	// private static MixerFiltros mixerFiltros = new MixerFiltros();

	/**
	 * Guarda o buffer da imagem atual
	 */
	private static BufferedImage bufferedImageAtual;

	private static java.awt.Frame configFrame;

	/**
	 * Método que chama o filtro escolhido pelo usuário
	 */
	public void aplicarFiltro() {
		try {
			if (pause) {
				// System.out.println("Pausado");
				return;
			}
			if (imagemFile == null)
				return;
			if (panelBlur == null)
				return;
			int w = panelBlur.getWidth();
			int h = panelBlur.getHeight();
			int x = panelBlur.getWidth() + panelBlur.getLocationOnScreen().x;
			int y = panelBlur.getLocationOnScreen().y;
			BufferedImage bi;
			bi = new Robot().createScreenCapture(new Rectangle(x, y, w, h));
			// ImageIcon imageIcon = new
			// ImageIcon(mixerFiltros.aplicaFiltro(bi));
	
			bufferedImageAtual = filtro.aplicaFiltro(bi);
			ImageIcon imageIcon = new ImageIcon(bufferedImageAtual);
	
			imagemFile.setIcon(imageIcon);
			imagemFile.setSize(w, h);
		} catch (IllegalArgumentException e) {
			/*
			 * Dont stop!
			 */
			e.printStackTrace();
		} catch (IllegalComponentStateException e) {
			/*
			 * Dont stop!
			 */
			e.printStackTrace();
		} catch (SWTException e) {
			/*
			 * Dont stop!
			 */
			e.printStackTrace();
		} catch (AWTException e) {
			/*
			 * Dont stop!
			 */
			e.printStackTrace();
		}
	}

	/**
	 * Componente que faz a simulação de baixa visão
	 * 
	 */
	public NavegadorPrint() {
		// this.browser=browser;
		tempoParaBlur = new TempoParaAtualizarImagem(this);
		Thread tempoBlur = new Thread(tempoParaBlur);
		tempoBlur.start();

	}

	
	
	
	/**
	 * Cria o menu
	 * 
	 * @param args
	 */
	private static void criaMenu(final Shell shell) {
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);

		fileItem.setText(TradPaineisBV.E_ARQUIVO);
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);

		MenuItem itemSalvar = new MenuItem(submenu, SWT.PUSH);
		itemSalvar.setText(TradPaineisBV.SALVAR_IMAGEM);

		MenuItem itemSair = new MenuItem(submenu, SWT.PUSH);
		itemSair.setText(GERAL.SAIR);
		// item.setAccelerator (SWT.MOD1 + 'A');

		Menu submenuAjuda = new Menu(shell, SWT.DROP_DOWN);
		Menu submenuDef = new Menu(shell, SWT.DROP_DOWN);
		Menu submenuDalton = new Menu(shell, SWT.DROP_DOWN);
		MenuItem filtrosItem = new MenuItem(bar, SWT.CASCADE);
		filtrosItem.setText(TradPaineisBV.DEFICIENCIAS);
		filtrosItem.setMenu(submenuDef);

		MenuItem ajudaItem = new MenuItem(bar, SWT.CASCADE);
		ajudaItem.setText(TradPaineisBV.AJUDA);
		ajudaItem.setMenu(submenuAjuda);
		MenuItem itemCreditos = new MenuItem(submenuAjuda, SWT.PUSH);
		itemCreditos.setText(GERAL.ASES_CREDITOS);
		MenuItem itemManual = new MenuItem(submenuAjuda, SWT.PUSH);
		itemManual.setText("Manual");
				
		/*
		 * Astigmatismo Problema com luminosidade
		 */
		// MenuItem itemAstigmatismo = new MenuItem (submenuDef, SWT.PUSH);
		// itemAstigmatismo.setText ("Astigmatismo");
		final MenuItem itemHipermetropia = new MenuItem(submenuDef, SWT.PUSH);
		itemHipermetropia.setText(TradPaineisBV.HIPERMETROPIA);

		final MenuItem itemMiopia = new MenuItem(submenuDef, SWT.PUSH);
		itemMiopia.setText(TradPaineisBV.MIOPIA);

		MenuItem itemDaltonismo = new MenuItem(submenuDef, SWT.CASCADE);
		itemDaltonismo.setText(TradPaineisBV.DALTONISMO);
		itemDaltonismo.setMenu(submenuDalton);

		/*
		 * Tipos de Daltonismo
		 */
		final MenuItem itemDaltonismoProtanopia = new MenuItem(submenuDalton, SWT.PUSH);
		itemDaltonismoProtanopia.setText(TradPaineisBV.PROTANOPIA);
		final MenuItem itemDaltonismoDeuteranopia = new MenuItem(submenuDalton, SWT.PUSH);
		itemDaltonismoDeuteranopia.setText(TradPaineisBV.DEUTERANOPIA);
		final MenuItem itemDaltonismoTritanopia = new MenuItem(submenuDalton, SWT.PUSH);
		itemDaltonismoTritanopia.setText(TradPaineisBV.TRITANOPIA);
		final MenuItem itemDaltonismoMonocromia = new MenuItem(submenuDalton, SWT.PUSH);
		itemDaltonismoMonocromia.setText(TradPaineisBV.MONOCROMIA);

		/*
		 * Estrabismo foi removido por não fazer sentido
		 */
		// MenuItem itemEstrabismo = new MenuItem (submenuDef, SWT.PUSH);
		// itemEstrabismo.setText ("Estrabismo");
		final MenuItem itemCatarata = new MenuItem(submenuDef, SWT.PUSH);
		itemCatarata.setText(TradPaineisBV.CATARATA);

		final MenuItem itemGlaucoma = new MenuItem(submenuDef, SWT.PUSH);
		itemGlaucoma.setText(TradPaineisBV.GLAUCOMA);

		final MenuItem itemRetinopatia = new MenuItem(submenuDef, SWT.PUSH);
		itemRetinopatia.setText(TradPaineisBV.RETINOPATIA);
		
		//MenuItem itemContraste = new MenuItem(submenuDef, SWT.PUSH);
		//itemContraste.setText("Verificar Contraste");
		
		/*
		 * Mancha no meio do campo de visão
		 */
		// MenuItem itemDegeneracaoMacular = new MenuItem (submenuDef,
		// SWT.PUSH);
		// itemDegeneracaoMacular.setText ("Degeneração macular");
		/*
		 * Igual ao glaucoma com branqueamento no circulo do meio
		 */
		// MenuItem itemRetinosePigmentaria = new MenuItem (submenuDef,
		// SWT.PUSH);
		// itemRetinosePigmentaria.setText ("Retinose pigmentária");

		/*
		 * Retirado ressecamento e falta de vitamina
		 */
		// MenuItem itemXeroftalmia = new MenuItem (submenuDef, SWT.PUSH);
		// itemXeroftalmia.setText ("Xeroftalmia");
		/*
		 * Retirado visão dupla
		 */
		// MenuItem itemDiplopia = new MenuItem (submenuDef, SWT.PUSH);
		// itemDiplopia.setText ("Diplopia");
		
		/*
		itemContraste.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				panelContraste = new PanelContraste();
				panelConfigAtual = panelContraste;
				panelConfigAtual.setVisible(true);
				configFrame.add(panelConfigAtual, BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		//*/
		
		itemCreditos.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				new Creditos();
			}
		});
		
		itemManual.addListener(SWT.Selection, new Listener() {
			public void handleEvent (Event e) {
				MenuSilvinha.abreManual();
			}
		});


		itemGlaucoma.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				Glaucoma glaucoma = new Glaucoma();
				filtro = glaucoma;
				shell.setText(itemGlaucoma.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				panelConfigAtual = new PanelGlaucoma(glaucoma,TradPaineisBV.GLAUCOMA);
				panelConfigAtual.setVisible(true);
				configFrame.add(panelConfigAtual, BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});

		itemRetinopatia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				Retinopatia retinopatia = new Retinopatia();
				filtro = retinopatia;
				shell.setText(itemRetinopatia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				panelConfigAtual = new PanelRetinopatia(retinopatia,TradPaineisBV.RETINOPATIA);
				panelConfigAtual.setVisible(true);
				configFrame.add(panelConfigAtual, BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});

		itemCatarata.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				Catarata catarata = new Catarata();
				filtro = catarata;
				shell.setText(itemCatarata.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				panelConfigAtual = new PanelCatarata(catarata,TradPaineisBV.CATARATA);
				panelConfigAtual.setVisible(true);
				configFrame.add(panelConfigAtual, BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		itemHipermetropia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Hipermetropia hipermetropia = new Hipermetropia();
				filtro = hipermetropia;
				shell.setText(itemHipermetropia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				configFrame.removeAll();
				panelConfigAtual = new PanelHipermetropia(hipermetropia,TradPaineisBV.HIPERMETROPIA);
				panelConfigAtual.setVisible(true);
				configFrame.add(panelConfigAtual, BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		
		

		/*
		 * itemAstigmatismo.addListener(SWT.Selection, new Listener () { public
		 * void handleEvent (Event e) { configFrame.removeAll(); Astigmatismo
		 * astigmatismo = new Astigmatismo(); filtro = astigmatismo;
		 * configFrame.add(new
		 * PanelAstigmatismo(astigmatismo),BorderLayout.CENTER);
		 * configFrame.setVisible(true); } });
		 */

		itemMiopia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				Miopia miopia = new Miopia();
				filtro = miopia;
				shell.setText(itemMiopia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				configFrame.add(new PanelMiopia(miopia,TradPaineisBV.MIOPIA), BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});

		itemDaltonismoMonocromia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				filtro = new PretoEBranco();
				shell.setText(itemDaltonismoMonocromia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				configFrame.add(new PanelVazio(TradPaineisBV.DALTONISMO+" - "+TradPaineisBV.MONOCROMIA), BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		itemDaltonismoDeuteranopia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				filtro = new DaltonismoDeuteranopia();
				shell.setText(itemDaltonismoDeuteranopia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				configFrame.add(new PanelVazio(TradPaineisBV.DALTONISMO+" - "+TradPaineisBV.DEUTERANOPIA), BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		itemDaltonismoProtanopia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				filtro = new DaltonismoProtanopia();
				shell.setText(itemDaltonismoProtanopia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				configFrame.add(new PanelVazio(TradPaineisBV.DALTONISMO+" - "+TradPaineisBV.PROTANOPIA), BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		itemDaltonismoTritanopia.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				configFrame.removeAll();
				filtro = new DaltonismoTritanopia();
				shell.setText(itemDaltonismoTritanopia.getText()+" - " + TradPaineisBV.SIMULADOR_BV);
				configFrame.add(new PanelVazio(TradPaineisBV.DALTONISMO+" - "+TradPaineisBV.TRITANOPIA), BorderLayout.CENTER);
				configFrame.setVisible(true);
			}
		});
		itemSair.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Display.getCurrent().dispose();
				System.exit(0);
			}
		});

		itemSalvar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					pause = true;
					/*
					 * G_File imgJpg = new G_File("./");
					 * imgJpg.openDialogSaveAs(bufferedImageAtual);
					 */
					FileDialog dialog = new FileDialog(shell, SWT.SAVE);
					dialog.setFilterNames(new String[] { "Imagem JPG", "Todos tipos (*.*)" });
					dialog.setFilterExtensions(new String[] { "*.jpg", "*.*" }); 
					// Windows wild cards
					// dialog.setFilterPath ("c:\\"); //Windows path
					dialog.setFileName("imagem.jpg");
					String fullPath = dialog.open();
					if (fullPath != null) {
						if (!new File(fullPath).exists()) {
							ImageIO.write(bufferedImageAtual, "jpg", new File(fullPath));
						} else {
							// *
							String tl = "\n    ";
							String br = "    \n\n";
							final boolean[] result = new boolean[1];
							final Shell sobrescreve = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
							sobrescreve.setLayout(new GridLayout(2, true));
							sobrescreve.setText(TradPaineisBV.ARQUIVO_EXISTENTE);
							// sobrescreve.setBounds(400, 300, 300,120);
							GridData data1 = new GridData();
							data1.horizontalSpan = 2;

							final Label aviso = new Label(sobrescreve, SWT.PUSH);
							aviso.setLayoutData(data1);

							aviso.setText(tl + TradPaineisBV.ARQUIVO_EXISTENTE_SOBRESCREVER + br);
							final Button ok = new Button(sobrescreve, SWT.PUSH);
							ok.setText(TradPaineisBV.SIM);
							ok.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
							Button cancel = new Button(sobrescreve, SWT.PUSH);

							cancel.setText(TradPaineisBV.NAO);
							cancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
							Listener listener = new Listener() {
								public void handleEvent(Event event) {
									result[0] = event.widget == ok;
									sobrescreve.close();
								}
							};
							ok.addListener(SWT.Selection, listener);
							cancel.addListener(SWT.Selection, listener);
							sobrescreve.pack();
							sobrescreve.open();
							// System.out.println ("Prompt ...");
							while (!sobrescreve.isDisposed()) {
								if (!display.readAndDispatch())
									display.sleep();
							}
							// System.out.println ("Result: " + result [0]);
							if (result[0]) {
								ImageIO.write(bufferedImageAtual, "jpg", new File(fullPath));
							}
						}
						/*
						 * / ImageIO.write(bufferedImageAtual, "jpg", new
						 * File(fullPath)); //
						 */
					}
					pause = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * Carrega opção de idioma 
	 */
	private static void setLang(){
		String lang = "pt";

		Properties prop = new Properties();
		File arquivoPropriedades = new File("config/config_geral.properties");
		if (!arquivoPropriedades.exists()) {
			arquivoPropriedades = new File("../config/config_geral.properties");
		}
		if (arquivoPropriedades.exists()) {
			FileInputStream in;
			try {
				in = new FileInputStream(arquivoPropriedades);
				prop.load(in);
				lang = prop.getProperty("lang");
				if (lang == null || lang.equals(""))
					lang = "pt";
				in.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		TokenLang.LANG = lang;
		GERAL.carregaTexto(lang);
		TradPaineisBV.carregaTexto(lang);
		TradCreditos.carregaTexto(lang);
	}
	
	public static void main(String[] args) {
		// ToolBar toolbar;
		Button itemBack, itemForward, itemStop, itemRefresh, itemGo;
		final Text location;
		display = new Display();
		final Shell shell = new Shell(display);

		/*
		 * Carrega opção de idioma 
		 */
		setLang();
		

		shell.setText(TradPaineisBV.SIMULADOR_BV);
		criaMenu(shell);
		
		
		shell.setLayout(new GridLayout(2, true));
		{
			/*
			 * Coloca a barra de ferramentas
			 */
			GridData data1 = new GridData();
			//GridData data = new GridData();

			Composite barrasFiltrosNavegador;
			barrasFiltrosNavegador = new Composite(shell, SWT.NONE);
			data1.horizontalSpan = 2;
			data1.horizontalAlignment = GridData.FILL;
			data1.grabExcessHorizontalSpace = true;
			// barrasFiltrosNavegador.setLayoutData(new GridData(SWT.CENTER,
			// SWT.NONE, true, true, 2, 1));
			barrasFiltrosNavegador.setLayoutData(data1);
			barrasFiltrosNavegador.setLayout(new GridLayout(2, true));
			// barrasFiltrosNavegador=shell;
			{
				/*
				 * Barra dos filtros
				 */
				Composite barrasFiltros = new Composite(barrasFiltrosNavegador, SWT.EMBEDDED);
				// panelConfig
				configFrame = SWT_AWT.new_Frame(barrasFiltros);
				barrasFiltros.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				panelConfig = new JPanel(new BorderLayout());
				panelConfigAtual = new JPanel();
				panelConfig.add(panelConfigAtual, BorderLayout.CENTER);
				configFrame.add(panelConfig);
			}
			{
				/*
				 * Barra de navegação
				 */
				Composite compNavegador = new Composite(barrasFiltrosNavegador, SWT.NONE);
				compNavegador.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true, 1, 1));
				compNavegador.setLayout(new GridLayout(5, true));
				//data = new GridData();
				// data.horizontalAlignment = GridData.FILL;

				// *
				itemBack = new Button(compNavegador, SWT.PUSH);
				itemBack.setText(TradPaineisBV.VOLTAR);
				itemBack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				itemForward = new Button(compNavegador, SWT.PUSH);
				itemForward.setText(TradPaineisBV.AVANCAR);
				itemForward.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				itemStop = new Button(compNavegador, SWT.PUSH);
				itemStop.setText(TradPaineisBV.PARAR);
				itemStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				itemRefresh = new Button(compNavegador, SWT.PUSH);
				itemRefresh.setText(TradPaineisBV.ATUALIZAR);
				itemRefresh.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				itemGo = new Button(compNavegador, SWT.PUSH);
				itemGo.setText(TradPaineisBV.IR);
				itemGo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

				Composite compEndereco = new Composite(compNavegador, SWT.PUSH);
				compEndereco.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
				compEndereco.setLayout(new GridLayout(2, false));

				Label labelAddress = new Label(compEndereco, SWT.PUSH);
				labelAddress.setText(TradPaineisBV.ENDERECO);

				location = new Text(compEndereco, SWT.BORDER);
				location.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			}

		}

		Composite compBlur = new Composite(shell, SWT.EMBEDDED);

		try {
			browser = new Browser(shell, SWT.PUSH);
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		} catch (SWTError e) {
			//System.out.println("Could not instantiate Browser: " + e.getMessage());
			return;
		}

		initializeEventos(display, browser);
		/*
		 * Pegar a cor
		 */
		//mouseListener(browser);
		{
			/*
			 * Eventos
			 */
			Listener listener = new Listener() {
				public void handleEvent(Event event) {
					Button item = (Button) event.widget;
					String string = item.getText();
					if (string.equals(TradPaineisBV.VOLTAR))
						browser.back();
					else if (string.equals(TradPaineisBV.AVANCAR))
						browser.forward();
					else if (string.equals(TradPaineisBV.PARAR))
						browser.stop();
					else if (string.equals(TradPaineisBV.ATUALIZAR))
						browser.refresh();
					else if (string.equals(TradPaineisBV.IR))
						browser.setUrl(location.getText());
				}
			};
			browser.addProgressListener(new ProgressListener() {
				public void changed(ProgressEvent event) {
					if (event.total == 0)
						return;
					// int ratio = event.current * 100 / event.total;
					// progressBar.setSelection(ratio);
				}

				public void completed(ProgressEvent event) {
					// progressBar.setSelection(0);
				}
			});
			browser.addStatusTextListener(new StatusTextListener() {
				public void changed(StatusTextEvent event) {
					browser.setData("query", event.text);
				}
			});
			browser.addLocationListener(new LocationListener() {
				public void changed(LocationEvent event) {
					if (event.top)
						location.setText(event.location);
				}

				public void changing(LocationEvent event) {
				}
			});
			itemBack.addListener(SWT.Selection, listener);
			itemForward.addListener(SWT.Selection, listener);
			itemStop.addListener(SWT.Selection, listener);
			itemRefresh.addListener(SWT.Selection, listener);
			itemGo.addListener(SWT.Selection, listener);
			location.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event e) {
					browser.setUrl(location.getText());
				}
			});
		}

		//browser.setUrl("http://www.acessobrasil.org.br");
		// browser.setUrl("http://www.ufv.br/dbg/trab2002/HRSEXO/HRS002.htm");
		// browser.setUrl("http://www.psych.ndsu.nodak.edu/mccourt/Psy460/Color%20Vision/Ishihara%20Plates/ishihara%20plate%2024.JPG");
		browser.setUrl("about:blank");
		// browser.setUrl("http://www.globo.com");
		//browser.setUrl("http://vischeck.com/examples/");

		// Button b = new Button(c, SWT.PUSH);
		// browser.getClientArea()
		/*
		 * Panel da imagem desfocada
		 */
		java.awt.Frame locationFrame = SWT_AWT.new_Frame(compBlur);
		compBlur.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		panelBlur = new javax.swing.JPanel();
		imagemFile = new JLabel();
		panelBlur.setLayout(new BorderLayout());
		panelBlur.add(imagemFile, BorderLayout.CENTER);
		locationFrame.add(panelBlur);

		
		NavegadorPrint np = new NavegadorPrint();
		imagemFile.addMouseListener(np);
		
		// shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		imagemFile = null;
		panelBlur = null;
		np = null;
		//System.out.print("Saindo do aplicativo...\n");
		Runtime.getRuntime().runFinalization();
		//System.out.print("runFinalization ok\n");
		// System.exit(0);
		//System.out.print("dispose...\n");
		display.dispose();
		/*
		 * Mensagem de que o programa terminou
		 */
		//System.out.print("dispose ok\n\0");
		System.exit(0);
	}

	/**
	 * Cria os WindowEvent listeners
	 */
	static void initializeEventos(final Display display, Browser browser) {
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				Shell shell = new Shell(display);
				shell.setText(TradPaineisBV.NOVA_JANELA);
				shell.setLayout(new FillLayout());
				Browser browser = new Browser(shell, SWT.NONE);
				initializeEventos(display, browser);
				event.browser = browser;
			}
		});
		browser.addVisibilityWindowListener(new VisibilityWindowListener() {
			public void hide(WindowEvent event) {
				Browser browser = (Browser) event.widget;
				Shell shell = browser.getShell();
				shell.setVisible(false);
			}

			public void show(WindowEvent event) {
				Browser browser = (Browser) event.widget;
				final Shell shell = browser.getShell();
				/* popup blocker - ignore windows with no style */
				if (!event.addressBar && !event.menuBar && !event.statusBar && !event.toolBar) {
					//System.out.println("Popup blocked.");
					event.display.asyncExec(new Runnable() {
						public void run() {
							shell.close();
						}
					});
					return;
				}
				if (event.location != null)
					shell.setLocation(event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize(shell.computeSize(size.x, size.y));
				}
				shell.open();
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				Browser browser = (Browser) event.widget;
				Shell shell = browser.getShell();
				shell.close();
			}
		});
	}

	public void mouseClicked(MouseEvent e) {
		//System.out.println("X"+e.getXOnScreen());
		//*
		int x=e.getXOnScreen();
		int y=e.getYOnScreen();
		/*/
		int x = e.getComponent().getLocationOnScreen().x+e.getX();
		int y = e.getComponent().getLocationOnScreen().y+e.getY();
		//*/
		BufferedImage bi;
		try {
			bi = new Robot().createScreenCapture(new Rectangle(x, y, 1,1));
			Raster raster = bi.getData();
			int w = raster.getWidth();
			int h = raster.getHeight();
			int[] R = raster.getSamples(0, 0, w, h, 0, (int[]) null);
			int[] G = raster.getSamples(0, 0, w, h, 1, (int[]) null);
			int[] B = raster.getSamples(0, 0, w, h, 2, (int[]) null);
			//System.out.println("R="+R[0]);
			//System.out.println("G="+G[0]);
			//System.out.println("B="+B[0]);
			if(panelContraste!=null){
				panelContraste.setCor(R[0],G[0],B[0]);
			}
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
