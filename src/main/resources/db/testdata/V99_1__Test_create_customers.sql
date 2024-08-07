alter table customer add column name varchar,
    add column mail varchar;

insert into customer
values ('customer-id-1', 'Paul Updated', 'paul.updated@gmail.com');