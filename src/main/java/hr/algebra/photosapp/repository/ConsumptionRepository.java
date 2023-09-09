package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Consumption;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface ConsumptionRepository {
    //public Consumption findByProfileId(Long profileId);
    Consumption getConsumptionByProfileId(Long profileId, LocalDate today);
    void updateNumberOfUploadedPhotos (long consumptionId, int numberOfUploadedPhotos);
    void save(Consumption consumption);
}
