ALTER TABLE cu_receipt ADD COLUMN cold_water_rate NUMERIC(10,2);
ALTER TABLE cu_receipt ADD COLUMN hot_water_rate NUMERIC(10,2);

UPDATE cu_receipt SET cold_water_rate = 36.54, hot_water_rate = 126.68 WHERE id=1;

ALTER TABLE cu_receipt ALTER COLUMN cold_water_rate SET NOT NUll;
ALTER TABLE cu_receipt ALTER COLUMN hot_water_rate SET NOT NUll;