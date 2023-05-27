
var g_BodyClassNames = null;
var g_oForm = null;
var g_oBody = null;

var keyShift = 16;
var keyStrg = 17;
var keyAlt = 18;

var g_extraKey = new Array();

// =================================================================
function initExtraKey(key)
{
	g_extraKey["key_"+key] = 0;
}

// =================================================================
function initExtraKeys()
{
	initExtraKey(keyShift);
	initExtraKey(keyStrg);
	initExtraKey(keyAlt);
}

// =================================================================
initExtraKeys();

// =================================================================
function hasExtraKey(key)
{
  switch (key) {
    case 16:
    case 17:
    case 18:
      return "down" == g_extraKey["key_" + key];
	}
	return false;
}

// =================================================================
function setExtraKey(key, state)
{
	switch (key) {
    case 16:
    case 17:
    case 18:
			initExtraKey(keyAlt);			
      g_extraKey["key_" + key] = state;
      break;
	}
}

// =================================================================
function getBody ()
{
  if ("undefined" == typeof (g_oBody) || null == g_oBody)
  {
    g_oBody = $$('body').first();
  }
  return g_oBody;
}

// =================================================================
function hasInnerText ()
{
	return (getBody ().innerText != undefined) ? true : false;
}

// =================================================================
function hasTextContent ()
{
	return (getBody ().textContent != undefined) ? true : false;
}

// =================================================================
// caching fuer IE6: Der braucht unwahrscheinlich lange beim Zugriff auf
// die class im Body
function getBodyClasses ()
{
	// alert (g_BodyClassNames);
  if ("undefined" == typeof (g_BodyClassNames) || null == g_BodyClassNames)
  {
  	if (getBody()) {
  	
    g_BodyClassNames = getBody().className;
   }
  }
  return g_BodyClassNames;
}

// =================================================================
function getForm ()
{
  if ("undefined" == typeof (g_oForm) || null == g_oForm)
  {
    g_oForm = $$('form').first();
  }
  return g_oForm;
}

// =================================================================
function clonePosition (target, obj)
{
	Position.clone($(target), $(obj), { setHeight: false, offsetTop: $(target).offsetHeight });
}

// =================================================================
function readScriptList (listName)
{
	
	dataList = null;
	try
	{
		dataList = eval(listName);		
		if (dataList && "string" == typeof dataList && dataList.length > 0)
		{
			return dataList;
		}
	}
	catch (ex){}
  return "";
}

// =================================================================
function getHttpResRoot() {
	var httpRoot = '' + document.location;
	if (hasBodyClass ("fw_dotNet"))
	{
		httpRoot = (httpRoot.split("/NewadaOnWeb/"))[0] + "/NewadaOnWeb/Common/res";
	}
	else
	{
		httpRoot = (httpRoot.split("/newadaJ/"))[0] + "/newadaJ/res";		
	}
	return httpRoot;
}

// =================================================================
function now_debug_out (msg,level,modul)
{
  // Ausgabe nur wenn develop im footer verfuegbar ist.
  var oDbgWnd = $("developDebugWin");
  if (oDbgWnd && oDbgWnd.tagName && "INPUT" == oDbgWnd.tagName.toUpperCase())
  {
    if ("function" == typeof (developDebugWin_WriteLn)) {developDebugWin_WriteLn (msg,level || "",modul || "");}
  }
}

// =================================================================
function local_alert (msg)
{
  now_debug_out ("*** ALERT *** " + msg);
}

// =================================================================
function IsNumeric(val)
{
	val = "" + val;
	var numericExpression = /^[0-9]+$/;
	return val.match(numericExpression);
  // return !isNaN(val);
}

// =================================================================
function IsDefined (oEle)
{
  var isDef = false;
  try
  {
    if ("undefined" != typeof (oEle))
    {
      if ("string" == typeof (oEle))
      {
        oObj = $(oEle);
        isDef = !Object.isUndefined(oObj) && (oObj !== null);
      }
      else
      {
        if (oEle)
        {
          if ("string" == typeof (oEle.tagName) || ("object" == typeof (oEle)) || ("function" == typeof (oEle)))
          {
            isDef = true;
          }
          else
          {
            oObj = $A(oEle);
            isDef = (oObj && (oObj !== null) && !Object.isUndefined(oObj) && (oObj.length > 0));
          }
        }
      }
    }
  } catch (e) {
    local_alert ("ERR: Not IsDefined execption = (" + e + ")");
    isDef = false;
  }

  return isDef;
}

