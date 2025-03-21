package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("""
            SELECT g FROM Guest g
            WHERE ( LOWER(g.email) = LOWER(:email) OR :email IS NULL)
            AND (LOWER(g.firstName) = LOWER(:firstName) OR :firstName IS NULL)
            AND (LOWER(g.lastName) = LOWER(:lastName) OR :lastName IS NULL)
            AND (g.birthDate = :birthDate OR :birthDate IS NULL)
            """)
    List<Guest> searchBy(String email, String firstName, String lastName, LocalDate birthDate);

    @Query("""
            select case when count(g) > 0
                            then true
                            else false
                            end
            from Guest g where lower(g.email) = lower(:email)
            """)
    boolean existsByEmail(String email);
}
