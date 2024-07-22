package com.pedro.financing.processor;

import com.pedro.financing.model.FormaPagamentoEnum;
import com.pedro.financing.model.TipoTransacaoEnum;
import com.pedro.financing.processor.impl.ItiBankAccountProcessor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;


@SpringBootTest
public class ItiBankAccountProcessorTests {

    @Test
    void shouldProcessTransactionsCorrectly() {
        var processor = new ItiBankAccountProcessor();

        var dto = processor.processNotificacao("pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$ 123,59. Vai paarecer como itaucard");


        assert dto.getValor() == 12359;
        assert Objects.equals(dto.getInstituicao(), "CLARO");
        assert dto.getTipo() == TipoTransacaoEnum.DEBITO;
        assert dto.getFormaPagamento() == FormaPagamentoEnum.PIX;
    }

    @Test
    void notificationWithoutValueShouldThrowException() {
        var processor = new ItiBankAccountProcessor();
        Assert.assertThrows(IllegalArgumentException.class,
                () -> processor.processNotificacao("pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831). Vai paarecer como itaucard"));
    }

    @Test
    void notificationWithRSWithoutNumberShouldThrowException() {
        var processor = new ItiBankAccountProcessor();

        Assert.assertThrows(IllegalArgumentException.class,
                () -> processor.processNotificacao("pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$. Vai paarecer como itaucard"));
    }

    @Test
    void notificationWithoutInstitutionShouldProcessCorrectly() {
        var processor = new ItiBankAccountProcessor();

        var dto = processor.processNotificacao("pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para (34271831) No valor de R$ 123,59. Vai paarecer como itaucard");

        assert dto != null;
        assert dto.getInstituicao() == null;
    }

    @Test
    void notificationWithoutIdentifiedPatternShouldThrowException() {
        var processor = new ItiBankAccountProcessor();

        Assert.assertThrows(IllegalArgumentException.class,
                () -> processor.processNotificacao("Valor enviado para o usuario xpto"));
    }

}
