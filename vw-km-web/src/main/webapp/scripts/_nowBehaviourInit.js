// =================================================================
// Das Verhalten der Seite festlegen
// =================================================================
g_first_timestamp = -1;
function now_get_timestamp ()
{
 var theDayTime = new Date();
 timestamp = theDayTime.getSeconds() + theDayTime.getMilliseconds() / 1000;

  if (g_first_timestamp < 0)
  {
    g_first_timestamp = timestamp;
  }

 return timestamp;
}

function doHandleOnScroll ()
{
  var windowScroll = document.viewport.getScrollOffsets();    
  now_setCookie ("scrollPos", getCurrendLocation () + "$"+windowScroll.top + ";" + windowScroll.left);  
}

function getScrollValues ()
{
  var top = 0;
  var left = 0;
  var scrollPos = now_getCookie ("scrollPos");
  if (scrollPos && scrollPos.length > 6)
  {
    var aPos = scrollPos.split("$");
    var scrollPage = aPos[0];
    if (scrollPage == getCurrendLocation ())
    {
      var values = aPos[1];
      if (values && values.length > 2)
      {
        var aValues = values.split(";");
        top = 1*Math.round (aValues[0]);
        left = 1*Math.round (aValues[1]);
      }
    }
  }
  return {top:top,left:left};
}

function handleScrolling (values)
{
  window.scrollTo(values.left,values.top);
}

g_prev_timestamp = 0;
function now_print_timestamp (str)
{
  var msEndTime = now_get_timestamp();
  var msTime = msEndTime - g_first_timestamp;
// now_debug_out (0 + msEndTime - g_prev_timestamp +": time to "+str+" = (" + msTime + ") seconds",1000,"A");
	g_prev_timestamp = msEndTime;
}

