
/* *****************************************************************
 * newadaWeb Javascript Basis-Verhalten festlegen
 * (c) 2007 VOLKSWAGEN AG K-SOB-18
 *
 * *************************************************************** */

// =================================================================
// Die zum Verhalten benoetigte javascript-Dateien laden.
// Die Dateien muessen im Verzeichnins NewadaOnWeb\Common\js liegen
// und sind als Komma-separierte Liste ohne das Suffix .js anzugeben
// =================================================================

var required_script_files = "";
required_script_files += "effects_1.8.2,controls_1.8.2";
//required_script_files +=",dragdrop_1.8.2";
required_script_files +=",builder_1.8.2";	
required_script_files += ",now_jsMethods";
required_script_files += ",browser-detect";
required_script_files += ",now_baseClientCtrl";
required_script_files += ",now_spinnerCtrl_behaviour";
required_script_files += ",autoCompletion";

required_script_files += ",now_i18n";
required_script_files += ",now_BaseData";
required_script_files += ",now_BaseData-Vehicle";
required_script_files += ",now_BaseUtils";
required_script_files += ",calendar_date_select";
required_script_files += ",now_calendar_date_format";
// required_script_files += ",format_euro_24hr";

required_script_files += ",now_calendar_behaviour";
required_script_files += ",now_errorMessages";
required_script_files += ",now_collapsible_behaviour";
required_script_files += ",now_tabControl_relBehaviour";

required_script_files += ",now_comboBox_Behaviour";
required_script_files += ",now_bodyClass_Behaviour";
required_script_files += ",now_hoverMenu_Behaviour";
required_script_files += ",now_headlineBehaviour";
required_script_files += ",now_FieldValidator";
required_script_files += ",now_NavigationBehaviour";
required_script_files += ",now_softScroll_behaviour";
required_script_files += ",now_tooltips_class";
required_script_files += ",now_Tooltips_behaviour";
required_script_files += ",now_Footer_behaviour";
required_script_files += ",now_Unittest_behaviour";
required_script_files += ",now_DataTable_behaviour";
required_script_files += ",page_Locating_Behaviour";
required_script_files += ",now_hrefTarget";
// required_script_files += ",__iFrameShim";
//required_script_files += ",prototype-ui";
required_script_files += ",ie_fixes";
required_script_files += ",w3c_selector_compatibility";
required_script_files += ",now_UserBehaviourCookie";

NewadaBase.load (required_script_files);



// =================================================================
// Das Verhalten der Seite festlegen
// =================================================================
function fnBaseBehaviour ()
{

  var oBody = getBody();
  oBody.addClassName ("canScript");
  
  if ("function" == typeof (browser_detect_Init)) {browser_detect_Init();}
  if ("function" == typeof (userBehaviourCookie_Init))  {userBehaviourCookie_Init ();}
 
  
  if ("function" == typeof (HoverMenu_Behaviour_Init)) {HoverMenu_Behaviour_Init(".hoverMenu");}
  if ("function" == typeof (Footer_Behaviour_Init)) {Footer_Behaviour_Init ();}

  if ("function" == typeof (BodyClass_Behaviour_Init)) {BodyClass_Behaviour_Init ();}
  if ("function" == typeof (AutoCompletion_Init)) {AutoCompletion_Init ();}

  if ("function" == typeof (HeadlineBehaviourInit)) {HeadlineBehaviourInit ();}
  if ("function" == typeof (NavigationBehaviourInit)) {NavigationBehaviourInit ();}

  if ("function" == typeof (SpinnerCtrl_Behaviour_Init)) {SpinnerCtrl_Behaviour_Init ();}
  if ("function" == typeof (ComboBox_Behaviour_Init)) {ComboBox_Behaviour_Init ();}
  if ("function" == typeof (Href_Target_Init)) {Href_Target_Init ("external", "_blank");}

  if ("function" == typeof (nowFieldValidatorInit)) {nowFieldValidatorInit ();}
  if ("function" == typeof (nowErrorMessagesInit)) {nowErrorMessagesInit ();}
  if ("function" == typeof (DataTable_Behaviour_Init)) {DataTable_Behaviour_Init ();}

  if ("function" == typeof (PageLocating_Behaviour_Init)) {PageLocating_Behaviour_Init ();}

  if ("function" == typeof (Calendar_Behaviour_Init)) {Calendar_Behaviour_Init ();}
  if ("function" == typeof (Collapsible_Behaviour_Init)) {Collapsible_Behaviour_Init ();}
  if ("function" == typeof (SoftScroll_Behaviour_Init))  {SoftScroll_Behaviour_Init ();}
  if ("function" == typeof (TabControl_relBehaviour_Init))
  {
    TabControl_relBehaviour_Init ("");
  }

  if ("function" == typeof (Unit_Behaviour_Init))  {Unit_Behaviour_Init ();}
  // if ("function" == typeof (Tooltips_Behaviour_Init)) {Tooltips_Behaviour_Init ("*[title]");}
  if ("function" == typeof (w3c_selector_compatibility_Init))  {w3c_selector_compatibility_Init ();}
  if ("function" == typeof (ie_fixes_Init))  {ie_fixes_Init ();}

  // === Spezialverhalten und weitere Seitenspezifische ad-on's  ===
  if ("function" == typeof (SpecialPageBehaviour))
  {
    SpecialPageBehaviour ();
  }

  if (!$("areaGeneric"))
  {
   // $(oBody).insert({bottom: '<div id="areaGeneric"></div><div class="loader"></div>'});
  }

  $$('form').first().focusFirstElement ();
  
}


function nowOnUnloadBehaviour()
{
  if ($("areaGeneric"))
  {
    addBodyClasses('mode_Modal sys_submit');
    $("areaGeneric").setStyle({display:'block',height:'100%',width:'100%'});
  }
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

document.observe('dom:loaded', fnBaseBehaviour);

/* prevent memory leaks in IE */
// Event.observe(window, 'unload', Event.unloadCache, false);

Event.observe(window, 'unload', nowOnUnloadBehaviour);
Event.observe(window, 'beforeunload', nowOnUnloadBehaviour);

