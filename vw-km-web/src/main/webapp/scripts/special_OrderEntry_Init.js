// var COLREQU='#FFEBCC';
// var COLNOREQ='#FFFFFF';
var dispatchTabIndex = -1;
var NoCheck = false;
var win;
function OrderEntry_Init()
{
	
	// 2010-09-08: Hotfix Auftragsbearbeitung (38/2010 wird nach Serverroundtrip zu 38. Woche 2010)
	// SetValue ("_ctl0_inpRequiredWeek", GetValue("_ctl0_inpRequiredWeek").replace(". Woche ", "/"));
	// SetValue ("_ctl0_inpDesiredDeliveryWeek", GetValue("_ctl0_inpDesiredDeliveryWeek").replace(". Woche ", "/"));
	// Ende: Hotfix Auftragsbearbeitung
	
  OnBrandChangedOe(true);
  OnFinancingChangedOe();
  OnClubcardChangedOe();
  if (HasCommNbrOe())
  {
    SelectGridLineStd(grdRemarks);
  }
  EnableButtonsOe();
  if (typeof(tblAddresses) != "undefined" && typeof(btnAssume) != "undefined")
  {
    SelectAssumeButton(tblAddresses, btnAssume, 1);
    var tblClpSelect = document.getElementById("tblClipCustType");
    if (tblClpSelect != null)
    {
      SetDisabled(tblClpSelect,true);
    }
  }
  //OnBeforeClientUnload = OnBeforeUnloadOrderEntry;

  var isDeliveryClosed = $F(hddIsDeliveryClosed) == "1";
  var deliveryBlock = document.getElementById("trDeliveryBlock");
  var delivery = document.getElementById("divDelivery");
  if (delivery != null && deliveryBlock != null)
  {
    SetCtrlVisible (deliveryBlock, isDeliveryClosed);
    SetCtrlVisible (delivery, !isDeliveryClosed);
    // deliveryBlock.style.display = isDeliveryClosed ? "block" : "none";
    // delivery.style.display = isDeliveryClosed ? "none" : "block";
  }

  var conditionClosed = document.getElementById(hddIsConditionClosed);
  var isConditionClosed = (conditionClosed != null && conditionClosed.value == "1");
  var conditionBlock = document.getElementById("trConditionBlock");
  var conditions = document.getElementById("divConditions");
  if (conditions != null && conditionBlock != null)
  {
    SetCtrlVisible (conditionBlock, isConditionClosed);
    SetCtrlVisible (conditions, !isConditionClosed);
  //  conditionBlock.style.display = isConditionClosed ? "block" : "none";
  //  conditions.style.display = isConditionClosed ? "none" : "block";
  }


  var isCustomerClosed = $F(hddIsCustomerClosed) == "1";
  var customerBlock = document.getElementById("trCustomerBlock");
  var customerAdd = document.getElementById("divCustomer");
  if (customerBlock != null && customerAdd != null)
  {
    SetCtrlVisible (customerBlock, isCustomerClosed);
    SetCtrlVisible (customerAdd, !isCustomerClosed);
   // customerBlock.style.display = isCustomerClosed ? "block" : "none";
   // customerAdd.style.display = isCustomerClosed ? "none" : "block";
  }

  var vehicleEditClosed = document.getElementById(hddIsVehicleEditClosed);
  var vehicleViewClosed = document.getElementById(hddIsVehicleViewClosed);

  var isVehicleEditClosed = (vehicleEditClosed != null && vehicleEditClosed.value == "1");
  var vehicleBlock = document.getElementById("trVehicleBlock");
  var vehicleAdd = document.getElementById("tblVehicleAdd");
  var vehiclePrice = document.getElementById("tblVehiclePrice");

  if (vehicleBlock != null && vehicleAdd != null && vehiclePrice != null)
  {
    SetCtrlVisible (vehicleBlock, isVehicleEditClosed);
    SetCtrlVisible (vehicleAdd, !isVehicleEditClosed);
    SetCtrlVisible (vehiclePrice, !isVehicleEditClosed);
   // var state = isVehicleEditClosed ? "none" : "block";
   // vehicleBlock.style.display = isVehicleEditClosed ? "block" : "none";
   // vehicleAdd.style.display = state;
   // vehiclePrice.style.display = state;
  }
  
  OnProcessStateChangedOe();
  vehiclePrNumberList_Init();
}

