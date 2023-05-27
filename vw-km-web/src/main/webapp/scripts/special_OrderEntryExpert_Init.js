
var orderExpertCustomerRequiredFields = "#_ctl0_inpSearchNbrCustomer,#_ctl0_inpSeller,#_ctl0_inpCustomerGroup,#_ctl0_inpClubCardVw,#_ctl0_inpCustomerSign,#_ctl0_inpIntakeOfOrdersVw,#_ctl0_inpFinance";



function callSkipInputPrBlock(oThis)
{
	var state = $F(inpProcessState);
	
	// now_debug_out ("state=" + state);
	if ("K" == state || "V" == state || "" == state)
	{
		var inp = $(inpSearchNbrCustomer);
		if (IsDefined(inp))
		{
			SetFocus(inp);
		}
	}
	else
	{
		SetFocus("_ctl0_btnErrorCheck");
	}
	
}

function ltmChange(sValue)
{
  if( bIsDebug ) now_debug_out ("ltmChange : " + sValue);
  if( sValue.toLowerCase() == 'true' ) {
    if (isLTMValue != true) {
      isLTMValue = true;
      OrderEntryExpertSetState();
    }
  }
  else {
    if (isLTMValue != false) {
      isLTMValue = false;
      OrderEntryExpertSetState();
    }
  }
}

function OeExpertClone (oThis)
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
		
		//Week-Types
		case "_ctl0_inpRequiredWeekTypeVw":
			dst = "_ctl0_inpRequiredWeekTypeAudi";
			src = "_ctl0_inpRequiredWeekTypeVw";
			break;
		case "_ctl0_inpRequiredWeekTypeAudi":
			dst = "_ctl0_inpRequiredWeekTypeVw";
			src = "_ctl0_inpRequiredWeekTypeAudi";
			break;
	}
	
	if ("" != dst)
	{
		SetValue($(dst), $F(src));
	  nowValidatorSetValid($(dst), nowIsValid ($(dst)));
	}

}

function OeExpertGetBrandName(brand)
{
	var brandName = "",ubl="";
	// now_debug_out ("OeExpertGetBrandName:" + brand);
	if ("" != brand)
	{
		try
		{
			// now_debug_out ("try userBrandNameList...");
			ubl = eval("userBrandNameList");		
		}
		catch (ex){
		// now_debug_out ("OeExpertGetBrandName: exception = " + ex);
		}
		// now_debug_out ("OeExpertGetBrandName: ubl = " + ubl);

		if (IsDefined (ubl) && "" != ubl)
		{
		// now_debug_out ("OeExpertGetBrandName: try json = " + ubl);
		return eval ("ubl." + brand);
		}
	}
	return brandName;
}

function OeExpertHandleBrand(oThis)
{
	switch ($F(oThis))
	{
		case "A":
			// Werte von VW kopieren
			OeExpertClone ($("_ctl0_inpRequiredWeek"));
			OeExpertClone ($("_ctl0_inpRequiredWeekTypeVw"));
			break;
		case "V":
		case "N":
			// Werte von Audi kopieren
			OeExpertClone ($("_ctl0_inpDesiredDeliveryWeek"));
			OeExpertClone ($("_ctl0_inpRequiredWeekTypeAudi"));
			break;
	}

	removeBodyClasses ("Volkswagen Audi Nutzfahrzeuge Seat");
	var brandName = OeExpertGetBrandName($F(oThis));
	// now_debug_out ("brandName= " + brandName);
	if (("" != brandName) && ("undefined" != brandName) && nowIsValidField (oThis))
	{
		addBodyClasses (brandName);
		// now_debug_out ("addBodyClasses: " + brandName);
	}
  OrderEntryExpertSetState();
}

function OnFocusPr(input, btnMore)
{
	now_debug_out ("special_OrderEntryExpert OnFocusPr: handle AddNewGridLine obsolete or generic???");
	
	
  if( "function" == typeof(OnFocusPr_OrderCheck_CarConfig) )
  {
      return OnFocusPr_OrderCheck_CarConfig(input, btnMore);
  }
  else
  {
      if (input != null)
      {
            var parent = input.parentElement; // TD
            if (parent != null && parent.cellIndex == 9)
            {
            var parent = parent.parentElement;
          if (parent != null && parent.nextSibling == null)
          {
                    AddNewGridLine(dtlInclude, 0, 60, btnMore);
          }
        }
      }
  }
}

