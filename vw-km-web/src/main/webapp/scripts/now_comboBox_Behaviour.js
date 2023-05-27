
var g_InputField2ComboBox_curHideId = "";
var g_InputField2ComboBox_curId = "";

function InputField2ComboBoxDoSubmit (oCtrl)
{
  if (oCtrl.hasClassName("doSubmit"))
  {
    if ("function" == typeof (set_modal_submit)) {set_modal_submit();}
    oCtrl.up("form").submit();
  }
}

function InputField2ComboBoxSelect (oThis)
{

  var theUl = $(oThis).up("ul");
  var theId = $(theUl.className);
  theId.value = oThis.innerHTML.split(':')[0];
  InputField2ComboBox_CheckValid (theId);
  theUl.up("div").hide();
  InputField2ComboBoxDoSubmit ($(theId));
  return false;
}

function InputField2ComboBox_IsValid (inputCtrl)
{
  var theValue = inputCtrl.value;
  inputCtrl = $(inputCtrl);
  dataListName = InputField2ComboBox_GetDataListName (inputCtrl);
  if (dataListName && "" != dataListName)
  {
    var classListName = dataListName + "_List";
    var comboData = eval(classListName);
    var isValid = false;
    comboData.split(',').each(function(val)
    { 
      if (val.trim().split(':')[0] == theValue)
      {
        isValid = true;
      }
    })
    return isValid
  }
  return true;
}

function InputField2ComboBox_CheckValid (inputCtrl)
{
  var isValid = InputField2ComboBox_IsValid (inputCtrl);
  if (isValid)
  {
    inputCtrl.removeClassName ("invalid");
  }
  else
  {
    inputCtrl.addClassName ("invalid");
  }
  return isValid;
}

function InputField2ComboBox_AutoCompId (dataListName)
{
  return "autocomplete_choices_"+dataListName;
}

function InputField2ComboBox_SetPositon (inputCtrl)
{
  var autoCompId = InputField2ComboBox_AutoCompId(InputField2ComboBox_GetDataListName (inputCtrl));
  autoCompId += inputCtrl.identify();
  oAutoId = $(autoCompId);
  if (!IsDefined(oAutoId))
  {
    InputField2ComboBox_Create (inputCtrl);
    oAutoId = $(autoCompId);
  }

  var offset = Position.cumulativeOffset(inputCtrl);
  //var comboW = inputCtrl.getWidth() + 2 + "px !important";
  var comboW = inputCtrl.getWidth() + "px";
  var comboH = inputCtrl.getHeight();
  var comboX = offset[0] + "px";
  var comboY = (offset[1] + comboH) + "px";

  if (oAutoId.style.left != comboX || oAutoId.style.top != comboY)
  {
   // now_debug_out ("ComboBox ("+inputCtrl.className+") newPos: (" + comboX + "," + comboY + ")...", 9, "G");
    oAutoId.setStyle({top:comboY,left:comboX,width:comboW})
   // now_debug_out ("ComboBox setStyle finished...", 9, "G");
  }
}

function InputField2ComboBox_RePositon (inputCtrl)
{
  InputField2ComboBox_SetPositon (inputCtrl);
}

function doUnescape (val)
{
  val = val.replace(/&quot;/g, "\"");
  return unescape(val);
}

function InputField2ComboBox_Spinner (inputCtrl, curVal, direction)
{
  dataListName = InputField2ComboBox_GetDataListName (inputCtrl);
  if (dataListName && "" != dataListName)
  {
    var classListName = dataListName + "_List";
    var comboData = eval(classListName);
    var firstOption = "";
    var prevOption = "";
    var found = false;
    if (comboData && comboData.length > 0)
    {
      comboData.split(',').each (function (el)
      {
        thisOption = el.trim().split(':')[0];
        if ("" == firstOption)
        {
          firstOption = thisOption;
        }
        if (direction > 0)
        {
          if ((prevOption == curVal) && !found)
          {
            curVal = doUnescape (thisOption);
            found = true;
            return curVal;
          }
        }
        else
        {
          if ((thisOption == curVal) && ("" != prevOption) && !found)
          {
            curVal = doUnescape (prevOption);
            found = true;
            return curVal;
          }
        }
        prevOption = thisOption;
      })
      if ((prevOption == curVal) && (direction > 0))
      {
        curVal = doUnescape (firstOption);
        found = true;
        return curVal;
      }
      if (!found)
      {
        if (direction > 0)
        {
          curVal = doUnescape (firstOption);
        }
        else
        {
          curVal = doUnescape (prevOption);
        }
        return curVal;
      }
    }
  }
  return curVal;
}



