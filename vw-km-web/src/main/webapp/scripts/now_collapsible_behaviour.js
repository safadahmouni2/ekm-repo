
function Collapsible_Behaviour_Selector_Init(selector)
{
	$$(selector).each(function(el)
  {
    var headElem = el.down(".head");
    if (headElem)
    {
      if (headElem.hasClassName("closed"))
      {
        nowToggleHeadBody(headElem);
      }
      else
      {
        var bdyClsId = getClassValue (el.className, 'usp_');
        if (("" != bdyClsId) && hasBodyClasses("usp_" + bdyClsId))
        {
          el.select(".body").first().hide();
          setCollapsibleState(headElem, "closed");
        }
      }
      headElem.addClassName("cursorPointer active");
		  headElem.onclick = function()
      {
        return nowToggleHeadBody(this);
		  }
    }
    else
    {
      // local_alert ("no head");
    }
  })

//  if (IsBrowserVersion("IE_6-7"))
  if (Prototype.Browser.IE)
  {
	  $$(selector + " > h2").each(function(el)
    {
		  el.onmouseover = function()
      {
        this.addClassName('hover');
		  }
		  el.onmouseout = function()
      {
        this.removeClassName('hover');
		  }
    })
  }
}

function Collapsible_Behaviour_Init()
{
  Collapsible_Behaviour_Selector_Init(".collapsible");
}

function nowGetToggleElem(elem)
{
  var toggleEl = $(elem);
  if (!toggleEl.hasClassName ("collapsible"))
  {
		toggleEl = toggleEl.up(".collapsible");
		if (!toggleEl || !IsDefined(toggleEl))
		{
			toggleEl = $(elem).up(".dragWindow");
		}
	}
  return toggleEl;
}

function setCollapsibleState(oThis, state)
{

  var bdyClsId = "";
  var collEl = oThis.up(".collapsible");
  if (collEl)
  {
    bdyClsId = getClassValue (collEl.className, 'usp_');
    if ("" != bdyClsId)
    {
      bdyClsId = "usp_" + bdyClsId;
    }
  }


  if ("closed" == state)
  {
		if ("H3" == oThis.tagName.toUpperCase())
		{
			var oTxt = oThis.down("em.collapsibleText")
			if (!IsDefined (oTxt))
			{
				oThis.insert ('<em class="collapsibleText">'+getLanguageText("collabsibleClosed")+'</em>');
			}
		}
    oThis.addClassName ("closed");
    addBodyClasses(bdyClsId);
	}
  else
  {
    oThis.removeClassName ("closed");
    removeBodyClasses(bdyClsId);
  }
}

function nowToggleHeadBody(oThis)
{
  var toggleEl = nowGetToggleElem($(oThis));
  toggleEl.select(".body").each(function(el)
  {
    if (("TBODY" == el.tagName) && (!Prototype.Browser.Gecko))
    {
		  el.toggle ();
      if (el.visible())
      {
        setCollapsibleState(oThis, "open");
      }
      else
      {
        setCollapsibleState(oThis, "closed");
      }
    }
    else
    {
      if (el.visible())
      {
        if (!IsBrowserVersion("W3C"))
        {
          el.hide();
        }
        else
        {
					try {
						new Effect.SlideUp(el, { duration: 0.12, transition: Effect.Transitions.linear });
					} catch(e){}
        }
        setCollapsibleState(oThis, "closed");
      }
      else
      {
        if (!IsBrowserVersion("W3C"))
        {
          el.show();
        }
        else
        {
					try {
						new Effect.SlideDown(el, { duration: 0.12, transition: Effect.Transitions.linear});
					} catch(e){}
        }
        setCollapsibleState(oThis, "open");
      }
    }
  })
  return false;
}

