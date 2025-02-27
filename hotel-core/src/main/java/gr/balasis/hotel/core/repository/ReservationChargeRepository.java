package gr.balasis.hotel.core.repository;

import gr.balasis.hotel.core.entity.ReservationChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationChargeRepository extends JpaRepository<ReservationChargeEntity,Long> {
    Optional<ReservationChargeEntity> findByReservationId(Long reservationId);
}
