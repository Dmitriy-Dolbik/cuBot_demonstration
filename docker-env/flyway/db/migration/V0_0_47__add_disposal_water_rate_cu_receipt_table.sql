ALTER TABLE cu_receipt ADD COLUMN disposal_water_rate numeric(10,2);
UPDATE cu_receipt SET disposal_water_rate = 36.54 WHERE id = 1;
ALTER TABLE cu_receipt ALTER COLUMN disposal_water_rate set not null;
