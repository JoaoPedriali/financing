package com.pedro.financing.processor;


import com.pedro.financing.dto.TransacaoDTO;

public interface TransactionProcessor {

    TransacaoDTO processNotificacao(String notificacao);
}
