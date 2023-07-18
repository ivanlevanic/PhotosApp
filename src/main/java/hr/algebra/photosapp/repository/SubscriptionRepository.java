package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.PackagePlan;
import hr.algebra.photosapp.domain.Subscription;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository {
    public Subscription getSubscriptionByProfileId(Long profileId);

    public void savePlanChangeRequest(Subscription subscription);

    public void saveAfterPlanChange(Subscription subscription);

    public void save(Subscription subscription);
}
