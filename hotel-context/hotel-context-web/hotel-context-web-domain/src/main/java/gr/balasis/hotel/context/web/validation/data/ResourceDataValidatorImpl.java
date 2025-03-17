package gr.balasis.hotel.context.web.validation.data;

import gr.balasis.hotel.context.base.enumeration.BedType;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.web.exception.*;
import gr.balasis.hotel.context.web.resource.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class ResourceDataValidatorImpl implements ResourceDataValidator {

    @Override
    public void validateResourceData(BaseResource toBeValidatedResource, boolean toCreate) {
        if (toBeValidatedResource == null) {
            throw new InvalidResourceException("Resource cannot be null");
        }

        String className = toBeValidatedResource.getClass().getSimpleName();

        switch (className) {
            case "GuestResource":
                validateResourceData((GuestResource) toBeValidatedResource, toCreate);
                break;
            case "RoomResource":
                validateResourceData((RoomResource) toBeValidatedResource, toCreate);
                break;
            case "ReservationResource":
                validateResourceData((ReservationResource) toBeValidatedResource, toCreate);
                break;
            case "FeedbackResource":
                validateResourceData((FeedbackResource) toBeValidatedResource, toCreate);
                break;
            case "PaymentResource":
                validateResourceData((PaymentResource) toBeValidatedResource, toCreate);
                break;
            default:
                throw new InvalidResourceException("Unknown resource type: " + className);
        }
    }

    @Override
    public void validateResourceData(GuestResource guestResource, boolean toCreate) {

        if (guestResource == null) {
            throw new InvalidGuestResourceException("GuestResource cannot be null");
        }

        if (toCreate && guestResource.getId() != null) {
            throw new InvalidGuestResourceException("Guest ID must not be provided when creating a new guest.");
        }

        if (guestResource.getFirstName() == null || guestResource.getFirstName().trim().isEmpty()) {
            throw new InvalidGuestResourceException("First name cannot be null or empty");
        }

        if (guestResource.getLastName() == null || guestResource.getLastName().trim().isEmpty()) {
            throw new InvalidGuestResourceException("Last name cannot be null or empty");
        }

        if (guestResource.getEmail() == null || guestResource.getEmail().trim().isEmpty()) {
            throw new InvalidGuestResourceException("Email cannot be null or empty");
        }

        if (!isValidEmail(guestResource.getEmail())) {
            throw new InvalidGuestResourceException("Invalid email format");
        }

        if (guestResource.getBirthDate() == null) {
            throw new InvalidGuestResourceException("Birth date cannot be null");
        }

    }

    @Override
    public void validateResourceData(RoomResource roomResource, boolean toCreate) {
        if (roomResource == null) {
            throw new InvalidRoomResourceException("RoomResource cannot be null");
        }

        if (toCreate && roomResource.getId() != null) {
            throw new InvalidRoomResourceException("Room ID must not be provided when creating a new room.");
        }

        if (roomResource.getRoomNumber() == null || roomResource.getRoomNumber().trim().isEmpty()) {
            throw new InvalidRoomResourceException("Room number cannot be null or empty");
        }

        if (roomResource.getPricePerNight() == null || roomResource.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRoomResourceException("Price per night must be greater than zero");
        }

        if (roomResource.getBedType() == null) {
            throw new InvalidRoomResourceException("Bed type can't be null");
        }

        if (!isValidEnum(BedType.class, roomResource.getBedType())) {
            throw new InvalidRoomResourceException("Invalid bed type: " + roomResource.getBedType());
        }

        if (roomResource.getFloor() == null || roomResource.getFloor() < 0) {
            throw new InvalidRoomResourceException("Floor must be a non-negative integer");
        }

    }

    @Override
    public void validateResourceData(ReservationResource reservationResource, boolean toCreate) {

        if (reservationResource == null) {
            throw new InvalidReservationResourceException("ReservationResource cannot be null");
        }

        if (toCreate && reservationResource.getId() != null) {
            throw new InvalidReservationResourceException("Reservation ID must not be provided when creating a new reservation.");
        }

        if (reservationResource.getGuest() == null) {
            throw new InvalidReservationResourceException("Guest information is mandatory");
        }

        if (reservationResource.getRoom() == null || reservationResource.getRoom().getId() == null) {
            throw new InvalidReservationResourceException("Room information is mandatory");
        }

        if (reservationResource.getFeedback() != null) {
            validateResourceData(reservationResource.getFeedback(), toCreate);
        }

        if (reservationResource.getStatus() != null && !isValidReservationStatus(reservationResource.getStatus())) {
            throw new InvalidReservationResourceException("Invalid reservation status");
        }

        if (reservationResource.getCheckInDate() == null) {
            throw new InvalidReservationResourceException("Check-in date is mandatory");
        }

        if (reservationResource.getCheckOutDate() == null) {
            throw new InvalidReservationResourceException("Check-out date is mandatory");
        }

        if (reservationResource.getCheckOutDate().isBefore(reservationResource.getCheckInDate())) {
            throw new InvalidReservationResourceException("Check-out date cannot be earlier than check-in date");
        }
    }

    @Override
    public void validateResourceData(FeedbackResource feedbackResource, boolean toCreate) {
        if (feedbackResource == null) {
            throw new InvalidFeedbackResourceException("FeedbackResource cannot be null");
        }

        if (toCreate && feedbackResource.getId() != null) {
            throw new InvalidFeedbackResourceException("Feedback ID must not be provided when creating a new feedback.");
        }

        if (feedbackResource.getCreatedAt() != null && feedbackResource.getCreatedAt().isAfter(LocalDateTime.now())) {
            throw new InvalidFeedbackResourceException("Creation date (createdAt) cannot be in the future");
        }

        if (feedbackResource.getMessage() == null || feedbackResource.getMessage().trim().isEmpty()) {
            throw new InvalidFeedbackResourceException("Message cannot be null or empty");
        }

    }

    @Override
    public void validateResourceData(PaymentResource paymentResource, boolean toCreate) {
        if (paymentResource == null) {
            throw new InvalidPaymentResourceException("PaymentResource cannot be null");
        }

        if (toCreate && paymentResource.getId() != null) {
            throw new InvalidPaymentResourceException("PaymentResource ID must not be provided when creating" +
                    " a new PaymentResource.");
        }

        if (paymentResource.getAmount() == null || paymentResource.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentResourceException("Amount must be greater than zero");
        }
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidEnum(Class<? extends Enum<?>> enumClass, String value) {
        return Stream.of(enumClass.getEnumConstants()).anyMatch(e -> e.name().equals(value));
    }

    private boolean isValidReservationStatus(String status) {
        try {
            ReservationStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
