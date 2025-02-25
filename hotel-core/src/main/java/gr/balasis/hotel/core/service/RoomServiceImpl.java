package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.core.entity.RoomEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.RoomMapper;
import gr.balasis.hotel.core.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends BasicServiceImpl<Room, RoomResource, RoomEntity> implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public JpaRepository<RoomEntity, Long> getRepository() {
        return roomRepository;
    }

    @Override
    public BaseMapper<Room, RoomResource, RoomEntity> getMapper() {
        return roomMapper;
    }
}
