package com.pedro.financing.service;

import com.pedro.financing.controller.v1.request.TransactionRequest;
import com.pedro.financing.dto.TransacaoDTO;
import com.pedro.financing.model.Transacao;
import com.pedro.financing.processor.TransactionProcessorFactory;
import com.pedro.financing.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;
    private final TransactionProcessorFactory factory = new TransactionProcessorFactory();

    ModelMapper modelMapper = new ModelMapper();

    @JmsListener(destination = "${spring.artemis.embedded.queues}", containerFactory = "jmsFactory")
    @Transactional
    public void processTransacao(TransactionRequest message) {

        log.info("Message: {}", message.toString());
        var processor = factory.createTransactionProcessor(message.getApp());

        log.info("Processor: {}", processor);
        var dto = processor.processNotificacao(message.getNotificacao());

        var transacao = modelMapper.map(dto, Transacao.class);

        log.info("DTO: {}", dto.toString());
        transacaoRepository.save(transacao);

    }

    public Page<TransacaoDTO> getTransactions(Integer page, int size) {

        if (page == null || page < 0)
            page = 0;

        if (size <= 0)
            size = 10;

        if (size > 1000)
            throw new IllegalArgumentException("Size must be less than 1000");

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        var transacaoPage = transacaoRepository.findAll(pageable);

        var dtos = transacaoPage.getContent().stream().map(transacao -> modelMapper.map(transacao, TransacaoDTO.class));

        return new PageImpl<>(dtos.toList(), pageable, transacaoPage.getTotalElements());

    }
}
