
function naviManagementBodyClass (cls)
{
  removeBodyClasses ("navMngMove navMngRename navMngOffline navMngAuthorisation");
  addBodyClasses (cls);
  return false;
}

function naviManagementGetRenameForm (oThis)
{
  alert ("rename " + $(oThis).innerHTML);
}
function naviManagementInitMove ()
{
  Sortable.create($$('div.naviManagement').first().identify(), {tree:true,scroll:window});
  return naviManagementBodyClass ("navMngMove");
}

function naviManagementInitAuthorisation ()
{
  return naviManagementBodyClass ("navMngAuthorisation");
}
 
function naviManagementInitRename ()
{
  $$('div.naviManagement a').each(function(el)
  {
    el.onclick = function()
    {
      if (hasBodyClass ("navMngRename"))
      {
        naviManagementGetRenameForm (this);
        
      }
      return false;
    }
  })
  return naviManagementBodyClass ("navMngRename");
}

function naviManagementInitOffline ()
{
  $$('div.naviManagement a').each(function(el)
  {
    el.onclick = function()
    {
      if (hasBodyClass ("navMngOffline"))
      {
        $(this).up("li").toggleClassName("offline");
      }
      return false;
    }
  })
  return naviManagementBodyClass ("navMngOffline");
}

function SpecialPageBehaviour ()
{
}