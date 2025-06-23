package com.remnant.orderservice.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public String getLoggedInUser() {
        return "user";
    }
}
