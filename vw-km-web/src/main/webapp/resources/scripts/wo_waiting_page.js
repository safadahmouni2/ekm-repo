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

}
