package jwzp.cinema_city.repository;

import jwzp.cinema_city.models.Screening;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends MongoRepository<Screening, String> {
}
