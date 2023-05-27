function actDate(inputID, comboBox) {
	var date = new Date();
	var day = date.getDate();
	var month = date.getMonth()+1;
	var year = date.getFullYear();
	var dayFiller = ((day < 10) ? "0" : "");
	var monthFiller = ((month < 10) ? "0" : "");
	
	if( comboBox.checked){
		document.getElementById(inputID).value = dayFiller + day + "." + monthFiller + month + "." + year;
	}
	else {
	  document.getElementById(inputID).value = "";
	}
}

function calc() {
	var remainingValueF;
	var netPriceF;
	var netPrice = document.getElementById('vehicleNetPrice').value;
	if(netPrice == ""){
		netPrice = "0";
	}
	// alert(netPrice);
	netPrice = netPrice.replace(".","");
	netPrice = netPrice.replace(",",".");
	netPriceF = parseFloat(netPrice);
	// alert(netPriceF);

	var partPaymentAmount = document.getElementById('idPartPaymentAmountInput').value;
	if(partPaymentAmount == ""){
		partPaymentAmount = "0";
	}
	// alert(partPaymentAmount);
	partPaymentAmount = partPaymentAmount.replace(".","");
	partPaymentAmount = partPaymentAmount.replace(",",".");
	var partPaymentAmountF = parseFloat(partPaymentAmount);
	// alert(partPaymentAmountF);
	
	var advanceReceivedAmount = document.getElementById('idAdvanceReceivedAmountInput').value;
	if(advanceReceivedAmount == ""){
		advanceReceivedAmount = "0";
	}
	// alert(advanceReceivedAmount);		
	advanceReceivedAmount = advanceReceivedAmount.replace(".","");
	advanceReceivedAmount = advanceReceivedAmount.replace(",",".");
	var advanceReceivedAmountF = parseFloat(advanceReceivedAmount);
	//alert(advanceReceivedAmountF);
	
	remainingValueF = netPriceF - partPaymentAmountF - advanceReceivedAmountF;
	remainingValueF = Math.round(remainingValueF * 100) / 100;
	// alert(remainingValueF);
	
	var price = String(remainingValueF);
	price = price.replace(".",",");
	if(price.indexOf(",") == -1){
		price = price + ",00";
	}
	// alert(price);		
	
	document.getElementById('idRemainingAmountAmountInput').value = formatPrice(price);
}

function typing(obj) {
	var value = obj.value;
	var len = 0;
	// alert(value);
	
	// only numbers
	//value = value.replace(/[^\.^,\d]/g, '');
	value = value.replace(/[^\d]/g, '');
	// alert(value);
	len = value.length

	while(value.length > 1 && value.substring(0,1) == "0"){
		value =  value.substring(1,len);
	}

	len = value.length
	if(len == 0){
		value = "";
		//value = "0,00";
	}
	else if(len == 1){
		value = "0,0" + value;
	}else if(len == 2){
		value = "0," + value;
	}else{
		value = value.substring(0,len-2) + "," + value.substring(len-2,len);
	}

	obj.value = formatPrice(value);
}

function formatPrice(price){

	var parts;
	var dotVal="";
	
	if(price != null && price.length>0){
		parts = price.split(",");
		parts[0] = parts[0].replace(".","");
		len = parts[0].length;
		
		while(len > 0){ 
	    	if(len - 3 > 0) {
	        	dotVal = "." + parts[0].substring( len - 3, len) + dotVal; 
	        }else {
	        	dotVal = parts[0].substring(0, len) + dotVal; 
	        }
	        len -= 3; 
		} 

		parts[1] = parts[1] + "00";
		parts[1] = parts[1].substring(0, 2);
		price = dotVal + "," + parts[1];		
	}
	return price;
}
