package com.omnicell.example.axon.OrderService.command.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRestModel {
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;
    private String orderId;
}