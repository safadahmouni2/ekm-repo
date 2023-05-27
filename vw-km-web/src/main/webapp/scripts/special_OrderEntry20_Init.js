
var COLPRODD='#EFEFEF';
var COLHEAD=COLPRODD;
var COLPREVEN='#F6F6F6';
var NoCheck = false;
var win;

function callSkipInputPrBlock(oThis)
{
	SetFocus("_ctl0_oeCommissionCtrl_btnCheckSpecificationDirect");
}

function AddNewVehiclePrLines(moreBtn)
{
	// now_debug_out("AddNewVehiclePrLines 60");
	AddNewVehiclePrLine(moreBtn, 60)
}

function saveChange () {
}


function writeEvaContractNumber (sValue) {
  $(inpEvaContractNumber).value = sValue;
  var oDragWnd = $(dragWindowCloneId ('DivEvaContractList'));
  dragWindowHide(oDragWnd);
}
// AJAX Funktionen für OrderEntry Auswahlmodus

function viewEvaContractList (sValue)
{
  var oDragWnd = $(dragWindowCloneId ('DivEvaContractList'));
  if (!oDragWnd)
  {
    oDragWnd = dragWindowSelect('.DivEvaContractList');
  }  
  
  if (oDragWnd)
  {
		var div = oDragWnd.down("div");
		if (div)
		{
		  oDragWnd.down("div").innerHTML = sValue;
		}
	  oDragWnd = dragWindowShow (oDragWnd, true);
	}
  return false;
}

function ltmChange(sValue)
{
      if( sValue.toLowerCase() == 'true' ) {
        if (isLTMValue != true) {
          isLTMValue = true;
          ltmChanged ();
        }
      }
      else {
        if (isLTMValue != false) {
          isLTMValue = false;
          ltmChanged ();
        }
      }
}

// ENDE Ajax Funktionen

function OrderEntry20_Init()
{
	
	vehiclePrNumberList_Init();
  OnFinancingChangedOe2();
  OnBrandChangedOe2(true);
  SetCustomTabOe2();

  if (GetValue(hddIsSpecialOrder)=="1") 
  {
    SetSpecialTabOe2();
  }
  //if (GetValue(hddIsDeliveryClosed)=="1") 
  //{
    CheckDeliveryCtrlOe2();
  //}
	
  if (IsShowModeOe2())
  {
    SelectGridLineStd(grdRemarks);
  }
  EnableButtonsOe2();
  if (typeof(tblAddresses) != "undefined" && typeof(btnAssume) != "undefined")
  {
    SelectAssumeButton(tblAddresses, btnAssume, 1);
  }
	
	OnBeforeClientUnload = OnBeforeUnloadOrderEntry;
  OnCarConfigLoad();
  OnCarCheckEditLoad();
	
  var hasCommNbr = (GetValue(inpCarCommissionNbr).length > 5);
	enableBtn(btnShowCommNbr, hasCommNbr);

  CustomerSelect();
  OnProcessStateChangedOe2();
  ReadGridDataOe2();
	
  //enableBtn(btnOrderSave, GetValue(hddSpecialIsSaved) == "1" || GetValue(hddIsSpecialOrder) == "0");
}

function SetOrderBrandChangedOe2()
{
  SetBrandChanged();
  OnBrandChanged4Order();
}
function OnBeforeUnloadOrderEntry()
{
  ReadGridDataOe2();  
}