function now_behaviour_init ()
{
 var msBegTime = now_get_timestamp();
  if ("function" == typeof (browser_detect_Init)) {browser_detect_Init();}
  if ("function" == typeof (userBehaviourCookie_Init))  {userBehaviourCookie_Init ();}

now_print_timestamp ("browser_detect_Init");
now_print_timestamp ("begin");
  if ("function" == typeof (modal_Behaviour_Init)) {modal_Behaviour_Init();}
  
  var scrollValues = getScrollValues ();
  if ("function" == typeof (errorDragWindowInit)) {errorDragWindowInit();}

  if ("function" == typeof (submitOnEnter_Init)) {submitOnEnter_Init();}

  // if ("function" == typeof (HoverMenu_Behaviour_Init)) {HoverMenu_Behaviour_Init(".hoverMenu");}
  // if ("function" == typeof (Footer_Behaviour_Init)) {Footer_Behaviour_Init ();}

  if ("function" == typeof (BodyClass_Behaviour_Init)) {BodyClass_Behaviour_Init ();}
//now_print_timestamp ("BodyClass_Behaviour_Init");

  if ("function" == typeof (HeadlineBehaviourInit)) {HeadlineBehaviourInit ();}
  
  if (!IsBrowserVersion("IE_6-7"))
  {
    if ("function" == typeof (NavigationBehaviourInit)) {NavigationBehaviourInit ();}
  }

  if (IsBrowserVersion("IE_6-7"))
  {
    if ("function" == typeof (ie_fixes_Init)) {ie_fixes_Init ();}    
  }

  if ("function" == typeof (w3c_selector_compatibility_Init))  {w3c_selector_compatibility_Init ();}
  if ("function" == typeof (input_readonly_init))  {input_readonly_init ();}

  if ("function" == typeof (ComboBox_Behaviour_Init)) {ComboBox_Behaviour_Init ();}

  if ("function" == typeof (nowFieldValidatorInit)) {nowFieldValidatorInit ();}
  if ("function" == typeof (nowErrorMessagesInit)) {nowErrorMessagesInit ();}

  if (!IsBrowserVersion("IE6"))
  {
    if ("function" == typeof (Calendar_Behaviour_Init)) {Calendar_Behaviour_Init ();}
  }

  if ("function" == typeof (Collapsible_Behaviour_Init)) { Collapsible_Behaviour_Init(); }
  if ("function" == typeof (dragWindow_Init)) {dragWindow_Init();}

  if ("function" == typeof (TabControl_relBehaviour_Init))  {TabControl_relBehaviour_Init ();}
  

  if ("function" == typeof (PopupWin_Behaviour_Init)) {PopupWin_Behaviour_Init ();}
  
//  if ("function" == typeof (Unit_Behaviour_Init))  {Unit_Behaviour_Init ();}


  if ("function" == typeof (ajaxAutoComplete_Init)) {ajaxAutoComplete_Init();}
  if ("function" == typeof (ajaxAutoLoad_Init))     {ajaxAutoLoad_Init();}
  if ("function" == typeof (ajaxDownload_Init))     {ajaxDownload_Init();}
  if ("function" == typeof (ajaxAutoCall_Init))     {ajaxAutoCall_Init();}

  if ("function" == typeof (now_TableSettings_Init)) {now_TableSettings_Init();}


  if (hasBodyClasses ("usp_style_Audi"))
  {
    if ("function" == typeof (Breadcrumb_Init))  {Breadcrumb_Init ();}
  }
  if ("function" == typeof (Tooltips_Behaviour_Init)) {Tooltips_Behaviour_Init ("*[title]");}

	if ("function" == typeof (notSelectableFields_init)) {notSelectableFields_init();}

  // === direktaufruf Seitenspezifischer ad-on's  ===
  if ("function" == typeof (NewadaOnline_Init)) {NewadaOnline_Init();}
  
  if ("function" == typeof (SchedulingVehicleCollection_Init)) {SchedulingVehicleCollection_Init();}
  
	if ("function" == typeof (dataPrivacy_init)) {dataPrivacy_init();}
	
  // === indirektes Spezialverhalten und weitere Seitenspezifische ad-on's ueber <form class="special_...."  ===
  if ("function" == typeof (SpecialPageBehaviour))
  {
    SpecialPageBehaviour ();
  }

  if ("function" == typeof (SpecialPageBehaviourPreviousOwner))
  {
    SpecialPageBehaviourPreviousOwner ();
  }
 
  if ("function" == typeof (OS_SpecialPageBehaviour))
  {
    OS_SpecialPageBehaviour();
  }
  
  if ("function" == typeof (special_page_behaviour_init_by_form_class))
  {
    special_page_behaviour_init_by_form_class ();
  }

  if ("function" == typeof (navigation_open_click_behaviour_init))  {navigation_open_click_behaviour_init ();}
  if ("function" == typeof (navigation_content_click_behaviour_init))  {navigation_content_click_behaviour_init ();}
  if ("function" == typeof (SoftScroll_Behaviour_Init))  {SoftScroll_Behaviour_Init ();}

  if ("function" == typeof (generic_new_main_functions_init)) {generic_new_main_functions_init();}
  if ("function" == typeof (usp_userStatePersistence_Init)) {usp_userStatePersistence_Init();}

  if ("function" == typeof (dataTableSelectInit)) {dataTableSelectInit ();}

  if ("function" == typeof (Init_OS_ViewRowsByBrands)) { Init_OS_ViewRowsByBrands(); }
  
  removeBodyClasses ("loading load_Modal");
	
// now_print_timestamp ("first_focus_behaviour..");

//  if ("" == scrollValues)
  {
    if ("function" == typeof (first_focus_behaviour)) {first_focus_behaviour();}
  }
//now_print_timestamp ("finish first_focus_behaviour");

 if ("function" == typeof (initTableSettings))
 {
 	 initTableSettings('table.ajaxTable thead input[type=text], table.ajaxTable thead select');
 }
  if ("function" == typeof (handleScrolling)) { handleScrolling(scrollValues); }
  if ("function" == typeof (pageHelp_init)) { pageHelp_init(); }
  
  //if ("function" == typeof (loadMessages)) { loadMessages(); }
	
//* ********************************************************** */
//*   experimental inplace-Edit Beta                           */
//* ********************************************************** */
//  if ("function" == typeof (inplaceEdit_Init)) {inplaceEdit_Init();}

  var msEndTime = now_get_timestamp();
  var msTime = msEndTime - msBegTime;

  // now_debug_out ("prototype Init performs in (" + msTime + ") seconds",1000,"A");
  now_print_timestamp ("prototype Init end");

}

function nowOnUnloadBehaviour()
{
	if (IsBrowserVersion("IE_6-7"))
	{
		Event.unloadCache();
	}
	if ("function" == typeof (set_modal_submit)) {set_modal_submit();}
  return true;
}

Event.unloadCache = function() {
  if (!Event.observers) return;
  for (var i = 0; i < Event.observers.length; i++) {
    Event.stopObserving.apply(this, Event.observers[i]);
    Event.observers[i][0] = null;
  }
  Event.observers = false;
};

// prevent memory leaks in IE
// Event.observe(window, 'unload', Event.unloadCache, false);

if (IsBrowserVersion("IE_6-7"))
{
  Event.observe(window, 'unload', nowOnUnloadBehaviour, false);
}
else
{
	Event.observe(window, 'beforeunload', nowOnUnloadBehaviour);
}

window.onscroll = doHandleOnScroll;
