INSERT INTO T_ORDNER (ID,BEZEICHNUNG,OBJEKT_TYP_REF_ID,ERSTELLER_REF_ID) 
SELECT SEQ_FOLDER.NEXTVAL,'Element Ordner_'||USER_ID,1, USER_ID FROM T_BENUTZER ;

INSERT INTO T_ORDNER (ID,BEZEICHNUNG,OBJEKT_TYP_REF_ID,ERSTELLER_REF_ID) 
SELECT SEQ_FOLDER.NEXTVAL,'Baustein Ordner_'||USER_ID,2, USER_ID FROM T_BENUTZER ;

commit;