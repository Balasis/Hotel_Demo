package gr.balasis.hotel.context.web.resource;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReservationResource extends BaseResource {
    private GuestResource guest;
    private RoomResource room;
    private String status;
    private FeedbackResource feedbackResource;
    private PaymentResource payment;
    private LocalDateTime createdAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
