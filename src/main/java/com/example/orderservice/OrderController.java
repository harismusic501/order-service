package com.example.orderservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Value("${app.message}")
    private String message;

    @Value("${db.url}")
    private String dbUrl;

    @GetMapping("/order")
    public String getOrder() {
        return "Message: " + message + " | DB: " + dbUrl;
    }
}
