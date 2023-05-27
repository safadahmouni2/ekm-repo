
function getTableSettingsColKey(tableId) 
{
    colKey = getClassValue( $(tableId).className, 'colKey_' ) + "_";
    if ("_" == colKey)
    {
       	if (typeof(nTable)=="undefined"){
    		nTable = 65
    	};
        colKey = "setCol_" + String.fromCharCode(nTable);
        now_debug_out ("WRN: table.tableSettings: missing classValue(colKey_); using generic key (setCol)");
    }
    return colKey;
}

function tableSettings(tableId) 
{
    var devId = tableId + '_Settings';

    if ( !$(devId) )
    {
        var colKey = getTableSettingsColKey(tableId);
        var sSetCol  = '';
        var xContent = '<input type="hidden" id="tableSettings_Name" value="' + tableId + '" />';
     
        xContent += '<ul class="tableSettings">';
     
      //$$("#" + tableId + " tr:first-child td").each(function(td)
        var thead = $(tableId).down('thead');
        if (IsDefined(thead))
        {
	        $(thead).select('th').each(function(td)
	        {
	            if( td.innerHTML.trim().length > 0 && !td.hasClassName( 'noselect' ) )
	            {
	                sSetCol = getClassValue( td.className, colKey );
	                var anker = td.down("a");
	                var txt = td.innerHTML;
	                if (IsDefined(anker))
	                {
	                  txt = anker.innerHTML;
	                }
	                
	                if( !hasBodyClass( "usp_" + colKey + sSetCol ) )
	                    xContent += '<li><input checked="checked" onclick="applyTableSettings(this)" type="checkbox" value="' + colKey + sSetCol + '" id="' + colKey + sSetCol + '" class="checkbox"/>';
	                else
	                    xContent += '<li><input onclick="applyTableSettings(this)" type="checkbox" value="' + colKey + sSetCol + '" id="' + colKey + sSetCol + '" class="checkbox"/>';
	                
	                xContent += '<label for="' + colKey + sSetCol + '" />' + txt + '</label></li>';
	            }
	        });
	        
	        xContent += '</ul>';
        }
        else
        {
	        $(tableId).down('td').adjacent('td').each(function(td)
	        {
	            if( td.innerHTML.trim().length > 0 && !td.hasClassName( 'noselect' ) )
	            {
	                sSetCol = getClassValue( td.className, colKey );
	                
	                if( !hasBodyClass( "usp_" + colKey + sSetCol ) )
	                    xContent += '<li><input checked="checked" onclick="applyTableSettings(this)" type="checkbox" value="' + colKey + sSetCol + '" id="' + colKey + sSetCol + '" class="checkbox"/>';
	                else
	                    xContent += '<li><input onclick="applyTableSettings(this)" type="checkbox" value="' + colKey + sSetCol + '" id="' + colKey + sSetCol + '" class="checkbox"/>';
	                
	                xContent += '<label for="' + colKey + sSetCol + '" />' + td.innerHTML + '</label></li>';
	            }
	        });
	        
	        xContent += '</ul>';
        }
        dragWindowCreate(devId, "Einstellungen", xContent, '<a class="button abort" href="#">Schliessen</a>', "usp_TableSettings screenCenter");   
    }

    dragWindowShow(devId);
    return false;
}

function applyTableSettings(oThis) 
{
    if (oThis.checked)
        removeBodyClasses('usp_'+oThis.value);
    else
        addBodyClasses('usp_'+oThis.value);

 // Nur IE6
    $($('tableSettings_Name').value).select('.TrHeader td, thead th').invoke('toggleClassName','dummyIE');
}

function now_TableSettings_Init() 
{
    if( $A($$("table.tableSettings")).length == 0 )
        return;
    
    now_TableSettings_SelectorInit("table.tableSettings");
}

function now_TableSettings_SelectorInit(selector) 
{
    var tr, td;

    $$(selector + " .settings").each(function(el)
    {
        el.addClassName("cursorPointer");
        el.onclick = function() 
        {
            return tableSettings($(this).up('table.tableSettings').identify());
        }
    });
    
    
    var nTable    = 65;
    var nRow      =  0;
    var nCol      =  0;
    var aNoSelect = new Array();
    var colKey = "";
    
    $$(selector).each(function(table)
    {

        nRow = 0;

        colKey = getTableSettingsColKey(table.identify()) 
        
        table.select('tr').each(function(tr)
        {
            nCol = 0;
            
            tr.childElements().each(function(td)
            {
                if(nRow == 0)
                {
                    if( td.hasClassName( 'noselect' ) )
                        aNoSelect[nCol] = true;
                    else
                        aNoSelect[nCol] = false;
                }
                
                if( aNoSelect[nCol] == false )
                    td.addClassName( colKey + nCol );

                nCol++;
            });
            
            nRow++;
        });
        nTable++;
    });
    
    if( nTable > 67 )
        now_debug_out("Zur Zeit sind nur max. drei Fenster für Tabelleneinstellungen vorgesehen!");
}



