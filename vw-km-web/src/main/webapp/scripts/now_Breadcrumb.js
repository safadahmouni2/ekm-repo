
function Breadcrumb_Init()
{
  var oBct = $('bct');
  try
  {
    if (IsDefined(oBct))
    {
      var bct = "";
      var prevLink = "";
      var prevName = "";
      $$("div.nowMenu ul li.active>a").each(function(el)
      {
        if (prevLink.length > 0)
        {
          bct += "<li>"+prevLink+"</li>";
        }
        prevName = el.innerHTML;
        var href = el.href.replace('#CLICKOPEN','');
        prevLink = "<a href='"+href+"'>"+el.innerHTML+"</a>";
      })
      if (prevName.length > 0)
      {
        bct += "<li><strong>"+prevName+"</strong></li>";
      }
      if (bct.length > 0)
      {
        bct = "<ul>" + bct + "</ul>";
        oBct.update(bct);
      }
    }
  } catch (e) {
    local_alert ("ERR: Breadcrumb_Init execption = (" + e + ")"); alert ("typeof (oBct) = (" + typeof (oBct) + ")");
  }
}


