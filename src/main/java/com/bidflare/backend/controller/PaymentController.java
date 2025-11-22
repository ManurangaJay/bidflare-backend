package com.bidflare.backend.controller;

import com.bidflare.backend.dto.payment.PaymentRequestDTO;
import com.bidflare.backend.dto.payment.PaymentResponseDTO;
import com.bidflare.backend.exception.AuctionNotFoundException;
import com.bidflare.backend.exception.InvalidAuctionStateException;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.service.AuctionService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final AuctionService auctionService;
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public PaymentController (AuctionService auctionService){
        this.auctionService=auctionService;
    }

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponseDTO> createPaymentIntent(
            @RequestBody PaymentRequestDTO paymentRequest,
            @AuthenticationPrincipal User currentUser
            ){
        if(currentUser == null){
            logger.warn("Attempt to create payment intent without authentication.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        long amountInCents;
        try{
            amountInCents = auctionService.getAuctionPriceInCents(paymentRequest.auctionId(), currentUser);
        } catch (AuctionNotFoundException e) {
            logger.warn("Payment creation failed for auction {}: {}", paymentRequest.auctionId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessDeniedException e) {
            logger.warn("Payment creation failed for user {}: {}", currentUser.getId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InvalidAuctionStateException e) {
            logger.error("Payment creation failed due to invalid state for auction {}: {}", paymentRequest.auctionId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("lkr")
                    .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                    .putMetadata("auctionId", paymentRequest.auctionId().toString())
                    .putMetadata("userId",currentUser.getId().toString())
                    .build();

            // Create the PaymentIntent on Stripe's servers
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Send the 'client_secret' back to the frontend
            return ResponseEntity.ok(new PaymentResponseDTO(paymentIntent.getClientSecret()));
        } catch (StripeException e){
            logger.error("Stripe error creating payment intent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint for Stripe Webhook
     * @param payload the raw JSON payload from Stripe
     * @param sigHeader the 'Stripe-Signature' header
     * @return A 200 OK to Stripe, or 400/500 on error
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ){
        Event event;

        try{
            // Verify the event came from Stripe
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e){
            // Invalid signature
            logger.warn("Webhook signature verification failed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }

        // Handle the event
        switch (event.getType()){
            case "payment_intent.succeeded":
                handlePaymentSucceeded(event);
                break;

            case "payment_intent.payment_failed":
                handlePaymentFailed(event);
                break;

            default:
                logger.info("Unhandled event type: {}", event.getType());
        }
        return ResponseEntity.ok().body("Received");
    }

    private void handlePaymentSucceeded(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (paymentIntent == null) {
            logger.error("Could not deserialize PaymentIntent for event ID: {}", event.getId());
            return;
        }

        Map<String, String> metadata = paymentIntent.getMetadata();
        String auctionIdStr = metadata.get("auctionId");
        if (auctionIdStr == null) {
            logger.error("Webhook 'payment_intent.succeeded' received without 'auctionId' in metadata for PaymentIntent ID: {}", paymentIntent.getId());
            return;
        }

        UUID auctionId = UUID.fromString(auctionIdStr);
        logger.info("Payment succeeded for auctionId: {}. Fulfilling order.", auctionId);
        auctionService.markAuctionAsPaid(auctionId);
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        String auctionId = paymentIntent != null ? paymentIntent.getMetadata().get("auctionId") : "unknown";
        logger.warn("Payment failed for auctionId: {}. Reason: {}", auctionId, paymentIntent != null ? paymentIntent.getLastPaymentError().getMessage() : "unknown");
    }
}
