package jwzp.cinema_city.repository;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByUser(UserEntity user);
}