function ltmChange(sValue)
{
      if( sValue.toLowerCase() == 'true' ) {
        if (isLTM != true) {
          isLTM = true;
          ltmChanged ();
        }
      }
      else {
        if (isLTM != false) {
          isLTM = false;
          ltmChanged ();
        }
      }
}

function ltmChanged () {
  var vwCar1 = document.getElementById(trLtmVwcar1);
  var audiCar1 = document.getElementById(trLtmAudi1);
  if (vwCar1 != null && audiCar1 != null)
  {
    if (vwCar1.value != "")
		SetCtrlVisible (vwCar1, !isLTM);
		SetCtrlVisible (audiCar1, isLTM);
  }
}

function OeInternClone (oThis)
{
	// now_debug_out(oThis.identify());
	var dst = "";
	var src = "";

	switch (oThis.identify())
	{
		// Weeks
		case "_ctl0_inpRequiredWeek":
			dst = "_ctl0_inpDesiredDeliveryWeek";
			src = "_ctl0_inpRequiredWeek";
			break;
		case "_ctl0_inpDesiredDeliveryWeek":
			dst = "_ctl0_inpRequiredWeek";
			src = "_ctl0_inpDesiredDeliveryWeek";
			break;
	}
	
	if ("" != dst)
	{
		SetValue($(dst), $F(src));
	  nowValidatorSetValid($(dst), nowIsValid ($(dst)));
	}

}

function OnBeforeUnloadOrderEntry()
{
  ReadGridDataOe();  
}
function EnableButtonsOe()
{
  var eva = $(hddIsEvaRequest);
  if (eva)
  {
    var isEvaRequest = (eva.value == "1");
    if (GetCustomRequiredOe())
    {
      var varHasCommNbrOe = HasCommNbrOe();
      var save = $(btnOrderSave);
      if (save)
      {
        SetDisabled(save,varHasCommNbrOe);
      }
      var detail = $(btnDetail);
      var show = $(btnSearchCustId);
      if (detail && show && eva)
      {
        SetDisabled(detail,true);
        SetDisabled(show, isEvaRequest || varHasCommNbrOe);

        if (typeof(ClpDivId) == "undefined" && !varHasCommNbrOe)
        {
          var input = $F(inpCustomer);
					var name = GetValue(txtLastName);
					var first = GetValue(txtFirstName);
          var isEvaWithoutCustNbr = isEvaRequest && GetCustomRequiredOe() && name != "" && input == "";
          var disable = (input.length < 6);
          var custDisabled = disable || (name == "" || name == " ") && (first == "" || first == " ");
            
          SetDisabled(detail, custDisabled || input.length != 6);
          SetDisabled(show, disable || isEvaRequest);
          CheckCustomerCombosOe(!custDisabled || isEvaWithoutCustNbr);
        }
        enableBtn(btnMoreRemarks, !HasCommNbrOe());
      }
      enableBtn(btnNewAddress, !varHasCommNbrOe && !isEvaRequest);
      enableBtn(btnSearchAddress, !varHasCommNbrOe && !isEvaRequest);
    }
    else
    {
      enableBtn(btnDetail, false);
      enableBtn(btnSearchCustId, false);
      enableBtn(btnSearchAddress, false);
      enableBtn(btnNewAddress, false);
    }
  }

}

