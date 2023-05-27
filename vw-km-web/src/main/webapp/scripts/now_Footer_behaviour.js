
var g_must_redraw = true;

function Modal_Redraw()
{
  if ($('areaGeneric'))
  {
    if ($('areaGeneric').visible())
    {
      // now_debug_out ("Modal_Redraw", 2000, "G");
      $('areaGeneric').setStyle ({bottom:'-1px'});
      $('areaGeneric').setStyle ({bottom:'0px'});
    }
  }
  if ($('#loadingCancel'))
  {
    $('#loadingCancel').setStyle ({bottom:'66%'});
  }
}

function Footer_IFrame_Redraw()
{
  if ($('areaFooter'))
  {
    var oIFrame = dragWindowIFrame('areaFooter');
    if (oIFrame)
    {
      oIFrame.show();
    }
    ie6_fixes_WinReposition ('areaFooter', 0)
  }
}

function Footer_Redraw()
{
  if ($('areaFooter'))
  {
    $('areaFooter').setStyle ({bottom:'-1px'});
    $('areaFooter').setStyle ({bottom:'0px'});
  }
}

function Footer_All_Redraw()
{
  Footer_Redraw();
  Footer_IFrame_Redraw();
}

function perform_redraw()
{
  Footer_All_Redraw();
  g_must_redraw = false;
  // now_debug_out ("perform_redraw", 2000, "G");
  removeBodyClasses("onWindowResizeScroll");
  Modal_Redraw();
}

function Footer_OnScroll_Behaviour ()
{
  if (IsBrowserVersion("IE6"))
  {
    Modal_Redraw();
    Footer_Redraw()
    if (need_ie6_IFrame())
    {
      if ($('areaFooter'))
      {
    // addBodyClasses("onWindowResizeScroll");
        if (!g_must_redraw)
        {
          g_must_redraw = true;
          var oIFrame = dragWindowIFrame('areaFooter');
          if (oIFrame)
          {
            oIFrame.hide();
          }
        }
      }
    }
  }
}

function Footer_OnResize_Behaviour ()
{
  if (need_ie6_IFrame())
  {
 // addBodyClasses("onWindowResizeScroll");
    if ($('areaFooter'))
    {
   //   ie6_fixes_WinReposition ('areaFooter', 0)
    //  now_debug_out ("Footer_OnResize_Behaviour", 2000, "G");
      if (!g_must_redraw)
      {
        g_must_redraw = true;
        var oIFrame = dragWindowIFrame('areaFooter');
        if (oIFrame)
        {
          oIFrame.hide();
        }
      }
    }
  }
}

function Footer_Behaviour_Init ()
{

 // now_debug_out ("Footer_Behaviour_Init", 2000, "G");
  if (IsBrowserVersion("IE6"))
  {

    now_debug_out ("SKIP IE6 Footer_Behaviour_Init", 2000, "G");
    return false;

    Event.observe(window, 'scroll', Footer_OnScroll_Behaviour);
    Event.observe(window, 'resize', Footer_OnResize_Behaviour);

    var oFooter = $('areaFooter');
    if (need_ie6_IFrame())
    {

	    getBody().onmouseover = function()
      {
        if (g_must_redraw)
        {
          perform_redraw();
        }
	    }
	    getBody().onmouseout = function()
      {
        if (g_must_redraw)
        {
          perform_redraw();
        }
	    }

	    if (oFooter)
      {
        ie6_dragWindowCreateIFrame ($('areaFooter'));
	    }
    }

	  if (oFooter)
    {
	    oFooter.onmouseover = function()
      {
        addBodyClasses("hideListBoxes hover");
        // now_debug_out ("areaFooter: hover on", 2000, "G");
		  }
	    oFooter.onmouseout = function()
      {
        removeBodyClasses("hideListBoxes hover");
        // now_debug_out ("areaFooter: hover off", 2000, "G");
		  }
      oFooter.select ('li.pullup').each(function(li)
      {
	      li.onmouseover = function()
        {
          addBodyClasses("hover");
          now_debug_out ("li.pullup: hover on", 2000, "G");
		    }
	      li.onmouseout = function()
        {
          removeBodyClasses("hover");
          now_debug_out ("li.pullup: hover off", 2000, "G");
		    }
      })
    }

  }
}