function EnableButtonsOe2()
{
  //var isEvaRequest = GetValue(hddIsEvaRequest) == "1";
  var isEnterMode = !IsShowModeOe2() && typeof(ClpDivId) == "undefined";
  var enabled = isEnterMode && GetCustomRequiredOe2();

  var inputCustomer = GetValue(inpCustomer);
  var inputDriver = GetValue(inpDriver);
	
  var custEnabled = (inputCustomer.length == 6);
  var driverEnabled = (inputDriver.length == 6);

	var lastNameCustomer = GetValue(txtLastNameCustomer);
	var firstNameCustomer = GetValue(txtFirstNameCustomer);
  var custContEnabled = custEnabled && (lastNameCustomer.length > 0 || firstNameCustomer.length > 0);
	
  CheckCustomerCombosOe2(custContEnabled);

  enableBtn(btnMoreRemarks, isEnterMode);
  // customer buttons
  
  enableBtn(btnDetailCustomer, custEnabled);
  enableBtn(btnSearchCustIdCustomer, custEnabled);
  enableBtn(btnSearchAddressCustomer, enabled);
  enableBtn(btnSearchNameCustomer, enabled);
  
  enableBtn(btnNewAddressPrivateCustomer, enabled);
  enableBtn(btnNewAddressBusinessCustomer, enabled);
  enableBtn(btnCustomerStorno, enabled && $(hddIsFleetOrder).value != '1');
  enableBtn(btnCustomerToClipboard,  enabled);
 
  // driver buttons
  enableBtn(btnDetailDriver, driverEnabled);
  enableBtn(btnSearchCustIdDriver, driverEnabled);
  enableBtn(btnSearchAddressDriver, enabled);
  enableBtn(btnSearchNameDriver, enabled);
  
  enableBtn(btnNewAddressPrivateDriver, enabled);
  enableBtn(btnNewAddressBusinessDriver, enabled);
  enableBtn(btnDrivererStorno,  enabled && $(hddIsFleetOrder).value != '1');
  enableBtn(btnDrivererToClipboard,  enabled);
}

function ReadGridDataOe2()
{
  // now_debug_out ("ReadGridDataOe2");
	GetGridData(grdPrInclude, "INPUT", 0, hddPrInclude);
	GetGridDataSep(grdRemarks, "INPUT", 0, hddRemarks, "\n");
	GetGridData(grdRemarks, "SELECT", 0, hddRemarkTypes);
}

function PrepareNewInputOe2()
{
  ReadGridDataOe2();
	PrepareServerClick();
  if (GetValue(hddDirectMode) == "0")
  {
    PrepareCarSelectPostData();
  }
}
function OnConfigMouseUpOe2()
{
  NoCheck = false;
}
function OnConfigMouseDownOe2()
{
  NoCheck = true;
}
function IsSelectModeCalledOe2()
{
  return NoCheck;
}

function OnBrandChangedOe2(isOnLoad)
{
  if (!isOnLoad)
  {
		ClearInputByName(inpModelKey);
  }
  OnBrandChanged4Order();
	
	removeBodyClasses("Volkswagen Audi Nutzfahrzeuge");
	switch (GetBrandOe2())
	{
		case "30000":
			addBodyClasses("Volkswagen");
			break;
		case "50000":
			addBodyClasses("Audi");
			break;
		case "70000":
			addBodyClasses("Nutzfahrzeuge");
			break;
	}
	
}
function ltmChanged () {
  if (isLTMValue)
  {
    SetValue(lblRequiredWeek, litRequiredDeliveryWeek);
    SetValue(lblFirstDeliveryWeek, litFirstDeliveryDate);
    SetValue(lblActDeliveryWeek, litActDeliveryDate);
    SetValue(lblDeliveryConfirmation, "");
    SetValue('lblRequiredWeekHead', litRequiredDeliveryWeek);
    SetValue('lblActDeliveryWeekHead', litActDeliveryDate);
  }
  else
  {
    SetValue(lblRequiredWeek, litRequiredWeek);
    SetValue(lblFirstDeliveryWeek, litFirstLookingGlasWeek);
    SetValue(lblActDeliveryWeek, litActLookingGlasWeek);
    SetValue('lblRequiredWeekHead', litRequiredWeek);
    SetValue('lblActDeliveryWeekHead', litActLookingGlasWeek); 
  }

}
function OnBrandChanged4Order()
{
  TargetChangedOe2();

  SetValue(txtDeliveryConfirmation, "");
  SetPromotionsOe2();

  ltmChanged();
  
	var lblSuppDealer = $('lblSupportingDealer');
	var inpSuppDealer = $(inpSupportingDealer);
	if (lblSuppDealer != null && inpSuppDealer != null )
	{
		var isVwCar = (GetBrandOe2() == "30000");
		isVwCar ? lblSuppDealer.removeClassName("hidden") : lblSuppDealer.addClassName("hidden");
		isVwCar ? inpSuppDealer.removeClassName("hidden") : inpSuppDealer.addClassName("hidden");
  }
}

