package com.bidflare.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict is a good fit for invalid state
public class InvalidAuctionStateException extends RuntimeException {
    public InvalidAuctionStateException(String message) {
        super(message);
    }
}