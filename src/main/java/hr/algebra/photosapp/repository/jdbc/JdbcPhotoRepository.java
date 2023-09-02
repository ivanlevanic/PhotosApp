package hr.algebra.photosapp.repository.jdbc;
import hr.algebra.photosapp.domain.Profile;
import hr.algebra.photosapp.repository.rowMappers.PhotoRowMapper;
import hr.algebra.photosapp.domain.Photo;
import hr.algebra.photosapp.repository.PhotoRepository;
import hr.algebra.photosapp.repository.rowMappers.ProfileRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcPhotoRepository implements PhotoRepository {

    private JdbcTemplate jdbcTemplate;
    private PhotoRowMapper photoRowMapper;
    private ProfileRowMapper profileRowMapper;



    public JdbcPhotoRepository(JdbcTemplate jdbcTemplate,
                               PhotoRowMapper photoRowMapper,
                               ProfileRowMapper profileRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.photoRowMapper = photoRowMapper;
        this.profileRowMapper = profileRowMapper;
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

//    public List<Photo> getAllPhotos() {
//        String query = "SELECT * FROM photo";
//        return jdbcTemplate.query(query, new PhotoRowMapper());
//    }

//    public Photo getPhotoByName(String fileName) {
//        String sql = "SELECT * FROM photo WHERE photo = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{fileName}, new PhotoRowMapper());
//    }
//
//    public Photo getPhotoById(Long id) {
//        String sql = "SELECT * FROM photo WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PhotoRowMapper());
//    }

    public Photo getPhotoByName(String fileName) {
        String query = "SELECT p.*, pr.* " +
                "FROM photo p " +
                "JOIN profile pr ON p.profileId = pr.id " +
                "WHERE p.photo = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{fileName}, (rs, rowNum) -> {
            Photo photo = photoRowMapper.mapRow(rs, rowNum);
            Profile profile = profileRowMapper.mapRow(rs, rowNum);
            photo.setProfile(profile);
            return photo;
        });
    }

    public Photo getPhotoById(Long id) {
        String query = "SELECT p.*, pr.* " +
                "FROM photo p " +
                "JOIN profile pr ON p.profileId = pr.id " +
                "WHERE p.id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
            Photo photo = photoRowMapper.mapRow(rs, rowNum);
            Profile profile = profileRowMapper.mapRow(rs, rowNum);
            photo.setProfile(profile);
            return photo;
        });
    }
//    public List<Photo> getAllPhotosByProfileId(Long profileId) {
//        String query = "SELECT * FROM photo WHERE profileId = ?";
//        return jdbcTemplate.query(query, new Object[]{profileId}, new PhotoRowMapper());
//    }
//
//    public List<Photo> getPhotosByDescription(String description) {
//        String query = "SELECT * FROM photo WHERE description LIKE ?";
//        String searchPattern = "%" + description + "%";
//        return jdbcTemplate.query(query, new Object[]{searchPattern}, new PhotoRowMapper());
//    }

    public List<Photo> getAllPhotosByProfileId(Long profileId) {
        String query = "SELECT p.*, pr.* " +
                "FROM photo p " +
                "JOIN profile pr ON p.profileId = pr.id " +
                "WHERE p.profileId = ?";
        return jdbcTemplate.query(query, new Object[]{profileId}, rs -> {
            List<Photo> photos = new ArrayList<>();
            while (rs.next()) {
                Photo photo = photoRowMapper.mapRow(rs, rs.getRow());
                Profile profile = profileRowMapper.mapRow(rs, rs.getRow());
                photo.setProfile(profile);
                photos.add(photo);
            }
            return photos;
        });
    }

    public List<Photo> getPhotosByDescription(String description) {
        String query = "SELECT p.*, pr.* " +
                "FROM photo p " +
                "JOIN profile pr ON p.profileId = pr.id " +
                "WHERE p.description LIKE ?";
        String searchPattern = "%" + description + "%";
        return jdbcTemplate.query(query, new Object[]{searchPattern}, rs -> {
            List<Photo> photos = new ArrayList<>();
            while (rs.next()) {
                Photo photo = photoRowMapper.mapRow(rs, rs.getRow());
                Profile profile = profileRowMapper.mapRow(rs, rs.getRow());
                photo.setProfile(profile);
                photos.add(photo);
            }
            return photos;
        });
    }

    public void updatePhoto(Long photoId, String description, String hashtags) {
        String query = "UPDATE photo SET description = ?, hashtags = ? WHERE id = ?";
        jdbcTemplate.update(query, description, hashtags, photoId);
    }

//    public List<Photo> getAllPhotosWithUsername() {
//        String query = "SELECT p.*, pr.username " +
//                "FROM photo p " +
//                "JOIN profile pr ON p.profileId = pr.id";
//        return jdbcTemplate.query(query, (rs, rowNum) -> {
//            Photo photo = new Photo();
//            photo.setId(rs.getLong("id"));
//            photo.setProfileId(rs.getLong("profileId"));
//            photo.setPhoto(rs.getString("photo"));
//            photo.setDescription(rs.getString("description"));
//            photo.setUploadTime(rs.getTimestamp("uploadTime").toLocalDateTime());
//            photo.setSize(rs.getLong("size"));
//            photo.setUnparsedHashtags(rs.getString("hashtags"));
//
//            // Set the username retrieved from the profile table
//            String username = rs.getString("username");
//            Profile profile = new Profile();
//            profile.setUsername(username);
//            photo.setProfile(profile);
//
//            return photo;
//        });
//    }

    public List<Photo> getAllPhotosWithUsername() {
        String query = "SELECT p.*, pr.username " +
                "FROM photo p " +
                "JOIN profile pr ON p.profileId = pr.id";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Photo photo = photoRowMapper.mapRow(rs, rowNum);

            // Set the username retrieved from the profile table
            String username = rs.getString("username");
            Profile profile = new Profile();
            profile.setUsername(username);
            photo.setProfile(profile);

            return photo;
        });
    }
}
