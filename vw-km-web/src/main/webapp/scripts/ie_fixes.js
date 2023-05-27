function ie_fixes_Init ()
{
  var client = getBodyClassValue("client_")
  if (client.length > 0)
  {
    getForm ().addClassName("client_" + client);
  }
}

