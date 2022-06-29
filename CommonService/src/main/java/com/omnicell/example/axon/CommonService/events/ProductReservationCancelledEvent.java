package com.omnicell.example.axon.CommonService.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservationCancelledEvent
{

    private final String productId;
    private final int quantity;
    private final String orderId;
    private final String userId;
}
