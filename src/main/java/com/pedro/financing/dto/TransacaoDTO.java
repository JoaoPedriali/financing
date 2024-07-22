package com.pedro.financing.dto;

import com.pedro.financing.model.FormaPagamentoEnum;
import com.pedro.financing.model.TipoTransacaoEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class TransacaoDTO {
    private UUID id;

    private int valor;

    private TipoTransacaoEnum tipo;

    private FormaPagamentoEnum formaPagamento;

    private String instituicao;

    private Instant createdAt;

    private Instant updatedAt;
}
