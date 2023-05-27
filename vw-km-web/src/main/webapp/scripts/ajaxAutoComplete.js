// UTF 8 für RegEx
var bIsDebug    = false;

var nKeyCode    = 0;
var xControl    = null;
var sControl    = "";
var xMenu       = null;
var nSelected   = -1;
var sSelected   = "";
var xSelected   = null;
var bEntry      = false;
var bIsMouseIn  = false;

// Options
var bViewKey    = true;
var bViewText   = false;

var sLastVal    = '';
var sAjaxUrl    = '/NewadaOnWeb/AjaxDataService/GetData.aspx?';
var sAjaxUrlDB2 = '/NewadaOnWeb/AjaxDataService/GetDataDB2.aspx?';

var bIsWaiting  = false;
var sWaitIcon   = '<img src="/NewadaOnWeb/Common/res/img/now-xloader-tiny.gif" >';
var xTimeOut;

function ajaxAction_Init() {
  // $$("input[class*='ajaxAction_']").each (
  $$("input.ajaxReturnType_autoSuggest").each (
    function (el) {
      el.addClassName("ajax");
      el.setAttribute("autocomplete", "off");
      el.onkeyup = function(evt) {
        return ajaxAutoSuggestAction(evt, this);
      }
      el.ondblclick = function(evt) {
        return ajaxAutoSuggestAction(evt, this);
      }
      el.onkeydown = function(evt) {
        runThroughSuggBox(evt);
      }
    }
  )
}

