package gr.balasis.hotel.context.web.resource;


import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.web.validation.custom.DateComparison;
import gr.balasis.hotel.context.web.validation.custom.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@DateComparison(
        firstField = "checkInDate",
        secondField = "checkOutDate",
        condition = "beforeOrEqual",
        message = "Check-in date must be before or equal to check-out date"
)
public class ReservationResource extends BaseResource {
    @NotNull(message ="Guest is mandatory")
    private GuestResource guest;

    @NotNull(message ="Room is mandatory")
    private RoomResource room;

    @ValidEnum(enumClass = ReservationStatus.class, message = "Invalid reservation status")
    private String status;

    private FeedbackResource feedback;
    private PaymentResource payment;
    private LocalDateTime createdAt;

    @NotNull(message ="CheckIn Date is mandatory")
    private LocalDate checkInDate;

    @NotNull(message ="CheckOut Date is mandatory")
    private LocalDate checkOutDate;
}
