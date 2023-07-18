package hr.algebra.photosapp.repository.jdbc;
import hr.algebra.photosapp.domain.Hashtag;
import hr.algebra.photosapp.repository.HashtagRepository;
import hr.algebra.photosapp.repository.rowMappers.HashtagRowMapper;
import hr.algebra.photosapp.repository.rowMappers.PhotoRowMapper;
import hr.algebra.photosapp.domain.Photo;
import hr.algebra.photosapp.repository.PhotoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcPhotoRepository implements PhotoRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcPhotoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Photo photo) {
        String sql = "INSERT INTO photo (profileId, photo, description, uploadTime, size, hashtags) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                photo.getProfileId(),
                photo.getPhoto(),
                photo.getDescription(),
                photo.getUploadTime(),
                photo.getSize(),
                photo.getHashtags());
    }

    public List<Photo> getAllPhotos() {
        String query = "SELECT * FROM photo";
        return jdbcTemplate.query(query, new PhotoRowMapper());
    }

    public Photo getPhotoByName(String fileName) {
        String sql = "SELECT * FROM photo WHERE photo = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{fileName}, new PhotoRowMapper());
    }

    public Photo getPhotoById(Long id) {
        String sql = "SELECT * FROM photo WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PhotoRowMapper());
    }

    public List<Photo> getAllPhotosByProfileId(Long profileId) {
        String query = "SELECT * FROM photo WHERE profileId = ?";
        return jdbcTemplate.query(query, new Object[]{profileId}, new PhotoRowMapper());
    }

    public List<Photo> getPhotosByDescription(String description) {
        String query = "SELECT * FROM photo WHERE description LIKE ?";
        String searchPattern = "%" + description + "%";
        return jdbcTemplate.query(query, new Object[]{searchPattern}, new PhotoRowMapper());
    }

    public void updatePhoto(Long photoId, String description, String hashtags) {
        String query = "UPDATE photo SET description = ?, hashtags = ? WHERE id = ?";
        jdbcTemplate.update(query, description, hashtags, photoId);
    }
}
