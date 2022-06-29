package com.omnicell.example.axon.ProductService.command.api.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRestModel {
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
