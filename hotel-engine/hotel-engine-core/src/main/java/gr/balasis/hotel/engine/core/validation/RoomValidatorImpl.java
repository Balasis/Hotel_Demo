package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.exception.dublicate.DuplicateRoomException;
import gr.balasis.hotel.context.base.exception.notfound.RoomNotFoundException;
import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.engine.core.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoomValidatorImpl implements RoomValidator{
    private final RoomRepository roomRepository;

    @Override
    public Room validate(Room room) {
        boolean exists = roomRepository.existsByRoomNumber(room.getRoomNumber());
      if (exists) {
          System.out.println("got in here");
          throw new DuplicateRoomException("Room with number " + room.getRoomNumber() + " already exists");
      }
      return room;
    }

    @Override
    public Room validateForUpdate(Room room) {
        if(!roomRepository.existsById(room.getId())) {
            throw new RoomNotFoundException("Room with id " + room.getId() + " does not exist");
        }
        return room;
    }
}
