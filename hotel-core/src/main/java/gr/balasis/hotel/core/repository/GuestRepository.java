package gr.balasis.hotel.core.repository;

import gr.balasis.hotel.core.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity,Long> {
    GuestEntity findByEmail(String email);
    List<GuestEntity> findByFirstNameAndLastName(String firstName, String lastName);
    List<GuestEntity> findByFirstName(String firstName);
    List<GuestEntity> findByLastName(String lastName);

}
