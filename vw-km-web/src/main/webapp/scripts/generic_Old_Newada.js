
function set_background_color_class_legacy(oCtrl, cls)
{
	if ($(oCtrl))
	{
	  removeClassNames ($(oCtrl), "CLR_ODD CLR_EVEN Color_Selected CLR_HILITE COLNOREQ COLREQU");
	  $(oCtrl).addClassName (cls);
	}

}







function EnableObjectId(parent, enable, noCursorHandling, strongHandling)
{
  var parentCtrl=$(parent);
  if (parentCtrl != null)
  {
    EnableObject(parentCtrl, enable, noCursorHandling, strongHandling);
  }
}
function EnableObject(parent, enable, noCursorHandling, strongHandling)
{
  var name = parent.tagName;
	var type = parent.type;
  if (name == "INPUT" || name == "SELECT" || 
      strongHandling && 
      (name == "DIV" || name == "TABLE" || name == "TR" || name == "TD" || name == "IMG"))
  {
    if (typeof(type) == "undefined" || type != "hidden") 
    {
			SetDisabled(parent,!enable);
    }
  }
}

function onContextMenu()
{
	event.returnValue=false;
}

function PrepareServerClick()
{
  if (typeof(hddCommand) != "undefined")
  {
      var obj = $(hddCommand);
    if (obj != null)
    {
      obj.value="BtnClick";
    }
  }
  return true
}
function AddRefId(id, refId)
{
  clientRef[id]=refId;
}
function ClearClientError()
{
}
function UniqueId()
{
  var ID = new Date();
  return ID.getTime();
}
function OpenWindow(Win)
{
  var newWindow = window.open(Win);
  if (newWindow != null)
  {
    var wait=0;
    while(newWindow.closed == true && wait < 50)
    {
      wait++;
      setTimeout("", 100);
    }
    newWindow.focus();
  }
}

function SetGridLine(grid, parent, selectedIndex)
{
  SetGridLineDirect($(grid), parent, selectedIndex);
}
function SetGridLineDirect(tbl, parent, selectedIndex)
{
  var selected = false;
  if (tbl != null)
  {
	  if (parent != null && parent.tagName=="TR")
	  {
	    var rowIndex = parent.rowIndex -1;
      var indexCtrl = $(selectedIndex);
      var actRow = parent.firstChild;
      // $(parent).removeClassName("selected");
	    if (rowIndex >= 0 && indexCtrl != null && actRow != null && actRow.firstChild != null)
	    {
 	      indexCtrl.value=parseInt(rowIndex +1);
        input = actRow.firstChild;
        if (input != null && input.tagName == "INPUT")
        {
          selected = true;
	        $(parent).addClassName("selected");
          input.checked = true;
        } 
      }
    }
  }
  return selected;
}

function SelectGridLineStd(grid)
{
  SelectGridLineStdDirect($(grid));
}
function SelectGridLineStdDirect(tbl)
{
	// now_debug_out ("SelectGridLineStdDirect is obsolete; use table.zebra");
}
function SelectGridLineForm(grid)
{
  var obj = window.event.srcElement;
  if (obj != null && obj.tagName=="INPUT")
  {
    obj=obj.parentElement;
  }
  var parent = obj.parentElement;
	var tbl = $(grid);	
  if (tbl != null)
  {
	  SetGridLineForm(tbl, parent);
  }
}


function SelectAssumeButton(grid, assume, startCol)
{
  var length = grid.rows.length;
  var btn = $(assume);
  if (btn != null)
  {
    if (length - startCol == 1)
    {
	    SetGridLineForm(grid, grid.rows[1]);
    }
    if (length - startCol > 1)
    {
      if (GetSelectedGridLine(grid) < 0)
      {
        btn.cursor = 'pointer';
        SetDisabled(btn,true);
      }
      else
      {
        SetDisabled(btn,false);
        btn.cursor = 'hand';
      }
    }
  }
}
function SetGridLineForm(grid, parent)
{
	if (parent != null && parent.tagName=="TR")
	{
	  var rowIndex = parent.rowIndex -1;
	  if (rowIndex >= 0)
	  {

	    $(parent).addClassName("selected");
			
      var obj = parent.firstChild;
      if (obj != null)
      {
        obj = obj.firstChild;
        if (obj != null)
        {
          obj.checked = true;
        }
      }          
    }
  }
}
function GetSelectedGridLine(grid)
{
  var selectedIndex = -1;
	if (grid != null)
	{
		for (var rowIndex = grid.rows.length -1; rowIndex >= 0; rowIndex -=1)
		{
			var parent = grid.rows[rowIndex].cells[0];
			if (parent != null && parent.tagName == "TD")
			{
        var radio = parent.firstChild;
        if (radio != null && radio.tagName == "INPUT" && radio.type == "radio" && radio.checked)
        {
          selectedIndex = rowIndex;
          break;
				}
			}
		}
	}
  return selectedIndex;
}

