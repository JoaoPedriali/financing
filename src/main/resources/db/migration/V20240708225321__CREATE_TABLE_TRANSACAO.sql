CREATE TABLE IF NOT EXISTS transacao(
id CHAR(36) NOT NULL,
valor INT not null,
tipo varchar(100) null,
forma_pagamento varchar(30) null,
instituicao varchar(100) null,
data timestamp(4) not null default CURRENT_TIMESTAMP(4),

primary key(id)
);
