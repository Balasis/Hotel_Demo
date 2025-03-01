package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.entity.GuestEntity;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.exception.EntityNotFoundException;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationResource, ReservationEntity> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public List<Reservation> findReservationsByGuestId(Long id) {
        return reservationRepository.findByGuestId(id).stream()
                .map(reservationMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation createReservationForGuest(Long id, Reservation reservation) {

        ReservationEntity SavedReservationEntity = reservationRepository.save(reservationMapper.toEntity(reservation));
        return reservationMapper.toDomainFromEntity(SavedReservationEntity);
    }

    @Override
    public void cancelReservation(Long id, Long reservationId) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!reservationEntity.getGuest().getId().equals(id)) {
            throw new IllegalArgumentException("Reservation does not belong to the guest");
        }

        reservationRepository.delete(reservationEntity);
    }


    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }
}
