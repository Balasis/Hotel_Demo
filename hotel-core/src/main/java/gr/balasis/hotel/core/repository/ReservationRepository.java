package gr.balasis.hotel.core.repository;

import gr.balasis.hotel.core.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {
    List<ReservationEntity> findByGuestId(Long guestId);
}
