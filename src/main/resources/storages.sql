CREATE TABLE public.storages
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    storage_name character varying(20),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.storages
    OWNER to "Bogos";