ALTER TABLE transacao
    rename column data to created_at,
    ADD COLUMN updated_at timestamp(4) null on update CURRENT_TIMESTAMP(4)