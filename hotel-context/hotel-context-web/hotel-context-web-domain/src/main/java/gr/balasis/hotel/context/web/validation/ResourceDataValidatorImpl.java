package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.base.enumeration.BedType;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.web.resource.*;
import gr.balasis.hotel.context.web.validation.ResourceDataValidator;
import gr.balasis.hotel.context.web.validation.exception.*;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.util.EnumUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ResourceDataValidatorImpl implements ResourceDataValidator {

    @Override
    public void validateResourceData(GuestResource guestResource) {
        if (guestResource == null) {
            throw new InvalidGuestResourceException("GuestResource cannot be null");
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
    public void validateResourceData(RoomResource roomResource) {
        if (roomResource == null) {
            throw new InvalidRoomResourceException("RoomResource cannot be null");
        }

        if (roomResource.getRoomNumber() == null || roomResource.getRoomNumber().trim().isEmpty()) {
            throw new InvalidRoomResourceException("Room number cannot be null or empty");
        }

        if (roomResource.getPricePerNight() == null || roomResource.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRoomResourceException("Price per night must be greater than zero");
        }

        if (roomResource.getBedType() == null){
            throw new InvalidRoomResourceException("Bed type can't be null");
        }

        boolean bedTypeIsValid = Stream.of(BedType.values())
                .anyMatch(bedType -> bedType.name().equals(roomResource.getBedType()));

        if (!bedTypeIsValid){
            throw new InvalidRoomResourceException("Invalid bed type: " + roomResource.getBedType());
        }

        if (roomResource.getFloor() == null || roomResource.getFloor() < 0) {
            throw new InvalidRoomResourceException("Floor must be a non-negative integer");
        }

    }

    @Override
    public void validateResourceData(ReservationResource reservationResource) {
        if (reservationResource == null) {
            throw new InvalidReservationResourceException("ReservationResource cannot be null");
        }

        if (reservationResource.getGuest() == null) {
            throw new InvalidReservationResourceException("Guest information is mandatory");
        }

        if (reservationResource.getRoom() == null || reservationResource.getRoom().getId() == null) {
            throw new InvalidReservationResourceException("Room information is mandatory");
        }

        if (reservationResource.getFeedback() != null){
            validateResourceData(reservationResource.getFeedback());
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
    public void validateResourceData(FeedbackResource feedbackResource) {
        if (feedbackResource == null) {
            throw new InvalidFeedbackResourceException("FeedbackResource cannot be null");
        }

        if (feedbackResource.getCreatedAt() != null && feedbackResource.getCreatedAt().isAfter( LocalDateTime.now())) {
            throw new InvalidFeedbackResourceException("Creation date (createdAt) cannot be in the future");
        }

        if (feedbackResource.getMessage() == null || feedbackResource.getMessage().trim().isEmpty()) {
            throw new InvalidFeedbackResourceException("Message cannot be null or empty");
        }

    }

    @Override
    public void validateResourceData(PaymentResource paymentResource) {
        if (paymentResource == null) {
            throw new InvalidPaymentResourceException("PaymentResource cannot be null");
        }
        if (paymentResource.getAmount() == null || paymentResource.getAmount().compareTo( BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentResourceException("Amount must be greater than zero");
        }
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
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
