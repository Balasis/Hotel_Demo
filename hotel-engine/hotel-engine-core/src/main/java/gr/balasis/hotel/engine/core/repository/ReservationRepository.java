package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuest(Guest guest);

    List<Reservation> findByGuestId(Long guestId);
}
