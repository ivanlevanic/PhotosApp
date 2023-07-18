package hr.algebra.photosapp.repository.rowMappers;

import hr.algebra.photosapp.domain.Profile;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileRowMapper implements RowMapper<Profile> {

    @Override
    public Profile mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Profile profile = new Profile();
        profile.setId(resultSet.getLong("id"));
        profile.setUsername(resultSet.getString("username"));
        return profile;
    }
}
