package gr.balasis.hotel.context.base.domain.domains;

import gr.balasis.hotel.context.base.domain.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
}