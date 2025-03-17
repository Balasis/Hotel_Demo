package gr.balasis.hotel.context.web.resource;

import gr.balasis.hotel.context.base.enumeration.BedType;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RoomResource extends BaseResource {
    private String roomNumber;
    private BigDecimal pricePerNight;
    private String bedType;
    private Integer floor;
}
