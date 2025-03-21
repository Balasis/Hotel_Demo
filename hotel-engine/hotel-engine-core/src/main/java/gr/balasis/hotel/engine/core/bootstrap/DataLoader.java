package gr.balasis.hotel.engine.core.bootstrap;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import gr.balasis.hotel.context.base.enumeration.BedType;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.engine.core.service.GuestService;
import gr.balasis.hotel.engine.core.service.ReservationService;
import gr.balasis.hotel.engine.core.service.RoomService;
import gr.balasis.hotel.engine.core.validation.GuestValidator;
import gr.balasis.hotel.engine.core.validation.ReservationValidator;
import gr.balasis.hotel.engine.core.validation.RoomValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@AllArgsConstructor
@Profile({"h2", "postgre"})
public class DataLoader implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private static final Lorem lorem = LoremIpsum.getInstance();
    private final GuestService guestService;
    private final GuestValidator guestValidator;
    private final RoomService roomService;
    private final RoomValidator roomValidator;
    private final ReservationService reservationService;
    private final Random random = new Random();
    private final MessageSource feedbackMessages;
    private final ReservationValidator reservationValidator;

    @Override
    public void run(ApplicationArguments args) {
        loadRooms();
        loadGuests();
        loadReservations();
        loadPayments(); // Payments are created when reservations are made; this just marks some as paid.
        loadFeedback();
    }

    private void loadRooms() {
        logger.trace("Loading rooms...");
        for (int i = 0; i < 9; i++) {
            try {
                createRoomTransactional(i);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }

        logger.trace("Finished loading rooms");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createRoomTransactional(int i) {
        roomService.create(
                roomValidator.validate(
                        Room.builder()
                                .roomNumber("10" + (i + 1))
                                .pricePerNight(new BigDecimal("100.00").add(new BigDecimal(i * 10)))
                                .bedType(random.nextBoolean() ? BedType.SINGLE : BedType.DOUBLE)
                                .floor(random.nextInt(1, 4))
                                .build()
                )
        );
    }

    private void loadGuests() {
        logger.trace("Loading guests...");
        for (int i = 0; i < 5; i++) {
            try {
                createGuestTransactional();
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }

        logger.trace("Finished loading guests");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createGuestTransactional() {
        guestService.create(
                guestValidator.validate(
                        Guest.builder()
                                .firstName(lorem.getFirstName())
                                .lastName(lorem.getLastName())
                                .email(lorem.getEmail())
                                .createdAt(LocalDate.now())
                                .birthDate(LocalDate.now().minusYears(20 + random.nextInt(41)))
                                .build()
                )
        );
    }

    private void loadReservations() {
        logger.trace("Loading reservations...");
        List<Guest> guests = guestService.searchBy(null, null, null, null);
        List<Room> rooms = roomService.searchBy(null, null, null, null);

        if (guests.isEmpty() || rooms.isEmpty()) {
            logger.trace("Skipping reservations: No guests or rooms.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            if (guests.isEmpty() || rooms.isEmpty()) return;

            try {
                createReservationTransactional(guests.removeFirst(), rooms.removeFirst());
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        logger.trace("Finished loading reservations");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createReservationTransactional(Guest guest, Room room) {
        LocalDate checkInDate = LocalDate.now().plusDays(random.nextInt(10) + 1);
        reservationService.create(
                reservationValidator.validate(
                        Reservation.builder()
                                .guest(guest)
                                .room(room)
                                .checkInDate(checkInDate)
                                .checkOutDate(checkInDate.plusDays(random.nextInt(5) + 1))
                                .build())
        );
    }

    private void loadPayments() {
        logger.trace("Loading payments...");
        List<Reservation> reservations = reservationService.findAll();

        for (Reservation reservation : reservations) {
            try {
                if (random.nextBoolean()) {
                    markPaymentAsCompleted(reservation.getId());
                }
            } catch (Exception e) {
                logger.warn("Failed to mark payment as completed: " + e.getMessage());
            }
        }
        logger.trace("Finished loading payments");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPaymentAsCompleted(Long reservationId) {
        reservationService.manageReservationAction(reservationId, "Pay");
    }

    private void loadFeedback() {
        logger.trace("Loading feedback...");
        List<Reservation> reservations = reservationService.findAll();

        for (Reservation reservation : reservations) {
            try {
                createFeedbackTransactional(reservation);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }

        }
        logger.trace("Finished loading feedback");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createFeedbackTransactional(Reservation reservation) {
        int messageNumber = random.nextInt(10) + 1;
        var theMessage = "feedback.message" + messageNumber;
        var feedback = reservationValidator.validateFeedback(
                reservation.getId(),
                reservation.getGuest().getId(),
                Feedback.builder()
                        .createdAt(LocalDateTime.now())
                        .reservation(reservation)
                        .message(feedbackMessages.getMessage(theMessage, new Object[]{reservation.getGuest().getFirstName()},
                                Locale.getDefault()))
                        .build()
        );

        reservationService.createFeedback(reservation.getId(), feedback);
    }

}