function ajaxAutoCall_Init()
{
  
    var aAjax = $$("input.ajaxAutoCall");
    if( $A(aAjax).length == 0 )  // Keine Ajax-Controls eingebunden
        return;
  
    aAjax.each(function(xControl) 
    {
        xControl.addClassName('ajax');
        
        xControl.onkeyup = function(evt) 
        {
            nKeyCode = keyCodeFromEvent (evt);
            
            // Wenn class ein validate beinhaltet wird validiert
            if( xControl.className.indexOf( 'validate') > -1 )
                nowFieldValidatorOnKeyup($(this), nKeyCode);
            
            switch (nKeyCode) 
            {
                case Event.KEY_TAB:
                case Event.KEY_RETURN:
                case Event.KEY_ESC:
                case Event.KEY_LEFT:
                case Event.KEY_RIGHT:
                case Event.KEY_HOME:
                case Event.KEY_END:
                case 16:  // Shift --> Fehlt in prototype als Konstante
                case 17:  // STRG
                case 18:  // ALT
                case 91:  // WIN
                case 93:  // Context  // es fehlen noch einige Tasten
                    // Nichts zu tun
                    break;
                case Event.KEY_PAGEUP:
                case Event.KEY_PAGEDOWN:
                    // Nichts zu tun
                    break;
                case Event.KEY_UP:
                case Event.KEY_DOWN:
                    // Nichts zu tun
                    break;
                default:
                    GetData_AutoCall(this);
                    break;
            }
        }
    });
}
function ajaxAutoComplete_Init()
{
    ajaxAction_Init();
    
    var aAjax = $$("input.ajaxAutoComplete");
    if( $A(aAjax).length == 0 )  // Keine Ajax-Controls eingebunden
        return;

    if( !document.getElementById('autoCompleteMenu') )
    {
        xElement             = document.createElement('div');
        xAttribute           = document.createAttribute('id');
        xAttribute.nodeValue = 'autoCompleteMenu';
        xElement.setAttributeNode(xAttribute);
     
        xAttribute           = document.createAttribute('class');
        xAttribute.nodeValue = 'autoCompleteMenu';
        xElement.setAttributeNode(xAttribute);
       
        document.getElementsByTagName('form')[0].appendChild(xElement);
    }
    
    Event.observe(window, 'resize', Window_OnResize);
   
    $('autoCompleteMenu').onmouseout = function()
    {
        bIsMouseIn = false;
    }

    aAjax.each(function(xControl) 
    {
        bViewKey  = xControl.hasClassName('autoComplete_Key');
        bViewText = xControl.hasClassName('autoComplete_Text');
        xControl.addClassName('ajax');
        xControl.setAttribute( "autocomplete", "off" );
        
        xControl.onblur = function(evt) 
        {
            var nKeyCode = keyCodeFromEvent(evt);
            if( bIsMouseIn )
            {
                var oControl = $(xControl);
             
                if( bViewKey && !bViewText )
                    oControl.value = xSelected.innerHTML.split(':')[0];
                else
                if( bViewText && !bViewKey )
                    oControl.value = xSelected.innerHTML.split(':')[1].replace('\&nbsp;', '');
                else
                if( bViewKey && bViewText )
                    oControl.value = xSelected.innerHTML.replace('\&nbsp;', '');
                else
                    oControl.value = xSelected.innerHTML.split(':')[0];
                
                SetFocus(oControl);
            }
         
            sControl = "";
            
            HideMenu('autoCompleteMenu');
          
            if( !xControl.up('.validate') )
            {
                if (xControl.hasClassName ('validate'))
                {//RMO: Noch ein Kompromiss: Wenn class ein validate beinhaltet wird validiert
                    nowValidatorApply( xControl );
                    nowValidatorErrorBehaviour();
                }
            }
            
            if( xControl.hasClassName('ajaxAutoLoad') )
            {
                GetData_AutoLoad(this, "92");
            }
            
          // Zu langsam - Ist eine Validierung hier überhaupt notwendig
          // TMUE: Ja, Validiertung ist notwendig. BSP. Auftragsbearbeitung-Expertenmodus ZBhf wird nicht geprueft
          // Kompromiss: Beim IE6 vorerst keine Pruefung.
          //if (!IsBrowserVersion("IE6"))
          //{RMO: Noch ein Kompromiss: Wenn class ein validate beinhaltet wird validiert
            if (xControl.hasClassName ('validate'))
            {
              nowFieldValidatorOnBlur($(this), nKeyCode);
            }
          //}          
        }
        
        xControl.onkeydown = function(evt) 
        {
            nKeyCode = keyCodeFromEvent (evt);
            
            switch (nKeyCode) 
            {
                case Event.KEY_UP:
                    SelectItem(true, this);
                    break;
                case Event.KEY_DOWN:
                    SelectItem(false, this);
                    break;
                case Event.KEY_RETURN:
                    HideMenu('autoCompleteMenu');
                    
                    if( xControl.hasClassName('ajaxAutoLoad') )
                    {
                        GetData_AutoLoad(this, "122");
                    }
                    
                    return false;
                    break;
            }
            return true;
        }
     
        xControl.onkeyup = function(evt) 
        {
            nKeyCode = keyCodeFromEvent (evt);

            // Zu langsam - Ist eine Validierung hier überhaupt notwendig
            // TMUE: Ja, Validiertung ist notwendig. BSP. Auftragsbearbeitung-Expertenmodus ZBhf wird nicht geprueft
            // Kompromiss: Beim IE6 vorerst keine Pruefung.
            //if (!IsBrowserVersion("IE6"))
            //{RMO: Noch ein Kompromiss: Wenn class ein validate beinhaltet wird validiert
            //    if( xControl.className.indexOf( 'validate') > -1 )
            //        nowFieldValidatorOnKeyup($(this), nKeyCode);
            //}
            // TMUE: nowFieldValidatorOnKeyup muß immer aufgerufen werden, da ansonsten der uppercase nicht durchlaufen wird (z.B bei LVP)
            nowFieldValidatorOnKeyup($(this), nKeyCode);
            
            switch (nKeyCode) 
            {
                case Event.KEY_TAB:
                case Event.KEY_RETURN:
                case Event.KEY_ESC:
                case Event.KEY_LEFT:
                case Event.KEY_RIGHT:
                case Event.KEY_HOME:
                case Event.KEY_END:
                case 16:  // Shift --> Fehlt in prototype als Konstante
                case 17:  // STRG
                case 18:  // ALT
                case 91:  // WIN
                case 93:  // Context  // es fehlen noch einige Tasten
                    // Nichts zu tun
                    break;
                case Event.KEY_PAGEUP:
                case Event.KEY_PAGEDOWN:
                    // Nichts zu tun
                    break;
                case Event.KEY_UP:
                case Event.KEY_DOWN:
                    // Nichts zu tun
                    break;
                default:
                    Position.clone($(this), $('autoCompleteMenu'), { setHeight: false, offsetTop: $(this).offsetHeight });
                    $('autoCompleteMenu').setStyle({ width: 'auto' });
                    if ("" == $('autoCompleteMenu').innerHTML)
                    {
                      $('autoCompleteMenu').addClassName('hidden');
                    }
                    bHasChange = true;
                    GetData_AutoComplete(this);
                    break;
            }
        }
    });
   
    ResetData_Autoload();
}

