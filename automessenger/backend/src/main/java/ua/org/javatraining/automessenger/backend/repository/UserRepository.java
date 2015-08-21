package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ua.org.javatraining.automessenger.backend.entity.User;

/**
 * Created by fisher on 28.07.15.
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(@Param("name") String name);
}
