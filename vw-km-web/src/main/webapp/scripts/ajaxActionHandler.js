
function getAjaxUrl() {
	var ajaxUrl = '' + document.location;
	if (hasBodyClass ("fw_dotNet"))
	{
		ajaxUrl = (ajaxUrl.split("/NewadaOnWeb/"))[0] + "/NewadaOnWeb/AjaxDataService/AjaxAutoCompleter.aspx";
	}
	else
	{
		ajaxUrl = (ajaxUrl.split("/newadaJ/"))[0] + "/newadaJ/ajax/ajaxBase.jsa";		
	}
	return ajaxUrl;
}

function getPageName() {
	var pageName = '' + document.location;
	var suffix = ".jsf";
	if (hasBodyClass ("fw_dotNet"))
	{
		suffix = ".aspx";
	}
	pageName = (pageName.split(suffix))[0];
	return pageName.substr(pageName.lastIndexOf("/") + 1);
}

function getPageId() {
	var pageName = '' + document.location;
	var suffix = ".jsf";
	if (hasBodyClass ("fw_dotNet"))
	{
		suffix = ".aspx";
	}
	pageName = (pageName.split(suffix))[0];
	var pn1 = pageName.substr(0, pageName.lastIndexOf("/"));
	return pn1.substr(pn1.lastIndexOf("/") + 1) + "_" + getPageName();
}

function ajaxLoadSitemap(oThis)
{

	if (hasBodyClass ("fw_dotNet"))
	{
		var service = getAjaxUrl();
		var extraParams = $H({ajaxClassNames:oThis.className,ajaxFieldId:oThis.identify()})
		if (getTagName(oThis) != 'A') {
			extraParams.set('ajaxFieldValue', $F(oThis));
		}
		ajaxLoadContent(service, '', extraParams);		
	}

}

function ajaxLinkAction(oThis) {
	var extraParams = $H({ajaxClassNames:oThis.className,ajaxFieldId:oThis.identify()})
	var tagName = getTagName(oThis);
	if (tagName != 'A' && tagName != 'DIV' && tagName != 'SPAN') {
		extraParams.set('ajaxFieldValue', $F(oThis));
	}
	
	ajaxLoadContent(getAjaxUrl(), '', extraParams);
}

//function loadMessages() {
//	var extraParams = $H({ajaxClassNames:'ajaxAction_loadMessages:areaError',ajaxFieldId:'none'})
//	ajaxLoadContent(getAjaxUrl(), '', extraParams);
//}

// ajax suggestion Box
function ajaxAutoSuggestAction(event, oThis) {
	getForm().onsubmit = function() { return false; }
	var suggBox = $('suggestionBox');
	var nKeyCode = keyCodeFromEvent(event);

	switch (nKeyCode) {
		case Event.KEY_UP:
		case Event.KEY_DOWN:
			break;
		case Event.KEY_TAB:
		case Event.KEY_ESC:
			if (IsDefined(suggBox)) {
				suggBox.down('.autoCompleteMenu').hide();
			}
			getForm().onsubmit = function() { return true; }
			break;
		case Event.KEY_RETURN:
			// dv30kml, 2010-08-10: nur ENTER, wenn Box auch sichtbar ist !!
			if (suggBox==null || IsDefined(suggBox)==false || IsVisibleElement(suggBox.down('.autoCompleteMenu'))==false){
				return false;
			}
			var oSelected = suggBox.down('li.selected');
			if (IsDefined(oSelected)) {
				mouse_handling_onclick(oSelected, oThis);
			}
			break;
		default:
			nowFieldValidatorOnKeyup($(oThis));
			
			var classNames = oThis.className;
			var extraParams = $H({ajaxClassNames:classNames,ajaxFieldValue:$F(oThis),ajaxFieldId:oThis.identify()});

			// dv30kml: special handling for HSN/TSN
			if (classNames.indexOf("referHSN") > -1) {
				extraParams = handleHsnTsn(oThis, extraParams);
			}
			
			if ($('_ctl0_txtNewadaState')) {
				extraParams.set('ajaxNewadaState', $F('_ctl0_txtNewadaState'));
			}
			ajaxLoadContent(getAjaxUrl(), '', extraParams);
	}
	return false;
}

