

ALTER TABLE T_KOSTENATTRIBUT ADD KA_VERRECHNUNG_REF_ID  NUMBER(19,0);


UPDATE T_KOSTENATTRIBUT SET KA_VERRECHNUNG_REF_ID = 9 where ID= 41 ;  


UPDATE T_KOSTENATTRIBUT SET KA_VERRECHNUNG_REF_ID = 7 where ID= 8 ;  


update T_KOSTENATTRIBUT set BESCHREIBUNG = BEZEICHNUNG ;


update T_KOSTENATTRIBUT SET BESCHREIBUNG = 'M - Planung, Methodenplanung' where ID IN (2,3,4);
UPDATE T_KOSTENATTRIBUT SET KA_VERRECHNUNG_REF_ID = 2 where ID in (3,4) ;


update T_KOSTENATTRIBUT SET BESCHREIBUNG = 'E - Planung, Methodenplanung' where ID IN (27,28,29);
UPDATE T_KOSTENATTRIBUT SET KA_VERRECHNUNG_REF_ID = 27 where ID in (28,29) ;

--- update wert to be equal in the same catalog (kostAttributeID 41 must be equal with kostAttributeID 9 )
update T_STUNDENSAETZE std set std.wert = (select wert from T_STUNDENSAETZE where KOSTEN_ATTRIBUT_REF_ID = 9 and STD_SATZ_KAT_REF_ID = std.STD_SATZ_KAT_REF_ID)
where std.KOSTEN_ATTRIBUT_REF_ID = 41;

--- update wert to be equal in the same catalog (kostAttributeID 8 must be equal with kostAttributeID 7 )
update T_STUNDENSAETZE std set std.wert = (select wert from T_STUNDENSAETZE where KOSTEN_ATTRIBUT_REF_ID = 7 and STD_SATZ_KAT_REF_ID = std.STD_SATZ_KAT_REF_ID)
where std.KOSTEN_ATTRIBUT_REF_ID = 8;

--- update wert to be equal in the same catalog (kostAttributeID (3,4) must be equal with kostAttributeID 2 )
update T_STUNDENSAETZE std set std.wert = (select wert from T_STUNDENSAETZE where KOSTEN_ATTRIBUT_REF_ID = 2 and STD_SATZ_KAT_REF_ID = std.STD_SATZ_KAT_REF_ID)
where std.KOSTEN_ATTRIBUT_REF_ID IN (3,4);

--- update wert to be equal in the same catalog (kostAttributeID (28,29) must be equal with kostAttributeID 27 )
update T_STUNDENSAETZE std set std.wert = (select wert from T_STUNDENSAETZE where KOSTEN_ATTRIBUT_REF_ID = 27 and STD_SATZ_KAT_REF_ID = std.STD_SATZ_KAT_REF_ID)
where std.KOSTEN_ATTRIBUT_REF_ID IN (28,29);
