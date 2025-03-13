package gr.balasis.hotel.context.web.resource;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class GuestResource extends BaseResource {
    private String firstName;
    private String lastName;
    private String email;
}
