
// ***************************
// Deutsche Texte
// ***************************
var g_de_TitleEmailExplain = "Bitte &uuml;berpr&uuml;fen Sie das Format der E-Mail Addresse. Eine E-Mail Addresse muss neben Buchstaben und Ziffern auch genau einmal ein @-Zeichen sowie dahinter einen Punkt (.) enthalten und am Ende mit einer zwei- oder dreistelligen L&auml;nderkennung (z.B. .de oder .com) enden. Ein Beispiel f&uuml;r eine formal g&uuml;ltige E-Mail w&auml;re: info@volkswagen.de";
var g_de_showDetails = "Details anzeigen";
var g_de_collabsibleClosed = "Details anzeigen";

// ***************************
// English (prefix: g_de_)
// ***************************
var g_en_TitleEmailExplain = "invalid email-format";
var g_en_collabsibleClosed = "Show details";


// ***************************
// Tschechisch (prefix: g_cz_)
// ***************************
var g_cz_collabsibleClosed = "Zobrazení detailů";


// ***************************
// Finnisch (prefix: g_fi_)
// ***************************
var g_fi_collabsibleClosed = "Tiedot näytä";


// =================================================================
function getLanguageText(key)
{
  var lang = getBodyClassValue('lang_');
  var langVal = lang + ":" + key;
  var varKey = "g_" + lang + "_" + key;
  
  try {
    langVal = eval(varKey);
  } catch(e){}  
  return langVal;
}

