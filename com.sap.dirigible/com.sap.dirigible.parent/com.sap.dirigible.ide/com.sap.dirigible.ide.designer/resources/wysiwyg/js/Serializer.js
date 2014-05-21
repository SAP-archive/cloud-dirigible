function save(){

	var iframe = document.querySelector("iframe");
	var style = iframe.contentDocument.querySelector("#dirigible-wysiwyg-patches");
	style.parentNode.removeChild(style);
	return iframe.contentDocument.querySelector("html").outerHTML;
}