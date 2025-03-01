package gr.balasis.hotel.core.repository;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.core.entity.GuestEntity;
import gr.balasis.hotel.core.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {
    List<ReservationEntity> findByGuest(GuestEntity guest);

    List<ReservationEntity> findByGuestId(Long guestId);
}
