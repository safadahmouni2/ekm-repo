update T_BAUSTEINVERSIONANDERUNG set ANDERUNG='Beistellung AG'  where ANDERUNG='bBeistellungenValid';
update T_BAUSTEIN set UUIDKB=null where ID in(select distinct TB1.ID  from T_BAUSTEIN TB1 join T_BAUSTEIN TB2 on (TB1.UUIDKB = TB2.UUIDKB) where  TB1.UUIDKB is not null and  TB1.id > TB2.id );
delete from T_REL_BENUTZER_ROLLEN where USER_REF_ID=1 and ROLLE_REF_ID=1;
insert into T_REL_BENUTZER_ROLLEN (USER_REF_ID,ROLLE_REF_ID) values (16,1);