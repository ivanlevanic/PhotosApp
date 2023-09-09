package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Profile;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository {
    public Long getProfileIdByUsername(String username);

    //public Profile findByUsername(String username);

    public Profile getProfileByUsername(String username);

    public void save(Profile profile);
}
