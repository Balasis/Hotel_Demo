package gr.balasis.hotel.modules.feedback.resource;

import gr.balasis.hotel.context.base.domain.domains.Guest;
import gr.balasis.hotel.context.web.resource.BaseResource;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResource extends BaseResource {
    private Guest guest;
    private Long reservationId;
    private String message;
    private int rating;
}
