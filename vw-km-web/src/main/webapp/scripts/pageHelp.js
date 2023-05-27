
function pageHelp_onclick (oThis)
{
	// if (hasBodyClass("usp_pageHelp"))
	{
		var pageName = getPageId();
		// now_debug_out ("calling pageHelp for page " + pageName);
		var service = getAjaxUrl();
		var extraParams = $H({ajaxClassNames:oThis.className,ajaxPageId:pageName,ajaxTitle:oThis.innerHTML,ajaxFieldId:oThis.identify()})
		ajaxLoadContent(service, '', extraParams);		
	}
	return false;
}
function pageHelp_updateContent (oThis)
{
	// if (hasBodyClass("usp_pageHelp"))
	{

	  var oTxt = $$("textarea[name='pageHelpEditContent']");
	  var editTxt = "";
	  if (IsDefined(oTxt))
	  {
	    editTxt = $F(oTxt.first());
	  }
	  	var pageName = getPageId();
		// now_debug_out ("calling save pageHelp for page " + pageName);
		var service = getAjaxUrl();
		var extraParams = $H({ajaxClassNames:oThis.className,ajaxPageId:pageName,ajaxTitle:oThis.innerHTML,ajaxFieldId:oThis.identify(),ajaxEditText:editTxt})
		ajaxLoadContent(service, '', extraParams);		
	}
	return false;
}

function pageHelp_init ()
{
	$("contentHeadline").onclick = function()
	{
    oThis = $(this);
    oThis.addClassName ("ajaxAction_getPageHelp:pageHelpWinId usp_PageHelpWin ajaxReturnType_dragWindow helpView screenCenter");
		return pageHelp_onclick ($(this));
	}
	
}
