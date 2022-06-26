package com.dailycodebuffer.OrderService.query.api.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodebuffer.OrderService.command.api.model.OrderRestModel;
import com.dailycodebuffer.OrderService.query.api.query.GetOrdersQuery;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderQueryController
{
    private QueryGateway queryGateway;

    @GetMapping
    public List<OrderRestModel> getAllOrders (){
        GetOrdersQuery getOrdersQuery = new GetOrdersQuery();
        return queryGateway.query(getOrdersQuery, ResponseTypes.multipleInstancesOf(OrderRestModel.class))
                .join();
    }

}
