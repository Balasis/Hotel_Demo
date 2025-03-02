package gr.balasis.hotel.context.web.resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ReservationResource extends BaseResource{
    private GuestResource guest;
    private RoomResource room;
    private LocalDateTime createdAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private PaymentResource payment;
}
