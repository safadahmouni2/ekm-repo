// Parser Testfälle für den Datums-Autocompleter:
// 379      ->  03.07.2009
// 315      ->  03.01.2005
// 0315     ->  03.01.2005
// 3015     ->  03.01.2005
// 30157    ->  30.01.2057
// 30127    ->  30.12.2007
// 1312125  ->  13.12.2125

// Parser Testfälle für den Kalenderwochen-Autocompleter:
// 92011    ->  09/2011
// 88       ->  08/2008
// 777      ->  09/2077
// 9999     ->  09/2999

var g_minValidCalendarYear = 1900;
var g_maxValidCalendarYear = 2100;
var g_maxValidCalendarWeekYear = 53; // maximal moegliche Kalenderwoche
var g_defaultYear = 2010; // in der Regel das Folgejahr
var g_invalidYear = -9999;

function i18nGetExclamationMessageTitle (oThis, valOptions)
{
  var typeTxt = "";
  switch (valOptions.type) {
    case "numeric":
      typeTxt = " Ziffern";
      break;
    default:
      typeTxt = " Zeichen";
      break;
  }

  var thisTitle = "";
  if ("" != valOptions.title)
  {
    thisTitle = valOptions.title + ". \n";
  }
  
  if (oThis.hasClassName("calendar"))
  {
    if (getCalendarMinLength(oThis) <= oThis.value.length)
    {
      var format = getClassValue (oThis.className, 'Format_');
      if ("weekYear" == format)
      {
        thisTitle += "Geben Sie bitte eine gueltige Kalenderwoche ein";
      }
      else
      {
        var className = oThis.className;
        var format = getClassValue (className, 'Format_');
        var date = Date.parseFormattedString (GetValue(oThis), format);
        if (!IsInCalendarLimits (date, oThis.className))
        {
          var limitMin = getClassValue (className, 'limitMin_');
         	var limitMax = getClassValue (className, 'limitMax_');
          if (limitMax.length > 0)
          {
            var maxDate = CalendarGetLimitDate (limitMax);
            if (limitMin.length > 0)
            {
              var minDate = CalendarGetLimitDate (limitMin);
              thisTitle += "Geben Sie bitte ein Datum im Zeitraum vom " +
                minDate.toFormattedString(false, format, true) +
                " bis " +
                maxDate.toFormattedString(false, format, true) +
                " ein. ";
            }
            else
            {
              thisTitle += "Geben Sie bitte kein Datum nach dem " +
                maxDate.toFormattedString(false, format, true) +
                " ein. ";              
            }
          }
          else
          {
            if (limitMin.length > 0)
            {
              var minDate = CalendarGetLimitDate (limitMin);
              thisTitle += "Geben Sie bitte kein Datum vor dem " +
                minDate.toFormattedString(false, format, true) +
                " ein";   
            }
            else
            {
              thisTitle += "Geben Sie bitte ein gueltiges Datum ein. ";              
            }
          }
        }
        else
        {
          thisTitle += "Geben Sie bitte ein gueltiges Datum ein. ";
        }
      }
      return thisTitle;
    }
  }
 
 
  if ("" != valOptions.lenList)
  {
    thisTitle += "Geben Sie hier bitte"+typeTxt+" der folgenden Laenge ein: \n[" + valOptions.lenList + "]";
  }
  else if ("" != valOptions.valueList)
  {
    thisTitle += "Geben Sie hier bitte einen Wert der folgenden Liste ein: \n[" + valOptions.valueList + "]";
  }
  else
  {
    if ("" != valOptions.min)
    {
      if (("" != valOptions.max) && (valOptions.max > 0))
      {
        if (valOptions.max == valOptions.min)
        {
          if ((valOptions.max == 1) && ("Ziffern" == typeTxt))
          {
            thisTitle += "Geben Sie hier bitte genau \neine Ziffer ein";
          }
          else
          {
            thisTitle += "Geben Sie hier bitte genau \n" + valOptions.max + typeTxt + " ein";
          }
        }
        else
        {
          if (("" != valOptions.max) && (valOptions.max == 1))
          {
            thisTitle += "Geben Sie hier bitte \nein " + typeTxt + " ein";
          }
          else
          {
            thisTitle += "Geben Sie hier bitte \n" + valOptions.min + "\n bis " + valOptions.max + typeTxt + " ein";
          }
        }
      }
      else
      {
        thisTitle += "Geben Sie hier bitte mindestens\n " + valOptions.min + typeTxt + " \nein";
      }
    }
    else
    {
      if ("" != valOptions.max)
      {
        if (valOptions.max > 1)
        {
          thisTitle += "Geben Sie hier bitte 1 bis " + valOptions.max + typeTxt + " ein";
        }
        else
        {
          thisTitle += "Geben Sie hier bitte " + valOptions.max + typeTxt + " ein";
        }
      }
    }
  }

  if ("" == thisTitle)
  {
    switch (valOptions.type) {
      case "email":
        thisTitle = g_de_TitleEmailExplain;
        break;
      default:
        switch (oThis.tagName) {
          case "SELECT":
            thisTitle = "Bitte w&auml;hlen Sie einen Eintrag aus";
            break;
          case "INPUT":
            thisTitle = "Bitte geben Sie einen Wert ein";
            break;
          default:
            thisTitle = "F&uuml;r den Feldtyp (" + oThis.tagName + ") ist derzeit leider keine Hilfe verf&uuml;gbar";
            break;
        }
        break;
    }
  }

  if (!IsRequired(oThis))
  {
    thisTitle += ", \noder lassen Sie das Feld leer.";
  }
  else
  {
    thisTitle += ".";
    
  }

  return thisTitle;

}

function nowValidateEmail(oThis, classNames)
{
  var thisVal = GetValue(oThis);
  if (thisVal && thisVal.length > 0)
  {
    firstAt = thisVal.indexOf("\@");
    lastAt = thisVal.lastIndexOf("@");
    lastDot = thisVal.lastIndexOf(".");
    if ((firstAt < 0) || (lastDot < 0) || (lastDot < firstAt) || (lastAt != firstAt) || (lastDot+3 > thisVal.length))
    {
      return false;
    }
  }
  return true;
}

function nowValidateMinLength(oThis, minLen)
{
  if (!minLen)
  {
    minLen = getMinFieldLength (oThis);
  }
  var isValid = false;
  var thisVal = GetValue(oThis);
  if (thisVal && thisVal.length >= minLen)
  {
    isValid = true;
  }
  return isValid;
}

