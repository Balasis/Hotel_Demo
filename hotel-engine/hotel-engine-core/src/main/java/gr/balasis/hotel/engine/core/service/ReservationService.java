package gr.balasis.hotel.engine.core.service;


import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.resource.ReservationResource;

import java.util.List;

public interface ReservationService extends BaseService<Reservation,Long> {
    List<Reservation> findReservations(Long id);
    Reservation createReservation(Long id, Reservation reservation);
    Reservation getReservation(Long guestsId, Long reservationId);
    void updateReservation(Long guestId, Long reservationId, ReservationResource reservationResource);
    void manageReservationAction(Long guestId, Long reservationId, String action);

    Payment finalizePayment(Long guestId, Long reservationId, Payment payment) ;
    Payment getPayment(Long id, Long reservationId);

    Feedback createFeedback(Long guestsId, Long reservationId, Feedback domain);
    Feedback getFeedback(Long guestsId, Long reservationId);
    void updateFeedback(Long guestsId, Long reservationId, Feedback domain);
    void deleteFeedback(Long guestsId, Long reservationId);



}
