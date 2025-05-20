CREATE TABLE public.links
(
    id integer,
    link character varying(20),
    PRIMARY KEY (link),
    CONSTRAINT id FOREIGN KEY (id)
        REFERENCES public.pictures (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.links
    OWNER to "Bogos";