function nowValidateMaxLength(oThis, maxLen)
{
  var isValid = false;
  var thisVal = GetValue(oThis);
  if (thisVal && thisVal.length <= maxLen)
  {
    isValid = true;
  }
  return isValid;
}

function nowValidateListLength(oThis)
{
  var isValid = false;
  var valOptions = nowGetValidOptions (oThis);
  var thisVal = GetValue(oThis);
  if (thisVal && valOptions.lenList.length > 0)
  {
    $A(valOptions.lenList.split(",")).each(function(item)
    {
      if (thisVal.length == item)
      {
        isValid = true;
      }
    })
  }
  else
  {
    isValid = true;
    
  }
  return isValid;
}

function nowValidateValueList(oThis)
{
  var isValid = false;
  var valOptions = nowGetValidOptions (oThis);
  var thisVal = GetValue(oThis);
  if (thisVal && valOptions.valueList.length > 0)
  {
    $A(valOptions.valueList.split(",")).each(function(item)
    {
      if (thisVal == item)
      {
        isValid = true;
      }
    })
  }
  else
  {
    isValid = true;
  }
  return isValid;
}

function nowValidatePwdConfirm(oThis, classNames)
{
  var thisVal = GetValue(oThis);
  if (thisVal && thisVal.length > 0)
  {
    var aPwd = $$(".required input.password");
    if (!aPwd || !aPwd[0] || aPwd[0].value != thisVal)
    {
      return false;
    }
  }
  return true;
}

function nowValidateCombo(oThis, classNames)
{
  if ("function" == typeof (InputField2ComboBox_CheckValid))
  {
    return InputField2ComboBox_CheckValid (oThis);
  }
  return true;
}

function nowValidateNumeric(oThis, classNames)
{
  return IsNumeric(GetValue(oThis));
}

function nowValidatePhone (oThis, classNames)
{
  return nowValidateMinLength(oThis, 4);
}

function nowValidatePrice (oThis, classNames)
{
  var price = GetValue(oThis);
  // reg = /^\d+(\.\d{2})?$/;
  // reg = /^\$?(?:\d+|\d{1,3}(?:,\d{3})*)(?:\.\d{1,2}){0,1}$/ ;
  // var lang= getBodyClassValue("lang_");
  reg = /^\$?[0-9]+(\.[0-9]{3})*(,[0-9]{2})?$/;
  return reg.test(price);
}

function nowValidateAlpha(oThis, classNames)
{
  var thisVal = GetValue(oThis);
  var posNumber = thisVal.search(/\d/);
  return (posNumber < 0);
}

function nowValidateAlphaNumeric(oThis, classNames)
{
  return nowValidateMinLength(oThis, 4);
}

function getMaxClassFieldLength (oThis, classNames)
{
  var maxVal = getClassValue (classNames, 'max_');
  if (maxVal.length <= 0)
  {
    var maxVal = $(oThis).readAttribute ('maxlength');
    if (!maxVal || maxVal.length <= 0 || maxVal >= 9999)
    {
      maxVal = "";
    }
  }
  return maxVal;
}

function getMinFieldLength (oThis)
{
  return 1 * getClassValue (oThis.className, 'min_');
}

function getMaxFieldLength (oThis)
{
  return 1 * getMaxClassFieldLength (oThis, oThis.className);
}

function nowValidateLengthRange(oThis, cssNames)
{
  var isValid = true;
  if (cssNames && cssNames.length > 0)
  {
    var thisVal = GetValue(oThis);
    var minVal = getClassValue (cssNames, 'min_');
    if (minVal.length > 0)
    {
      isValid = nowValidateMinLength(oThis, minVal);
    }
    if (isValid)
    {
      isValid = nowValidateListLength(oThis);
    }
    if (isValid)
    {
      var maxVal = getMaxClassFieldLength (oThis, cssNames);
      if (maxVal.length > 0)
      {
        isValid = nowValidateMaxLength(oThis, maxVal);
      }
    }
  }
  return isValid;
}

function nowValidateRequired(oThis)
{
  return nowValidateMinLength(oThis, 1) && nowValidateListLength(oThis);
}

function nowValidateDefault(oThis, cssName)
{
  return nowValidateRequired(oThis);
}

function doHighlightInvalid(oThis)
{
  return;
}

function nowIsValidField (oThis, clsNames)
{
  var isValid = true;

  if (clsNames)
  {
    var lcClsNames = clsNames.toLowerCase();
    $w(lcClsNames).each(function(cssName)
    {
      switch (cssName) 
      {
        case "alpha":
          isValid = isValid && nowValidateAlpha (oThis, cssName);
          break;
        case "numeric":
          isValid = isValid && nowValidateNumeric (oThis, cssName);
          break;
        case "alphaNumeric":
          isValid = isValid && nowValidateAlphaNumeric (oThis, cssName);
          break;
        case "phone":
          isValid = isValid && nowValidatePhone (oThis, cssName);
          break;
        case "price":
          isValid = isValid && nowValidatePrice (oThis, cssName);
          break;
        case "email":
          isValid = isValid && nowValidateEmail (oThis, cssName);
          break;
        case "password":
          isValid = isValid && nowValidateAlphaNumeric (oThis, cssName);
          break;
        case "pwdConfirm":
          isValid = isValid && nowValidatePwdConfirm (oThis, cssName);
          break;
        case "combo":
          isValid = isValid && nowValidateCombo (oThis, cssName);
          break;
        case "calendar":
          isValid = isValid && nowValidateCalendar (oThis, clsNames);
          break;
        default:
          isValid = isValid && nowValidateDefault (oThis, cssName);
          break;
      }
    })
  }
  
  if (isValid)
  {
    isValid = nowValidateLengthRange(oThis, clsNames) && nowValidateRequired (oThis) && nowValidateValueList(oThis);
  }

  if (!isValid && !IsRequired(oThis))
  {
    var thisVal = GetValue(oThis);
    if ("" == thisVal)
    {
      isValid = true;
    }
  }
  return isValid;
}

