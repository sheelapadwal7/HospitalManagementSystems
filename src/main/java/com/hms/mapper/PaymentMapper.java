package com.hms.mapper;

import com.hms.dto.PaymentDTO;
import com.hms.model.Payment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    private final ModelMapper modelMapper;

    public PaymentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PaymentDTO toDto(Payment payment) {
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public Payment toEntity(PaymentDTO paymentDTO) {
        return modelMapper.map(paymentDTO, Payment.class);
    }
}
