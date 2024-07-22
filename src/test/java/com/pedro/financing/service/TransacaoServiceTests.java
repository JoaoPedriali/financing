package com.pedro.financing.service;

import com.pedro.financing.controller.v1.request.AppEnum;
import com.pedro.financing.controller.v1.request.TransactionRequest;
import com.pedro.financing.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class TransacaoServiceTests {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @ServiceConnection
    @Container
    static MariaDBContainer<?> container = new MariaDBContainer<>("mariadb:10.11");

    @AfterEach
    void cleanup() {
        transacaoRepository.deleteAll();
    }

    @Test
    void transactionShouldProcessCorrectly() {

        transacaoService.processTransacao(new TransactionRequest(AppEnum.ITI, "pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$ 123,59. Vai paarecer como itaucard"));

        var transactions = transacaoService.getTransactions(0,10);

        assert transactions.getTotalElements() == 1;

    }

    @Test
    void getTransactionWithPageLessThanZeroShouldReturnFirstPage() {
        transacaoService.processTransacao(new TransactionRequest(AppEnum.ITI, "pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$ 123,59. Vai paarecer como itaucard"));

        var transactions = transacaoService.getTransactions(-1,10);

        assert transactions.getPageable().getPageNumber() == 0;
    }

    @Test
    void getTransactionWithPageNullShouldReturnFirstPage() {
        transacaoService.processTransacao(new TransactionRequest(AppEnum.ITI, "pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$ 123,59. Vai paarecer como itaucard"));

        var transactions = transacaoService.getTransactions(null,10);

        assert transactions.getPageable().getPageNumber() == 0;
    }

    @Test
    void getTransactionWithSizeLessThanZeroShouldReturnTenElements() {

        for (int i = 0; i < 12; i++) {
            transacaoService.processTransacao(new TransactionRequest(AppEnum.ITI, "pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$ 123"+ i +",99. Vai paarecer como itaucard"));
        }



        var transactions = transacaoService.getTransactions(0,-1);
        System.out.println(transactions.getContent());
        assert transactions.getPageable().getPageSize() == 10;
        assert transactions.getContent().getLast().getValor() == 123999;
    }

    @Test
    void getTransactionWithSizeBiggerThan1000ShouldThrowException() {

        for (int i = 0; i < 12; i++) {
            transacaoService.processTransacao(new TransactionRequest(AppEnum.ITI, "pagamento Pix feito com sucesso! Você fez um pagamento do seu iti para CLARO (34271831) No valor de R$ 123"+ i +",99. Vai paarecer como itaucard"));
        }


        Assert.assertThrows(IllegalArgumentException.class, () -> transacaoService.getTransactions(0,1001));

    }
}

