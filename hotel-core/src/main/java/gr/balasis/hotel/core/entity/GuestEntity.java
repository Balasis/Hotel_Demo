package gr.balasis.hotel.core.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
public class GuestEntity extends BaseEntity{

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate createdAt;
}