function SetCustDepend(ctrl, hasCustomer, clear)
{
  // now_debug_out ("SetCustDepend(" + ctrl + ", " + hasCustomer + ", " + clear +")");
  SetReadOnly(ctrl, hasCustomer, false, false);
  if (clear)
  {
    SetValue(ctrl, "");
  }
}

function AddNewVehiclePrLines(moreBtn)
{
	// now_debug_out("AddNewVehiclePrLines expert 6");
	AddNewVehiclePrLine(moreBtn, 6)
}

function OnBeforeUnloadOrderEntry()
{
  ReadGridData();  
}
function ReadGridData()
{
	GetGridData(dtlInclude, "INPUT", 0, hddInclude);

}
function CommNbrKeyDown()
{
	SubmitBtnEnter(btnShowComm);
}
function OnCustomerNbrKeyDown()
{
  SetValue(inpSearchNameCustomer, "");
}
function OnCustomerNameKeyDown()
{
  //SetValue(inpSearchNbrCustomer, "");
}
function OnDriverNbrKeyDown()
{
  SetValue(inpSearchNameDriver, "");
}
function OnDriverNameKeyDown()
{
  SetValue(inpSearchNbrDriver, "");
}

function handleOrderExpertDisabledFields(csvIdList, disabled)
{
	$$(csvIdList).each(function(el)
	{
		SetDisabled (el, disabled);
	})
}

function handleorderExpertRequiredFields(hasCustomer)
{
	var mode = "remove";
	if (hasCustomer)
	{
		mode="add";
	}
	$$(orderExpertCustomerRequiredFields).invoke(mode+"ClassName", "required");

	// handleOrderExpertDisabledFields(orderExpertCustomerRequiredFields, mode=="add");
	
}

function HandleDisabledValidState(oThis)
{
	theBtn = getSubmitOnEnterButton(oThis);
	if (IsDefined(theBtn))
	{
	  SetDisabled (theBtn, !nowIsValid (oThis) || $F(oThis).length < getMinFieldLength (oThis));
	}
}

function HandleDisabledValidStates()
{
  var csvIdList = "#_ctl0_inpSearchNbrCustomer,#_ctl0_inpSearchNbrDriver,#_ctl0_inpCarCommissionNbr,#_ctl0_inpEvaContractDataNumber";
	$$(csvIdList).each(function(el)
	{
		HandleDisabledValidState(el);
	})	
}

function HandleProcessState(oThis)
{
	var process = $F(inpProcessState);
	// now_debug_out ("HandleProcessState:" + process);
	var hasCustomer = (process == "K" || process == "V");
	handleorderExpertRequiredFields(hasCustomer);
	
	if (process == "K")
	{
		addBodyClasses("processState_K");
	}
	else
	{
		removeBodyClasses("processState_K");
	}

	

  if (nowIsValid (oThis))
	{
		var custNr = $F(inpSearchNbrCustomer);
		if (!hasCustomer)
		{
			if (custNr.length > 0)
			{
				nowValidatorSetValid($(inpSearchNbrCustomer), false);
				return;
			}
		}
	}
	else
	{
		// oThis.activate();
	}
	nowValidatorSetValid($(inpSearchNbrCustomer), nowIsValid ($(inpSearchNbrCustomer)));
	HandleDisabledValidState (oThis);
}

function HandleCustomerNumber(oThis)
{
	var custNr = $F(oThis);
  if (custNr.length > 0)
	{
		var process = $F(inpProcessState);
		var hasCustomer = (process == "K" || process == "V");

		if (!hasCustomer)
		{
			nowValidatorSetValid($(inpProcessState), false);
		}
		else
		{
			nowValidatorSetValid($(inpProcessState), nowIsValid ($(inpProcessState)));
		}
	}
	else
	{
		nowValidatorSetValid($(inpProcessState), nowIsValid ($(inpProcessState)));
	}

	// SetDisabled ($("_ctl0_btnAssumeCustomer"), !nowIsValid (oThis) || custNr.length == 0);
  HandleDisabledValidState (oThis);
//	GetAjaxBusinessPartner(oThis);
}

