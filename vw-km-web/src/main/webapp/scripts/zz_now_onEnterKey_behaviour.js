

function onEnterKey_behaviour_init()
{
 
  now_debug_out("onEnterKey_behaviour_init...");
  $$("button[onclick]").each(function(btn)
  {
    now_debug_out("btn: " + btn.identify());
    var click = btn.readAttribute("onclick");
    if (("string" == typeof click) && ("" != click))
    {
      var kDwn = btn.readAttribute("onkeydown");
      if (("string" != typeof click))
      {
        btn.onkeydown = function(evt)
        {
      now_debug_out("kDwn: " + kDwn);
          var nKeyCode = keyCodeFromEvent (evt);
          alert (nKeyCode);
          switch (nKeyCode) 
          {
           case Event.KEY_RETURN:
               $(this).click();
               return false;
          }
          return true;
        }
      }
    }
  })
}