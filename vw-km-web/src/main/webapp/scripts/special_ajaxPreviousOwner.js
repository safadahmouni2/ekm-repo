
// define here the editable purchaseReasons
//var notEditablePurchaseReasons = new Array("11","31");
var notEditablePurchaseReasons = new Array("31");

var prevOwn_table_tr = "table.prevOwnTable tbody tr";

// start initilization
function SpecialPageBehaviourPreviousOwner(){
	init();
}

function init() {
	//alert("init()");
	
	$$ (prevOwn_table_tr).each(function(el)
	{
		//alert ("row");
		var inputKaufGrund = $(el.getElementsByClassName('renderer_assumeKaufGrund', el.down("td")));		
		//alert("inputKaufGrund: "+inputKaufGrund);
		
		if( inputKaufGrund != null && inputKaufGrund != "" 
			&& inputKaufGrund.length == 1 && inputKaufGrund[0].value != ""
			&& isNotEditablePurchaseReason(inputKaufGrund[0].value.substr(0,2))) {

			// alert("31");
			var index = getIdIndex(inputKaufGrund[0]);
			// disable fields
			var nextField;
			nextField = $("id_prevOwnHSN" + index);
			if(nextField){nextField.setAttribute ("disabled", "true");}
			nextField = $("id_prevOwnTSN" + index);
			if(nextField){nextField.setAttribute ("disabled", "true");}
			nextField = $("id_prevOwnBuildYear" + index);
			if(nextField){nextField.setAttribute ("disabled", "true");}
		}
	})
}

function assumeKaufGrund(listObj, oThis){
	// alert(oThis.id);

	// set kaufGrund and descriptin in kaufGrund-field
	$(oThis).value = listObj.innerHTML;
	
	var val1 = (listObj.innerHTML.split(' : '))[0];
	// alert(val1);

	var index = getIdIndex(oThis);
			
	//if( val1 == "31" ){
	if( isNotEditablePurchaseReason(val1) ){
		//alert("sperren!");
		disableFields(index);
	}
	else {
		//alert("nicht sperren!");		
		// set HSN, TSN and buildYear as required
		enableFields(index);
	}
}

function disableFields(index){
	// remove required from HSN, TSN and buildYear input-fields
	removeRequireds(index);
			
	// disable and clear HSN content
	var nextField;
	nextField = $("id_prevOwnHSN" + index);
	if (nextField){
		nextField.setAttribute ("disabled", "true");
		nextField.value = "";
	}
	
	nextField = $("id_prevOwnHSN_Description" + index);
	if (nextField){nextField.innerHTML = "";}
	
	nextField = $("hiddenFahrzeugHersteller" + index);
	if (nextField){nextField.value = "";}
	
	// disable and clear TSN content	
	nextField = $("id_prevOwnTSN" + index);
	if (nextField){
		nextField.setAttribute ("disabled", "true");
		nextField.value = "";
	}
	nextField = $("id_prevOwnTSN_Description" + index);
	if (nextField){nextField.innerHTML = "";}		
	nextField = $("hiddenHandelsBezeichnung" + index);
	if (nextField){nextField.value = "";}
	
	// disable and clear buildYear		
	nextField = $("id_prevOwnBuildYear" + index);
	if (nextField){
		nextField.setAttribute ("disabled", "true");
		nextField.value = "";
	}
}

function enableFields(index){
	nextField = $("id_prevOwnHSN" + index);
	if (nextField){nextField.addClassName ("required");}
	nextField = $("id_prevOwnTSN" + index);
	if (nextField){nextField.addClassName ("required");}
	nextField = $("id_prevOwnBuildYear" + index);
	if (nextField){nextField.addClassName ("required");}
		
	// enable HSN
	nextField = $("id_prevOwnHSN" + index);
	if (nextField){nextField.removeAttribute ("disabled");}

	// enable TSN		
	nextField = $("id_prevOwnTSN" + index);
	if (nextField){nextField.removeAttribute ("disabled");}
	
	// enable buildYear		
	nextField = $("id_prevOwnBuildYear" + index);
	if (nextField){nextField.removeAttribute ("disabled");}
}

