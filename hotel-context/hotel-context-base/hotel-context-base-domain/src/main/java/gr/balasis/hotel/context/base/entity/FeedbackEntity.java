package gr.balasis.hotel.context.base.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feedback")
public class FeedbackEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(nullable = false)
    private ReservationEntity reservation;

    @Column(nullable = false)
    private String message;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