// =================================================================
function uncheckRadioGroup(oContainer, radioName)
{
	$(oContainer).select("input[name="+radioName+"]").each(function(el)
	{
		if (el.hasAttribute ("checked"))
		{
			// now_debug_out ("radio uncheck of " + el.value);
			el.removeAttribute ("checked");
		}
	})
}

// =================================================================
function GetInnerText(oThis)
{
	if (hasInnerText())
	{
		return oThis.innerText;
	}
	else if (hasTextContent())
	{
		return oThis.textContent;
	}
	return oThis.innerHTML;
}

// =================================================================
function SetInnerText(oThis, val)
{
	if (hasInnerText())
	{
		oThis.innerText = val;
	}
	else if (hasTextContent())
	{
	  oThis.textContent = val;
	}
	else
	{
	  oThis.innerHTML = val;
	}
}

// =================================================================
function SetValue (oThis, val)
{
  if (oThis)
  {
    oThis = $(oThis);
  }
  if (oThis && IsDefined(oThis))
  {
    switch (oThis.tagName.toLowerCase())
    {
      case "input":
				switch (oThis.type)
		    {
					case "radio":
					case "checkbox":
						
						if ("radio" == oThis.type)
						{
							// uncheck radio-group
							uncheckRadioGroup(oThis.up("form"), oThis.name);
						}
						if (String(oThis.value) == String(val))
						{
							// now_debug_out ("input set checked elem value = " + oThis.value);
							oThis.setAttribute ("checked", "checked");
							oThis.checked = true;
						}
						else
						{
							// now_debug_out ("input remove checked elem value = " + oThis.value);
							oThis.removeAttribute ("checked");
						}
						break;
					default:
						if ( String(oThis.value) !=  String(val) )
						{
							oThis.value = val;
						}
						break;
        }
        break;
        
      case "tr":
      case "td":
      case "label":
			case "option":				
        // oThis.innerHTML=val;
				SetInnerText(oThis, val);
        break;
        
      case "select":
        // inpossible
        break;
        
      case "textarea":
        if (String(oThis.innerHTML) != String(val))
				{
				  oThis.innerHTML = val;
				}
        break;
        
      default:
      {
          now_debug_out ("ERR: SetValue: tagName ("+oThis.tagName+") not implemented!")
      }
      break;
    }
  }
	return val;
}

// =================================================================
function GetValue(oThis)
{
	var val = "";
  if (oThis)
  {
		oThis = $(oThis);
    if (IsDefined(oThis) && oThis.tagName)
    {
			switch (oThis.tagName.toLowerCase())
			{
				case "select":
				case "input":
					val = $F(oThis);
					break;
		  		case "li":
		  		case "textarea":
		  			val = oThis.innerHTML;
		  			break;
	      		case "tr":
				case "td":
				case "b":
				case "span":
				case "option":
					val = GetInnerText (oThis);
					break;
				default:
					val = GetInnerText (oThis);
					now_debug_out ("CHECK: GetValue: tagName ("+oThis.tagName+"), id = ("+oThis.identify()+") uses default implementation GetInnerText")
				break;
			}
		}
  }
	if (!val || "undefined" == typeof (val))
	{
	  val = "";
	}
  return  val;
}

// =================================================================
function SelectToText (SelectElement, TextElement) 
{
	SetValue (TextElement, GetValue(SelectElement));
}

// =================================================================
function SetDisabled (oThis, isDisabled)
{
	oThis = $(oThis);
	if (oThis)
	{
		var tgName = oThis.tagName.toUpperCase();
		if ("SELECT" == tgName)
		{
			SetSelectable(oThis, !isDisabled);
		}
		else
		{
			oThis.disabled = isDisabled;
		}
	
		if ("BUTTON" != tgName)
		{
			// now_debug_out (tgName + ": SetDisabled (" + isDisabled + ") id = " + oThis.identify());
		}
	}
}

// =================================================================
function SetFocus (oThis)
{
	var ret = false;
  if (oThis)
  {
		oThis = $(oThis);
	}
  if (oThis && IsDefined(oThis))
  {
		try
		{
			if (IsReadOnly (oThis))
			{
				return nowErrorMessagesScrollTo (oThis, g_scrollDuration);
			}
			else
			{
        if (IsVisibleElement(oThis))
        {
				  oThis.focus();
				  ret = true;
          // now_debug_out ("OK: SetFocus ("+oThis.identify()+")");
        }
			}
		}
		catch(ex)
		{
      var vis= oThis.visible();
			now_debug_out ("SetFocus ("+oThis.identify()+") failed. Error-Message=" + ex + "; visible=" + vis);
			ret = false;
		}
	}

	return ret;
}

