
Date.prototype.toFormattedString = function(useTime, format, useText)
{
  str = "";
  switch (format) 
  {
    case "isoDate":
      str = calendarDateFormat_isoDate(this, useTime, useText);
      break;
    case "fullDate":
      str = calendarDateFormat_fullDate(this, useTime, useText);
      break;
    case "weekYear":
     str = calendarDateFormat_weekYear(this, useTime, useText);
      break;
    default:
      str = calendarDateFormat_default(this, useTime, useText);
      break;
  }
  return str;
}

Date.parseFormattedString = function (string, format)
{

  date = new Date();

  switch (format) 
  {
    case "weekYear":
     var kwYear= string.split("/");
     var kw = kwYear[0];
     var year = kwYear[1];
     var days = 7 * (kw-1) + 3;
     month = Math.floor(days / 30)
     dayFrac = days - 30*month;
     date.setFullYear(year);
     date.setMonth(month);
     date.setDate(dayFrac);
   //  local_alert ("date("+string+") kw=["+kw+"];days="+days+";   "+dayFrac+"."+month+"."+year);
     break;

    case "fullDate":
    default:
      // DD.MM.YYYY (Tag.Monat.Jahr) z.B.: 17.03.2009
      var aDate = string.split(".");
      var d = aDate[0];
      var m = aDate[1];
      var y = aDate[2];
      date = (new Date(y, m-1, d)).stripTime();
     break;
  }

 return date;

  var regexp = "([0-9]{4})(-([0-9]{2})(-([0-9]{2})" +
      "( ([0-9]{1,2}):([0-9]{2})? *(pm|am)" +
      "?)?)?)?";
  var d = string.match(new RegExp(regexp, "i"));
  if (d==null) return Date.parse(string); // at least give javascript a crack at it.
  var offset = 0;
  var date = new Date(d[1], 0, 1);
  if (d[3]) { date.setMonth(d[3] - 1); }
  if (d[5]) { date.setDate(d[5]); }
  if (d[7]) {
    hours = parseInt(d[7], 10);
    offset=0;
    is_pm = (d[9].toLowerCase()=="pm")
    if (is_pm && hours <= 11) hours+=12;
    if (!is_pm && hours == 12) hours=0;
    date.setHours(hours); 
    
  }
  if (d[8]) { date.setMinutes(d[8]); }
  if (d[10]) { date.setSeconds(d[10]); }
  if (d[12]) { date.setMilliseconds(Number("0." + d[12]) * 1000); }
  if (d[14]) {
      offset = (Number(d[16]) * 60) + Number(d[17]);
      offset *= ((d[15] == '-') ? 1 : -1);
  }

  return date;
}

function calendarDateFormat_fullDate(oThis, useTime)
{
  var idxDay = (oThis.getDay()+7-Date.first_day_of_week)%7
  return Date.dayNames[idxDay] + ", " + Date.padded2(oThis.getDate()) + ". " + Date.months[oThis.getMonth()] + " " + oThis.getFullYear();
}

function calendarDateFormat_isoDate (oThis, useTime)
{
  return oThis.getFullYear() + "-" + Date.padded2(oThis.getMonth() + 1) + "-" +Date.padded2(oThis.getDate()); 
}

MonatsTage= new Array();
MonatsTage[1]= 31; MonatsTage[2]= 28; MonatsTage[3]= 31;
MonatsTage[4]= 30; MonatsTage[5]= 31; MonatsTage[6]= 30;
MonatsTage[7]= 31; MonatsTage[8]= 31; MonatsTage[9]= 30;
MonatsTage[10]= 31; MonatsTage[11]= 30; MonatsTage[12]= 31;

function dattage(Ta, Mo, Ja) {
  var dattag = 365 * Ja + Ta;
  for (i=1;i<Mo;i++) dattag+= MonatsTage[i]
  if (Ja < 1582) {
    dattag+= 10;
    for (i = 1580; i >= Ja; i-= 4 )
      dattag--;
  }
  else
  if (Ja == 1582) {
    if (Mo == 10) {
      if (Ta < 5) dattag+= 10;
      else
      if (Ta < 15) dattag = -99;
    }
    else if (Mo < 10) dattag+= 10;
  }
  else
  if (Ja > 2499) dattag = -99;
  else {
    for (i = 1584; i < Ja; i+= 4)
      dattag++;
    for (i = 1700; i < Ja; i+= 100)
      if ((i % 400) > 0) dattag--;
  }
  if (dattag > 0) {
    if ((Ja % 4) == 0 && Mo > 2 & (Ja < 1582 || (Ja % 100) > 0 || (Ja % 400) == 0)) dattag++;
  }
  return dattag;
}


