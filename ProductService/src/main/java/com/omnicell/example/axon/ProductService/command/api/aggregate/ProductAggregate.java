package com.omnicell.example.axon.ProductService.command.api.aggregate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.omnicell.example.axon.CommonService.commands.CancelProductReservationCommand;
import com.omnicell.example.axon.CommonService.commands.ReserveProductCommand;
import com.omnicell.example.axon.CommonService.events.ProductReservationCancelledEvent;
import com.omnicell.example.axon.CommonService.events.ProductReservedEvent;
import com.omnicell.example.axon.ProductService.command.api.commands.CreateProductCommand;
import com.omnicell.example.axon.ProductService.command.api.events.ProductCreatedEvent;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        //You can perform all the validations
        ProductCreatedEvent productCreatedEvent =
                new ProductCreatedEvent();

        BeanUtils.copyProperties(createProductCommand,productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand)
    {
        if (quantity < reserveProductCommand.getQuantity())
        {
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand)
    {

        ProductReservationCancelledEvent productReservationCancelledEvent =
                ProductReservationCancelledEvent.builder().orderId(cancelProductReservationCommand.getOrderId()).productId(cancelProductReservationCommand.getProductId()).quantity(cancelProductReservationCommand.getQuantity()).userId(cancelProductReservationCommand.getUserId()).build();

        AggregateLifecycle.apply(productReservationCancelledEvent);
    }



    public ProductAggregate() {
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.quantity = productCreatedEvent.getQuantity();
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.name = productCreatedEvent.getName();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }


}
