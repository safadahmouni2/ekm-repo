// jedes text-Eingabefeld mit dem Klassennamen="submitOnEnter" wird definiert eine zugehörige Schaltfläche (submit_btnCommissionNbr)
// Alles was nach dem Prefix submit_ steht, definiert den zugehörigen Button, also hier "btnCommissionNbr".
// Der erste Button mit diesem Klassennamen wird dann ausgeführt
//
// <input class="submitOnEnter submit_btnCommissionNbr" ...
// <button class="btnCommissionNbr"...
function getSubmitOnEnterButton(oThis)
{
	var theBtn = null;
	if (IsDefined(oThis))
	{
		var btn = getClassValue (oThis.className, 'submit_');
	 // now_debug_out ("*** getSubmitOnEnterButton: oThis.className = " + oThis.className);
		if ("" == btn)
		{
			btn = $$('.mainSubmit');					
		}
		else
		{
			btn = $$("." + btn);
		}
		if (IsDefined (btn))
		{
			theBtn = btn.first();
			// if (IsDefined (theBtn)) {now_debug_out ("btn FOUND: id = " + theBtn.identify());}
		}
	}
	return theBtn;

}

function handleSubmitOnEnter(oThis, keyCode)
{
  // now_debug_out ("handleSubmitOnEnter: " + oThis.identify());
	var theBtn = getSubmitOnEnterButton(oThis);
	if (IsDefined(theBtn))
	{
		if (13 == keyCode)
		{
			isValid = nowIsValid (oThis);
			SetDisabled (theBtn, !isValid);
			if (!isValid)
			{
				nowValidatorSetValid(oThis, isValid);
				return false;
			}
			// now_debug_out ("theBtn.click id = " + theBtn.identify());
			set_modal(true);
			theBtn.click();
		}
		else
		{
			isValid = nowIsValid (oThis) && nowValidateMinLength(oThis);
			// now_debug_out("theBtn:" + theBtn.identify() + "valid=" + isValid);
			nowValidatorSetValid(oThis, isValid);
			SetDisabled (theBtn, !isValid);
			// return isValid;
		}
		return false;
	}	
	return true;
}

function handleFormSubmitOnEnter(oThis, keyCode)
{
	var ret = true;
	if (13 == keyCode)
	{
		ret = handleSubmitOnEnter(oThis, keyCode);
		if (ret)
		{
			if (!nowIsValid (oThis))
			{
				nowValidatorSetValid(oThis, false);
				ret = false;
			}
			else
			{
				set_modal(true);
				var aBtns = $$('.formSubmitOnEnter');
				if ($A(aBtns).length > 0)
				{
					var theBtn = aBtns.first();
					theBtn.click();
					ret = false;
				}
			}
		}
	}

	return ret;
}

function submitOnEnter_Init()
{
	$$('.formSubmitOnEnter').each(function(el)
  {
    var theForm = $$('form').first();
		theForm.onkeypress = function(evt)
    {
			return handleFormSubmitOnEnter($(this), keyCodeFromEvent (evt))
		}	
  })

	$$('input[type=text].submitOnEnter').each(function(el)
  {
    // local_alert ("submitOnEnter: init");
//		el.onkeydown = function(evt) 
		el.onkeyup = function(evt) 
    {
			return handleSubmitOnEnter($(this), keyCodeFromEvent (evt));
		}	
		return true;
	})
}
