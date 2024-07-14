package com.pedro.financing.dto;

import com.pedro.financing.model.FormaPagamentoEnum;
import com.pedro.financing.model.TipoTransacaoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransacaoDTO {
    private UUID id;

    private int valor;

    private TipoTransacaoEnum tipo;

    private FormaPagamentoEnum formaPagamento;

    private String instituicao;

    private LocalDateTime data;
}
