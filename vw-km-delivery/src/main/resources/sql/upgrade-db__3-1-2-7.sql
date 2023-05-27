
-- Make the KostAttribute display on katalog Interface
update T_KOSTENATTRIBUT SET X_PREIS_PRO_EINHEIT=1 where ID IN (19,38); 

-- For Kostelement and Kostenbaustein which are already existing in DB,  
-- After Making X_PREIS_PRO_EINHEIT=1 of Kostattribute, in kalkulation interface the value will be set and get from Anzahl 
update T_ELEMENTWERTE SET ANZAHL = BETRAG where KOSTEN_ATTRIBUT_REF_ID in (19,38);
update T_BAUSTEINKOSTEN SET ANZAHL = BETRAG where KOSTEN_ATTRIBUT_REF_ID in (19,38);

-- ADD COLUMN 
Alter Table T_STUNDENSAETZE ADD FAKTOR NUMBER(19,2);


--to make the KostAttribute Id 19 and 38 display in  Kataolgs which existing in DB
insert into T_STUNDENSAETZE (ID,WERT,KOSTEN_ATTRIBUT_REF_ID,FAKTOR,STD_SATZ_KAT_REF_ID) select seq_hourly_rate.NEXTVAL,0, 19,100,ID from T_STDSATZKATALOG ;
insert into T_STUNDENSAETZE (ID,WERT,KOSTEN_ATTRIBUT_REF_ID,FAKTOR,STD_SATZ_KAT_REF_ID) select seq_hourly_rate.NEXTVAL,0, 38,100,ID from T_STDSATZKATALOG ;

--update the description of Kostattribute which will be display on katalog interface
update T_KOSTENATTRIBUT SET BESCHREIBUNG = 'M - Kaufteilfaktor'  where ID=19;
update T_KOSTENATTRIBUT SET BESCHREIBUNG = 'E - Kaufteilfaktor'  where ID=38;


-- delete unused data, because BETRAG column became unused for kostAtribute iD 19 and 38 
Update T_BAUSTEINKOSTEN SET BETRAG = null where KOSTEN_ATTRIBUT_REF_ID in (19,38);
Update T_ELEMENTWERTE SET BETRAG = null where KOSTEN_ATTRIBUT_REF_ID in (19,38);