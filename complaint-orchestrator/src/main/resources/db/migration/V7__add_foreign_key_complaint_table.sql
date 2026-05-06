ALTER TABLE complaint
    ALTER COLUMN code TYPE INT,
    ADD FOREIGN KEY (code) REFERENCES process_code_type(code);