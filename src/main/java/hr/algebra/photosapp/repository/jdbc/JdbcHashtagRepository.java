package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.Hashtag;
import hr.algebra.photosapp.domain.Photo;
import hr.algebra.photosapp.repository.HashtagRepository;
import hr.algebra.photosapp.repository.rowMappers.HashtagRowMapper;
import hr.algebra.photosapp.repository.rowMappers.PhotoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcHashtagRepository implements HashtagRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcHashtagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Hashtag hashtag) {
        String sql = "INSERT INTO hashtag (id, photoId, name) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                hashtag.getId(),
                hashtag.getPhotoId(),
                hashtag.getName());
    }

    public List<Hashtag> getHashtagsForPhoto(Long photoId){
        String sql = "SELECT * FROM hashtag WHERE photoId = ?";
        return jdbcTemplate.query(sql, new Object[]{photoId}, new HashtagRowMapper());
    }

    public List<Long> getPhotoIdsByHashtag (String hashtag) {
        if (hashtag.startsWith("#")) {
            hashtag = hashtag.substring(1);
        }
        String sql = "SELECT photoId FROM hashtag WHERE name LIKE ?";
        String searchPattern = "%" + hashtag + "%";
        return jdbcTemplate.queryForList(sql, new Object[]{searchPattern}, Long.class);
    }

    public void deleteAllTheHashtags(Long photoId) {
        String query = "DELETE FROM hashtag WHERE photoId = ?";
        jdbcTemplate.update(query, photoId);
    }
}
