package com.example.fraga.HubSpot.adapters.input.auth;

import com.example.fraga.HubSpot.adapters.input.models.DefaultController;
import com.example.fraga.HubSpot.adapters.input.models.DefaultResponse;
import com.example.fraga.HubSpot.port.input.AuthHubSpotUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints de autenticação com HubSpot")
public class AuthController implements DefaultController {

    private final AuthHubSpotUseCase authHubSpotUseCase;

    @PostMapping("/url")
    @Operation(summary = "Gera URL de autorização", description = "Gera uma URL para redirecionamento ao HubSpot para autenticação")
    public ResponseEntity<DefaultResponse<AuthResponse>> generateAuthorizationUrl(@RequestBody AuthRequest request) {
        return success(authHubSpotUseCase.generateAuthorizationUrl(request));
    }

    @GetMapping("/callback")
    @Operation(summary = "Callback de autenticação", description = "Endpoint de callback após autenticação no HubSpot")
    public  ResponseEntity<DefaultResponse<AuthResponse>> handleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state) {
        return success(authHubSpotUseCase.handleCallback(code, state));
    }
} 