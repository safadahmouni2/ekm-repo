function getDocumentWidth() {
	if (IsBrowser("IE")) {//for IE
		return document.body.scrollWidth;
	} else {
		return (window.innerWidth > document.documentElement.clientWidth) ? window.innerWidth
				: document.documentElement.clientWidth;
	}
}
function getDocumentHeight() {
	if (IsBrowser("IE")) {//for IE
		return document.body.scrollHeight;
	} else {
		return (window.innerHeight > document.documentElement.clientHeight) ? window.innerHeight
				: document.documentElement.clientHeight;
	}
}
function resizeWaitingPage(name) {
	document.getElementById(name).style.width = getDocumentWidth();
	document.getElementById(name).style.height = getDocumentHeight();
}

function stopWait() {
	hideWaitingDiv();
}
function hideWaitingDiv() {
	resizeWaitingPage('wait-box');
	if (document.layers) {
		document.layers['wait-box'].visibility = 'hidden';
		return true;
	}
	document.getElementById('wait-box').style.visibility = "hidden";
	document.getElementById('wait-box').style.cursor = null;
}

function disableButton(buttonId) {
    var obj = document.getElementById(buttonId);
    if (obj) {
        obj.disabled = true;
    }
}


function wait() {
	resizeWaitingPage('wait-box');
	if (document.layers) {
		document.layers['wait-box'].visibility = 'visible';
		return true;
	}
	document.getElementById('wait-box').style.visibility = "visible";
	document.getElementById('wait-box').style.cursor = "wait";
}
document.writeln( "<div id=\"wait-box\"><p>wait...</p><img src=\"../images/now-loader.gif\"/></div>");
with ( document.getElementById("wait-box").style )
{
  visibility = "hidden"; 
  fontFamily = "Arial, sans-serif";
  backgroundColor = "gray";
  position = "absolute"; 
  left = "0px"; 
  top = "0px"; 
  width = "100%"; 
  height = "100%";
  zIndex = "10";
  textAlign = "center"; 
  //paddingTop = "250px";
  opacity = "0.5"; 
  filter = "alpha(opacity=50)";
}
