CREATE TABLE pessoas(
    usuario_id BIGSERIAL NOT NULL,
    nome varchar(100) NOT NULL,
    imagem_url varchar(255),

    CONSTRAINT pessoas_id_pkey PRIMARY KEY(usuario_id),
    CONSTRAINT fk_pessoas_usuarios_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);