// =================================================================
function IsReadOnly(ctrl)
{
	return ctrl.hasClassName("readonly") || ctrl.hasAttribute("disabled") || ctrl.hasAttribute("readonly");
}

// =================================================================
function remove_unselected_options (oCtrl)
{
	var sel = oCtrl.down ("option[selected]");
	if (sel)
	{
		sel.addClassName ("selected");
	}
	else
	{
		var sel = oCtrl.down ("option");
		if (sel)
		{
			// sel.setAttribute ("selected", "selected");
			sel.addClassName ("selected");
		}
		else
		{
			now_debug_out ("select with no options: id = " + oCtrl.identify());
		}
	}

	// alle nicht ausgewaehlten options loeschen -> damit kann die ausgewaehlte nicht mehr verstellt werden
	// im Firefox wuerde onfocus -> blur reichen.
	oCtrl.select ("option").each(function(el)
	{
		if (!el.hasClassName("selected"))
		{
			el.remove ();
		}
	})

}

// =================================================================
// Ein Aufruf von SetSelectable (ele, false) verhindert, daß das übergebene Element (ele)
// mit der Tab-Taste angesprungen werden kann sowie mit der Maus selektiert werden kann.
function SetSelectable(oCtrl, selectable)
{
	oCtrl = $(oCtrl);
  if (oCtrl != null)
  {
		if (selectable)
	  {
			var tabIdx = oCtrl.getAttribute ("tabindex");
			if ("-1" == tabIdx)
			{
				oCtrl.removeAttribute ("tabindex");
			  oCtrl.removeAttribute ("onfocus");
			  // oCtrl.removeAttribute ("disabled");
			}
		}
		else
		{
			// now_debug_out ("Set Not Selectable id=" + oCtrl.identify());
			oCtrl.setAttribute ("tabindex", "-1");
			
			oCtrl.onfocus = function()
			{
				// now_debug_out ($(this).identify());
				this.blur();
			}

			if ("SELECT" == oCtrl.tagName.toUpperCase())
			{
				remove_unselected_options (oCtrl);
			}
			
			
		}
	}	
}

// =================================================================
function SetReadOnly(ctrl, required, readOnly, delDisable)
{
  var inp = $(ctrl);

//	now_debug_out ("SetReadOnly(" + inp.identify() + ", " + required + ", " + readOnly + ", " + delDisable +")");
	
  if (inp != null)
  {
		if (readOnly)
	  {
			inp.addClassName("readonly");
		}
		else
		{
			inp.removeClassName("readonly");
			// now_debug_out ("remove-readonly at SetReadOnly: " + inp.identify());
		}
		SetSelectable(inp, !readOnly);
    if (typeof (inp.readOnly) != "undefined")
    {
			inp.readOnly = readOnly;
		}
		else
		{
			SetDisabled(inp,readOnly);
		}
    if (readOnly && delDisable)
    {
      if (inp.tagName == "SELECT")
      {
        // inp.selectedIndex = 0;
      }
      else
      {
        // inp.value = "";
      }
    }
  }
}

// =================================================================
function input_readonly_init ()
{
  $$("#areaContent input[readonly]").each(function(el)
  {
    el.addClassName ("readonly");
    SetSelectable(el, false);
  })
}

// =================================================================
function IsVisibleElement (oEle)
{
  // Pruefen ob das element in einem unsichtbaren Container steckt,
  // dazu im document-baum "hochlaufen" bis ein
  var isVisible = false;
  if (oEle)
  {
    isVisible = ('hidden' != oEle.type) && oEle.visible();
    var oContainer = oEle;
    while (isVisible && oContainer && IsDefined (oContainer))
    {
      oContainer = oContainer.up("*[style*=none]");
      if (oContainer && IsDefined (oContainer))
      {
        isVisible = (oContainer.visible() && ('hidden' != oContainer.type))
        if (!isVisible)
        {
		       // now_debug_out ("visibleElement ("+oEle.identify()+"): in invisible container ("+oContainer.identify()+")");
        }
      }
    }
  }

  return isVisible;

}

