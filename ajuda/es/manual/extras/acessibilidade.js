<!--

   function CVabre(v){
	   var op = MM_findObj(v+"_open");
	   var cl = MM_findObj(v+"_close");
	   if (op.style.display=="none"){
			op.style.display="";
			cl.style.display="none";
	   }else{
			op.style.display="none";
			cl.style.display="";
	   }
   }

/**
* Script que gerencia cookies.
*/

/** Funcao que recupera valores no cookie */
function get_cookie(Name) {
   var search = Name + "=";
   var returnvalue = "";

   if (document.cookie.length > 0) {
       offset = document.cookie.indexOf(search);
       if (offset != -1) {
           offset += search.length
           end = document.cookie.indexOf(";", offset);
           if (end == -1) {
			   end = document.cookie.length;
		   }
           returnvalue = unescape(document.cookie.substring(offset, end));
      }
   }

   return returnvalue;
}

/** Funcao que armazena valores no cookie */
function set_cookie(nome, valor) {
  	var date = new Date();
  	date.setTime(date.getTime() + 1172800);
  	document.cookie = nome + "=" + valor + ";expires=" + date.toGMTString();
}

function clearDefault(el,v) {
  if (v==el.value) el.value = "";
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_setTextOfLayer(objName,x,newText) { //v4.01
  if ((obj=MM_findObj(objName))!=null) with (obj)
    if (document.layers) {document.write(unescape(newText)); document.close();}
    else innerHTML = unescape(newText);
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


var DOM2=document.getElementById
var valorinicial;

function zoom(Marcos){
	if (Marcos.indexOf('mais')==0) valorinicial=valorinicial+0.1;
	if (Marcos.indexOf('menos')==0) valorinicial=valorinicial-0.1;
	if (Marcos.indexOf('padrao')==0){
		//verificar se o zoom já está como 1
		if (valorinicial==1){
			//já está com 1, ver o estilo de cor
			var title=get_cookie("style");
			if (title!="normal"){
				//mudar os estilos para o normal
				setActiveStyleSheet("normal");
			}
		}else{
			//mudar primeiro o zoom
			valorinicial=1;
		}
	}
	if (DOM2){
		mudarTamanho(valorinicial);
		set_cookie("mudaL",valorinicial);
	}
}
function initzoom(valor){
	var v=get_cookie("mudaL");
	if (v!="" && v!="NaN" && v!=null && v!=undefined){
		valor=v*1;
	}
	mudarTamanho(valor);
	valorinicial = valor;
	if(css_btn!="")MM_swapImage(css_btn,'','extras/Ocor.gif',1);
	trocalogo();
	MarcaLink();
	MediaPB();
	MM_preloadImages('extras/Ocor.gif');
}

function MediaPB(){
	//Troca a imagem para PB
	//Desabilitado temporariamente por falta da GD 2.0
	//*
	var imgkey="download.php?id=";
	var pbkey="&pb=s";
	var arr=document.getElementsByTagName("img");
	var i;
	if(css_title!="normal"){
		for(i=0;i<arr.length;i++){
			var pth=arr[i].src;
			if (pth.indexOf(imgkey)!=-1 && pth.indexOf(pbkey)==-1){
				arr[i].src=pth+pbkey;
			}
		}
	}else{
		for(i=0;i<arr.length;i++){
			var pth=arr[i].src;
			if (pth.indexOf(imgkey)!=-1 && pth.indexOf(pbkey)!=-1){
				var arq=pth.split(pbkey);
				arr[i].src=arq[0];
			}
		}
	}
	//*/
}

function MarcaLink(){
	//passar por todos os links
	// *
	var stl="underline";
	if (css_title!="normal"){
		stl="underline";
	}
	var lnk=document.location.href;
	var cont=document.getElementById("sidebar");
	var arr=cont.getElementsByTagName("a");
	var i;
	for(i=0;i<arr.length;i++){
		if (lnk==arr[i].href){
			arr[i].style.textDecoration=stl;
			parent.lastlink=arr[i].href;
			return true;
		}
	}
	if (lnk.indexOf("&query=")!=-1 || lnk.indexOf("archivelist=")!=-1 ){
		parent.lastlink="";
		return true;
	}
	//não achou então procurar no último
	/*/alert(parent.lastlink);
	for(i=0;i<arr.length;i++){
		if (parent.lastlink==arr[i].href){
			arr[i].style.background=cor;
			return true;
		}
	}
	//*/
}

function elemento(elemento , valor){
	//alert(elemento);
	//alert(valor);
	if (DOM2){
		document.getElementById(elemento).style.fontSize=valor+"em";
		//document.getElementById(elemento).innerHTML;
	}
}
function FontTag(v,valor){
	var arr=document.getElementsByTagName(v);
			var i;
			for(i=0;i<arr.length;i++){
				arr[i].style.fontSize=valor+"em";
			}
}
function mudarTamanho(valor){


		if (DOM2){
			//elemento("tipoTexto" , (valor-.25));
			//elemento("atalhos" , (valor));
			//elemento("wrap" , valor);
			//elemento("MenuPrincipal" , (valor-.3));
			//elemento("MenuParaIE" , (valor));
			//elemento("conteudo" , (valor));
			FontTag("body",valor);
			//elemento("barrazoom" , 1);
			//FontTag("h2",(valor*3)+0.5-(valor*valor+1));
			//FontTag("h3",valor+0.15);
			//FontTag("h4",valor+0.0);
			//FontTag("h5",valor-0.05);

			//parabola cresce depois diminui :-)
			//FontTag("h3",(valor*3)+0.15-(valor*valor+1));
	 }
}

/**
* Script que troca o estilo do site.
*/

/* Define qual será o estilo ativo */

function setActiveStyleSheet(title) {
    var i, a, main;
    if (title==get_cookie("style")){
		title="normal";
    }
    for (i=0; (a = document.getElementsByTagName("link")[i]); i++) {
        if (a.getAttribute("rel") && a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
            a.disabled = true;
            if (a.getAttribute("title") == title) {
                a.disabled = false;
				set_cookie("style", title);
            }
        }
    }

	for (i=0;i<a_css.length;i++){
		if (title==a_css_t[i] && title!="normal" && title!=null){
			MM_swapImage(a_css_b[i],'','extras/Ocor.gif',1);
		}else if(a_css_b[i]!=""){
			MM_swapImage(a_css_b[i],'','extras/' + a_css_b[i] + '.gif',1);
		}
	}
	css_title=title;
	trocalogo();
	MarcaLink();
	MediaPB();
}

function trocalogo(){
	/*alterar o logo
	if (css_title=="normal"){
		MM_swapImage('img_logo_cli','','images/logo_cli.gif',1);
	}else if (css_title=="branco_e_preto_acessivel"){
		MM_swapImage('img_logo_cli','','images/logo_cli_bp.gif',1);
	}else if (css_title=="preto_e_branco_acessivel"){
		MM_swapImage('img_logo_cli','','images/logo_cli_pb.gif',1);
	}
	*/
}

/* Define recupera o estilo ativo */
function getActiveStyleSheet() {
    var i, a;

    for (i=0; (a = document.getElementsByTagName("link")[i]); i++) {
        if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title") && !a.disabled) {
			return a.getAttribute("title");
		}
     }

  return null;
}

/* Recupera o estilo default */
function getPreferredStyleSheet() {
    var i, a;
    for (i=0; (a = document.getElementsByTagName("link")[i]); i++) {
        if (a.getAttribute("rel").indexOf("style") != -1
            && a.getAttribute("rel").indexOf("alt") == -1
            && a.getAttribute("title")) {

		    return a.getAttribute("title");
	    }
    }

    return null;
}

function dw(v){
	document.write(v);
}

var cookie = get_cookie("style");
var css_title = (cookie != "") ? cookie : getPreferredStyleSheet();
var a_css=new Array("normal.css","fundoBranco.css","fundoPreto.css");
var a_css_t=new Array("normal","branco_e_preto_acessivel","preto_e_branco_acessivel");
var a_css_b=new Array("","Obp","Opb");
var css_btn;
var i;
for (i=0;i<a_css.length;i++){
	if (css_title==a_css_t[i] || css_title=="" || css_title==null){
		css_btn=a_css_b[i];
		css_title=a_css_t[i];
		dw('<link rel="stylesheet" type="text/css" href="images/'+a_css[i]+'" media="screen" title="'+a_css_t[i]+'" />');
	}else{
		dw('<link rel="alternate stylesheet" type="text/css" href="images/'+a_css[i]+'" media="screen, projection" title="'+a_css_t[i]+'" />');
	}
}
window.onload = function(){
	initzoom(1);
}

//-->