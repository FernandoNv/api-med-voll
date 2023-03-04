ALTER TABLE pessoas DROP COLUMN genero;
ALTER TABLE pessoas DROP COLUMN data_nascimento;

ALTER TABLE pessoas ADD COLUMN genero VARCHAR(100);
ALTER TABLE pessoas ADD COLUMN data_nascimento DATE;