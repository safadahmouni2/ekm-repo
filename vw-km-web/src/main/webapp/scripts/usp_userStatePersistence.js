
function sync_footer_with_bodyClass()
{
  var oFooter = $('areaFooter');
  if (oFooter)
  {
    oFooter.select ('li.pullup input[type=radio],li.pullup input[type=checkbox]').each(function(inp)
    {
      inp.checked = hasBodyClasses(inp.value);
      if (inp.hasClassName("invers"))
      {
        inp.checked = !inp.checked;
      }
      if (inp.checked)
      {
        usp_setCookie (inp.value);
      }
    })
    
  }
}

function usp_userStatePersistence_Init()
{
  sync_footer_with_bodyClass();
}