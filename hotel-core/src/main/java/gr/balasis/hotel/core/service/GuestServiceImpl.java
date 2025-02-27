package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.core.entity.GuestEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.GuestMapper;
import gr.balasis.hotel.core.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest, GuestResource,GuestEntity> implements GuestService{
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public Guest findByEmail(String email) {
        return guestMapper.toDomainFromEntity( guestRepository.findByEmail(email) )   ;
    }

    public List<Guest> findByFirstNameAndLastName(String firstName, String lastName) {
        return guestRepository.findByFirstNameAndLastName(firstName, lastName).stream()
                .map(guestMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    public List<Guest> findByFirstName(String firstName) {
        return guestRepository.findByFirstName(firstName).stream()
                .map(guestMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    public List<Guest> findByLastName(String lastName) {
        return guestRepository.findByLastName(lastName).stream()
                .map(guestMapper::toDomainFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public JpaRepository<GuestEntity,Long> getRepository() {
        return guestRepository;
    }

    @Override
    public BaseMapper<Guest, GuestResource,GuestEntity> getMapper() {
        return guestMapper;
    }
}
