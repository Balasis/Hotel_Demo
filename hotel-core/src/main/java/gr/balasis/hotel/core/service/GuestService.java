package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Guest;

import java.util.List;

public interface GuestService extends BaseService<Guest,Long>{
    Guest findByEmail(String email);
    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);
    List<Guest> findByFirstName(String firstName);
    List<Guest> findByLastName(String lastName);
    boolean existsById(Long guestId);
}
