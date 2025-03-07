package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.engine.core.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends BasicServiceImpl<Room> implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public JpaRepository<Room, Long> getRepository() {
        return roomRepository;
    }

}
