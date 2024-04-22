CREATE SEQUENCE readings_id_seq;
ALTER TABLE readings ALTER COLUMN id SET DEFAULT nextval('readings_id_seq');

INSERT INTO readings(value, reading_type, created_date, meter_id)
VALUES ('92.658', 'COLD', '2024-01-09 18:05:38.791329+03', 4),
        ('95.030', 'COLD', '2024-01-09 18:07:56.327633+03', 4),
        ('80.062', 'HOT', '2024-01-09 18:05:38.799457+03', 4),
        ('87.446', 'HOT', '2024-01-09 18:07:56.332276+03', 4),
        ('4117.8', 'ELECTRICITY', '2024-01-09 18:05:38.803007+03', 4),
        ('4180.5', 'ELECTRICITY', '2024-01-09 18:07:56.335762+03', 4);