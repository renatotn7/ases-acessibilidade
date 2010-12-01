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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import br.org.acessobrasil.ases.entidade.EstadoSilvinha;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.silvinha2.mli.GERAL;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.G_Link;
/**
 * Mostra as imagens com erros 
 *
 */
class TabelaDescricao extends JTable implements MouseListener {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private final PanelDescricaoImagens panelDescricaoImagens;

	TabelaDescricao(PanelDescricaoImagens imagens, ArrayList<FerramentaDescricaoModel> erros) {
		this.panelDescricaoImagens = imagens;
		initComponents(erros);

	}

	void initComponents(ArrayList<FerramentaDescricaoModel> erros) {

		this.panelDescricaoImagens.dtm = new DefaulTableModelNotEditable();
		this.panelDescricaoImagens.dtm.setColumnIdentifiers(new String[] { GERAL.LINHA, GERAL.COLUNA, GERAL.TAG });
		this.addMouseListener(this);
		setModel(this.panelDescricaoImagens.dtm);
		{
			TableColumnModel cm = getColumnModel();
			cm.getColumn(0).setMaxWidth(50);
			cm.getColumn(1).setMaxWidth(50);
		}

		for (FerramentaDescricaoModel flm : erros) {
			addLinha(flm.getLinha(), flm.getColuna(), flm.getTexto());
		}
	}

	public void clearTab() {
		this.panelDescricaoImagens.dtm = new DefaulTableModelNotEditable();
		this.panelDescricaoImagens.dtm.setColumnIdentifiers(new String[] { GERAL.LINHA, GERAL.COLUNA, GERAL.TAG });
		setModel(this.panelDescricaoImagens.dtm);
		{
			TableColumnModel cm = getColumnModel();
			cm.getColumn(0).setMaxWidth(50);
			cm.getColumn(1).setMaxWidth(50);
		}
	}

	public void addLinha(int linha, int coluna, String codigo) {
		try {
			if (this.panelDescricaoImagens.dtm.getRowCount() > 0)
				if (Integer.parseInt((String) this.panelDescricaoImagens.dtm.getValueAt(0, 0)) == 0
						&& Integer.parseInt((String) this.panelDescricaoImagens.dtm.getValueAt(0, 1)) == 0)
					this.panelDescricaoImagens.dtm.removeRow(0);
		} catch (Exception e) {

		}
		try {
			this.panelDescricaoImagens.dtm.addRow(new Object[] { linha, coluna, codigo });
		} catch (Exception e) {

		}
	}

	public void delAtualLinha() {
		this.panelDescricaoImagens.dtm.removeRow(this.getSelectedRow());
	}

