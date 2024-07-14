package com.pedro.financing.controller.v1.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@JsonSerialize
@JsonDeserialize
public class TransactionRequest implements Serializable {
    private AppEnum app;
    private String notificacao;
}
