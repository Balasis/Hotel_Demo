package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.engine.core.mapper.base.GuestMapper;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.exception.EntityNotFoundException;
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
            throw new IllegalArgumentException("Email already exists"); // Handle accordingly
        }
        return guestMapper.toDomain(guestRepository.save(guestMapper.toEntity(guest)));
    }

    @Override
    public Guest findGuestById(Long guestId) {
       GuestEntity guest= guestRepository.findById(guestId)
               .orElseThrow(() -> new EntityNotFoundException("Guest not found"));

        return  guestMapper.toDomain(guest);
    }

    public void updateGuest(Long guestId, Guest updatedGuest) {
        validateGuestId(guestId, updatedGuest);
        GuestEntity existingGuest = findExistingGuest(guestId);
        validateEmailUniqueness(existingGuest, updatedGuest.getEmail());

        existingGuest.setEmail(updatedGuest.getEmail());
        guestRepository.save(existingGuest);
    }

    @Override
    public void deleteGuestById(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new EntityNotFoundException("Guest not found");
        }
        guestRepository.deleteById(guestId);
    }

    @Override
    public void updateEmail(Long guestId, String email) {
        GuestEntity guestEntity = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
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

    private void validateGuestId(Long guestId, Guest updatedGuest) {
        if (!guestId.equals(updatedGuest.getId())) {
            throw new IllegalArgumentException("Guest ID mismatch");
        }
    }

    private GuestEntity findExistingGuest(Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));
    }

    private void validateEmailUniqueness(GuestEntity existingGuest, String newEmail) {
        if (!existingGuest.getEmail().equals(newEmail) &&
                guestRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

}