function GetAjaxBusinessPartner(oThis)
{
	if (nowIsValid (oThis))
	{
		now_debug_out("GetAjaxBusinessPartner");
	}
}

function OrderEntryExpertSetState()
{
	
  var process = $F(inpProcessState);
  var finance = $F(inpFinance);
  var hasCustomer = (process == "K" || process == "V");
  var actFinance = (finance == "L" || finance == "F");
  var brand = $F(inpBrand);
	
  var isImporter = g_IsImporter == "1";
	
  var isAudi = brand=="A";
  var isLikeAudi = isAudi || isImporter;
  var isLikeVw = !isAudi || isImporter;
  var hasBrandChanged = (($F(inpBrand)=="A") != isAudi);
  var club = isAudi ? $F(inpClubCardAudi) : $F(inpClubCardVw);
	
	
	 // now_debug_out("club: a=" +$F(inpClubCardAudi) + "; VW="+$F(inpClubCardVw))


 	//SetCustDepend(inpSeller, hasCustomer, !hasCustomer);
	var mustClear = false;
	// var mustClear = !hasCustomer;
 	
	SetCustDepend(inpCustomerGroup, hasCustomer);
//	SetCustDepend(inpCustomerGroup, hasCustomer, mustClear);
 	SetCustDepend(inpCustomerSign, hasCustomer, mustClear);
 	SetCustDepend(inpFinance, hasCustomer, mustClear);
 	SetCustDepend(inpLeasingContractNbr, hasCustomer && actFinance, mustClear);
 	SetCustDepend(inpLeasingCompany, hasCustomer && actFinance, mustClear);
 	SetCustDepend(inpClubCardSendToVw, hasCustomer && (club == "1" || club == "2"), mustClear);
 	SetCustDepend(inpClubCardSendToAudi, hasCustomer && (club == "1" || club == "2"), mustClear);
 	
 	
 	// now_debug_out("process : " + process + ", isAudi : " + isAudi + ", hasCustomer : " + hasCustomer);
  //SetCustDepend(inpPromotionAction1, isAudi && process == "K", mustClear);

  SetCtrlVisible('trAudiData1', isLTMValue);
  SetCtrlVisible('trAudiData2',  isLTMValue);
  SetCtrlVisible('trAudiData3',  isAudi);
  SetCtrlVisible('trAudiData4',  isAudi);
  SetCtrlVisible('trAudiData5',  isAudi);

	// now_debug_out("SetCtrlVisible: vw");
  
  SetCtrlVisible('trVwData1',  !isAudi);
  SetCtrlVisible('trVwData2',  !isLTMValue);
  SetCtrlVisible('trVwData3', !isLTMValue);
  SetCtrlVisible('trVwData4', !isAudi);
  SetCtrlVisible('trVwData5', !isAudi);
  SetCtrlVisible('trVwData6', !isAudi);

	// now_debug_out("SetCtrlVisible: FKON");
  
  SetCtrlVisible('tdFKON',     isImporter);
  SetCtrlVisible(inpFKON,      isImporter);
  SetCtrlVisible('tdDealer1', !isImporter);
  SetCtrlVisible('tdFrDate',   isImporter);
  SetCtrlVisible(inpFrDate,    isImporter);
  SetCtrlVisible('tdDealer2', !isImporter);

	// now_debug_out("SetCtrlVisible: END");

  if (hasBrandChanged)
  {
    var toLeft = (brand == "A");
		
	// now_debug_out("hasBrandChanged: brand=" +brand)		
		
    ChangeValues(inpClubCardAudi, inpClubCardVw, toLeft);
    ChangeValues(inpClubCardSendToAudi, inpClubCardSendToVw, toLeft);
    ChangeValues(inpCarTownPackageAudi, inpCarTownPackageVw, toLeft);
    ChangeValues(inpCustomerCommissionNbrAudi, inpCustomerCommissionNbrVw, toLeft);
    ChangeValues(inpIntakeOfOrdersAudi, inpIntakeOfOrdersVw, toLeft);
    ChangeValues(inpConfirmationOfOrderAudi, inpConfirmationOfOrderVw, toLeft);
    ChangeValues(inpConquestAudi, inpConquestVw, toLeft);
    ChangeValues(inpCocAudi, inpCocVw, toLeft);
    
    SetValue(inpDeliveryConfirmation, "");
  }
  
	removeBodyClasses("oeBrand_A oeBrand_V oeBrand_N hasCustomer isImporter isLikeVw isLikeAudi");

	// now_debug_out("removeBodyClasses: oeBrand...");

  var bdCls = "";
	
  if (brand && "" != brand)
  {
    bdCls += " oeBrand_" + brand;
	}

	// now_debug_out("a1...");

  if (isImporter)
  {
    bdCls += " isImporter";
	}
	// now_debug_out("a2...");
	
  if (hasCustomer)
  {
    bdCls += " hasCustomer";
	}
	// now_debug_out("a3...");
	  
  if (!isLTMValue)
  {
    bdCls += " isLikeVw";
	}
	// now_debug_out("a4...");
	  
  if (isLTMValue)
  {
    bdCls += " isLikeAudi";
	}
	// now_debug_out("a5...");

	addBodyClasses(bdCls);
 // now_debug_out ("OrderEntryExpertSetState: " + bdCls);
  
}

