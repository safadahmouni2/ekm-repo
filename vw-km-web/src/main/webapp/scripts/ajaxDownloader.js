
var ajaxDownloadUrl = 'CDMDownload.aspx';


function ajaxDownload_Init()
{
    
    var aAjax = $$("input.ajaxDownload");
    if( $A(aAjax).length == 0 )  // Keine Ajax-Controls eingebunden
        return;

    aAjax.each(function(xControl) 
    {
        xControl.onclick = function(evt) 
        {
            GetData_Download( xControl );
            return true;
        }
    });
}

function GetData_Download(xControl)
{
    var aPath      = window.location.pathname.split( '/' );
    var sUrl       = window.location.href.replace( aPath[aPath.length-1], ajaxDownloadUrl );
    var sParams    = '';
    var sValue     = '';
    var ajaxAction = '';
    
    xControl   = $(xControl);
    sControl   = xControl.identify();
    ajaxAction = getClassValue(xControl.className, 'ajaxAction_');
 
    sParams = "context=ajaxDownload&" +
              "id="                   + xControl.identify() + "&" +
              "value="                + xControl.value      + "&" +
              "action="               + ajaxAction;
 
    sUrl += '&' + sParams;
 
    new Ajax.Request( sUrl, 
                       {  method:   'post',
                          onSuccess: function(xData) { SetData_Download(xData); },
                          onFailure: function()      { AjaxResponseError();                 }
                       }
                    );
}

function SetData_Download(xData)
{
    var xDocument = null;
    var xDataCtrl = null;
    
    try
    {
        xDocument           = new Element( 'div' );
        xDocument.innerHTML = xData.responseText;
        xDataCtrl           = xDocument.getElementsByClassName('ajaxDownloadLink');
        
        window.open( xDataCtrl[0].innerHTML, "Export2Excel", "width=1000,height=400,left=200,top=200,menubar=yes,resizable=yes,scrollbars=no,status=no" );
    }
    catch( e )
    {
      
    }
}

