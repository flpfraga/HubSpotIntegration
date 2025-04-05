package com.example.fraga.HubSpot.adapters.input.contactCreate;

import com.example.fraga.HubSpot.adapters.input.models.DefaultController;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@Tag(name = "Contatos", description = "Endpoints para gerenciamento de contatos")
public class ContactController implements DefaultController {
    private final ContactUseCase contactUseCase;

    @Operation(
            summary = "Cria um novo contato",
            description = "Endpoint para criar um novo contato no sistema e no HubSpot"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados do contato inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<DefaultResponse<ContactResponse>> create(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody ContactRequest contactRequest) {
        return success(contactUseCase.createContact(contactRequest, accessToken));
    }
}
