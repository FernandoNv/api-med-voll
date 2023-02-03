CREATE TABLE usuarios(
    id BIGSERIAL not null,
    login varchar(100) not null,
    senha varchar(255) not null,
    CONSTRAINT usuarios_id_pkey PRIMARY KEY(id)
);