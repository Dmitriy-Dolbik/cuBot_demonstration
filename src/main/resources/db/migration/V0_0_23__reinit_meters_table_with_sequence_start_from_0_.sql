DELETE FROM cold_water_meters;
DELETE FROM hot_water_meters;
DELETE FROM electricity_meters;

ALTER SEQUENCE cold_water_meters_id_seq RESTART WITH 1;
ALTER SEQUENCE hot_water_meters_id_seq RESTART WITH 1;
ALTER SEQUENCE electricity_meters_id_seq RESTART WITH 1;

INSERT INTO cold_water_meters(number, apartment_id) VALUES ('180802941', '1') ;
INSERT INTO hot_water_meters(number, apartment_id) VALUES ('180709315', '1') ;
INSERT INTO electricity_meters(number, apartment_id) VALUES ('012358136396818', '1');

INSERT INTO cold_water_meters(number, apartment_id) VALUES ('180559444', '2');
INSERT INTO hot_water_meters(number, apartment_id) VALUES ('180802484', '2');
INSERT INTO electricity_meters(number, apartment_id) VALUES ('012358136480722', '2');

INSERT INTO cold_water_meters(number, apartment_id) VALUES ('180699605', '3');
INSERT INTO hot_water_meters(number, apartment_id) VALUES ('180801782', '3');
INSERT INTO electricity_meters(number, apartment_id) VALUES ('012358136480670', '3');

INSERT INTO cold_water_meters(number, apartment_id) VALUES ('180775724', '4');
INSERT INTO hot_water_meters(number, apartment_id) VALUES ('180785465', '4');
INSERT INTO electricity_meters(number, apartment_id) VALUES ('012358136392774', '4');