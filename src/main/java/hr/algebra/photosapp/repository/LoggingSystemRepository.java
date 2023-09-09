package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.LoggingSystem;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoggingSystemRepository {
    public void save(String username, String action, LocalDateTime time);
    public List<LoggingSystem> getAllActivity();
}
