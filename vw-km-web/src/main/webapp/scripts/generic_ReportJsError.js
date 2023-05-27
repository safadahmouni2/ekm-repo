
// IE & compatibles
window.onerror=fnNowReportJsError;
// Netscape & compatibles
window.onError=fnNowReportJsError;

var g_now_errorReporting_url = "";

function fnNowReportJsError(msg, url, line)
{
  now_debug_out ("<div class='error'>Javascript-ERROR:<br />\n<ul>\n <li>line: <b>" + line + "</b></li>\n <li>msg: <b>" + msg + "</b></li>\n</ul></div>",1000,"A");
  addBodyClasses('has_scriptError');

  return false; // show error alert
  return true; // no alert error

	var hr = window.location.href;       // URL of current page
  if ((!g_now_errorReporting_url || "" == g_now_errorReporting_url))
  {
    var strDomain = hr.split("/NewadaOnWeb/")[0];
    g_now_errorReporting_url = strDomain + "/NewadaOnWeb/Common/gif/Trans.gif";
  }
  if (g_now_errorReporting_url) 
  {
	  var appcode;  // Codename of browser
	  var app;      // Appname of browser
	  var ver;      // OS and version of browser
	  var usr;      // Programmheader in HTTP
	  var wl;       // URL and query string of the server-side script 
	                // which processes the error
	  var qs;       // Query String der beim Aufruf dieser Seite benutzt wird

	  // Save current URL
	  qs=window.location.search;
	  if (qs.indexOf('JsError=return')==-1)
	  {
	    appcode=navigator.appCodeName;
	    app=navigator.appName;
	    ver=navigator.appVersion;
	    usr=navigator.userAgent;
	    wl=g_now_errorReporting_url+"?JsErrorUrl="+escape(url);
	    wl+="&JsErrorLine="+escape(line);
	    wl+="&JsErrorMsg="+escape(msg);
	    wl+="&JsErrorAppcode="+escape(appcode);
	    wl+="&JsErrorApp="+escape(app);
	    wl+="&JsErrorVer="+escape(ver);
	    wl+="&JsErrorUsr="+escape(usr);

		// Fetch the dummy image with the error information
	  // spaeter:  var errorImage = new Image(1,1);
	  // spaeter:  errorImage.src = wl;
	  }
	}
  // Don't suppress the browsers error default error handling
  return false;
}
