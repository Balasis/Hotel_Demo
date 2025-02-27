package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.ReservationCharge;

public interface ReservationChargeService extends BaseService<ReservationCharge,Long> {
    ReservationCharge getChargeByReservationId(Long reservationId);
    ReservationCharge createCharge(Long reservationId, ReservationCharge reservationCharge);
}
