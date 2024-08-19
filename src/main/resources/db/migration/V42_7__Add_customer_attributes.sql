alter table customer
    add column if not exists name varchar,
    add column if not exists mail varchar;