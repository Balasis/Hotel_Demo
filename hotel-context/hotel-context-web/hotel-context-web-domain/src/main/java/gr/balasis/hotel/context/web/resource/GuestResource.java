package gr.balasis.hotel.context.web.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class GuestResource extends BaseResource{
    private String firstName;
    private String lastName;
    private String email;
}
