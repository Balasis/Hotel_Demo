package gr.balasis.hotel.context.base.domain;

import java.time.LocalDate;

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
    private LocalDate checkInDate;
    private LocalDate checkOutDate;


}
