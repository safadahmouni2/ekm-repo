function OrderStatusV5_Init() 
{
  initOrderStatusMemberClick ();
}

function handleOrderStatusMemberClick (oThis)
{
  oThis = $(oThis);

  $$('.memberGroup .member').invoke("removeClassName", "selected");

  $$('.memberGroup .member input[type=text]').each(function(el)
  {
    nowValidatorSetValid(el, true);
    SetDisabled(el, true);
    el.removeClassName("required");
  })

  oThis.addClassName("selected");

  var inp = oThis.down("input[type=text]");
  inp.addClassName("required");
  SetDisabled(inp, false);
  SetFocus(inp);
  var radio = oThis.down("input[type=radio]");
  radio.checked=true;
  return true;
}

function initOrderStatusMemberClick ()
{
  var toFocus = $$('.memberGroup .member input[type=text]').first();
  $$('.memberGroup .member').each(function(el)
  {
    var inp = el.down("input[type=text]");
    if (inp && inp.value != "")
    {
      toFocus = inp;
    }
		el.onclick = function()
    {
      return handleOrderStatusMemberClick (this);
		}
  })

  var trFocus = toFocus.up("tr");
  handleOrderStatusMemberClick (trFocus);
  // handleOrderStatusMemberClick ($$('.memberGroup .member').first());
}

