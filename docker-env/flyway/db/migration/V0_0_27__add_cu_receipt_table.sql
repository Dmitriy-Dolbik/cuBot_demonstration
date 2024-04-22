CREATE TABLE cu_receipt
(
    id BIGSERIAL PRIMARY KEY,
    total_cost NUMERIC(10,2) NOT NULL,
    electricity_cost NUMERIC(10,2) NOT NULL,
    cold_water_rate Numeric(10,2) NOT NULL,
    hot_water_rate numeric(10,2) NOT NULL,
    cold_water_consumption NUMERIC(10,2) NOT NULL,
    hot_water_consumption NUMERIC(10,2) NOT NULL,
    electricity_day_rate  NUMERIC(10,2) NOT NULL,
    internetPayment  NUMERIC(10,2) NOT NULL
);