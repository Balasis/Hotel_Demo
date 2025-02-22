package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.core.entity.RoomEntity;
import gr.balasis.hotel.core.mapper.entitydomain.EDbaseMapper;
import gr.balasis.hotel.core.mapper.entitydomain.EDroomMapper;
import gr.balasis.hotel.core.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends BasicServiceImpl<Room, RoomEntity> implements RoomService {
    private final RoomRepository roomRepository;
    private final EDroomMapper eDroomMapper;

    @Override
    public JpaRepository<RoomEntity, Long> getRepository() {
        return roomRepository;
    }

    @Override
    public EDbaseMapper<RoomEntity, Room> getMapper() {
        return eDroomMapper;
    }
}
