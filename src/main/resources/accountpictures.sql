-- Table: public.accountpictures

-- DROP TABLE IF EXISTS public.accountpictures;

CREATE TABLE IF NOT EXISTS public.accountpictures
(
    user_id integer NOT NULL,
    picture_id integer NOT NULL,
    CONSTRAINT "pictureId" PRIMARY KEY (picture_id),
    CONSTRAINT "userId" FOREIGN KEY (user_id)
        REFERENCES public."TESTusers" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.accountpictures
    OWNER to "Bogos";