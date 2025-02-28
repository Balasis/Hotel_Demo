package gr.balasis.hotel.context.web.resource;

import java.time.LocalDate;
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
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