function InputField2ComboBox_Next (inputCtrl)
{
  return InputField2ComboBox_Spinner (inputCtrl, $F(inputCtrl), 1);
}

function InputField2ComboBox_Prev (inputCtrl)
{
  return InputField2ComboBox_Spinner (inputCtrl, $F(inputCtrl), -1);
}

function InputField2ComboBox_Build (inputCtrl)
{
  dataListName = InputField2ComboBox_GetDataListName (inputCtrl);
  if (dataListName && "" != dataListName)
  {
    var classListName = dataListName + "_List";
    var comboData = eval(classListName);
    if (comboData && comboData.length > 0)
    {
      var inputCtrlId = inputCtrl.identify();
      var aHref = "<a href='#' onclick='return InputField2ComboBoxSelect(this);'>";
      var autoCompId = InputField2ComboBox_AutoCompId(dataListName);
      autoCompId += inputCtrlId;

      var comboDataNames = new Array();
      comboData.split(',').each (function (el)
      {
        comboDataNames[comboDataNames.length] = el.trim().split(':')[0];
      })

      csvNames = comboDataNames.join(",");

      var divClass = "autocomplete " + inputCtrl.className;
      var choiseList = "<ul class='"+inputCtrlId+"'>\n<li>" + aHref + csvNames.replace(/,/g, "</a></li>\n<li>"+aHref) + "</a></li>\n</ul>";

      if (!IsDefined($(autoCompId)))
      {

        var autoChoiseDiv = '\n<div id="'+autoCompId+'" class="'+divClass+'">\n'+choiseList+'\n</div>\n';
        getBody().insert (autoChoiseDiv);
        InputField2ComboBox_SetPositon (inputCtrl);

        if (IsRequired(inputCtrl))
        {
          if (!inputCtrl.hasClassName ("required"))
          {
            $(autoCompId).addClassName ("required");
          }
        }

      }
      $(autoCompId).hide();
      return true;
    }
  }
  return false;
}


function InputField2ComboBox_ComboInputClick (inputCtrl)
{
  if (inputCtrl)
  {
    var autoCompId = InputField2ComboBox_AutoCompId(InputField2ComboBox_GetDataListName (inputCtrl));
    autoCompId += inputCtrl.identify();

    if (!IsDefined($(autoCompId)))
    {
      InputField2ComboBox_Build (inputCtrl);
    }

    if (IsDefined($(autoCompId)))
    {
      if (inputCtrl.hasClassName ("is_open"))
      {
//  now_debug_out ("Click("+inputCtrl.identify()+"): is open",1000,"A");
        $(autoCompId).hide();
        inputCtrl.removeClassName ("is_open");
      }
      else
      {
//  now_debug_out ("Click("+inputCtrl.identify()+"): is closed",1000,"A");
        InputField2ComboBox_Show (inputCtrl);
          g_InputField2ComboBox_curHideId = inputCtrl.identify();
        if ((inputCtrl.identify() != g_InputField2ComboBox_curHideId) || ("" == g_InputField2ComboBox_curHideId))
        {
//  now_debug_out ("SHOW("+inputCtrl.identify()+")",1000,"A");
        }
        else
        {
//  now_debug_out ("SKIP("+inputCtrl.identify()+")",1000,"A");
        //  InputField2ComboBox_Hide ("immediately");
        }
      }
    }
    return false;
  }
  else
  {
    return true;
  }
}
function InputField2ComboBox_ComboHandleClick (oThis)
{
  oThis = $(oThis);
  if (oThis && oThis.hasClassName ("readonly"))
  {
    return false;
  }

  var inputCtrl = oThis.previous("input");
//  now_debug_out ("* ComboHandleClick(" + inputCtrl.identify() + ")", 1000, "A")

  if ((inputCtrl.identify() != g_InputField2ComboBox_curHideId) && ("" != g_InputField2ComboBox_curHideId))
  {
    // now_debug_out ("HIDE (" + g_InputField2ComboBox_curHideId + "): NEXT=" + inputCtrl.identify(),1000,"A");
    InputField2ComboBox_Hide ("immediately")
  }

  return InputField2ComboBox_ComboInputClick (inputCtrl);
}

