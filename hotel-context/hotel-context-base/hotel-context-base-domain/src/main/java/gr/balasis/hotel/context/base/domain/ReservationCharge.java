package gr.balasis.hotel.context.base.domain;

import gr.balasis.context.base.enums.ChargeStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ReservationCharge extends BaseDomain{
    private Long reservationId;
    private BigDecimal totalAmount;
    private BigDecimal roomCharge;
    private BigDecimal tax;
    private ChargeStatus chargeStatus;
}