package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.Consumption;
import hr.algebra.photosapp.repository.ConsumptionRepository;
import hr.algebra.photosapp.repository.rowMappers.ConsumptionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcConsumptionRepository implements ConsumptionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcConsumptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Consumption findByProfileId(Long profileId) {
        String sql = "SELECT * FROM consumption WHERE profileId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{profileId}, (resultSet, rowNum) -> {
            Consumption consumption = new Consumption();
            consumption.setId(resultSet.getLong("id"));
            consumption.setProfileId(resultSet.getLong("profileId"));
            consumption.setDayOfTheMonth(resultSet.getDate("dayOfTheMonth").toLocalDate());
            consumption.setNumberOfUploadedPhotos(resultSet.getInt("numberOfUploadedPhotos"));
            return consumption;
        });
    }

    public Consumption getConsumptionByProfileId(Long profileId) {
        String query = "SELECT * FROM consumption WHERE profileId = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{profileId}, new ConsumptionRowMapper());
    }
}
