package gr.balasis.hotel.context.web.resource;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResource {

    private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
}