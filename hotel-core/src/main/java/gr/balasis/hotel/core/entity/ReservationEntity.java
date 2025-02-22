package gr.balasis.hotel.core.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class ReservationEntity extends BaseEntity{

    @Column(nullable = false)
    private Long guestId;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
