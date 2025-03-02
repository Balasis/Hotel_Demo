package gr.balasis.hotel.modules.feedback.domain;

import gr.balasis.hotel.context.base.domain.domains.BaseDomain;
import gr.balasis.hotel.context.base.domain.domains.Guest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseDomain {
    private Guest guest;
    private Long reservationId;
    private String message;
    private int rating;
    private LocalDateTime createdAt;
}
