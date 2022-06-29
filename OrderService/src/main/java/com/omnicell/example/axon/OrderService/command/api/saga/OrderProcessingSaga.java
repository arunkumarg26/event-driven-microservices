package com.omnicell.example.axon.OrderService.command.api.saga;

import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.omnicell.example.axon.CommonService.commands.CancelOrderCommand;
import com.omnicell.example.axon.CommonService.commands.CancelPaymentCommand;
import com.omnicell.example.axon.CommonService.commands.CancelProductReservationCommand;
import com.omnicell.example.axon.CommonService.commands.CompleteOrderCommand;
import com.omnicell.example.axon.CommonService.commands.ReserveProductCommand;
import com.omnicell.example.axon.CommonService.commands.ShipOrderCommand;
import com.omnicell.example.axon.CommonService.commands.ValidatePaymentCommand;
import com.omnicell.example.axon.CommonService.events.OrderCancelledEvent;
import com.omnicell.example.axon.CommonService.events.OrderCompletedEvent;
import com.omnicell.example.axon.CommonService.events.OrderShippedEvent;
import com.omnicell.example.axon.CommonService.events.PaymentCancelledEvent;
import com.omnicell.example.axon.CommonService.events.PaymentProcessedEvent;
import com.omnicell.example.axon.CommonService.events.ProductReservedEvent;
import com.omnicell.example.axon.CommonService.model.User;
import com.omnicell.example.axon.CommonService.queries.GetUserPaymentDetailsQuery;
import com.omnicell.example.axon.OrderService.command.api.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderProcessingSaga
{

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    public OrderProcessingSaga()
    {
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event)
    {
        //Check available product quantity
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(event.getOrderId()).productId(event.getProductId()).quantity(event.getQuantity()).userId(event.getUserId()).build();
        log.info("OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() + " and productId: " + reserveProductCommand.getProductId());
        try
        {
            commandGateway.sendAndWait(reserveProductCommand);
        }
        catch (CommandExecutionException exception)
        {
            log.error(exception.getMessage());
            cancelOrderCommand(event.getOrderId());
        }

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent)
    {
        // Fetch the user payment details
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(productReservedEvent.getUserId());
        User user = null;

        try
        {
            String a = null;
            a.toString();
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
            ValidatePaymentCommand validatePaymentCommand =
                    ValidatePaymentCommand.builder().cardDetails(user.getCardDetails()).orderId(productReservedEvent.getOrderId()).paymentId(UUID.randomUUID().toString()).build();
            commandGateway.sendAndWait(validatePaymentCommand);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            //Start the Compensating transaction
            cancelOrderCommand(productReservedEvent.getOrderId());
            cancelProductReservation(productReservedEvent);
        }

    }


    private void cancelOrderCommand(String orderId)
    {
        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent) {

        CancelProductReservationCommand cancelProductReservationCommand =
                CancelProductReservationCommand.builder()
                        .orderId(productReservedEvent.getOrderId())
                        .productId(productReservedEvent.getProductId())
                        .quantity(productReservedEvent.getQuantity())
                        .userId(productReservedEvent.getUserId())
                        .build();

        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event)
    {
        log.info("PaymentProcessedEvent in Saga for Order Id : {}", event.getOrderId());
        try
        {

            ShipOrderCommand shipOrderCommand =
                    ShipOrderCommand.builder().shipmentId(UUID.randomUUID().toString()).orderId(event.getOrderId()).build();
            commandGateway.send(shipOrderCommand);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            // Start the compensating transaction
            cancelPaymentCommand(event);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event)
    {
        CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(event.getPaymentId(), event.getOrderId());

        commandGateway.send(cancelPaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event)
    {

        log.info("OrderShippedEvent in Saga for Order Id : {}", event.getOrderId());

        CompleteOrderCommand completeOrderCommand =
                CompleteOrderCommand.builder().orderId(event.getOrderId()).orderStatus("COMPLETE").build();

        commandGateway.send(completeOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event)
    {
        log.info("OrderCompletedEvent in Saga for Order Id : {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event)
    {
        log.info("OrderCancelledEvent in Saga for Order Id : {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event)
    {
        log.info("PaymentCancelledEvent in Saga for Order Id : {}", event.getOrderId());
        cancelOrderCommand(event.getOrderId());
    }
}
