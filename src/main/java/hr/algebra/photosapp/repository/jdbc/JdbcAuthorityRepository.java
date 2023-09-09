package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.Authority;
import hr.algebra.photosapp.repository.AuthorityRepository;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class JdbcAuthorityRepository implements AuthorityRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthorityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Authority authority) {
        String sql = "INSERT INTO authorities SET authority = ?, username = ?";
        jdbcTemplate.update(sql, authority.getAuthority(), authority.getUsername());
    }

    public Boolean getAuthorityByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM authorities WHERE username = ? AND authority = 'ROLE_ADMIN'";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
        return count > 0;
    }

}
