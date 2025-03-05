package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.entity.GuestEntity;
import gr.balasis.hotel.context.base.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByGuest(GuestEntity guest);

    List<ReservationEntity> findByGuestId(Long guestId);
}