function ReadGridDataOe()
{
	GetGridData(grdInclude, "INPUT", 0, hddInclude);
	GetGridDataSep(grdRemarks, "INPUT", 0, hddRemarks, "\n");
	GetGridData(grdRemarks, "SELECT", 0, hddRemarkTypes);
}
function PrepareNewInputOe()
{
  ReadGridDataOe();
	PrepareServerClick();
}
function PrepareConfigOe()
{
  ReadGridDataOe();
	PrepareServerClick();
}
function OnConfigMouseUp()
{
  NoCheck = false;
}
function OnConfigMouseDown()
{
  NoCheck = true;
  var ctrl = document.getElementById(inpModelKey);
  if (ctrl != null)
	{
		ctrl.onchange = null;
	}
}
function OnBrandChangedOe(isOnLoad)
{
  var dispatchType = $(selDispatchType);
  var brand = $(selBrand);
	if (dispatchType != null && brand != null)
	{
    if (!isOnLoad)
    {
		  ClearInputByName(inpModelKey);
		  //OnModelKeyChangedOe(false);
    }
    else
    {
      // dispatchTabIndex = brand.tabIndex;
    }

    var disabledAudi = (brand.value != "50000");
    //TargetChanged();

    if (!HasCommNbrOe())
    {
      //dispatchType.style.backgroundColor = disabledAudi ? COLNOREQ : COLREQU;
      // dispatchType.style.backgroundColor = COLNOREQ;
			// set_background_color_class_legacy (dispatchType, "COLNOREQ");
			$(dispatchType).removeClassName("required");
    }
    SetDisabled(dispatchType, disabledAudi);

    if (disabledAudi)
    {
      // dispatchTabIndex = brand.tabIndex;
      // brand.tabIndex = -1;
    }
    else
    {
      // brand.tabIndex = dispatchTabIndex;
    }
    
    var brand = document.getElementById(selBrand);
    
    ltmChanged();
    
		var lblSuppDealer = document.getElementById(lblSupportingDealer);
		var inpSuppDealer = document.getElementById(inpSupportingDealer);
		if (lblSuppDealer != null && inpSuppDealer != null && brand != null)
		{
			var isVwCar = (brand.value == "30000");

      SetCtrlVisible (lblSuppDealer, isVwCar);
      SetCtrlVisible (inpSuppDealer, isVwCar);
			// lblSuppDealer.style.display = isVwCar ? "block" : "none";
			// inpSuppDealer.style.display = isVwCar ? "block" : "none";
    }
  }
  SetPromotionsOe();
	ReadGridDataOe();
}
function OnProgramChangedOe(update)
{
	var isOk = true;
  var input = document.getElementById(inpProgram);
  if (input != null && update)
  {
    var len = input.value.length;
    if (len == 0)
		{
			isOk = false;
		}
		else if (len < 3)
    {
			isOk = false;
	  }
  }
  if (update)
  {
    DisablePricesOe();
  }
	return isOk;
}
function OnModelYearChangedOe(update)
{
	var isOk = true;
  var input = document.getElementById(inpModelYear);
  if (input != null && update)
  {
    var len = input.value.length;
	  if (len == 1 || len == 3)
		{
			isOk = false;
		}
  }
  if (update)
  {
    DisablePricesOe();
  }
	return isOk;
}
function OnModelKeyChangedOe(update, checkNull)
{
	ClearTextByName(txtModelDesc);
	ClearTextByName(txtModelPrice);
	if (OnInteriorChangedOe(update, false))
	{
		OnExteriorChangedOe(update, false);
  }
  ReadGridDataOe();
	//__doPostBack(inpModelKey, '');
}
function OnInteriorChangedOe(update, checkNull)
{
	var isOK = true;
	ClearTextByName(txtInteriorDesc);
	ClearTextByName(txtInteriorPrice);
  var input = document.getElementById(inpInteriorPr);
  if (input != null && update)
  {
    var len = input.value.length;
    if (len == 0 && checkNull)
    {
			isOk = false;
	  }
		else if (len < 2 && len > 0 || checkNull && len == 0)
		{
			isOk = false;
		}
  }
  if (update)
  {
    DisablePricesOe();
  }
}
function OnExteriorChangedOe(update, checkNull)
{
	var isOk = true;
	ClearTextByName(txtExteriorDesc);
	ClearTextByName(txtExteriorPrice);
  var input = document.getElementById(inpExteriorPr);
  if (input != null && update)
  {
    var len = input.value.length;
    if (len == 0 && checkNull)
    {
			isOk = false;
	  }
		else if (len < 4 && len > 0 || checkNull && len == 0)
		{
			isOk = false;
		}
  }
  if (update)
  {
    DisablePricesOe();
  }

	return isOk;
}
function OnChangePrOe(oThis)
{
  DisablePricesOe();
}

function OnFocusTypeOe(oThis)
{
  if ($(oThis))
  {
    var td = $(oThis).up("td");
    if (!IsDefined (td.next("td")) || !IsDefined (td.next("td").down("select")))
    {
      AddNewGridLine(grdRemarks,1,8,btnMoreRemarks);
    }
    else
    {
      now_debug_out ("has following td ("+oThis.identify()+")");
    }
  }
}
function DisablePricesOe()
{
  ClearGridCol(grdInclude, 2);
  ClearTextByName(txtModelPrice);
  ClearTextByName(txtInteriorPrice);
  ClearTextByName(txtExteriorPrice);
  ClearTextByName(txtPriceGros);
  ClearTextByName(txtPriceNet);
  ClearTextByName(txtListPrice);
  ClearTextByName(txtPrice);
  var ctrl = document.getElementById(hddHasPrices);
  if (ctrl != null)
  {
    ctrl.innerText = "0";
  }
}
function ClearIncludeTableOe()
{
	ClearTableByName(grdInclude, 1);
}