function ajaxAutoLoad_Init()
{
    var nRequiredCount = 0;
    var nValueCount    = 0;

    var aAjax = $$("input.ajaxAutoLoad,select.ajaxAutoLoad");
    if( $A(aAjax).length == 0 )  // Keine Ajax-Controls eingebunden
        return;
    
    aAjax.each(function(xControl)
    {
        if( xControl.type == 'radio' )
        {
            xControl.observe('click', function(evt)
            {
              GetData_AutoLoad(xControl, "194");
            });
        }
        else
        if( xControl.type == 'select-one' || xControl.type == 'select' )
        {
            xControl.observe('change', function(evt)
            {
              GetData_AutoLoad(xControl, "202");
            });
        }
        
        if( xControl.hasClassName( 'required' ) )
        {
            nRequiredCount++;
            sControl = xControl.identify();
            
            if( xControl.value.length )
                nValueCount++;
        }
    });
 
  
    if( nRequiredCount > 0 && nRequiredCount == nValueCount )
    {
        GetData_AutoLoad( $(sControl), "218" );
    }
}

function GetData_AutoLoad(xControl, sStartPoint)
{
    var oControl;
    var sUrl       = sAjaxUrl;
    var sContext   = '';
    var nCounter   = 10;
    var bIsValue   = false;
    var bRequired  = true;
    var nMinLength = 0;
    var sValues    = '';
    var sMandant   = getBodyClassValue( 'Mandant_' );
    var sParams    = '';
    var sUseWith   = '';
    var aUseWith   = new Array(0);
    var sChecked   = '';
    var sFilter    = '';
    var sSubFilter = '';
    
    xControl   = $(xControl);
    nMinLength = parseFloat( getClassValue(xControl.className, 'min_') );


    // Wenn schon vorhanden, nicht mehr nachladen
    if( xControl.identify().startsWith( 'autoLoadSubContent_' ) )
    {
      if( xControl.down('ul') )
      {
        if( xControl.down('ul').hasChildNodes() )
        {
            xControl.down('ul').toggle();
            xControl.toggleClassName( 'closed' );
            return false;
        }
      }
      
      if( xControl.down('div') )
      {
        if( xControl.down('div').identify() == 'X' + xControl.identify() )
        {
            return false;
        }
      }
      
      sSubFilter = getClassValue( xControl.identify(), 'autoLoadSubContent_');
      sUseWith   = 'autoLoadSubFilter_' + sSubFilter;
      sFilter    = $(sUseWith).value;
     
      sValues += 'autoLoadSubFilter=' + sFilter + '/';
      nCounter = 60;
     
      ShowWaitCursorX( 'autoLoadSubContent_' + sSubFilter );
     
      sUseWith = '';
      sFilter  = '';
      
    }

    sParams  = "context=ajaxAutoLoad&" +
               "id="                   + xControl.identify() + "&" +
               "clsName="              + xControl.className  + "&" +
               "mandant="              + sMandant            + "&" +
               "useWith="              + sUseWith            + "&" +
               "useWithValue="         + sFilter             + "&" +
               "counter="              + nCounter            + "&";

    $$(".ajaxAutoLoad").each( function(xControl) 
    {
        oControl = $(xControl);
       
        if( oControl.identify() == sUseWith )
        {
          // do nothing
        }
        else
        if( getClassValue( oControl.className, 'checked_' ) )
        {// Nur wenn korrosp. Control checked ist
         
            if( bIsValue )
                sValues += '/';
         
            sChecked = getClassValue( oControl.className, 'checked_' );
         
            if( $(sChecked).checked )
            {
                sContext = "data_"  + getClassValue( oControl.className, 'data_' );
                sValues += sContext + "=" + oControl.value;
            }
         
            bIsValue = true;
        }
        else
        if( oControl.value.length > 0 )
        {
            if( bIsValue )
                sValues += '/';
            
            nMinLength = parseFloat( getClassValue(oControl.className, 'min_') );
            
            if( nMinLength && oControl.value.length < nMinLength )
            {
                bRequired = false;
                sLastVal  = '';
                ShowNotFound();
                return false;
            }
         
            sContext = getClassValue( oControl.className, 'data_' );
           
            if( oControl.type == 'radio' )
            {
                if( oControl.checked && sContext.length > 0 )
                {
                    sContext = "data_" + sContext;
                    sValues += sContext + "=" + oControl.value;
                }
                
                bIsValue = true;
            }
            else
            if( sContext.length > 0 )
            {
                sContext = "data_" + sContext;
                sValues += sContext + "=" + oControl.value;
                
                bIsValue = true;
            }
           
        }
        else
        if( oControl.hasClassName( 'required' ) )
        {
            bRequired = false;
            return false;
        }
        
        if( sValues.length > 0 && sValues.lastIndexOf('/') == sValues.length - 1 )
            sValues = sValues.substring( 0, sValues.length - 1 );
     
        return true;
    });

    if(!bRequired)
        return false;
    
    if( sLastVal == sValues )
        bIsValue  = false;
    
    sLastVal = sValues;
    
    if( bIsValue )
    {
        sParams += "value=" + sValues;
        sUrl    += sParams;
     
        ajaxCall_start (xControl);
        new Ajax.Request( sUrl, 
                           {  method:   'post',
                              onSuccess: function(xData) { SetData_AutoLoad(xData); },
                              onFailure: function()      { AjaxResponseError();                 }
                           }
                        );
     
        if(!xControl.identify().startsWith( 'autoLoadSubContent_' ) )
            ShowWaitCursorResult();
    }
    
    return true;
}
function SetData_AutoLoad(xData) 
{
  ajaxCall_stop ();
  var nLine 	   = 0;
	var sLineColor = '';//' odd ';//In Klärung
    var aLine      = new Array();
    
    try
    {
        xElement           = document.createElement('div');
        xElement.innerHTML = xData.responseText;
        xElement           = $(xElement);
        xElement           = xElement.down();
        
        if( xElement.identify() == 'autoLoadItems' )
        {
            $$(".ajaxAutoLoadResult").each( function(xControl) 
            {
                xControl           = xControl.down('div');
                xControl.innerHTML = '';
             
			  
                xElement.childElements().each( function(oControl)
                {
                    aLine = oControl.innerHTML.split( '{#|#}' );
                    
                    if( aLine[3] )  // Inkl. Stückzahl
                        oControl.innerHTML = '<div class="collapsible expandItem">' +
                                             '   <input type="hidden" id="autoLoadSubFilter_'  + nLine + '" value="' + aLine[0] + '" />' +
                                             '   <span class="fright">' + aLine[2] + '  (' + aLine[3] + ')</span>' +
                                             '   <div id="autoLoadSubContent_' + nLine + '" onclick="GetData_AutoLoad(this, 423)" class="' + sLineColor + ' paddingLeft collapsibleHead head closed cursorPointer data_searchprnumbers useWith_autoLoadSubFilter_'  + nLine + '">' + aLine[1] + '</div>' +
                                             '</div>';
                    else
                        oControl.innerHTML = '<div class="collapsible expandItem">' +
                                             '   <input type="hidden" id="autoLoadSubFilter_'  + nLine + '" value="' + aLine[0] + '" />' +
                                             '   <span class="fright">' + aLine[2] + '</span>' +
                                             '   <div id="autoLoadSubContent_' + nLine + '" onclick="GetData_AutoLoad(this, 429)" class="' + sLineColor + ' paddingLeft collapsibleHead head closed cursorPointer data_searchprnumbers useWith_autoLoadSubFilter_'  + nLine + '">' + aLine[1] + '</div>' +
                                             '</div>';
                 
                    xControl.innerHTML += oControl.innerHTML;
                    nLine++;
					
					if( nLine % 2 == 0)
					    sLineColor = '';//In Klärung'odd'; // ToDo: raus? oder ggf. div .odd ins css?
					else
						sLineColor = '';
                });
              
                if( xControl.innerHTML.length == 0 )
                    ShowNotFound();
                //    xControl.innerHTML = '<div class="marginLeft collapsibleHead head closed cursorPointer">' + $('_ctl0_autoLoadNoResult').value + '</div>';
            });
            
        }
        else
        if( xElement.identify() == 'autoLoadSubItems' )
        {
            xControl = $( xElement.readAttribute('title') );
            
            if( xControl.hasClassName( 'closed' ) )
                xControl.removeClassName( 'closed' );
            
            xControl.childElements().each( function(oControl)
            {
                oControl.remove();
            });
            
            xControl.innerHTML += xElement.up().innerHTML;
         
            Collapsible_Behaviour_Selector_Init( '#' + xControl.identify() );
        }
    }
    catch (e) 
    {
      if( bIsDebug )
          local_alert( "Error!\n"+e + " - "+ xData.responseText );
      
    }
}
function ResetData_Autoload()
{
    $$(".ajaxAutoLoadResult").each( function(xControl) 
    {
        xControl = xControl.down('div');
        xControl.innerHTML = '<div class="marginLeft collapsibleHead head closed cursorPointer">' + $('_ctl0_autoLoadNoResult').value + '</div>';
    });
}

