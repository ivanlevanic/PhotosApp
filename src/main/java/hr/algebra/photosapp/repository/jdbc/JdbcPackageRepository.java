package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.PackagePlan;
import hr.algebra.photosapp.repository.PackageRepository;
import hr.algebra.photosapp.repository.rowMappers.PackageRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPackageRepository implements PackageRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPackageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public PackagePlan getPackageByPlan(String plan) {
        String query = "SELECT * FROM package WHERE plan = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{plan}, new PackageRowMapper());
    }

}
