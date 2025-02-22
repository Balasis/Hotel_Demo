package gr.balasis.hotel.context.base.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
public class Guest extends BaseDomain{
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
}
