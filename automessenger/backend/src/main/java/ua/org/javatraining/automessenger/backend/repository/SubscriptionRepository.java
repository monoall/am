package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ua.org.javatraining.automessenger.backend.entity.Subscription;

/**
 * Created by fisher on 28.07.15.
 */
@RepositoryRestResource(collectionResourceRel = "subscription", path = "subscription")
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
