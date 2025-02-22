package gr.balasis.hotel.core.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;




@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GuestEntity extends BaseEntity{

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate createdAt;
}
