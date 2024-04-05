CREATE TABLE squad
(
    id character varying(36) NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(256) NOT NULL,
    company_id character varying(36) NOT NULL,
    manager_id character varying(36) NOT NULL,
    CONSTRAINT squad_pkey PRIMARY KEY (id)
);