function AddNewGridLine(tblName, startIndex, maxLength, btnCopy)
{
	AddNewGridLineDirect($(tblName), startIndex, maxLength, btnCopy);
}
function AddNewGridLineDirect(tbl, startIndex, maxLength, btnCopy)
{
	btnCopy = $(btnCopy);
	if (tbl != null && tbl.rows.length < maxLength + startIndex)
	{
		var row = tbl.insertRow(-1);

		if (row != null && tbl.rows.length > startIndex)
		{
			var firstRow = tbl.rows[startIndex];
			for(var index = 0; index < firstRow.cells.length; index++)
			{
				var cell = firstRow.cells[index];
				
				if (cell != null && cell.tagName == "TD")
				{
					var td = document.createElement("TD");
					if (td != null)
					{
						td.className = cell.className;
            td.title = cell.title;
						row.selected = false;
						row.appendChild(td);
						for (var childIndex = 0; childIndex < cell.children.length; childIndex++)
						{
							var source = cell.children[childIndex];
							var dest = document.createElement(source.tagName);
							td.appendChild(dest);

							var type = dest.type;

							switch(dest.tagName)
							{
								case "INPUT":
									switch (type)
									{
										case "radio":
											break;
											
										case "text":
										case "hidden":
											dest.maxlength = source.maxlength;
											dest.size = source.size;
											// SetValue (dest, GetValue (source));
											SetValue (dest, "");
											dest.className = source.className;
											dest.onchange = source.onchange;
											dest.onkeypress = source.onkeypress;
											dest.onkeydown = source.onkeydown;
											dest.onkeyup = source.onkeyup;
                      dest.onblur = source.onblur;
                      dest.onfocus = source.onfocus;
                      dest.title = source.title;
											dest.id = UniqueId() + "_" + String(index);
											break;
											
										default:
											break;
									}
									break;
									
								case "SELECT":
									dest.className = source.className;
                  dest.onblur = source.onblur;
                  dest.onfocus = source.onfocus;
                  dest.onchange = source.onchange;
									dest.id = UniqueId() + "_" + String(index);
									for (var childIndex = 0; childIndex < source.children.length; childIndex++)
									{
										var sub = source.children[childIndex];
										
										if (sub.tagName == "OPTION")
										{
											var option = document.createElement("OPTION");
											dest.appendChild(option);
											option.className = sub.className;
											// var tx = document.createTextNode(sub.innerText);
											var tx = document.createTextNode(GetValue(sub));
											option.appendChild(tx);
											option.value = sub.value;
										}
									}
									break;
							}
						}
					}
				}
			}
		}
	}
	
	var disabled = tbl.rows.length >= maxLength + startIndex && btnCopy != null;
	enableBtn(btnCopy, !disabled)
	
}

function GetGridDataList (tblName, tagName, separator)
{
	var outList = "";
	var val = "";
	var tbl = $(tblName);
	if (tbl)
	{
	  tbl.select(tagName).each(function(el)
		{
			switch(el.tagName.toUpperCase())
			{
				case "INPUT":
					switch (el.type.toLowerCase())
					{
						case "radio":
							break;
							
						case "text":
						case "hidden":
							if ("" != el.value)
							{
								if ("" != outList)
								{
									outList += separator;
								}
								outList += el.value;
							}
							break;
							
						case "checkbox":
							if ("" != outList)
							{
								outList += separator;
							}
							outList += el.checked ? "1": "0";
							break;
																			
						default:
							break;
					}
					break;
					
				case "SELECT":
					if ("" != el.value)
					{
						if ("" != outList)
						{
							outList += separator;
						}
						outList += $F(el);
					}
					break;
			}
		})
	}
  return outList;
}


