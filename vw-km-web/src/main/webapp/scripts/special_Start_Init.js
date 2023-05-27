var OnBeforeClientUnload = null;
var offLeft = 0;
var offTop = 0;
var mouseDown = false;
var frame = null;
var div = null;
var IsTickerVisible = false;
var curTickerIndex  = -1;
var grdTerminationRequest = document.getElementById("_ctl0_grdTerminationRequest");
var hddTermInfoIndex      = document.getElementById("_ctl0_hddTermInfoIndex");

function Start_Init()
{
  
	// Newsticker
  fnStartNewsticker ();
  
  if (typeof(grdTerminationRequest) != "undefined")
  {
    SetGridLineFirstTermInfo(grdTerminationRequest.id, hddTermInfoIndex);
    SetGridLineFirstTermInfo(grdCommissionInfo.id, hddTermInfoIndex);
	}
	
	if (typeof(hddTermInfoIndex) != "undefined")
	{
        var isInfoClosed = $F(hddIsInfoClosed) == "1";
        var infoBlock    = document.getElementById("trInfoBlock");
        var infoAdd      = document.getElementById("tblInfo");
    
        if (infoBlock != null && infoAdd != null)
        {
          infoBlock.style.display = isInfoClosed ? "block" : "none";
          infoAdd.style.display = isInfoClosed ? "none" : "block";
        }
	
 
	  var tbl = document.getElementById(grdTerminationRequest);	
		if (tbl == null)
		{
	    tbl = document.getElementById(grdCommissionInfo);	
		}
		if (tbl != null)
	  {
      var oldTitle = "";
  		for (var RowIndex=0; RowIndex < tbl.rows.length; RowIndex++)
  		{
	      var row = tbl.rows[RowIndex];
        if (row != null)
        {
				  for (var ColIndex=0; ColIndex < row.cells.length; ColIndex++)
				  {
            var title = row.cells[ColIndex].innerText;
            if (ColIndex == 0 && title == "")
            {
              title = oldTitle;
            }
					  //row.cells[ColIndex].title = title;
            if (ColIndex == 0)
            {
              oldTitle = title;
            }
				  }
        }
      }
    }
	}

}
function PrepareNewInput()
{
  if (typeof(cblBrand) != "undefined")
  {
  	GetGridData(cblBrand, "INPUT", 0, hddBrand);
  }
  PrepareServerClick();
}
function ResetInput()
{
  PrepareNewInput();
  ResetGridData();
}
function ResetGridData()
{
  //RemoveTableByName(grdTerminationRequest);
  //RemoveTableByName(grdCommissionInfo);

  $$('.divResultData').each(function(el)
  {
    el.addClassName('invisible');
  });
  
//  var counter = document.getElementById(spanCountInfo);
//  var counter1 = document.getElementById(spanCountInfo1);
//  if (counter != null && counter1 != null)
//  {
//    counter.innerText = "0 - 0";
//    counter1.innerText = "0 - 0";
//  }
  return true;
}
function SelectionChanged()
{
  ResetGridData();
}
function SortAction(SortExpression) 
{
    document.getElementById('_ctl0_hddSortOrder').setAttribute('value', SortExpression);
    document.getElementById('_ctl0_hddCommand').setAttribute('value', 'SortClick');
    return false;
}

function ShowDetailsWindow(gridName, trCtrl) 
{
    var aInfoData   = $('InfoData').select('td');
    var aTrCtrl     = $(trCtrl).select('td');
    var nHeight     = 0;
    var nTop        = 0;
    
    RowSelect(trCtrl);

    SetValue( hddTermInfoIndex, trCtrl.rowIndex - 1 );

    aInfoData[1].innerHTML = aTrCtrl[0].innerHTML;
    aInfoData[3].innerHTML = aTrCtrl[3].innerHTML;
    aInfoData[5].innerHTML = aTrCtrl[6].innerHTML;

    dragWindowShow('overWindow', false);

    nHeight = document.viewport.getDimensions().height;
    nTop    = Math.abs( Position.realOffset(trCtrl)[1] ) + Math.floor( nHeight / 4 );

    $$('.dragWindow').each(function(xWindow)
    {
        xWindow.style.top = nTop;
        //xWindow.setStyle( { top: nTop } );
        
    });
    
}
function RowSelect(trCtrl) 
{
    SetValue(hddTermInfoIndex, trCtrl.rowIndex -1);
    
    $(trCtrl).up('table').select('tr').invoke('removeClassName', 'selected');
    $(trCtrl).addClassName ('selected');
}

