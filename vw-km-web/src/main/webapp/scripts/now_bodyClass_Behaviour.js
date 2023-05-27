
function BodyClass_Behaviour_Init()
{
	$$('#areaFooter li.pullup ul li a.bodyClass').each(function(lnk)
	// $$('#areaFooter li.pullup ul li input[type=radio], #areaFooter li.pullup ul li input[type=checkbox]]').each(function(inp)
  {
    var li = lnk.up("li");
    var inp = li.down("input");
		lnk.onclick = function()
    {
      return behaviourBodyClassClick(this);
		}
		inp.onclick = function()
    {
      behaviourBodyClassClick(this);
      return true;
		}
  })
}

// =================================================================
function fnSyncCheckBoxByBodyClass (idCheckBox, bdyClassName)
{
  if ($(idCheckBox))
  {
    $(idCheckBox).checked = hasClassNames (getBody(), bdyClassName);
  }
  return false;
}

// =================================================================
function fnToggleClassName (oObj, clsName)
{
  var retVal = false;
  if (oObj)
  {
    if (hasClassNames (oObj, clsName))
    {
      removeBodyClasses (clsName);
    }
    else
    {
      addBodyClasses (clsName)
      retVal = true;
    }
  }
  return retVal;
}

// =================================================================
function fnToggleClassNameSync (oObj, clsName, idCheckBox)
{
  if (oObj)
  {
    fnToggleClassName (oObj, clsName);
    fnSyncCheckBoxByBodyClass (idCheckBox, clsName);
  }
  return false;
}

// =================================================================
function getBodyClassName (oThis)
{
  var clsName = oThis.className.replace('bodyClass','');
  // clsName = clsName.replace(' ','');
  return clsName.trim();
}

// =================================================================
function behaviourBodyClassClick (oThis)
{
  var li = oThis.up("li");
  var input = li.down("input");
  if ("radio" == input.type)
  {
    var inpName = input.name;
    var ul = li.up("ul");
    ul.select("li input[name='"+inpName+"']").each(function (inp)
    {
      var bdyClsName = getBodyClassName (inp.next("a"));
      removeBodyClasses (bdyClsName);
    })
  }
  var id = input.identify();
  var clsName = getBodyClassName (li.down("a"));
  return fnToggleClassNameSync (getBody(), clsName, id);
}
