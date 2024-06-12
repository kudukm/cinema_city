package jwzp.cinema_city.repository;

import jwzp.cinema_city.models.Screening;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends MongoRepository<Screening, String> {
    List<Screening> findByScreeningTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
