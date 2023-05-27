
function performBaseRenderer (oThis, ajaxFieldId)
{
	var assumeVal = getClassValue($(ajaxFieldId).className, "assumeVal_");
	if (IsNumeric(assumeVal) && assumeVal > -1) {
		SetValue ($(ajaxFieldId), GetValue(oThis).split(' : ')[assumeVal]);
	} else {
		SetValue ($(ajaxFieldId), GetValue(oThis));
	}
}

function adjustSuggestPosition (target, obj)
{
	clonePosition (target, obj);
}

// spezial renderer, dieser wird verwendet, wenn die
//   ajaxAction_loadPreVehicleList
//		 aufgefufen wird
function assumeloadPreVehicleList (oThis, ajaxFieldId)
{
	SetValue ($(ajaxFieldId), GetValue (oThis.down("b")).substr(0,3));
	$(ajaxFieldId).title = GetValue (oThis.down("span"));
}
