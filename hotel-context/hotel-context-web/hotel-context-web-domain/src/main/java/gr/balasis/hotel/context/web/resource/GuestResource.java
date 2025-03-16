package gr.balasis.hotel.context.web.resource;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class GuestResource extends BaseResource {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}
