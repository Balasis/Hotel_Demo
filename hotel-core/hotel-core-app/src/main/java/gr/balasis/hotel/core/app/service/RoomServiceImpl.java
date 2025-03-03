package gr.balasis.hotel.core.app.service;

import gr.balasis.hotel.context.base.domain.domains.Room;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.RoomMapper;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.data.entity.RoomEntity;
import gr.balasis.hotel.data.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends BasicServiceImpl<Room, RoomResource, RoomEntity> implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;


    public boolean existsById(Long roomId) {
        return roomRepository.existsById(roomId);
    }

    @Override
    public JpaRepository<RoomEntity, Long> getRepository() {
        return roomRepository;
    }

    @Override
    public BaseMapper<Room, RoomResource, RoomEntity> getMapper() {
        return roomMapper;
    }
}
