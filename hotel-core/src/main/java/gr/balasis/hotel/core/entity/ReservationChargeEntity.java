package gr.balasis.hotel.core.entity;

import gr.balasis.context.base.enums.ChargeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
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
    private BigDecimal amenitiesCharge;

    @Column(nullable = false)
    private BigDecimal tax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargeStatus chargeStatus;
}