function GetData_AutoComplete(xControl)
{
    var sUrl     = sAjaxUrl;
    var sParams  = '';
    var sValue   = '';
    var bIsValid = false;
    var sChecked = '';
    var xForm;
  //var xParams;

    xControl = $(xControl);
    xForm    = getForm();

    // Validierung
    if (xControl.className.match('data_countryPhonePrefix|data_cityPhonePrefix'))
        bIsValid = Validate_Phone(xControl);
    else
        bIsValid = Validate_Text(xControl);

    if( bIsValid )
    {
        var oControl;
        var sUseWith = '';
        var aUseWith = getClassValues(    xControl.className, 'useWith_' );
        var sCount   = getClassValue(     xControl.className, 'view_'    );
        var sMandant = getBodyClassValue( 'Mandant_'                     );
        var sFilter  = '';
     
        sControl = xControl.identify();
     
        if( aUseWith.length > 0 )
        {
            for( var i = 0; i < aUseWith.length; i++ )
            {
                oControl = $(aUseWith[i]);
                sChecked = getClassValue( $(aUseWith[i]).className, 'checked_' );
             
                if( oControl == null )
                {
                    // Nichts zu tun
                }
                else
                if( sChecked.length > 0 )
                {// Abhängigkeit zur Checkbox/Radiobutton
                    if( $(sChecked).checked )
                    {
                        sUseWith += 'data_' + getClassValue( oControl.className, 'data_' ) + ';';
                        sFilter  += oControl.value + ';';
                    }
                }
                else
                if( oControl.type == 'radio' )
                {
                    if( oControl.checked )
                    {
                        sUseWith += 'data_' + getClassValue( oControl.className, 'data_' ) + ';';
                        sFilter  += oControl.value + ';';
                    }
                }
                else
                {
                    sUseWith += 'data_' + getClassValue( oControl.className, 'data_' ) + ';';
                    sFilter  += GetControlValueById( aUseWith[i] ) + ';';
                }
            }
         
            sUseWith = sUseWith.substring( 0, sUseWith.length-1 );
            sFilter  = sFilter.substring(  0, sFilter.length-1  );
        }
     
        if( sCount.length == 0 )
            sCount = '10';
     
      //xParams  = {  context:'ajaxAutoComplete',   
      //              id:      xControl.identify(),
      //              value:   xControl.value,
      //              clsName: xControl.className,
      //              counter: 10,
      //              action: 'autoCompleteMenu:GetCity' // action später als Übergabeparameter!!!
      //           }
        sParams = "context=ajaxAutoComplete&" +
                  "id="                       + xControl.identify() + "&" +
                  "value="                    + xControl.value      + "&" +
                  "mandant="                  + sMandant            + "&" +
                  "clsName="                  + xControl.className  + "&" +
                  "useWith="                  + sUseWith            + "&" +
                  "useWithValue="             + sFilter             + "&" +
                  "counter="                  + sCount;//              + "&" +
                  //"action=autoCompleteMenu:GetCity";
     
      //serializedForm = $H( xForm.serialize( true ) ); // funkt nicht mit IE inkl. ViewState
      //serializedForm.update( $H( xParams ) );
     
        sUrl += sParams; //serializedForm.toQueryString();
     
        ShowWaitCursor();
        ajaxCall_start (xControl);
        new Ajax.Request( sUrl, 
                           {  method:   'post',
                            //parameter: serializedForm,
                              onSuccess: function(xData) { SetData_AutoComplete(xData); },
                              onFailure: function()      { AjaxResponseError();                     }
                           }
                        );
    }
}
function SetData_AutoComplete(xData)
{
    ajaxCall_stop ();
    var jsonRes = null;
    var xTest   = null;
    var xCtrl   = null;
    var sCtrl   = '';
 
    try
    {
        xData.responseText = "{autoCompleteMenu:'" + 
                              xData.responseText.slice(0,xData.responseText.length-2) +
                             "'}";
        jsonRes            = $H( xData.responseText.evalJSON() );
    }
    catch (e) 
    {
        if( bIsDebug )
            local_alert( "Error!\n"+e + " - "+ xData.responseText );
      
        jsonRes = $H();
    }

    jsonRes.each( function(pair)
    {
        xTest           = new Element( 'div' ).update(pair.value);
        xTest.innerHTML = pair.value;
        xCtrl           = xTest.getElementsBySelector('[id="autoCompleteMenuItemsCaller"]') ;
        sCtrl           = xCtrl[0].readAttribute('value');
     
        if( sCtrl == sControl && $(pair.key) )
            $(pair.key).update('' + pair.value + '');
    });

    ShowMenu('autoCompleteMenu');

}


