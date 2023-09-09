package hr.algebra.photosapp.repository;

import hr.algebra.photosapp.domain.Authority;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository {
    public void save(Authority authority);
    public Boolean getAuthorityByUsername(String username);
}
