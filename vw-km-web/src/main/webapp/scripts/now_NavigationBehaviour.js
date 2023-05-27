
function generateToolTips (menu)
{
  var first = menu.down("a");
	if (first)
	{
		if (!first.title || first.title == "")
		{
			menu.select ("a").each(function(el)
			{
				if (!el.title || el.title == "")
				{
					el.title = el.innerHTML;
				}
			})
		}
	}
}

function NavigationBehaviourInit ()
{
  $$('a.usp_collapseMenu,div.nowMenu .head h3.legend, #nowHead fieldset.logoTop').each(function(el)
  {
 	  el.addClassName("cursorPointer");
 	  el.onclick = function()
	  {
      return fnToggleClassNameSync (getBody(), "usp_collapseMenu", "bodyClass_usp_collapseMenu");
  	}
  })
  
  $$('div.nowMenu li.active li.active').each(function(el)
  {
 	  el.up("li.active").addClassName("subSelected");
  })
  $$('div.nowMenu ul').each(function(el)
  {
		el.onmouseover= function()
		{
			generateToolTips ($(this));
		}
  })
  
  // if (IsBrowserVersion("IE_6-7"))
  if (Prototype.Browser.IE)
  {
    $$('#areaHeader').onmouseover= function()
	  {
      this.addClassName("hover");
  	}
    $$('#areaHeader').onmouseout= function()
	  {
      this.removeClassName("hover");
  	}
		$$('#nowHead .nowMenu').each(function(el)
		{
			el.onmouseover= function()
			{
				this.addClassName("nowMenuHover");
			}
			el.onmouseout= function()
			{
				this.removeClassName("nowMenuHover");
			}
		})
		$$('#nowHead .nowMenu div.head').each(function(el)
		{
	    el.addClassName("hover");
			el.onmouseover= function()
			{
				this.addClassName("hover");
			}
			el.onmouseout= function()
			{
				this.removeClassName("hover");
			}
			var legend = el.down("h3.legend");
			legend.addClassName("hover");
			legend.onmouseover= function()
			{
				this.addClassName("hover");
			}
			legend.onmouseout= function()
			{
				this.removeClassName("hover");
			}
		})
  }
}

function navigation_open_click_behaviour_init ()
{
	$$('div.nowMenu a[href*="#CLICKOPEN"]').each(function(el)
  {
		if ("function" != typeof (el.onclick))
    {
			var elLi = el.up("li");
			if (elLi.down("ul"))
			{
        elLi.addClassName("activateOnClick");
			}
		  if (Prototype.Browser.IE)
			// if (IsBrowserVersion("IE_6-7"))
			{
				elLi.onmouseover = function()
				{
					this.addClassName('hover');
				}
				elLi.onmouseout = function()
				{
					this.removeClassName('hover');
				}
			}
		  el.onclick = function()
      {
	      $$('div.nowMenu li.active').invoke("removeClassName", "active");
	      $$('div.nowMenu li.subSelected').invoke("removeClassName", "subSelected");
        var li = $(this).up("li");
				var isParent = false;
        while (li && "string" == typeof (li.tagName) && "LI" == li.tagName.toUpperCase())
        {
          li.addClassName("active");
				  if (isParent)
					{
						li.addClassName("subSelected");
					}
				  isParent = true;
          li = li.up("li");
        }
			  if (Prototype.Browser.IE)
//        if (IsBrowserVersion("IE_6-7"))
        {
	        $$('div.nowMenu li a').invoke("removeClassName", "immediate"); // forces MSIE redraw / repaint / render
	        $$('div.nowMenu li.active ul').invoke("toggleClassName", "msieDummy"); // forces MSIE redraw / repaint / render
					$$('div.nowMenu li.active>a').invoke("addClassName", "immediate");
        }
        return false;
  	  }
			
    }
  })
	if (IsBrowserVersion("IE_6-7"))
	{
		$$('div.nowMenu li.active>a').invoke("addClassName", "immediate");
	}

}

function navigation_content_click_behaviour_init ()
{
	$$('div.nowMenu a[href^="#CONTENTCLICK_"]').each(function(el)
  {
		if ("function" != typeof (el.onclick))
    {
		  el.onclick = function()
      {
        var theRef = $(this).href.split("#CONTENTCLICK_")[1];
        theRef = theRef.split("?")[0];
	      $$('.CONTENTCLICK_' + theRef).each(function(btn)
        {
          btn.click();
        })
        return true;
  	  }
    }
  })
}