function InputField2ComboBox_OnArrowBlur (oThis)
{

  oThis = $(oThis);
// return true;

  inputCtrl = oThis.previous("input");

  InputField2ComboBox_CheckValid (inputCtrl);
  if ("" != g_InputField2ComboBox_curHideId)
  {
    g_InputField2ComboBox_curHideId = inputCtrl.id;
    new PeriodicalExecuter(function(pe) {pe.stop();InputField2ComboBox_Hide();}, 0.3);
  }
  return true;
}

function InputField2ComboBox_CreateArrow (inputCtrl)
{
  var cls = "comboHandle";
  if (inputCtrl.hasClassName("readonly"))
  {
    cls += " readonly";
  }
  var choiseHandle = GetClientCtrlHandleHtml (inputCtrl, "<span class='"+cls+"' onclick='InputField2ComboBox_ComboHandleClick(this)' onblur='InputField2ComboBox_OnArrowBlur(this)'></span>")
  inputCtrl.insert ({after: choiseHandle});
  newId = inputCtrl.next("span.clientCtrl").identify()
  $(newId).insert ({top: inputCtrl});
}

function InputField2ComboBox_Create (inputCtrl)
{

  g_InputField2ComboBox_curId = inputCtrl.identify();
  var autoCompId = InputField2ComboBox_AutoCompId(InputField2ComboBox_GetDataListName (inputCtrl));
  autoCompId += inputCtrl.identify();
  if (!IsDefined($(autoCompId)))
  {
    InputField2ComboBox_Build (inputCtrl);
  }
}

function fnAfterShow (autoCompId)
{
  var autoCompId = InputField2ComboBox_AutoCompId(InputField2ComboBox_GetDataListName (g_InputField2ComboBox_curId));
  if (IsDefined($(autoCompId)))
  {
    IsVisible = $(autoCompId).visible();
  }
}


function InputField2ComboBox_Show (inputCtrl)
{

  var curComboId = g_InputField2ComboBox_curId;
  var curHideId  = g_InputField2ComboBox_curHideId;

 // InputField2ComboBox_Hide ();
  var autoCompId = InputField2ComboBox_AutoCompId(InputField2ComboBox_GetDataListName (inputCtrl));
  var IsVisible = false;
  autoCompId += inputCtrl.identify();
  if (!IsDefined($(autoCompId)))
  {
    InputField2ComboBox_Create (inputCtrl);
  }
  else
  {
    // IsVisible = $(autoCompId).visible();
  }

  InputField2ComboBox_Hide ();

  if (need_ie6_IFrame())
  {
    addBodyClasses("hideListBoxes");
  }

  inputCtrl.addClassName ("is_open");

  g_InputField2ComboBox_curId = inputCtrl.identify();
//  InputField2ComboBox_RePositon (inputCtrl);
  
  new PeriodicalExecuter(function(pe) 
  {
    curCombo = $(g_InputField2ComboBox_curId);
    if (curCombo)
    {
      InputField2ComboBox_RePositon (curCombo);
    }
    else
    {
      pe.stop();
    }
  }, 0.5);
 // $(autoCompId).show();

  if (!IsVisible)
  {
    new Effect.Appear($(autoCompId), { duration: 0.2, afterFinish:fnAfterShow });
  }


}

function fnAfterHide ()
{
  g_InputField2ComboBox_curHideId = "";
}

function InputField2ComboBox_Hide (mode)
{
  try
  {
    if (g_InputField2ComboBox_curHideId.length > 0)
    {
      inputCtrl = $(g_InputField2ComboBox_curHideId);
      if (inputCtrl)
      {
        var autoCompId = InputField2ComboBox_AutoCompId(InputField2ComboBox_GetDataListName (inputCtrl));
        autoCompId += inputCtrl.identify();

        if (IsDefined($(autoCompId)))
        {
          inputCtrl.removeClassName ("is_open");
          if ("immediately" == mode)
          {
            $(autoCompId).hide();
          }
          else
          {
            $(autoCompId).hide();
          }
          fnAfterHide ();
        }

      }
    }
  }
  catch (e) 
  {
    now_debug_out ("ERR: InputField2ComboBox_Hide: " + e + ")", 1000, "A")
  }

  if (need_ie6_IFrame())
  {
    removeBodyClasses("hideListBoxes");
  }

}

