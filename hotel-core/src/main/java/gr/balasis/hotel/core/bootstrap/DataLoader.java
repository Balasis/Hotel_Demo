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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private static final Lorem lorem = LoremIpsum.getInstance();
    private final Random random = new Random();

    @Override
    public void run(ApplicationArguments args) {
        loadRooms();
        loadGuests();
        loadReservations();
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

    private void loadReservations() {
        List<Guest> guests = guestService.findAll();
        List<Room> availableRooms = roomService.findAll().stream()
                .filter(room -> !room.isReserved())
                .collect(Collectors.toList());

        if (guests.isEmpty() || availableRooms.isEmpty()) {
            System.out.println("Skipping reservations: No guests or available rooms.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            Room room = pickRandomRoom(availableRooms);
            reservationService.create(createReservationDomain( pickRandomGuest(guests),room) );
            room.setReserved(true);
            roomService.update(room);
        }

    }

    private Guest pickRandomGuest(List<Guest> guests){
        return guests.get(random.nextInt(guests.size()));
    }

    private Room pickRandomRoom(List<Room> rooms){
        return rooms.remove(random.nextInt(rooms.size()));
    }

    private Reservation createReservationDomain(Guest guest, Room room){
        LocalDate checkInDate = LocalDate.now().plusDays(random.nextInt(10) + 1);
        return Reservation.builder()
                .guest(guest)
                .room(room)
                .checkInDate(checkInDate)
                .checkOutDate(checkInDate.plusDays(random.nextInt(5) + 1))
                .build();
    }


}