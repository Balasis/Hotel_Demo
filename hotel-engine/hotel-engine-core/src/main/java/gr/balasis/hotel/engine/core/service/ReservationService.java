package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.domain.Feedback;
import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.service.BaseService;

import java.util.List;

public interface ReservationService extends BaseService<Reservation, Long> {
    List<Reservation> findReservationsByGuestId(Long id);

    Reservation createReservationForGuest(Long id, Reservation reservation);

    void cancelReservation(Long id, Long reservationId);

    Payment finalizePaymentForReservation(Long guestId, Long reservationId, Payment domainFromResource);

    Payment getPaymentForReservation(Long id, Long reservationId);

    Reservation findReservationById(Long guestsId, Long reservationId);


    Feedback createFeedback(Long guestsId, Long reservationId, Feedback domain);
    Feedback getFeedbackById(Long guestsId, Long reservationId);
    void updateFeedback(Long guestsId, Long reservationId, Feedback domain);
    void deleteFeedback(Long guestsId, Long reservationId);

}
