create table if not exists voucher
(
    id varchar PRIMARY KEY,
    code varchar,
    validation TIMESTAMP NOT NULL,
    expiration TIMESTAMP NOT NULL,
    creation_datetime TIMESTAMP NOT NULL,
    customer_id varchar references customer(id)
    );