
function HoverMenu_Behaviour_Init(rootSelector) 
{
	var listSelector = rootSelector + " ul li";

	$$(listSelector).each(function(elem)
  {
    $(elem).addClassName("cursorPointer");
    if ($(elem).immediateDescendants()[1])
    {
      elem.addClassName('smenu');
    }
	//	if (IsBrowserVersion("IE_6-7"))
//		if (!IsBrowserVersion("W3C"))
		if (Prototype.Browser.IE)
    {
		  elem.onmouseover = function()
      {
        this.addClassName('hover');
		  }
		  elem.onmouseout = function()
      {
        this.removeClassName('hover');
		  }
    }
  })
}
