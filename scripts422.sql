CREATE TABLE car (
	id SERIAL PRIMARY KEY,
	brand VARCHAR(20),
	model VARCHAR(20),
	price NUMERIC(11,2)
);

CREATE TABLE human (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50),
	age INT,
	is_driver_license BOOLEAN,
	id_car INT,
	foreign key (id_car) REFERENCES car(id)
);