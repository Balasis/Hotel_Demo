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
import org.springframework.web.bind.annotation.*;

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
                .map(guestMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GuestResource> findByEmail(
            @PathVariable String email) {
        Guest guest = guestService.findByEmail(email);
        return ResponseEntity.ok(guestMapper.toResource(guest));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GuestResource>> searchGuests(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        if (firstName == null && lastName == null) {
            throw new IllegalArgumentException("At least one of 'firstName' or 'lastName' must be provided");
        }

        List<Guest> guests;
        if (firstName != null && lastName != null) {
            guests = guestService.findByFirstNameAndLastName(firstName, lastName);
        } else if (firstName != null) {
            guests = guestService.findByFirstName(firstName);
        } else {
            guests = guestService.findByLastName(lastName);
        }

        return ResponseEntity.ok(guests.stream()
                .map(guestMapper::toResource)
                .collect(Collectors.toList()));
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
