-- Adding new rolle and affect it to the ROOT_ADMINISTRATOR
insert into T_ROLLEN(ID,BEZEICHNUNG,BESCHREIBUNG) values(5,'INVESTERMITTLER','Investermittler');
insert into T_REL_ROLLEN_ROLLEN (ROLLEN_ID_REF, MANAGED_ROLLEN_ID_REF) values (1,5);

--Adding new column KOSTEN_GRUPPE ON  T_BAUSTEINVERSION
 ALTER TABLE T_BAUSTEINVERSION ADD KOSTEN_GRUPPE VARCHAR2(1 CHAR);

-- Adding new rights : MANAGE_CALCULATOR_MODULE & MANAGE_COST_MANAGEMENT_MODULE
insert into T_RECHT (RECHT_ID, KODE) values (9,'MANAGE_CALCULATOR_MODULE');
insert into T_RECHT (RECHT_ID, KODE) values (10,'MANAGE_COST_MANAGEMENT_MODULE');
-- Affect new rights to their roles
insert into T_REL_ROLLEN_RECHT (ROLLEN_ID_REF, RECHT_ID_REF) values(1,9);
insert into T_REL_ROLLEN_RECHT (ROLLEN_ID_REF, RECHT_ID_REF) values(2,9);
insert into T_REL_ROLLEN_RECHT (ROLLEN_ID_REF, RECHT_ID_REF) values(3,9);
insert into T_REL_ROLLEN_RECHT (ROLLEN_ID_REF, RECHT_ID_REF) values(4,9);
insert into T_REL_ROLLEN_RECHT (ROLLEN_ID_REF, RECHT_ID_REF) values(5,10);