// init
function SpecialPageBehaviour() {
	syncFields();
}

// general
function dataTableSelectOneRadio(radio) {
    $$("table.radioToggle tbody input[type=radio]").each(function(el)
    {
      el.checked = false;
    })
    radio.checked = true;
}
function selectAllCheckboxes(checkbox) {
	var chk = checkbox.checked;
	
    $$("table.selectAllCheckboxes tbody input[type=checkbox]").each(function(el)
    {
      el.checked = chk;
    })
	
	//$(checkbox).up('table.selectAllCheckboxes').select('tbody input[type=checkbox]').each(function(el)
    //{
    //  el.checked = chk;
    //})
}
function dataTableSelectInit() {
	//alert("1");
    $$("table.selectModifiedItem tbody input[type=text]").each(function(el)
    {
 		el.onchange = function() {
 			setModifiedItem(this);
 			return true;
		}
    })
    
    $$("input.moreEntries").each(function(el)
    {
 		el.onclick = function() {
 			return showNextRowOnClick();
		}
    })

	var table = $$("table.newRowOnTab").first();
	if (table) {
	
		var colNumber = getClassValue(table.className, 'newRowOnCol_');
		$$("table.newRowOnTab tbody tr:first-child").first().addClassName("visible");
	    $$("table.newRowOnTab tbody tr").each(function(tr)
	    {
	    	if (tr.down("input[type=checkbox]"))
	    	{
		    	var trArray = $A(tr.select("td"));
		    	var el = trArray[colNumber];
		    	var elCheckbox = trArray[0];
		    	var elNwCommNo = trArray[1];
				
			    if (elCheckbox.down("input[type=checkbox]").checked || !elNwCommNo.down("input")) {
			    	elNwCommNo.up("tr").addClassName("visible");
			    }
		    	
		    	var txtFieldForAction = null;
		    	if (colNumber == 8) {
			 		txtFieldForAction = el.select("input[type=text]")[2];
		    	} else {
		    		txtFieldForAction = el.down("input[type=text]");
				}
				
		 		txtFieldForAction.onkeydown = function(evt)
        		{
          			var nKeyCode = keyCodeFromEvent (evt);
		 			return showNextRowOnTab(this, nKeyCode);
				}
			}
	    })
    }
}
function setModifiedItem(oThis) {
	$(oThis).up('tr').down('input[type=checkbox]').checked = true;
}

// stockTransfer
function showMoreFields(oThis) {
	var liHiddenArray = $(oThis).up('ul.ulShowMoreFields').select('li.hidden');
	if (liHiddenArray.length > 0) {
		liHiddenArray.first().removeClassName('hidden');
		liHiddenArray.first().up('ul').toggleClassName('jaunty');
		if (liHiddenArray.length == 1) {
			$(oThis).addClassName('disabled');
		}
	}
	return false;
}
function syncFields() {
	var fieldsArray = $$("ul.ulShowMoreFields li.hiddenLine input:not([value=''])");
	if (fieldsArray.length > 0) {
		fieldsArray.each(function(el) {
			el.up('li').removeClassName('hidden');
		});
	}
}


// selectiveOrderMod
function showNextRowOnTab(oThis, nKeyCode) {
	var row = $(oThis).up('tr').next('tr');
	if (row && nKeyCode == Event.KEY_TAB) {
		row.addClassName("visible");
	}
	return true;
}

function showNextRowOnClick() {
	var perform = true;
    $$("table.newRowOnTab tbody tr").each(function(el)
    {
    	if (perform && !el.hasClassName("visible")) {
    		el.addClassName("visible");
    		SetFocus(el.down("input[type=text]"));
    		perform = false;
    	}
    })
	return false;
}



// message boxes
function askUserDeleteMmUser(oThis) {
	messageBoxCreate(
		'delMmUser',
		'ACHTUNG!',
		'Wollen Sie den User wirklich loeschen?',
		'<a href="#" onclick=\"'+getClickCode(oThis)+'\" class="button">Ja</a> <a href="#" class="button abort">Nein</a>'
	);
	return true;
}

function askUserSrDeleteSelected(oThis) {
	messageBoxCreate(
		'delSrSelected',
		'ACHTUNG!',
		'Wollen Sie die ausgewaehlten Daten wirklich loeschen?',
		'<a href="#" onclick=\"'+getClickCode(oThis)+'\" class="button">Ja</a> <a href="#" class="button abort">Nein</a>'
	);
	return true;
}

function askUserSrDeleteAll(oThis) {
	messageBoxCreate(
		'delSrAll',
		'ACHTUNG!',
		'Wollen Sie wirklich alle Daten loeschen?',
		'<a href="#" onclick=\"'+getClickCode(oThis)+'\" class="button">Ja</a> <a href="#" class="button abort">Nein</a>'
	);
	return true;
}

function askUserRefreshOrders(oThis) {
	var hasManipulatedOrders = false;
    $$("table.selectModifiedOrders tbody input[type=checkbox]").each(function(el)
    {
      if (el.checked) {
      	hasManipulatedOrders = true;
      }
    })
    
    if (hasManipulatedOrders) {
    	var winName = 'refreshOrders';
    	var cloneName = dragWindowCloneId(winName);
		if ($(cloneName) && !$(cloneName).hasClassName('hidden')) {
			return true;
		}
		else {
			messageBoxCreate(
				winName,
				'ACHTUNG!',
				'Es wurden bereits Daten geaendert oder eingegeben, sollen alle Eingaben verworfen werden?',
				'<a href="#" onclick=\"'+getClickCode(oThis)+'\" class="button">Ja</a> <a href="#" class="button abort">Nein</a>'
			);
			return false;
		}
    }
    else {
    	return true;
    }
}

// eva simulation
function doEvaSimulation(oThis)
{
	getForm().action=$F('evaRequestUrl');
	getForm().method="post";
	getForm().submit();
	return false;
}

// initialOutfitter
function posControl(oThis) {
	var id = oThis.identify();
	
	var ddlA = null;
	var ddlB = null;
	var ddlC = null;
	
	switch (id) {
		case "pos1" : {
			ddlA = document.getElementById('pos2');
			ddlB = document.getElementById('pos3');
			ddlC = document.getElementById('pos4');
			break;
		}
		case "pos2" : {
			ddlA = document.getElementById('pos1');
			ddlB = document.getElementById('pos3');
			ddlC = document.getElementById('pos4');
			break;
		}
		case "pos3" : {
			ddlA = document.getElementById('pos1');
			ddlB = document.getElementById('pos2');
			ddlC = document.getElementById('pos4');
			break;
		}
		case "pos4" : {
			ddlA = document.getElementById('pos1');
			ddlB = document.getElementById('pos2');
			ddlC = document.getElementById('pos3');
			break;
		}
	}
	
	if (ddlA && ddlB && ddlC) {
		var newValue = oThis.value;
		if (ddlA.value == newValue) {
			ddlA.value = getMissingPos(ddlA.value, ddlB.value, ddlC.value);
		} else if (ddlB.value == newValue) {
			ddlB.value = getMissingPos(ddlA.value, ddlB.value, ddlC.value);
		} else if (ddlC.value == newValue) {
			ddlC.value = getMissingPos(ddlA.value, ddlB.value, ddlC.value);
		}
	}
}
function getMissingPos(a, b, c) {
	if (a != 1 && b != 1 && c != 1) {
		return 1;
	} else if (a != 2 && b != 2 && c != 2) {
		return 2;
	} else if (a != 3 && b != 3 && c != 3) {
		return 3;
	} else if (a != 4 && b != 4 && c != 4) {
		return 4;
	} else {
		return 1;
	}
}