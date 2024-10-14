CREATE TABLE public.products
(
    id bigserial NOT NULL,
    name character varying(50) NOT NULL,
    price integer NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    modified_at timestamp with time zone NOT NULL DEFAULT now(),
    is_deleted boolean NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);