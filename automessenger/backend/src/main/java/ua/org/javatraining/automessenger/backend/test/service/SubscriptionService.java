package ua.org.javatraining.automessenger.backend.test.service;

import ua.org.javatraining.automessenger.backend.entity.Subscription;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface SubscriptionService {
    Subscription addSubscription(Subscription subscription);
    boolean delete(Long id);
    Subscription getById(Long id);
    Subscription editSubscription(Subscription subscription);
    List<Subscription> getAll();
}
