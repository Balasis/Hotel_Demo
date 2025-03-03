package gr.balasis.hotel.core.app.service;

import gr.balasis.hotel.context.base.domain.domains.Guest;

public interface GuestService extends BaseService<Guest, Long> {
    //    Guest findByEmail(String email);
//    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);
//    List<Guest> findByFirstName(String firstName);
//    List<Guest> findByLastName(String lastName);
//    boolean existsById(Long guestId);
    void updateGuest(Long guestId, Guest updatedGuest);

    void updateEmail(Long id, String email);

    Guest findGuestById(Long guestId);

    void deleteGuestById(Long guestId);
}
