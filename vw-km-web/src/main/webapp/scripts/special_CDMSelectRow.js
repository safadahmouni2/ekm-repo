var sCDMSelected     = '';
var sCDMSelectedById = '';

function SelectRow(sRow) 
{
    CDMResetSelection('trid' + sRow);

    sCDMSelected    = 'trid' + sRow;

    if( $(sCDMSelected) != null ) 
        $(sCDMSelected).addClassName( 'selected' );

}

function CDMResetSelection(sRow) 
{

    if ( $(sCDMSelected) != null )
    {
         $(sCDMSelected).removeClassName('selected');
    }
    else
    {
        if( $(sRow) != null )
        {
            if( $(sRow).up('table') != null )
            {

                $(sRow).up('table').select('tr').each( function(xRow)
                {
                    xRow.removeClassName('selected');
                });
            }
        }
    }
}



function SelectRowById(sRowId) 
{
    CDMResetSelectionById();

    sCDMSelectedById = sRowId;

    if( $(sCDMSelectedById) != null ) 
        $(sCDMSelectedById).addClassName( 'selected' );

}

function CDMResetSelectionById() 
{
    if ( $(sCDMSelectedById) != null ) 
         $(sCDMSelectedById).removeClassName('selected');
}