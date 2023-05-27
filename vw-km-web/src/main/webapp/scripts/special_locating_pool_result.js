
// --------------------------------------------------------------
// ordercheck: aehnliche Fahrzeuge aus dem location pool
// --------------------------------------------------------------

function locatingPoolResultOnclick (oThis)
{
  cssNames = oThis.className;
  if (cssNames)
  {
    var position = getClassValue (cssNames, 'pos_');
    var selector = ".pool div.content ul li span.pos_"+position;
    removeBodyClasses ("pos_1 pos_2 pos_3 pos_4 pos_5 pos_6 pos_7 pos_8");
    addBodyClasses ("pos_" + position);
    var lockState = "";
    $$(selector + " input").each (function(el)
    {
      el.checked = true; 
      lockState = "status_" + getClassValue (el.className, 'status_');
    })
    locatingPoolSyncButtons (lockState);
  }
  return true;
}

//wird nur onload, nicht onlick ausgeführt
function locatingPoolSyncRadioButtons ()
{
  var isAnyRadioButtonChecked = false;
  //var lockState = "";

  $$('.pool span.radio input').each(function(radio)
  {
    var clsPos ="";
    if (radio.checked)
    {
      isAnyRadioButtonChecked = true;

      //Position (1-8) des RadioButtons ermitteln

      var theSpan = radio.up('span'); //span, in welchem der radiobutton embedded ist
      var position = getClassValue (theSpan.className, 'pos_');

      if (position >= 1)
      {
        locatingPoolResultOnclick (theSpan);
		  }

      // wenn ein RadioButton gecheckt ist, muss die Spalte des Fzg. markiert werden
      addBodyClasses ("pos_" + position)

    }
  })

  // wenn keiner der RadioButtons gecheckt ist können alle ActionButtons disabled werden
  if (!isAnyRadioButtonChecked)
  {
    locatingPoolSyncButtons ("disableButtons"); //css klasse kann nicht gefunden werden, also werden alle Buttons disabled
  }
}


function locatingPoolSyncButtons (lockState)
{
	$$('.buttonshead input').each(function(inp)
  {
       //local_alert (lockState);
    if (!inp.hasClassName(lockState))
    {
      inp.addClassName('button-disabled');
      inp.onclick = function(){return false;};
      inp.addClassName("cursorDefault");
      SetDisabled(inp, true);
    }
    else
    {
      inp.removeClassName('button-disabled');
      inp.onclick = function(){return true;};
      inp.addClassName("cursorPointer");
      SetDisabled(inp, false);
    }
    SetButtonCssClassById(inp.identify());
  })
}

function locatingPoolResultInit ()
{
  locatingPoolSyncRadioButtons ();
	$$('.pool div.content ul li span').each(function(el)
  {
    clsPos = el.className || "";
    if (clsPos.indexOf ("pos") >= 0)
    {
		  el.addClassName("cursorPointer");
		  el.onclick = function()
      {
        return locatingPoolResultOnclick (this);
		  }
		}
  })
}

