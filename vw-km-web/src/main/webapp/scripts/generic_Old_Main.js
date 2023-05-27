/////////////////////////////////////////////////////////////////////////////
// Filename	  :	Main.js
// Project	  :	NewadaOnWeb .Frame
// Short Descr:	Common JS functions
//
// Created	  :	2003.11.13
// --------------------------------------------------------------------------
// Description:	Common Javasript functions

// function IsAlphaNumeric(Zeichen) 
// Test of being alphanumeric CharCode
// Param Zeichen: a char code like theString.charAt(0)
//
// function IsDigit(Zeichen) 
// Test of being CharCode of a digit
// Param Zeichen: a char code like theString.charAt(0)
//
// function IsCapitalLetter(Zeichen) 
// Test of being CharCode of a capital Letter (A - Z)
// Param Zeichen: a char code like theString.charAt(0)
//
// Test of being CharCode of a small Letter (a - z)
// Param Zeichen: a char code like theString.charAt(0)
//
// --------------------------------------------------------------------------
// Developer  :	Matthias Hillmer (MH)
//            :	Thomas Mueller   (TMUE)
// --------------------------------------------------------------------------
// Version    :	0, 0, 2
// Modified   : 2006.03.17
// Modified   : 2009.11.30
/////////////////////////////////////////////////////////////////////////////


// Test of being CharCode of a digit
// Param Zeichen: a char code like theString.charAt(0)
function IsDigit(Zeichen) 
{
  return ("0" <= Zeichen) && (Zeichen <= "9");
}

// Test of being CharCode of a small Letter (a - z)
// Param Zeichen: a char code like theString.charAt(0)
function IsSmallLetter(Zeichen) 
{
  return ("a" <= Zeichen) && (Zeichen <= "z");
}

// Test of being CharCode of a capital Letter (A - Z)
// Param Zeichen: a char code like theString.charAt(0)
function IsCapitalLetter(Zeichen) 
{
  return ("A" <= Zeichen) && (Zeichen <= "Z");
}

// Test of being alphanumeric CharCode
// Param Zeichen: a char code like theString.charAt(0)
function IsAlphaNumeric(Zeichen) 
{
  return IsSmallLetter(Zeichen) || IsCapitalLetter(Zeichen) || IsDigit(Zeichen);
}


