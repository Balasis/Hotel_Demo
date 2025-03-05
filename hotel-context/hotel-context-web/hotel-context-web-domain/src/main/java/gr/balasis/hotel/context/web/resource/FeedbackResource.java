package gr.balasis.hotel.context.web.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResource extends BaseResource {
    private ReservationResource reservation;
    private String message;
}
