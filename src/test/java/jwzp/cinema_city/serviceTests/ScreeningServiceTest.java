package jwzp.cinema_city.serviceTests;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.repository.ScreeningRepository;
import jwzp.cinema_city.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private ScreeningService screeningService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddScreening() {
        Screening screening = new Screening();
        when(screeningRepository.save(screening)).thenReturn(screening);

        Screening result = screeningService.addScreening(screening);

        assertNotNull(result);
        verify(screeningRepository, times(1)).save(screening);
    }

    @Test
    public void testFindScreeningsByDate() {
        LocalDate date = LocalDate.of(2024, 6, 21);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Screening screening1 = new Screening();
        screening1.setScreeningTime(startOfDay.plusHours(1));

        Screening screening2 = new Screening();
        screening2.setScreeningTime(endOfDay.minusHours(1));

        when(screeningRepository.findByScreeningTimeBetween(startOfDay, endOfDay))
                .thenReturn(List.of(screening1, screening2));

        List<Screening> result = screeningService.findScreeningsByDate(date);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(screeningRepository, times(1)).findByScreeningTimeBetween(startOfDay, endOfDay);
    }

    @Test
    public void testFindScreeningById_ScreeningExists() {
        Screening screening = new Screening();
        screening.setId("testId");
        when(screeningRepository.findById("testId")).thenReturn(Optional.of(screening));

        Screening result = screeningService.findScreeningById("testId");

        assertNotNull(result);
        assertEquals("testId", result.getId());
    }

    @Test
    public void testFindScreeningById_ScreeningNotFound() {
        when(screeningRepository.findById("testId")).thenReturn(Optional.empty());

        Screening result = screeningService.findScreeningById("testId");

        assertNull(result);
    }

    @Test
    public void testUpdateSeatsForReservation_ScreeningExists() {
        Screening screening = new Screening();
        screening.setId("testScreeningId");
        Boolean[] seats = new Boolean[100];
        screening.setSeats(seats);

        Reservation reservation = new Reservation();
        reservation.setScreening(screening);
        reservation.setSeats(new int[]{1, 2, 3});

        when(screeningRepository.findById("testScreeningId")).thenReturn(Optional.of(screening));

        screeningService.updateSeatsForReservation(reservation);

        assertTrue(screening.getSeats()[1]);
        assertTrue(screening.getSeats()[2]);
        assertTrue(screening.getSeats()[3]);
        verify(screeningRepository, times(1)).save(screening);
    }

    @Test
    public void testUpdateSeatsForReservation_ScreeningNotFound() {
        Screening screening = new Screening();
        screening.setId("testScreeningId");

        Reservation reservation = new Reservation();
        reservation.setScreening(screening);

        when(screeningRepository.findById("testScreeningId")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            screeningService.updateSeatsForReservation(reservation);
        });

        String expectedMessage = "Screening not found for ID: " + reservation.getScreening();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
