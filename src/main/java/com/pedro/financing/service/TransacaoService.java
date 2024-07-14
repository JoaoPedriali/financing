package com.pedro.financing.service;

import com.pedro.financing.controller.v1.request.TransactionRequest;
import com.pedro.financing.dto.TransacaoDTO;
import com.pedro.financing.model.Transacao;
import com.pedro.financing.repository.TransacaoRepository;
import com.pedro.financing.processor.TransactionProcessorFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class TransacaoService {


    private TransacaoRepository transacaoRepository;
    private TransactionProcessorFactory factory;

    Logger log = LoggerFactory.getLogger(TransacaoService.class);
    ModelMapper modelMapper = new ModelMapper();

    @JmsListener(destination = "${spring.artemis.embedded.queues}", containerFactory = "jmsFactory")
    public TransacaoDTO processTransacao(TransactionRequest message) {

        var processor = factory.createTransactionProcessor(message.getApp());

        var dto = processor.processNotificacao(message.getNotificacao());

        var transacao = modelMapper.map(dto, Transacao.class);

        return modelMapper.map(transacaoRepository.save(transacao), TransacaoDTO.class);

    }
}
