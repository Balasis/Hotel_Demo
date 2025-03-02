package gr.balasis.hotel.modules.feedback.resource;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.web.resource.BaseResource;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResource extends BaseResource {
    private Guest guest;
    private String message;
    private int rating;
}
