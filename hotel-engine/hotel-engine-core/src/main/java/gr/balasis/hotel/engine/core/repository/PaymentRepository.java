package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.entity.PaymentEntity;
import gr.balasis.hotel.context.base.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    PaymentEntity getByReservation(ReservationEntity reservation);
    void deleteByReservation(ReservationEntity reservation);
}
