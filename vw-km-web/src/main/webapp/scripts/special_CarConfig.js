var CLR_EVA_EXCLUDE_SELECTED ="#BC8C8C"; 
var CLR_EVA_EXCLUDE          ="#DCACAC"; 
var CLR_EVA_INCLUDE_SELECTED ="#ADC2EB";
var CLR_EVA_INCLUDE			     ="#D6E0F5";
var CLR_INT_INCLUDE_SELECTED ="#FFADAD";
var CLR_INT_INCLUDE			     ="#FFCCCC";
var CLR_SELECTED			       ="#CCCCCC";
var CLR_NOCOLOR              ="#FFFFFF";
var g_maxPR=60;
var g_IsOriginType=false;
var g_vi=new VehicInfo();
var SetOptionChangedFct = new NullFct();
function NullFct(){};
function VehicInfo()
{
	this.arrPr=new Array();
	this.arrFixPr=new Array();
}

function AddNewVehiclePrLines(moreBtn)
{
	AddNewGridLine($$("table.vehiclePrNumberList").first().identify(),0,12,moreBtn);
}

function callSkipInputPrBlock(oThis)
{
	oTable = oThis.up("table");
	if (oTable.hasClassName("grdExtraInclude"))
	{
		SetFocus($$("table.grdExtraExclude input").first());
	}
}

function carConfig_move_selected(srcTab, dstTab)
{
	// now_debug_out ("carConfig_move_selected("+srcTab+", "+dstTab+")");
  oTable = $(srcTab);
  oDestTable = $(dstTab);
	var delList="";
	oTable.select("tr.selected").each(function(oRow)
	{
		var trCopy = oRow.innerHTML;
		var tBody = oDestTable.down("tbody");
		if (!IsDefined(tBody))
		{
			oDestTable.insert("<tbody>"+trCopy+"</tbody>");
		}
		else
		{
			tBody.insert(trCopy);
		}
	//  now_debug_out ("carConfig_move_selected:" + oRow.down("td").innerHTML);
		delList += "#" + oRow.identify() + ",";
	})
	
	oTable.select(delList).each(function(oRow)
	{
		oRow.remove();
	})

	carConfig_rowClickInit(oDestTable);
  CheckPrOutput();


}

function carConfig_std_moveSelected(oThis)
{
	var oTable = $(oThis).up("table");
	var dstTab = grdInclude;
	if (!oTable.hasClassName("grdAllPr"))
	{
		dstTab = grdAllPr;
	}	
	carConfig_move_selected(oTable, dstTab);
}

function carConfig_on_rowClick(oThis)
{
	var oRow = $(oThis);
	oRow.toggleClassName("selected");
  // now_debug_out ("rowClick:" + oRow.down("td").innerHTML);
}
function carConfig_on_rowDblClick(oThis)
{
	var oRow = $(oThis);
	oRow.addClassName("selected");
	carConfig_std_moveSelected(oThis);
}

function carConfig_rowClickInit(oTable)
{
	oTable.select("tr").each(function(el)
	{
		el.onclick = function()
		{
			return carConfig_on_rowClick (this);
		}
		el.ondblclick = function()
		{
			return carConfig_on_rowDblClick (this);
		}
	})
}

function carConfig_onHoverInit(tab)
{
	oTable = $(tab);
	if (IsDefined(oTable))
	{
		oTable.onmouseover = function()
		{
			var oTab = $(this);
			if (!oTab.hasClassName("isInitialzed"))
			{
				carConfig_rowClickInit(oTab);
				oTab.addClassName("isInitialzed");
			}
		}
	}
}

