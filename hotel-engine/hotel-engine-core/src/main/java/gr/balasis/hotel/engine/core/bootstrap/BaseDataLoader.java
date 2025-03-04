package gr.balasis.hotel.engine.core.bootstrap;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.engine.core.service.GuestService;
import gr.balasis.hotel.engine.core.service.ReservationService;
import gr.balasis.hotel.engine.core.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class BaseDataLoader {
    public static final Logger logger = LoggerFactory.getLogger(BaseDataLoader.class);
    private static final Lorem lorem = LoremIpsum.getInstance();
    private final Random random = new Random();

    public abstract GuestService getGuestService();

    public abstract RoomService getRoomService();

    public abstract ReservationService getReservationService();

    protected void loadRooms() {
        for (int i = 0; i < 10; i++) {
            getRoomService().create(
                    Room.builder()
                            .roomNumber("10" + (i + 1))
                            .reserved(false)
                            .pricePerNight(new BigDecimal("100.00").add(new BigDecimal(i * 10)))
                            .build()
            );
        }
    }

    protected void loadGuests() {
        for (int i = 0; i < 5; i++) {
            getGuestService().create(
                    Guest.builder()
                            .firstName(lorem.getFirstName())
                            .lastName(lorem.getLastName())
                            .email(lorem.getEmail())
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
    }

    protected void loadReservations() {
        List<Guest> guests = getGuestService().findAll();
        List<Room> availableRooms = getRoomService().findAll().stream()
                .filter(room -> !room.isReserved())
                .collect(Collectors.toList());

        if (guests.isEmpty() || availableRooms.isEmpty()) {
            System.out.println("Skipping reservations: No guests or available rooms.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            if (guests.isEmpty() || availableRooms.isEmpty()) return;
            Room room = pickRandomRoom(availableRooms);
            getReservationService().create(createReservationDomain(pickRandomGuest(guests), room));
            room.setReserved(true);
            getRoomService().update(room);
        }
    }

    protected void loadPayments() {
        List<Reservation> reservations = getReservationService().findAll();

        for (Reservation reservation : reservations) {
            if (random.nextBoolean()) {
                getReservationService().finalizePaymentForReservation(
                        reservation.getGuest().getId(),
                        reservation.getId(),
                        reservation.getPayment()
                );
            }
        }
    }

    private Guest pickRandomGuest(List<Guest> guests) {
        return guests.removeFirst();
    }

    private Room pickRandomRoom(List<Room> rooms) {
        return rooms.removeFirst();
    }

    private Reservation createReservationDomain(Guest guest, Room room) {
        LocalDate checkInDate = LocalDate.now().plusDays(random.nextInt(10) + 1);
        return Reservation.builder()
                .guest(guest)
                .room(room)
                .checkInDate(checkInDate)
                .checkOutDate(checkInDate.plusDays(random.nextInt(5) + 1))
                .build();
    }
}