function jumpPurchaseReason(event, oThis) {
	event = event || window.event;

	if( event && event.keyCode == 9 ){
		var suggBox = $('suggestionBox');
		if( IsDefined(suggBox) && IsVisibleElement(suggBox.down('.autoCompleteMenu'))) {
			var oSelected = suggBox.down('li.selected');		
			var oFirst = $$('#suggestionBox li').first();
			if(IsDefined(oSelected)){
				oThis.value = oSelected.innerHTML;
			}
			else if(IsDefined(oFirst)){
				oThis.value = oFirst.innerHTML;
			}
		}
		var val1 = (oThis.value.split(' : '))[0];
		var index = getIdIndex(oThis);		
		//if( val1 == "31" ){
		if( isNotEditablePurchaseReason(val1) ){
			//alert("sperren!");
			disableFields(index);
		}
		else {
			//alert("nicht sperren!");		
			// set HSN, TSN and buildYear as required
			enableFields(index);
		}
	}
}

function jumpHSN(event, oThis) {
	event = event || window.event;
	
	if( event && event.keyCode == 9 ){
		var suggBox = $('suggestionBox');
		if( IsDefined(suggBox) && IsVisibleElement(suggBox.down('.autoCompleteMenu')) ) {

			var oSelected = suggBox.down('li.selected');
			var oFirst = $$('#suggestionBox li').first();

			if (IsDefined(oSelected)) {
				mouse_handling_onclick(oSelected, oThis);
			}
			else if(IsDefined(oFirst)){
				mouse_handling_onclick(oFirst, oThis);				
			}			
		}
	}
}

function jumpTSN(event, oThis) {
	event = event || window.event;
	
	if( event && event.keyCode == 9 ){
		var suggBox = $('suggestionBox');
		if( IsDefined(suggBox) && IsVisibleElement(suggBox.down('.autoCompleteMenu')) ) {

			var oSelected = suggBox.down('li.selected');
			var oFirst = $$('#suggestionBox li').first();
			
			if (IsDefined(oSelected)) {
				mouse_handling_onclick(oSelected, oThis);
			}
			else if(IsDefined(oFirst)){
				mouse_handling_onclick(oFirst, oThis);
			}
		}
	}
}

function assumeHsnValue(listObj, field){
	setValuePrevOwn(field, listObj, "id_prevOwnHSN_Description");
	// FahrzeugHersteller in hiddenField für Vnw0t00vBOWrapper setzen	
	var val2 = (listObj.innerHTML.split(' : '))[1];
	var nextField = getNextField(field,"hiddenFahrzeugHersteller");
	if (nextField){nextField.value = val2;}
}

function assumeTsnValue(listObj, field){
	setValuePrevOwn(field, listObj, "id_prevOwnTSN_Description");
	
	// HandelsBezeichnung in hiddenField für Vnw0t00vBOWrapper setzen	
	var val2 = (listObj.innerHTML.split(' : '))[1];
	var nextField = getNextField(field,"hiddenHandelsBezeichnung");
	if (nextField){nextField.value = val2;}
}

function setValuePrevOwn(oThis, listObj, nextFieldName){
	var val1 = (listObj.innerHTML.split(' : '))[0];
	// alert("val1="+val1);
	var val2 = (listObj.innerHTML.split(' : '))[1];
	// alert("val2="+val2);

	oThis.value = val1;
	// löscht rotes Ausrufezeichen		
	setValidChecked(oThis);

	var nextField = getNextField(oThis,nextFieldName);
	// alert("nextField="+nextField);
	if (nextField){nextField.innerHTML = val2;}
}

function getNextField(oThis,nextFieldName){
	var nameAndId = oThis.id.split("[");
	
	nextFieldName = nextFieldName.concat("[");
	nextFieldName = nextFieldName.concat(nameAndId[1]); // add id-part e.g. "5]"

	var nextField = $(nextFieldName);
	
	return nextField;
}

