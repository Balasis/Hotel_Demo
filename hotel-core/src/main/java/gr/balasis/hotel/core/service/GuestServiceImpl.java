package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.data.entity.GuestEntity;
import gr.balasis.hotel.data.entity.ReservationEntity;
import exception.EntityNotFoundException;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.GuestMapper;
import gr.balasis.hotel.context.base.mapper.ReservationMapper;
import gr.balasis.hotel.data.repository.GuestRepository;
import gr.balasis.hotel.data.repository.ReservationRepository;
import gr.balasis.hotel.data.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest, GuestResource,GuestEntity> implements GuestService{
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;
    private final GuestMapper guestMapper;
    private final ReservationMapper reservationMapper;
    private final RoomRepository roomRepository;

    @Override
    public List<Reservation> findReservationsByGuestId(Long id) {
        GuestEntity guestEntity =
                guestRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Guest entity not found"));

        return reservationRepository.findByGuest(guestEntity).stream()
                .map(reservationMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation createReservationForGuest(Long id, Reservation reservation) {

        guestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        roomRepository.findById(reservation.getRoom().getId()).orElseThrow(() -> new EntityNotFoundException("Room not found"));

        ReservationEntity SavedReservationEntity = reservationRepository.save(reservationMapper.toEntity(reservation));

        return reservationMapper.toDomainFromEntity(SavedReservationEntity);
    }

    @Override
    public void cancelReservation(Long id, Long reservationId) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!reservationEntity.getGuest().getId().equals(id)) {
            throw new IllegalArgumentException("Reservation does not belong to the guest");
        }

        reservationRepository.delete(reservationEntity);
    }

    @Override
    public void deleteById(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new EntityNotFoundException("Guest not found");
        }
        guestRepository.deleteById(id);
    }

    @Override
    public Guest updateEmail(Long id, String email) {
        GuestEntity guestEntity = guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        guestEntity.setEmail(email);
        return guestMapper.toDomainFromEntity(guestRepository.save(guestEntity));
    }

//    public Guest findByEmail(String email) {
//        return guestMapper.toDomainFromEntity( guestRepository.findByEmail(email) )   ;
//    }
//
//    public List<Guest> findByFirstNameAndLastName(String firstName, String lastName) {
//        return guestRepository.findByFirstNameAndLastName(firstName, lastName).stream()
//                .map(guestMapper::toDomainFromEntity)
//                .collect(Collectors.toList());
//    }
//
//    public List<Guest> findByFirstName(String firstName) {
//        return guestRepository.findByFirstName(firstName).stream()
//                .map(guestMapper::toDomainFromEntity)
//                .collect(Collectors.toList());
//    }
//
//    public List<Guest> findByLastName(String lastName) {
//        return guestRepository.findByLastName(lastName).stream()
//                .map(guestMapper::toDomainFromEntity)
//                .collect(Collectors.toList());
//    }
//
//    public boolean existsById(Long guestId) {
//        return guestRepository.existsById(guestId);
//    }



    @Override
    public JpaRepository<GuestEntity,Long> getRepository() {
        return guestRepository;
    }

    @Override
    public BaseMapper<Guest, GuestResource,GuestEntity> getMapper() {
        return guestMapper;
    }
}
