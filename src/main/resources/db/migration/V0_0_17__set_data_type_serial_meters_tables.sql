CREATE SEQUENCE cold_water_meters_id_seq;
CREATE SEQUENCE hot_water_meters_id_seq;
CREATE SEQUENCE electricity_meters_id_seq;

ALTER TABLE cold_water_meters ALTER COLUMN id SET DEFAULT nextval('cold_water_meters_id_seq');
ALTER TABLE hot_water_meters ALTER COLUMN id SET DEFAULT nextval('hot_water_meters_id_seq');
ALTER TABLE electricity_meters ALTER COLUMN id SET DEFAULT nextval('electricity_meters_id_seq');