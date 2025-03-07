package gr.balasis.hotel.context.web.resource;

import gr.balasis.hotel.context.base.model.Payment;
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
    private Payment payment;
    private LocalDateTime createdAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
