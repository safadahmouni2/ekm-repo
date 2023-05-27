
function Href_Target_Init (relName, targetName)
{
	$$("a[rel='"+relName+"']").each( function(el) {
 		el.target = targetName;
	});
}
