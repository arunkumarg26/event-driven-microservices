package com.omnicell.example.axon.OrderService.query.api;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.omnicell.example.axon.CommonService.events.OrderCancelledEvent;
import com.omnicell.example.axon.CommonService.events.OrderCompletedEvent;
import com.omnicell.example.axon.OrderService.command.api.data.Order;
import com.omnicell.example.axon.OrderService.command.api.data.OrderRepository;
import com.omnicell.example.axon.OrderService.command.api.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@ProcessingGroup("order")
@Slf4j
public class OrderEventsHandler
{

    private OrderRepository orderRepository;

    public OrderEventsHandler(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event)
    {
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
        log.info("Order created "+event.getOrderId());
    }

    @EventHandler
    public void on(OrderCompletedEvent event)
    {
        Order order = orderRepository.findById(event.getOrderId()).get();

        order.setOrderStatus(event.getOrderStatus());

        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent event)
    {
        log.info("OrderCancelledEvent called for the orderId "+event.getOrderId());
        Order order = orderRepository.findById(event.getOrderId()).get();
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }
}
