ALTER TABLE T_ENUM_BAUSTEINTYP ADD BAUSTEIN_TYP_EN VARCHAR2(256 CHAR) ;

UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Workshop bay equipment' WHERE ID = 1;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Clinching technique' WHERE ID = 2;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Container' WHERE ID = 3;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Folding technique' WHERE ID = 4;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Handling and warehouse systems' WHERE ID = 5;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Building services engineering' WHERE ID = 6;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Bonding technique' WHERE ID = 7;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Laser technology' WHERE ID = 8;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Measuring and test engineering' WHERE ID = 9;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Rivet technology' WHERE ID = 10;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Roboter' WHERE ID = 11;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Robotic equipment ' WHERE ID = 12;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Threaded technology' WHERE ID = 13;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Security technology' WHERE ID = 14;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Weldandsoldering technique' WHERE ID = 15;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Special resources' WHERE ID = 16;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Structural steelwork' WHERE ID = 17;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Standard resources' WHERE ID = 18;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Control engineering' WHERE ID = 19;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Separation technique ' WHERE ID = 20;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Joining and separation technique' WHERE ID = 21;
UPDATE T_ENUM_BAUSTEINTYP SET BAUSTEIN_TYP_EN = 'Device technique' WHERE ID = 22;