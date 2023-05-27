function fnAfterUpdateHeadline (effect)
{
  var totalFrames = effect.options.fps * effect.options.duration;
  var offset = Math.ceil (-220 * effect.currentFrame / totalFrames);
  $$('#areaContainer').each(function(el)
  {
    el.setStyle ({'margin-top':offset});
  })
}

function fnBeforeStartHeadline (effect)
{
}

function fnAfterFinishHeadline (effect)
{

 $$('#areaHeader').each(function(el)
 {
 		el.addClassName("cursorPointer");
    // el.title = "Click to re-expand Picture";
 		el.onclick = function()
		{
      new Effect.SlideUp($$('#areaHeader').first().identify(), { duration: 0.3, afterFinish:fnReExpand});
    }
 })

 $$('#areaHeader').first().setStyle ({'margin-top':'0', 'overflow-x':'hidden', width: '188px'});

 new Effect.SlideDown($$('#areaHeader').first().identify(), { duration: 1.2, transition: Effect.Transitions.spring, afterFinish:fnHighLightHeadline });

}

function fnSyncCheckBoxHeadline ()
{
  oHeader = $('areaHeader');
  var inVis = $(oHeader).invis;
  
  if(inVis) {
	 IncreaseContentHeight();
  }
    if (inVis) {
		addBodyClasses("usp_headClosed");
	} else {
		removeBodyClasses("usp_headClosed");
	}
	$('areaContainer').setStyle( {
		top : 0
	});
}

function HeadlineBehaviourToggleClick (oThisClick)
{
  oThis = $$('#areaHeader').first();

  var hHead = $(oThis).getHeight() -10;

  if (!IsBrowserVersion("W3C"))
  {
    hHead -= 10;

  }
  if (Prototype.Browser.WebKit)
  {
    hHead -= 10;

  }
  var inVis = $(oThis).invis;
  
  /*PTS change 26651:ABA : Optimierung 1: Der obere Rand mit „Volkswagen Aktiengesellschaft“ soll initial zugeklappt sein 
	*Initially the header should be closed
	*/
  
  if (inVis==0)
  {	   //  "Click to expand";
	    $(oThis).invis = 1;
	    hHead = -hHead;
  }
  else
  {   // "Click to hide";
	    $(oThis).invis = 0;
	    if (0 == $('areaContainer').getStyle("margin-top"))
	    {
	     fnSyncCheckBoxHeadline ();
	      return false;
	    }
  }

  new Effect.Move($('areaContainer'), { y: hHead, mode: 'relative', duration:0.2, afterFinish:fnSyncCheckBoxHeadline });
  if(inVis) { 
	  wait(300);
	  ResetContentHeight();	    
   }
  return false;
}

function HeadlineBehaviourInit ()
{   /*PTS change 26651:ABA : Optimierung 1: Der obere Rand mit „Volkswagen Aktiengesellschaft“ soll initial zugeklappt sein 
	*Initially the header should be closed
	*/
  	$$('#areaHeader').invis = 0;
  	addBodyClasses("usp_headClosed");
  	IncreaseContentHeight();
  	$$('input#behaviour_HeadVis, #areaHeader').each(function(el)
  {
	  el.addClassName("cursorPointer");
	  el.onclick = function()
    {
      return HeadlineBehaviourToggleClick (this);
    }
  })
}

function wait(msecs) {
	var start = new Date().getTime();
	var cur = start
	while(cur - start < msecs){
		cur = new Date().getTime();
	}
 } 
