package gr.balasis.hotel.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="rooms")
public class RoomEntity extends BaseEntity{

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private boolean reserved;

    @Column(nullable = false)
    private BigDecimal pricePerNight;
}
