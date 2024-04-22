ALTER TABLE cold_water_readings ALTER COLUMN id TYPE bigint USING id::bigint;
ALTER TABLE hot_water_readings ALTER COLUMN id TYPE bigint USING id::bigint;
ALTER TABLE electricity_readings ALTER COLUMN id TYPE bigint USING id::bigint;