package com.remnant.orderservice.jobs;

import com.remnant.orderservice.domain.OrderService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);
    private final OrderService orderService;

    public OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${orders.new-orders-job-cron}")
    @SchedulerLock(
            name = "processNewOrders"
            // ,
            // lockAtLeastFor = "PT5S", // lock for at least a minute, overriding defaults
            // lockAtMostFor = "PT10S" // lock for at most 7 minutes
            )
    public void processNewOrders() {
        LockAssert.assertLocked();
        log.info("New Orders processing started {}", Instant.now());
        orderService.processNewOrders();
    }
}
