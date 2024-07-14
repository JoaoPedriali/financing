package com.pedro.financing.processor;

import com.pedro.financing.controller.v1.request.AppEnum;
import com.pedro.financing.processor.impl.ItiBankAccountProcessor;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessorFactory {
    public TransactionProcessor createTransactionProcessor(AppEnum type) {

        TransactionProcessor processor = null;

        switch (type) {
            case AppEnum.ITI -> processor = new ItiBankAccountProcessor();
            case null -> throw new IllegalArgumentException("Type cannot be null");
            default -> throw new IllegalArgumentException("Type not supported");
        }

        return processor;
    }
}
