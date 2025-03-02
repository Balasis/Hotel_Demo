package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.domains.Guest;

public interface GuestService extends BaseService<Guest,Long>{
//    Guest findByEmail(String email);
//    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);
//    List<Guest> findByFirstName(String firstName);
//    List<Guest> findByLastName(String lastName);
//    boolean existsById(Long guestId);

    void deleteById(Long id);
    Guest updateEmail(Long id, String email);
}
