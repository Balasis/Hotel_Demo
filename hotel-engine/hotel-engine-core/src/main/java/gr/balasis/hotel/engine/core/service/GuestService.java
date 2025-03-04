package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.service.BaseService;

public interface GuestService extends BaseService<Guest> {

    void updateGuest(Long guestId, Guest updatedGuest);

    void updateEmail(Long id, String email);

    Guest findGuestById(Long guestId);

    void deleteGuestById(Long guestId);
}
