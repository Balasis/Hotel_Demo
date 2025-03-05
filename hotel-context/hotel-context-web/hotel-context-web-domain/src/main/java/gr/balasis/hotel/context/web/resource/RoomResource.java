package gr.balasis.hotel.context.web.resource;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RoomResource extends BaseResource {
    private String roomNumber;
    private boolean reserved;
    private BigDecimal pricePerNight;
}
