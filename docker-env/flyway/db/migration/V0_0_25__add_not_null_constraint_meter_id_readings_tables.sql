ALTER TABLE cold_water_readings ALTER COLUMN meter_id SET NOT NULL;
ALTER TABLE hot_water_readings ALTER COLUMN meter_id SET NOT NULL;
ALTER TABLE electricity_readings ALTER COLUMN meter_id SET NOT NULL;