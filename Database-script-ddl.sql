-- public.fee_list definition

-- Drop table

-- DROP TABLE fee_list;

CREATE TABLE fee_list (
	id int8 NOT NULL,
	document_type varchar(255) NULL,
	document_number varchar(255) NULL,
	state_registration varchar(255) NULL,
	origin varchar(255) NULL,
	"type" varchar(255) NULL,
	erp_code varchar(255) NULL,
	crop varchar(255) NULL,
	revenue_model varchar(255) NULL,
	"month" int4 NULL,
	operational_year int4 NULL,
	percentage varchar(255) NULL,
	value float8 NULL,
	creation_date varchar(255) NULL,
	update_date varchar(255) NULL,
	status varchar(255) NULL,
	vendor varchar(255) NULL,
	po_number varchar(255) NULL,
	CONSTRAINT fee_list_pkey PRIMARY KEY (id)
);


-- public."transaction" definition

-- Drop table

-- DROP TABLE "transaction";

CREATE TABLE "transaction" (
	id int8 NOT NULL,
	payment_forecast float8 NULL,
	our_number varchar(255) NULL,
	due_date date NULL,
	transaction_status varchar(255) NULL,
	observation varchar(255) NULL,
	creation_date date NULL,
	deletion_date date NULL,
	CONSTRAINT transaction_pkey PRIMARY KEY (id)
);


-- public.fee_list_transaction definition

-- Drop table

-- DROP TABLE fee_list_transaction;

CREATE TABLE fee_list_transaction (
	id int8 NOT NULL,
	creation_date date NULL,
	deletion_date date NULL,
	fee_list_id int8 NULL,
	transaction_id int8 NULL,
	CONSTRAINT fee_list_transaction_pkey PRIMARY KEY (id),
	CONSTRAINT fk_fee_list_transaction__fee_list_id FOREIGN KEY (fee_list_id) REFERENCES fee_list(id),
	CONSTRAINT fk_fee_list_transaction__transaction_id FOREIGN KEY (transaction_id) REFERENCES "transaction"(id)
);


-- public.invoice definition

-- Drop table

-- DROP TABLE invoice;

CREATE TABLE invoice (
	id int8 NOT NULL,
	s_3_url varchar(255) NULL,
	value float8 NULL,
	creation_date date NULL,
	invoice_status varchar(255) NULL,
	transaction_id int8 NULL,
	CONSTRAINT invoice_pkey PRIMARY KEY (id),
	CONSTRAINT fk_invoice__transaction_id FOREIGN KEY (transaction_id) REFERENCES "transaction"(id)
);


-- public.transaction_history definition

-- Drop table

-- DROP TABLE transaction_history;

CREATE TABLE transaction_history (
	id int8 NOT NULL,
	jhi_user varchar(255) NULL,
	description varchar(255) NULL,
	creation_date date NULL,
	transaction_id int8 NULL,
	CONSTRAINT transaction_history_pkey PRIMARY KEY (id),
	CONSTRAINT fk_transaction_history__transaction_id FOREIGN KEY (transaction_id) REFERENCES "transaction"(id)
);
