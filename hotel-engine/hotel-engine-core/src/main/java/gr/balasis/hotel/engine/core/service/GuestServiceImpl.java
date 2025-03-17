package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.exception.notfound.GuestNotFoundException;
import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.engine.core.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl extends BasicServiceImpl<Guest,GuestNotFoundException> implements GuestService {
    private final GuestRepository guestRepository;

    @Override
    public List<Guest> searchBy(String email, String firstName, String lastName, LocalDate birthDate) {
        return guestRepository.searchBy(email, firstName, lastName, birthDate);
    }

    @Override
    public JpaRepository<Guest, Long> getRepository() {
        return guestRepository;
    }

    @Override
    public Class<GuestNotFoundException> getNotFoundExceptionClass() {
        return GuestNotFoundException.class;
    }

    @Override
    public String getModelName() {
        return "Guest";
    }
}
