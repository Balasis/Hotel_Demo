package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.exception.dublicate.DuplicateRoomException;
import gr.balasis.hotel.context.base.exception.notfound.RoomNotFoundException;
import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.engine.core.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoomValidatorImpl implements RoomValidator {
    private final RoomRepository roomRepository;

    @Override
    public Room validate(Room room) {
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new DuplicateRoomException("Room with number " + room.getRoomNumber() + " already exists");
        }
        return room;
    }

    @Override
    public Room validateForUpdate(Long id, Room room) {
        if (!roomRepository.existsById(room.getId())) {
            throw new RoomNotFoundException("Room with id " + room.getId() + " does not exist");
        }
        return room;
    }
}
