package gr.balasis.hotel.engine.core.service;


import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.service.BaseService;

import java.time.LocalDate;
import java.util.List;

public interface GuestService extends BaseService<Guest,Long> {
    List<Guest> searchBy(String email, String firstName, String lastName, LocalDate birthDate);
}
