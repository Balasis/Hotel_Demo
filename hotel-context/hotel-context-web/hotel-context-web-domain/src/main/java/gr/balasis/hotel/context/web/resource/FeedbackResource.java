package gr.balasis.hotel.context.web.resource;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResource extends BaseResource {
    private LocalDateTime createdAt;
    private String message;
}
