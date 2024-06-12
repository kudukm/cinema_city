package jwzp.cinema_city.service;


import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScreeningService {
    @Autowired
    private ScreeningRepository screeningRepository;

    public Screening addScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    public List<Screening> findScreeningsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return screeningRepository.findByScreeningTimeBetween(startOfDay, endOfDay);
    }
}