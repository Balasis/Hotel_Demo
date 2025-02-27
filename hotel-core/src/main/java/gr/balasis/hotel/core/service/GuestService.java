package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Guest;

import java.util.List;
import java.util.stream.Collectors;

public interface GuestService extends BaseService<Guest,Long>{
    Guest findByEmail(String email);
    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);
    List<Guest> findByFirstName(String firstName);
    List<Guest> findByLastName(String lastName);
}
