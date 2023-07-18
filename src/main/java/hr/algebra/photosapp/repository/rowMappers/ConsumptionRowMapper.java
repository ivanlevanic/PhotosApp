package hr.algebra.photosapp.repository.rowMappers;

import hr.algebra.photosapp.domain.Consumption;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsumptionRowMapper implements RowMapper<Consumption> {

    @Override
    public Consumption mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Consumption consumption = new Consumption();
        consumption.setId(resultSet.getLong("id"));
        consumption.setProfileId(resultSet.getLong("profileId"));
        consumption.setDayOfTheMonth(resultSet.getDate("dayOfTheMonth").toLocalDate());
        consumption.setNumberOfUploadedPhotos(resultSet.getInt("numberOfUploadedPhotos"));
        return consumption;
    }
}

