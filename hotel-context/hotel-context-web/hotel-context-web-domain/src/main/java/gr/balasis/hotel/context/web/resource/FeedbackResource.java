package gr.balasis.hotel.context.web.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResource extends BaseResource {
    @Past(message = "Creation date must be in the past")
    private LocalDateTime createdAt;
    @NotBlank(message = "Feedback message cannot empty")
    private String message;
}
