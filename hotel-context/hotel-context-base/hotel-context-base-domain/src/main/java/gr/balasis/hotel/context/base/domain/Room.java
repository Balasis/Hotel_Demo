package gr.balasis.hotel.context.base.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseDomain{
    private String roomNumber;
    private boolean reserved;
    private BigDecimal pricePerNight;
}