function OnCustomerKeyUpOe2()
{
	SetValue(txtLastNameCustomer, "");
	SetValue(txtSolitaryCustomer, "");
	SetValue(txtFirstNameCustomer, "");
	SetValue(txtStreetCustomer, "");
	SetValue(txtHouseNbrCustomer, "");
	SetValue(txtCountryCustomer, "");
	SetValue(txtCityCustomer, "");
	SetValue(txtPobCustomer, "");

  EnableButtonsOe2();   
}
function OnDriverKeyUp()
{
	SetValue(txtLastNameDriver, "");
	SetValue(txtSolitaryDriver, "");
	SetValue(txtFirstNameDriver, "");
	SetValue(txtStreetDriver, "");
	SetValue(txtHouseNbrDriver, "");
	SetValue(txtCountryDriver, "");
	SetValue(txtCityDriver, "");
	SetValue(txtPobDriver, "");

  EnableButtonsOe2();   
}
function AddrSelect()
{
	SelectGridLineForm(tblAddresses.id);
  SelectAssumeButton(tblAddresses, btnAssume, 1);
}


function ClearButtonActionOe2()
{
  if (typeof(hddCommand) != "undefined")
  {
		SetValue(hddCommand, "");
  }
}
function OnFinancingChangedOe2()
{
  if (typeof(selFinancing) != "undefined" && typeof(selLeasingCompany) != "undefined")
  {
    var financing = $(selFinancing);
    var leasing = $(selLeasingCompany);
    if (financing != null && leasing != null)
    {
      if ((financing.value != "LEASING")) {
        leasing.addClassName("readonly");
        leasing.removeClassName("required");
      } else {
				// now_debug_out ("leasing: remove readonly" + leasing.identify());
        leasing.removeClassName("readonly");
        leasing.addClassName("required");
      }
    }
  }
}
function OnClubcardChangedOe2()
{
  var clubcard = $(selClubCard);
  var isShowMode = IsShowModeOe2();
  if (clubcard != null)
  {
    var disabled = clubcard.disabled || 
									(clubcard.value == "" || clubcard.value == "NO_ATTENDANCE") || isShowMode || !GetCustomRequiredOe2();
    SetReadOnly(selClubCardSendTo, true, disabled, !clubcard.disabled);
  }
}
function IsShowModeOe2()
{
  return GetValue(hddIsShowMode) == "1" && GetValue(hddRightLevel) != "1";
}