function runThroughSuggBox(event) {
	var nKeyCode = keyCodeFromEvent(event);
	switch (nKeyCode) {
		case Event.KEY_UP:
			selectSuggBoxItem(true, 'suggestionBox');
			break;
		case Event.KEY_DOWN:
			selectSuggBoxItem(false, 'suggestionBox');
			break;
	}
}

function selectSuggBoxItem(bUp, xControl) {
	if($$('#' + xControl + ' li').length < 1) {
		return;
	}
	
	var oSelected = $$('#' + xControl + ' li.selected');
	
	if (oSelected.length > 0) {
		oSelected = oSelected.first();
		if (bUp) {
			if(IsDefined(oSelected.previous('li'))) {
				oSelected.removeClassName('selected');
				oSelected = oSelected.previous('li');
				oSelected.addClassName('selected');
			}
		} else {
			if(IsDefined(oSelected.next('li'))) {
				oSelected.removeClassName('selected');
				oSelected = oSelected.next('li');
				oSelected.addClassName('selected');
			}
		}
		//oSelected.scrollIntoView(false);
	} else {
		oSelected = $$('#' + xControl + ' li');
		oSelected.first().addClassName('selected');
	}
	
	$$('#' + xControl + ' li.selected').first().up('ul').addClassName('keyboard');
}

function mouse_handling_Init(divId, ajaxFieldId) {
	var bIsMouseInJ = false;
	
	$(divId).select('li').each(function(el){
		el.onmouseover = function() {
			var ulElement = $(this).up('ul');
			ulElement.removeClassName('keyboard');
			var selectedEle = ulElement.down('li.selected');
			if (selectedEle) {
				selectedEle.removeClassName('selected');
			}
		}
	});
	
	$(divId).onmouseout = function() {
		bIsMouseInJ = false;
	}
	
	$(divId).onmouseover = function() {
		bIsMouseInJ = true;
	}
	
	$(ajaxFieldId).onblur = function() {
		if (!bIsMouseInJ) {
			$(divId).down('.autoCompleteMenu').hide();
			getForm().onsubmit = function() { return true; }
		}
	}
	
	var liArray = $$('#' + divId + ' li');
	if (liArray.length > 0) {
		$(divId).down('.autoCompleteMenu').show();
		
		liArray.each(function(el) {
			el.onclick=function(evt) {
				mouse_handling_onclick(this, ajaxFieldId);
			};
		});
	}
}

function performSpecialRenderer(oThis, ajaxFieldId, funcName) {
	var hasRenderer = false;
	if (("string" == typeof (funcName)) && funcName.length > 0)
	{
		try
		{
			funcName = (funcName.split(':'))[0];			
			if ("function" == typeof (eval(funcName))) {
				eval(funcName +"($(oThis), $(ajaxFieldId))");
				hasRenderer = true;
			}
		} catch(e) {
		}
	}
	return hasRenderer;
}

function mouse_handling_onclick(oThis, ajaxFieldId)
{
	// spezial-renderer (prefix renderer_FUNCNAME) aufrufen, falls definiert
	if (!performSpecialRenderer(oThis, ajaxFieldId, getClassValue($(ajaxFieldId).className, "renderer_")))
	{
		// spezial renderer zur ajaxAction aufrufen, falls definiert (der actuib wird assume vorangestellt)
		// Bsp. zur ajaxAction_loadPreVehicleList:suggestionBox
		//   kann eine Funktion mit dem Namen assumeloadPreVehicleList definiert werden
		if (!performSpecialRenderer(oThis, ajaxFieldId, "assume" + getClassValue($(ajaxFieldId).className, "ajaxAction_")))
		{
			performBaseRenderer (oThis, ajaxFieldId);
		}
	}
	oThis.up('.autoCompleteMenu').hide();
	getForm().onsubmit = function() { return true; }
}


function ajaxClientUpdate(clientId, content, type) {
	var cId = $(clientId);
	
	if(!IsDefined(cId)) {
		getForm().insert("<div id='"+clientId+"'></div>");
		cId = $(clientId);
	}
	
	switch (type) {
		case "append":
			cId.insert({bottom: content});
			break;
		case "insert":
			cId.insert({top: content});
			break;
		case "replace":
			cId.replace(content);
			break;
		case "update":
		default:
			cId.update(content);
	}
  if ("function" == typeof (richedit_enable)) {richedit_enable ();}

}

function setElementPosition(source, target) {
	Position.clone($(source), $(target), { setHeight: false, offsetTop: $(source).offsetHeight });
}
