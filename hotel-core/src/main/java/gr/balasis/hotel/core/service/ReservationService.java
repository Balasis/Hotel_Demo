package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.base.domain.Reservation;

import java.util.List;

public interface ReservationService extends BaseService<Reservation,Long> {
    List<Reservation> findReservationsByGuestId(Long id);
    Reservation createReservationForGuest(Long id, Reservation reservation);
    void cancelReservation(Long id, Long reservationId);
    Payment processPaymentForReservation(Long guestId, Long reservationId, Payment domainFromResource);
    Payment getPaymentForReservation(Long id, Long reservationId);
    List<Payment> getReservationPaymentsForGuest(Long id);
}
