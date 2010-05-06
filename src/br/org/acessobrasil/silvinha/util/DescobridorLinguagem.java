package br.org.acessobrasil.silvinha.util;

import br.org.acessobrasil.silvinha.entidade.RelatorioDaUrl;
import br.org.acessobrasil.silvinha2.util.G_File;

public class DescobridorLinguagem {
	private G_File html = new G_File(RelatorioDaUrl.pathHD+"paginas-html.txt");
	private G_File php = new G_File(RelatorioDaUrl.pathHD+"paginas-php.txt");
	private G_File asp = new G_File(RelatorioDaUrl.pathHD+"paginas-asp.txt");
	private G_File aspx = new G_File(RelatorioDaUrl.pathHD+"paginas-aspx.txt");
	private G_File struts1 = new G_File(RelatorioDaUrl.pathHD+"paginas-struts1.txt");
	private G_File struts2 = new G_File(RelatorioDaUrl.pathHD+"paginas-struts2.txt");
	private G_File cgi = new G_File(RelatorioDaUrl.pathHD+"paginas-cgi.txt");
	private G_File jsp = new G_File(RelatorioDaUrl.pathHD+"paginas-jsp.txt");
	private G_File indefinido = new G_File(RelatorioDaUrl.pathHD+"paginas-indefinido.txt");
	
	public DescobridorLinguagem(){
		
	}
	public void limparLogs(){
		//limpar os arquivos
		php.write("");
		asp.write("");
		aspx.write("");
		struts1.write("");
		struts2.write("");
		cgi.write("");
		jsp.write("");
		indefinido.write("");
		html.write("");
	}
	/**
	 * Tenta prever o tipo de linguagem utilizada no site
	 * @param link
	 */
	public void descobrirLinguagem(String link){
		if(testarExtensao(link,".php")){
			php.append(link+"\n");
		}else if(testarExtensao(link,".asp")){
			asp.append(link+"\n");
		}else if(testarExtensao(link,".aspx")){
			aspx.append(link+"\n");
		}else if(testarExtensao(link,".jsp")){
			jsp.append(link+"\n");
		}else if(testarExtensao(link,".do")){
			struts1.append(link+"\n");
		}else if(testarExtensao(link,".action")){
			struts2.append(link+"\n");
		}else if(testarExtensao(link,".exe") || link.contains("/cgi-bin/")){
			cgi.append(link+"\n");
		}else if(testarExtensao(link,".html") || testarExtensao(link,".htm")){
			html.append(link+"\n");
		}else{
			indefinido.append(link+"\n");
		}
	}
	private boolean testarExtensao(String url,String extensao){
		if(url.endsWith(extensao) || url.indexOf(extensao+"?")!=-1  || url.indexOf(extensao+"#")!=-1){
			return true;
		}else{
			return false;
		}
	}
}
