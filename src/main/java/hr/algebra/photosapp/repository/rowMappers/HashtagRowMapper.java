package hr.algebra.photosapp.repository.rowMappers;

import hr.algebra.photosapp.domain.Hashtag;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HashtagRowMapper implements RowMapper<Hashtag> {
    @Override
    public Hashtag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hashtag hashtag = new Hashtag();
        hashtag.setId(rs.getLong("id"));
        // Set other properties of the hashtag

        return hashtag;
    }
}
