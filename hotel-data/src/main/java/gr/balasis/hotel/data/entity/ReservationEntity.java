package gr.balasis.hotel.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class ReservationEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private GuestEntity guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomEntity room;
    private LocalDateTime createdAt;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private PaymentEntity payment;
}
