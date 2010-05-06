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

package br.org.acessobrasil.ases.ferramentas_de_reparo.vista.imagem.analise_geral;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import br.org.acessobrasil.silvinha.vista.frames.classePrincipal.FrameSilvinha;
import br.org.acessobrasil.silvinha2.util.G_File;
import br.org.acessobrasil.silvinha2.util.TxtBuffer;
/**
 *	Salva as alterações feitas 
 */
public class SalvaAlteracoes {

	private String nomeDoDiretorioEmDisco;

	private JTextPane jtextpane;

	private JButton salvar;

	private JMenuItem btnSalvar;

	private boolean textoModificado;

	private FrameSilvinha parentFrame;

	private String conteudo;

	private int escolha;

	private ArrayList<JButton> arrSalvar;

	public SalvaAlteracoes(JTextPane jtextpane, JButton salvar,
			JMenuItem btnSalvar, FrameSilvinha parentFrame) {

		this.jtextpane = jtextpane;
		this.salvar = salvar;
		this.btnSalvar = btnSalvar;
		this.parentFrame = parentFrame;
		this.textoModificado = false;
	}

	public SalvaAlteracoes(FrameSilvinha parentFrame, String conteudo) {
		this.conteudo = conteudo;
		this.parentFrame = parentFrame;
		this.textoModificado = false;
	}

	public SalvaAlteracoes() {

	}

	// fazer o frame que salva deve trabalhar o foco

	public SalvaAlteracoes(JTextPane jtextpane, ArrayList<JButton> salvar,
			JMenuItem btnSalvar, FrameSilvinha parentFrame) {

		this.jtextpane = jtextpane;
		this.arrSalvar = salvar;
		this.btnSalvar = btnSalvar;
		this.parentFrame = parentFrame;
		this.textoModificado = false;
	}

	public void sair() {

		Sair sair = new Sair();
		sair.start();

	}

	public void setAlterado() {
		if (jtextpane != null)
			conteudo = jtextpane.getText();
		if (salvar != null)
			salvar.setEnabled(true);
		if (arrSalvar != null) {
			for (JButton b : arrSalvar) {
				b.setEnabled(true);
			}
		}
		if (btnSalvar != null)
			btnSalvar.setEnabled(true);
		textoModificado = true;
	}

	// se for salvar e cancelar é uma coisa sair é outra
	public void cancelar() {
		Cancelar cancelar = new Cancelar();
		cancelar.start();
	}

	public void salvarComo() {
		if (jtextpane != null)
			conteudo = jtextpane.getText();
		G_File arq = new G_File("");
		String filtro[] = { "" };
		if (arq.openDialogSaveAs(conteudo, filtro,parentFrame)) {
			if (salvar != null)
				salvar.setEnabled(false);
			if (arrSalvar != null) {
				for (JButton b : arrSalvar) {
					b.setEnabled(false);
				}
			}
			if (btnSalvar != null)
				btnSalvar.setEnabled(false);
		}
	}

	// salvarComo();
	private void salvar(String texto) {
		if (nomeDoDiretorioEmDisco == null) {
			G_File arq = new G_File("");
			String filtro[] = { "" };
			if (arq.openDialogSaveAs(texto, filtro,parentFrame)) {
				if (salvar != null)
					salvar.setEnabled(false);
				if (arrSalvar != null) {
					for (JButton b : arrSalvar) {
						b.setEnabled(false);
					}
				}
				if (btnSalvar != null)
					btnSalvar.setEnabled(false);
				textoModificado = false;
				nomeDoDiretorioEmDisco = arq.getFile().getAbsolutePath();
			}

			// System.out.println(nomeDoArquivoEmDisco);
		} else {
			G_File arq = new G_File(nomeDoDiretorioEmDisco);
			arq.write(texto);
			if (salvar != null)
				salvar.setEnabled(false);
			if (arrSalvar != null) {
				for (JButton b : arrSalvar) {
					b.setEnabled(false);
				}
			}
			if (btnSalvar != null)
				btnSalvar.setEnabled(false);
			textoModificado = false;
		}
	}