function GetData_AutoCall(xControl, bUseSQL)
{
    var sUrl     = sAjaxUrlDB2;
    var sParams  = '';
    var sValue   = '';
    var sMandant = '';
    var sCount   = '';
    var sCallF   = '';
    var xForm    = null;
    var aUseVar = null;
    var sUseVar = '';
    var sUseVarValue = '';
    var sFilter  = '';
    
    if( bUseSQL == true ) sUrl = sAjaxUrl;
    
    xControl = $(xControl);
    
    
    xForm    = getForm();
    
    sMandant = getBodyClassValue( 'Mandant_' );
    sCallF   = getClassValue    ( xControl.className, 'ajaxCall_');
    aUseVar  = getClassValues   ( xControl.className, 'useVar_' );

    if( aUseVar.length > 0 )
    {
        for( var i = 0; i < aUseVar.length; i++ )
        {
            //oControl = $(aUseVar[i]);
          
            if( aUseVar[i] == null )
            {
                // Nichts zu tun
            }
            else
            {
                sUseVar += 'data_' + aUseVar[i] + ';';
                sUseVarValue  += readScriptList( aUseVar[i] ) + ';';
            }
        }
      
        sUseVar = sUseVar.substring( 0, sUseVar.length-1 );
        sUseVarValue  = sUseVarValue.substring(  0, sUseVarValue.length-1  );
    }


    sParams  = "context=ajaxAutoCall&" +
               "id="                       + xControl.identify() + "&" +
               "value="                    + xControl.value      + "&" +
               "mandant="                  + sMandant            + "&" +
               "clsName="                  + xControl.className  + "&" +
               "useVar="                   + sUseVar						 + "&" +
               "useVarValue="              + sUseVarValue        + "&" +
               "counter="                  + sCount;

    sUrl += sParams;
 

    ajaxCall_start (xControl);
    new Ajax.Request( sUrl, 
                       {  method:   'post',
                          onSuccess: function(xData) { SetData_AutoCall(xData,sCallF) },
                          onFailure: function()      { AjaxResponseError();                     }
                       }
                    );    
}
function SetData_AutoCall(xData,sFunctionCall)
{
    var xElement = null;
    ajaxCall_stop();
    try
    {
        xElement           = new Element( 'div' ).update( xData.responseText );
        xElement.innerHTML = xData.responseText;
    }
    catch (e) 
    {
        if( bIsDebug )
            local_alert( "Error!\n"+e + " - "+ xData.responseText );
    }

    if (xElement.down('table') != null)
    {
        if( bIsDebug )
            local_alert("table : " + sFunctionCall);
        eval( sFunctionCall + "(" + xElement.innerHTML + ")" );
    }

    else if( xElement.down('li') != null )
    {
        if( bIsDebug )
            local_alert("li : " + sFunctionCall);
        eval( sFunctionCall + "('" + xElement.down('li').innerHTML + "')" );
    }
}
function ShowNotFound()
{
    $$(".ajaxAutoLoadResult").each( function(xControl) 
    {
       xControl           = xControl.down('div');
       xControl.innerHTML = '<div class="marginLeft collapsibleHead head closed cursorPointer">' + $('_ctl0_autoLoadNoResult').value + '</div>';
    });
}
function ShowWaitCursor()
{
    StopWaitCursor();
    
    bIsWaiting = true;
    xTimeOut   = window.setTimeout( 'ShowWaitCursor_Async()', 500 );
}
function StopWaitCursor()
{
    bIsWaiting = false;
    window.clearTimeout(xTimeOut);
}
function ShowWaitCursor_Async()
{
    if( bIsWaiting == true )
    {
        bIsWaiting             = false;
        
        xMenu                  = document.getElementById( 'autoCompleteMenu' );
        xMenu.innerHTML        = sWaitIcon;
        xMenu.style.visibility = 'visible';
     
        if( need_ie6_IFrame() )               //IE6-Bug
            addBodyClasses("hideListBoxes");
    }
    
}
function ShowWaitCursorX(sSelectedLine)
{
    xControl = $(sSelectedLine);
    
    xControl.innerHTML += '<div id="X' + sSelectedLine + '" class="marginLeft">' + sWaitIcon + '</div>';
}
function ShowWaitCursorResult()
{
    $$(".ajaxAutoLoadResult").each( function(xControl) 
    {
        xControl           = xControl.down('div');
        xControl.innerHTML = sWaitIcon;
    });
}
function ShowMenu(sID) 
{
    var aSel  = $$('#autoCompleteMenuItems li');
    var sCtrl = ($('autoCompleteMenuItemsCaller')).readAttribute('value');

    if( sCtrl != sControl )   // Antwort kam zu spät, Control schon verlassen.
        return;

    StopWaitCursor();
  
    nSelected  = -1;
    bIsMouseIn = false;

    aSel.each(function(el)
    {
        el.removeClassName( "selected" );
        el.innerHTML  += "&nbsp;";
        
        el.onmouseover = function()
        {
            bIsMouseIn = true;
         
            if( xSelected != null )
                xSelected.removeClassName( "selected" );
           
            xSelected = this;
            xSelected.addClassName( "selected" );
        }
      
    });
  
    if( aSel.length > 0 )
    {
        nSelected              = -1;
        xMenu                  = document.getElementById( sID );
       // xMenu.style.visibility = 'visible';
        xMenu.removeClassName('hidden');
     
        if( need_ie6_IFrame() )               //IE6-Bug
            addBodyClasses("hideListBoxes");
  
    }
    else
    {
        HideMenu('autoCompleteMenu');
    }

}
function HideMenu(sID) 
{
    //var aSel = $$('#autoCompleteMenuItems li');

    StopWaitCursor();

    //if (aSel.length > 0) 
    //{
    //    $(sID).setStyle({ top: '-5000px' });
    // 
    //    for (var i = 0; i < aSel.length; i++)
    //        $(aSel[i]).remove();
    // 
    //}

    if( need_ie6_IFrame() )                 //IE6-Bug
        removeBodyClasses("hideListBoxes");

    if( xMenu != null )
    {
        xMenu.innerHTML        = '';
        // xMenu.style.visibility = 'hidden';
        xMenu.addClassName('hidden');
    }
    
    nSelected  = -1;
    bIsMouseIn = false;
}
function SelectItem(bUp, xControl)
{
    var aSel     = $$('#autoCompleteMenuItems li');
    var oControl = $(xControl);
  
    if( aSel.length < 1 )
        return;
  
    if( nSelected < 0 )
    {
        bEntry    = true;
        sSelected = xControl.value;
    }

    if( bUp )
        nSelected--;
    else
        nSelected++;
  
    if( nSelected < 0 && bUp )  // Menü verlassen und alten Wert wiederherstellen
        nSelected = -1;   
    else
    if( nSelected < 0 )
        nSelected = aSel.length - 1;
    else
    if( nSelected > aSel.length - 1 ) 
        nSelected = 0;

    aSel.each(function(el)
    {
        el.removeClassName("selected");
    });

    if( nSelected >= 0 )
    {
        xSelected = $(aSel[nSelected]);
        xSelected.addClassName("selected");
       
        if( bViewKey && !bViewText )
           xControl.value = xSelected.innerHTML.split(':')[0].replace('\&nbsp;', '');
        else
        if( bViewText && !bViewKey )
           xControl.value = xSelected.innerHTML.split(':')[1].replace('\&nbsp;', '');
        else
        if( bViewKey && bViewText )
           xControl.value = xSelected.innerHTML.replace('\&nbsp;', '');
        else
           xControl.value = xSelected.innerHTML.split(':')[0].replace('\&nbsp;', '');
    }
    else
    {
        //xControl.value = sSelected;
    }
}


