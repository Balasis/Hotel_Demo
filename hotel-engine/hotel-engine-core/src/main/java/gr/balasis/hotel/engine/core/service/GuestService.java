package gr.balasis.hotel.engine.core.service;


import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.service.BaseService;

public interface GuestService extends BaseService<Guest> {

    void updateGuest(Long guestId, Guest updatedGuest);

    void updateEmail(Long id, String email);

    Guest getGuest(Long guestId);

    void deleteGuest(Long guestId);
}
