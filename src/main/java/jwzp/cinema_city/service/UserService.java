package jwzp.cinema_city.service;

import jwzp.cinema_city.models.Reservation;
import jwzp.cinema_city.models.UserEntity;
import jwzp.cinema_city.repository.ReservationRepository;
import jwzp.cinema_city.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(UserEntity user) {
        if (user.getAuthorities().isEmpty()) {
            return new String[]{"USER"};
        }
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    public UserEntity getUserByID(String id){
        return userRepository.findById(id).orElse(null);
    }

    public List<Reservation> getMyReservations(UserEntity user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getPastReservations(UserEntity user, LocalDateTime currentTime) {
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return reservations.stream()
                .filter(reservation -> reservation.getScreening().getScreeningTime().isBefore(currentTime))
                .collect(Collectors.toList());
    }

    public List<Reservation> getFutureReservations(UserEntity user, LocalDateTime currentTime) {
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return reservations.stream()
                .filter(reservation -> reservation.getScreening().getScreeningTime().isAfter(currentTime))
                .collect(Collectors.toList());
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
