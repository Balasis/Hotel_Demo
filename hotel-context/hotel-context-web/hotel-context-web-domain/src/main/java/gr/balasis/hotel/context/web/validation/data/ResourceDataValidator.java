package gr.balasis.hotel.context.web.validation.data;

import gr.balasis.hotel.context.web.resource.*;

public interface ResourceDataValidator {

    void validateResourceData(BaseResource toBeValidatedResource, boolean toCreate);

    void validateResourceData(GuestResource guestResource, boolean toCreate);

    void validateResourceData(RoomResource roomResource, boolean toCreate);

    void validateResourceData(ReservationResource reservationResource, boolean toCreate);

    void validateResourceData(FeedbackResource feedbackResource, boolean toCreate);

    void validateResourceData(PaymentResource paymentResource, boolean toCreate);

}
