-- ============================================================
--   Database name :   VW_KM
--   DBMS name     :   Oracle
--   Version       :   $Revision: 1.1 $
--   Date          :   $Date: 2018/02/23 14:41:36 $
--   Author        :   Zied Saidi
--   Modifier      :   $Author: saidi $
--   Copyright: Copyright (c) 2018
--   Company: VW
-- ============================================================

-- this file contain query that should be executed only on the dev or test environment

-- insert temporary data for the real data of users table

update T_BENUTZER set     VORNAME  = regexp_replace(VORNAME, '(^| )([^ ])|.', '\2'),
	                        NACHNAME = regexp_replace(NACHNAME, '(^| )([^ ])|.', '\2'),
	                        EMAIL     ='test@test.de',
	                        TELEFON='00000-00-00000' ;
	                        