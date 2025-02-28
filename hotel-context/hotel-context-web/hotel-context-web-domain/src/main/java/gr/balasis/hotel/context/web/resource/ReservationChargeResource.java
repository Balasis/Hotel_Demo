package gr.balasis.hotel.context.web.resource;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReservationChargeResource extends BaseResource{
    private ReservationResource reservation;
    private BigDecimal totalAmount;
    private BigDecimal roomCharge;
    private String chargeStatus;
}