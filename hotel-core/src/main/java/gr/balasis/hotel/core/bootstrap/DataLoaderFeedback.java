package gr.balasis.hotel.core.bootstrap;


import gr.balasis.hotel.context.base.domain.domains.Guest;
import gr.balasis.hotel.context.base.domain.domains.Reservation;
import gr.balasis.hotel.core.service.GuestService;
import gr.balasis.hotel.core.service.ReservationService;
import gr.balasis.hotel.core.service.RoomService;
import gr.balasis.hotel.data.entity.ReservationEntity;
import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.service.FeedbackService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

import java.util.List;


@Component
@Profile("dev-feedback")
@AllArgsConstructor
public class DataLoaderFeedback extends BaseDataLoader implements ApplicationRunner {
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final FeedbackService feedbackService;
    private final MessageSource messageSource;


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        loadRooms();
        loadGuests();
        loadReservations();
        loadFeedbackMessages();
        BaseDataLoader.logger.info("dev-feedback runs");
    }

    public void loadFeedbackMessages(){
        Random random = new Random();
        List<Reservation> reservations = reservationService.findAll();
        for(Reservation reservation:reservations){
            if(feedbackExistsAlready(reservation.getId())){
                continue;
            }
            Guest guest = reservation.getGuest();
            int messageNumber = random.nextInt(10) + 1;
            String theMessage = "feedback.message"+messageNumber;
            Feedback feedback = Feedback.builder()
                    .guest(guest)
                    .createdAt(LocalDateTime.now())
                    .reservationId(reservation.getId())
                    .message( messageSource.getMessage( theMessage,new Object[]{guest.getFirstName()}, Locale.getDefault()) )
                    .build();
            feedbackService.createFeedback(guest.getId(),reservation.getId(),feedback);
        }
    }

    private boolean feedbackExistsAlready(Long reservationId){
      return  feedbackService.feedbackExistsForReservationId(reservationId);
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
