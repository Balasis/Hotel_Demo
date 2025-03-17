package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.exception.notfound.RoomNotFoundException;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.engine.core.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends BasicServiceImpl<Room, RoomNotFoundException> implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<Room> searchBy(String roomNumber, BigDecimal pricePerNight) {
        return roomRepository.searchBy(roomNumber, pricePerNight);
    }

    @Override
    public JpaRepository<Room, Long> getRepository() {
        return roomRepository;
    }

    @Override
    public Class<RoomNotFoundException> getNotFoundExceptionClass() {
        return RoomNotFoundException.class;
    }

    @Override
    public String getModelName() {
        return "Room";
    }

}
