String.prototype.trimLeft = function () {
  return (this.replace(/^\s+/,""));
};
String.prototype.trimRight = function () {
  return (this.replace(/\s+$/,""));
};
String.prototype.trim = function () {
	var	str = this.replace(/^\s\s*/, ''), ws = /\s/, i = str.length;
	while (ws.test(str.charAt(--i)));
	return str.slice(0, i + 1);
};

Element.Methods.getPrefixClassValue = function(prefixClassName){
//  local_alert (prefixClassName);
//  local_alert (document.body.className);
  local_alert (typeof this.readAttribute);
  local_alert (this.className);
 // debug;
  //local_alert (typeof this.classNames);
  var cls = this.className;
  return cls;
};

