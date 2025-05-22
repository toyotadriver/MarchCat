CREATE TABLE public.pictures
(
    id integer NOT NULL DEFAULT 0,
    orig_name character varying(30) NOT NULL DEFAULT 'noname',
    rnd_name character varying(20) NOT NULL DEFAULT 'error',
    ext character varying(5) NOT NULL DEFAULT 'error',
    dou timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT id PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.pictures
    OWNER to "Bogos";
    
ALTER TABLE IF EXISTS public.pictures
    ADD COLUMN storage integer;