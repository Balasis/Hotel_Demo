package gr.balasis.hotel.context.base.model;


import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments" ,indexes = {
        @Index(name = "index_payment_reservation",columnList = "reservation_id"),
        @Index(name = "index_payment_paymentStatus",columnList = "payment_status")
})
public class Payment extends BaseModel {
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Reservation reservation;
    @Column(nullable = false)
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
}

