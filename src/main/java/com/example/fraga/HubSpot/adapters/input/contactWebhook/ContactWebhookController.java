package com.example.fraga.HubSpot.adapters.input.contactWebhook;

import com.example.fraga.HubSpot.adapters.input.models.DefaultController;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactWebhookUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
public class ContactWebhookController implements DefaultController {

    private final ContactWebhookUseCase contactWebhookUseCase;

    @PostMapping("/contact-creation")
    public ResponseEntity<DefaultResponse<Void>> handleContactCreation(
            @RequestHeader("X-Hubspot-Secret") String secret,
            @RequestBody Map<String, Contact> eventContact) {
        contactWebhookUseCase.processContactCreation(eventContact, secret);
        
        return success(null);
    }
} 