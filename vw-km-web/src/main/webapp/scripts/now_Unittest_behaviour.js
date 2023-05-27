
function handleUnitTestDropElem (dropElem)
{
  var name = dropElem.down("label").innerHTML;

  var elType = "";
  elem = dropElem.down("input");
  if (!IsDefined(elem))
  {
    elem = dropElem.down("select");
    elType = "listBox";
  }
  else
  {
    elType = elem.type;
  }

  var value = $F(elem);
  var id = elem.identify();

  var toolTip = id + ": ["+elType+"]: " + name + " = " + value;
  var testItem = "<li title='"+toolTip+"'><strong>"+name+"</strong>: "+value+"</li>";

	var dropZone = $$(".unitTestDropContainer .droppZone").first();
	dropZone.insert(testItem);

  Tooltips_Behaviour_Init (".unitTestDropContainer *[title]");

  return false;
}

function enableUnitTest (oThis)
{

  return false;
}

function Unit_Behaviour_Init ()
{
	$$("a.enableUnitTest").each( function(el) {
 		el.addClassName("cursorPointer");
    el.title = "Click to enable unit test";
 		el.onclick = function()
		{
      return enableUnitTest (this);
    }
	});

	$$("a.enableUnitTest").each( function(el) {
 		el.addClassName("cursorPointer");
    el.title = "Click to enable unit test";
 		el.onclick = function()
		{
      return enableUnitTest (this);
    }
	});

}
