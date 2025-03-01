package gr.balasis.hotel.core.tests;

import gr.balasis.hotel.core.mapper.GuestMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.core.mapper.RoomMapper;
import gr.balasis.hotel.core.service.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Mappers {
    private final GuestMapper guestMapper;
    private final RoomMapper roomMapper;
    private final ReservationMapper reservationMapper;
    private final GuestService guestService;

    @Bean
    public CommandLineRunner run() {
        return args -> {
            //mappers are disabled from using the builders, it wouldn't pass parent fields(builders' use skips Id)
//            GuestEntity guestEntity = GuestEntity.builder()
//                    .firstName("john")
//                    .lastName("balasis")
//                    .email("giovani1994a@gmail.com")
//                    .build();
//            guestEntity.setId(325L);
//            Guest guest = guestMapper.toDomainFromEntity(guestEntity);
//
////
////            RoomEntity roomEntity = RoomEntity.builder()
////                    .pricePerNight(new BigDecimal("100"))
////                    .roomNumber("r12")
////                    .build();
////            roomEntity.setId(125L);
////            Room room = roomMapper.toDomainFromEntity(roomEntity);
////
////            ReservationEntity reservationEntity = ReservationEntity.builder()
////                    .roomId(125L)
////                    .guestId(325L)
////                    .build();
////            reservationEntity.setId(1L);
////            Reservation reservation = reservationMapper.toDomainFromEntity(reservationEntity);
////
////            System.out.println("Guest domain id is :  "  + guest.getId() );
////            System.out.println("Guest entity id is :  "  + guestMapper.toEntity(guest).getId() );
////            System.out.println("Guest resource id is: "  + guestMapper.toResource(guest).getId() );
////
////            System.out.println("Room domain id is :  "  + room.getId() );
////            System.out.println("Room entity id is :  "  + roomMapper.toEntity(room).getId() );
////            System.out.println("Room resource id is: "  + roomMapper.toResource(room).getId() );
////
////            System.out.println("Reservation domain id is :  "  + reservation.getId() );
////            System.out.println("Reservation entity id is :  "  + reservationMapper.toEntity(reservation).getId() );
////            System.out.println("Reservation resource id is: "  + reservationMapper.toResource(reservation).getId() );

        };
    }

}
