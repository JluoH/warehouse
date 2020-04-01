DROP SCHEMA IF EXISTS warehouse CASCADE;
CREATE SCHEMA warehouse;

CREATE TABLE warehouse.category
(
   name varchar(30) NOT NULL, 
   description text, 
   CONSTRAINT pk_c_name PRIMARY KEY (name)
);

CREATE TABLE warehouse.product
(
  category varchar(30) NOT NULL,
  image varchar(255),
  name varchar(50) NOT NULL,
  description text,
  price real NOT NULL DEFAULT 0,
  amount integer NOT NULL DEFAULT 0,
  CONSTRAINT pk_p_c_name PRIMARY KEY (category, name),
  CONSTRAINT fk_p_c_name FOREIGN KEY (category)
      REFERENCES warehouse.category (name)
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT negat_price CHECK (price >= 0),
  CONSTRAINT negat_amount CHECK (amount >= 0)
);

INSERT INTO warehouse.category(name, description) VALUES 
	   ('Куртки', 'Вид одежды, предназначенной для защиты от ветра, холода и дождя. Куртки имеют общие характеристики с куртками-парками, но отличаются от них длиной, которая может варьировать от уровня значительно ниже талии до середины бедра, но не ниже.'),
	   ('Ветровки', NULL),
	   ('Брюки', NULL),
	   ('Футболки', NULL),
	   ('Толстовки', NULL);
	   
INSERT INTO warehouse.product(category, image, name, description, price, amount) VALUES 
       ('Куртки', NULL, 'Outventure', 'Предоставляет двойной покрой и максимальную защиту от холода.', 2000, 12),
	   ('Куртки', NULL, 'Fila', NULL, 1300, 32),
	   ('Куртки', 'КурткиNike', 'Nike', NULL, 2500, 12),
	   ('Куртки', 'КурткиAdidas', 'Adidas', NULL, 600, 32),
	   ('Куртки', NULL, 'Demix', NULL, 560, 1),
	   ('Куртки', NULL, 'Reebok', NULL, 1000, 6),
	   ('Куртки', NULL, 'Puma', NULL, 2000, 10),
	   ('Куртки', NULL, 'Everest', NULL, 3000, 0),
	   ('Куртки', NULL, 'Ruman', NULL, 3600, 4),
	   ('Куртки', NULL, 'Teoden', NULL, 3600, 16),
	   ('Куртки', NULL, 'Rohan', NULL, 2440, 82),
	   ('Ветровки', NULL, 'Shir', NULL, 800, 2),
	   ('Ветровки', NULL, 'Nika', NULL, 100, 65),
	   ('Брюки', NULL, 'Bella', NULL, 540, 8),
	   ('Брюки', NULL, 'Tonks', NULL, 700, 0),
	   ('Футболки', NULL, 'Terra', NULL, 500, 12),
	   ('Футболки', NULL, 'Nova', NULL, 1200, 0),
	   ('Толстовки', NULL, 'Supreme', NULL, 500, 15), 
	   ('Толстовки', NULL, 'Joker', NULL, 900, 23);