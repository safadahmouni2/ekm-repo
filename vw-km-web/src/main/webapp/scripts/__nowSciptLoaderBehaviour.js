
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

var required_script_files = "";

if (typeof Prototype=='undefined')
{
  required_script_files = "prototype_1.6.0.3,";
}
required_script_files += "effects_1.8.2";
required_script_files += ",now_jsMethods";
required_script_files += ",now_baseClientCtrl";
required_script_files += ",now_BaseUtils";
required_script_files += ",now_collapsible_behaviour";
required_script_files += ",now_bodyClass_Behaviour";
required_script_files += ",now_headlineBehaviour";
required_script_files += ",now_UserBehaviourCookie";
required_script_files += ",now_submit_Behaviour";
required_script_files += ",w3c_selector_compatibility";
required_script_files += ",now_special_behaviour";

// =================================================================
// Generelle Funktionen
// =================================================================
required_script_files += ",browser-detect";


// =================================================================
// Hier die speziellen Javascript-Dateien auflisten
// =================================================================

required_script_files += ",now_screen_resize";
// =================================================================
// Funktionen aus den importierten scripten aufrufen
// =================================================================
required_script_files += ",_nowBehaviourInit";

NewadaBase.load (required_script_files);