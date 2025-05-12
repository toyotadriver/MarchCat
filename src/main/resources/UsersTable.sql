CREATE TABLE public."TESTusers"
(
    id integer NOT NULL DEFAULT 0,
    username character varying(20) NOT NULL DEFAULT unnamed,
    password character varying(30) NOT NULL DEFAULT notspecified,
    dor date NOT NULL DEFAULT unknown,
    CONSTRAINT id PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public."TESTusers"
    OWNER to "Bogos";