function OnCustomerChangedOe(oThis)
{
  var custNo = $F(oThis);
  if (custNo != null)
  {
    var enable = true;
    if (custNo.length > 0 && custNo.length < 6)
    {
      enable = false;
    }
  }

}
function OnCustomerChangedOe2(oThis)
{
  var custNo = $F(oThis);
  var enable = true;


  if( custNo != null && custNo.length < 6 )
  {
      enable = false;
  }
  
  enableBtn(btnDetail, enable);

}

function OnCustomerKeyUpOe()
{
	SetValue(txtLastName, "");
	SetValue(txtSolitary, "");
	SetValue(txtFirstName, "");
	SetValue(txtStreet, "");
	SetValue(txtHouseNbr, "");
	SetValue(txtCountry, "");
	SetValue(txtCity, "");
	SetValue(txtPob, "");

  if (!HasCommNbrOe())
  {
    EnableButtonsOe();   
  }
}

function ClearButtonActionOe()
{
  if (typeof(hddCommand) != "undefined")
  {
		SetValue(hddCommand, "");
  }
}
function OnFinancingChangedOe()
{
  if (typeof(selFinancing) != "undefined" && typeof(selLeasingCompany) != "undefined")
  {
    var financing = document.getElementById(selFinancing);
    var leasing = document.getElementById(selLeasingCompany);
    if (financing != null && leasing != null)
    {
      var disabled = (financing.value != "LEASING");
      SetDisabled(leasing, disabled);
			if (disabled)
			{
				leasing.addClassName("disabled");
			}
			else
			{
				leasing.removeClassName("disabled");
			}
    }
  }
}
function OnClubcardChangedOe()
{
  var clubcard = $(selClubCard);
  var varHasCommNbrOe = HasCommNbrOe();
  if (clubcard != null)
  {
    var disabled = (clubcard.value == "" || clubcard.value == "NO_ATTENDANCE") || varHasCommNbrOe || !GetCustomRequiredOe();
    if (!varHasCommNbrOe)
    {
			SetReadOnly(selClubCardSendTo, !disabled, disabled, false)    
    }
  }
}
function HasCommNbrOe()
{
  var CommNbr = document.getElementById(inpCarCommissionNbr);
  return (CommNbr != null && CommNbr.value != "");
}
function OnProcessStateChangedOe()
{
  var seller = document.getElementById(inpSeller);
  var rights = $F(hddRightLevel);

  if (seller)
  {
    var required = GetCustomRequiredOe();
    var customerAll = $('divCustomerAll');
    if (customerAll)
    {
			// customerAll.style.display = required ? "block" : "none";
      SetCtrlVisible (customerAll, required);
		}
    if (required)
		{
			var varHasCommNbrOe = HasCommNbrOe();
			if ($F(selCustomerSign) == "RETAIL_CUSTOMER" && rights == "1" && !varHasCommNbrOe)
			{
				$(seller).addClassName("required");
			}
	   
			var isEva = $F(hddIsEvaRequest) == "1";
			SetReadOnly(inpCustomer, !isEva && !varHasCommNbrOe, isEva || varHasCommNbrOe, false);
			
		}
    CheckCustomerCombosOe(required);
    OnClubcardChangedOe();
    
    EnableButtonsOe();  
	  SetPromotionsOe();
  }
}
function GetCustomRequiredOe()
{
  var state = document.getElementById(selProcessState);
  return state != null && (state.value == "CUSTOMER_ORDER" || state.value == "DEMONSTRATION_CAR_ORDER");
}
function SetComboOe(selCombo, required, enabled)
{
  SetReadOnly(selCombo, required, !enabled, false);
}
function CheckCustomerCombosOe(required)
{
  if (!HasCommNbrOe())
  {
	  SetComboOe(selCustomerSign, required, false);
	  SetComboOe(selCustomerGroup, required, required);
	  SetComboOe(selSpecialCustomer, false, required);
	  SetComboOe(selClubCard, required, required);
	}
}

