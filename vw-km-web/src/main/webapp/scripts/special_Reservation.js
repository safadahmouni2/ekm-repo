
function special_ReservationContact(data)
{
  var aData     = data.split( '#;#' );
  var aKeyValue = new Array();
  var oControl  = null;
 
  for(var i = 0; i < aData.length; i++ )
  {
    aKeyValue          = aData[i].split( '=' );
    oControl           = $(aKeyValue[0]);
    oControl.innerHTML = aKeyValue[1];
  }

}
