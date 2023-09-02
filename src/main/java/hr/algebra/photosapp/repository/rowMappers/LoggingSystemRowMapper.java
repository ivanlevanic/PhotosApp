package hr.algebra.photosapp.repository.rowMappers;

import hr.algebra.photosapp.domain.LoggingSystem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoggingSystemRowMapper implements RowMapper<LoggingSystem> {
    @Override
    public LoggingSystem mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoggingSystem loggingSystem = new LoggingSystem();
        loggingSystem.setId(rs.getLong("id"));
        loggingSystem.setUser(rs.getString("user"));
        loggingSystem.setAction(rs.getString("action"));
        loggingSystem.setTime(rs.getTimestamp("time").toLocalDateTime());
        return loggingSystem;
    }
}
