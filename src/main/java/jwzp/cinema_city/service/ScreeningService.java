package jwzp.cinema_city.service;


import jwzp.cinema_city.models.Screening;
import jwzp.cinema_city.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreeningService {
    @Autowired
    private ScreeningRepository ScreeningRepository;

    public Screening registerScreening(Screening Screening) {
        return ScreeningRepository.save(Screening);
    }
}