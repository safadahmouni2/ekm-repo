

function ajax_language_refresh (oThis)
{
  var oWnd = oThis.up(".dragWindow");
  var id = oWnd.down("label").innerHTML;
  var lbl = $$("label[for="+id+"]").first().identify();
  var txt = oWnd.down("input[type=text]").value;
  var language = getBodyClassValue('lang_');

  now_debug_out ("ToDo: ajax refresh (Language="+language+") ("+id+": "+ txt+")");
  SetValue (lbl, txt);
  dragWindowHide(oWnd);
  return false;
}

function ajax_InplaceWindow (oThis)
{
  var winId = "idInplaceWindow";
  var oWnd = dragWindowSelect(winId);

  inplaceBtn = '<span class="buttons"><a class="button" title="&Uuml;bernehmen" onclick="return ajax_language_refresh($(this));" href="#">OK</a>';
  inplaceBtn += '<a class="abort" title="Schließen und verwerfen der Änderungen (Cancel)" href="#">Verwerfen</a></span>';
  
  var lblId = oThis.readAttribute("for");
  var orgText = oThis.innerHTML;
  var content = "<label for='ipe_'" + lblId + "'>" + lblId + "</label> <input id ='ipe_'" + lblId + "' type='text' value='"+orgText+"' />"; 
  content += inplaceBtn; 

  if (!IsDefined(oWnd))
  {
    var footer = '';
    oWnd = dragWindowCreate (winId, "Editieren", content, footer, "collapsible usp_InplaceWindow");
  }
  else
  {
    oWnd.down("div.content").update(content);
    dragWindow_Behaviour_Selector_Init("#" + oWnd.identify());
  }
  dragWindowShow (oWnd,true, oThis);
  return false;
}

function inplaceEdit_Init()
{
  if (!Prototype.Browser.IE)
  {
    if (hasBodyClasses ("usp_adminEditLanguageText"))
    {
      // now_debug_out ("inplaceEdit_Init..");
      $$("div.usp_DealerControl label[for]").each(function(el)
      {
        el.up().addClassName("positionRelative")
        // now_debug_out ("inplaceEdit_Init id=" +  el.identify());
        el.addClassName("cursorPointer inplaceElement");
        el.onclick = function()
        {
          if (hasBodyClasses ("usp_adminEditLanguageText"))
          {
            return ajax_InplaceWindow ($(this));
          }
          else
          {
            return true;  
          }
        }
      })
    }
  }
}


