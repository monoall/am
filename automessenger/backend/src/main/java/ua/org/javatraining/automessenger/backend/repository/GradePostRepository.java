package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.javatraining.automessenger.backend.entity.GradePost;

/**
 * Created by fisher on 28.07.15.
 */

//@RepositoryRestResource(collectionResourceRel = "grade_post", path = "grade_post")
public interface GradePostRepository extends JpaRepository<GradePost, Long> {
}
