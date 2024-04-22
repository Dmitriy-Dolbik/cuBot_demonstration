CREATE TABLE apartments
(
    id                 smallint PRIMARY KEY
);

CREATE TABLE users
(
    chat_id           bigint PRIMARY KEY,
    first_name        varchar(255),
    last_name         varchar(255),
    user_name         varchar(255) NOT NULL UNIQUE,
    role              varchar(255) NOT NULL,
    registered_date   timestamp with time zone,
    apartment_id      smallint NOT NULL REFERENCES apartments(id) ON DELETE SET NULL
);

CREATE TABLE cold_water_meters
(
    id                 int PRIMARY KEY,
    apartment_id       smallint UNIQUE REFERENCES apartments(id) ON DELETE SET NULL
);

CREATE TABLE hot_water_meters
(
    id                 int PRIMARY KEY,
    apartment_id       smallint UNIQUE REFERENCES apartments(id) ON DELETE SET NULL
);

CREATE TABLE electricity_meters
(
    id                 int PRIMARY KEY,
    apartment_id       smallint UNIQUE REFERENCES apartments(id) ON DELETE SET NULL
);

CREATE TABLE cold_water_readings
(
    id                  varchar PRIMARY KEY,
    value               numeric(8,3) NOT NULL UNIQUE,
    created_date        timestamp with time zone,
    meter_id            int REFERENCES cold_water_meters(id) ON DELETE SET NULL
);

CREATE TABLE hot_water_readings
(
        id              varchar PRIMARY KEY,
        value           numeric(8,3) NOT NULL UNIQUE,
        created_date    timestamp with time zone,
        meter_id        int REFERENCES hot_water_meters(id) ON DELETE SET NULL
);

CREATE TABLE electricity_readings
(
    id                 varchar PRIMARY KEY,
    value              numeric(6,1) NOT NULL UNIQUE,
    created_date       timestamp with time zone,
    meter_id           int REFERENCES electricity_meters(id) ON DELETE SET NULL
);



