package com.omnicell.example.axon.ProductService.query.api.projection;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.omnicell.example.axon.ProductService.command.api.data.Product;
import com.omnicell.example.axon.ProductService.command.api.data.ProductRepository;
import com.omnicell.example.axon.ProductService.command.api.model.ProductRestModel;
import com.omnicell.example.axon.ProductService.query.api.queries.GetProductsQuery;

@Component
public class ProductProjection {

    private ProductRepository productRepository;

    public ProductProjection(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> handle(GetProductsQuery getProductsQuery) {
        List<Product> products =
                productRepository.findAll();

        List<ProductRestModel> productRestModels =
                products.stream()
                        .map(product -> ProductRestModel
                                .builder()
                                .quantity(product.getQuantity())
                                .price(product.getPrice())
                                .name(product.getName())
                                .productId(product.getProductId())
                                .build())
                        .collect(Collectors.toList());

        return productRestModels;
    }
}
