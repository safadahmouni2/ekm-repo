function btnEvaExportClientClick () 
{
  return true;
}

function btnBackClientClick () 
{
  return true;
}

function PopUp(CustSessId, Url)
{
  // local_alert ("function PopUp is obsolet\n use class=dragWindow");
  Url = Url + "?vwcstat=" + CustSessId;
  window.open(Url,'','100%','100%');
}

