package gr.balasis.hotel.core.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
public class ReservationEntity extends BaseEntity{

    @Column(nullable = false)
    private Long guestId;

    @Column(nullable = false)
    private Long roomId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
