package gr.balasis.hotel.data.repository;

import gr.balasis.hotel.data.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity,Long> {

    Optional<GuestEntity> findByEmail(String email);
    List<GuestEntity> findByFirstNameAndLastName(String firstName, String lastName);
    List<GuestEntity> findByFirstName(String firstName);
    List<GuestEntity> findByLastName(String lastName);

}
