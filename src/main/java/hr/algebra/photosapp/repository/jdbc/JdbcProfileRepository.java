package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.Profile;
import hr.algebra.photosapp.repository.ProfileRepository;
import hr.algebra.photosapp.repository.rowMappers.ProfileRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProfileRepository implements ProfileRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long getProfileIdByUsername(String username) {
        String sql = "SELECT id FROM profile WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }

//    public Profile findByUsername(String username) {
//        String sql = "SELECT p.* FROM profile p INNER JOIN users u ON p.username = u.username WHERE u.username = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (resultSet, rowNum) -> {
//            Profile profile = new Profile();
//            profile.setId(resultSet.getLong("id"));
//            profile.setUsername(resultSet.getString("username"));
//            return profile;
//        });
//    }

    public Profile getProfileByUsername(String username) {
        String query = "SELECT * FROM profile WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{username}, new ProfileRowMapper());
    }

    public void save(Profile profile) {
        String sql = "INSERT INTO profile SET username = ?";
        jdbcTemplate.update(sql, profile.getUsername());
    }
}