function GetGridData(tblName, tagName, elemIndex, hddOutput)
{
	return GetGridDataSep(tblName, tagName, elemIndex, hddOutput, ",");
}

function GetGridDataSep(tblName, tagName, elemIndex, hddOutput, separator)
{
	var outCtrl = $(hddOutput);
	if ($(tblName) && outCtrl)
	{
		outCtrl.value = GetGridDataList (tblName, tagName, separator);
	}
}

function GetCellData(tblName, tagName, colIndex, rowIndex)
{
	return GetCellDataDirect($(tblName), tagName, colIndex, rowIndex);
}
function GetCellDataDirect(tbl, tagName, colIndex, rowIndex)
{
	var value = "";
	if (tbl != null && rowIndex < tbl.rows.length && colIndex < tbl.rows[rowIndex].cells.length)
	{
		var cell = tbl.rows[rowIndex].cells[colIndex];
		if (cell != null && cell.tagName == "TD" && cell.children)
		{
      if (cell.children.length == 0)
      {
        value = GetValue(cell);
      }
      else
      {
			  var source = cell.children[0];
			  if (source.tagName == tagName)
			  {
				  switch(source.tagName)
				  {
					  case "INPUT":
						  switch (source.type)
						  {
							  case "radio":
								  break;
  								
							  case "text":
							  case "hidden":
								  value = source.value;
								  break;
  								
							  default:
								  break;
						  }
						  break;
  						
					  case "SELECT":
						  value = source.value;
						  break;
				  }
        }
			}
		}
	}
  return value;
}
function GetCellControl(tblName, colIndex, rowIndex)
{
	var value = "";
	var tbl = $(tblName);

	if (tbl != null && rowIndex < tbl.rows.length && colIndex < tbl.rows[rowIndex].cells.length)
	{
		var cell = tbl.rows[rowIndex].cells[colIndex];
		if (cell != null && cell.tagName == "TD" && cell.children)
		{
      if (cell.children.length == 0)
      {
        value = cell;
      }
      else
      {
			  value = cell.children[0];
			  if (value.firstChild != null)
			  {
					value = value.firstChild;
			  }
			}
		}
	}
  return value;
}
function DisplayCell(tblName, colIndex, startRow, enabled)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var rowIndex = startRow; rowIndex < tbl.rows.length; rowIndex++)
		{
			var cell = tbl.rows[rowIndex].cells[colIndex];
			if (cell != null && cell.tagName == "TD")
			{
			    //$(cell).toggleClassName('hidden');
			  cell.style.display = enabled ? "block" : "none";
			}
		}
	}
}
//Untestet
function EnableCell(tblName, colIndex, startRow, enabled)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var rowIndex = startRow; rowIndex < tbl.rows.length; rowIndex++)
		{
			var cell = tbl.rows[rowIndex].cells[colIndex];
			if (cell != null && cell.tagName == "TD")
			{
			  if (cell.children.length > 0)
			  {
				  for (var childIndex = 0; childIndex < cell.children.length; childIndex++)
				  {
					  var source = cell.children[childIndex];
            if (source != null)
            {
              SetDisabled(source,!enabled);
            }
				  }
				}
				else
				{
				  SetDisabled(cell,!enabled);
				}
			}
		}
	}
}
function PrepareGridIds(tblName)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var rowIndex = 0; rowIndex < tbl.rows.length; rowIndex++)
		{
			for(var colIndex = 0; colIndex < tbl.rows[rowIndex].cells.length; colIndex++)
			{
				var cell = tbl.rows[rowIndex].cells[colIndex];
				if (cell != null && cell.tagName == "TD" &&  cell.children)
				{
					for (var childIndex = 0; childIndex < cell.children.length; childIndex++)
					{
						var source = cell.children[childIndex];
						switch(source.tagName)
						{
							case "INPUT":
								switch (source.type)
								{
									case "radio":
										break;
										
									case "text":
									case "hidden":
										source.id = UniqueId() + "_" + String(colIndex) + "_" + String(rowIndex) + 
                                "_" + String(childIndex);
										break;
										
									default:
										break;
								}
								break;
								
							case "SELECT":
							  source.id = UniqueId() + "_" + String(colIndex) + "_" + String(rowIndex) + 
                            "_" + String(childIndex);
								break;
						}
					}
				}
			}
		}
	}
}
function ClearGridLine(tblName, rowIndex)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var colIndex = 0; colIndex < tbl.rows[rowIndex].cells.length; colIndex++)
		{
			var cell = tbl.rows[rowIndex].cells[colIndex];
			if (cell != null && cell.tagName == "TD")
			{
				SetValue (cell, "");
				for (var childIndex = 0; childIndex < cell.children.length; childIndex++)
				{
					var source = cell.children[childIndex];
					switch(source.tagName)
					{
						case "INPUT":
							switch (source.type)
							{
								case "radio":
									break;
									
								case "text":
								case "hidden":
									source.value = null;
									break;
									
								default:
									break;
							}
							break;
							
						case "SELECT":
							source.selectedIndex = 0;
							break;
					}
					
				}
			}
		}
	}
}
function EmptyGridLine(tblName, rowIndex, startCol)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var colIndex = startCol; colIndex < tbl.rows[rowIndex].cells.length; colIndex++)
		{
			var cell = tbl.rows[rowIndex].cells[colIndex];
			if (cell != null && cell.tagName == "TD")
			{
        if (cell.children)
        {
          if (cell.children.length == 0)
          {
				    SetValue (cell, " ");
          }
				  for (var childIndex = 0; childIndex < cell.children.length; childIndex++)
				  {
					  var source = cell.children[childIndex];
					  switch(source.tagName)
					  {
						  case "INPUT":
							  switch (source.type)
							  {
								  case "radio":
									  break;
  									
								  case "text":
								  case "hidden":
									  source.value = "";
									  break;
  									
								  default:
									  break;
							  }
							  break;
  							
						  case "SELECT":
							  source.selectedIndex = 0;
							  break;
					  }
				  }
				}
			}
		}
	}
}