var g_isInitialized_OnCarConfigLoad = false;
function OnCarConfigLoad()
{
	
	if (!g_isInitialized_OnCarConfigLoad)
	{

// now_debug_out ("OnCarConfigLoad...");

		g_isInitialized_OnCarConfigLoad = true;

		if (typeof(hddPageCommand) != "undefined")
		{
			hddPageCommand.value = "";
		}
	 
		if (typeof(hddMpCommand) != "undefined")
		{
			hddMpCommand.value = "";
		}
		
		vehiclePrNumberList_Init();

		if (typeof(grdAllPr) != "undefined" && typeof(grdInclude) != "undefined" && typeof(grdExclude) != "undefined")
		{

			carConfig_onHoverInit(grdAllPr);
		  carConfig_onHoverInit(grdInclude);
		  carConfig_onHoverInit(grdExclude);
					
			var len = g_WildType.length;
			if (len >= 1)
			{
				SetValue(txtType1, g_WildType.substr(0,1));
			}
			if (len >= 2)
			{
				SetValue(txtType2, g_WildType.substr(1,1));
			}
			if (len >= 3)
			{
				SetValue(txtType3, g_WildType.substr(2,1));
			}
			
			SetWildcard(chkWild4, txtType4, 4);
			SetWildcard(chkWild5, txtType5, 5);
			SetWildcard(chkWild6, txtType6, 6);
			var hddType = $(hddVehicleWildType);    
			if (hddType != null)
			{
				hddType.innerHTML = g_WildType;
			}
		
			if (typeof(g_EvaType) != "undefined")
			{
				g_IsOriginType = (g_Type.length > 2 && g_EvaType.length > 2 && g_Type.substr(0,3) == g_EvaType.substr(0,3));
			}
			SortEquipmentTable(grdAllPr, SortDefault);
			prepSelection(grdInclude);
			prepSelection(grdExclude);
			var allGrid = document.getElementById(grdAllPr);
			if (allGrid != null)
			{
				allGrid.style.display="block";
			}
			var incGrid = document.getElementById(grdInclude);
			if (incGrid != null)
			{
				incGrid.style.display="block";
			}
			
			var excGrid = document.getElementById(grdExclude);
			if (excGrid!=null && typeof(excGrid)!="undefined")
			{
				excGrid.style.display="block";
			}
			CheckPrOutput();
	
			PrepareGridData(hddExtraInclude, grdExtraInclude);
			PrepareGridData(hddExtraExclude, grdExtraExclude);
	
			PrepBackground(grdAllPr);
			PrepBackground(grdInclude);
	
			var btnSortDesc = document.getElementById('btnSortDesc');
			if (btnSortDesc != null && btnSortDesc.firstChild != null)
			{
				btnSortDesc.firstChild.src = "shared/gif/arrow_sort_up.gif";
			}
			
			btnSortDesc = document.getElementById('btnSortDescInc');
			if (btnSortDesc != null && btnSortDesc.firstChild != null)
			{
				btnSortDesc.firstChild.src = "shared/gif/arrow_sort_up.gif";
			}
			
		}
	}
}
function ClearCarline()
{
  ClearSelection(ddlCarlines);
  ClearModel();
}
function ClearModel()
{
  ClearSelection(ddlModels);
  ClearInterior();
  ClearExterior();
}
function ClearInterior()
{
  ClearSelection(cllInterior);
  SetClientChanged();
}
function ClearExterior()
{
  ClearSelection(cllExterior);
  SetClientChanged();
}
function ClearIncludePr()
{
  EmptyTable(grdExtraInclude, 0);
  SetPrChanged();
  SetClientChanged();
}
function ClearExcludePr()
{
  EmptyTable(grdExtraExclude, 0);
  SetPrChanged();
  SetClientChanged();
}
function IsTouran()
{
  return g_Type.substr(0,3) == "1T1";
}
function SetWildcard(CheckPos, TypePos, pos)
{
  var btnObj = document.getElementById(CheckPos);
  var txtObj = document.getElementById(TypePos);

  if (g_WildType.length >= pos)
  {
    var str = g_WildType.substr(pos-1,1);
    if (btnObj != null)
    {
			var isDetermined = $F(hddIsVehicleDetermined) == "1";
      SetDisabled(btnObj,(g_Type.length >= pos && (g_Type.substr(pos-1,1) == "*" && !isDetermined)));
      btnObj.checked = (str == "*");
    }

    if (txtObj != null)
    {
      txtObj.innerHTML = str;
    }
  }
  else if (btnObj != null)
  {
    btnObj.checked=false;
    SetDisabled(btnObj,true);
  }
}
function SetWildtext(oThis, txtType, pos)
{
  var txtObj = $(txtType);
	var oCtrl = $(oThis);
  
  if (txtObj != null && g_Type.length >= pos && g_WildType.length >= pos && oCtrl)
  {
    var str = "";
    if (oCtrl.checked)
    {
      str = "*";
    }
    else
    {
      str = g_Type.substr(pos-1,1);
    }
    g_WildType = g_WildType.substr(0,pos-1) + str + g_WildType.substr(pos);
    txtObj.innerHTML = str;
    var hddType = $(hddVehicleWildType);
    
    if (hddType != null)
    {
      hddType.value = g_WildType; //hddType.innerHTML = g_WildType;
    }
  }

  SetClientChanged();
}
function prepSelection(tblName)
{
	var tbl = document.getElementById(tblName);
	var tblAll = document.getElementById(grdAllPr);
	var i;
  var cnt = 0;
  if (tbl != null)
  {
	  for (var i = tbl.rows.length -1; i >= 0; i -=1)
	  {
		  var srcRow = tbl.rows[i];
		  
			if( srcRow.firstChild.nextSibling.innerHTML )
					srcRow.firstChild.title = srcRow.firstChild.nextSibling.innerHTML;
			else
					srcRow.firstChild.title = '';
			
			if( srcRow.firstChild.title )
				 srcRow.firstChild.nextSibling.title = srcRow.firstChild.title;
			else
					srcRow.firstChild.nextSibling.title = '';
          
	    if (tblAll != null)
	    {
		    for(var j = tblAll.rows.length -1; j >= 0; j -=1)
		    {
			    var chkRow = tblAll.rows[j];
			    if (chkRow.firstChild.innerHTML == srcRow.firstChild.innerHTML)
			    {
				    chkRow.firstChild.innerHTML = null;
				    chkRow.firstChild.nextSibling.innerHTML = null;
				    chkRow.lastChild.innerHTML = null;
				    tblAll.deleteRow(j);
				    break;		
			    }
		    }
		  }
		  
      if (!g_IsOriginType && srcRow.cells.length >=2)
      {
		    for(var k = g_EvaPrArr.length -1; k >= 0; k -=1)
			  {
				  if (srcRow.firstChild.innerHTML == g_EvaPrArr[k])
				  {
				    srcRow.firstChild.innerHTML = null;
				    srcRow.firstChild.nextSibling.innerHTML = null;
				    srcRow.lastChild.innerHTML = null;
				    tbl.deleteRow(i);
				    break;
				  }
			  }
      }
    }
    cnt = tbl.rows.length;
    SortEquipmentTable(tblName, SortDefault);
	}
	return cnt;
}
function PrepBackground(tblName)
{
	var tbl = document.getElementById(tblName);	
  if (tbl != null)
  {
    var isEva = (typeof(g_EvaType) != "undefined");
	  for (var i = 0; i < tbl.rows.length; i++)
	  {
		  var srcRow = tbl.rows[i];
		  if (srcRow.cells.length >= 3)
		  {
		    var isIntern = (srcRow.cells[2].innerHTML == "I");
		    
		    SetBackgroundClr(srcRow, isIntern? CLR_INT_INCLUDE : CLR_NOCOLOR);
		    
        if (!isIntern && isEva && g_IsOriginType)
        {
			    for(var index=0; index < g_EvaPrArr.length; index++)
			    {
				    if (srcRow.firstChild.innerHTML == g_EvaPrArr[index])
				    {
        		  SetBackgroundClr(srcRow, CLR_EVA_INCLUDE);
					    break;
				    }
		      }
        }
      }
    }
  }
}
function SetBackgroundClr(row, clr)
{
  if (row.firstChild && row.lastChild && row.firstChild.style)
  {
	  row.firstChild.style.backgroundColor = clr;
	  row.firstChild.nextSibling.style.backgroundColor = clr;
	  row.lastChild.style.backgroundColor = clr;
	}
}

