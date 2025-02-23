package gr.balasis.hotel.context.web.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RoomResource extends BaseResource{
    private Long id;
    private String roomNumber;
    private boolean available;
    private BigDecimal pricePerNight;
}
