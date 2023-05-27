function browser_detect_Init ()
{
  var version = "?";
  var keyVers = "?";
  var pos = -1;
  var usrAgent = navigator.userAgent.toLowerCase();
  if (Prototype.Browser.IE)
  {
    Prototype.Browser.KeyName = "IE"
    pos = usrAgent.indexOf('msie ');
    if (pos > -1)
    {
      version = usrAgent.substr(pos + 5);
      version = $A(version.split(';'))[0],
      keyVers = version.substr(0,1);
    }
  }
  else if (Prototype.Browser.Gecko)
  {
    pos = usrAgent.indexOf('firefox/');
    if (pos > -1)
    {
      Prototype.Browser.KeyName = "Firefox"
      version = usrAgent.substr(pos + 8);
      keyVers = version.substr(0,3);
    }
    else
    {
      Prototype.Browser.KeyName = "Gecko"
    }
  }
  else if (Prototype.Browser.WebKit)
  {
    Prototype.Browser.KeyName = "WebKit"
  }
  else if (Prototype.Browser.Opera)
  {
    Prototype.Browser.KeyName = "Opera"
  }
  else if (Prototype.Browser.MobileSafari)
  {
    Prototype.Browser.KeyName = "MobileSafari"
  }
  else
  {
    Prototype.Browser.KeyName = "Other"
  }
  Prototype.Browser.Version = Prototype.Browser.KeyName + "_" + version;
  Prototype.Browser.KeyVersion = Prototype.Browser.KeyName + keyVers;
}

function IsBrowser(check)
{
  return Prototype.Browser.KeyName == check;
}

function IsBrowserVersion(check)
{
  switch (check) {
    case "W3C":
      return !("IE6" == Prototype.Browser.KeyVersion || "IE7" == Prototype.Browser.KeyVersion);
      break;
    case "IE_6-7":
      return "IE6" == Prototype.Browser.KeyVersion || "IE7" == Prototype.Browser.KeyVersion;
    default:
      return Prototype.Browser.KeyVersion == check;
  }
}

function need_ie6_IFrame()
{
  if (IsBrowserVersion("IE6"))
  {
    return ($$("select").length > 0);
  }
  return false;
}