function nowMustValidate (oThis)
{
  var mustValidate = IsRequired(oThis);
  if (!mustValidate && !IsReadOnly (oThis))
  {
    if (oThis.hasClassName ("validate"))
    {
      if ("FORM" != oThis.tagName.toUpperCase())
      {
        var thisVal = GetValue(oThis);
        mustValidate = (thisVal && thisVal.length > 0);
      }
    }
  }
  return mustValidate;
}

function nowIsValid (oThis)
{
  var isValid = true;
  oThis = $(oThis);
  if (nowMustValidate(oThis))
  {
    isValid = nowIsValidField (oThis, oThis.className);
  }
  return isValid;

}

function nowGetValidOptions (oThis)
{
  var valType = "";
  var valMin = 0;
  var valMax = "";
  var valTitle = "";

  var ml = $(oThis).readAttribute ('maxlength');
  if (ml && ml < 9999)
  {
    valMax = ml;
  }
  if (oThis.title)
  {
    valTitle = oThis.title;
  }

  var clsNames = oThis.className;
  
  var lenListName = getClassValue (clsNames, 'lenList_');
  var valLenList = readScriptList ('lenList_' + lenListName)  
  
  var valListName = getClassValue (clsNames, 'valueList_');
  var valValueList = readScriptList ('valueList_' + valListName)  
  
  if (clsNames)
  {
    // clsNames = clsNames.toLowerCase();
    $w(clsNames).each(function(cssName)
    {
      switch (cssName) 
      {
        case "alpha":
        case "numeric":
        case "alphaNumeric":
        case "phone":
        case "email":
        case "password":
        case "pwdConfirm":
        case "combo":
          valType = cssName;
          break;

        case "calendar":
          valType = cssName;
          var val = getClassValue (clsNames, 'min_');
          if (val.length == 0)
          {
            valMin = getCalendarMinLength(oThis);
          }
          break;
        default:
          if (cssName && cssName.length > 0)
          {
            var val = getClassValue (cssName, 'min_');
            if (val.length > 0)
            {
              valMin = val;
            }

            val = getClassValue (cssName, 'max_');
            if (val.length > 0)
            {
              valMax = val;
            }
          }
          break;
      }
    })
  }

  return {type: valType, min: valMin, max: valMax, lenList: valLenList, valueList: valValueList, title:valTitle };
}

function setValidChecked(oThis)
{
  if (nowMustValidate(oThis) || oThis.hasClassName("validate"))
  {
    var nxTag = $(oThis).next("span.exclamation");
    if (nxTag)
    {
      nxTag.remove();
    }
  }
  return true;
}

function labelSetClassName(oCtrl, action, clsName)
{
  if (oCtrl && oCtrl.id)
  {
    $$("label[for='"+oCtrl.id+"']").invoke(action + "ClassName", clsName);
  }
}

function setExclamation(oThis)
{
  var thisTitle = i18nGetExclamationMessageTitle (oThis, nowGetValidOptions (oThis));

  var imgErr = '<span title="'+thisTitle+'" class="exclamation"/>&#160; ! &#160;<span>';
  oCtrl = $(oThis);
  var nxTag = $(oThis).next("span.exclamation");
  if (!nxTag)
  {
    if (oCtrl.hasClassName ("comboBox"))
    {
      var oSpan = $(oThis).next("span");
      if (oSpan && oSpan.hasClassName ("comboHandle"))
      {
        oCtrl = $(oSpan);
      }
    }
    $(oCtrl).insert({after: imgErr});
    if ("function" == typeof (Tooltips_Behaviour_Init)) {Tooltips_Behaviour_Init ("span.exclamation[title]");}

  }
  nxTag = $(oThis).next("span.exclamation");
  var ctrlW = oCtrl.getWidth() - 7;

  Position.clone(oCtrl, nxTag, { setHeight: false, setWidth: false, offsetTop:-5, offsetLeft: ctrlW});

  labelSetClassName(oCtrl, "add", "invalid");
  labelSetClassName(oCtrl, "remove", "valid");
  oCtrl.addClassName ("revalidate");
  nowFieldValidatorInitInvalid(oCtrl);
  return true;
}

function nowValidatorErrorBehaviour()
{
  $$('.validate input.invalid, .validate select.invalid, .validate textarea.invalid').each(function(el)
  {
    if (!el.hasClassName("ajax"))
    {
      el.onkeyup = function()
      {
        return nowValidatorApply(this);
      }
    }
  })
}

function getCalendarMinLength(oThis)
{
  var minLen = 4;
  var format = getClassValue (oThis.className, 'Format_');
  switch (format) {
    case "weekYear": // WWYY (Kalenderwoche Jahr) z.B.: 4809 
      break;
    default:
    case "": // DDMMYY (Tag Monat Jahr) z.B.: 170309 
      minLen = 6;
      break;
  }  
  return minLen;
}

function calendarWeekYearToString (calDate)
{
  var y = calDate.year;
  var w = calDate.week;
  
  if ((g_invalidYear == y) || !IsNumeric(w) || !IsNumeric(y))
  {
    return calDate.orign;
  }
  
  var wyString = "";;
  
  if (w < 10)
  {
    wyString = "0"
  }
  wyString += w + "/";

  if (y < 10)
  {
    wyString += "200"
  }
  else if (y < 100)
  {
    wyString += "20"
  }
  else if (y < 1000)
  {
    wyString += "2"
  }
  wyString += y;

  return wyString;
}

function calendarDateToString (d,m,y)
{
  // use the fastest string to int conversion number = (+string);
  d = (+d);
  m = (+m);
  y = (+y);
  var dateString = "";
  if (d < 10)
  {
    dateString = "0"
  }
  dateString += d + ".";
  if (m < 10)
  {
    dateString += "0"
  }
  dateString += m + ".";
  if (y < 10)
  {
    dateString += "200"
  }
  else if (y < 100)
  {
    dateString += "20"
  }
  else if (y < 1000)
  {
    dateString += "2"
  }
  dateString += y;
  return dateString;  
  
}  

function calendarCalDateToString (calDate)
{
  var y = calDate.year;
  var d = calDate.day;
  var m = calDate.month;
  
  if ((g_invalidYear == y) || !IsNumeric(d) || !IsNumeric(m) || !IsNumeric(y))
  {
    return calDate.orign;
  }
  
  return calendarDateToString (d,m,y);
  
}  

