--Update T_KOSTENATTRIBUT 
UPDATE T_KOSTENATTRIBUT SET BEZEICHNUNG='M - Montage, Inbetriebnahme bei AN' WHERE BEZEICHNUNG='M - Montage, Inbetriebnahme AN'; 
UPDATE T_KOSTENATTRIBUT SET BEZEICHNUNG='M - Montage, Inbetriebnahme bei AG' WHERE BEZEICHNUNG='M - Montage, Inbetriebnahme AG';

--Update T_BAUSTEINVERSIONANDERUNG
UPDATE T_BAUSTEINVERSIONANDERUNG set ANDERUNG='M - Montage, Inbetriebnahme bei AN' where ANDERUNG='M - Montage, Inbetriebnahme AN' AND ART='ATTRIBUTE_VALUE';
UPDATE T_BAUSTEINVERSIONANDERUNG set ANDERUNG='M - Montage, Inbetriebnahme bei AG' where ANDERUNG='M - Montage, Inbetriebnahme AG' AND ART='ATTRIBUTE_VALUE';

--Update T_ELEMENTVERSIONANDERUNG
UPDATE  T_ELEMENTVERSIONANDERUNG set ANDERUNG='M - Montage, Inbetriebnahme bei AN' where ANDERUNG='M - Montage, Inbetriebnahme AN' AND ART='ATTRIBUTE_VALUE';
UPDATE  T_ELEMENTVERSIONANDERUNG set ANDERUNG='M - Montage, Inbetriebnahme bei AG' where ANDERUNG='M - Montage, Inbetriebnahme AG' AND ART='ATTRIBUTE_VALUE';