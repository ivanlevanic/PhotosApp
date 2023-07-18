package hr.algebra.photosapp.repository.rowMappers;

import hr.algebra.photosapp.domain.Subscription;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionRowMapper implements RowMapper<Subscription> {

    @Override
    public Subscription mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(resultSet.getLong("id"));
        subscription.setProfileId(resultSet.getLong("profileId"));
        subscription.setPackagePlan(resultSet.getString("packagePlan"));
        return subscription;
    }
}

