package gr.balasis.hotel.core.repository;

import gr.balasis.hotel.core.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity,Long> {

}
