// ajax on a table
function initTableSettings(selector)
{
	$$(selector).each(function(el)
	{
		el.addClassName("ajax sendAll");
		el.setAttribute("autocomplete", "off");
		el.onchange=function(evt) {
			if(!$(this).hasClassName("calendar")) {
				doAjaxTableActionJ(this);
			}
		};
		el.onkeyup=function(evt) {
			doAjaxTableActionJ(this);
		};
		el.onfocus=function(evt) {
			if($(this).hasClassName("calendar")) {
				doAjaxTableActionJ(this);
			}
		};
	})
}
function doAjaxTableActionJ(oThis) {
	$(oThis).addClassName('componentLoading');
	
	var value = $F(oThis);
	var oldValue = oThis.getAttribute('savedValue');
	
	if (hasBodyClass('usp_ajaxFeature') && (!oldValue || (oldValue != value))) {
		oThis.setAttribute('savedValue', value);
		var tbodyId = $$('table.ajaxTable tbody').first().identify();
		ajaxLoadContent (
			getPageName() + '.ajax',
			tbodyId + '=' + tbodyId + '&datascrollerDiv=datascrollerDiv',
			$H({ajaxClassNames:$(oThis).className})
		)
	}
}