function getSelectColor(row)
{
	var clr = CLR_SELECTED;
  if (row != null)
  {
	  switch (row.firstChild.style.backgroundColor.toUpperCase())
	  {
	  case CLR_EVA_INCLUDE_SELECTED:  
	  case CLR_EVA_INCLUDE: clr = CLR_EVA_INCLUDE_SELECTED; break;          
	  case CLR_EVA_EXCLUDE_SELECTED:  
	  case CLR_EVA_EXCLUDE:	clr = CLR_EVA_EXCLUDE_SELECTED; break;          
	  case CLR_INT_INCLUDE_SELECTED:  
	  case CLR_INT_INCLUDE: clr = CLR_INT_INCLUDE_SELECTED; break;          
	  }
  }
	return clr;
}

function getUnselectColor(row)
{
	var clr = CLR_NOCOLOR;
  if (row != null)
  {
	  switch (row.firstChild.style.backgroundColor.toUpperCase())
	  {
	  case CLR_EVA_INCLUDE: 
	  case CLR_EVA_INCLUDE_SELECTED: clr = CLR_EVA_INCLUDE; break;
	  case CLR_EVA_EXCLUDE:
	  case CLR_EVA_EXCLUDE_SELECTED: clr = CLR_EVA_EXCLUDE; break;
	  case CLR_INT_INCLUDE:
	  case CLR_INT_INCLUDE_SELECTED: clr = CLR_INT_INCLUDE; break;
	  }
  }
	return clr;
}

