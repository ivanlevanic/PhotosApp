package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.PackagePlan;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository {
    public PackagePlan getPackageByPlan(String plan);
}
