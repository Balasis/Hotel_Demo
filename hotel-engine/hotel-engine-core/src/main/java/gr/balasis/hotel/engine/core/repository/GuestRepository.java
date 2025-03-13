package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);

    List<Guest> findByFirstName(String firstName);

    List<Guest> findByLastName(String lastName);

    boolean existsByEmail(String email);
}
