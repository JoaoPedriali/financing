package com.pedro.financing.service;

import com.pedro.financing.controller.v1.request.TransactionRequest;
import com.pedro.financing.model.Transacao;
import com.pedro.financing.processor.TransactionProcessorFactory;
import com.pedro.financing.repository.TransacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;
    private final TransactionProcessorFactory factory = new TransactionProcessorFactory();

    ModelMapper modelMapper = new ModelMapper();

    @JmsListener(destination = "${spring.artemis.embedded.queues}", containerFactory = "jmsFactory")
    public void processTransacao(TransactionRequest message) {

        log.info("Message: {}", message.toString());
        var processor = factory.createTransactionProcessor(message.getApp());

        log.info("Processor: {}", processor);
        var dto = processor.processNotificacao(message.getNotificacao());

        var transacao = modelMapper.map(dto, Transacao.class);

        log.info("DTO: {}", dto.toString());
        transacaoRepository.save(transacao);

    }
}
