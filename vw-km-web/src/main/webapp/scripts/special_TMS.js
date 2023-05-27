//
//	special functions to handle user-actions in TMS
//

var tms_table_tr = "table.tmsTableCellEdit tbody tr";

// start initilization
function SpecialPageBehaviour(){
	tmsTable_init();
}

// init the TMS-allRecords-table: add a click-event to show edit-fields
function tmsTable_init(){
	$$ (tms_table_tr).each(function(el)
	{
		// add an click-event on each table-row
		el.onclick=function() {
			tmsTable_createEditRow($(this));
		};	   
	})
}

// create the click-event to show edit-fields
function tmsTable_createEditRow(oThis){
	// change the checkBox as marked		
	var box = $(oThis.getElementsByClassName('tmsCheckBox', oThis.down("td")));
	//alert(box);
	//alert(box.length);		
	//alert(box.item(0).type);
	//alert(box.item(0).value);
	box.item(0).setAttribute ("checked", "checked");
	box.item(0).checked = true;
	box.item(0).value = 'true';
	box.item(0).setAttribute ("disabled", "disabled");
	
	// add an input-field into each column (but only if there is no input-field)
	oThis.select("td span").each(function(el)
	{
		// select the td element above the span-element and place the input-field in it
		if(el.up("td").select("input")==""){
			var text = el.innerHTML;
	 	   	var editable = "<input type='text' name='bla' class='tmsGridInput' value='" + text + "'/>";
		 	el.addClassName("displayNone");
		   	el.up("td").insert(editable);
		   	el.up("td").addClassName("editMode");
		}
	})	
}
// canceling all changes in the table
function cancel(){
	if (confirm("Alle Eingaben verwerfen ??")){
		$$ (tms_table_tr).each(function(el)
		{
			tmsTable_removeEditRow($(el));
		})
	}
}

// remove all editable fields and values from the allRecords-table
function tmsTable_removeEditRow(oThis){
	// clean the checkBox
	var box = $(oThis.getElementsByClassName('tmsCheckBox', $(oThis).down("td")));
	
	box.item(0).removeAttribute ("checked");
	box.item(0).checked = false;
	box.item(0).value = 'false';
	box.item(0).removeAttribute ("disabled");
	
	// delete all input-fields of the table row
	box = $(oThis.getElementsByClassName('tmsGridInput', $(oThis).down("td")));
	while (box.length>0) {
		box.item(0).remove();
	}
	
	// enable the old values
	oThis.select("td span").each(function(el)
	{
		el.removeClassName("displayNone");
	})	
}

// enable the checkBox to get the values on server
function prepareToSend(){
	$$ (tms_table_tr).each(function(el)
	{
		// enable the checkBox to get the values on server
		var box = $(el.getElementsByClassName('tmsCheckBox', $(el).down("td")));
		box.item(0).removeAttribute ("disabled");
	})
}
