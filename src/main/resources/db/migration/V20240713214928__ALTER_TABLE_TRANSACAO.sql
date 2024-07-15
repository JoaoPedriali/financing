ALTER TABLE transacao
    rename column data to created_at,
    ADD COLUMN updated_at timestamp null