package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Consumption;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository {
    public Consumption findByProfileId(Long profileId);
    public Consumption getConsumptionByProfileId(Long profileId);
}
