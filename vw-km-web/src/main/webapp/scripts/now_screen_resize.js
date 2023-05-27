
function screenResizeInit (){
  oHeader = $('areaHeader');
  var inVis = $(oHeader).invis;
  browser_detect_Init ();
   /* PTS change 26651:ABA : Optimierung 1: Der obere Rand mit „Volkswagen Aktiengesellschaft“ soll initial zugeklappt sein 
	*Initially the header should be closed
	*/
  if(inVis==0)	{
	  ResetContentHeight() ;
   }
  else {
	  IncreaseContentHeight();
  }
  if (sessionStorage.getItem('leftSideWidth') != null) {
	ice.ace.jq( "#f\\:rightSide" ).css({"margin-left" : sessionStorage.getItem('leftSideWidth')});
	ice.ace.jq( '#f\\:leftSide' ).css({"width" : sessionStorage.getItem('leftSideWidth')});
  }
  
  //set Last scroll position
    setTimeout(function(){SetScrollPos();},50);
  	   
}


function SetInitScrollPos(){sessionStorage.setItem("scroll",0);}

function SetScrollPos() {
	
	var div = document.getElementById('f:dividerpanelFirst');
	if (div) {
		
		if (!div.addEventListener) {
			    div.attachEvent('onscroll', 
			    		function() {sessionStorage.setItem("scroll",div.scrollTop);}, false);

		}

		else {  div.addEventListener("scroll", 
					   function() {sessionStorage.setItem("scroll", div.scrollTop);}, false);

		}		
		div.scrollTop=parseInt(sessionStorage.getItem("scroll"));
	}// End if element exist

}// End function

function ResetContentHeight(){	
	 var calculatedHeight = getDocumentHeight() - 120 ;
	 if($('f:dividerpanel')){
	 $('f:dividerpanel').setStyle ({height: calculatedHeight+'px'});	}
	 return calculatedHeight;
	
}

function IncreaseContentHeight(){	
	var calculatedHeight=0;
	if (IsBrowserVersion("IE7"))  calculatedHeight = getDocumentHeight() - 65 ;
	else calculatedHeight = getDocumentHeight() - 60 ;
		
	if($('f:dividerpanel')){
	 $('f:dividerpanel').setStyle ({height: calculatedHeight+'px'});}	
	return calculatedHeight;
}


function costComponetDeviceResize (){
	if($('asdForm:dependentDeviceAssigning') != null){

	 calculatedHeighthdiv = $('asdForm:dependentDeviceAssigning').getHeight() - 50 ;
	 $('asdForm:dependentDeviceDivider').setStyle ({height: calculatedHeighthdiv+'px'},{overflow:'auto'});
	 
	 $('asdForm:dividerpanelelement').setStyle ({height: calculatedHeighthdiv+'px'},{overflow:'auto'});
   }
}

function initCostComponetDeviceSize (){
	if($('asdForm:dependentDeviceAssigning') != null){
	 var documentHeight = $('f:dividerpanel').getHeight() ;	
	 var documentWidth = $('f:dividerpanel').getWidth() ;
	 calculatedHeightdiv = documentHeight - 10 ;
	 calculatedWidthtdiv = documentWidth - 100 ;	 
	 $('asdForm:dependentDeviceAssigning').setStyle ({height: calculatedHeightdiv+'px'});	 
	 $('asdForm:dependentDeviceAssigning').setStyle ({width: calculatedWidthtdiv+'px'});	 
	 
	 var calculatedHeightdiv2 = calculatedHeightdiv - 50;
	 $('asdForm:dependentDeviceDivider').setStyle ({height: calculatedHeightdiv2+'px'});	 
	 $('asdForm:dividerpanelelement').setStyle ({height: calculatedHeightdiv2+'px'});
   }
}

function scrollToSelectedNode(){
	//alert('scrollToSelectedNode');
	if($$(".iceCmdLnk.selectedNodetrue") != null && $$(".iceCmdLnk.selectedNodetrue") != "") {
		//alert('scrollToSelectedNode' + 1);
	  var item = $$(".iceCmdLnk.selectedNodetrue")[0].id;
	  oElem = $(item);
	  var pos = oElem.positionedOffset()[1];
	  var pos2 = oElem.cumulativeOffset()[1]
      pos = pos2 -150;
	  $("f:dividerpanelFirst").scrollTop = pos;
	  sessionStorage.setItem("scroll",pos);
	}
}

function xmlExportModelResize (){
	
	if($('exportXMLPopup_main') != null){
	 var calculatedPopupHeighth = $('exportXMLPopup_main').getHeight() ;
	 var documentHeight = $('f:dividerpanel').getHeight() ;
	 if(documentHeight < calculatedPopupHeighth) {	 
		 calculatedPopupHeighth = documentHeight;
		 $('exportXMLd').setStyle ({height: calculatedPopupHeighth+'px'});
	 }	 
   }
	
   if ($('exportXMLComponentPopup_main') != null) {
	var calculatedPopupHeighth = $('exportXMLComponentPopup_main').getHeight();
	var documentHeight = $('f:dividerpanel').getHeight();
	if (documentHeight < calculatedPopupHeighth) {
		calculatedPopupHeighth = documentHeight;
		$('exportXMLdc').setStyle({height : calculatedPopupHeighth + 'px'});
	}
   } 	
}