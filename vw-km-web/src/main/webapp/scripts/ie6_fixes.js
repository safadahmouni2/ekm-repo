

function ie6_dragWindowGetIFrame (theId)
{
  if ($(theId))
  {
    var frameId = "iFrameChild_" + getClassValue ($(theId).className, 'iFrameChild_');
    return $(frameId);
  }
  return null;
}

function ie6_dragWindowCreateIFrame (oMainElem, iFrmClsName)
{
  if (oMainElem)
  {
    var iFrmId = "iFrameChild_now" + oMainElem.identify();
    
    var mainId = oMainElem.identify();
    if (0 == mainId.indexOf("clone_"))
    {
      iFrameId = mainId.substr(6);
      clonedIFrmId = "iFrameChild_now" + mainId.substr(6);
      if ($(clonedIFrmId))
      {
        iFrmId = clonedIFrmId;
      }
    }
    
    var zIndex = oMainElem.getStyle("z-index");
    // now_debug_out ("ie6_dragWindowCreateIFrame ("+oMainElem.identify()+"), z-index = " + zIndex);
    // now_debug_out ("iFrmId = ("+iFrmId+")");
    
    if (!$(iFrmId))
    {
      if ("string" == typeof (iFrmClsName))
      {
        iFrmClsName += " ie6IFrameFix";
      }
      else
      {
        iFrmClsName = "ie6IFrameFix";
      }
      var iFrame = '<iframe id="'+iFrmId+'" class="'+iFrmClsName+'" src="/NewadaOnWeb/res/html/iFrameDummyContent.html" border="0"></iframe>';
      getBody().insert (iFrame);
    }
    var oIFrame = $(iFrmId);
    oIFrame.setStyle({"z-index": zIndex-1});
    oMainElem.addClassName(iFrmId);

    Position.clone(oMainElem, oIFrame);
    dragWindowReposition (oMainElem.identify(), 2);
  }

}

function remove_size_units(size)
{
  size = size.replace('px','');
  size = size.replace('%','');
  size = size.replace('em','');
  return size;
}

function ie6_fixes_WindowRedraw (oMainElemId, offset)
{ // hier darf kein now_debug_out aufgerufen werden, sonst gibt's ne endlosschleife !!!
  var oIFrame = ie6_dragWindowGetIFrame(oMainElemId);
  if (oIFrame)
  {
    if (!offset)
    {
      offset = 0;
    }
    Position.clone($(oMainElemId), oIFrame);
    var t = oIFrame.style.top.replace('px','');
    var l = oIFrame.style.left.replace('px','');
    var w = oIFrame.style.width.replace('px','');
    var h = oIFrame.style.height.replace('px','');
    oIFrame.style.left = (1 * l - offset) + "px";
    oIFrame.style.top = (1 * t - offset) + "px";
    oIFrame.style.width = (1 * w + 2 * offset) + "px";
    oIFrame.style.height = (1 * h + 2 * offset) + "px";
  }
}

function ie6_fixes_WinReposition (oMainElemId, offset)
{
  var oIFrame = ie6_dragWindowGetIFrame(oMainElemId);
  if (oIFrame)
  {
    if (!offset)
    {
      offset = 0;
    }
    ie6_fixes_WindowRedraw (oMainElemId, offset);
  }
}


function ie6_fixes_Init ()
{
}