	public void selectTag() {

	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			new Thread(new Runnable() {
				public void run() {
					System.gc();
				}
			}).start();
			
			// this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().selectAll();
			this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(new Color(255, 255, 255), new Color(0, 0, 0));
			// this.panelDescricaoImagens.scrollPaneDescricao.coloreSource();

			this.panelDescricaoImagens.tArParticipRotulo.apagaTexto();
			if (this.panelDescricaoImagens.conteudoDoAlt.getText().length() > 0) {
				this.panelDescricaoImagens.aplicar.setEnabled(true);
			}
			this.panelDescricaoImagens.analiseSistematica.setEnabled(true);
			TabelaDescricao tcl = ((TabelaDescricao) e.getComponent());
			int linha = (Integer) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 0);
			int coluna = (Integer) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 1);
			String tag = (String) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 2);
			RegrasHardCodedEmag regra = new RegrasHardCodedEmag();
			String src = regra.getAtributo(tag, "src");
			String fullUrl = null;
			// fullUrl =
			// endImagem(tag,panelDescricaoImagens.enderecoPagina);//this.panelDescricaoImagens.getConteudoSrc(tag);
			try {
				fullUrl = G_Link.getFullPath(EstadoSilvinha.getLinkAtual(), src);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			SetImage setImage = new SetImage(this.panelDescricaoImagens, fullUrl);
			setImage.start();
			int posAtual = 0;
			int posFinal = 0;
			String codHTML = this.panelDescricaoImagens.textAreaSourceCode.getTextPane().getText().replace("\r", "");
			int i;
			for (i = 0; i < (linha - 1); i++) {
				posAtual = codHTML.indexOf("\n", posAtual + 1);
			}
			i = 0;
			// Adaptação provisória
			posFinal = codHTML.indexOf((String) this.panelDescricaoImagens.dtm.getValueAt(tcl.getSelectedRow(), 2), posAtual + coluna);
			while (codHTML.charAt(posFinal + i) != '>') {
				i++;
			}

			this.panelDescricaoImagens.setPosTagRepInit(posFinal);
			this.panelDescricaoImagens.setPosTagRepEnd(posFinal + i + 1);

			this.panelDescricaoImagens.textAreaSourceCode.goToLine(linha);
			this.panelDescricaoImagens.textAreaSourceCode.getTextPane().select(this.panelDescricaoImagens.getPosTagRepInit(), this.panelDescricaoImagens.getPosTagRepEnd());
			this.panelDescricaoImagens.arTextPainelCorrecao.setColorForSelectedText(Color.decode("0xEEEEEE"), new Color(255, 0, 0));
			this.panelDescricaoImagens.arTextPainelCorrecao.setUnderline();
			/*
			 * /
			 * this.panelDescricaoImagens.scrollPaneDescricao.getTextPane().select(200,250); //
			 */

		}

	}

	private String endImagem(String tag, String pagina) {
		// reconverte para o endereço da internet

		// pega o diretorio de onde se encontra a pagina
		pagina = pagina.replaceAll("\\\\", "/");
		int posfinalStrDir = pagina.lastIndexOf("/");
		String dirPagina = pagina.substring(0, posfinalStrDir + 1);
		// System.out.println("dirPagina: " + dirPagina);

		// pega o endereço contido em src
		int posSrcImg = tag.indexOf("src=");
		int posFimEndImg = 0;
		int posIniEndImg = posSrcImg + "src=".length();
		if (tag.charAt(posIniEndImg) == '\"') {
			posIniEndImg++;
			posFimEndImg = tag.indexOf('\"', posIniEndImg);
		} else {
			posFimEndImg = tag.indexOf(" ", posIniEndImg);
			if (posFimEndImg == -1) {
				posFimEndImg = tag.indexOf(">", posIniEndImg);
			}
		}

		String src = tag.substring(posIniEndImg, posFimEndImg);
		/*
		 * if(tag.toLowerCase().indexOf("c:/")!=-1){ //return
		 * G_Link.getFullPath(pagina, src);
		 * pagina.substring(0,pagina.lastIndexOf("/")); }//
		 */
		// System.out.println("source: " + src);
		if (src.indexOf(":\\") != -1 || src.indexOf("://") != -1) {
			// System.out.println("retorno: " + src);

			System.out.print(".");
			return src;
		}
		// combina o endereço com o diretório
		Pattern pattern = Pattern.compile("\\.\\./");
		// Texto onde procurar o regex
		Matcher matcher = pattern.matcher(src);
		int quant = 0;
		while (matcher.find()) {
			quant++;
		}
		String dirPComb = dirPagina;
		for (int i = 0; i <= quant; i++) {
			dirPComb = dirPComb.substring(0, dirPComb.lastIndexOf("/"));
		}
		dirPComb += "/";
		String srcPComb = src;
		for (int i = 0; i < quant; i++) {
			srcPComb = srcPComb.substring(srcPComb.indexOf("../") + "../".length());
		}

		pattern = Pattern.compile("\\./");
		// Texto onde procurar o regex
		matcher = pattern.matcher(srcPComb);
		quant = 0;
		while (matcher.find()) {
			quant++;
		}

		for (int i = 0; i < quant; i++) {

			srcPComb = srcPComb.substring(srcPComb.indexOf("./") + ".".length());
		}

		// System.out.println("quant: " + quant);
		// System.out.println("dirPComb: " + dirPComb);
		// System.out.println("srcPComb: " + srcPComb);
		// System.out.println("retorno: " + dirPComb + srcPComb);

		System.out.print(".");
		return dirPComb + srcPComb;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

}

class SetImage extends Thread {

	private PanelDescricaoImagens ferramentaDescricaoImagens;

	private String fullUrl;

	public SetImage(PanelDescricaoImagens ferramentaDescricaoImagens, String url) {
		this.ferramentaDescricaoImagens = ferramentaDescricaoImagens;
		this.fullUrl = url;
	}

	public void run() {
		try {
			this.ferramentaDescricaoImagens.parentFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			G_File arq = new G_File("temp/img.html");
			String myHTML = "<html><head></head><body><img src=\"" + fullUrl + "\" /></body></html>";
			arq.write(myHTML);
			//G_Cronometro cro = new G_Cronometro();
			//cro.start();
			this.ferramentaDescricaoImagens.imagemSemDesc.setDocument(arq.getFile());
			// this.ferramentaDescricaoImagens.imagemSemDesc.setPage("file:///"+arq.getFile().getAbsolutePath());
			//System.out.println("fullUrl " + fullUrl);
			// System.out.println("visivel " +
			// this.ferramentaDescricaoImagens.imagemSemDesc.isVisible());
			// System.out.println("isDisplayable " +
			// this.ferramentaDescricaoImagens.imagemSemDesc.isDisplayable());
			// this.ferramentaDescricaoImagens.parentFrame.setVisible(true);
			//cro.stop("Imagem carregada");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.ferramentaDescricaoImagens.parentFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
