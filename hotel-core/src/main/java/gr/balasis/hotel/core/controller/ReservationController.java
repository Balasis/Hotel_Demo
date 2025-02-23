package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.mapper.resourcedomain.RDbaseMapper;
import gr.balasis.hotel.core.mapper.resourcedomain.RDreservationMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController extends BaseController<Reservation, ReservationResource> {
    private final ReservationService roomService;
    private final RDreservationMapper rDroomMapper;

    @Override
    protected BaseService<Reservation, Long> getBaseService() {
        return roomService;
    }

    @Override
    protected RDbaseMapper<ReservationResource, Reservation> getMapper() {
        return rDroomMapper;
    }
}