function isValidCalendarDate (d,m,y)
{
    
  var isValid = (y != g_invalidYear) && (g_minValidCalendarYear <= y) && (y <= g_maxValidCalendarYear) && (1 <= m) && (m <= 12) && (1 <= d) && (d <= 31);
  if (isValid)
  {
    switch (m)
    {
      case 2: // 28 oder 29 Tage
        if (nowIsLeapYear(y))
        {
          isValid = (d <= 29);
        }
        else
        {
          isValid = (d <= 28);
        }
        break;
      case 4: // 30 Tage
      case 6:
      case 9:
      case 11:
        isValid = (d <= 30);
        break;
      default:
        isValid = (d <= 31);
        break;
    }
    
  }
  return isValid;
}

function parseCalendarDate (calVal, classNames)
{
  var d = 1;
  var m = 1;
  var y = g_invalidYear;
  
  calVal = calVal.replace(/\,/g,'\.');
  calVal = calVal.replace(/\;/g,'\.');
  calVal = calVal.replace(/\-/g,'\.');
  calVal = calVal.replace(/\_/g,'\.');
  calVal = calVal.replace(/\:/g,'\.');
  
  var firstDot = calVal.indexOf("\.");
  if (firstDot >= 0)
  {
    var lastDot = calVal.lastIndexOf ("\.");
    if (lastDot > firstDot)
    {
      d = calVal.substr(0,firstDot);
      m = calVal.substring(firstDot+1, lastDot);
      y = calVal.substr(lastDot+1);
    }
    else
    {
      d = calVal.substr(0,firstDot);
      var m_y = calVal.substr(firstDot+1);
      switch (m_y.length)
      {
        case 1:
          m = m_y;
          y = g_defaultYear;
          break;
        case 2:
          m = m_y.substr(0,1);
          y = m_y.substr(1);
          break;
        case 3:
          m = m_y.substr(0,1);
          y = m_y.substr(1);
          break;
        default:
          m = m_y.substr(0,2);
          y = m_y.substr(2);
          break;
      }
    }
  }
  else
  {
    switch (calVal.length) {
      case 2: // DM
        d = calVal.substr(0,1);
        m = calVal.substr(1,1);
        y = g_defaultYear;
        break;
      case 3: // DMY
      case 4: // DMYY
        d = calVal.substr(0,1);
        if (1*d == 0)
        {
          d = calVal.substr(1,1);
          m = calVal.substr(2,1);
          if (1*m == 0)
          {
            m = calVal.substr(3,1);
            y = calVal.substr(4);
          }
          else
          {
            y = calVal.substr(3);
          }
        }
        else
        {
          m = calVal.substr(1,1);
          if (1*m == 0)
          {
            m = calVal.substr(2,1);
            y = calVal.substr(3);
          }
          else
          {
            y = calVal.substr(2);
          }
        }
        break;
      case 5: // DDMMY
      case 6: // DDMMYY
      case 7: // DDMMYYY
      case 8: // DDMMYYYYY
        d = calVal.substr(0,2);
        if (1*d <= 31)
        {
          m = calVal.substr(2,2);
          if (1*m <= 12)
          {
            y = calVal.substr(4);
          }
          else
          {
            m = calVal.substr(2,1);
            y = calVal.substr(3);
          }
        }
        else
        {
          d = calVal.substr(0,1);
          m = calVal.substr(1,2);
          if (1*m <= 12)
          {
            y = calVal.substr(3);
          }
          else
          {
            m = calVal.substr(1,1);            
            y = calVal.substr(2);
          }
        }
        break;
    }
  }

  // now_debug_out ("parsed (calVal) = ("+d +", " + m + ", " + y + ")");
  
  d = 1*d;
  m = 1*m;
  y = 1*y;
  
	if (d < 1)
	{
		d=1;
	}
	if (m < 1)
	{
		m=1;
	}	
  return {day: d, month: m, year: y, orign:calVal, valid:isValidCalendarDate (d,m,y)};
}


function parseCalendarWeekYear (calVal, className)
{
  var w = 1;
  var y = g_invalidYear;

  calVal = calVal.replace(/\,/g,'\/');
  calVal = calVal.replace(/\./g,'\/');
  calVal = calVal.replace(/\-/g,'\/');
  calVal = calVal.replace(/\;/g,'\/');
  calVal = calVal.replace(/\_/g,'\/');
  calVal = calVal.replace(/\:/g,'\/');
  
  var firstSlash = calVal.indexOf("/");
  if (firstSlash >= 0)
  {
    var lastSlash = calVal.lastIndexOf ("/");
    if (lastSlash == firstSlash)
    {
      w = calVal.substr(0,firstSlash);
      y = calVal.substring(firstSlash+1);
    }
  }
  else
  {
    switch (calVal.length) {
      case 1: // W
        w = calVal;
        y = g_defaultYear;
        break;
      case 2: // WW
        w = calVal;
        if (1*w > g_maxValidCalendarWeekYear)
        {
          w = calVal.substr(0,1);
          y = calVal.substr(1);
        }
        else
        {
          y = g_defaultYear;
        }
        break;
      case 3: // WWY
        w = calVal.substr(0,2);
        if (1*w > g_maxValidCalendarWeekYear)
        {
          w = calVal.substr(0,1);
          y = "20" + calVal.substr(1);
        }
        else
        {
          y = "200" + calVal.substr(2); // Muss alle Jahrhunderte angepasst werden
        }
        break;
      case 4: // WWYY
        w = calVal.substr(0,2);
        if (1*w > g_maxValidCalendarWeekYear)
        {
          w = calVal.substr(0,1);
          y = "2" + calVal.substr(1); // Muss alle Jahrtausende angepasst werden
        }
        else
        {
          y = "20" + calVal.substr(2); // Muss alle Jahrzehnte angepasst werden
          
        }
        break;
      case 5: // WWYYY
        w = calVal.substr(0,2);
        if (1*w > g_maxValidCalendarWeekYear)
        {
          w = calVal.substr(0,1);
          y = calVal.substr(1);
        }
        else
        {
          y = "2" + calVal.substr(2); // Muss alle Jahrzehnte angepasst werden
        }
        break;
      case 6: // WWYYYYY
        w = calVal.substr(0,2);
        y = calVal.substr(2);
        break;
    }
  }
 
  w = 1*w;
  y = 1*y;
   
  var isValid = (y != g_invalidYear) && (g_minValidCalendarYear <= y) && (y <= g_maxValidCalendarYear) && (1 <= w) && (w <= g_maxValidCalendarWeekYear);
  if (isValid)
	{
		var limitMin = getClassValue (className, 'limitMin_');
		// now_debug_out ("isvalid: w="+w+"; y="+y + "; className=" + className)
		if ("" != limitMin)
		{
			var minDate = CalendarGetLimitDate (limitMin);
			var kw = CalcKalenderWoche(minDate.getDate(), minDate.getMonth(), minDate.getFullYear());
			isValid = !((y <= minDate.getFullYear()) && (w < kw));
			// now_debug_out ("checked: isvalid (kw="+kw+"; y="+minDate.getFullYear() + ") = " + isValid);
		}
	}
	
//  now_debug_out ("parsed (calVal) = (" + w +", " + y + "); valid = " + isValid );
  return {week: w, year: y, orign:calVal, valid:isValid};    
  
}