function TargetChangedOe2()
{
  if( hddHasPackages == null )
      return;
  
  var disabled = GetValue(hddHasPackages) != "1" || IsShowModeOe2() || !GetCustomRequiredOe2();

  SetReadOnly(selCarTownPackage, !disabled, disabled, true);
}
function OnProcessStateChangedOe2()
{
  
  var isShowMode = IsShowModeOe2();
  var required = GetCustomRequiredOe2();
  var isEvaRequest = GetValue(hddIsEvaRequest) == "1";
  var isReadOnly = isShowMode || !required;
   
  TargetChangedOe2();

	SetCustomTabOe2();  
  SetReadOnly(inpConquest, false, isReadOnly, true);
  SetReadOnly(inpIntakeOfOrders, required, false, false);
  
  SetPromotionsOe2();

  ReadGridDataOe2();
  
  var rights = GetValue(hddRightLevel);
  var sign = GetValue(selCustomerSign);
                          
  SetAuthorityOe2(isShowMode);
	var hasCommNbr = HasCommNbrOe2();
	var seller = $(inpSeller);

	if (seller != null && 
			sign == "RETAIL_CUSTOMER" && rights == "1" && !hasCommNbr)
	{
	  required ? seller.addClassName("required") : seller.removeClassName("required");
	}
	
	SetReadOnly(inpCustomer, 
							!isEvaRequest && (!hasCommNbr || rights == "1"), 
							isEvaRequest || (hasCommNbr && rights != "1"), 
							false);
	
  CheckCustomerCombosOe2(required);
  
  if ($F(selProcessState) == "CUSTOMER_ORDER")
	{
	  addBodyClasses("processState_K");
	}
	else
	{
		removeBodyClasses("processState_K");
	}
	
  EnableButtonsOe2();  
}
function SetCustomTabOe2()
{
  GetCustomRequiredOe2() ? removeBodyClasses("customerCtrl") : addBodyClasses("customerCtrl");
}
function SetSpecialTabOe2()
{
	GetValue(hddIsSpecialOrder) == "1" ? removeBodyClasses("specialCtrl") : addBodyClasses("specialCtrl");
}
function SetFleetTabOe2()
{
	var isFleetOrder = GetValue(hddIsFleetOrder) == "1";
	SetStyle('divFleetOrderCtrlOuter', "display", isFleetOrder? "block" : "none");
	SetStyle('refFleetOrder', "display", isFleetOrder ? "inline" : "none");
}
function SetPhaetonTabOe2()
{
	var isPhaetonOrder = GetValue(hddIsPhaetonOrder) == "1";
	SetStyle('divPhaetonOrderCtrlOuter', "display", isPhaetonOrder? "block" : "none");
	SetStyle('refPhaetonOrder', "display", isPhaetonOrder ? "inline" : "none");
}
function IsDisponentOe2()
{
  return GetValue(hddRightLevel) == "1";
}
function OnCustomerGroupChanged() {

}
function SetAuthorityOe2(isShowMode)
{
  var IsDisponent = IsDisponentOe2();
  //SetReadOnly(selProductionPriority, true, !IsDisponentOe2, false);
  SetReadOnly(inpSeller, IsDisponent && GetCustomRequiredOe2(), !IsDisponent, false);
}
function OnOrderStateChangedOe2(oThis)
{
  SetPromotionsOe2();
  SaveChangedSelectValue(oThis, hddOrderState);
  CheckDeliveryInputsOe2();
}
function CheckDeliveryCtrlOe2 ()
{
  var deliveryVisible = GetValue(hddIsDeliveryClosed) == "1";
  deliveryVisible ? addBodyClasses("deliveryCtrl") : removeBodyClasses("deliveryCtrl");
}
function SetPromotionsOe2()
{
  //var isShowMode = IsShowModeOe2();
  //var isReadOnly = isShowMode || (GetValue(selProcessState) != "CUSTOMER_ORDER");
                  
  //SetReadOnly(inpPromotionAction1, IsAudiOe2() && GetValue(selProcessState) == "CUSTOMER_ORDER", false, true);
  SetReadOnly(inpPromotionAction2, false, false, true);
  SetReadOnly(inpPromotionAction3, false, false, true);
}
function CheckDeliveryInputsOe2()
{
  var isShowMode = IsShowModeOe2();
  var IsDisponentOe2 = IsDisponentOe2();
  var isOffer = GetValue(selOrderState) != "ORDER_IN_WORK";
  var isReadOnly = isShowMode || isOffer;

  SetReadOnly(inpDelivery, false, isReadOnly, false);
  SetReadOnly(inpVehicleApproval, isReadOnly, false, false);
  SetReadOnly(inpRegistrationNumber, false, isReadOnly, false);
  SetReadOnly(selWeekType, !isReadOnly && IsDisponentOe2, isShowMode || !IsDisponentOe2, false);
}
function GetCustomRequiredOe2()
{
	var stateValue = $F(selProcessState);
  return stateValue != null && (stateValue == "CUSTOMER_ORDER" || stateValue == "DEMONSTRATION_CAR_ORDER");
  
}
function CheckCustomerCombosOe2(enabled)
{
  SetReadOnly(inpSeller, true, !enabled, false);
  SetComboOe2(selCustomerSign, false, enabled);
  SetComboOe2(selCustomerGroup, true, enabled);
  //SetComboOe2(selSpecialCustomer, true, enabled);
  SetComboOe2(selFinancing, false, enabled, false);
  SetComboOe2(selLeasingCompany, false, enabled);
  SetReadOnly(inpLeasingContractNbr, false, !enabled, false);
  SetComboOe2(selClubCard, true, enabled);
	OnClubcardChangedOe2();
	SetReadOnly(inpMajorClientContractNbr, false, !enabled, false);
	SetReadOnly(inpMajorClientNbr, false, !enabled, false);
	SetReadOnly(inpIntakeOfOrders, false, !enabled, false);
	SetReadOnly(inpAgreedDeliveryDate, false, !enabled, false);
	SetReadOnly(inpSalesConfirmationDate, false, !enabled, false);
	
}
function SetComboOe2(selCombo, required, enabled)
{
	// now_debug_out ("*** SetComboOe2: selCombo= " + $(selCombo).identify());
  if (!IsShowModeOe2())
  {
	  SetReadOnly(selCombo, required, !enabled, false);
  }
}

