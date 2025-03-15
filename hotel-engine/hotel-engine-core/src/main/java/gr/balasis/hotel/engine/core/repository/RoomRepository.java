package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
    select r
    from Room r
    where r.id= :roomId
    """)
    Optional<Room> getRoomByIdCompleteFetch(Long roomId);
}
