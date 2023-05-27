
function call_function_by_string (funcName, oThis, keyCode)
{
  if (("string" == typeof (funcName)) && funcName.length > 0)
  {
    try
    {
      if ("function" == typeof (eval(funcName)))
      {
        return eval(funcName +"($(oThis), keyCode)");
      }
    }
    catch(e)
    {
      //local_alert ("FEHLER in special_page_behaviour_init_by_form_class: \nInit-Function ("+initFunc+"); \nerror=" + e);
    }
  }
  return false;
}

function special_page_behaviour_init_by_form_class()
{
  var oForm = getForm();
  var ret = false;
  if (oForm)
  {
    var allFunctions = oForm.className;
    var initFunc = getClassValue (allFunctions, 'special_');
    while (initFunc.length > 0)
    {
      // now_debug_out ("initFunc="+initFunc);
      ret =  call_function_by_string (initFunc, oForm, 0);
      allFunctions = allFunctions.replace('special_' + initFunc,'');
      initFunc = getClassValue (allFunctions, 'special_');
    }
  }
  return ret;
}