function getCalendarAutoCompleteValue (className, calVal)
{
  var retVal = calVal;
	var calFormat = getClassValue (className, 'Format_');
  switch (calFormat) {
    case "weekYear": // WW/YYYY (Kalenderwoche/Jahr) z.B.: 48/2009 
        var calDate = parseCalendarWeekYear (calVal, className);
				var limitMin = getClassValue (className, 'limitMin_');
				if ("" != limitMin)
				{
          var minDate = CalendarGetLimitDate (limitMin);
				  var kw = CalcKalenderWoche(minDate.getDate(), minDate.getMonth(), minDate.getFullYear());
					if ((calDate.year <= minDate.getFullYear()) && (calDate.week < kw))
					{
			      if (calendarWeekYearToString (calDate) != calVal)
						{
							calDate.year = 1 + minDate.getFullYear();
						}
					}
				}
        retVal = calendarWeekYearToString (calDate);
      break;

    default:
    case "": // DD.MM.YYYY (Tag.Monat.Jahr) z.B.: 17.03.2009
        var calDate = parseCalendarDate (calVal, className);
        retVal = calendarCalDateToString (calDate);
      break;
  }
  
  // now_debug_out ("getCalendarAutoCompleteValue ("+calFormat + ", " + calVal+") = " + retVal);  
  
  return retVal;
}

function nowIsLeapYear(year)
{
  return ((year % 400) == 0) || (((year % 4) == 0) && ((year % 100) != 0));
}

function getFieldCalendarDate(oFld)
{
  var date = null;
  if (IsDefined(oFld) && oFld.hasClassName("calendar"))
  {
    var format = getClassValue (oFld.className, 'Format_');
    date = Date.parseFormattedString (GetValue(oFld), format);
  }
  return date;
}

function nowValidateCalendar(oThis, classNames)
{
  var format = getClassValue (classNames, 'Format_');
  var thisVal = GetValue(oThis);
  var len = thisVal.length;
  var isValid = false;
  
  var calValue = getCalendarAutoCompleteValue (classNames, thisVal);
  // now_debug_out ("nowValidateCalendar: getCalendarAutoCompleteValue ("+thisVal+") = " + calValue);
  
  switch (format) {
    case "weekYear": // WW/YYYY (Kalenderwoche/Jahr) z.B.: 48/2009
      var calDate = parseCalendarWeekYear (calValue, classNames);
      isValid = calDate.valid;
      break;

    default:
    case "": // DD.MM.YYYY (Tag.Monat.Jahr) z.B.: 17.03.2009 
      var calDate = parseCalendarDate (calValue, classNames);
      isValid = calDate.valid;
      if (isValid)
      {
        var date = Date.parseFormattedString (calValue, format);
        isValid = IsInCalendarLimits (date, classNames);
        // now_debug_out ("IsInCalendarLimits("+date+", "+classNames+")="+isValid);
      }
      break;
  }
  return isValid;
}

function nowValidatorAutoCompleteCalendar (oThis)
{
  return getCalendarAutoCompleteValue (oThis.className, GetValue(oThis));
}

function nowValidatorAutoComplete (oThis)
{
  var thisVal = GetValue(oThis);
  thisVal = thisVal.trim();
  var retVal = thisVal;

  var complete = getClassValue (oThis.className, 'complete_');
  if ("" == complete && oThis.hasClassName("calendar"))
  {
    complete = "calendar"; // calendar immer autocompete
  }
  // now_debug_out ("nowValidatorAutoComplete: " + complete);
  if ("" != complete)
  {
    switch (complete) {
      case "modelYear":
        if (2 == thisVal.length)
        {
          retVal = "20" + thisVal; // gilt nur bis zum Jahr 2099 !!! who cares :-)
        }
        break;
     case "calendar":
        retVal = nowValidatorAutoCompleteCalendar (oThis);
        break;
    }
  }
  return retVal;
}

function nowKeyUpCalendarBehaviour(oThis, keyCode)
{
	oThis = $(oThis);
	if (!$(oThis).hasClassName ("readonly"))
	{
		var oldDate = GetValue (oThis);
		oldDate = oldDate.trim();
		var clsNames = oThis.className;
		var format = getClassValue (clsNames, 'Format_');
		var calDate = new Date();
		if ("" != oldDate)
		{
		  calDate = Date.parseFormattedString (oldDate, format);
		}

	 // now_debug_out ("keyCode  " + keyCode);

	  var offset = 0;
		switch (keyCode)
		{
			case 107: // + (Plus)
			case Event.KEY_UP:
				offset = 1;
				break;
			case 109: // - (Minus)
			case Event.KEY_DOWN:
				offset = -1;
				break;
			case 32: // space
  			initExtraKeys();
				if ("" == oldDate)
				{
					newDate = new Date();
					nextDate = newDate.toFormattedString (false, format);
					// now_debug_out ("init date to  " + nextDate);
					SetValue (oThis, nextDate);
				}
				else
				{
				  var val = getCalendarAutoCompleteValue (clsNames, oldDate);
					SetValue (oThis, val);
				}
				break;
		}
		if (0 != offset)
		{
			var sign = offset;
			offset = 0;
			if (hasExtraKey(keyAlt))
			{
				calDate.setFullYear(calDate.getFullYear() + sign);
				sign = 0;
			}
			if ("weekYear" == format)
			{
				if (hasExtraKey(keyStrg))
				{
					calDate.setFullYear(calDate.getFullYear() + sign);
					sign = 0;
				}
				else
				{
					offset += sign * 7;
				}
			}
			else
			{
				if (hasExtraKey(keyStrg))
				{
					calDate.setMonth(calDate.getMonth() + sign);
				  sign = 0;
				}
			}
			if (0 == offset)
			{
				offset = sign;
			}
			// now_debug_out ("nowKeyUpInputBehaviour: offset = " + offset);
			var newDate = new Date(calDate.getTime() + offset * 86400000);

			var prevDate = calDate.toFormattedString (false, format);
			var nextDate = newDate.toFormattedString (false, format);
			if (nextDate == prevDate)
			{
			  newDate = new Date(calDate.getTime() + offset * 90000000);
			  nextDate = newDate.toFormattedString (false, format);
			}			
		  SetValue (oThis, nextDate);
	
		  clsNames = clsNames.replace(/limitM/g,'unboundM"');
			if (!nowValidateCalendar (oThis, clsNames))
			{
				SetValue (oThis, oldDate);
			}
		}

	}	
}

