package gr.balasis.hotel.modules.feedback.entity;

import gr.balasis.hotel.context.base.entity.BaseEntity;
import gr.balasis.hotel.context.base.entity.GuestEntity;
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


    @ManyToOne
    @JoinColumn(nullable = false)
    private GuestEntity guest;

    private Long reservationId;

    @Column(nullable = false)
    private String message;

    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
