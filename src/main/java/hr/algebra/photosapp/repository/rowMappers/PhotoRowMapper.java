package hr.algebra.photosapp.repository.rowMappers;
import hr.algebra.photosapp.domain.Photo;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoRowMapper implements RowMapper<Photo> {
    @Override
    public Photo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Photo photo = new Photo();
        photo.setId(resultSet.getLong("id"));
        photo.setProfileId(resultSet.getLong("profileId"));
        photo.setPhoto(resultSet.getString("photo"));
        photo.setDescription(resultSet.getString("description"));
        photo.setUploadTime(resultSet.getTimestamp("uploadTime").toLocalDateTime());
        photo.setSize(resultSet.getLong("size"));
        photo.setUnparsedHashtags(resultSet.getString("hashtags"));

        return photo;
    }

}
