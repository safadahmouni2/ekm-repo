
function OS_SelectRow(osControl, nRow)
{
    var setSelectedValue1 = document.getElementById('_ctl0_selectedRow_ASP');
    var setSelectedValue2 = document.getElementById('selectedRow_ASP');
    var setSelectedValue3 = document.getElementById('selectedRow');
    
    if( setSelectedValue1 != null )
        setSelectedValue1.value = nRow;

    if( setSelectedValue2 != null )
        setSelectedValue2.value = nRow;

    if( setSelectedValue3 != null )
        setSelectedValue3.value = nRow;
        
}

function CommisssionId_Click(osControl, nRow)
{
    var oRow = $(osControl).up( 'tr' );

    oRow.onclick = null;    
    
    OS_SelectRow(osControl, nRow);

  __doPostBack( osControl.id, nRow );
  
  
}

function Init_OS_ViewRowsByBrands() 
{
    if( typeof(selectedBrand) != 'undefined' )
        OS_ViewRowsByBrands(selectedBrand);
    
    if( typeof(hddBrandId) != 'undefined' && typeof(ddlBrandId) != 'undefined' )
      $(hddBrandId).value = $(ddlBrandId).value;
}

function OS_ViewRowsByBrands(xBrand) 
{
    var sValue = '';


    if( typeof( xBrand ) == 'object' )
        sValue = xBrand.value
    else
        sValue = xBrand;

        
    if( $(hddSelectedBrand) != null )
        $(hddSelectedBrand).value = sValue;

}