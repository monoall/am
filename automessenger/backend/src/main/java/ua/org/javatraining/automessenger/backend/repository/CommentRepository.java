package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.javatraining.automessenger.backend.entity.Comment;

/**
 * Created by fisher on 28.07.15.
 */
//@RepositoryRestResource(collectionResourceRel = "comment", path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
