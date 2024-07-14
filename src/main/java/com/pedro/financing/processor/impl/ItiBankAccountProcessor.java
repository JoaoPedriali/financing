package com.pedro.financing.processor.impl;

import com.pedro.financing.dto.TransacaoDTO;
import com.pedro.financing.model.FormaPagamentoEnum;
import com.pedro.financing.model.TipoTransacaoEnum;
import com.pedro.financing.processor.TransactionProcessor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@Setter
public class ItiBankAccountProcessor implements TransactionProcessor {

    @Override
    public TransacaoDTO processNotificacao(String notificacao) {
        var pixDebitPattern = Pattern.compile("pagamento Pix feito com sucesso!").matcher(notificacao);

        if (pixDebitPattern.find()) {
            return processPixDebit(notificacao);
        }

        return null;
    }

    private TransacaoDTO processPixDebit(String notificacao) {

        var transacaoDTO = new TransacaoDTO();

        transacaoDTO.setTipo(TipoTransacaoEnum.DEBITO);
        transacaoDTO.setFormaPagamento(FormaPagamentoEnum.PIX);
        transacaoDTO.setData(LocalDateTime.now());


        // Get values after `R$`
        var valueMatcher = Pattern.compile("R\\$\\d*,\\d{2}").matcher(notificacao);

        var instituicaoPattern = Pattern.compile("iti para \\w.*\\(").matcher(notificacao);

        if(valueMatcher.find()) {
            // Clears out R$ and , from string e.g: R$123,45 -> 12345
            var valor = Integer.getInteger(valueMatcher.group(1)
                    .replace("R$", "")
                    .replace(",",""));
            transacaoDTO.setValor(valor);
        }

        if(instituicaoPattern.find()) {
            // Removes matcher string and '(' at the end of the string
            var instituicao = instituicaoPattern.group(1).replace("iti para", "($");
            transacaoDTO.setInstituicao(instituicao);
        }

        return transacaoDTO;

    }
}
