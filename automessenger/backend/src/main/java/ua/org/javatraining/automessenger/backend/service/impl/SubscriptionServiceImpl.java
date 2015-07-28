package ua.org.javatraining.automessenger.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.Subscription;
import ua.org.javatraining.automessenger.backend.repository.SubscriptionRepository;
import ua.org.javatraining.automessenger.backend.service.SubscriptionService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription addSubscription(Subscription subscription) {
        return subscriptionRepository.saveAndFlush(subscription);
    }

    @Override
    public boolean delete(Long id) {
        if (subscriptionRepository.exists(id)) {
            subscriptionRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public Subscription getById(Long id) {
        return subscriptionRepository.findOne(id);
    }

    @Override
    public Subscription editSubscription(Subscription subscription) {
        return subscriptionRepository.saveAndFlush(subscription);
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }
}
