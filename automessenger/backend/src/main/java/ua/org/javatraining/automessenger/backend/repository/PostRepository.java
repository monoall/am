package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ua.org.javatraining.automessenger.backend.entity.Post;

/**
 * Created by fisher on 28.07.15.
 */
@RepositoryRestResource(collectionResourceRel = "post", path = "post")
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p FROM Post p WHERE p.userId = " +
            "(SELECT u.id FROM User u WHERE u.name = :name)")
    Post getPostByUserName(@Param("name") String name);
}
