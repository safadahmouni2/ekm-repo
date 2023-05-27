
var g_anker_name_hash = null;
// =================================================================
function getAnkerNameHash ()
{
	if (null == g_anker_name_hash)
	{
		now_print_timestamp ("BEG: init g_anker_name_hash...");
		g_anker_name_hash = new Array();
		$$('a[name]').each (function(el)
    {
			g_anker_name_hash[el.name] = $(el);
			// now_debug_out (el.name);
		})
		now_print_timestamp ("END: g_anker_name_hash");
	}
	return g_anker_name_hash;
}

// =================================================================
function tabControl_hideTabContents (oThis)
{
  oTabCtrl = oThis.up("ul");
  oTabCtrlLi = oThis.up("li");
  var hideHead = !oTabCtrlLi.hasClassName ("toggle");
  oAnker = oTabCtrl.previous("a");
  if (IsDefined(oAnker))
  {
		var ankers = getAnkerNameHash ();
	  var oTabBody = ankers[oAnker.rel].up("div");
//	  var oTabBody = $$('a[name="'+oAnker.rel+'"]').first().up("div");
	  oTabCtrl.select('li a').each(function(el)
    {
      el.up("li").removeClassName ("active");
      if (el.rel)
      {
        $w(el.rel).each(function(relName)
        {
          tabControl_hideRelElement (oTabBody, relName, hideHead)
        })
      }
      else
      {
        if (el.href && "" != el.href)
        {
          var relName = el.href.split("#")[1];
          tabControl_hideRelElement (oTabBody, relName, hideHead);
        }
      }
    })
    return false;
  }
  return true;
}

// =================================================================
function TabControl_relBehaviour_active_init ()
{
  // initialize active
	var activeId = "";
	$$("#areaContent .tabCtrlHead").each(function(tabHead)
  {
	  firstElem = tabHead.select("ul.tabControl li a").first();
	  if (firstElem)
    {
      var oCtrlBlock = firstElem.up(".tabControl");
	    oCtrlBlock.select(".active a").each(function(el)
      {
				activeId = el.identify();
      })
			// if (oCtrlBlock.hasClassName("tabPersist"))
			{
				oCtrlBlock.select("li[class^=usp_]").each(function(usp)
				{
					uspTab = "usp_" + getClassValue (usp.className, 'usp_');
					if (hasBodyClass (uspTab))
					{
						activeId = usp.down("a").identify();
					}
				})
			}
    }
  })

	if ("" != activeId)
	{
		tabControl_showByRel ($(activeId));
	}

}
// =================================================================
function TabControl_relBehaviour_SelectorInit (selector)
{
// now_debug_out ("TabControl_relBehaviour_SelectorInit (" + selector + ")");
	$$(selector).each(function(el)
  {
    var li = el.up('li');
    if (li && li.hasClassName("disabled"))
    {
		  el.onclick = function()
      {
        return false;
		  }
    }
    else
    {
		  el.onclick = function()
      {
        return tabControl_showByRel (this);
		  }
    }
  })


}

// =================================================================
function tabControl_hideRelElement (oTabBody, relName, hideHead)
{
 // now_print_timestamp ("hide relName: " + relName + " hideHead=" + hideHead);
	var ankers = getAnkerNameHash ();
	var el = ankers[relName];
	// oTabBody.select('a[name="'+relName+'"]').each(function(el)
	if (IsDefined (el))
  {
    var oCtrlBlock = el.up();
    oCtrlBlock.removeClassName ("active");
    oCtrlBlock.removeClassName ("selected");
    if (hideHead)
    {
      oCtrlBlock.hide();
    }
    else
    {
      oCtrlBlock.show();
    }


    $A(oCtrlBlock.down (".head")).each (function(elHead)
    // oCtrlBlock.select (".head").each (function(elHead)
    {
      elHead.addClassName ("closed");
    })

    $A(oCtrlBlock.down (".body")).each (function(elBody)
  // oCtrlBlock.select (".body").each (function(elBody)
    {
      elBody.hide();
    })
  }
}

// =================================================================
function tabControl_showRelElement (oTabBody, relName, clsName)
{
	
	// now_print_timestamp ("BEG: tabControl_showRelElement: " + relName);
	var ankers = getAnkerNameHash ();
	var el = ankers[relName];
	
	// oTabBody.select('a[name="'+relName+'"]').each(function(el)
	if (IsDefined (el))
  {
    var oCtrlBlock = el.up();
    oCtrlBlock.addClassName ("active");
    if (clsName)
    {
      oCtrlBlock.addClassName (clsName);
    }
    oCtrlBlock.show();
    oCtrlBlock.select (".head").each (function(elHead)
    {
      elHead.removeClassName ("closed");
    })
    oCtrlBlock.select (".body").each (function(elBody)
    {
      elBody.show();
    })
  }
	
	// now_print_timestamp ("END: tabControl_showRelElement: " + relName);
	
}

