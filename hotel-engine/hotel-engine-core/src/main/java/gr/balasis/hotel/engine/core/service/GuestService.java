package gr.balasis.hotel.engine.core.service;


import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.service.BaseService;

public interface GuestService extends BaseService<Guest,Long> {

    Guest getGuest(Long guestId);
    void updateGuest(Long guestId, Guest updatedGuest);
    void deleteGuest(Long guestId);

    void updateEmail(Long id, String email);




}