/////////////////////////////////////////////////////////////////////////////
function SortEquipmentTable(tblName, func)
{
	// now_debug_out ("SortEquipmentTable is disabled");
}

/////////////////////////////////////////////////////////////////////////////
function SortDefault(e1, e2)
{
	var e1EvaUpper = e1.col3.toUpperCase();
	var e2EvaUpper = e2.col3.toUpperCase();

	if (e1EvaUpper == e2EvaUpper)
	{
		return SortDescDown(e1, e2);
	}
	else if (e1EvaUpper > e2EvaUpper)
		return -1;
  else
		return 1;
}

function SortPrUp(e1, e2)
{
	return -SortPrDown(e1, e2);
}
function SortPrDown(e1, e2)
{
	var e1DescUpper = e1.col2.toUpperCase();
	var e2DescUpper = e2.col2.toUpperCase();
	var e1PrUpper = e1.col1.toUpperCase();
	var e2PrUpper = e2.col1.toUpperCase();

	if (e1PrUpper == e2PrUpper)
	{
		if (e1DescUpper == e2DescUpper)
			return 0;
	  else if (e1DescUpper < e2DescUpper)
			return -1;
		else
			return 1;
	}
  else if (e1PrUpper < e2PrUpper)
		return -1;
  else
		return 1;
}
function SortDescUp(e1, e2)
{
	return -SortDescDown(e1, e2);
}
function SortDescDown(e1, e2)
{
	var e1DescUpper = e1.col2.toUpperCase();
	var e2DescUpper = e2.col2.toUpperCase();
	var e1PrUpper = e1.col1.toUpperCase();
	var e2PrUpper = e2.col1.toUpperCase();

	if (e1DescUpper == e2DescUpper)
	{
		if (e1PrUpper == e2PrUpper)
			return 0;
	  else if (e1PrUpper < e2PrUpper)
			return -1;
		else
			return 1;
	}
  else if (e1DescUpper < e2DescUpper)
		return -1;
  else
		return 1;
}

