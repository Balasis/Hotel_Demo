package gr.balasis.hotel.core.entity;

import gr.balasis.context.base.enums.ChargeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationChargeEntity extends BaseEntity{


    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private ReservationEntity reservation;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal roomCharge;

    @Column(nullable = false)
    private BigDecimal tax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargeStatus chargeStatus;
}
