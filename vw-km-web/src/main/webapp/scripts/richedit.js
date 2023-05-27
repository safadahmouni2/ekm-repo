
function richedit_enable()
{
  if (IsDefined($("richeditScripts")))
  {
    if ("undefined" != typeof (tinyMCE))
    {
      tinyMCE.init({
        mode : "textareas",
        theme : "simple",
        theme_advanced_statusbar_location : "bottom",
        editor_selector : "mceSimpleEditor",
        theme_advanced_resizing : true
      });

      tinyMCE.init({
        mode : "textareas",
        theme : "advanced",
        plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
        advanced_buttons1 : "mybutton,save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect",
        theme_advanced_toolbar_location : "top",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_toolbar_location : "external",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_buttons1_add_before : "mybutton",
        apply_source_formatting : false,
        remove_linebreaks: false,
        save_callback: "saveRicheditContent",
        cleanup_on_startup : true,
	      cleanup: true,
        theme_advanced_resizing : true,

        setup : function(ed) {
          // Add a custom button
          ed.addButton('mybutton', {
            title : 'My button',
            image : '/NewadaOnWeb/common/res/img/example.gif',
            onclick : function() {
              // Add you own code to execute something on click
              ed.focus();
              // ed.selection.setContent('<strong>Hello world!</strong>');
              saveRicheditContent();
            }
          });
        }


      });
    }
    else
    {
      now_debug_out ("ERR: tinyMCE type: " + typeof (tinyMCE));
    }
  }
}

function saveRicheditContent()
{
  var ed = tinyMCE.get("pageHelpEditContent");
  var editTxt = ed.getContent();
  // now_debug_out ("saveRicheditContent=" + ed.getContent());
  // alert ("saveRicheditContent" + editTxt);
  
if (false)
{
  editTxt = editTxt.replace(/&uuml;/g,'ü');
  editTxt = editTxt.replace(/&ouml;/g,'ö');
  editTxt = editTxt.replace(/&auml;/g,'ä');
  editTxt = editTxt.replace(/&szlig;/g,'ß');
  editTxt = editTxt.replace(/&Uuml;/g,'Ü');
  editTxt = editTxt.replace(/&Ouml;/g,'Ö');
  editTxt = editTxt.replace(/&Auml;/g,'Ä');
  editTxt = editTxt.replace(/&bdquo;/g,'"');
  editTxt = editTxt.replace(/&ldquo;/g,'"');
  editTxt = editTxt.replace(/&ndash;/g,'-');
  editTxt = editTxt.replace(/&bull;/g,'*');
}


  var classNames="perform_save ajaxAction_getPageHelp:pageHelpWinId usp_PageHelpWin ajaxReturnType_dragWindow";
  var pageName = getPageId();
  var ajTitle = "Saved";
   // now_debug_out ("calling save pageHelp for page " + pageName);
  var service = getAjaxUrl();
  var extraParams = $H({ajaxClassNames:classNames,ajaxPageId:pageName,ajaxTitle:ajTitle,ajaxFieldId:'pageHelpEditContent',ajaxEditText:editTxt})
  ajaxLoadContent(service, '', extraParams);	

  



  return false;
}

function richedit_load_scripts()
{
  if (!IsDefined($("richeditScripts")))
  {
    now_debug_out ("richedit_load_scripts...");
    var libraryName = getHttpResRoot() + "/tiny/tiny_mce.js"
    now_debug_out ("richedit_load_scripts.lib = " + libraryName);
    getBody().insert("<div id='richeditScripts'><script type=\"text/javascript\" src=\"'+libraryName+'\"></script></div>");
  //  document.write('<script type="text/javascript" src="'+libraryName+'"></script>');
  }
}

