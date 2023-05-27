@echo off
echo building Database...

sqlplus @prop.application.db.admin_user@/@prop.application.db.admin_pwd@@@prop.application.db.tns_name@ as sysdba @createSchema.sql


sqlplus @prop.application.db.system_user@/@prop.application.db.system_pwd@@@prop.application.db.tns_name@ @buildDB.sql

