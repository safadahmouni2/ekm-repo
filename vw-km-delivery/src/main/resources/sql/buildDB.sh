#!/bin/sh




echo "building Database..."

NLS_LANG=GERMAN
export NLS_LANG
echo $NLS_LANG

sqlplus @prop.application.db.system_user@/@prop.application.db.system_pwd@@@prop.application.db.tns_name@ @buildDB.sql