// =================================================================
function tabControl_hideAll (oTabCtrl)
{
	// now_print_timestamp ("*** BEG: tabControl_hideAll");
  oAnker = oTabCtrl.previous("a");
  if (IsDefined(oAnker))
  {
		
		var ankers = getAnkerNameHash ();
//	  var oTabBody = ankers[oAnker.rel].up("div");
	  var oTabBody = ankers[oAnker.rel].up("ul");
		
	 //  var oTabBody = $$('a[name="'+oAnker.rel+'"]').first().up("div");
	  oTabCtrl.select('li a').each(function(el)
    {
      var li = el.up("li");
      li.removeClassName ("active");
			removeBodyClasses ("usp_" + getClassValue (li.className, 'usp_'));
			
      if (el.rel)
      {
        $w(el.rel).each(function(relName)
        {
          tabControl_hideRelElement (oTabBody, relName, true)
        })
      }
      else
      {
        if (el.href && "" != el.href)
        {
          var relName = el.href.split("#")[1];
          tabControl_hideRelElement (oTabBody, relName, true);
        }
      }
    })
	// now_print_timestamp ("*** END: tabControl_hideAll");
    return false;
  }
  return true;
}

// =================================================================
function tabControl_showAll (oTabCtrl)
{
  oAnker = oTabCtrl.previous("a");
  if (IsDefined(oAnker))
  {
		
		var ankers = getAnkerNameHash ();
	  var oTabBody = ankers[oAnker.rel].up("div");
		
	  // var oTabBody = $$('a[name="'+oAnker.rel+'"]').first().up("div");
	  oTabCtrl.select('li a').each(function(el)
    {
      var li = el.up("li");
      li.removeClassName ("active");
			removeBodyClasses ("usp_" + getClassValue (li.className, 'usp_'));
      if (el.rel)
      {
        $w(el.rel).each(function(relName)
        {
          tabControl_showRelElement (oTabBody, relName)
        })
				uspTab = getClassValue (li.className, 'usp_');
				if ("" != uspTab)
				{
					addBodyClasses ("usp_" + uspTab);
				}
      }
      else
      {
        if (el.href && "" != el.href)
        {
          var relName = el.href.split("#")[1];
          tabControl_showRelElement (oTabBody, relName);
					uspTab = getClassValue (li.className, 'usp_');
					if ("" != uspTab)
					{
						addBodyClasses ("usp_" + uspTab);
					}
				}
      }
    })
    return false;
  }
  return true;
}

// =================================================================
function tabControl_showByRel (oThis)
{
	
	now_print_timestamp ("BEG: tabControl_showByRel");
	
  oTabCtrl = oThis.up("ul");
  oTabCtrlLi = oThis.up("li");
  oTabCtrlLi.addClassName ("active");

  if (IsBrowserVersion("IE_6-7"))
  { 
    oThis.toggleClassName ("msieDummy");
  }
  
  var isToggle = oTabCtrlLi.hasClassName ("toggle");
  var canOpen = (!isToggle || !oTabCtrl.hasClassName ("allOpen"))
  clsSelected = "";

  oAnker = oTabCtrl.previous("a");

  if (IsDefined(oAnker))
  {
		
		var ankers = getAnkerNameHash ();
	  var oTabBody = ankers[oAnker.rel].up("div");
		
    if (canOpen)
    {
      tabControl_hideAll (oTabCtrl);
    }
    else
    {
      tabControl_hideTabContents(oThis);
    }
    if ($(oThis).hasClassName ("selection_behaviourShowAll"))
    {
     if (canOpen)
     {
      tabControl_showAll (oTabCtrl);
     }
    }
    else
    {
      if (oThis.rel && "" != oThis.rel)
      {
        var clsSelected = "selected";
        var aRels = $w(oThis.rel);
        if (aRels.length > 1)
        {
          clsSelected = "";
        }
        if (canOpen)
        {
          aRels.each(function(relName)
          {
            tabControl_showRelElement (oTabBody, relName, clsSelected);
          })
        }
      }
      else
      {
        if (canOpen)
        {
          if (oThis.href && "" != oThis.href)
          {
            var relName = oThis.href.split("#")[1];
            tabControl_showRelElement (oTabBody, relName, "selected");
          }
        }
      }
    }

    if (isToggle)
    {
      oTabCtrl.toggleClassName ("allOpen");
    }
    else
    {
      oTabCtrl.removeClassName ("allOpen");
    }
    oTabCtrlLi.addClassName ("active");
		uspTab = getClassValue (oTabCtrlLi.className, 'usp_');
		if ("" != uspTab)
		{
			addBodyClasses ("usp_" + uspTab);
		}
  }
	
  now_print_timestamp ("END: tabControl_showByRel");

  return false;
}

// =================================================================
function SectionControl_relBehaviour_Init ()
{
	TabControl_relBehaviour_SelectorInit ("#areaContent .sectionCtrl a");
}

// =================================================================
function TabControl_relBehaviour_Init ()
{
  TabControl_relBehaviour_SelectorInit ("#areaContent ul.tabControl li a");
	SectionControl_relBehaviour_Init ();
  TabControl_relBehaviour_active_init ();
}

