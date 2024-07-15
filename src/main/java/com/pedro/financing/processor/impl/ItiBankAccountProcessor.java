package com.pedro.financing.processor.impl;

import com.pedro.financing.dto.TransacaoDTO;
import com.pedro.financing.model.FormaPagamentoEnum;
import com.pedro.financing.model.TipoTransacaoEnum;
import com.pedro.financing.processor.TransactionProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@Setter
@Slf4j
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


        // Get values after `R$`
        var valueMatcher = Pattern.compile("R\\$ \\d*,\\d{2}").matcher(notificacao);

        var instituicaoPattern = Pattern.compile("iti para \\w.*\\(").matcher(notificacao);

        if(!valueMatcher.find()) {
            throw new RuntimeException("Value of notification nor found");
        }

        // Clears out R$ and , from string e.g: R$123,45 -> 12345
        var valorString = valueMatcher.group(0)
                .replace("R$ ", "")
                .replace(",","");

        log.info("Value Before conversion: {}", valorString);

        var valor = Integer.parseInt(valorString) ;

        log.info("Value after Conversion: {}", valor);
        transacaoDTO.setValor(valor);

        if(instituicaoPattern.find()) {
            // Removes matcher string and '(' at the end of the string
            var instituicao = instituicaoPattern.group(0).replace("iti para", "").replace(" (", "");
            log.info("Instituicao: {}", instituicao);
            transacaoDTO.setInstituicao(instituicao);
        }


        return transacaoDTO;

    }
}
