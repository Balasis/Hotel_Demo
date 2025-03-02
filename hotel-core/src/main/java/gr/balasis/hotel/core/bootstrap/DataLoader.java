package gr.balasis.hotel.core.bootstrap;

import gr.balasis.hotel.core.service.GuestService;
import gr.balasis.hotel.core.service.ReservationService;
import gr.balasis.hotel.core.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("dev")
@AllArgsConstructor
public class DataLoader extends BaseDataLoader implements ApplicationRunner {
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        loadRooms();
        loadGuests();
        loadReservations();
        BaseDataLoader.logger.info("Default dev DataLoader started");
    }

    @Override
    public GuestService getGuestService() {
        return guestService;
    }

    @Override
    public RoomService getRoomService() {
        return roomService;
    }

    @Override
    public ReservationService getReservationService() {
        return reservationService;
    }

}