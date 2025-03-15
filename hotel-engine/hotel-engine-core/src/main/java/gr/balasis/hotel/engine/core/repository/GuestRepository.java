package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    boolean existsByEmail(String email);
}