// =================================================================
function readStyleVal(obj, key)
{
  var val = obj.getStyle(key);
  if (val.indexOf("px") > 0)
  {
    val = val.substr(0, val.indexOf("px"));
  }
  else if (val.indexOf("%") > 0)
  {
    val = val.substr(0, val.indexOf("%"));
  }
  return Math.round (val);
}

// =================================================================
function readStyleUnitVal(obj, key)
{
  var val = obj.getStyle(key);
  var unit = "px";
  if (val.indexOf("%") > 0)
  {
    unit = "%";
  }
  return "" + readStyleVal(obj, key) + unit;
}

// =================================================================
function getCurrendLocation ()
{
  var location = " " + window.location;
  location = location.split('?')[0];
  var page = location.split('/NewadaOnWeb/')[1];
  if (("string" != typeof (page)) || page.length == 0) {
  	page = location.split('/newadaJ/')[1];
  }
  return page;
}

// =================================================================
function now_setCookie (key, value)
{
  if (IsDefined (g_mCookie))
  {
    g_mCookie.set (key, value);
  }
}

// =================================================================
function now_getCookie (key)
{
	var val = "";
  if (IsDefined (g_mCookie))
  {
    val = g_mCookie.get (key);
  }
	return val;
}

// =================================================================
function usp_setCookie (key)
{
	if (key.length > 4 && key.indexOf ("usp_") == 0)
	{
		now_setCookie (key,1);
	}
}

// =================================================================
function hasClassNames(oThis, classNames, requiredMatchCount)
{
	
  var hasClsName = false;
  if (IsDefined(oThis))
  {
		// now_debug_out ("hasClassNames (" + classNames + ") " + oThis.className + ", requiredMatchCount:" + requiredMatchCount);
    var clsCount = 0;
    var clsMatchedCount = 0;
    $w(classNames).each(function(oneClass)
    {
      if (oneClass.length > 0)
      {
        clsCount++;
        if (oThis && oThis.hasClassName (oneClass))
        {
          clsMatchedCount++;
        }
      }
    })
    if (1 != requiredMatchCount && (!requiredMatchCount))
    {
      requiredMatchCount = clsCount;
    }
    hasClsName = (clsMatchedCount >= requiredMatchCount) && (clsMatchedCount > 0);
  }
  else
  {
    if (oThis)
    {
      now_debug_out ("not defined: " + oThis.tagName)
    }
  }
	
  return hasClsName;
}

// =================================================================
function addClassNames (oThis, classNames)
{
  if (IsDefined(oThis))
  {
    $w(classNames).each(function(oneClass)
    {
      if (oneClass.length > 0)
      {
        oThis.addClassName (oneClass);
      }
    })
  }
}

// =================================================================
function removeClassNames (oThis, classNames)
{
  if (IsDefined(oThis))
  {
    $w(classNames).each(function(oneClass)
    {
      if (oneClass.length > 0)
      {
        oThis.removeClassName (oneClass);
      }
    })
  }
}

// =================================================================
function getClassValue(classNames, classKeyName)
{
  var classValue = "";
  $w(classNames).each(function(oneClass)
  {
    if (oneClass.indexOf(classKeyName) == 0)
    {
      classValue = oneClass.substr(classKeyName.length);
    }
  })
  return classValue;
}

// =================================================================
function getClassValueKey (clsNames, name)
{
  var val = getClassValue(clsNames, name);
  if (val.length > 0)
  {
    val = " " + name + val;
  }
  return val;
}

// =================================================================
function hasClassValue(classNames, classKeyName)
{
  return "" != getClassValue(classNames, classKeyName);
}

// =================================================================
function getBodyClassValue(classKeyName)
{
  return getClassValue (getBodyClasses (), classKeyName);
}

// =================================================================
function getClassValues(classNames, classKeyName)
{
  var classValues = new Array();
  var nCount      = 0;

  $w(classNames).each(function(oneClass)
  {
    if (oneClass.indexOf(classKeyName) == 0)
    {
      classValues[nCount] = oneClass.substr(classKeyName.length);
      nCount++;
    }
  });
  
  return classValues;
}

// =================================================================
function hasBodyClasses (classNames, requiredMatchCount) 
{
  return hasClassNames (getBody(), classNames, requiredMatchCount);
}

// =================================================================
function hasBodyClass (className) 
{
	var bdyClasses = " " +  getBodyClasses() + " ";
	
  return bdyClasses.indexOf (" " +  className + " ") >= 0;
}

