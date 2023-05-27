

function SoftScroll_Behaviour_Init ()
{
	$$('#areaContent a[href^="#"], #areaFooter a[href="#refTop"], #areaFooter a[href="#refBottom"], #errReportWin .foot a[href^="#"]').each(function(el)
  {
		if ("function" != typeof (el.onclick))
    {
		  el.onclick = function()
      {
        var aName = this.href.split("#")[1];
        if (aName && aName.length > 0)
				{
					var selector = 'a[name="'+aName+'"]';
					var aEle = $$(selector);
					if (!IsDefined(aEle))
					{
						now_debug_out (selector + " matched elements: " + aEle.length);
						return false;
					}
					
					if ("refTop" == aName || "refBottom" == aName)
					{
						var softScroll = $('areaFooter').down ("ul li input[value=usp_softScroll]");
						if (IsDefined(softScroll) && !softScroll.checked)
						{
							return true;
						}
					}
					
					var off = 0;
					if (IsDefined('areaContainer'))
					{
						// off = -Position.cumulativeOffset($('areaContainer'))[1];

						if ("refBottom" == aName)
						{
							var nHeight = document.viewport.getDimensions().height;
							var pdBtm = readStyleVal(getBody(), "padding-bottom")
							if (pdBtm > 0)
							{
								off = -nHeight + 40;
							}
						}
					}
					else
					{
						 now_debug_out ("areaContainer is undefined");
					}
					new Effect.ScrollTo(aEle.first(),{offset:off, duration:g_scrollDuration});
				}
        return false;
		  }
    }
  })
}

// =================================================================
