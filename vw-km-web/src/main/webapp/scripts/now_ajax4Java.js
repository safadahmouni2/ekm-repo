
function ajax4Java_Init()
{
  	$$(".ajaxTableUpdate").each(function(input) {
	  var table = $(input).up('body').down('table.ajaxupdate');	
	  if(table){
		  var src = getClassValue(table.className,'src_');
		//  alert('src: '+src);
		  input.onkeyup = function() {
		       addBodyClasses("loading");
			   var paramvalue =  getBody().down("form").serialize();
			   new Ajax.Request(src+'.jsf', {
				    method:'get', 
				    parameters: {params: paramvalue},
				    onSuccess: function(transport){
				     	var response = transport.responseText || "no response text";
				      	var tbody = transport.responseText.match(/<tbody[\s\S]*<\/tbody>/g);
				      	$$(".ajaxupdate > tbody").first().replace(tbody);
				      	removeBodyClasses("loading");
				    },
				    onFailure: function(){ alert('Something went wrong...') }
				});
			}
		}
	 })
	 
	 
	
}

function editRow(check, index){	
	var checked = $(check).hasClassName('checked');
	if(!checked){
		$(check).addClassName('checked');
		var tr = $(check).up('tr');
		tr.addClassName('selected');
		var tds = tr.childElements();
		tds.each(function(td){
			var input = td.down('input');
			if(input && !$(input).hasClassName('checkbox')){
				$(input).removeClassName('notEditable');
				input.default=input.value;
				input.disabled=false;
			}			
		})
	}else{
		$(check).removeClassName('checked');
		var tr = $(check).up('tr');
		tr.removeClassName('selected');
		var tds = tr.childElements();
		tds.each(function(td){
			var input = td.down('input');
			if(input && !$(input).hasClassName('checkbox')){
				$(input).addClassName('notEditable');
				input.value=input.default;
				input.disabled=true;
			}
		})
	}
	
	var checkedFields = $(check).up('tbody').down('input.checked');
	var body = $(check).up('body')
 	if(checkedFields){
		body.down('.saveButton').disabled=false;
		body.down('.newButton').disabled='disabled';
		body.down('.copyButton').disabled=false;
		body.down('.deleteButton').disabled=false;
	}else{
		body.down('.saveButton').disabled='disabled';
		body.down('.newButton').disabled=false;
		body.down('.copyButton').disabled='disabled';
		body.down('.deleteButton').disabled='disabled';
	}
}