function nowGetValueListIndex(aList, val)
{
	var idx =-1;
	for (var i=0;i<aList.length;i++)
	{
		if (aList[i] == val)
		{
			idx = i;
			break;
		}
	}
	return idx;
}

function nowKeyUpInputCompleteBehaviour(oThis, complete, keyCode)
{
	var val = GetValue(oThis).trim();
	if ("modelYear" == complete)
	{
		var year = 1 * val;
		switch (keyCode)
		{
			case 107: // + (Plus)
			case Event.KEY_UP:
				if (nowIsValid (oThis))
				{
					SetValue(oThis, 1 + 1*val);
				}
			  else if ("" == val)
				{
					SetValue(oThis, g_defaultYear+1);
				}
				break;
			case 109: // - (Minus)
			case Event.KEY_DOWN:
				if (nowIsValid (oThis))
				{
					SetValue(oThis, -1 + 1*val);
				}
			  else if ("" == val)
				{
					SetValue(oThis, g_defaultYear-1);
				}
				break;
			case 32: // space
				if ("" == val)
				{
					SetValue(oThis, g_defaultYear);
				}
		}
	}
	else if ("valueList" == complete)
	{
		var valListName = getClassValue (oThis.className, 'valueList_');
		var valValueList = readScriptList ('valueList_' + valListName)
		if ("" != valValueList)
		{
			var aList = valValueList.split(",");
			var idx = nowGetValueListIndex(aList, val);
			
			switch (keyCode)
			{
				case 107: // + (Plus)
				case Event.KEY_UP:
					idx++;
					if (idx >= aList.length)
					{
						idx=0;
					}
					break;
				case 109: // - (Minus)
				case Event.KEY_DOWN:
					idx--;
					if (idx < 0)
					{
						idx=aList.length-1;
					}
					break;
				case 32: // space
					if ("" == val)
					{
						idx=0;
					}
			}
			if (0 <= idx && idx < aList.length)
			{
			  SetValue(oThis, aList[idx]);
			}
		}
	}		
	
	
}

function nowKeyUpInputBehaviour(oThis, keyCode)
{
  if (oThis.hasClassName("uppercase"))
  {
    SetValue (oThis, oThis.value.toUpperCase());
  }

  if (oThis.hasClassName("lowercase"))
  {
    SetValue (oThis, oThis.value.toLowerCase());
  }
  
  if (oThis.hasClassName("calendar"))
  {
		nowKeyUpCalendarBehaviour(oThis, keyCode)
	}
	else
	{
		var complete = getClassValue (oThis.className, 'complete_');
		if ("" != complete)
		{
			nowKeyUpInputCompleteBehaviour(oThis, complete, keyCode)
		}
		else if ("" != getClassValue (oThis.className, 'valueList_'))
		{
			nowKeyUpInputCompleteBehaviour(oThis, 'valueList', keyCode)
		}		
	}
  if (oThis.hasClassName("submitOnEnter"))
  {
    return handleSubmitOnEnter(oThis, keyCode);
  }

  return true;
}

function nowKeyUpTextareaBehaviour(oThis, keyCode)
{
  if (oThis.hasClassName("uppercase"))
  {
    var org = oThis.innerHTML;
    var upper = org.toUpperCase();
    if (org != upper)
    {
      oThis.innerHTML = upper;
    }
  }
  if (oThis.hasClassName("lowercase"))
  {
    var org = oThis.innerHTML;
    var lower = org.toLowerCase();
    if (org != lower)
    {
      oThis.innerHTML = lower;
    }
  }
  return true;
}

function nowSpecialFunctionCall(oThis, keyCode)
{
  var func = getClassValue (oThis.className, 'func_');
  if (func.length > 0)
  {
    // now_debug_out ("nowSpecialFunctionCall: " + func);
    return call_function_by_string (func, oThis, keyCode);
  }
  return false;
}  

function nowKeyUpBehaviour(oThis, keyCode, eventName)
{
  var ret = true;
  var tagName = oThis.tagName.toLowerCase();
  
  if ("undefined" == typeof (eventName))
  {
    eventName = "keyup";
  }

  switch (tagName) {
    case "input":
      ret = nowKeyUpInputBehaviour(oThis, keyCode);
      break;
    case "textarea":
      ret = nowKeyUpTextareaBehaviour(oThis, keyCode);
      break;
    case "select":
      // no supported
      break;
    default:
      now_debug_out("UNSUPPORTED KeyUpBehaviour for tagName " + tagName);
      break;
  }
  nowSpecialFunctionCall(oThis, keyCode);
  errorDragWindowNotify (oThis, keyCode, eventName);
  
  return ret ;
}


function nowSyncAreaError(oThis, valid)
{
  var oErrWin = $('errReportWin');
  if (oErrWin)
  {
    var theId = $(oThis).identify();
    // now_debug_out("nowSyncAreaError : " + theId);
    oErrWin.select("div.body li label[for="+theId+"]").each(function(lbl)
    { // es koennen mehrere labels auf die id zeigen, z.b. Direkteingabemodus Auftragsbearbeitung Aussen- und Innenfarbe
      var li = lbl.up("li");
      if (valid)
      {
         li.addClassName("valid");
      }
      else
      {
         li.removeClassName("valid");
      }
    })
  }
}

