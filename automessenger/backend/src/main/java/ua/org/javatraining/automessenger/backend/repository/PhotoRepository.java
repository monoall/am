package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.javatraining.automessenger.backend.entity.Photo;

/**
 * Created by fisher on 28.07.15.
 */

//@RepositoryRestResource(collectionResourceRel = "photo", path = "photo")
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