// =================================================================
function addBodyClasses (classNames)
{
  $w(classNames).each(function(oneClass)
  {
	  if (!hasBodyClass (oneClass))
	  {
      addClassNames (getBody(), oneClass);
      g_BodyClassNames = null;
      usp_setCookie (oneClass);
    }
  })
}

// =================================================================
function removeBodyClasses (classNames)
{
  $w(classNames).each(function(oneClass)
  {
	  if (hasBodyClass (oneClass))
	  {
		  
      removeClassNames (getBody(), oneClass);
      g_BodyClassNames = null;
     
      if (IsDefined (g_mCookie))
      {
        if (oneClass.length > 0)
        {
          g_mCookie.del (oneClass);
        }
      }
    }
  })
}

// =================================================================
function setUspBodyClassByCheckbox(oThis)
{
	var val = getClassValueKey (oThis.className, "usp_");
	if (val.length > 0)
	{
		var add = oThis.checked;
		if (oThis.hasClassName("invers"))
		{
			add = !add;
		}
		if (add)
		{
			addBodyClasses (val);
		}
		else
		{
			removeBodyClasses (val);
		}
	}
}

// =================================================================
function keyCodeFromEvent(evt)
{
	var keyCode=0;
	if (window.event) 
	{
		keyCode = window.event.keyCode;
	}
	else 
	{
		if (evt)
		{
			keyCode = evt.which;
		}
	}
	if ("undefined" == typeof (keyCode))
	{
		keyCode=0;
	}
	return keyCode;
}

// =================================================================
function IsControlKey(nKeyCode)
{
	var ret=false;
	if (nKeyCode)
	{
		switch (nKeyCode) 
		{
			case Event.KEY_TAB:
			case Event.KEY_RETURN:
			case Event.KEY_ESC:
			case Event.KEY_LEFT:
			case Event.KEY_RIGHT:
			case Event.KEY_HOME:
			case Event.KEY_END:
			case Event.KEY_PAGEUP:
			case Event.KEY_PAGEDOWN:
			case Event.KEY_UP:
			case Event.KEY_DOWN:			
			case 16:  // Shift --> Fehlt in prototype als Konstante
			case 17:  // STRG
			case 18:  // ALT
			case 91:  // WIN
			case 93:  // Context  // es fehlen noch einige Tasten
				ret=true;
				break;
		}

	}
	else
	{
		ret=true;
	}
	return ret;
}

// =================================================================
function IsRequired(oThis)
{
  var isReq = hasClassNames (oThis, "required");
  if (!isReq) {isReq = hasClassNames (oThis.up("ul"), "required");}
  if (!isReq) {isReq = hasClassNames (oThis.up("li"), "required");}
  if (!isReq) {isReq = hasClassNames (oThis.up("div"), "required");}
  if (!isReq) {isReq = hasClassNames (oThis.up("fieldset"), "required");}
  if (!isReq)
	{ // Pflichtfeld, falls das element im Fehlerfenster enthalten ist.
    isReq = $A($$('#errReportWin div.body li label[for='+oThis.identify()+']')).length > 0;
	}
  return isReq && !IsReadOnly (oThis);
}
// =================================================================
function SetButtonCssClassById (btnId) 
{
  var oBtn = $(btnId);
  if (oBtn) 
  {
    if (oBtn.disabled)
    {
      oBtn.addClassName("disabled");
    }
    else
    {
      oBtn.removeClassName("disabled");
    }
  } 
}

// ==================================================================
function enableBtn(btnId, enable)
{
  var btn = $(btnId);
  if (btn != null)
  {
    SetDisabled(btn,!enable);
    SetButtonCssClassById($(btn).identify());
  }
}

// ==================================================================
function SetCtrlVisible(ctrId, enable)
{
  var ctrl = $(ctrId);
  if (ctrl != null)
  {
    enable ? $(ctrl).removeClassName('hidden') : $(ctrl).addClassName('hidden');
  }
}

// ==================================================================
function $RF(el, radioGroup) 
{   
  if($(el).type && $(el).type.toLowerCase() == 'radio')
	{   
    radioGroup = $(el).name;   
    el = $(el).form;   
  } else if ($(el).tagName.toLowerCase() != 'form') {   
    return false;   
  }   

  var checked = $(el).getInputs('radio', radioGroup).find(   
      function(re) {return re.checked;}   
  );   
  return (checked) ? GetValue(checked) : "";   
} 

// ==================================================================
function getTagName(oThis)
{   
  if (oThis)
  {
    return oThis.tagName.toUpperCase();
  }
  else
  {
    return "";
  }
}
