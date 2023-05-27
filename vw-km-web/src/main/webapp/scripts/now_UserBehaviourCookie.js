
g_mCookie = null; 

function fnUserBehaviourEvent (evnt, arg)
{
  if (IsDefined (g_mCookie)) {g_mCookie.set (evnt, arg);}
}

function userBehaviourCookie_Init ()
{
  if ((null == g_mCookie) && "function" == typeof(MergedCookie))
  {
    g_mCookie = new MergedCookie ({cookieName:'newadaWebUserSettings', delimiter:',', expireDays:0});
  }
}
