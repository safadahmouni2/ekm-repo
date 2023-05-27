function notSelectableFields_init ()
{
	// now_debug_out ("notSelectableFields_init...");
	$$('input.auth_dis,select.auth_dis').each(function(el)
  {
		SetSelectable(el, false);
	})
}

