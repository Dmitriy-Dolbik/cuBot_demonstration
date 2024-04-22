CREATE TABLE meters
(
    id bigint PRIMARY KEY,
    number bigint NOT NULL UNIQUE,
    reading_type varchar(255) NOT NULL,
    apartment_id smallint REFERENCES apartments(id) ON DELETE SET NULL

);

INSERT INTO meters(id, number, reading_type ,apartment_id) SELECT id, number, 'COLD', apartment_id FROM cold_water_meters;