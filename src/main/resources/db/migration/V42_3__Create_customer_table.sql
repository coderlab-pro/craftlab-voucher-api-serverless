create table if not exists customer
(
    id varchar
        constraint customer_pk primary key
);

insert into customer (id)
values ('customer-id-1')