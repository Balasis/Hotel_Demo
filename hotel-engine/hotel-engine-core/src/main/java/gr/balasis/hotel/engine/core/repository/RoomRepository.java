package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
        SELECT r FROM Room r
        WHERE (LOWER(r.roomNumber) = LOWER(:roomNumber) OR :roomNumber IS NULL)
        AND (r.pricePerNight = :pricePerNight OR :pricePerNight IS NULL)
        AND (LOWER(r.bedType) = LOWER(:bedType) OR :bedType IS NULL)
        AND (r.floor = :floor OR :floor IS NULL)
    """)
    List<Room> searchBy(String roomNumber, BigDecimal pricePerNight,String bedType,Integer floor);

    @Query("""
       select case when COUNT(r) > 0 then true else false end
       from Room r
       where LOWER(r.roomNumber) = LOWER(:roomNumber)
       """)
    boolean existsByRoomNumber(String roomNumber);

    @Query("""
    select r
    from Room r
    where r.id= :roomId
    """)
    Optional<Room> getRoomByIdCompleteFetch(Long roomId);

}
