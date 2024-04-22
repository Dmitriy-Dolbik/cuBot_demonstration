ALTER TABLE cu_receipt RENAME COLUMN cold_water_rate to cold_water_cost;
ALTER TABLE cu_receipt RENAME COLUMN hot_water_rate to hot_water_cost;
ALTER TABLE cu_receipt RENAME COLUMN cold_water_consumption to water_disposal_cost;
ALTER TABLE cu_receipt DROP COLUMN hot_water_consumption;

UPDATE cu_receipt SET hot_water_cost = 891.32, cold_water_cost = 214.64, water_disposal_cost = 471.74;