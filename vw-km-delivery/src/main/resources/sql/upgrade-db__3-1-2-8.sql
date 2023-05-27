

-- Add two Columns in T_BENUTZER table, one for the validationCode and the other for the SentCodeDate --
Alter table T_BENUTZER ADD  AKTIVIERUNGS_CODE VARCHAR2(36 CHAR);
         
Alter table T_BENUTZER ADD AKTIVIERUNGS_CODE_GESENDET_AM DATE;