function CommRowSelected(gridName, trCtrl)
{
  RowSelected(gridName, trCtrl);
  var rowIndex = trCtrl.rowIndex -1;
  SetValue(hddTermInfoIndex, rowIndex);

  __doPostBack(grdCommissionInfo, 'Click');
}

function SetGridLineFirstTermInfo(tblName, selectedIndex)
{
  var tbl = document.getElementById(tblName);

	var index = $F(selectedIndex);


  if (tbl != null && index != "")
  {
	  var intVal = parseInt(index) +1;
	  if (tbl.rows.length > intVal)
	  {
        $(tbl).select('tr')[intVal].addClassName('selected');
	  }
  }
}
function OpenTermPrintWindow(indexVal, sessionId)
{
  SetValue(hddCommand, "PrintClick");
  window.open("TermInfoPdf.apdf?Index="+indexVal+"&amp;vwcstat="+sessionId);
}
function OpenListPrintWindow(sessionId)
{
  SetValue(hddCommand, "PrintClick");
  window.open("TermInfoListPdf.apdf?vwcstat="+sessionId);
}
function OnCommInfoRadioClick(parent)
{
  if (parent.parentElement != null && parent.parentElement.parentElement != null)
  {
    var rowIndex = parent.parentElement.parentElement.rowIndex -1;
    SetValue(hddTermInfoIndex, rowIndex);
    __doPostBack(grdCommissionInfo, 'Click');
  }
}
function OnTermination()
{
  SetValue(hddCommandTerm, "TermClick");
  SetValue(hddCommand, "TermClick");
}
function fnStartNewsticker (contentSelector)
{
  // now_debug_out ("fnStartNewsticker...");
    
	if (fnShowNextNewsTickerEntry ())
  {
    new PeriodicalExecuter(fnShowNextNewsTickerEntry, 2);
  }
}

function fnShowNextNewsTickerEntry ()
{
  return fnShowNewsTickerEntry (curTickerIndex+1);
}

function fnShowNewsTickerEntry (index)
{
  var ret = false;
  if (IsDefined($("txtActNewsTickerEntry")) && typeof(grdNewsTickerData) == "string")
  {
    var aTr = $$("#" + grdNewsTickerData + " tr");
    if (0 < aTr.length)
    {
      if (0 > index || index >= aTr.length)
      {
        index = 0;
      }
      curTickerIndex = index;
      var oLine = $("txtActNewsTickerEntry");
      var oSrc = aTr[curTickerIndex];
      if (IsDefined(oLine))
      {
        ret = aTr.length > 1;
        oLine.update(oSrc.innerHTML);
        var oClick = oSrc.down("td[onclick]");
        if (IsDefined(oClick))
        {
          oLine.onclick = oClick.onclick;
          oLine.addClassName("cursorPointer");
          oLine.removeClassName("cursorDefault");
        }
        else
        {
          oLine.onclick = function (){return true};
          oLine.removeClassName("cursorPointer");
          oLine.addClassName("cursorDefault");
        }
      }
    }
  }
  return ret;

}

function OnTickerClick(url)
{

}


/////////////////////////////////////////////////////////////////////
//COMMISSIONINFO.js
var OnBeforeCommInfoClientUnload = null;
var offLeftCommInfo = 0;
var offTopCommInfo = 0;
var mouseDownCommInfo = false;
var g_CommInfoFrame = null;
var g_CommInfoDiv = null;


