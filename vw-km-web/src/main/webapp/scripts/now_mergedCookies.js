
var MergedCookie = Class.create();
MergedCookie.prototype = {

	// Constructor
	initialize: function(options) {
    this.options    = options || {};
    this.cookieName = this.options.cookieName || 'ClientPersist';
    this.delimiter  = this.options.delimiter  || '|';
    this.splitter   = this.options.splitter   || '=';
    this.expireDays = this.options.expireDays || '';
    this.path       = this.options.path       || '/';
    this.domain     = this.options.domain     || '';
  },

  set: function(name,value,cookieName)
  {
    if (name && name.length > 0)
    {
      if (!cookieName)
      {
        cookieName = this.cookieName;
      }

      var baseCookie = this.getBase(cookieName);
      if (baseCookie)
      {
        // now_debug_out ("set cookie: " + cookieName + " (" + name + "=" + value + "); baseCookie = " + baseCookie);
        var newCookie = "";
        var hasKey = false;
        var allCookies = baseCookie.split(this.delimiter);
        for (var i=0;i < allCookies.length; i++) 
        {
	      var cookiePair = allCookies[i].split(this.splitter);
          var thisKey = cookiePair[0];
          var thisVal = cookiePair[1];

          if (thisKey && thisKey.length > 0 && "undefined" != thisKey)
          {

            if ("" != newCookie)
            {
              newCookie += this.delimiter;
            }
            if (thisKey == name)
            {
              thisVal = value;
              hasKey = true;
            }
            newCookie += thisKey + this.splitter + thisVal;
            if ("undefined" == value)
            {
               now_debug_out ("baseCookie: undefined thisVal: set cookie ("+name+"): " + value)
            }
            if ("undefined" == name)
            {
               now_debug_out ("baseCookie: undefined name: set cookie ("+name+"): " + value)
            }
          }
        }

        if (!hasKey)
        {
          if ("" != newCookie)
          {
            newCookie += this.delimiter;
          }
          newCookie += name + this.splitter + value;

          if ("undefined" == value)
          {
             now_debug_out ("undefined thisVal: set cookie ("+name+"): " + value)
          }
          if ("undefined" == name)
          {
             now_debug_out ("undefined name: set cookie ("+name+"): " + value)
          }
        }
        baseCookie = newCookie;
      }
      else
      {
        baseCookie = name + this.splitter + value;
      }
      return this.setBase (cookieName, baseCookie, this.expireDays, this.path, this.domain)
    }
    else
    {
      return false;
    }
  },

  get: function(name, cookieName) 
  {
    if (!cookieName)
    {
      cookieName = this.cookieName;
    }
    var baseCookie = this.getBase(cookieName);
    if (baseCookie)
    {
      var allCookies = baseCookie.split(this.delimiter);
      for (var i=0;i < allCookies.length; i++) 
      {
	      var cookiePair = allCookies[i].split(this.splitter);
        if (cookiePair[0] == name)
        {
          return cookiePair[1];
        }
      }
    }
    return null;
  },

  getBase: function(cookieName) 
  {
    if (!cookieName)
    {
      cookieName = this.cookieName;
    }
    var baseCookie = document.cookie.match(new RegExp('(^|;)\\s*' + escape(cookieName) + '=([^;\\s]*)'));
    if (baseCookie)
    {
      baseCookie = unescape(baseCookie[2]);
    }
    return baseCookie;
  },

  setBase: function(cookieName, baseValue, baseExpires, basePath, baseDomain) 
  {
    var cookieArgs  = "";
    if ((baseExpires) && (baseExpires != undefined) && (baseExpires > 0))
    {
      var expire = '';
      var d = new Date();
      d.setTime(d.getTime() + (86400000 * parseFloat(baseExpires)));
      cookieArgs = '; expires=' + d.toGMTString();
    }
    if (basePath) {cookieArgs += "; path=" + basePath};
    if (baseDomain) {cookieArgs += "; domain=" + baseDomain};
    if (!cookieName)
    {
      cookieName = this.cookieName;
    }
    return document.cookie = escape(cookieName) + '=' + escape(baseValue || '') + cookieArgs;
  },
  
  del: function(name, cookieName)
  {
    var keyVal = "";
    if (name && name.length > 0)
    {
      if (!cookieName)
      {
        cookieName = this.cookieName;
      }
      var baseCookie = this.getBase(cookieName);
      if (baseCookie)
      {
        var newCookie = "";
        var allCookies = baseCookie.split(this.delimiter);
        for (var i=0;i < allCookies.length; i++) 
        {
	        var cookiePair = allCookies[i].split(this.splitter);
          var thisKey = cookiePair[0];
          if (thisKey && thisKey.length > 0 && "undefined" != thisKey)
          {
            if ("" != newCookie)
            {
              newCookie += this.delimiter;
            }
            var thisVal = cookiePair[1];
            if (thisKey != name)
            {
              newCookie += thisKey + this.splitter + thisVal;

              if ("undefined" == thisVal)
              {
                 now_debug_out ("undefined thisVal: del cookie ("+thisKey+"): " + thisVal)
              }
              if ("undefined" == thisKey)
              {
                 now_debug_out ("undefined thisKey: del cookie ("+thisKey+"): " + thisVal)
              }
            }
            else
            {
            //  now_debug_out ("del cookie ("+thisKey+"): " + thisVal)
              keyVal = thisKey + this.splitter + thisVal;
            }
          }
        }
        if ("" == newCookie)
        {
          this.eraseBase (cookieName);
        }
        else
        {
          this.setBase (cookieName, newCookie, this.expireDays, this.path, this.domain);
        }
      }
    }
    return keyVal;
  },

  eraseBase: function(cookieName) {
    if (!cookieName)
    {
      cookieName = this.cookieName;
    }
    var cookie = this.getBase(cookieName) || true;
    this.setBase(cookieName, '', -1);
    return cookie;
  },

  accept: function() {
    if (typeof navigator.cookieEnabled == 'boolean') 
    {
      return navigator.cookieEnabled;
    }
    var key = "_this_is_a_test_key";
    var val = "_the_test_value";
    var bas = "acceptName";
    this.set(key, val, bas);
    return (this.del(key, bas) == val);
  }

}
    		
