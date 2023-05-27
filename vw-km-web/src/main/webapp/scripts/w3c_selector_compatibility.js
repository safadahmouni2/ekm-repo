
function w3c_selector_compatibility_Init ()
{
  if (Prototype.Browser.IE)
  {
    $$("#areaContent input[type=radio], #areaFooter input[type=radio]").invoke("addClassName", "radio");
    $$("#areaContent input[type=checkbox], #areaFooter input[type=checkbox]").invoke("addClassName", "checkbox");
    $$("#areaContent input[disabled],button[disabled]").invoke("addClassName", "disabled");
    $$("#areaContent table.konzernLayout8 th:first-child").invoke("addClassName", "firstChild");
    $$("#areaContent table.zebra > tbody > tr:nth-child(odd)").invoke("addClassName", "odd");
    $$("#areaContent ul.zebra > li:nth-child(odd)").invoke("addClassName", "odd");
  }
}

