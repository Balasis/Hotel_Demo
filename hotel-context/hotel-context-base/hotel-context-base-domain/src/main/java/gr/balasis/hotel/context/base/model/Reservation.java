package gr.balasis.hotel.context.base.model;

import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation extends BaseModel {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Guest guest;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.ACTIVE;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private Feedback feedback;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private Payment payment;
}
