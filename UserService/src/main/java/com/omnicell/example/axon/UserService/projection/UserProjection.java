package com.omnicell.example.axon.UserService.projection;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.omnicell.example.axon.CommonService.model.CardDetails;
import com.omnicell.example.axon.CommonService.model.User;
import com.omnicell.example.axon.CommonService.queries.GetUserPaymentDetailsQuery;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
        //Ideally Get the details from the DB
        CardDetails cardDetails
                = CardDetails.builder()
                .name("Tom Cruise")
                .validUntilYear(2022)
                .validUntilMonth(01)
                .cardNumber("123456789")
                .cvv(111)
                .build();

        return User.builder()
                .userId(query.getUserId())
                .firstName("Tom")
                .lastName("Cruise")
                .cardDetails(cardDetails)
                .build();
    }
}
