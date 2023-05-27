
function skipPrBlock(oThis)
{

  if ("function" == typeof (callSkipInputPrBlock))
  {
    callSkipInputPrBlock(oThis);
  }
  else
  {
    now_debug_out ("Skip PR-Block: function callSkipInputPrBlock(oThis) not found");
  }
}

function AddEquipementLines(count)
{
  var moreBtn = $$("button.btnMorePrNumbers").first().identify();
	AddNewVehiclePrLine(moreBtn, 60)
}

function AddNewVehiclePrLine(moreBtn, count)
{
  if( "function" == typeof(AddNewPrLine_OrderCheck_CarConfig) )
  {
    return AddNewPrLine_OrderCheck_CarConfig(moreBtn);
  }
  else
  {
		if (!count || count < 1)
		{
			count = 12;
		}
    AddNewGridLine($$("table.vehiclePrNumberList").first().identify(),0,count,moreBtn);
  }
}

function getOrderEntryInputPr(oThis, direction)
{
	var dirTd = null;
	if ("prev" == direction)
	{
		dirTd = oThis.up("td").previous("td");
	}
	else
	{
		dirTd = oThis.up("td").next("td");
	}
  var prInp = null;
	if (dirTd)
	{
		prInp = dirTd.down("input.prNumber");
    if (!IsDefined(prInp))
    {
      dirTd = null;
    }
	}
	if (!dirTd)
	{
		// now_debug_out ("row: " + direction);
		var dirTr = null;
		if ("prev" == direction)
		{
			dirTr = oThis.up("tr").previous("tr");
		}
		else
		{
			dirTr = oThis.up("tr").next("tr");
		}

		if (dirTr)
		{
			if ("prev" == direction)
			{
				prInp = dirTr.select("input.prNumber").last();
			}
			else
			{
				prInp = dirTr.down("input.prNumber");
			}
      if (!IsDefined(prInp))
      {
        prInp = null;
      }
		}		
		
	}
	var dirInp = null;
	if (prInp)
	{
		dirInp = $(prInp);
	}
	return dirInp;
}
function OrderEntryPrevInputPr(oThis)
{
	var prInp = getOrderEntryInputPr(oThis, "prev");
	if (prInp)
	{
		SetFocus(prInp);
	}
}
function OrderEntryNextInputPr(oThis)
{
	var prInp = getOrderEntryInputPr(oThis, "next");
	if (prInp)
	{
		SetFocus(prInp);
		// now_debug_out ("OrderEntryNextInputPr..");
	}
  else
  {
    skipPrBlock(oThis)
  }
}

function MustSkipInputPrBlock(oThis)
{
	var prInp = getOrderEntryInputPr(oThis, "prev");
	if (prInp && ("" == $F(prInp)))
	{
		prInp = getOrderEntryInputPr(prInp, "prev");
		if (prInp && ("" == $F(prInp)))
		{
			return true;
		}
	}
	return false;
}


function vehiclePrNumberList_Init()
{
  // now_debug_out("vehiclePrNumberList_Init...");
	$$("table.vehiclePrNumberList input.prNumber").each(function(el)
	{
		el.addClassName("uppercase alphaNumeric");
		if (hasBodyClass ("Mandant_0"))
		{
			el.addClassName("min_3 max_3");
		}
		else
		{
			el.addClassName("min_4 max_4");
		}
    
		el.onkeydown = function(evt)
		{
			if (32 == keyCodeFromEvent(evt))
			{ // space ignorieren
				return false;
			}
			return true;
		}

		el.onkeyup = function(evt)
		{
			var oThis = $(this);
			var keyCode = keyCodeFromEvent(evt);
      
			if (32 == keyCode)
			{ // space ignorieren
				// now_debug_out ("skip keycode = " + keyCode);
				var val = $F(oThis);
				SetValue (oThis, val.trim());
				return false;
			}
			
			if (keyCode == Event.KEY_TAB)
			{
				if (MustSkipInputPrBlock(oThis))
				{
          skipPrBlock(oThis);
				}				
			}
			else
			{
        if (hasClassValue(oThis.className, "ajax"))
        {
          ajaxAutoComplete_OnKeyUp(evt, oThis);
        }
				// now_debug_out ("keyCode=" + keyCode);
				var maxLen = getMaxFieldLength (oThis);
				var val = $F(oThis);
				if (maxLen > 0 && (val.length > maxLen))
				{
					SetValue (oThis, val.substr(0, maxLen));
				}
				if (keyCode > 47)
				{
					if ($F(oThis).length > 0 && nowValidateLengthRange(oThis, oThis.className))
					{
						// now_debug_out ("nowValidateLengthRange ("+oThis.className+"): " + nowValidateLengthRange(oThis, oThis.className));
						OrderEntryNextInputPr(oThis);
            if ("function" == typeof (ReadGridData)) {ReadGridData ();}
					}
				}
			}
			
			return nowKeyUpBehaviour(oThis, keyCode);
		}

		el.onblur = function(evt)
		{
      var keyCode = keyCodeFromEvent(evt);
			var oThis = $(this);
			// nowKeyUpInputBehaviour(oThis, keyCode);
			nowKeyUpBehaviour (oThis, keyCode, "blurKey");
			nowValidatorApply(oThis);
			if ("function" == typeof (ReadGridDataOe2))
			{
				ReadGridDataOe2();
			}
			return true;
		}
		el.onfocus = function(evt)
		{
			var oThis = $(this);
      var keyCode = keyCodeFromEvent(evt);
			var prInp = getOrderEntryInputPr(oThis, "next");
			if (!prInp)
			{
				//now_debug_out ("check more pr");
        
        if ("function" == typeof (AddNewVehiclePrLines))
        {
          AddNewVehiclePrLines($$("button.btnMorePrNumbers").first().identify());
        }
        else
        {
          now_debug_out ("Missing Function AddNewVehiclePrLines");
        }
        
				
			}
			nowFieldValidatorOnFocus(oThis, keyCode);
			return true;
		}
	})
	
}

















