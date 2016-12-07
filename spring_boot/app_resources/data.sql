insert into Customer (id, firstname, lastname ) values (1, 'jeden', 'jeden');

insert into product (id, code, name ) values (1, 'GOLD', 'złoto');
insert into product (id, code, name ) values (2, 'SLIVER', 'Srebro ŻŹĆżź');
insert into product (id, code, name ) values (3, 'B1', 'Brąz');
insert into product (id, code, name ) values (4, 'A1', 'Aliminium');

insert into product_parameter_type (id, name) values ( 1, 'waga' );
insert into product_parameter_type (id, name) values ( 2, 'dlugosc' );
insert into product_parameter_type (id, name) values ( 3, 'szerokosc' );
insert into product_parameter_type (id, name) values ( 4, 'penetracja' );

insert into product_parameter (id, type_id, product_id, value, big_value ) values ( 1, 4, 1, 'zaza1', null );
insert into product_parameter (id, type_id, product_id, value, big_value ) values ( 2, 4, 1, 'zaza2', null );
insert into product_parameter (id, type_id, product_id, value, big_value ) values ( 3, 2, 1, '101', null );
insert into product_parameter (id, type_id, product_id, value, big_value ) values ( 4, 2, 2, '101', null );