function nowValidatorSetValid(oThis, valid)
{
  if (valid)
  {
    setValidChecked(oThis);
    $(oThis).removeClassName('invalid');
    $(oThis).addClassName('valid');
    labelSetClassName(oThis, "remove", "invalid");
    labelSetClassName(oThis, "add", "valid");
  }
  else
  {
    $(oThis).addClassName('invalid');
    $(oThis).removeClassName('valid');
    setExclamation(oThis);
    labelSetClassName(oThis, "add", "invalid");
    labelSetClassName(oThis, "remove", "valid");
    if ("function" == typeof (doHighlightInvalid)) {doHighlightInvalid (oThis);}
  }
  nowSyncAreaError(oThis, valid);
  return true;
}

function nowValidatorApply(oThis, keyCode)
{
  if (!keyCode || "undefined" == typeof (keyCode))
  {
    keyCode = 0;
  }
  nowValidatorSetValid($(oThis), nowIsValid (oThis));
  ret = nowSpecialFunctionCall(oThis, keyCode);
  return ret;
}

function isValidForm()
{
  var isValid = true;
  var invalidFields = "";
  var now = new Date();
  return isValid;
}


function errorDragWindowNotify (oThis, keyCode, eventName)
{
  var oErrWin = $('errReportWin');
  if (oErrWin)
  {
    // now_debug_out ("errorDragWindowNotify ("+oThis.identify() + ", " + eventName);
    
    errElem = oErrWin.down("div.content label[for='"+oThis.identify()+"']");
    var isInList = IsDefined (errElem);
    if (0 < keyCode)
    {
      // now_debug_out ("errorDragWindowNotify ("+oThis.identify() + ", " + eventName + ", isInList=" + isInList + ", keyCode=" + keyCode);
    }

    if ("keyup" != eventName)
    {
      var oLastFocus = null;
      oErrWin.select("div.body li label[for]").each(function(lbl)
      {
        var li = lbl.up("li");
        if (li.hasClassName ("hasFocus"))
        {
          if ("blur" == eventName)
          {
            if (oLastFocus)
            {
              oLastFocus.removeClassName ("lastFocus");
            }
            li.addClassName ("lastFocus");
            var prvLi = li.previous();
            if (!IsDefined(prvLi) || !prvLi.hasClassName ("lastFocus"))
            {
              oLastFocus = li;
            }
          }
          li.removeClassName ("hasFocus");
        }
        else if ("blur" == eventName && isInList)
        {
          li.removeClassName ("lastFocus");
        }
      })
      
      if ("focus" == eventName)
      {
        var theId = $(oThis).identify();
        oErrWin.select("div.body li label[for="+theId+"]").each(function(lbl)
        {
          var li = lbl.up("li");
          li.addClassName ("hasFocus");
        })
      }
    }
    else
    {
      if (Event.KEY_TAB == keyCode)
      {
       // now_debug_out ("nowKeyUpInputBehaviour: keyCode = " + keyCode + "; id = " + oThis.identify());
        var oPV = $("errFieldPreview");
        if (IsDefined(oPV) && oPV.checked)
        {
          var errFld = nowErrorGetNextErrorField (oThis);
          if (IsDefined (errFld))
          {
            nowErrorMessagesScrollTo (errFld, g_scrollDuration);
            return false;
          }
          else  
          {
            // now_debug_out ("no Next ("+oThis.identify()+"): keyCode = " + keyCode + "; isInList = " + isInList);
          }
        }
      }      
    }

    
  }
  return true;
}

function errorDragWindowInit ()
{
    var errCnt = 0;
    if (!IsBrowserVersion("IE6"))
    {
      errCnt = $A($$('#areaContent div.areaError div.body li.Error')).length;
    }
   
    if (errCnt > 0)
    {
      addBodyClasses("hasPageErrors pageScrollMargin");
      var theErr = "<ul>" + $('areaContent').down('div.areaError div.body li.Error').up("ul").innerHTML + "</ul>";

      // var btnModal = '<span class="fRight"><input type="checkbox" class="checkbox" checked="checked" name="errWndModal" id="errWndModal" onclick="return dragWindowToggleModal(this)" /><label for="errWndModal">modal</label></span>';
      var btnClose = '<a href="#" class="button abort" title="Fenster schließen">Schlie&szlig;en</a>';
      var checked = '';
      if (hasBodyClass ("usp_errFieldTabConnection"))
      {
        checked = ' checked="checked"';
      }

      var btnFldPreview = '<input type="checkbox"'+checked+' class="checkbox usp_errFieldTabConnection" onchange="setUspBodyClassByCheckbox(this);" name="errFieldPreview" id="errFieldPreview" /><label for="errFieldPreview">Fehlerfeld TAB-Vern&uuml;pfung</label>';

      // Server-Zeit aus body-class
      var nowDate = getBodyClassValue("date_");
      var nowTime= getBodyClassValue("time_");
      var aDate = nowDate.split("-");
      var aTime = nowTime.split("-");
      var sDate = calendarDateToString (aDate[2],aDate[1],aDate[0]) + " " + aTime[0] + ":" + aTime[1];
      
      var sDestApp = getBodyClassValue('mqDestApp_');
      var dayTime = '<span class="fRight dayTime"><span class="destApp">'+sDestApp+'</span>-System: '+sDate+'</span>';

      var errId = 'errReportWin';
      var ddw_footer = btnClose + btnFldPreview + dayTime;
      ddw_footer += "<a class='scroll' href='#refBottom' title='Zum Seitenende'>Nach unten</a> <a class='scroll' href='#refTop' title='Zum Seitenanfang'>Nach oben</a>";
      
      var extraCls = "usp_ErrorWin collapsible";
    
      var oErrWin = $(errId);
      var clsClosed = "";
      var errWin = '\n<div id="'+errId+'" class="usp_ErrorWin dragWindow collapsible">\n<h4><span class="dragHandle">Fehler</span><span class="head'+clsClosed+'">minify</span><a class="abort close" href="#">[x]</a></h4>\n<div class="body"><div class="content">'+theErr+'</div></div><div class="foot buttons">'+ddw_footer+'</div>\n</div>\n';
      getBody().insert (errWin);
      oErrWin = $(errId);
      ComboBox_Behaviour_Selector_Init ("#" + errId + " input.comboBox");
      Collapsible_Behaviour_Selector_Init("#" + errId);

      // oErrWin = dragWindowCreate(errId, "Fehler neu", theErr, ddw_footer, extraCls);   

      if ("-1px" == oErrWin.getStyle("left"))
      {
        dragWindow_SetToCenter(oErrWin);
      }

      var errFldFocusId = "";
      var validFieldIds = 0;
      oErrWin.select("div.body label[for]").each(function(el)
      {
        var theId = el.readAttribute('for');
        if (theId && "" != theId && IsDefined($(theId)))
        {
          if ("" == errFldFocusId)
          {
            errFldFocusId = theId;
          }
          var oFld = $(theId);
          el.onclick = function()
          {
            var theId = $(this).readAttribute('for');
            var oPV = $("errFieldPreview");
            // if (IsDefined(oPV) && oPV.checked)
            {
              var duration = 0.25;
              if (!hasBodyClasses ("usp_softScroll"))
              {
                duration = 0;
              }
              return nowErrorMessagesScrollTo (theId, duration);
            }
            return SetFocus(theId);
          }

          oFld.addClassName("validate revalidate");
          nowFieldValidatorInitElement (oFld);
          nowFieldValidatorInitInvalid (oFld);
          validFieldIds++;
        }
        else
        {
          el.addClassName("missingId");
        }
      })
      
      if (0 == validFieldIds)
      {
        oErrWin.addClassName ("noValidFields");
      }
      else if ("" != checked && "" != errFldFocusId)
      {
        // now_debug_out ("SetErrFocus: id = " + errFldFocusId);
        addBodyClasses("errorFocus_" + errFldFocusId);
      }

      // now_debug_out ("info: page has "+errCnt+" error/s");
      
    }
      
}

