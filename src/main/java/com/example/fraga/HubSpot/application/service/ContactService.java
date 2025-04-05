package com.example.fraga.HubSpot.application.service;

import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import com.example.fraga.HubSpot.domain.exception.InfrastructureException;
import com.example.fraga.HubSpot.domain.exception.ValidationException;
import com.example.fraga.HubSpot.domain.model.Contact;
import com.example.fraga.HubSpot.port.input.ContactUseCase;
import com.example.fraga.HubSpot.port.output.ContactRepositoryPort;
import com.example.fraga.HubSpot.port.output.CrmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService implements ContactUseCase {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_ERROR = "ERROR";

    private final CrmClient crmClient;
    private final ContactRepositoryPort contactRepository;

    @Override
    @Transactional
    public Contact createContact(Contact contact) {
        try {
            log.info("m=createContact contact={}", contact);
            
            validateContact(contact);
            
            contact.setStatus(STATUS_PENDING);
            contact = contactRepository.save(contact);
            
            log.info("m=createContact status=success contact={}", contact);
            return contact;
        } catch (ValidationException | BusinessException e) {
            log.error("m=createContact status=error contact={} exception={} message={}", 
                contact, e.getClass().getSimpleName(), e.getMessage());
            contact.setStatus(STATUS_ERROR);
            contact.setErrorMessage(e.getMessage());
            contactRepository.save(contact);
            throw e;
        } catch (Exception e) {
            log.error("m=createContact status=error contact={} exception={} message={}", 
                contact, e.getClass().getSimpleName(), e.getMessage());
            contact.setStatus(STATUS_ERROR);
            contact.setErrorMessage(e.getMessage());
            contactRepository.save(contact);
            throw new InfrastructureException(
                ErrorCode.INTERNAL_ERROR.getCode(),
                ErrorCode.INTERNAL_ERROR.getMessage(),
                e.getMessage()
            );
        }
    }

    private void validateContact(Contact contact) {
        if (contact.getEmail() == null || contact.getEmail().isEmpty()) {
            throw new ValidationException(
                ErrorCode.INVALID_CONTACT_DATA.getCode(),
                ErrorCode.INVALID_CONTACT_DATA.getMessage(),
                "Email é obrigatório"
            );
        }

        if (contactRepository.findByEmail(contact.getEmail()).isPresent()) {
            throw new BusinessException(
                ErrorCode.CONTACT_ALREADY_EXISTS.getCode(),
                ErrorCode.CONTACT_ALREADY_EXISTS.getMessage()
            );
        }
    }
}
