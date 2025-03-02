package gr.balasis.hotel.context.base.domain.domains;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;


@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseDomain{
    private Guest guest;
    private Room room;
    private LocalDateTime createdAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Payment payment;
}
