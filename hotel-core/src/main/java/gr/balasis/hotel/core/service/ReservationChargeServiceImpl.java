package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.ReservationCharge;
import gr.balasis.hotel.context.web.resource.ReservationChargeResource;
import gr.balasis.hotel.core.entity.ReservationChargeEntity;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.exception.EntityNotFoundException;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationChargeMapper;
import gr.balasis.hotel.core.repository.ReservationChargeRepository;
import gr.balasis.hotel.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReservationChargeServiceImpl extends BasicServiceImpl<ReservationCharge,
        ReservationChargeResource, ReservationChargeEntity> implements ReservationChargeService {

    private final ReservationRepository reservationRepository;
    private final ReservationChargeRepository reservationChargeRepository;
    private final ReservationChargeMapper reservationChargeMapper;

    //TODO createCharge, getChargeByReservationId(correct them...)

    public ReservationCharge getChargeByReservationId(Long reservationId) {
        Optional<ReservationChargeEntity> reservationChargeEntity = reservationChargeRepository
                .findByReservationId(reservationId);

        return reservationChargeMapper.toDomainFromEntity(reservationChargeEntity.orElseThrow(
                () -> new EntityNotFoundException("Reservation charge not found for reservation ID: " + reservationId)
        ));
    }

    public ReservationCharge createCharge(Long reservationId, ReservationCharge reservationCharge) {
        ReservationChargeEntity reservationChargeEntity = reservationChargeMapper.toEntity(reservationCharge);
        reservationChargeEntity.setReservation(reservationRepository.findById(reservationId).orElseThrow(
                () -> new EntityNotFoundException("Could not find reservation with id: " + reservationId)
        ));
        reservationChargeEntity = reservationChargeRepository.save(reservationChargeEntity);

        return reservationChargeMapper.toDomainFromEntity(reservationChargeEntity);

    }


    @Override
    public JpaRepository<ReservationChargeEntity, Long> getRepository() {

        return reservationChargeRepository;
    }

    @Override
    public BaseMapper<ReservationCharge, ReservationChargeResource, ReservationChargeEntity> getMapper() {

        return reservationChargeMapper;
    }
}
