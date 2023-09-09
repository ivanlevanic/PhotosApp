package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Profile;
import hr.algebra.photosapp.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    boolean existsByUsername(String username);
    void save(User user);
    User findByUsername(String username);
    public User getUserByUsername(String username);
    //public Profile getProfileByUsername(String username);
}
