package gr.balasis.hotel.context.web.resource;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResource extends BaseResource{
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
}