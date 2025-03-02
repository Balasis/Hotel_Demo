package gr.balasis.hotel.context.base.domain.domains;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Guest extends BaseDomain{
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
}
