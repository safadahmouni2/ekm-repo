ALTER TABLE T_BAUSTEIN DROP CONSTRAINT BAUSTEIN_BAUSTEIN_NR_UK DROP INDEX;
ALTER TABLE T_BAUSTEIN ADD (CONSTRAINT BAUSTEIN_BAUSTEIN_NR_UK UNIQUE (BAUSTEIN_NR,BESITZER)
 USING INDEX PCTFREE 10 TABLESPACE @prop.application.db.tablespace.index@);
 
ALTER TABLE T_BAUSTEIN DROP CONSTRAINT BAUSTEIN_BEZEICHNUNG_UK DROP INDEX;
ALTER TABLE T_BAUSTEIN ADD (CONSTRAINT BAUSTEIN_BEZEICHNUNG_UK UNIQUE   (BEZEICHNUNG,BESITZER)
 USING INDEX  PCTFREE 10 TABLESPACE @prop.application.db.tablespace.index@); 

ALTER TABLE T_ELEMENT DROP CONSTRAINT ELEMENT_ELEMENT_NR_UK DROP INDEX;
ALTER TABLE T_ELEMENT ADD (CONSTRAINT ELEMENT_ELEMENT_NR_UK UNIQUE (ELEMENT_NR,BESITZER)
 USING INDEX  PCTFREE 10 TABLESPACE @prop.application.db.tablespace.index@);

ALTER TABLE T_ELEMENT DROP CONSTRAINT ELEMENT_BEZEICHNUNG_UK DROP INDEX; 
ALTER TABLE T_ELEMENT ADD (CONSTRAINT ELEMENT_BEZEICHNUNG_UK UNIQUE (BEZEICHNUNG,BESITZER)
 USING INDEX  PCTFREE 10 TABLESPACE @prop.application.db.tablespace.index@); 