function nowFieldValidatorInitElement(el)
{
  el.onblur = function(evt)
  {
    var keyCode = keyCodeFromEvent(evt);
    return nowFieldValidatorOnBlur(this, keyCode);
  }
  el.onfocus = function(evt)
  {
    var keyCode = keyCodeFromEvent(evt);
		initExtraKeys();
    return nowFieldValidatorOnFocus(this, keyCode);
  }
}

function nowFieldValidatorCheck4Activate(oThis,isControlKey)
{
  var thisVal = GetValue(oThis);
  var maxLen = getMaxFieldLength (oThis);
  if (!nowIsValidField (oThis, oThis.className))
  {
  // now_debug_out("VALID("+keyCode+") maxLen = " + maxLen + "; len=" + thisVal.length);
    if (!isControlKey)
    {
      if (thisVal.length == maxLen)
      {
   // now_debug_out("Activate");
        oThis.activate();
      }
    }
    else
    {
     // now_debug_out("IsControlKey("+keyCode+")");
    }
  }
  else
  {
   // now_debug_out("INVAL ("+keyCode+") maxLen = " + maxLen + "; len=" + thisVal.length);
  }
}

function nowFieldValidatorInitInvalid(el)
{
  if (!el.hasClassName("ajax"))
  {
    el.onkeyup = function(evt)
    {
      var keyCode = keyCodeFromEvent(evt);
      nowKeyUpBehaviour($(this), keyCode);
      var ret = nowValidatorApply($(this), keyCode);
      nowFieldValidatorCheck4Activate($(this),IsControlKey(keyCode));
      return ret;
    }
  }
}

function nowFieldValidatorOnKeyup(oThis, keyCode)
{
  oThis = $(oThis);
  nowKeyUpBehaviour(oThis, keyCode, "keyup");
  if (hasClassNames(oThis, "invalid valid revalidate", 1))
  {
    nowValidatorApply(oThis, keyCode);
  }
  return true;
}

function nowFieldValidatorOnKeydown(oThis)
{
  return true;
}

function nowFieldValidatorOnFocus(oThis, keyCode)
{
  oThis = $(oThis);
  if (!nowIsValid (oThis))
  {
    if (GetValue(oThis) != "")
    {
      if ("function" == typeof (doHighlightInvalid)) {doHighlightInvalid (oThis);}
    }
    nowFieldValidatorCheck4Activate(oThis,false);
  }
  
  if (hasClassNames(oThis, "invalid valid revalidate", 1))
  {
    nowValidatorApply(oThis);
  }
  
  return errorDragWindowNotify (oThis, keyCode, "focus");
}

function nowFieldValidatorOnBlur(oThis, keyCode)
{
  oThis = $(oThis);
  // now_debug_out ("-----onblur -----");
  errorDragWindowNotify (oThis, keyCode, "blur");
  nowKeyUpBehaviour (oThis, keyCode, "blurKey");
  var val = nowValidatorAutoComplete(oThis);
  SetValue(oThis, val);
  nowValidatorApply(oThis);
  return true;  
}


function nowFieldValidatorInit()
{
//  $$('input.uppercase, input.lowercase').each(function(el)
  $$('#areaContent input[type=text]').each(function(el)
  {
    if (!el.hasClassName("ajax") || "" != getClassValue (el.className, 'ajax'))
    {
      el.onkeyup = function(evt)
      {
        var keyCode = keyCodeFromEvent(evt);
        return nowFieldValidatorOnKeyup(this, keyCode);
      }
    }
  })
  
//  getBody().onkeydown = function(evt) {
    // now_debug_out ("getBody: onkeydown: " + keyCodeFromEvent(evt));
	//	setExtraKey(keyCodeFromEvent(evt), "down");
//  }
//  getBody().onkeyup = function(evt) {
    // now_debug_out ("getBody: onkeyup: " + keyCodeFromEvent(evt));
//		setExtraKey(keyCodeFromEvent(evt), 0);
//  }
		
  if (!Prototype.Browser.IE)
  {
    //addBodyClasses("validate");
    // $$('#areaContent input[type=text], #areaContent textarea').each(function(el)
    // {
      //if (!el.hasClassName("required"))
      //{
      //  el.addClassName("validate");
      //}
    // })
  }

  $$('.validate input, .validate select, .validate textarea, input.validate').each(function(el)
  {
		if (el.hasClassName("calendar") || el.hasClassName("ajax") || ("" != getClassValue (el.className, 'ajax')))
		{
		  el.setAttribute("autocomplete", "off");
		}
    nowFieldValidatorInitElement(el);
  })

  $$('.validate input.invalid, .validate select.invalid, .validate textarea.invalid').each(function(el)
  {
    nowFieldValidatorInitInvalid(el);
  })

  nowValidatorErrorBehaviour();
}
