package gr.balasis.hotel.context.base.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseDomain{
    private String roomNumber;
    private boolean available;
    private BigDecimal pricePerNight;
}
