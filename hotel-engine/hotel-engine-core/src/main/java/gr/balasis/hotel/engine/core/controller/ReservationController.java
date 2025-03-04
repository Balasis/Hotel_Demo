package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.service.BaseService;

import gr.balasis.hotel.context.web.mapper.BaseWebMapper;
import gr.balasis.hotel.engine.core.mapper.web.ReservationWebMapper;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.resource.ReservationResource;


import gr.balasis.hotel.engine.core.service.ReservationService;

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
public class ReservationController extends BaseController<Reservation, ReservationResource> {
    private final ReservationService reservationService;
    private final ReservationWebMapper reservationMapper;

    @GetMapping
    public ResponseEntity<List<ReservationResource>> findAll() {
        List<ReservationResource> resources = reservationService.findAll()
                .stream()
                .map(reservationMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @Override
    protected BaseService<Reservation> getBaseService() {
        return reservationService;
    }

    @Override
    protected BaseWebMapper<Reservation, ReservationResource> getMapper() {
        return reservationMapper;
    }
}
