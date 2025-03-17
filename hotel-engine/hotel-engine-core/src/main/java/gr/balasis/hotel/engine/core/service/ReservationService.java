package gr.balasis.hotel.engine.core.service;


import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.engine.core.transfer.KeyValue;
import gr.balasis.hotel.engine.core.transfer.ReservationGuestStatisticsDTO;
import gr.balasis.hotel.engine.core.transfer.ReservationRoomStatisticsDTO;

import java.util.List;

public interface ReservationService extends BaseService<Reservation,Long> {
    List<Reservation> findAllHotelReservations();
    List<Reservation> findByGuestId(Long id);
    void manageReservationAction(Long reservationId, String action);
    Payment getPayment(Long reservationId);
    Feedback createFeedback(Long reservationId, Feedback domain);
    Feedback getFeedback(Long reservationId);
    void updateFeedback(Long reservationId, Feedback domain);
    void deleteFeedback(Long reservationId);

    KeyValue<String,Float> getAvgPercentageRateOfFeedback();
    List<ReservationRoomStatisticsDTO> findRoomStatistics();
    List<ReservationGuestStatisticsDTO> findGuestStatistics();
}
