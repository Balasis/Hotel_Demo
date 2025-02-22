package gr.balasis.hotel.context.base.domain;

import java.time.LocalDate;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
public class Reservation extends BaseDomain{
    private Long guestId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;


}
