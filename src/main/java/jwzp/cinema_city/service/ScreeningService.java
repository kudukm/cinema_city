package jwzp.cinema_city.service;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    public Screening findScreeningById(String id) {
        Optional<Screening> optionalScreening = screeningRepository.findById(id);
        return optionalScreening.orElse(null);
    }

    public void updateSeatsForReservation(Reservation reservation) {
        Optional<Screening> screeningOptional = screeningRepository.findById(reservation.getScreening().getId());
        if (screeningOptional.isPresent()) {
            Screening screening = screeningOptional.get();
            Boolean[] seats = screening.getSeats();
            for (int seat : reservation.getSeats()) {
                seats[seat] = true;
            }
            screening.setSeats(seats);
            screeningRepository.save(screening);
        } else {
            throw new IllegalArgumentException("Screening not found for ID: " + reservation.getScreening());
        }
    }
}