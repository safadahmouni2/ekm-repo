
function AutoCompletion_GetLocalData(inputCtrl)
{
  var localData = new Array();
  dataListName = InputField2ComboBox_GetDataListName (inputCtrl);
  if (dataListName && "" != dataListName)
  {
    var classListName = dataListName + "_List";
    var theData = eval(classListName);
    localData = theData.split(',');
  }
  return localData;
}

function AutoCompletion_Init ()
{
	$$("input.autoComplete").each(function(inputCtrl)
	{
    var dataList = AutoCompletion_GetLocalData(inputCtrl);
    if (dataList.length > 0)
    {
      acmId = "autoCompleteMenu";
      acmClass = "autoCompleteMenu";
      if (IsRequired(inputCtrl))
      {
        acmId += "Required";
        acmClass += " required";
      }
      if (!$(acmId))
      {
        getBody().insert ({bottom: "<div id='"+acmId+"' class='"+acmClass+"'></div>"});
      }
	    new Autocompleter.Local(
          inputCtrl.identify(),
          acmId,
          dataList,
          {ignoreCase:true,frequency:0.1,minChars:1});
    }
	});
}
