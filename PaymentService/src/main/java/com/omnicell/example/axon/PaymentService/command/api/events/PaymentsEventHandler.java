package com.omnicell.example.axon.PaymentService.command.api.events;

import java.util.Date;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.omnicell.example.axon.CommonService.events.PaymentCancelledEvent;
import com.omnicell.example.axon.CommonService.events.PaymentProcessedEvent;
import com.omnicell.example.axon.PaymentService.command.api.data.Payment;
import com.omnicell.example.axon.PaymentService.command.api.data.PaymentRepository;

@Component
@ProcessingGroup("payment")
public class PaymentsEventHandler {

    private PaymentRepository paymentRepository;

    public PaymentsEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment
                = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timeStamp(new Date())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment
                = paymentRepository.findById(event.getPaymentId()).get();

        payment.setPaymentStatus(event.getPaymentStatus());

        paymentRepository.save(payment);
    }
}
