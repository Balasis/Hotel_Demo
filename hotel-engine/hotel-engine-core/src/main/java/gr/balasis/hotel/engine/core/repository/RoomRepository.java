package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
