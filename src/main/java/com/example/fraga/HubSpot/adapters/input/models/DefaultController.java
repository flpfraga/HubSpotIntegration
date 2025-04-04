package com.example.fraga.HubSpot.adapters.input.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface DefaultController {

    default <T extends Object> ResponseEntity<DefaultResponse<T>> retornarResponse(final HttpStatus httpStatus,
                                                                                   final T response) {
        return ResponseEntity.status(httpStatus.value()).body(new DefaultResponse<>(httpStatus.value(), response));
    }

    /*
     * HTTP 200
     */
    default <T extends Object> ResponseEntity<DefaultResponse<T>> success(final T response) {
        return retornarResponse(HttpStatus.OK, response);
    }
    
    /**
     * Retorna uma resposta HTTP 200 sem conteúdo
     * @return ResponseEntity com status 200 e body nulo
     */
    default ResponseEntity<DefaultResponse<Void>> successNoContent() {
        return retornarResponse(HttpStatus.OK, null);
    }

}