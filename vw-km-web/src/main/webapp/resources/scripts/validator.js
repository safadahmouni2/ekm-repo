/*
 *********************************************************
 * FUNCTION: mouseOver();
 *
 * 
 *********************************************************
 */
function validateBaseBigDecimal(field, message, size) {
	if (field.value != "") {
		var x = field.value;
		if (x.indexOf('\-') > -1) {
			field.className = field.className + " validation-error";
			field.title = message;
			field.focus();
			return false;
		}

		if (x.indexOf(',') > -1) {
			if ((x.length - 1) == x.indexOf(',')) {
				field.className = field.className + " validation-error";
				field.title = message;
				field.focus();
				return false;
			}
			if ((x.substring(0, x.indexOf(',') - 1)).length > size) {
				field.className = field.className + " validation-error";
				field.title = message;
				field.focus();
				return false;
			}
		} else {
			if (x.length > size) {
				field.className = field.className + " validation-error";
				field.title = message;
				field.focus();
				return false;
			}
		}
		field.value = x;
		x = x.replace(',', '.');
		if (x != parseFloat(x)) {
			field.className = field.className + " validation-error";
			field.title = message;
			field.focus();
			return false;
		}

	}
	field.title = "";
	field.className = field.className.replace(/ validation-error/g, "");
	return true;
}


function validateCurrency( field, message )
{
   var temp_value = field.value;
   var isCurrency_re = /^(?!0\.?\d)(((\d+)(\.\d{3})*)|(\d+))(\,\d{1,2})*?$/;
   if (temp_value != ""){
   	    var isCurrency=String(temp_value).search (isCurrency_re);
   	    if(isCurrency==-1){
			field.className = field.className + " validation-error";
			field.title = message;
			field.focus();
			return false;
   	    }
   }
	field.title = "";
	field.className = field.className.replace(/ validation-error/g, "");
	return true;
} 




function validateRequiredNumber(field, message) {
	if (field.value == "") {
		field.className = field.className + " validation-error";
		field.title = message;
		field.focus();
		return false;
	} else {
		var x = field.value;
		if (x.indexOf('\-') > -1) {
			field.className = field.className + " validation-error";
			field.title = message;
			field.focus();
			return false;
		}

		field.value = x;
		if (x != parseInt(x)) {
			field.className = field.className + " validation-error";
			field.title = message;
			field.focus();
			return false;
		}

	}
	field.title = "";
	field.className = field.className.replace(/ validation-error/g, "");
	return true;
}

function textCounter(field, cntfield, maxlimit, error_container, msg) {
	if (field.value.length > maxlimit) { // if too long...trim it!
		field.value = field.value.substring(0, maxlimit);
		error_container.innerHTML = msg;
		// otherwise, update 'characters left' counter
	} else if (field.value.length == maxlimit) {
		cntfield.innerHTML = maxlimit - field.value.length;
		error_container.innerHTML = msg;
	} else {
		cntfield.innerHTML = maxlimit - field.value.length;
		error_container.innerHTML = '';
	}
}

function validateTextAreaSize(field, msg, maxSize) {
	if (field.value.length > maxSize) { // if too long...trim it!
		field.value = field.value.substring(0, maxSize);
		if (field.title == ""){
			field.className = field.className + " validation-error";
			field.title = msg.replace("##maxSize##", maxSize);
			
		}
		field.scrollTop=field.scrollHeight;
	} else if (field.value.length == maxSize) {
		if (field.title == ""){
			field.className = field.className + " validation-error";
			field.title = msg.replace("##maxSize##", maxSize);
		}
		field.scrollTop=field.scrollHeight;
	} else {
		if (field.title != "") {
			field.title = "";
			field.className = field.className.replace(/ validation-error/g, "");
		}
	}
}
