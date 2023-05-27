
g_nowCurErrorMessageId = "";

function fnAfterErrorMessageHighlight ()
{
  if (IsDefined(g_nowCurErrorMessageId))
  {
    if ("function" == typeof (nowValidatorApply))
    {
      nowValidatorApply($(g_nowCurErrorMessageId));
    }
    SetFocus(g_nowCurErrorMessageId);
  }
  g_nowCurErrorMessageId = "";
}

function fnAfterErrorMessageScroll ()
{
  if (IsDefined(g_nowCurErrorMessageId))
  {
    $(g_nowCurErrorMessageId).removeClassName("invalid");
    // new Effect.Highlight(g_nowCurErrorMessageId, {startcolor: '#F6E5BC', endcolor: '#FFEEEE', afterFinish:fnAfterErrorMessageHighlight});
		if (!IsReadOnly($(g_nowCurErrorMessageId)))
		{
			SetFocus(g_nowCurErrorMessageId);
		}
  }
}

function nowErrorMessagesScrollTo (oElem, scrollDuration)
{
	oElem = $(oElem);
	g_nowCurErrorMessageId = oElem.identify();
	new Effect.ScrollTo(oElem,{offset:-4, duration:scrollDuration, afterFinish:fnAfterErrorMessageScroll});
	return false;
}

function nowErrorMessagesInit ()
{
	$$('.areaError li label[for]').each(function(el)
  {
    var theId = el.readAttribute('for');
		if (theId && "" != theId && IsDefined($(theId)))
	  {
			el.onclick = function()
			{
				var theId = $(this).readAttribute('for');
				return nowErrorMessagesScrollTo (theId, g_scrollDuration);
			}
		}
		else
		{
			el.addClassName("missingId");
		}
  })
}

function nowErrorGetNextErrorField (oThis)
{
	var errFld = null;
	
	var errWin = $("errReportWin");
	if (IsDefined (errWin))
	{
	  var id = oThis.identify();
		lblId = errWin.down("div.content ul li.lastFocus label[for]");
		// lblId = errWin.down("div.content label[for='"+id+"']");
		if (IsDefined (lblId))
		{
			var li = lblId.up ("li");

			var nxLbl = null;
			while(!nxLbl && IsDefined(li))
			{
			 li = li.next("li");
			 if (IsDefined(li))
			 {
				nxLbl = li.down("label[for]");
			 }
			 else
			 {
				 nxLbl = null
			 }
			}
			
			if (!IsDefined (nxLbl) || !nxLbl.down())	
			{
				li = lblId.up ("ul").down("li label[for]").up ("li");
				// now_debug_out ("next ("+lblId.readAttribute('for')+") = FIRST !");
			}
			else
			{
				li = nxLbl.up ("li");
			}
			
			if (IsDefined (li) && li.down("label[for]"))
			{
				var lblFor = li.down("label[for]");
		    var theId = lblFor.readAttribute('for');
				if (theId && "" != theId && IsDefined($(theId)))
			  {
					// now_debug_out ("next ("+lblId.readAttribute('for')+") = " + theId);
					return nowErrorMessagesScrollTo (theId, g_scrollDuration);
				}
				else
				{
					// now_debug_out ("undefined next id = " + theId);
				}
			}
		}
	}
	return errFld;
}

