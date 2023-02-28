CREATE TABLE consultas(
    id BIGSERIAL NOT NULL,
    medico_id BIGSERIAL NOT NULL,
    paciente_id BIGSERIAL NOT NULL,
    data DATE NOT NULL,

    CONSTRAINT consultas_id_pkey PRIMARY KEY(id),
    CONSTRAINT fk_consultas_medico_id FOREIGN KEY(medico_id) REFERENCES medicos(id),
    CONSTRAINT fk_consultas_paciente_id FOREIGN KEY(paciente_id) REFERENCES pacientes(id)
);