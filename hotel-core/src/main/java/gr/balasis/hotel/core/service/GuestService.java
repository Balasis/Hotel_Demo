package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;

import java.util.List;

public interface GuestService extends BaseService<Guest,Long>{
//    Guest findByEmail(String email);
//    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);
//    List<Guest> findByFirstName(String firstName);
//    List<Guest> findByLastName(String lastName);
//    boolean existsById(Long guestId);

    List<Reservation> findReservationsByGuestId(Long id);
    Reservation createReservationForGuest(Long id, Reservation reservation);
    void cancelReservation(Long id, Long reservationId);
    void deleteById(Long id);
    Guest updateEmail(Long id, String email);
}
