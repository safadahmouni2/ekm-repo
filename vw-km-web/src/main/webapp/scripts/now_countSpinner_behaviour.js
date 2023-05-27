
function SpinnerCtrl_CreateHandle (inputCtrl)
{
  var choiseHandle = GetClientCtrlHandleHtml (inputCtrl, "<span class='spinnerCtrlUp' onclick='SpinnerCtrl_Up(this)'></span><span class='spinnerCtrlDown' onclick='SpinnerCtrl_Down(this)'></span>")
  inputCtrl.insert ({after: choiseHandle});
  newId = inputCtrl.next("span.clientCtrl").identify()

  $(newId).addClassName ("leftSpinner");
  addClassNames ($(newId), "halloTest");

  $(newId).insert ({top: inputCtrl});

  $$(".spinnerCtrlUp, .spinnerCtrlDown").each(function(el)
  {
     el.observe('keydown', function (e) {return SpinnerCtrl_OnKeyDown (this, e);});
  })
}

function SpinnerCtrl_Up (oThis)
{
  var inp = $(oThis).previous("input");
  if (!inp)
  {
    local_alert ("spinnerCtrl input not fouund");
    return true;
  }
  else
  {
    var format = inp.className.replace('spinnerCtrl','');
    format = format.replace('required','');
    format = format.trim();
    var val = $F(inp);
    if (!IsNumeric(val))
    {
      val = "0";
    }
    inp.value = 1 * val + 1;

    return false;
  }
}

function SpinnerCtrl_Down (oThis)
{
  var inp = $(oThis).previous("input");
  if (!inp)
  {
    local_alert ("spinnerCtrl input not fouund");
    return true;
  }
  else
  {
    var format = inp.className.replace('spinnerCtrl','');
    format = format.replace('required','');
    format = format.trim();
    var val = $F(inp);
    if (!IsNumeric(val) || val < 1)
    {
      val = "1";
    }
    inp.value = 1 * val - 1;

    return false;
  }
}

function SpinnerCtrl_OnKeyDown (oThis, evt)
{
  retval = false;

  keyCode = evt.keyCode

  if (keyCode == 0 || keyCode == null) {
    // Firefox
    keyCode = evt.charCode;
  }


  switch (keyCode)
  {
    case Event.KEY_PLUS:
    case Event.KEY_UP:
      SpinnerCtrl_Up (oThis);
      break;

    case Event.KEY_MINUS:
    case Event.KEY_DOWN:
      SpinnerCtrl_Down (oThis);
      break;

    default:
      if ((16 != keyCode) && (17 != keyCode))
      {
      //  local_alert (keyCode)
      }
      retval = true;
      break;
  }
  return retval;
}

function SpinnerCtrl_Behaviour_Init ()
{
	$$("input.spinnerCtrl").each(function(el)
	{
    SpinnerCtrl_CreateHandle (el);
	})
}

