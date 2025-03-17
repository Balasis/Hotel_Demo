package gr.balasis.hotel.context.base.model;

import gr.balasis.hotel.context.base.enumeration.BedType;
import jakarta.persistence.*;
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
@Table(name = "rooms" ,indexes = {
        @Index(name = "index_room_roomNumber",columnList = "room_number")
})
public class Room extends BaseModel {

    @Column(nullable = false,unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedType bedType;

    @Column(nullable = false)
    private Integer floor;

}
