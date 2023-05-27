
function Calendar_CreateHandle (inputCtrl)
{
  var choiseHandle = GetClientCtrlHandleHtml (inputCtrl, "<span class='calendarHandle' onclick='Calendar_Show(this)' onblur='Calendar_Hide (this)'></span>")
  inputCtrl.insert ({after: choiseHandle});
  newId = inputCtrl.next("span.clientCtrl").identify()
  $(newId).insert ({top: inputCtrl});
}

function clientCtrl_SetCurrendState (oCtrl)
{
	if (oCtrl)
	{
		if (!oCtrl.hasClassName ("clientCtrl"))
		{
			oCtrl = oCtrl.up(".clientCtrl");
		}
		if (oCtrl && IsDefined(oCtrl))
		{
			var inp = $(oCtrl).down("input");
	
			if (inp && IsDefined(inp))
			{
			  oCtrl.select("span.calendarHandle,span.comboHandle").each(function(handle)
				{
					if (IsReadOnly (inp))
					{
						addClassNames (oCtrl, "readonly cursorNoDrop")
						addClassNames (handle, "readonly cursorNoDrop")
					}
					else
					{
						// now_debug_out ("clientCtrl_SetCurrendState: oCtrl remove readonly" + oCtrl.identify());
						// now_debug_out ("clientCtrl_SetCurrendState: handle remove readonly" + handle.identify());
						removeClassNames (oCtrl, "readonly cursorNoDrop")
						removeClassNames (handle, "readonly cursorNoDrop")
					}
				})
			}
		}
	}
}

var aCRefFldCache = new Array();
function CalendarGetLimitDate (limitString)
{
	var limitDate = new Date();
	if (limitString.startsWith('Today'))
	{
		var offset = getClassValue (limitString, 'Today');
		if (offset.length > 0)
		{
			limitDate = new Date(limitDate.getFullYear(),limitDate.getMonth(), parseInt(offset) + limitDate.getDate());
		}
	}
	else if (limitString.startsWith('cRef_'))
	{
		var clsName = getClassValue (limitString, 'cRef_');
		if (clsName.length > 0)
		{
			oFld = aCRefFldCache[clsName];
			if (!IsDefined (oFld))
			{
				var fld = $$("#areaContent input.cName_" + clsName);
				if ($A(fld).length > 0)
				{
					var oFld = fld.first();
					aCRefFldCache[clsName] = oFld;
				}				
			}
			if (IsDefined (oFld))
			{
				limitDate = getFieldCalendarDate(oFld)
		  }
		}
	}
	return limitDate.stripTime();
}

function IsInCalendarLimits (date, className)
{
	var limitMin = getClassValue (className, 'limitMin_');
	if (limitMin.length > 0)
	{
		var minDate = CalendarGetLimitDate (limitMin);
		if (date < minDate)
		{
		  // now_debug_out ("minDate INVALID: ("+date+") < " + minDate);
			return false;
		}
		else
		{
			// now_debug_out ("OK minDate = ("+minDate+") >= checkdate = " + date);
		}
	}

	var limitMax = getClassValue (className, 'limitMax_');
	if (limitMax.length > 0)
	{
		var maxDate = CalendarGetLimitDate (limitMax);
		if (date > maxDate)
		{
		  // now_debug_out ("maxDate INVALID: ("+date+") > " + maxDate);
			return false;
		}
	}
	
	// return(date < (new Date()).stripTime()) && (date.getDay() != 0 && date.getDay() != 6 )
	return true;	
}

function ValidateCalendarDateCheck (date, oThis)
{
	return IsInCalendarLimits (date, oThis.target_element.className)
}

function Calendar_Show (oThis)
{
  var inp = $(oThis).previous("input");
  if (!inp)
  {
    local_alert ("calendar input not found");
    return true;
  }
  else
  {
		var ctrl = inp.up(".clientCtrl");
		clientCtrl_SetCurrendState (ctrl);
		if (!IsReadOnly (inp))
		{
			var format = getClassValue (inp.className, 'Format_');
			new CalendarDateSelect (inp, {time:false, year_range:5, format:format,valid_date_check:ValidateCalendarDateCheck});
		}
    return false;
  }
}

function Calendar_Hide (oThis)
{
  return true;
}

function calendar_validate(oThis)
{
	var cName = getClassValue (oThis.className, 'cName_');
	if (cName.length > 0)
	{ // alle Elemente mit Referenz hierauf suchen
		var refName = "cRef_"+cName;
		oFld = aCRefFldCache[refName];
		if (!IsDefined (oFld))
		{
			$$("#areaContent input.calendar[class*="+refName+"]").each(function(el)
			{
				oFld = el;
				aCRefFldCache[refName] = oFld;
			})
		}
		if (IsDefined (oFld))
		{
			nowValidatorApply(oFld);
		}
	}
	
  return true;
}

function Calendar_Behaviour_Init ()
{
	$$("input.calendar").each(function(el)
	{
		// now_debug_out (el.identify() + ": readonly=" + typeof(el.readonly));
    Calendar_CreateHandle (el);
		clientCtrl_SetCurrendState (el);
    el.addClassName("validate");
	  el.setAttribute( "autocomplete", "off" );

		el.onchange = function()
		{
			return calendar_validate($(this))
		}
		
	})
}

