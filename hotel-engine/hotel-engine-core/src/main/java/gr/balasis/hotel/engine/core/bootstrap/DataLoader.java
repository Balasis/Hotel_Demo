package gr.balasis.hotel.engine.core.bootstrap;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.exception.HotelException;
import gr.balasis.hotel.context.base.model.*;
import gr.balasis.hotel.engine.core.service.GuestService;
import gr.balasis.hotel.engine.core.service.ReservationService;
import gr.balasis.hotel.engine.core.service.RoomService;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Component
@Profile("dev-includeloader")
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private static final Lorem lorem = LoremIpsum.getInstance();
    private final Random random = new Random();
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        loadRooms();
        loadGuests();
        loadReservations();
//        loadPayments();//payments been created when reservations do. loadPayments() only set some as paid.
        loadFeedback();
        logger.trace("Profile: dev-includeloader on.");
    }

    private void loadRooms() {
        logger.trace("Loading rooms...");
        for (int i = 0; i < 9; i++) {
            roomService.create(
                    Room.builder()
                            .roomNumber("10" + (i + 1))
                            .reserved(false)
                            .pricePerNight(new BigDecimal("100.00").add(new BigDecimal(i * 10)))
                            .build()
            );
        }
        logger.trace("Finished loading rooms");
    }

    private void loadGuests() {
        logger.trace("Loading guests...");
        for (int i = 0; i < 5; i++) {
            guestService.create(
                    Guest.builder()
                            .firstName(lorem.getFirstName())
                            .lastName(lorem.getLastName())
                            .email(lorem.getEmail())
                            .createdAt(LocalDate.now())
                            .build()
            );
        }
        logger.trace("Finished loading guests");
    }

    private void loadReservations() {
        logger.trace("Loading reservations...");
        List<Guest> guests = guestService.findAll();
        List<Room> availableRooms = roomService.findAll().stream()
                .filter(room -> !room.isReserved())
                .collect(Collectors.toList());

        if (guests.isEmpty() || availableRooms.isEmpty()) {
            logger.trace("Skipping reservations: No guests or available rooms.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            if (guests.isEmpty() || availableRooms.isEmpty()) return;
            Room room = pickRandomRoom(availableRooms);
            reservationService.create(createReservation(pickRandomGuest(guests), room));
            room.setReserved(true);
            roomService.update(room);
        }
        logger.trace("Finished loading guests");
    }

    private void loadPayments() {
        logger.trace("Loading payments...");
        List<Reservation> reservations = reservationService.findAll();
        for (Reservation reservation : reservations) {
                if (random.nextBoolean()) {
                    reservationService.manageReservationAction(reservation.getId(),"Pay");
                }
        }
        logger.trace("Finished loading payments");
    }

    private void loadFeedback() {
        logger.trace("Loading feedback...");
        List<Reservation> reservations = reservationService.findAll();
        for (Reservation reservation : reservations) {
            int messageNumber = random.nextInt(10) + 1;
            String theMessage = "feedback.message" + messageNumber;
            reservationService.createFeedback(
                    reservation.getId(),
                    Feedback.builder()
                            .createdAt(LocalDateTime.now())
                            .reservation(reservation)
                            .message(messageSource.getMessage(theMessage,
                                    new Object[]{reservation.getGuest().getFirstName()},
                                    Locale.getDefault()))
                            .build()
                    );
        }

        logger.trace("Finished loading feedback");
    }

    private Guest pickRandomGuest(List<Guest> guests) {
        return guests.removeFirst();
    }

    private Room pickRandomRoom(List<Room> rooms) {
        return rooms.removeFirst();
    }

    private Reservation createReservation(Guest guest, Room room) {
        LocalDate checkInDate = LocalDate.now().plusDays(random.nextInt(10) + 1);
        return Reservation.builder()
                .guest(guest)
                .room(room)
                .checkInDate(checkInDate)
                .checkOutDate(checkInDate.plusDays(random.nextInt(5) + 1))
                .build();
    }
}