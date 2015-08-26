package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ua.org.javatraining.automessenger.backend.entity.Tag;

/**
 * Created by fisher on 28.07.15.
 */
@RepositoryRestResource(collectionResourceRel = "tag", path = "tag")
public interface TagRepository extends JpaRepository<Tag, Long> {
}
