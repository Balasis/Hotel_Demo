package gr.balasis.hotel.context.base.domain;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.context.base.domain.Guest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseDomain {
    private Reservation reservation;
    private String message;
    private LocalDateTime createdAt;
}
