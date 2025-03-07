package gr.balasis.hotel.context.base.model;


import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseModel {
    @OneToOne
    @JoinColumn(nullable = false)
    private Reservation reservation;
    @Column(nullable = false)
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
}

