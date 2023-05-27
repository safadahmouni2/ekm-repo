
function messageBoxCreate(msgId, title, content, buttons)
{
	var oMsgWin = $(msgId);
	if (!oMsgWin)
	{
		oMsgWin = dragWindowCreate(msgId, title, content, buttons, "messageBox");
	}
	dragWindowShow(msgId, false);
	return oMsgWin;
}

function getClickCode(oThis) {
	if (oThis.tagName.toUpperCase() == 'A') {
		return "return " + oThis.readAttribute("onclick").split(';return ')[1];
	}
	else {
		return "$('"+$(oThis).identify()+"').click();";
	}
}
