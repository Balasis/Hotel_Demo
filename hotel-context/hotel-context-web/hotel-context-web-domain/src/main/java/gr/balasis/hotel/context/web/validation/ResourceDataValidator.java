package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.web.resource.*;

public interface ResourceDataValidator {

    void validateResourceData(FeedbackResource feedbackResource);

    void validateResourceData(PaymentResource paymentResource);

    void validateResourceData(GuestResource guestResource);

    void validateResourceData(ReservationResource reservationResource);

    void validateResourceData(RoomResource roomResource);
}
