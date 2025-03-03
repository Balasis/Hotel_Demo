package gr.balasis.hotel.core.app.service;

import gr.balasis.hotel.context.base.domain.domains.Guest;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.GuestMapper;
import gr.balasis.hotel.context.web.exception.EntityNotFoundException;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.data.entity.GuestEntity;
import gr.balasis.hotel.data.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest, GuestResource, GuestEntity> implements GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Override
    public Guest create(Guest guest) {
        if (guestRepository.findByEmail(guest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists"); // Handle accordingly
        }
        return guestMapper.toDomainFromEntity(guestRepository.save(guestMapper.toEntity(guest)));
    }

    @Override
    public Guest findGuestById(Long guestId) {
       GuestEntity guest= guestRepository.findById(guestId)
               .orElseThrow(() -> new EntityNotFoundException("Guest not found"));

        return  guestMapper.toDomainFromEntity(guest);
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
        guestMapper.toDomainFromEntity(guestRepository.save(guestEntity));
    }



    @Override
    public JpaRepository<GuestEntity, Long> getRepository() {
        return guestRepository;
    }

    @Override
    public BaseMapper<Guest, GuestResource, GuestEntity> getMapper() {
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
