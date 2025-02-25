package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.core.entity.GuestEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.GuestMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseController<Guest, GuestResource, GuestEntity>{
    private final GuestService guestService;
    private final GuestMapper guestMapper;

    @GetMapping
    public ResponseEntity<List<GuestResource>> findAll() {
        List<GuestResource> resources = guestService.findAll()
                .stream()
                .map(getMapper()::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @Override
    public BaseService<Guest, Long> getBaseService() {
        return guestService;
    }

    @Override
    public BaseMapper<Guest, GuestResource, GuestEntity> getMapper() {
        return guestMapper;
    }
}
