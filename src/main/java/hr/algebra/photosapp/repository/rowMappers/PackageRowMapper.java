package hr.algebra.photosapp.repository.rowMappers;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import hr.algebra.photosapp.domain.PackagePlan;


public class PackageRowMapper implements RowMapper<PackagePlan> {

    @Override
    public PackagePlan mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPlan(resultSet.getString("plan"));
        packagePlan.setPrice(resultSet.getFloat("price"));
        packagePlan.setUploadSize(resultSet.getLong("uploadSize"));
        packagePlan.setDailyUploadLimit(resultSet.getInt("dailyUploadLimit"));
        return packagePlan;
    }
}
