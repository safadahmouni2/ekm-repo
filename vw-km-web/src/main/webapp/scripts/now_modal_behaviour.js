
function modal_scroll_behaviour()
{
  if ("function" == typeof (Footer_OnScroll_Behaviour)) {Footer_OnScroll_Behaviour ();}
}

function set_modal(mode)
{
  if (mode)
  {
    addBodyClasses ('mode_Modal'); 
    removeBodyClasses("loadingCanceled");
  }
  else
  {
    removeBodyClasses ('mode_Modal sys_submit loading load_Modal');
    addBodyClasses("loadingCanceled");
  }
  modal_scroll_behaviour();
}

function set_modal_submit()
{
  set_modal(true);
  addBodyClasses('sys_submit');
}

function submit_cancel()
{
  if(self.stop)
  {
    stop();
  }
  else 
  {
    if(document.execCommand)
    {
     document.execCommand('Stop');
    }
  }
  set_modal(false);
  return false;
}

function modal_Behaviour_Init ()
{

}

