package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController extends BaseController<Reservation, ReservationResource, ReservationEntity> {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @Override
    protected BaseService<Reservation, Long> getBaseService() {
        return reservationService;
    }

    @Override
    protected BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }
}
