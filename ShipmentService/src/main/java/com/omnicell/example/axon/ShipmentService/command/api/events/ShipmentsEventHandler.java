package com.omnicell.example.axon.ShipmentService.command.api.events;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.omnicell.example.axon.CommonService.events.OrderShippedEvent;
import com.omnicell.example.axon.ShipmentService.command.api.data.Shipment;
import com.omnicell.example.axon.ShipmentService.command.api.data.ShipmentRepository;

@Component
@ProcessingGroup("shipment")
public class ShipmentsEventHandler {

    private ShipmentRepository shipmentRepository;

    public ShipmentsEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment
                = new Shipment();
        BeanUtils.copyProperties(event,shipment);
        shipmentRepository.save(shipment);
    }
}
