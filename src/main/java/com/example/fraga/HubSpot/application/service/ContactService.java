package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactRequest;
import com.example.fraga.HubSpot.adapters.input.contactCreate.ContactResponse;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import com.example.fraga.HubSpot.domain.exception.InfrastructureException;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import com.example.fraga.HubSpot.port.output.ContactRepositoryPort;
import com.example.fraga.HubSpot.port.output.CrmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService implements ContactUseCase {

    private final CrmClient crmClient;
    private final ContactRepositoryPort contactRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public ContactResponse createContact(ContactRequest contactRequest, String accessToken) {
        try {
            Contact contact = mapper.map(contactRequest, Contact.class);

            crmClient.create(contact, accessToken);

            contact = contactRepository.save(contact);

            log.info("m=createContact status=success contactRequest={}", contactRequest);
            return new ContactResponse("Contato criado");
        } catch (RedisConnectionFailureException | SerializationException e) {
            logError(e);
            throw new InfrastructureException(ErrorCode.DATABASE_ERROR.getCode(), e.getMessage());
        } catch (WebClientResponseException e) {
            logError(e);
            throw new InfrastructureException(ErrorCode.CLIENT_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            logError(e);
            throw new InfrastructureException(
                    ErrorCode.INTERNAL_ERROR.getCode(), e.getMessage()
            );
        }
    }
    private void logError(Exception e) {
        log.error("m=processContactCreation status=error exception={} message={}",
                e.getClass().getSimpleName(), e.getMessage());
    }
}
