package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.engine.core.mapper.ReservationMapper;
import gr.balasis.hotel.engine.core.service.ReservationService;

import gr.balasis.hotel.engine.core.transfer.KeyValue;
import gr.balasis.hotel.engine.core.transfer.ReservationGuestStatisticsDTO;
import gr.balasis.hotel.engine.core.transfer.ReservationRoomStatisticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController extends BaseController<Reservation, ReservationResource> {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public ResponseEntity<List<ReservationResource>> findAll() {
        return ResponseEntity.ok(reservationMapper.toResources(reservationService.findAll()) );
    }

    @GetMapping(headers = "action=reservationRoomStatisticsDTO")
    public ResponseEntity<List<ReservationRoomStatisticsDTO>> findReservationsRoomStatistics(){
        return ResponseEntity.ok(reservationService.findRoomStatistics());
    }

    @GetMapping(headers = "action=reservationGuestStatisticsDTO")
    public ResponseEntity<List<ReservationGuestStatisticsDTO>> findReservationsGuestStatistics(){
        return ResponseEntity.ok(reservationService.findGuestStatistics());
    }

    @GetMapping(headers = "action=avgPercentageRateOfFeedback")
    public ResponseEntity<KeyValue<String,Float>> getAvgPercentageRateOfFeedback(){
        return ResponseEntity.ok(reservationService.getAvgPercentageRateOfFeedback() );
    }

    @Override
    protected BaseService<Reservation,Long> getBaseService() {
        return reservationService;
    }

    @Override
    protected BaseMapper<Reservation, ReservationResource> getMapper() {
        return reservationMapper;
    }
}
