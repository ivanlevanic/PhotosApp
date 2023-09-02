package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.LoggingSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggingSystemRepository {
    public void save(LoggingSystem loggingSystem);
    public List<LoggingSystem> getAllActivity();
}
