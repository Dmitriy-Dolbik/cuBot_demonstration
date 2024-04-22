CREATE SEQUENCE cold_water_readings_id_seq;
CREATE SEQUENCE hot_water_readings_id_seq;
CREATE SEQUENCE electricity_readings_id_seq;

ALTER TABLE cold_water_readings ALTER COLUMN id SET DEFAULT nextval('cold_water_readings_id_seq');
ALTER TABLE hot_water_readings ALTER COLUMN id SET DEFAULT nextval('hot_water_readings_id_seq');
ALTER TABLE electricity_readings ALTER COLUMN id SET DEFAULT nextval('electricity_readings_id_seq');