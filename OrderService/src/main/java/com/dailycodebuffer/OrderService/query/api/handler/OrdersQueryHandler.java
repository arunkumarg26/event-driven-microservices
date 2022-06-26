package com.dailycodebuffer.OrderService.query.api.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.dailycodebuffer.OrderService.command.api.data.Order;
import com.dailycodebuffer.OrderService.command.api.data.OrderRepository;
import com.dailycodebuffer.OrderService.command.api.model.OrderRestModel;
import com.dailycodebuffer.OrderService.query.api.query.GetOrdersQuery;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrdersQueryHandler
{
    private OrderRepository orderRepository;

    @QueryHandler
    public List<OrderRestModel> handle(GetOrdersQuery getOrdersQuery)
    {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> OrderRestModel.builder().quantity(order.getQuantity()).productId(order.getProductId()).addressId(order.getAddressId()).orderStatus(order.getOrderStatus()).build()).collect(Collectors.toList());

    }

}
