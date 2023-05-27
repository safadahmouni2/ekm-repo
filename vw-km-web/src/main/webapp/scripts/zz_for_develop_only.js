// usage: now_debug_out (msg,level,modul)

g_devDbgWin_filter_modul = "";

DevDbgWinTreshold_List = "0,1,2,3,4,5,6,7,8,9,10,100,1000,10000";
DevDbgWinModul_List = "-- A=all,-- G=generic,start,locating,orderentry,ordersearch,ordercheck,cdm";



function getDevDbgWin ()
{

  var oDevWin = $('clone_devDbgWin');
  if (!oDevWin)
  {
    var devId = 'devDbgWin';
    oDevWin = $(devId);
    if (!oDevWin)
    {
      var clsClosed = "";
      if (IsDefined (g_mCookie))
      {
        if (("1" == g_mCookie.get ("usp_developDbgWnd")))
        {
          clsClosed = " closed";
        }
      }

      var ddw_tl = '<label for="DevDbgWinTreshold"><strong>Print Filter:</strong> Level L &gt;= </label><input class="comboBox DevDbgWinTreshold_List sSize max_2 fixed" id="DevDbgWinTreshold" type="text" maxlength="2" name="DevDbgWinTreshold" />';
      var ddw_moduls = '<label for="DevDbgWinModul">Modul = </label><input class="comboBox DevDbgWinModul_List xxmSize max_64 fixed" id="DevDbgWinModul" type="text" maxlength="64" name="DevDbgWinModul" />';
      var ddw_btnClear = '<a class="button" href="#" onclick="return developDebugWinClear()">clear</a>';
      var ddw_btnSysInfo = '<a class="button" href="#" onclick="return developDebugWinSysInfo()">SysInfo</a>';
      var ddw_btnModal = '<input type="checkbox" class="checkbox" name="dbgWndModal" id="dbgWndModal" onclick="return dragWindowToggleModal(this)" /><label for="dbgWndModal">modal</label>';
      var ddw_btn = ddw_btnClear + ddw_btnSysInfo + ddw_btnModal;
      var ddw_footer = ddw_tl + " " + ddw_moduls + " " + ddw_btn;

      oDevWin = dragWindowCreate(devId, "<span class=\"counter\">0</span>: now_debug_out (messages, level, modul)</span>", "", ddw_footer, "collapsible usp_developDbgWnd");   
  }
  }
  return oDevWin;
}

function developDebugWinClear ()
{
  var devWin = getDevDbgWin ();
  devWin.down(".content").update ("");
  devWin.down("h4 .counter").update ("0");
  dragWindowRedraw ('devDbgWin');
  return false;
}

function developDebugWin_OutText (txt,level,modul)
{
  outText = "";

  var devWin = getDevDbgWin ();
  if (devWin)
  {

    var curTrsHld = $F("DevDbgWinTreshold")
  
    if ("undefined" == typeof(level) || "" == level || curTrsHld <= 0)
    {
      level = 1;
    }
  
    if (curTrsHld <= level)
    {
      var curModul= $F("DevDbgWinModul")
  
      if ("undefined" == typeof(modul) || "" == modul)
      {
        curModul = "";
        modul = " - default - "
      }
      else
      {
        if ("-- A=all" == curModul)
        {
          curModul = "";
        }
        else
        {
          switch (modul) {
            case "G":
            case "GENERIC":
              if ("-- G=generic" == curModul)
              {
                curModul = "";
              }
              break;
            case "A":
            case "ALL":
              curModul = "";
              break;
            default:
              if (modul == curModul)
              {
                curModul = "";
              }
              break;
          }
        }
      }
  
      if (devWin.visible() && "" == curModul)
      {
        outText = "<abbr title='Level="+level+"; Modul="+modul+"'><span title='Level="+level+"; Modul="+modul+"'>L" + level + ":</span></abbr> " + txt;
      }
    }
  }
  
  return outText;

}

