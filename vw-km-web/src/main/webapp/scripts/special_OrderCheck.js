
function special_OrderCheck(sValue)
{
   var xLabel = document.getElementById( lbWeek );
   var xText1 = document.getElementById( txtWeek_WLW );
   var xText2 = document.getElementById( txtWeek_WW );
   
   if( xLabel != null )
   {
  
      if( sValue.toLowerCase() == 'true' )
      {
         xLabel.innerText = xText1.value;
      }
      else
      {
         xLabel.innerText = xText2.value;
      }
         
   }
}