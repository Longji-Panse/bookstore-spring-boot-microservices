package com.remnant.orderservice.jobs;

import com.remnant.orderservice.domain.OrderEventEntity;
import com.remnant.orderservice.domain.OrderEventService;
import com.remnant.orderservice.domain.models.OrderCreatedEvent;
import com.remnant.orderservice.domain.models.OrderEventType;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OrderEventsPublishingJobs {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsPublishingJobs.class);

    private final OrderEventService orderEventService;

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    @SchedulerLock(
            name = "publishOrderEvents"
            //,
           // lockAtLeastFor = "PT5S", // lock for at least a minute, overriding defaults
           // lockAtMostFor = "PT10S" // lock for at most 7 minutes
    )
    public void publishOrderEvents(){
        LockAssert.assertLocked();
        log.info("Publishing order events at {}", Instant.now());
        orderEventService.publishOrderEvents();
    }



}