function developDebugWin_TagWrite (txt,level,modul,tag)
{
  var devWin = getDevDbgWin ();
  if (devWin)
  {
 
    var curTrsHld = $F("DevDbgWinTreshold")
 
    if ("undefined" == typeof(level) || "" == level || curTrsHld <= 0)
    {
      level = curTrsHld;
    }
  
    if ("string" != typeof(tag))
    {
      tag = "p";
    }


    if (curTrsHld <= level)
    {
      var curModul= $F("DevDbgWinModul")
  
      if ("undefined" == typeof(modul) || "" == modul)
      {
        curModul = "";
        modul = " - default - "
      }
      else
      {
        if ("-- A=all" == curModul)
        {
          curModul = "";
        }
        else
        {
          switch (modul) {
            case "G":
            case "GENERIC":
              if ("-- G=generic" == curModul)
              {
                curModul = "";
              }
              break;
            case "A":
            case "ALL":
              curModul = "";
              break;
            default:
              if (modul == curModul)
              {
                curModul = "";
              }
              break;
          }
        }
      }
  
      if (devWin.visible() && "" == curModul)
      {
        
        var oCon = devWin.down(".content");
        if (!oCon)
        {
          oCon = devWin.down(".body");
        }

        if ("" == tag)
        {
          oCon.insert (developDebugWin_OutText (txt,level,modul));
        }
        else
        {
          oCon.insert ("<" + tag + ">" + developDebugWin_OutText (txt,level,modul) + "</" + tag + ">");
        }
        var oCount = devWin.down("h4 .counter");
        var count = 1 * oCount.innerHTML + 1;
        oCount.update (count);
      }
    }
  }
}

function developDebugWin_WriteLn (txt,level,modul)
{
  developDebugWin_TagWrite (txt,level,modul,"p");
}

function developDebugWinSysInfoOut (msg)
{
  developDebugWin_WriteLn (msg, "A", 10);
}

function developDebugWinSysInfo ()
{
  var bfXPath = Prototype.BrowserFeatures.XPath ? "XPath " : "";
  var bfSelectorsAPI = Prototype.BrowserFeatures.SelectorsAPI ? "SelectorsAPI " : "";
  var bfElementExtensions = Prototype.BrowserFeatures.ElementExtensions ? "ElementExtensions " : "";
  var bfSpecificElementExtensions= Prototype.BrowserFeatures.SpecificElementExtensions ? "SpecificElementExtensions " : "";

  developDebugWinSysInfoOut ("debug-window usage:");
  developDebugWinSysInfoOut (" &#160;  <b>now_debug_out (msg,level,modul)</b>");
  developDebugWinSysInfoOut (" &#160;   * <b>msg</b>: the message to display (required)");
  developDebugWinSysInfoOut (" &#160;   * <b>level</b>: output-Level L (optional, see Print <b>Filter L</b>)");
  developDebugWinSysInfoOut (" &#160;   * <b>modul</b>: Modul-Name, (optional, see Print <b>Filter Modul</b>)<br />");

  developDebugWinSysInfoOut ("------ SysInfo: -------");
  developDebugWinSysInfoOut ("Prototype Version: " + Prototype.Version);
  developDebugWinSysInfoOut ("Browser:  userAgent = " + navigator.userAgent);
  developDebugWinSysInfoOut ("Browser:  Browser.Version = " + Prototype.Browser.Version);
  developDebugWinSysInfoOut ("Browser:  Browser.KeyName = " + Prototype.Browser.KeyName);
  developDebugWinSysInfoOut ("Browser:  Browser.KeyVersion = " + Prototype.Browser.KeyVersion);
  developDebugWinSysInfoOut ("Browser:  BrowserFeatures = " + bfXPath + bfSelectorsAPI + bfElementExtensions + bfSpecificElementExtensions);
  
  if (Prototype.Browser.IE)
  {
    developDebugWinSysInfoOut ("Browser:  IE CompatMode = " + document.compatMode);
  }

  var windowScroll = document.viewport.getScrollOffsets();    
  developDebugWinSysInfoOut ("Window:  windowScroll = (" + windowScroll.left + ", " + windowScroll.top + ")");
  developDebugWinSysInfoOut ("Window:  location = (" + getCurrendLocation() + ")");
  developDebugWinSysInfoOut ("-------------");
  dragWindowRedraw ('devDbgWin');
  return false;
}

function developHandleDebugWin (oThis)
{
  var devWin = getDevDbgWin ();
  if ($(oThis).checked)
  {
    dragWindowShow(devWin);
  }
  else
  {
    dragWindowHide(devWin);
    // devWin.hide();
  }
  return true;
}
