
function GetClientCtrlHandleHtml (oBase, actionHtml)
{
  var clsNames = "clientCtrl";
  var baseCls = $(oBase).className;
  if (baseCls && baseCls.length > 0)
  {
    if (oBase.hasClassName ("readonly"))
    {
      clsNames += " readonly";
    }
    clsNames += getClassValueKey (baseCls, "margin");
    clsNames = clsNames.trim();
  }
  return "<span class='"+clsNames+"' onmouseover='clientCtrlOnHover($(this));' onfocus='clientCtrlOnFocus($(this))' onmouseout='clientCtrlOnOut ($(this))'>"+actionHtml+"</span>";
}

function clientCtrlOnHover (oThis)
{
  if (!oThis.hasClassName("readonly"))
  {
    oThis.addClassName("hover");
  }
  oThis.select("input.calendar,input.comboBox").each(function(cal)
  {
    clientCtrl_SetCurrendState (oThis);
  })
}

function clientCtrlOnOut (oThis)
{
  oThis.removeClassName("hover");
}

function clientCtrlOnFocus (oThis)
{
  if (!oThis.hasClassName("readonly"))
  {
    oThis.addClassName("hover");
  }
}
