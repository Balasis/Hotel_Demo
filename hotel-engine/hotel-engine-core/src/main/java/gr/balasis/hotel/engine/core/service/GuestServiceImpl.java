package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.exception.DuplicateEmailException;
import gr.balasis.hotel.context.base.exception.GuestNotFoundException;
import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.engine.core.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest> implements GuestService {
    private final GuestRepository guestRepository;

    @Override
    public Guest create(Guest guest) {
        if (guestRepository.findByEmail(guest.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists"); // Handle accordingly
        }
        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuest(Long guestId) {
        return  guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found"));
    }

    @Override
    public void updateGuest(Long guestId, Guest updatedGuest) {
        Guest existingGuest = guestRepository.findById(guestId).orElseThrow(
                () -> new GuestNotFoundException("Guest with ID " + guestId + " not found for update"));
        validateEmailUniqueness(updatedGuest.getEmail());
        existingGuest.setEmail(updatedGuest.getEmail());
        guestRepository.save(existingGuest);
    }

    @Override
    public void deleteGuest(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new GuestNotFoundException("Guest with ID " + guestId + " not found for deletion");
        }
        guestRepository.deleteById(guestId);
    }

    @Override
    public void updateEmail(Long guestId, String email) {
        Guest guestEntity = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest with ID " + guestId
                        + " not found for email update"));
        validateEmailUniqueness(email);
        guestEntity.setEmail(email);
        guestRepository.save(guestEntity);
    }

    @Override
    public JpaRepository<Guest, Long> getRepository() {
        return guestRepository;
    }

    private void validateEmailUniqueness(String newEmail) {
        if (guestRepository.findByEmail(newEmail).isPresent()) {
            throw new DuplicateEmailException("Email " + newEmail + " is already in use by another guest");
        }
    }

}
