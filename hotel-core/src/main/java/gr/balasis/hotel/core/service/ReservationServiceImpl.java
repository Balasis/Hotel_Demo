package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.mapper.entitydomain.EDbaseMapper;
import gr.balasis.hotel.core.mapper.entitydomain.EDreservationMapper;
import gr.balasis.hotel.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationEntity> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final EDreservationMapper eDreservationMapper;

    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public EDbaseMapper<ReservationEntity, Reservation> getMapper() {
        return eDreservationMapper;
    }
}
