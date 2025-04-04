# API de Integração com HubSpot

Esta API REST foi desenvolvida para integrar com o HubSpot, permitindo o gerenciamento de contatos através de uma interface RESTful.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.3
- HubSpot API Client
- Swagger/OpenAPI
- Maven
- JUnit 5
- Mockito

## Requisitos

- JDK 17 ou superior
- Maven 3.6 ou superior
- Conta no HubSpot com API Key

## Configuração

1. Clone o repositório
2. Configure a variável de ambiente `HUBSPOT_API_KEY` com sua chave de API do HubSpot
3. Execute o build do projeto:
```bash
mvn clean install
```

## Executando a Aplicação

Para executar a aplicação localmente:

```bash
mvn spring:boot run
```

A API estará disponível em `http://localhost:8080`

## Documentação da API

A documentação Swagger está disponível em:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Endpoints

### Contatos

#### Criar Contato
- **POST** `/api/v1/contacts`
- Cria um novo contato no HubSpot

#### Buscar Contato por ID
- **GET** `/api/v1/contacts/{id}`
- Retorna um contato específico do HubSpot

#### Listar Todos os Contatos
- **GET** `/api/v1/contacts`
- Retorna todos os contatos do HubSpot

#### Atualizar Contato
- **PUT** `/api/v1/contacts/{id}`
- Atualiza um contato existente no HubSpot

#### Deletar Contato
- **DELETE** `/api/v1/contacts/{id}`
- Remove um contato do HubSpot

## Exemplo de Uso

### Criar um Contato

```bash
curl -X POST http://localhost:8080/api/v1/contacts \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "1234567890",
    "company": "Test Company",
    "website": "www.test.com",
    "address": "123 Test St",
    "city": "Test City",
    "state": "Test State",
    "zipCode": "12345",
    "country": "Test Country"
  }'
```

## Testes

Para executar os testes:

```bash
mvn test
```

## Arquitetura

A aplicação foi desenvolvida seguindo os princípios da Arquitetura Limpa (Clean Architecture) e SOLID:

- **Domain**: Contém as entidades e regras de negócio
- **Application**: Contém os casos de uso
- **Infrastructure**: Implementa as interfaces de repositório e configurações
- **Interfaces**: Contém os controladores e DTOs

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request 