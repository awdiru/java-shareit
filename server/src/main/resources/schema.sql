DROP TABLE IF EXISTS PUBLIC.ratings;
DROP TABLE IF EXISTS PUBLIC.comments;
DROP TABLE IF EXISTS PUBLIC.bookings;
DROP TABLE IF EXISTS PUBLIC.items;
DROP TABLE IF EXISTS PUBLIC.item_requests;
DROP TABLE IF EXISTS PUBLIC.users;

CREATE TABLE public.users (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	name varchar NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (email)
);

CREATE TABLE public.item_requests (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	description varchar NULL,
	requestor int4 NOT NULL,
	created timestamp NULL,
	CONSTRAINT item_requests_pk PRIMARY KEY (id),
	CONSTRAINT item_requests_users_fk FOREIGN KEY (requestor) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.items (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	name varchar NOT NULL,
	description varchar NULL,
	owner int4 NOT NULL,
	number_of_rentals int4 DEFAULT 0 NULL,
	available bool DEFAULT false NULL,
	request int4 NULL,
	CONSTRAINT items_pk PRIMARY KEY (id),
	CONSTRAINT items_item_requests_fk FOREIGN KEY (request) REFERENCES public.item_requests(id) ON DELETE NO ACTION,
	CONSTRAINT items_users_fk FOREIGN KEY (owner) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.bookings (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	start_time timestamp NOT NULL,
	end_time timestamp NOT NULL,
	item int4 NOT NULL,
	booker int4 NOT NULL,
	status varchar NOT NULL,
	CONSTRAINT bookings_pk PRIMARY KEY (id),
	CONSTRAINT bookings_items_fk FOREIGN KEY (item) REFERENCES public.items(id) ON DELETE CASCADE,
	CONSTRAINT bookings_users_fk FOREIGN KEY (booker) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.comments (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	author int4 NOT NULL,
	item int4 NOT NULL,
	text varchar NOT NULL,
	created timestamp NOT NULL,
	CONSTRAINT comments_pk PRIMARY KEY (id),
	CONSTRAINT comments_items_fk FOREIGN KEY (item) REFERENCES public.items(id) ON DELETE CASCADE,
	CONSTRAINT comments_users_fk FOREIGN KEY (author) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.ratings (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	author int4 NOT NULL,
	item int4 NOT NULL,
	rating int4 NOT NULL,
	CONSTRAINT ratings_pk PRIMARY KEY (id),
	CONSTRAINT ratings_items_fk FOREIGN KEY (item) REFERENCES public.items(id) ON DELETE CASCADE,
	CONSTRAINT ratings_users_fk FOREIGN KEY (author) REFERENCES public.users(id) ON DELETE CASCADE
);