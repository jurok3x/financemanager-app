CREATE TABLE IF NOT EXISTS EXPENSES
(
    ID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME varchar(255),
    PRICE double,
    DATE date,
    CATEGORY_ID int,
    USER_ID int
);