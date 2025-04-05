package com.example.fraga.HubSpot.adapters.input.contactCreate;

import com.example.fraga.HubSpot.adapters.input.models.DefaultController;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class ContactController implements DefaultController {
    private final ContactUseCase contactUseCase;

    @PostMapping
    public ResponseEntity<DefaultResponse<ContactResponse>> create(@RequestBody ContactRequest request) {
        return success(contactUseCase.createContact(request));
    }
}
