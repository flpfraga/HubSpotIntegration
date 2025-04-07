package com.example.fraga.HubSpot.adapters.input.contactWebhook;

import com.example.fraga.HubSpot.adapters.input.models.DefaultController;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactWebhookUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
@Tag(name = "Webhooks", description = "Endpoints para recebimento de webhooks do HubSpot")
public class ContactWebhookController implements DefaultController {

    private final ContactWebhookUseCase contactWebhookUseCase;

    @Operation(
            summary = "Processa criação de contato",
            description = "Endpoint para receber notificações de criação de contatos do HubSpot",
            security = @SecurityRequirement(name = "hubspotSecret")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Webhook processado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Assinatura do webhook inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/contact-creation")
    public ResponseEntity<DefaultResponse<ContactWebhookResponse>> handleContactCreation(
            @Parameter(description = "Chave secreta para validação do webhook", required = true)
            @RequestHeader("X-Hubspot-Secret") String secret,

            @Parameter(description = "Dados do contato criado no HubSpot", required = true)
            @RequestBody Map<String, Contact> eventContact) {

        return success(contactWebhookUseCase.processContactCreation(eventContact, secret));
    }
} 