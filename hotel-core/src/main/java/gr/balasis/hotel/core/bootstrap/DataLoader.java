package gr.balasis.hotel.core.bootstrap;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.core.service.GuestService;
import gr.balasis.hotel.core.service.ReservationService;
import gr.balasis.hotel.core.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private static final Lorem lorem = LoremIpsum.getInstance();

    @Override
    public void run(ApplicationArguments args) {
        loadRooms();
        loadGuests();
//        loadReservations();
    }

    private void loadRooms() {
        for (int i = 0; i < 10; i++) {
            roomService.create(
                    Room.builder()
                            .roomNumber("10" + (i+1))
                            .reserved(false)
                            .pricePerNight(new BigDecimal("100.00").add(new BigDecimal(i * 10)))
                            .build()
            );
        }
    }

    private void loadGuests() {
        Set<String> usedEmails = new HashSet<>();

        for (int i = 0; i < 5; i++) {
            String email;
            do{
                email = lorem.getEmail();
            }while(usedEmails.contains(email));
            usedEmails.add(email);

            guestService.create(
            Guest.builder()
                    .firstName(lorem.getFirstName())
                    .lastName(lorem.getLastName())
                    .email(email)
                    .createdAt(LocalDateTime.now()).build()
            );
        }
    }

//    private void loadReservations() {
//        List<Reservation> reservations = List.of(
//                Reservation.builder().guestId(1L).roomId(1L).checkInDate(LocalDate.now()).checkOutDate(LocalDate.now().plusDays(3)).build(),
//                Reservation.builder().guestId(2L).roomId(2L).checkInDate(LocalDate.now()).checkOutDate(LocalDate.now().plusDays(2)).build()
//        );
//        reservations.forEach(reservationService::create);
//    }
}