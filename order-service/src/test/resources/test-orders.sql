truncate table orders cascade;
alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 100;

insert into orders (id, order_number, username, customer_name, customer_email, customer_phone, delivery_address,
    delivery_country, status, comments, created_at, updated_at )
values
    (1, 'order-123','user','John Doe','john.doe@example.com',
          '+2348012345678','12 Rayfield Road','Nigeria','NEW',
          'First test order',now(),now()),
    (2,'order-124','user','Alice Smith', 'alice.smith@example.com',
          '+2348098765432','45 Zarmaganda Street','Nigeria','NEW',
          'Second test order', now(), now() );


insert into order_items (code, name, price, quantity, order_id)
values
    ('ITEM-001', 'Book: Spring Boot in Action', 12000, 1, 1),
    ('ITEM-002', 'Book: Mastering React', 15000, 2, 1),
    ('ITEM-003', 'Book: Microservices Patterns', 18000, 1, 2),
    ('ITEM-004', 'Book: PostgreSQL Essentials', 10000, 3, 2);

