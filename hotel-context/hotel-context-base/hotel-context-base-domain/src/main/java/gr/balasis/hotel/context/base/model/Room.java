package gr.balasis.hotel.context.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends BaseModel {

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private boolean reserved;

    @Column(nullable = false)
    private BigDecimal pricePerNight;
}