function HasCustomerOe2()
{
	return (GetValue(txtLastNameCustomer) != "" && GetValue(inpCustomer).length == 6);
}
function HasDriverOe2()
{
	return (GetValue(txtLastNameDriver) != "" && GetValue(inpDriver).length == 6);
}
function HasCommNbrOe2()
{
  var CommNbr = $(inpCarCommissionNbr);
  return (CommNbr != null && CommNbr.value != "");
}

function OnCustomerSignChangedOe(oThis)
{
  SaveChangedSelect(oThis, hddCustomerSign);
  SetAuthorityOe2(IsShowModeOe2());
  if(GetValue(selCustomerSign) == "SPECIAL_CUSTOMER" ) SetComboOe2(selSpecialCustomer, false, true);
  else SetComboOe2(selSpecialCustomer, false, false);
}

function CommissionNbrUpOe2()
{
	var val = $(inpCarCommissionNbr).value;
	var upVal = val.toUpperCase();
	if (val != upVal)
	{
		$(inpCarCommissionNbr).value = upVal;
	}
	enableBtn(btnShowCommNbr, $(inpCarCommissionNbr).value.length > 5);
}
function PrepareChangeSelectClickOe2()
{
	if ($(divCarConfig) != null)
	{
		PrepareSelectClick(); // root it ro CarSelctor
	}

	if ($(divCarCheckEdit) != null)
	{
		PrepareEditClick();
	}
}
function DriverSelect()
{
	SetValue(hddCustomerSelect, "DRIVER");
}
function CustomerSelect()
{
	SetValue(hddCustomerSelect, "CUSTOMER");
}
function GetBrandOe2()
{
  var brand = "";
  if (typeof(selBrand) != "undefined" && $(selBrand) != null)
  {
    brand = GetValue(selBrand);
  }
  else if(typeof(ddlBrands) != "undefined" && $(ddlBrands) != null)
  {
    brand = GetValue(ddlBrands);
  }
  return brand;
  
}

function IsAudiOe2()
{
  return GetBrandOe2() == "50000";
}

function OnCustomerNameChangedOe2()
{
  SetValue(inpCustomer, "");
}

function oePromotionHelpUnCheckOtherRow (oList, oThis, otherName)
{
	if (otherName != oThis.name)
	{
		oCheck = oList.down('input[name='+otherName+'][value=' + GetValue(oThis) + ']');
		if (IsDefined(oCheck) && null != GetValue(oCheck) && GetValue(oThis) == GetValue(oCheck) )
		{
			oList.down('li.forUncheck input[name='+otherName+']').checked=true;
		}
	}	
}

function oePromotionHelpCheckRow (oThis)
{
	if (IsDefined(oThis))
	{
		var oList = oThis.up("ul.body");
		if (IsDefined(oList))
		{
			oePromotionHelpUnCheckOtherRow (oList, oThis, "inputVfm1");
			oePromotionHelpUnCheckOtherRow (oList, oThis, "inputVfm2");
			oePromotionHelpUnCheckOtherRow (oList, oThis, "inputVfm3");
		}
	}
	return true;
}

function oePromotionHelpWindow (oThis)
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

		var o1 = oDragWnd.down('input[name=inputVfm1][value=' + vfm1 + ']');
		var o2 = oDragWnd.down('input[name=inputVfm2][value=' + vfm2 + ']');
		var o3 = oDragWnd.down('input[name=inputVfm3][value=' + vfm3 + ']');
		
		SetValue (o1, vfm1);
		SetValue (o2, vfm2);
		SetValue (o3, vfm3);
		
		oePromotionHelpCheckRow (o1);
		oePromotionHelpCheckRow (o2);
		
		oDragWnd.select("input[name^=inputVfm]").each(function(el)
		{
			el.onclick = function()
			{
				return oePromotionHelpCheckRow ($(this));
			}
			
		})
	}
  return false;	
}

function oePreVehicleHelpWindow (oThis)
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

function PromotionCancel() {
  now_debug_out("PromotionCancel is obsolet");
  return false;
}

function PreVehicleCancel() {
  now_debug_out("PreVehicleCancel is obsolet");
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

function toggleProvision() {
  $('provisionBlock').toggleClassName('hidden');
}

