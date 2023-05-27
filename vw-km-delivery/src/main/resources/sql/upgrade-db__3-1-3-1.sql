UPDATE T_KOSTENATTRIBUT SET BEZEICHNUNG = 'M - Off-Line-Untersuchung (Simulation)', BESCHREIBUNG='M - Off-Line-Untersuchung (Simulation)' WHERE ID = 6;
UPDATE T_KOSTENATTRIBUT SET BEZEICHNUNG = 'M - Off-Line-Programmierung (Simulation)', BESCHREIBUNG='M - Off-Line-Programmierung (Simulation)' WHERE ID = 9;
UPDATE T_KOSTENATTRIBUT SET BEZEICHNUNG = 'E - Off-Line-Programmierung (Simulation)', BESCHREIBUNG='E - Off-Line-Programmierung (Simulation)' WHERE ID = 41;

ALTER TABLE T_BENUTZER ADD DATENSCHUTZ_HIN_BESTAETIGT_AM DATE ;