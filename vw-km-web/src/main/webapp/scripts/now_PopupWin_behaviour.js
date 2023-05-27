
function PopupWin_Behaviour_Init ()
{
	$$('a[rel="external"]').each( function(el) {
 		el.target = "_blank";
	});
}