function ChangeValues(ctrlLeft, ctrlRight, toLeft)
{
  if (toLeft)
  {
		if ($F(ctrlLeft) != "")
		{
			now_debug_out ("ChangeValues: overwrite; ctrlLeft has value=" + $F(ctrlLeft));
		}
    SetValue(ctrlLeft, $F(ctrlRight));
    SetValue(ctrlRight, "");
  }
  else
  {
		if ($F(ctrlRight) != "")
		{
			now_debug_out ("ChangeValues: overwrite; ctrlRight has value=" + $F(ctrlRight));
		}
    SetValue(ctrlRight, $F(ctrlLeft));
    SetValue(ctrlLeft, "");
  }
}

function OrderEntryExpert_Init()
{
	  //now_debug_out ("common/res: OrderEntryExpert_Init");
	
  $(inpProcessState).addClassName("revalidate ");
  $(inpSearchNbrCustomer).addClassName("revalidate ");

	  //now_debug_out ("2: OrderEntryExpert_Init");

	OrderEntryExpertSetState();
	  //now_debug_out ("3: OrderEntryExpert_Init");
	vehiclePrNumberList_Init();
	  //now_debug_out ("4: OrderEntryExpert_Init");
	ReadGridData ();
	  //now_debug_out ("5: OrderEntryExpert_Init");
	
	HandleProcessState ($(inpProcessState));
	  //now_debug_out ("6: OrderEntryExpert_Init");
	HandleDisabledValidStates();
	  //now_debug_out ("7: OrderEntryExpert_Init");
	

	OeExpertHandleBrand($(inpBrand));
	// ajax_OrderyModifyExpert_Init ();
}

function ajax_OrderyModifyExpert_Init ()
{
	 // now_debug_out ("ajax_OrderyModifyExpert_Init");
  $$("#_ctl0_btnSearchCustomer").each(function(el)
	{
		el.onclick = function()
    {
			var footer = '';
			footer += '<a class="button" title="&Uuml;bernehmen" onclick="alert(1); return false;" href="#">&Uuml;bernehmen</a>';
			footer += '<a class="button abort" title="Schließen und verwerfen der Änderungen" href="#">Verwerfen</a>';
      var oWnd = dragWindowCreate ("idOModAjaj", "Kundensuche", "<h1>Kundensuche</h1>", footer, "processWindow usp_OModAjaj");
			dragWindowShow(oWnd,true);
      return false;
		}
	})

}



