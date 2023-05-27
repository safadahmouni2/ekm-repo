
g_js_ajax_enabled = true;

// ================================
function ajaxDecode (response)
{
  return response;
}

// ================================
function ajaxReinit ()
{
  //w3c_selector_compatibility_Init ();
  return false;
}

// ================================
function ajaxSerialize(actionList, extraParams)
{
  var serializedForm = $H();
  
  if (extraParams) {
    var ajClNa = extraParams.get("ajaxClassNames");
    if (ajClNa && ajClNa.indexOf("sendAll") >= 0) {
      serializedForm = getForm().serialize(true);
      serializedForm = $H(serializedForm);
      serializedForm.unset("__VIEWSTATE");
      serializedForm.unset("__EVENTARGUMENT");
      serializedForm.unset("__EVENTTARGET");
    }
    
    try {
      serializedForm.update($H(extraParams));
    } catch(e) {
      alert(e);
      alert($H(extraParams).inspect());
    }
  }
      
  serializedForm.set('ajaxActionList', actionList);
  serializedForm.set('ajaxLanguage',  getBodyClassValue("lang_"));
  return serializedForm;
}

// ================================
function ajaxLoadContent (theUrl, actionList, extraParams)
{
  if(!g_js_ajax_enabled)
  {
    return true;
  }
  
  var serializedForm = ajaxSerialize(actionList, extraParams);
  new Ajax.Request(theUrl,
  {
    method:'get',
    parameters: serializedForm,
    //contentType: 'application/x-www-form-urlencoded; charset=ISO-8859-1',
    evalScripts: true,
    onSuccess: function(transport)
    {
      var response = ajaxDecode (transport.responseText || "");
      ajaxJsonUpdate(response);
    },
    onComplete: function()
    {
      ajaxLoadingFinished();
    },
    onCreate: function()
    {
      //addBodyClasses("loading");
    },
    onFailure: function()
    {
      ajaxLoadingFinished();
    }
  })
  return false;
}

// ================================
function ajaxLoadingFinished()
{
  removeBodyClasses("loading");
  set_modal(false);
  $$("input[type=submit].ajaxFiredButton").each(function(el)
  {
    SetDisabled(el,false);
    el.removeClassName ("ajaxFiredButton");
  })
}

// ================================
function ajaxJsonUpdate(response)
{
  //alert(response);
  if("" != response)
  {
    var jsonRes = "";
    try
    {
      jsonRes = $H(response.evalJSON ());
    }
    catch (e)
    {
      //alert (response);
      jsonRes = $H();
    }
    jsonRes.each (function(pair)
    {
      if("syncBodyClass" == pair.key)
      {
        SyncBodyClass (pair.value);
      }
      else
      {
        if(!$(pair.key))
        {
          if("" != pair.value)
          {
            buildContainer (pair.key);
          }
        }
        if($(pair.key))
        {
          updateContainer (pair.key, pair.value);
          $(pair.key).removeClassName("componentLoading");
        }
      }
    })
    ajaxReinit ();
  }
}

// ================================
function updateContainer (id, value)
{
  if($(id))
  {
    $(id).update (value);
  }
}

// ================================
function SyncBodyClass (syncNames)
{
  var testNames = " " + syncNames + " ";
  var curNames = getBody ().className;
  $w(curNames).each(function(cls)
  {
    if((cls.indexOf("sys_") == 0) && (testNames.indexOf(" "  + cls + " ") < 0))
    {
      removeBodyClasses (cls);
    }
  })
  $w(syncNames).each(function(cls)
  {
    addBodyClasses (cls);
  })
}

// ================================
function buildContainer (id)
{
  $("areaContent").insert({top: "<div id='"+id+"'></div>"});
}