function movePr(tblFromId,tblToId)
{
	// now_debug_out ("movePr: " + tblFromId + " to " + tblToId);
  SetClientChanged();
	carConfig_move_selected(tblFromId, tblToId);
  CheckPrOutput();
	return;
}
function getCount(tblId)
{
  var obj = document.getElementById(tblId);
	return obj == null ? 0: obj.rows.length;	
}
function moveRow(index, tblTo, tblFrom)
{
  var row = tblFrom.rows[index];
  if (row != null && tblTo != null && tblFrom != null)
  {
    InsertRow(tblTo, row, getUnselectColor(row))
	  tblFrom.deleteRow(index);	
  }
}
function InsertRow(tblTo, srcRow, clr)
{
	var row = tblTo.insertRow(tblTo.length);
	InsertCell(row, srcRow.firstChild.innerHTML, "ListGridColPr", clr);
	InsertCell(row, srcRow.firstChild.nextSibling.innerHTML, "ListGridColDesc", clr);
	InsertCell(row, srcRow.lastChild.innerHTML, "ListGridColState", clr);
	
	row.selected = false;
}
function InsertCell(row, colText, style, clr)
{
	var td = document.createElement("td");
  td.style.backgroundColor=clr;
	var tx = document.createTextNode(colText);
	row.selected = false;
  td.className=style;
	td.appendChild(tx);
	row.appendChild(td);
}
function moveAll(tblFromId,tblToId)
{
  var oTable = $(tblFromId);
	oTable.select("tr").invoke("addClassName", "selected");
	carConfig_move_selected(oTable,tblToId);
	// SortEquipmentTable(tblName, func);
	CheckPrOutput();
}

function SetClientChanged()
{
  if (typeof(ClearClientError) == "function")
  {
    ClearClientError();
  }
  if (typeof(SetOptionChangedFct) == "function")
  {
   // SetOptionChangedFct(g_Type.substr(0,3) == "1T1");
  }
}
function SetChangedInclude(input)
{
  SetClientChanged();
  var isOk = false;
  var incCount = 0;
  
  var error = "";
  if (grdInclude != null)
  {

	  if (getCount(grdInclude) + incCount >= g_maxPR)
	  {
      if (input != null)
      {
        var inpObj = document.getElementById(input);
        if (inpObj != null)
        {
          inpObj.value = "";
        }
      }
	    error = 'EUCCO#E_NO_MORE_PR_INC';
	  }
	  else
	  {
	    isOk = true;
	  }
  }

	return isOk;
}
function SetChangedExclude(input)
{
  SetClientChanged();
  var isOk = false;
  var excCount = 0;
  
  var error = "";
  if (grdExclude != null)
  {
	  if (getCount(grdExclude) + excCount >= g_maxPR)
	  {
      if (input != null)
      {
        var inpObj = $(input);
        if (inpObj != null)
        {
          inpObj.value = "";
        }
      }
	    error = 'EUCCO#E_NO_MORE_PR_EXC';
	  }
	  else
	  {
	    isOk = true;
	  }
  }

	return isOk;
}
function CheckPr(inputField, input, count)
{
  if (count >= 0)
  {
    var inp = document.getElementById(inputField);
    if (inp != null && inp.value != "")
    {
      if (input != inputField)
      {
        count++;
      }
      if (inp.value.length < 3)
      {
	      count = -1;
      }
    }
  }
  return count;
}

function CarConfigGetPrOutputList(tabId)
{
	var lst = "";
	$$("#" + tabId + " tr").each(function(el)
	{
		if ("" != lst)
		{
			lst += ",";	
		}
		lst += el.down("td:first-child").innerHTML;
		lst += "-";	
		lst += el.down("td:last-child").innerHTML;
	})

	return lst;
}

function CheckPrOutput()
{
	
  var incList =$(hddInclude);
  if (IsDefined (incList))
	{
		incList.value = CarConfigGetPrOutputList(grdInclude);
		// now_debug_out("incList: " + incList.value);	
	}
	
  var excList =$(hddExclude);
  if (IsDefined (excList))
	{
		excList.value = CarConfigGetPrOutputList(grdExclude);
		// now_debug_out("excList: " + excList.value);	
	}
	return;






  var tblInc=document.getElementById(grdInclude);
  var lstExc=document.getElementById(hddExclude);
  var tblExc=document.getElementById(grdExclude);

  if( lstInc != null && tblInc != null )
  {

    lstInc.value = "";
    
    for( var i = 0; i < tblInc.rows.length; i++ )
    {
      if( i > 0 )
      {
          lstInc.value +=",";
      }

      if( tblInc.rows[i].firstChild.innerHTML )
      {
          lstInc.value += tblInc.rows[i].firstChild.innerHTML;
          lstInc.value += "-" + tblInc.rows[i].lastChild.innerHTML;
      }   
    }
  }

  if (lstExc != null && tblExc != null)
  {
    lstExc.value="";
    
    for(var i=0;i<tblExc.rows.length;i++)
    {
      if( i > 0 )
      {
          lstExc.value +=",";
      }
      
      if( tblExc.rows[i].firstChild.innerHTML )
      {
          lstExc.value += tblExc.rows[i].firstChild.innerHTML;
          lstExc.value += "-"+tblExc.rows[i].lastChild.innerHTML;
      }
    }
  }
	
	now_debug_out("inc: " + lstInc.value);
	now_debug_out("exc: " + lstExc.value);
	
}


