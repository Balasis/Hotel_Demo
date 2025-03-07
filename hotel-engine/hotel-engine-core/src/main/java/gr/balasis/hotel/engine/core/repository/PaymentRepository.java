package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment getByReservation(Reservation reservation);
    void deleteByReservation(Reservation reservation);
}
