package hr.algebra.photosapp.repository.jdbc;

import hr.algebra.photosapp.domain.Subscription;
import hr.algebra.photosapp.repository.SubscriptionRepository;
import hr.algebra.photosapp.repository.rowMappers.SubscriptionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcSubscriptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Subscription getSubscriptionByProfileId(Long profileId) {
        String query = "SELECT * FROM subscription WHERE profileId = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{profileId}, new SubscriptionRowMapper());
    }

    public String getPackagePlanByProfileId(Long profileId) {
        String query = "SELECT packagePlan FROM subscription WHERE profileId = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{profileId}, String.class);
    }

    public void savePlanChangeRequest(Subscription subscription) {
        String sql = "UPDATE subscription SET newPackage = ?, dateOfLastChange = ? WHERE id = ?";
        jdbcTemplate.update(sql, subscription.getNewPackage(), subscription.getDateOfLastChange(), subscription.getId());
    }

    public void saveAfterPlanChange(Subscription subscription) {
        String sql = "UPDATE subscription SET packagePlan = ?, dateOfLastChange = ?, lastRefreshDate = ? WHERE id = ?";
        jdbcTemplate.update(sql, subscription.getPackagePlan(), subscription.getDateOfLastChange(), subscription.getLastRefreshDate(), subscription.getId());
    }

    public void save(Subscription subscription) {
        String sql = "INSERT INTO subscription SET profileId = ?, packagePlan = ?, dateOfLastChange = ?, lastRefreshDate = ?, newPackage = ?";
        jdbcTemplate.update(sql, subscription.getProfileId(), subscription.getPackagePlan(), subscription.getDateOfLastChange(), subscription.getLastRefreshDate(), subscription.getNewPackage());
    }
}
