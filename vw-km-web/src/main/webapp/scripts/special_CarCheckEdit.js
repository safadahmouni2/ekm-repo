function NullFct(){};
var OnModelKeyChangedFct = new NullFct();

function OnCarCheckEditLoad()
{
	if (typeof(inpModelKey) != "undefined")
	{
		var input = document.getElementById(inpModelKey);
		if (input != null)
		{
			OnModelKeyChangedFct = input.onchange;
			input.onchange=null;
		}
	}
}
function PrepareEditClick()
{
	GetGridData(grdPrInclude, "INPUT", 0, hddPrInclude);
	PrepareServerClick();
}
function OnModelKeyBlur(ctrl)
{
	if (ctrl != null && ctrl.id == inpModelKey && OnModelKeyChangedFct != null)
	{
		nowValidatorSetValid(ctrl, nowIsValid (ctrl));
		if (nowIsValid (ctrl))
		{
			OnModelKeyChangedFct();
		}
	}
}
function OnProgramChanged()
{
  DisablePrices();
}
function OnModelYearChanged(oThis)
{
  DisablePrices();
}
function OnModelKeyChanged(oThis)
{
	ClearTextByName(txtModelDesc);
  DisablePrices();  
  PrepareEditClick();
  if (!IsSelectModeCalledOe2())
  {
    __doPostBack(inpModelKey, '');
  }
}
function InteriorChanged(oThis, keyCode)
{
	ClearTextByName(txtInteriorDesc);
  ClearInteriorPrices()
 	ClearTotalPrices();
}
function ExteriorChanged(oThis, keyCode)
{
	ClearTextByName(txtExteriorDesc);
  ClearExteriorPrices()
 	ClearTotalPrices();
}
function OnChangePr(ctrl)
{
 	ClearTotalPrices();
  var len = ctrl.value.length;
	var parent = ctrl.parentElement; // TD
	if (parent != null)
	{
		var text = parent.nextSibling;
		if (text != null)
		{
			text.innerText = "";
			var nextText = text.nextSibling;
			if (nextText != null)
			{
				nextText.innerText = "";
			}
			nextText = text.nextSibling;
			if (nextText != null)
			{
				nextText.innerText = "";
			}
			nextText = text.nextSibling;
			if (nextText != null)
			{
				nextText.innerText = "";
			}
			nextText = text.nextSibling;
			if (nextText != null)
			{
				nextText.innerText = "";
			}
		}
	}
}
function OnPrKeyUp(input)
{
  if (Prototype.Browser.IE) 
  {
    if (IsAlphaNumeric(String.fromCharCode(event.keyCode)))
    {
      HandleLastClick(input,1,60,btnMorePrNumbers);
    }
  }
}
function OnPrKeyDown(input)
{
  if (Prototype.Browser.IE) 
  {
    if (event.keyCode == 9 && !event.shiftKey)
    {
      HandleLastClick(input,1,60,btnMorePrNumbers);
    }
  }
}
function OnFocusType(oThis)
{
	now_debug_out ("special_CarCheckEdit OnFocusPr: handle AddNewGridLine obsolete or generic???");
	
  var combo = $(oThis);
  if (combo)
  {
		var parent = combo.parentElement; // TD
		if (parent != null && parent.parentElement != null)
    {
   		var parent = parent.parentElement;
      if (parent != null && parent.nextSibling == null)
      {
        AddNewGridLine(grdRemarks,1,60,btnMorePrNumbers);
      }
    }
  }
}
function DisablePrices()
{
	ClearTotalPrices();
  ClearInteriorPrices();
  ClearExteriorPrices()
  ClearModelPrices();
  ClearEquipmentPrices()
  SetValue(hddHasPrices, "0");
}
function 	ClearTotalPrices()
{
  if (typeof(txtPriceNet) != "undefined")
  {
    ClearTextByName(txtPriceNet);
    ClearTextByName(txtPriceGross);
    ClearTextByName(txtVat);
  }
  SetValue(hddHasTotalPrices, "0")
}
function ClearInteriorPrices()
{
  ClearTextByName(txtInteriorPriceNet);
  ClearTextByName(txtInteriorPriceGross);
}
function ClearExteriorPrices()
{
  ClearTextByName(txtExteriorPriceNet);
  ClearTextByName(txtExteriorPriceGross);
}
function ClearModelPrices()
{
  ClearTextByName(txtModelPriceNet);
  ClearTextByName(txtModelPriceGross);
}
function  ClearEquipmentPrices()
{
  ClearGridCol(grdPrInclude, 2);
  ClearGridCol(grdPrInclude, 3);
}
