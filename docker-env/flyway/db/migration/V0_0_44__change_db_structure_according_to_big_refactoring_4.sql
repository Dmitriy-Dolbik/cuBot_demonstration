INSERT INTO meters(number, reading_type, apartment_id) SELECT number, 'ELECTRICITY', apartment_id FROM electricity_meters;

DROP TABLE hot_water_meters;
DROP TABLE cold_water_meters;
DROP TABLE electricity_meters;


CREATE TABLE readings (
    id bigint PRIMARY KEY,
    value numeric(8,3) NOT NULL,
    reading_type varchar(255) NOT NULL,
    created_date timestamp with time zone,
    meter_id bigint REFERENCES meters(id) ON DELETE CASCADE
)