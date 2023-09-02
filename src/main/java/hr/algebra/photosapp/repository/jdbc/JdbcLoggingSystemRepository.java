package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.LoggingSystem;
import hr.algebra.photosapp.repository.LoggingSystemRepository;
import hr.algebra.photosapp.repository.rowMappers.LoggingSystemRowMapper;
import hr.algebra.photosapp.repository.rowMappers.PhotoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcLoggingSystemRepository implements LoggingSystemRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcLoggingSystemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(LoggingSystem loggingSystem) {
        String sql = "INSERT INTO logging_system (user, action, time) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                loggingSystem.getUser(),
                loggingSystem.getAction(),
                loggingSystem.getTime());
    }

    public List<LoggingSystem> getAllActivity() {
        String query = "SELECT * FROM logging_system";
        return jdbcTemplate.query(query, new LoggingSystemRowMapper());
    }

}
