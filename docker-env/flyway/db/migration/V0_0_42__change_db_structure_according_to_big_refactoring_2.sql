ALTER TABLE hot_water_readings drop constraint hot_water_readings_meter_id_fkey;
drop table hot_water_readings;

ALTER TABLE cold_water_readings drop constraint cold_water_readings_meter_id_fkey;
drop table cold_water_readings;

ALTER TABLE electricity_readings drop constraint electricity_readings_meter_id_fkey;
drop table electricity_readings;



--UPDATE hot_water_meters SET id = id + (SELECT MAX(id) FROM meters);
--UPDATE electricity_meters SET id = id + (SELECT MAX(id) FROM hot_water_meters);
--
--INSERT INTO meters(id, number, reading_type, apartment_id) SELECT id, number, 'HOT', apartment_id FROM hot_water_meters;
--INSERT INTO meters(id, number, reading_type, apartment_id) SELECT id, number, 'ELECTRICITY', apartment_id FROM electricity_meters;
--
--CREATE TABLE readings (
--    id bigint PRIMARY KEY,
--    value numeric(8,3) NOT NULL,
--    reading_type varchar(255) NOT NULL,
--    created_date timestamp with time zone,
--    meter_id bigint REFERENCES meters(id) ON DELETE CASCADE
--)
--
--INSERT INTO readings(id, value, reading_type, created_date, meter_id) SELECT id, value, 'COLD', created_date, meter_id FROM cold_water_readings;
--INSERT INTO readings(id, value, reading_type, created_date, meter_id) SELECT
--    (SELECT COALESCE(MAX(id), 0) FROM hot_water_readings) + ROW_NUMBER() OVER (ORDER BY id), value, 'HOT', created_date, meter_id FROM hot_water_readings;
--INSERT INTO readings(id, value, reading_type, created_date, meter_id) SELECT
--    (SELECT COALESCE(MAX(id), 0) FROM electricity_readings) + ROW_NUMBER OVER (ORDER BY id), value, 'ELECTRICITY', created_date, meter_id FROM electricity_readings;

--DROP TABLE hot_water_meters;
--DROP TABLE cold_water_meters;
--DROP TABLE electricity_meters;
--
----DROP TABLE cold_water_readings;
----DROP TABLE hot_water_readings;
----DROP TABLE electricity_readings;