package gr.balasis.hotel.core.service;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.core.entity.GuestEntity;
import gr.balasis.hotel.core.mapper.entitydomain.EDbaseMapper;
import gr.balasis.hotel.core.mapper.entitydomain.EDguestMapper;
import gr.balasis.hotel.core.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest,GuestEntity> implements GuestService{
    private final GuestRepository guestRepository;
    private final EDguestMapper eDguestMapper;

    @Override
    public JpaRepository<GuestEntity,Long> getRepository() {
        return guestRepository;
    }

    @Override
    public EDbaseMapper<GuestEntity,Guest> getMapper() {
        return eDguestMapper;
    }
}