	/**
	 * Salva todas as paginas que foram modificadas
	 * @param enderecos links
	 * @param hashCode codigo
	 */
	public void salvar(ArrayList<String> enderecos, ArrayList<String> hashCode) {
		File diretorio;
		if (nomeDoDiretorioEmDisco == null) {
			JFileChooser fc = new JFileChooser();

			// restringe a amostra a diretorios apenas
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int res = fc.showSaveDialog(null);

			if (res == JFileChooser.APPROVE_OPTION) {
				diretorio = fc.getSelectedFile();
				nomeDoDiretorioEmDisco = diretorio.getAbsolutePath();
				JOptionPane.showMessageDialog(null,
						"Voce escolheu o diretório: " + diretorio.getName());
				int i = 0;
				for (String endereco : enderecos) {
					
					String nomeArq = endereco.substring(endereco.lastIndexOf("/"), endereco.length());
					if(nomeArq==null || nomeArq.equals("")){
						nomeArq="index.html";
					}else{
						//tratar caracteres especiais
						nomeArq = G_File.url2path(nomeArq) + ".html";
					}
					int begin = endereco.indexOf(":/") + 1;
					if (endereco.charAt(begin + 1) == '/') {
						begin++;
					}
					
					//Ex: endFinal=www.gmail.com/pasta/subpasta
					String endFinal = endereco.substring(begin, endereco.lastIndexOf("/"));
					//Trata caracteres especiais
					endFinal = G_File.url2path(endFinal);
					System.out.println(endFinal);
					File dir = new File(nomeDoDiretorioEmDisco + "/" + endFinal);
					if (!dir.isDirectory()) {
						dir.mkdirs();
					}
					TxtBuffer.setHashCode(hashCode.get(i));
					i++;
					new G_File(nomeDoDiretorioEmDisco + "/" + endFinal + "/"
							+ "End_" + nomeArq.substring(1) + ".txt")
							.write(endereco);

					new G_File(nomeDoDiretorioEmDisco + "/" + endFinal + "/"
							+ nomeArq).write(TxtBuffer.getContent());

				}
				// rotina para salvar

				if (salvar != null)
					salvar.setEnabled(false);
				if (arrSalvar != null) {
					for (JButton b : arrSalvar) {
						b.setEnabled(false);
					}
				}
				if (btnSalvar != null)
					btnSalvar.setEnabled(false);
				textoModificado = false;

			} else
				JOptionPane.showMessageDialog(null,
						"Voce nao selecionou nenhum diretorio.");

		}

		// System.out.println(nomeDoArquivoEmDisco);
		else {
			// rotina para salvar
			G_File arq = new G_File(nomeDoDiretorioEmDisco);
			arq.write(conteudo);
			if (salvar != null)
				salvar.setEnabled(false);
			if (arrSalvar != null) {
				for (JButton b : arrSalvar) {
					b.setEnabled(false);
				}
			}
			if (btnSalvar != null)
				btnSalvar.setEnabled(false);
			textoModificado = false;
		}
	}

	private class Cancelar extends Thread {

		public void run() {
			escolha = 9999;
			if (textoModificado) {
				// System.out.println("texto modificado: "+textoModificado);
				escolha = JOptionPane.showConfirmDialog(null,
						"Deseja salvar as alterações?");// new
														// SalvarAlteracoesVisao();
				// System.out.println("joptionpane: "+escolha);
				// System.out.println("depois");
				if (escolha == 0) {
					// System.out.println(escolha);
					salvar(conteudo);
					parentFrame.showLastActivePanel();
				} else if (escolha == 1) {
					// System.out.println("escolha 1");
					parentFrame.showLastActivePanel();
				} else if (escolha == 2) {
					// System.out.println("escolha 2 ou 9999");

				}
			} else {
				parentFrame.showLastActivePanel();
			}
			// System.out.println(escolha);

		}

	}

	private class Sair extends Thread {

		public void run() {
			escolha = 9999;
			if (textoModificado) {
				escolha = JOptionPane.showConfirmDialog(null,
						"Deseja salvar as alterações?");

				if (escolha == 0) {
					salvar(conteudo);
					System.exit(0);
				} else if (escolha == 1) {
					System.exit(0);
				}

			} else {
				System.exit(0);
			}

		}
	}

	public JMenuItem getBtnSalvar() {
		return btnSalvar;
	}

	public void setBtnSalvar(JMenuItem btnSalvar) {
		this.btnSalvar = btnSalvar;
		if (textoModificado == true) {
			setAlterado();
		}
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public JTextPane getJtextpane() {
		return jtextpane;
	}

	public void setJtextpane(JTextPane jtextpane) {
		this.jtextpane = jtextpane;
	}

	public String getNomeDoDiretorioEmDisco() {
		return nomeDoDiretorioEmDisco;
	}

	public void setNomeDoDiretorioEmDisco(String nomeDoArquivoEmDisco) {
		this.nomeDoDiretorioEmDisco = nomeDoArquivoEmDisco;
	}

	public FrameSilvinha getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(FrameSilvinha parentFrame) {
		this.parentFrame = parentFrame;
	}

	public JButton getSalvar() {
		return salvar;
	}

	public void setSalvar(JButton salvar) {

		this.salvar = salvar;
		if (textoModificado == true) {
			setAlterado();
		}
	}

	public static void main(String[] Args) {
		new SalvaAlteracoes();
	}

}
