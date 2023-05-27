
var PriceSelector = '';


function syncGrossNetPrice(oThis)
{
  var cur = $F($(oThis));
  if (!cur || "" == cur)
  {
    cur = "net";
  }
  cur = cur.toLowerCase();
  removeBodyClasses("gross net");
  addBodyClasses(cur);
  
  PriceSelector = cur;


	if (IsBrowserVersion("IE_6-7"))
	{
		$$('div.carCheckEdit td.' + cur).invoke("toggleClassName", "msieDummy"); // forces MSIE redraw / repaint / render
	}	
	
  return true;
}

function generic_new_main_functions_init ()
{
  // VehicleDetails: Initiale Einstellung der Brutto- Nettopreis- Anzeige anhand des Wertes in der Select-Box
  $$("select.B_N_selector").each(function(selCtrl)
  {
		syncGrossNetPrice (selCtrl);
		selCtrl.onchange = function()
    {
		  return syncGrossNetPrice (this);
		}
  })
}