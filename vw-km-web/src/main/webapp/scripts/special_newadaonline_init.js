
var theTeam = "Stephan,Thomas,Bernd,Werner,Thomas,Frank,Shpendi,Heissam,Rene,Michael";

var myTeamTicker = null;
var curTeamIndex = -1;
var orgTextSternUrkunde = "";

function NewadaOnline_Init()
{

}

var aTheTeam = null;
function fnShowTeamEntry (index)
{
  if (aTheTeam)
  {
    if (index < 0)
    {
      index = 0;
    }
    var maxIndex = aTheTeam.length-1;
    if (maxIndex >= 0)
    {
      if (index > maxIndex)
      {
        index = 0;
        if (myTeamTicker) myTeamTicker.stop();
  	    $("contentHeadline").innerHTML = orgTextSternUrkunde;
        return;
      }
      curTeamIndex = index;
	    $("contentHeadline").innerHTML = "newadaWeb created by: <strong>" + aTheTeam[curTeamIndex] + "</strong>";
	  }
	}
}

function fnShowNextTeamEntry ()
{
  fnShowTeamEntry (curTeamIndex+1);
}


function ShowTeamSlide(dropElem)
{
  curTeamIndex = -1;
  orgTextSternUrkunde = $("contentHeadline").innerHTML;
  aTheTeam = theTeam.split(",");
  $("contentHeadline").update ("newadaWeb -  <strong>Das Team:</strong>");
 	myTeamTicker = new PeriodicalExecuter(fnShowNextTeamEntry, 0.6);
}
