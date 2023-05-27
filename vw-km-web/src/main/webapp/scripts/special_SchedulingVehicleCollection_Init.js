function SchedulingVehicleCollection_Init()
{
    RowClick_Init();
}

function RowClick_Init() 
{
    $$('.rowClick tr').each(function(el) {
        el.onclick = function() 
        {
            DateChoiceClick($(this).down('input[type=radio]'));
            return true;
        }
    });
}

function DateChoiceClick(oControl)
{

  $$('.rowClick tr').invoke("removeClassName", "selected");

  oControl.up('tr').addClassName('selected');
  oControl.checked = true;

  if ("undefined" != typeof(HddSelectedChoiceId) && $(HddSelectedChoiceId)!=null) 
  {
    $(HddSelectedChoiceId).value = oControl.value;
  }

}


function DeliveryDate()
{
  if ("undefined" != typeof(HddSelectedChoiceId) && $(HddSelectedChoiceId)!=null) 
  {
    var SelIndex = $(HddSelectedChoiceId).value;
    SetRadioColor(SelIndex, "Color_Selected");
    var theId = "radioChoice_" + SelIndex;
    if (typeof($(theId)) != "undefined" && $(theId)!=null)
    {
      $(theId).checked = true;
    }
  }
}


function DdlASPackageChange(Ddl, Hdd)
{
  SelectToText(Ddl, Hdd);
  var sSrc = '|'+ Hdd.value + '|';
  var Suche = OverNightASPackages.indexOf(sSrc);
  
  if ($(DdlArrivalHotelId)!=null) 
  {
    if (Suche>=0)
    {
      $(DdlArrivalHotelId).addClassName ('required');
      $(DdlArrivalHotelId).removeClassName ('disabled');
      SetDisabled(DdlArrivalHotelId, false);
      if ($(DdlArrivalHotelId).selectedIndex == 0) {
        SetDisabled(IdButtonDates, true);
        SetDisabled(IdButtonAccount, true);
      }
    } else {
      $(DdlArrivalHotelId).removeClassName ('required');
      $(DdlArrivalHotelId).addClassName ('disabled');
      SetDisabled(DdlArrivalHotelId, true);
      $(DdlArrivalHotelId).value = '_';
      SetDisabled(IdButtonDates, false);
      SetDisabled(IdButtonAccount, false);
    }
  }
  // $(IdButtonDates).click();
  $(LastActionId).value = "SelectASPackage";
  //document.forms[0].submit();
}

function DdlArrivalHotelChange(Ddl, Hdd)
{
  SelectToText(Ddl, Hdd);
  if ($(DdlArrivalHotelId).selectedIndex == 0) {
    SetDisabled(IdButtonDates, true);
    SetDisabled(IdButtonAccount, true);
  } else {
    SetDisabled(IdButtonDates, false);
    SetDisabled(IdButtonAccount, false);
  }
  $(LastActionId).value = "SelectArrivalHotel";
}

function CbMountedWheelsetChange(Hdd)
{

    if( HddMountedWheelset != null )
      $(HddMountedWheelset).value = Hdd.value;

}

function ButtonPage_ClientClick(page)
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonPage";
  }
  if (typeof($(HddPageIndexId)) != "undefined")
  {
    $(HddPageIndexId).value = page;
  }
}

function ButtonSearch_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonSearch";
  }
}

function ButtonAccount_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonAccount";
  }
}
function ButtonConfirm_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
      $(LastActionId).value = "ButtonConfirm";
  }
}
function ButtonCancel_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonCancel";
  }
}

function ButtonPersons_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonPersons";
  }
}

function ButtonDates_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonDates";
  }
}


function ButtonBackward_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonBackward";
  }
}

function ButtonForward_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonForward";
  }
}

function ButtonMPCompanians_ClientClick()
{
  if (typeof($(LastActionId)) != "undefined")
  {
    $(LastActionId).value = "ButtonMPCompanians";
  }
}

function ResetSelection(pStart) 
{ 
  if ("undefined" != typeof(HddSelectedChoiceId) && $(HddSelectedChoiceId)!=null) 
  {
    $(HddSelectedChoiceId).value = "-1";
  }
  
  var Index = pStart;
  var theId = "radioChoice_" + Index;
  while($(theId)!=null) 
  {
    if (Index%2==1) 
    { 
      SetRadioColor(Index, "Color_Odd");
    }
    else 
    {
      SetRadioColor(Index, "Color_Even");
    }
    Index = Index+1;
    theId = "radioChoice_" + Index;
  }
}



function SetRadioColor(pIndex, pColor)
{
  var theId = "radioChoice_" + pIndex;
  if (typeof($(theId)) != "undefined" && $(theId)!=null)
  {
    var td = $(theId).parentElement;
    if (td!=null) {
      // td.style.backgroundColor=pColor;
      set_background_color_class_legacy (td, pColor);
      td = td.nextSibling;
    }
    if (td!=null) {
      // td.style.backgroundColor=pColor;
      set_background_color_class_legacy (td, pColor);
      td = td.nextSibling;
    }
    if (td!=null) {
      // td.style.backgroundColor=pColor;
      set_background_color_class_legacy (td, pColor);
      td = td.nextSibling;
    }
    if (td!=null) {
      // td.style.backgroundColor=pColor;
      set_background_color_class_legacy (td, pColor);
      td = td.nextSibling;
    }
  }
}

