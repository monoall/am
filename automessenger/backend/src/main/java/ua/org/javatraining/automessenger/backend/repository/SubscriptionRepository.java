package ua.org.javatraining.automessenger.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.org.javatraining.automessenger.backend.entity.Subscription;

/**
 * Created by fisher on 28.07.15.
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
