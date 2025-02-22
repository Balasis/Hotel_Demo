package gr.balasis.hotel.context.base.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
public class Room extends BaseDomain{
    private String roomNumber;
    private boolean available;
    private BigDecimal pricePerNight;
}