function Window_OnResize()
{
    HideMenu('autoCompleteMenu');
}

function AjaxResponseError()
{
    if( bIsDebug )
        now_debug_out("Serveranfrage fehlgeschlagen!");
    
    StopWaitCursor();
    HideMenu('autoCompleteMenu');
    ajaxCall_stop();
}


function Validate_Phone(oControl) 
{
    var sTextVal  = '';
    var sTextVal2 = '';
    var sCheck    = '';
    var sControl  = '';
    var bIsValid  = false;
    
    if( oControl != null && oControl.value )
    {
        sTextVal = oControl.value;
        sTextVal = sTextVal.replace(/^ +/, '');
        sTextVal = sTextVal.replace(/^0+/, '');
     
        for( var i = 0; i < sTextVal.length; i++ )
        {
            sCheck = sTextVal.substring(i, i + 1);
          
            if( sCheck.search( "[A-Za-zäöüÄÖÜß\.\-@ ]") > -1 )
                sTextVal2 += sCheck;
        }
     
        if( oControl.value != sTextVal2 )
            oControl.value = sTextVal2;
     
        bIsValid = oControl.value.length > 0;

    }

    return bIsValid;
}

function Validate_Text(oControl) 
{
    var sTextVal  = '';
    var sTextVal2 = '';
    var sCheck    = '';
    var sControl  = '';
    var bIsValid  = false;
    
    if( oControl != null && oControl.value )
    {
        sTextVal = oControl.value;
        sTextVal = sTextVal.replace(/^ +/, '');
     
        for( var i = 0; i < sTextVal.length; i++ )
        {
            sCheck = sTextVal.substring(i, i + 1);
          
            if( sCheck.search( "[A-Za-zäöüÄÖÜß\.\-@ ]") > -1 )
                sTextVal2 += sCheck;
        }
     
        if( oControl.value != sTextVal2 )
            oControl.value = sTextVal2;
     
        bIsValid = oControl.value.length > 0;

    }

    return bIsValid;
}

function ajaxCall_start(xControl)
{
  if (IsDefined(xControl))
  {
    xControl.addClassName ("ajaxPerformedField");
  }
  // addBodyClass ("ajaxLoading");
}

function ajaxCall_stop()
{
  // removeBodyClass ("ajaxLoading");
  $$("input.ajaxPerformedField").each (function(el)
  {
    el.removeClassName ("ajaxPerformedField");
  })
}