function DesiredDeliveryWeekChangeOe()
{
	SetValue(inpRequiredWeek, $F(inpDesiredDeliveryWeek));
}
function RequiredWeekChangeOe()
{
	SetValue(inpDesiredDeliveryWeek, $F(inpRequiredWeek));
}
function SetPromotionsOe()
{
  var isReadOnly = ($F(selProcessState) != "CUSTOMER_ORDER") || HasCommNbrOe();
	//SetReadOnly(inpPromotionAction1, false, isReadOnly, true);
	//SetReadOnly(inpPromotionAction2, false, isReadOnly, true);
	//SetReadOnly(inpPromotionAction3, false, isReadOnly, true);
	//SetReadOnly("btnPromotionHelpWindowEva", false, isReadOnly, true);
	if (isReadOnly) $(inpPromotionAction1).removeClassName("required");
	else $(inpPromotionAction1).addClassName("required");
}


// ********************************************
// Spezialfunktion
// ********************************************
function AddNewVehiclePrLines(moreBtn)
{
	// now_debug_out("AddNewVehiclePrLines 60");
	AddNewVehiclePrLine(moreBtn, 60);
  vehiclePrNumberList_Init();
}

function callSkipInputPrBlock(oThis)
{
  var oSkipTarget = $$(".SkipInputPrBlockTarget");
	if ($A(oSkipTarget).length > 0)
	{
		SetFocus(oSkipTarget.first());
	}
  else
  {
    now_debug_out ("function callSkipInputPrBlock: no element with class-name (SkipInputPrBlockTarget) found");
  }
}

// ********************************************
// Dublikate aus special_OrderEntry20_Init.js
// ********************************************
function PreVehicleChange() 
{
  var pvVal = $RF(getForm (), 'inputPV');

  oPvVal = $$("div.orderControl input[value="+pvVal+"]");

  var oInp = $$('input.pv').first();
  oInp.value = pvVal;

  if (1 == oPvVal.length)
  {
    oInp.title = oPvVal.first().title;
  }

  dragWindowHide($('DivPreVehicleHelp').identify());
  return false;
}

function oePromotionHelpWindowEva (oThis)
{
  var oDragWnd = dragWindowSelect('.DivPromotionHelp');
  if (oDragWnd)
  {
		oThis = $(oThis);
		oDragWnd = dragWindowShow (oDragWnd, true);
		uncheckRadioGroup(oDragWnd, "inputVfm1");
		uncheckRadioGroup(oDragWnd, "inputVfm2");
		uncheckRadioGroup(oDragWnd, "inputVfm2");
		var thisParent = oThis.up();
		var vfm1 = GetValue (thisParent.down("input.vfm1"));
		var vfm2 = GetValue (thisParent.down("input.vfm2"));
		var vfm3 = GetValue (thisParent.down("input.vfm3"));
		SetValue (oDragWnd.down('input[name=inputVfm1][value=' + vfm1 + ']'), vfm1);
		SetValue (oDragWnd.down('input[name=inputVfm2][value=' + vfm2 + ']'), vfm2);
		SetValue (oDragWnd.down('input[name=inputVfm3][value=' + vfm3 + ']'), vfm3);
	}
  return false;	
}

function oePreVehicleHelpWindowEva (oThis)
{
  var oDragWnd = dragWindowSelect('.DivPreVehicleHelp');
  if (oDragWnd)
  {
	  // oDragWnd = dragWindowShow (oDragWnd, true, oThis);
	  oDragWnd = dragWindowShow (oDragWnd, true);
		var val = GetValue ($(oThis).up().down("input.pv")).toUpperCase();
		var theRadio = oDragWnd.down("ul.body li input[value="+val+"]");
		if (IsDefined (theRadio))
		{
			// now_debug_out ("SetValue of " + theRadio.identify() + " value=" + val);
			SetValue (theRadio, val);
		}
		else
		{
			// now_debug_out ("Set undefined value to SONSTIGE: " + val);
			SetValue (oDragWnd.down("ul.body li input[value=XXX]"), "XXX");
		}
	}
  return false;
}

function PromotionChange() 
{
  var oForm = getForm ();
  $$('input.vfm1').first().value = $RF(oForm, 'inputVfm1');
  $$('input.vfm2').first().value = $RF(oForm, 'inputVfm2');
  $$('input.vfm3').first().value = $RF(oForm, 'inputVfm3');
  dragWindowHide($('DivPromotionHelp').identify());
  return false;
}
