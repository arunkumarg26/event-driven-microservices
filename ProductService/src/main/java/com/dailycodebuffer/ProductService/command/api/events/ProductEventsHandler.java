package com.dailycodebuffer.ProductService.command.api.events;

import com.dailycodebuffer.CommonService.events.ProductReservedEvent;
import com.dailycodebuffer.ProductService.command.api.data.Product;
import com.dailycodebuffer.ProductService.command.api.data.ProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@ProcessingGroup("product")
@Slf4j
public class ProductEventsHandler {

    private ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        Product product =
                new Product();
        BeanUtils.copyProperties(event,product);
        productRepository.save(product);
        //throw new Exception("Exception Occurred");
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent)
    {
        Product product = productRepository.findByProductId(productReservedEvent.getProductId());
        log.debug("ProductReservedEvent: Current product quantity " + product.getQuantity());

        product.setQuantity(product.getQuantity() - productReservedEvent.getQuantity());
        productRepository.save(product);

        log.debug("ProductReservedEvent: New product quantity " + product.getQuantity());
        log.info("ProductReservedEvent is called for productId:" + productReservedEvent.getProductId() + " and orderId: " + productReservedEvent.getOrderId());
    }
}
