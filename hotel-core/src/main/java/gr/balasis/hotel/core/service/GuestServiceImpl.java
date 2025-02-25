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


@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest, GuestResource,GuestEntity> implements GuestService{
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;


    @Override
    public JpaRepository<GuestEntity,Long> getRepository() {
        return guestRepository;
    }

    @Override
    public BaseMapper<Guest, GuestResource,GuestEntity> getMapper() {
        return guestMapper;
    }
}
