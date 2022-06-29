package com.omnicell.example.axon.ProductService.command.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omnicell.example.axon.ProductService.command.api.commands.CreateProductCommand;
import com.omnicell.example.axon.ProductService.command.api.model.ProductRestModel;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    private CommandGateway commandGateway;

    public ProductCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String addProduct(@RequestBody ProductRestModel productRestModel)
    {
        CreateProductCommand createProductCommand =
                CreateProductCommand.builder().productId(UUID.randomUUID().toString()).name(productRestModel.getName()).price(productRestModel.getPrice()).quantity(productRestModel.getQuantity()).build();
        String result = commandGateway.sendAndWait(createProductCommand);
        return "Product created with id " + result;
    }
}
