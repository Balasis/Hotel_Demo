package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.exception.DuplicateEmailException;
import gr.balasis.hotel.context.base.exception.GuestNotFoundException;
import gr.balasis.hotel.engine.core.mapper.base.GuestMapper;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.entity.GuestEntity;
import gr.balasis.hotel.engine.core.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest,GuestEntity> implements GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Override
    public Guest create(Guest guest) {
        if (guestRepository.findByEmail(guest.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists"); // Handle accordingly
        }
        return guestMapper.toDomain(guestRepository.save(guestMapper.toEntity(guest)));
    }

    @Override
    public Guest getGuest(Long guestId) {
       GuestEntity guest= guestRepository.findById(guestId)
               .orElseThrow(() -> new GuestNotFoundException("Guest not found"));
        return  guestMapper.toDomain(guest);
    }

    @Override
    public void updateGuest(Long guestId, Guest updatedGuest) {
        GuestEntity existingGuest = guestRepository.findById(guestId).orElseThrow(
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
        GuestEntity guestEntity = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest with ID " + guestId
                        + " not found for email update"));
        validateEmailUniqueness(email);
        guestEntity.setEmail(email);
        guestMapper.toDomain(guestRepository.save(guestEntity));
    }

    @Override
    public JpaRepository<GuestEntity, Long> getRepository() {
        return guestRepository;
    }

    @Override
    public BaseMapper<Guest,GuestEntity> getMapper() {
        return guestMapper;
    }

    private void validateEmailUniqueness(String newEmail) {
        if (guestRepository.findByEmail(newEmail).isPresent()) {
            throw new DuplicateEmailException("Email " + newEmail + " is already in use by another guest");
        }
    }

}
