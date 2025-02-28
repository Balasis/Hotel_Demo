package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.controller.BaseController;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController extends BaseController<Reservation, ReservationResource, ReservationEntity> {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public ResponseEntity<List<ReservationResource>> findAll() {
        List<ReservationResource> resources = reservationService.findAll()
                .stream()
                .map(reservationMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @Override
    protected BaseService<Reservation, Long> getBaseService() {
        return reservationService;
    }

    @Override
    protected BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }
}
