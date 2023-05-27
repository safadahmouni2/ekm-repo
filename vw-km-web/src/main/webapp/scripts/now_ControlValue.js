
// ToDo: ggf. erweitern --> CheckBox usw.

function GetControlValue(oControl)
{
   var xValue;   // Wird String oder Array
   
   if( oControl == null )
      now_debug_out( "GetControlValue(): Control nicht vorhanden!" )
   else
   if( oControl.type == 'radio' )
      xValue = oControl.checked;
   else
      xValue = oControl.getValue();

   return xValue;
}

function GetControlValueById(sControl)
{
   var xValue;   // Wird String oder Array
   var oControl = $(sControl);
   
   if( oControl == null )
       now_debug_out( "GetControlValue(): Control " + sControl + " nicht vorhanden!" )
   else
   if( oControl.type == 'radio' )
       xValue = oControl.checked;
   else
       xValue = oControl.getValue();

   return xValue;
}