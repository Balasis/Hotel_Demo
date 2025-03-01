package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.data.entity.ReservationEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.data.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation, ReservationResource, ReservationEntity> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }
}
