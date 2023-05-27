
function Tooltips_Behaviour_Init (selector)
{
	$$(selector).each( function(elem) {
		new Tooltip(elem, {backgroundColor: "#FC9", borderColor: "#C96", textColor: "#000", textShadowColor: "#FFF"});
	});
}
