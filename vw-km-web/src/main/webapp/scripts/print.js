 function friendlyPrint() {
	var disp_setting="toolbar=yes,location=no,directories=no,menubar=no,";
	disp_setting+="scrollbars=yes,width=800, height=600";
	var content_value = document.getElementById("f:dividerpanelSecond").innerHTML;
	var uniqueName = new Date();
	var windowName = 'Print' + uniqueName.getTime();
	var docprint=window.open("",windowName,disp_setting);
	
	docprint.document.write( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" );
	docprint.document.open("text/html");
	docprint.document.write('<html><head>');
	docprint.document.write('<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\" />');
	docprint.document.write('<link rel='+'"STYLESHEET"'+'type='+'"text/css"'+ 'href='+'"../styles/_import_styles.css"'+'/>');
	docprint.document.write('<link rel='+'"STYLESHEET"'+'type='+'"text/css"'+ 'href='+'"../styles/zz-htmlMockup-specific.css"'+'/>');
	docprint.document.write('<link rel='+'"STYLESHEET"'+'type='+'"text/css"'+ 'href='+'"../styles/updade_eKm.css?v=3-2-1-7"'+'/>');
	docprint.document.write('<link rel='+'"STYLESHEET"'+'type='+'"text/css"'+ 'href='+'"../styles/xp.css"'+'/>');
	docprint.document.write('<!--[if lte IE 7]><link rel='+'"STYLESHEET"'+'type='+'"text/css"'+ 'href='+'"../styles/updade_eKm_ie7.css"'+'/><![endif]-->');
	docprint.document.write('<link rel='+'"STYLESHEET"'+'type='+'"text/css"'+ 'href='+'"../styles/now_print.css"'+'/>');
	docprint.document.write("</head>");
	docprint.document.write("<body class=\"htmlMockup usp_showMenuOnHover usp_contrast lang_de usp_ExtentContentFull ExtentContentFull usp_ExtendSubnav ExtendSubnav\">");
	docprint.document.write('<div id='+'"areaContainer"'+'>');
	docprint.document.write(content_value);
	docprint.document.write("</div>");
	docprint.document.write("</body></html>");
	docprint.document.close();
	docprint.focus();
	docprint.print();
	window.location.reload();
	docprint.close();
}	