function GetCommInfoPageWidth()
{
  if (window.innerWidth) 
  {
    return window.innerWidth;
  }
  else if (document.body && document.body.offsetWidth) 
  {
    return document.body.offsetWidth;
  }
  else 
  {
    return 1024;
  }
}
function SetCommInfoPosition()
{
  if (typeof(divCommInfo) != "undefined" && typeof(frmCommInfo) != "undefined")
  {
    var left = (GetCommInfoPageWidth()-540) / 2; 
    var div = document.getElementById("divCommInfo")
    if (div!=null) 
    {
      div.style.left = left;
    }

    var frame = document.getElementById("frmCommInfo");
    if (frame!=null) 
    {
      frame.style.left = left;
    }
  }
}

function CommInfoLoad ()
{
  SetCommInfoPosition();
  g_CommInfoFrame = document.getElementById("frmCommInfo");
	g_CommInfoDiv = document.getElementById("divCommInfo");
  
  if (typeof(g_CommInfoDiv) != "undefined")
  {
    var div = document.getElementById("divCommInfo")
    if (div != null)
    {
      tblContentCommInfo.style.cursor = "default";
      document.body.onselectstart = OnCommInfoSelectStart;
      document.body.onmousemove = OnCommInfoMouseMove;
      document.body.onmouseup = OnCommInfoMouseUp;
    }
  }
}

function CommInfoResize ()
{
  SetCommInfoPosition();
  if (oldOnStartResize!=null) 
  {
    oldOnStartResize();
  }
}

function OnCommInfoMouseDown()
{
  var frame = document.getElementById("frmCommInfo");
  var div = document.getElementById("divCommInfo");
  if (frame != null && div != null && !IsCommInfoSpecialArea())
  {
    offLeftCommInfo = event.clientX - frame.offsetLeft;
    offTopCommInfo = event.clientY - frame.offsetTop;
    mouseDownCommInfo = true;
    div.style.borderWidth = "2";
  }
}

function OnCommInfoMouseUp()
{
  var div = document.getElementById("divCommInfo");
  if (div!=null) 
  {
    mouseDownCommInfo = false;
    div.style.borderWidth = "1";
  }
}

function OnCommInfoMouseMove()
{
  if (event.button == 1 && mouseDownCommInfo)
  {
    if (IsCommInfoSpecialArea())
    {
      mouseDownCommInfo = false;
    }
    else
    {
      if (g_CommInfoFrame!=null && g_CommInfoDiv != null)
      {
        g_CommInfoFrame.style.left = event.clientX - offLeftCommInfo;
        g_CommInfoDiv.style.left = event.clientX - offLeftCommInfo;

        g_CommInfoFrame.style.top  = event.clientY - offTopCommInfo; 
        g_CommInfoDiv.style.top  = event.clientY - offTopCommInfo - 3; 
      }
    }
  }
}
function IsCommInfoSpecialArea()
{
  return event.srcElement != null && event.srcElement.className != "TdHeadlineText";
}

function OnCommInfoSelectStart() 
{
  return false;
}

/////////////////////////////////////////////////////////////////////
//TerminationInfo.js

var offLeftTermInfo = 0;
var offTopTermInfo = 0;
var mouseDownTermInfo = false;
var g_TermInfoFrame = null;
var g_TermInfoDiv = null;

function GetTermInfoPageWidth()
{
  if (window.innerWidth) 
  {
    return window.innerWidth;
  }
  else if (document.body && document.body.offsetWidth) 
  {
    return document.body.offsetWidth;
  }
  else 
  {
    return 1024;
  }
}
function SetTermInfoPosition()
{
  if (typeof(divTermInfo) != "undefined" && typeof(frmTermInfo) != "undefined")
  {
    var left = (GetTermInfoPageWidth()-540) / 2; 
    var div = document.getElementById("divTermInfo")
    if (div!=null) 
    {
      div.style.left = left;
    }

    var frame = document.getElementById("frmTermInfo");
    if (frame!=null) 
    {
      frame.style.left = left;
    }
  }
}
