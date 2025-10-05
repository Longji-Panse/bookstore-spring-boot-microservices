package com.remnant.notifications.domain;

import com.remnant.notifications.ApplicationProperties;
import com.remnant.notifications.domain.models.OrderCancelledEvent;
import com.remnant.notifications.domain.models.OrderCreatedEvent;
import com.remnant.notifications.domain.models.OrderDeliveredEvent;
import com.remnant.notifications.domain.models.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final ApplicationProperties applicationProperties;

    public void sendOrderCreatedNotification(OrderCreatedEvent orderCreatedEvent) {
        String message =
                """
                ============================================================================
                Order Created Notification
                ----------------------------------------------------------------------------
                Dear %s,
                Your order with order-number: %s has been created successfully!

                Thanks.
                Bookstore Team.
                =============================================================================
                """
                        .formatted(orderCreatedEvent.customer().name(), orderCreatedEvent.orderNumber());
        log.info("Order Created Notification: {}", message);
        sendEmail(orderCreatedEvent.customer().email(), "Order Created Notification", message);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent orderDeliveredEvent) {
        String message =
                """
                ============================================================================
                Order Delivered Notification
                ----------------------------------------------------------------------------
                Dear %s,
                Your order with order-number: %s has been delivered successfully!

                Thanks.
                Bookstore Team.
                =============================================================================
                """
                        .formatted(orderDeliveredEvent.customer().name(), orderDeliveredEvent.orderNumber());
        log.info("Order Delivered Notification: {}", message);
        sendEmail(orderDeliveredEvent.customer().email(), "Order Delivered Notification", message);
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent orderCancelledEvent) {
        String message =
                """
                ============================================================================
                Order Cancelled Notification
                ----------------------------------------------------------------------------
                Dear %s,
                Your order with order-number: %s has been cancelled.
                Reason: %s

                Thanks.
                Bookstore Team.
                =============================================================================
                """
                        .formatted(
                                orderCancelledEvent.customer().name(),
                                orderCancelledEvent.orderNumber(),
                                orderCancelledEvent.reason());
        log.info("Order Cancelled Notification: {}", message);
        sendEmail(orderCancelledEvent.customer().email(), "Order Cancelled Notification", message);
    }

    public void sendOrderErrorNotification(OrderErrorEvent orderErrorEvent) {
        String message =
                """
                ============================================================================
                Order Error Notification
                ----------------------------------------------------------------------------
                Dear Team,
                An order with order-number: %s has an error.
                Reason: %s

                Thanks.
                Bookstore Team.
                =============================================================================
                """
                        .formatted(orderErrorEvent.orderNumber(), orderErrorEvent.reason());
        log.info("Order Error Notification: {}", message);
        sendEmail(applicationProperties.supportEmail(), "Order Error Notification", message);
    }

    private void sendEmail(String receiverEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(applicationProperties.supportEmail());
            helper.setTo(receiverEmail);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(mimeMessage);
            log.info("Email sent to: '{}'", receiverEmail);

        } catch (Exception e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}