function dattagT(TaT,MoT,JaT) {
  var dt, MonatsTag;
  Ta = parseInt(TaT);
  Mo = parseInt(MoT);
  Ja = parseInt(JaT);
  if (Ja > 2499 || Ja < 1) {
    return "Als Jahreszahlen sind nur 1 bis 2499 erlaubt!";
  }
  else {
    if (Ja<100) {
      return "Jahreszahlen unter 100 werden als Daten des 1. Jahrhunderts n. Chr. interpretiert.";
    }
    if (Mo > 12 || Mo < 1) {
      return "Als Monate bitte nur Zahlen von 1 bis 12 eingeben!";
    }
    else {
      if (Ta > 31 || Ta < 1) {
        return "Als Tage bitte nur Zahlen von 1 bis 31 eingeben!";
      }
      else {
        MonatsTag = MonatsTage[Mo];
        if (Mo == 2)
        {
          if ((Ja % 4) == 0 && (Ja < 1582 || (Ja % 100) > 0 || (Ja % 400) == 0)) MonatsTag = 29;
        }
        if (Ta > MonatsTag) {
          var zwi="Der Monat " + Mo + " hat nur " + MonatsTag + " Tage!";
          if (Mo == 2) {
            zwi="Der Monat Februar hat im Jahr "+Ja+" nur " + MonatsTag + " Tage!";
          }
          return zwi;
        }
        else
        {
          dt = dattage(Ta, Mo, Ja);
          if (dt <= -20)
          {
            return "Unerlaubtes Datum!";
          }
        }
      }
    }
  }
  return dt;
}

function kalwv(kJahr,kdtg) {
  var wth, datagw, kW;
  datagw = dattagT(4, 01, kJahr);
  if (isNaN(datagw)){return datagw;}
  wth = (datagw + 24) % 7;
  datagw = datagw - wth;
  kW = Math.floor((kdtg+7-datagw)/7)
  return kW;
}

function kalwo(kJ1,dtg) {
  var kT, kM, kJ, kalw, kalww, kW, kalJ;
  kJ=parseInt(kJ1);
  if (kJ>1975 && kJ<2500) {
    kalw = kalwv(kJ,dtg)
    if (isNaN(kalw)){return kalw;}
    if (kalw < 1)
    {
      kalw = kalwv(kJ-1,dtg); 
      if (isNaN(kalw)){return kalw;}
      kalJ = kJ-1;
    }
    else { kalJ = kJ;
      if (kalw > 52) {
        kalww = kalwv(kJ+1,dtg);
        if (isNaN(kalww)){return kalww;}
        if (kalww>0) { kalw = kalww; kalJ = kJ+1 }
      }
    }
    // kW = "" + kalw + ". Kalenderwoche " + kalJ
    kW = kalw;
  }
  else kW="";
  return kW;
}

function CalcKalenderWoche(day, mon, year)
{
  datag = dattagT(day, mon+1, year);
  if (isNaN(datag)){return datag;}
  return kalwo(year,datag);
}


function calendarDateFormat_weekYear(oThis, useTime, useText)
{
  var kw = CalcKalenderWoche(oThis.getDate(), oThis.getMonth(), oThis.getFullYear());
  if (isNaN(kw)){return kw;}
  if (useText)
  {
    return "Kalenderwoche " + Date.padded2(kw) + "/" + oThis.getFullYear(); 
  }
  else
  {
    return Date.padded2(kw) + "/" + oThis.getFullYear(); 
  }
}
function calendarDateFormat_default(oThis, useTime)
{
  return Date.padded2(oThis.getDate()) + "." + Date.padded2(oThis.getMonth() + 1) + "." + oThis.getFullYear(); 
}