function EmptyTable(tblName, startCol)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var rowIndex = 0; rowIndex < tbl.rows.length; rowIndex++)
    {
      EmptyGridLine(tblName, rowIndex, startCol);
    }
  }
}
function ClearGridCol(tblName, colIndex)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for(var rowIndex = 0; rowIndex < tbl.rows.length; rowIndex++)
		{
			var cell = tbl.rows[rowIndex].cells[colIndex];
			if (cell != null && cell.tagName == "TD")
			{
				SetValue (cell, "");
				for (var childIndex = 0; childIndex < cell.children.length; childIndex++)
				{
					var source = cell.children[childIndex];
					switch(source.tagName)
					{
						case "INPUT":
							switch (source.type)
							{
								case "radio":
									break;
									
								case "text":
								case "hidden":
									source.value = null;
									break;
									
								default:
									break;
							}
							break;
							
						case "SELECT":
							source.selectedIndex = 0;
							break;
					}
				}
			}
		}
	}
}

function SetGridTitlesFromCell(tblName, startRow)
{
	var tbl = $(tblName);
	if (tbl != null)
	{
		for (var rowIndex = tbl.rows.length -1; rowIndex >= startRow; rowIndex -=1)
		{
			for (var colIndex = 0; colIndex < tbl.rows[rowIndex].cells.length; colIndex++)
			{
				var cell = tbl.rows[rowIndex].cells[colIndex];
				if  (cell != null && cell.tagName == "TD" 
				  && 
            (SetValue (cell, GetValue(cell).replace(" ", "")) != "" && !(cell.firstChild != null && cell.firstChild.tagName == "SELECT"))
           )
				{
			    cell.title = GetValue (cell);
				}
			}
		}
	}
}
function ClearInputByName(name)
{
	var ctrl = $(name);
	if (ctrl != null)
	{
		ctrl.value = "";
	}
}
function ClearTextByName(name)
{
  if (typeof(name) != "undefined")
  {
	  var ctrl = $(name);
	  if (ctrl != null)
	  {
		  SetValue (ctrl, "");
	  }
  }
}
function RemoveTableByName(name)
{
	var tbl = $(name);
	if (tbl != null)
	{
		for (var rowIndex = tbl.rows.length -1; rowIndex >= 0; rowIndex -=1)
		{
			for(var colIndex = 0; colIndex < tbl.rows[rowIndex].cells.length; colIndex++)
			{
				var cell = tbl.rows[rowIndex].cells[colIndex];
				if (cell != null && cell.tagName == "TD")
				{
					SetValue (cell, "");
				}
			}
			tbl.deleteRow(rowIndex);
		}
	}
}
function ClearTableByName(name, startCol)
{
  ClearTableByNameColRow(name, startCol, 0);
}
function ClearTableByNameColRow(name, startCol, startRow)
{
	var tbl = $(name);
	if (tbl != null)
	{
		for (var rowIndex = tbl.rows.length -1; rowIndex >= startRow; rowIndex -=1)
		{
			for(var colIndex = startCol; colIndex < tbl.rows[rowIndex].cells.length; colIndex++)
			{
				var cell = tbl.rows[rowIndex].cells[colIndex];
				if (cell != null && cell.tagName == "TD")
				{
					SetValue (cell, "");
				}
			}
		}
	}
}

