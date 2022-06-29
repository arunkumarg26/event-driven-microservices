package com.omnicell.example.axon.ProductService.query.api;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.omnicell.example.axon.CommonService.events.ProductReservationCancelledEvent;
import com.omnicell.example.axon.CommonService.events.ProductReservedEvent;
import com.omnicell.example.axon.ProductService.command.api.data.Product;
import com.omnicell.example.axon.ProductService.command.api.data.ProductRepository;
import com.omnicell.example.axon.ProductService.command.api.events.ProductCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@ProcessingGroup("product")
@Slf4j
public class ProductEventsHandler
{

    private ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception
    {
        Product product = new Product();
        BeanUtils.copyProperties(event, product);
        productRepository.save(product);
        //throw new Exception("Exception Occurred");
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception
    {
        throw exception;
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent)
    {
        Product product = productRepository.findByProductId(productReservedEvent.getProductId());
        log.debug("ProductReservedEvent: for productId "+productReservedEvent.getProductId()+" Current product quantity " + product.getQuantity());
        product.setQuantity(product.getQuantity() - productReservedEvent.getQuantity());
        productRepository.save(product);
        log.debug("ProductReservedEvent: for productId "+productReservedEvent.getProductId()+" New product quantity " + product.getQuantity());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent)
    {
        log.info("ProductReservationCancelledEvent is called for productId:" + productReservationCancelledEvent.getProductId());
        Product product = productRepository.findByProductId(productReservationCancelledEvent.getProductId());
        int newQuantity = product.getQuantity() + productReservationCancelledEvent.getQuantity();
        product.setQuantity(newQuantity);
        productRepository.save(product);
        log.info("ProductReservationCancelledEvent for productId:" + productReservationCancelledEvent.getProductId() + " is successful , "+
                "new quantity is "+product.getQuantity());
    }
}
