package gr.balasis.hotel.context.web.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
