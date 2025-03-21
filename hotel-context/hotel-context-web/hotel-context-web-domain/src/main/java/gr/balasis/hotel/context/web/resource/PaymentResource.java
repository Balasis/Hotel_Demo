package gr.balasis.hotel.context.web.resource;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResource extends BaseResource {
    @DecimalMin(value = "0", message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
}