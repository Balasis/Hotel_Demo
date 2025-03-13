package gr.balasis.hotel.context.base.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "feedback")
public class Feedback extends BaseModel {

    @OneToOne
    @JoinColumn(nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private String message;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
