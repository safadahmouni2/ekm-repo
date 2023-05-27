
// * *****************************************************************
// * newadaWeb Javascript Basis-Verhalten festlegen
// * (c) 2009 VOLKSWAGEN AG K-SOB-18
// *
// * *****************************************************************

// =================================================================
// Hier die speziellen Javascript-Dateien für newadaJ auflisten
// =================================================================
var required_script_files = "_nowBehaviour";
required_script_files += ",special_ajax4java";
required_script_files += ",special_newadaJ";

function __js_doPostBack(eventTarget, eventArgument) {
  var theform;
  theform = $$("form").first();
  theform.__EVENTTARGET.value = eventTarget.split("$").join(":");
  theform.__EVENTARGUMENT.value = eventArgument;
  theform.submit();
}

function __js_setEvaButton(eventTarget, eventArgument) {
  var theform;
  theform = $$("form").first();
  theform.__EVENTTARGET.value = eventTarget.split("$").join(":");
  theform.__EVENTARGUMENT.value = eventArgument;
}
