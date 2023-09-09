package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.LoggingSystem;
import hr.algebra.photosapp.repository.LoggingSystemRepository;
import hr.algebra.photosapp.repository.rowMappers.LoggingSystemRowMapper;
import hr.algebra.photosapp.repository.rowMappers.PhotoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcLoggingSystemRepository implements LoggingSystemRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcLoggingSystemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String username, String action, LocalDateTime time) {
        String sql = "INSERT INTO logging_system (user, action, time) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                username,
                action,
                time);
    }

    public List<LoggingSystem> getAllActivity() {
        String query = "SELECT * FROM logging_system";
        return jdbcTemplate.query(query, new LoggingSystemRowMapper());
    }

}
