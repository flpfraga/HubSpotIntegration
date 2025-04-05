package com.example.fraga.HubSpot.domain.exception;

public enum ErrorCode {
    // Validação
    INVALID_SECRET_TOKEN("INVALID_SECRET_TOKEN", "Token de segurança inválido"),
    INVALID_EVENT("INVALID_EVENT", "Evento inválido"),
    INVALID_CONTACT_DATA("INVALID_CONTACT_DATA", "Dados do contato inválidos"),

    // Negócio
    CONTACT_NOT_FOUND("CONTACT_NOT_FOUND", "Contato não encontrado"),
    CONTACT_ALREADY_EXISTS("CONTACT_ALREADY_EXISTS", "Contato já existe"),
    WEBHOOK_PROCESSING_ERROR("WEBHOOK_PROCESSING_ERROR", "Erro ao processar webhook"),

    // Infraestrutura
    HUBSPOT_API_ERROR("HUBSPOT_API_ERROR", "Erro na comunicação com a API do HubSpot"),
    DATABASE_ERROR("DATABASE_ERROR", "Erro ao acessar o banco de dados"),
    CLIENT_ERROR("CLIENT_ERROR", "Erro na execução do Client"),
    INTERNAL_ERROR("INTERNAL_ERROR", "Erro interno do servidor");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 