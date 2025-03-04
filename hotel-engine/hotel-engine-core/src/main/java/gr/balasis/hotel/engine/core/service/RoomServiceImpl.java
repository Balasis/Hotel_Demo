package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.context.base.entity.RoomEntity;
import gr.balasis.hotel.engine.core.mapper.base.RoomMapper;
import gr.balasis.hotel.engine.core.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends BasicServiceImpl<Room,RoomEntity> implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public JpaRepository<RoomEntity, Long> getRepository() {
        return roomRepository;
    }

    @Override
    public BaseMapper<Room,RoomEntity> getMapper() {
        return roomMapper;
    }
}
