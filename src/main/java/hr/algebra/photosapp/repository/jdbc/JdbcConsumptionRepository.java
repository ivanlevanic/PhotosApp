package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.Consumption;
import hr.algebra.photosapp.repository.ConsumptionRepository;
import hr.algebra.photosapp.repository.rowMappers.ConsumptionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class JdbcConsumptionRepository implements ConsumptionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcConsumptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public Consumption findByProfileId(Long profileId) {
//        String sql = "SELECT * FROM consumption WHERE profileId = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{profileId}, (resultSet, rowNum) -> {
//            Consumption consumption = new Consumption();
//            consumption.setId(resultSet.getLong("id"));
//            consumption.setProfileId(resultSet.getLong("profileId"));
//            consumption.setDayOfTheMonth(resultSet.getDate("dayOfTheMonth").toLocalDate());
//            consumption.setNumberOfUploadedPhotos(resultSet.getInt("numberOfUploadedPhotos"));
//            return consumption;
//        });
//    }

    public Consumption getConsumptionByProfileId(Long profileId, LocalDate today) {
        java.sql.Date sqlDate = java.sql.Date.valueOf(today);
        String query = "SELECT * FROM consumption WHERE profileId = ? AND dayOfTheMonth = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{profileId, sqlDate}, new ConsumptionRowMapper());
    }

    public void updateNumberOfUploadedPhotos (long consumptionId, int numberOfUploadedPhotos) {
        String query = "UPDATE consumption SET numberOfUploadedPhotos = ? WHERE id = ?";
        jdbcTemplate.update(query, numberOfUploadedPhotos, consumptionId);
    }

    public void save(Consumption consumption) {
        String sql = "INSERT INTO consumption (profileId, dayOfTheMonth, numberOfUploadedPhotos) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                consumption.getProfileId(),
                consumption.getDayOfTheMonth(),
                consumption.getNumberOfUploadedPhotos());
    }
}