function SaveChangedSelect(oThis, saveInput)
{
	var ctrl = $(oThis);
	var input = $(saveInput);

	if (ctrl != null && input != null && typeof(ctrl) == "object")
	{
		input.value = ctrl.selectedIndex.toString();
	}
}
function SaveChangedSelectValue(oThis, saveInput)
{
	var ctrl = $(oThis);
	var input = $(saveInput);

	if (ctrl != null && input != null)
	{
		input.value = ctrl.value;
	}
}

function SaveIndex(DdList, HddIndex)
{
	var list = $(DdList);
	var index = $(HddIndex);

	if (list != null && index != null)
	{
		index.value = list.selectedIndex.toString();
	}
}
function SaveValue(DdList, HddValue)
{
	var list = $(DdList);
	var val = $(HddValue);

	if (list != null && val != null)
	{
		val.value = list.value;
	}
}
function RestoreIndex(DdList, HddIndex)
{
	var list = $(DdList);
	var index = $(HddIndex);

	if (list != null && index != null)
	{
		list.selectedIndex = parseInt(index.value);
	}
}
function RestoreValue(DdList, HddValue)
{
	var list = $(DdList);
	var val = $(HddValue);

	if (list != null && val != null)
	{
		list.value = val.value;
	}
}

function SubmitBtnEnter(btn)
{
  if (Prototype.Browser.IE) 
  {
    if (event.keyCode == 13) 
    {
	    PrepareServerClick();
      var btnObj = $(btn);
      if (btnObj != null)
      {
        window.event.returnValue = false;
        btnObj.fireEvent('onclick');
      }
    }
  }
}

function ClearDropDownList(ddl, hddIndex)
{
  var ddlType = $(ddl);
  var ddlIndex = $(hddIndex);
  if (ddlType != null && ddlIndex != null)
  {
    ddlType.selectedIndex = 0;
    ddlIndex.value = "0";
  }
}

function IsChecked(ctrl)
{
  var inp = $(ctrl);
  return inp != null && inp.checked == true;
}

function SetStyle(ctrl, entry, value)
{
  var tag = $(ctrl);
  if (tag != null && tag.style != null)
  {
		now_debug_out ("SetStyle: tagName=" + tag.tagName + "style["+entry+"]=("+value+")");
    tag.style[entry] = value;
  }
}
function SetTitle(ctrl, value)
{
  var tag = $(ctrl);
  if (tag != null && tag.title != null)
  {
    tag.title = value;
  }
}

function ClearSelection(ddl)
{
  var ddlType = $(ddl);
  if (ddlType != null)
  {
    ddlType.selectedIndex = 0;
	}
}

var toolTipCtrl = null;
var clientX = 0;
var clientY = 0;


function HandleLastClick(input, index, lineCount, btnCtrl)
{
	now_debug_out ("HandleLastClick: handle AddNewGridLine obsolete or generic???");

  if (input != null && input.tagName == "INPUT")
  {
    var len = input.value.length;
    if (len == input.maxlength)
    {    
		  var parent = input.parentElement; // TD
		  if (parent != null)
		  {
   		  var parent = parent.parentElement; // TR
        if (parent != null)
        {
          if (parent.nextSibling == null)
          {
            AddNewGridLineDirect(parent.parentElement, index, lineCount, btnCtrl);
          }
          
          if (!Prototype.Browser.IE || event.keyCode != 9)
          {
            if (parent.nextSibling != null && parent.nextSibling.firstChild != null)
            {
              var inp = parent.nextSibling.firstChild.firstChild;
              if (inp != null)
              {
                SetFocus(inp);
              }
            }
          }
        }
      }
    }
  }
}
