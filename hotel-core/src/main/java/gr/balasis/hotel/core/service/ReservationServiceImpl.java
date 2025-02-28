package gr.balasis.hotel.core.service;

import gr.balasis.context.base.enums.ChargeStatus;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.entity.ReservationChargeEntity;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.core.repository.ReservationChargeRepository;
import gr.balasis.hotel.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationResource, ReservationEntity> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationChargeRepository reservationChargeRepository;

    public List<Reservation> findByGuestId(Long guestId){
        return reservationRepository.findByGuestId(guestId).stream()
                .map(reservationMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation create(final Reservation item) {
        ReservationEntity reservationEntity = getMapper().toEntity(item);
        ReservationEntity savedReservation = getRepository().save(reservationEntity);

        long nights = ChronoUnit.DAYS.between(item.getCheckInDate(), item.getCheckOutDate());
        BigDecimal roomCharge = item.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(nights));

        ReservationChargeEntity reservationChargeEntity = ReservationChargeEntity.builder()
                .reservation(savedReservation)
                .roomCharge(roomCharge)
                .totalAmount(roomCharge)
                .chargeStatus(ChargeStatus.PENDING)
                .build();

        reservationChargeRepository.save(reservationChargeEntity);

        return getMapper().toDomainFromEntity(savedReservation);
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