function PrepareCarSelectPostData()
{
}
function PrepareSelectClick()
{
  PrepareServerClick();
}
function SetPrChanged(oThis)
{
  if( "function" == typeof(SetPrChanged_OrderCheck_CarConfig) )
  {
    return SetPrChanged_OrderCheck_CarConfig(oThis);
  }
  PrepareGridData(hddExtraInclude, grdExtraInclude);
  PrepareGridData(hddExtraExclude, grdExtraExclude);
}


function PrepareGridData(hdd, grd)
{
  var oStore = $(hdd);
	if (IsDefined(oStore))
	{
		var lst = "";
		$$("#" + grd + " input[type=text]").each(function(el)
		{
			if ("" != lst)
      {
		    lst +=",";
	    }
	    lst += el.value;
		})
		oStore.value = lst;
	}
}

function OnFocusPr(oThis, moreBtn) 
{
	now_debug_out ("special_CarConfig OnFocusPr: handle AddNewGridLine obsolete or generic???");

  if( "function" == typeof(OnFocusPr_OrderCheck_CarConfig) )
      return OnFocusPr_OrderCheck_CarConfig(oThis, moreBtn);
    
  var input = $(oThis);
  if (input)
  {
		var tdParent = input.parentElement; // TD
		if (tdParent != null)
		{
   		var parent = tdParent.parentElement; // TR
      if (parent != null && parent.nextSibling == null && parent.lastChild == tdParent)
      {
        parent = parent.parentElement; // TBODY
        if (parent != null)
        {
          parent = parent.parentElement; // TABLE
          if (parent != null && parent.tagName == "TABLE")
          {
            var allDiv=document.getElementById(divAllPr);
            var tbl=document.getElementById("tblOuter");
            if (allDiv != null && tbl != null)
            {
              var oldOffset = tbl.parentElement.offsetHeight;

              AddNewGridLine(parent.id,0,12,moreBtn);
              var offset = tbl.parentElement.offsetHeight - oldOffset;
              allDiv.style.height = allDiv.offsetHeight + offset;
            }
          }
        }
      }
    }
  }
}
function AddNewPrLine(moreBtn) 
{
	// now_debug_out ("special_CarConfig AddNewPrLine: handle AddNewGridLine obsolete or generic???");
  if( "function" == typeof(AddNewGridLine) )
  {
		AddNewGridLine(grdExtraInclude,0,12,moreBtn);
    return false;
  }
}

function SetBrandChanged()
{
  ClearSelection(ddlCarlines);
}
function SetCarlineChanged()
{
  ClearSelection(ddlModels);
}

function CarCfgHandleExtraInc(oThis)
{
  // now_debug_out ("handleExtraInc...");
  SetPrChanged(oThis);
  PrepareGridData(hddExtraInclude, grdExtraInclude);
}

function OnSort(tbl, btn, btnOld, content)
{
	if (btnOld != null && btnOld.firstChild != null)
	{
		btnOld.firstChild.src = "shared/gif/empty.gif";
	}

	if (btn != null && btn.firstChild != null)
	{
		if (btn.firstChild.src.lastIndexOf("arrow_sort_up.gif") < 0)
		{
			btn.firstChild.src ="shared/gif/arrow_sort_up.gif";
			SortEquipmentTable(tbl, content == "Pr" ? SortPrDown : SortDescDown);
		}
		else
		{
			btn.firstChild.src ="shared/gif/arrow_sort_down.gif";
			SortEquipmentTable(tbl, content == "Pr" ? SortPrUp : SortDescUp);
		}
	}
}
