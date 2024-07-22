ALTER TABLE customer ADD COLUMN name VARCHAR;
ALTER TABLE customer ADD COLUMN mail VARCHAR;


UPDATE customer
SET name = 'Paul Updated', mail = 'paul.updated@gmail.com'
WHERE id = 'customer-id-1';

