package gr.balasis.hotel.context.web.resource;

import gr.balasis.hotel.context.base.enumeration.BedType;
import gr.balasis.hotel.context.web.validation.custom.ValidEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RoomResource extends BaseResource {

    @NotBlank(message = "Room number is mandatory")
    private String roomNumber;

    @NotNull(message = "Price per night is mandatory")
    @DecimalMin(value = "0.01", message = "Price per night must be greater than zero")
    private BigDecimal pricePerNight;

    @NotNull(message = "Bed type is mandatory ")
    @ValidEnum(enumClass = BedType.class, message = "Invalid bed type")
    private String bedType;

    @NotNull(message = "Floor number cannot be null")
    private Integer floor;
}
