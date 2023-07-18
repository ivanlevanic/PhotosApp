package hr.algebra.photosapp.repository.rowMappers;
import hr.algebra.photosapp.domain.Hashtag;
import hr.algebra.photosapp.domain.Photo;
import hr.algebra.photosapp.repository.PhotoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PhotoRowMapper implements RowMapper<Photo> {
    @Override
    public Photo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Photo photo = new Photo();
        photo.setId(resultSet.getLong("id"));
        photo.setProfileId(resultSet.getLong("profileId"));
        photo.setPhoto(resultSet.getString("photo"));
//        photo.setPhoto(resultSet.getBytes("photo"));
        photo.setDescription(resultSet.getString("description"));
        photo.setUploadTime(resultSet.getTimestamp("uploadTime").toLocalDateTime());
        photo.setSize(resultSet.getLong("size"));
        photo.setHashtags(resultSet.getString("hashtags"));

        //List<Hashtag> hashtags = photoRepository.getHashtagsForPhoto(photo.getId());
        //photo.setHashtags(hashtags);

        return photo;
    }

}
