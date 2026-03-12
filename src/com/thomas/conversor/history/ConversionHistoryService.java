package com.thomas.conversor.history;

import com.thomas.conversor.service.ConversionResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversionHistoryService {

    private final List<ConversionRecord> records;

    public ConversionHistoryService() {
        this.records = new ArrayList<>();
    }

    public void register(ConversionResult conversionResult) {
        if (conversionResult == null) {
            throw new IllegalArgumentException("El resultado de conversion no puede ser nulo.");
        }

        ConversionRecord record = new ConversionRecord(
                conversionResult.getBaseCurrency(),
                conversionResult.getTargetCurrency(),
                conversionResult.getAmount(),
                conversionResult.getRate(),
                conversionResult.getConvertedAmount(),
                LocalDateTime.now()
        );

        records.add(record);
    }

    public List<ConversionRecord> findAll() {
        return Collections.unmodifiableList(records);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }
}