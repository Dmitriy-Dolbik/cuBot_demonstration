CREATE TABLE wifi
(
    id              smallint PRIMARY KEY,
    login           varchar(20) NOT NULL,
    password        varchar(20) NOT NULL,
    apartment_id    smallint REFERENCES apartments(id) ON DELETE CASCADE
);

INSERT INTO wifi (id, login, password, apartment_id)
VALUES ('1', 'TP-Link_4B18', '30826953', '1'),
        ('2', 'TP-Link_4B18', '30826953', '2'),
        ('3', 'TP-Link_4B18', '30826953', '3'),
        ('4', 'TP-Link_4B18', '30826953', '4');