// get the index of an ID, e.g. "myColumnField[6]": result = '[6]'
function getIdIndex(oThis){
	var index;
	var nameAndId = oThis.id.split("[");
	index = "[" + nameAndId[1];
	return index;
}

function showValues(oThis, elementName, descElementName) {
	//var suggBox = $('suggestionBox');
	var index = getIdIndex(oThis);

	// remove required from HSN, TSN and buildYear input-fields
	if( oThis.id.indexOf("id_comboArrowKaufGrund") > -1 ){
		removeRequireds(index);
		enableFields(index);
	}

	// kein HSN, TSN bei kaufgrund 31
	if (elementName == "id_prevOwnHSN" || elementName == "id_prevOwnTSN") {
		var val = $("id_prevOwnKaufGrund" + index).value;
		if( val != "" && isNotEditablePurchaseReason( val.substr(0,2)) ){
			return;
		}
	}

	var index = getIdIndex(oThis);
	var element = $(elementName+index);
	if(element){element.value = "";}

	if(elementName.indexOf("HSN") > -1){
		// clear HSN values!
		var hsnDescElement = $('id_prevOwnHSN_Description'+index);
		if(hsnDescElement){hsnDescElement.innerHTML = "";}
		var hsnHiddenElement = $('hiddenFahrzeugHersteller'+index);
		if(hsnHiddenElement){hsnHiddenElement.value = "";}		
	}
	
	if(elementName.indexOf("HSN") > -1 || elementName.indexOf("TSN") > -1){
		// clear TSN values!
		var tsnElement = $('id_prevOwnTSN'+index);
		if(tsnElement){tsnElement.value = "";}
		var tsnDescElement = $('id_prevOwnTSN_Description'+index);
		if(tsnDescElement){tsnDescElement.innerHTML = "";}
		var tsnHiddenElement = $('hiddenHandelsBezeichnung'+index);
		if(tsnHiddenElement){tsnHiddenElement.value = "";}		
	}

	ajaxAutoSuggestAction('', element);
	SetFocus (elementName + index);
}

// remove required from HSN, TSN and buildYear input-fields
function removeRequireds(index) {
	var nextField;
	
	nextField = $("id_prevOwnHSN" + index);
	if(nextField){nextField.removeClassName ("required");}
	// löscht rotes Ausrufezeichen
	setValidChecked(nextField);
	
	nextField = $("id_prevOwnTSN" + index);		
	if(nextField){nextField.removeClassName ("required");}
	// löscht rotes Ausrufezeichen
	setValidChecked(nextField);
	
	nextField = $("id_prevOwnBuildYear" + index);
	if(nextField){nextField.removeClassName ("required");}
	// löscht rotes Ausrufezeichen		
	setValidChecked(nextField);
}

function handleHsnTsn(oThis, extraParams) {
	var index = getIdIndex(oThis);
	var objToRefer = $(document.getElementById("id_prevOwnHSN"+index));
	if (IsDefined(objToRefer)) {
		var hsnValue = $F(objToRefer);
		if(hsnValue != ''){
			extraParams.set('ajaxReferHSN', hsnValue);
			var objToRefer = $(document.getElementById("id_prevOwnHSN_Description"+index));
			if (IsDefined(objToRefer)) {
				var hsnDescValue = objToRefer.innerHTML;
				if(hsnDescValue != ''){
					extraParams.set('ajaxReferHSNDescription', hsnDescValue);
				}
			}
		} 
		else {
			alert("Bitte zuerst HSN erfassen.");
		}
	}
	return extraParams;
}

function isNotEditablePurchaseReason(purchaseReason) {
	//alert("purchaseReason= "+purchaseReason);
	var ret = false;
	if( purchaseReason != "" ){
		var len = notEditablePurchaseReasons.length;
		for (var i=0; i<len; ++i) {
			if( purchaseReason == notEditablePurchaseReasons[i] ){
				ret = true;
				break;
			}
		}	
	}
	//alert("ret= "+ret);
	return ret;
}
