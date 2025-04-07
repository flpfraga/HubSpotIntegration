package com.example.fraga.HubSpot.shared;

import com.example.fraga.HubSpot.domain.exception.BusinessException;
import com.example.fraga.HubSpot.domain.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ConverterUtils {

    public ConverterUtils(){
        log.error("m=ConverterUtils status=error");
        throw new BusinessException(ErrorCode.INTERNAL_ERROR.getCode(),
                "ConverterUltils é static e não deve ser instanciada");
    }

    public static Long converterStringToLong(String value){
        try{
            return Long.parseLong(value);
        }
        catch (Exception e){
            log.error("m=converterStringToLong status=error message={}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR.getCode(),
                    "Erro ao tentar executar conversão de string para Long");
        }
    }

    public static LocalDateTime converterLongToDateTime(Long timestamp){
        if(ObjectUtils.isEmpty(timestamp)){
            log.error("m=converterLongToDateTime status=error message={}", "Timestamp é nulo.");
            throw new BusinessException(ErrorCode.INTERNAL_ERROR.getCode(),
                    "Erro ao converter Long para LocalDateTime");
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
