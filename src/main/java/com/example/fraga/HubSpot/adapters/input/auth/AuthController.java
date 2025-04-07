package com.example.fraga.HubSpot.adapters.input.auth;

import com.example.fraga.HubSpot.adapters.input.models.DefaultController;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.port.input.AuthHubSpotUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints de autenticação com HubSpot")
public class AuthController implements DefaultController {

    private final AuthHubSpotUseCase authHubSpotUseCase;

    @Operation(
            summary = "Gera URL de autorização",
            description = "Gera uma URL para redirecionamento ao HubSpot para autenticação OAuth2"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "URL de autorização gerada com sucesso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de autenticação inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<DefaultResponse<AuthResponse>> generateAuthorizationUrl(
            @Parameter(description = "Dados necessários para geração da URL de autorização", required = true)
            @Valid @RequestBody AuthRequest request) {
        return success(authHubSpotUseCase.generateAuthorizationUrl(request));
    }

    @Operation(
            summary = "Callback de autenticação",
            description = "Endpoint de callback para receber o código de autorização do HubSpot",
            hidden = true
    )
    @GetMapping("/callback")
    public ModelAndView handleCallback(
            @Parameter(description = "Código de autorização retornado pelo HubSpot", required = true)
            @RequestParam("code") String code,
            
            @Parameter(description = "Estado da requisição para validação", required = true)
            @RequestParam("state") String state) {
        try {
            AuthResponse token = authHubSpotUseCase.handleCallback(code, state);
            ModelAndView model = new ModelAndView("auth/success");
            model.addObject("token", token);
            return model;
        } catch (Exception e) {
            return new ModelAndView("auth/error");
        }
    }
} 