function InputField2ComboBox_GetDataListName (inputCtrl)
{
  var dataListName = "";
  try
  {
    $w(inputCtrl.className).each(function(className)
    {
      var posList = className.indexOf("_List");
      if (posList > 0)
      {
        dataList = eval(className);
        if (dataList && "string" == typeof dataList && dataList.length > 0)
        {
          dataListName = className.substr (0, posList);
          if (!inputCtrl.id)
          {
            inputCtrl.id = dataListName + "ipcbId";
          }
        }
        else
        {
          if (!dataList)
          {
            if ("string" != typeof (dataList))
            {
              now_debug_out ("combobox ("+className+"): missing definition of csv-list-values", 1000, "A")
              now_debug_out ("typeof dataList ="+ typeof (dataList), 1000, "A")
            }
          }
          else if ("string" != typeof dataList)
          {
            now_debug_out ("combobox ("+className+"): invalid type: type is: " + typeof eval(className), 1000, "A")
          }
        }
      }
    })
  }
  catch (e) 
  {
    now_debug_out ("ERR: InputField2ComboBox_GetDataListName: " + e + ")", 1000, "A")
  }
  return dataListName;
}

function ComboBox_Behaviour_Selector_Init (selector)
{
  try
  {
    $$(selector).each(function(inputCtrl)
    {
      dataListName = InputField2ComboBox_GetDataListName (inputCtrl);

      if (dataListName && "" != dataListName)
      {
        var classListName = dataListName + "_List";
        var comboData = eval(classListName);
        if (comboData && comboData.length > 0)
        {
          if (!inputCtrl.hasClassName("_isInitialized_"))
          {
            inputCtrl.addClassName("_isInitialized_");
            InputField2ComboBox_CreateArrow (inputCtrl);

            if (!inputCtrl.value || ("" == inputCtrl.value))
            {
              inputCtrl.value = comboData.split(',')[0].trim().split(':')[0];
            }
            inputCtrl.onfocus = function()
            {
              if ("" != g_InputField2ComboBox_curHideId)
              {
                new PeriodicalExecuter(function(pe) {pe.stop();InputField2ComboBox_Hide();}, 0.3);
              }
  		        return true;
		        }
            inputCtrl.onblur = function()
            {
              InputField2ComboBox_CheckValid (this);
              if ("" != g_InputField2ComboBox_curHideId)
              {
                g_InputField2ComboBox_curHideId = this.id;
                new PeriodicalExecuter(function(pe) {pe.stop();InputField2ComboBox_Hide();}, 0.3);
              }
  		        return true;
		        }
          }
          else
          {
            // now_debug_out ("* SKIP * ComboBox ("+inputCtrl.name+"): is already initialized", 0);
          }
        }
      }
    })
  }
  catch (e) 
  {
    now_debug_out ("ERR: InputField2ComboBox Init: " + e + ")", 1000, "A")
  }
}

function ComboBox_Behaviour_Init_simple ()
{
  try
  {
    $$("input.comboBox").each(function(inputCtrl)
    {
      dataListName = InputField2ComboBox_GetDataListName (inputCtrl);

      if (dataListName && "" != dataListName)
      {
        var classListName = dataListName + "_List";
        var comboData = eval(classListName);
        if (comboData && comboData.length > 0)
        {
          inputCtrl.addClassName ("simpleComboBox")
          if (!inputCtrl.value || ("" == inputCtrl.value))
          {
            inputCtrl.value = comboData.split(',')[0].trim().split(':')[0];
          }
          inputCtrl.onclick = function()
          {
            InputField2ComboBox_ComboInputClick(this);
  		      return true;
		      }
          inputCtrl.onblur = function()
          {
            InputField2ComboBox_CheckValid (this);
  		      return true;
		      }
        }
      }
    })
  }
  catch (e) 
  {
    now_debug_out ("ERR: InputField2ComboBox Init: " + e + ")", 1000, "A")
  }
}
function ComboBox_Behaviour_Init ()
{
  ComboBox_Behaviour_Selector_Init ("input.comboBox");
 // ComboBox_Behaviour_Init_simple ()

}