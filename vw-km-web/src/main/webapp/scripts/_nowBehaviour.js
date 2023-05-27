
// * *****************************************************************
// * newadaWeb Javascript Basis-Verhalten festlegen
// * (c) 2009 VOLKSWAGEN AG K-SOB-18
// *
// * *****************************************************************

// =================================================================
// Die zum Verhalten benoetigte javascript-Dateien laden.
// Die Dateien muessen im Verzeichnins NewadaOnWeb\res\js liegen
// und sind als Komma-separierte Liste ohne das Suffix .js anzugeben
// =================================================================

var required_script_files = "prototype_1.6.0.3";
required_script_files += ",effects_1.8.2";
// required_script_files += ",controls_1.8.2";
//required_script_files += ",dragdrop_1.8.2";
// required_script_files += ",builder_1.8.2";
required_script_files += ",zz_for_develop_only";
required_script_files += ",now_ControlValue";

required_script_files += ",now_jsMethods";
required_script_files += ",now_baseClientCtrl";
required_script_files += ",now_spinnerCtrl_behaviour";
required_script_files += ",ajaxAutoComplete";
required_script_files += ",ajaxActionHandler";
required_script_files += ",ajaxActionRenderer";
required_script_files += ",ajaxDownloader";
required_script_files += ",autoCompletion";
required_script_files += ",now_mergedCookies";

required_script_files += ",now_i18n";
required_script_files += ",now_BaseData";
required_script_files += ",now_BaseData-Vehicle";
required_script_files += ",now_BaseUtils";
required_script_files += ",calendar_date_select";
required_script_files += ",now_calendar_date_format";

required_script_files += ",now_calendar_behaviour";
required_script_files += ",now_errorMessages";
required_script_files += ",now_collapsible_behaviour";
//required_script_files += ",now_dragWindow";
required_script_files += ",now_tabControl_relBehaviour";

required_script_files += ",now_comboBox_Behaviour";
required_script_files += ",now_bodyClass_Behaviour";

required_script_files += ",now_headlineBehaviour";
required_script_files += ",now_FieldValidator";
required_script_files += ",now_NavigationBehaviour";
required_script_files += ",now_softScroll_behaviour";
required_script_files += ",now_tooltips_class";
// required_script_files += ",now_Tooltips_behaviour";
// required_script_files += ",now_Footer_behaviour";
required_script_files += ",now_Unittest_behaviour";
required_script_files += ",now_DataTable_behaviour";
//required_script_files += ",page_Locating_Behaviour";
required_script_files += ",now_hrefTarget";

required_script_files += ",now_UserBehaviourCookie";
required_script_files += ",now_Breadcrumb";
required_script_files += ",now_messageBox";
required_script_files += ",now_modal_behaviour";

required_script_files += ",now_submit_Behaviour";

required_script_files += ",w3c_selector_compatibility";
required_script_files += ",now_special_behaviour";
required_script_files += ",now_first_focus_behaviour";

required_script_files += ",usp_userStatePersistence";
required_script_files += ",now_TableSort";
required_script_files += ",now_TableSettings";
required_script_files += ",notSelectableFields";

// required_script_files += ",richedit";
required_script_files += ",pageHelp";
required_script_files += ",now_ajaxBase";

// =================================================================
// Generelle Funktionen
// =================================================================
required_script_files += ",ie_fixes";
required_script_files += ",ie6_fixes";
required_script_files += ",browser-detect";
required_script_files += ",generic_new_main_functions";
required_script_files += ",generic_new_eva";

// =================================================================
// Hier die gennerellen "Alt-Last-Javascript-Dateien auflisten
// =================================================================
required_script_files += ",generic_Old_Newada";
required_script_files += ",generic_Old_Main";
required_script_files += ",generic_ReportJsError";

// =================================================================
// Hier die speziellen Javascript-Dateien auflisten
// =================================================================
required_script_files += ",special_newadaonline_init";
required_script_files += ",special_Start_Init";
required_script_files += ",special_OrderStatus_Init";
required_script_files += ",special_locating_pool_result";
required_script_files += ",special_OrderStatusV5_Init";
required_script_files += ",special_OrderEntry_Init";
required_script_files += ",special_SchedulingVehicleCollection_Init";
required_script_files += ",special_CDMSelectRow";
required_script_files += ",special_OrderSelection";
required_script_files += ",special_Equipement";
required_script_files += ",special_ajax4DotNet";
required_script_files += ",special_OrderCheck";
required_script_files += ",special_Reservation";

// /* ********************************************************** */
// /*   experimental inplace-Edit Beta                           */
// /* ********************************************************** */
// required_script_files += ",now_inplaceEdit";

// =================================================================
// Funktionen aus den importierten scripten aufrufen
// =================================================================
required_script_files += ",_nowBehaviourInit";

if ("function" == typeof (now_behaviour_init))
{
  document.observe('dom:loaded', now_behaviour_init);
}
