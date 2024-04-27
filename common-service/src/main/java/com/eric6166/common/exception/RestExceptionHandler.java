package com.eric6166.common.exception;

import com.eric6166.base.exception.ErrorDetail;
import com.eric6166.base.exception.ErrorResponse;
import com.eric6166.base.utils.BaseUtils;
import com.eric6166.common.utils.Const;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RestExceptionHandler {

    private static final String KEY_COMMON_FIELD = "%s.%s";
    private static final String KEY_FIELD_TEMPLATE = "%s.%s.%s";

    MessageSource messageSource;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e, HttpServletRequest httpServletRequest) {
        var errorMessage = BaseUtils.getRootCauseMessage(e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage);
        var errorResponse = new ErrorResponse(e.getStatusCode(), e.getStatusCode().toString(),
                errorMessage, httpServletRequest, null);
        return BaseUtils.buildResponseExceptionEntity(errorResponse);
    }

//    @ExceptionHandler(AppException.class)
//    public ResponseEntity<Object> handleAppException(AppException e, HttpServletRequest httpServletRequest) {
//        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus(), e.getError(),
//                e.getMessage(), httpServletRequest, e.getErrorDetails());
//        return BaseUtils.buildResponseExceptionEntity(errorResponse);
//    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e, HttpServletRequest httpServletRequest, HandlerMethod handlerMethod) {
        String errorMessage = BaseUtils.getRootCauseMessage(e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
        List<ErrorDetail> errorDetails = new ArrayList<>();
        for (var error : e.getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                String messageTemplate = buildMessageTemplate(fieldError);
                var msg = messageTemplate;
                String apiClassName = handlerMethod.getBeanType().getSimpleName();
                var keyField = String.format(KEY_FIELD_TEMPLATE, apiClassName, fieldError.getObjectName(), fieldError.getField());
                if (messageTemplate.contains(Const.PLACEHOLDER_0)) {
                    var keyCommonField = String.format(KEY_COMMON_FIELD, Const.COMMON, fieldError.getField());
                    String model = buildModel(keyField, keyCommonField);
                    msg = formatMsg(messageTemplate, model);
                }
                errorDetails.add(new ValidationErrorDetail(keyField, fieldError.getField(), null, fieldError.getRejectedValue(), StringUtils.capitalize(msg)));
            }
        }
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(),
                null, httpServletRequest, errorDetails);
        return BaseUtils.buildResponseExceptionEntity(errorResponse);
    }

    private String buildMessageTemplate(FieldError fieldError) {
        var messageTemplate = StringUtils.EMPTY;
        if (ObjectUtils.isNotEmpty(fieldError.getCodes())) {
            for (var code : fieldError.getCodes()) {
                try {
                    messageTemplate = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
                    break;
                } catch (NoSuchMessageException ignored) {
                }
            }
        }
        if (StringUtils.isBlank(messageTemplate)) {
            messageTemplate = fieldError.getDefaultMessage();
        }
        return messageTemplate;
    }

    private String formatMsg(String messageTemplate, String model) {
        var formattedMsg = new MessageFormat(messageTemplate);
        return formattedMsg.format(new Object[]{model});
    }

    private String buildModel(String keyField, String keyCommonField) {
        var model = StringUtils.EMPTY;
        List<String> keyFieldList = List.of(keyField, keyCommonField);
        for (var key : keyFieldList) {
            try {
                model = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                break;
            } catch (NoSuchMessageException ignored) {
            }
        }
        if (StringUtils.isBlank(model)) {
            model = keyField; // comment // for local testing
//            model = messageSource.getMessage(Const.GENERAL_FIELD, null, LocaleContextHolder.getLocale()); // uncomment
        }
        return model;
    }

    @ExceptionHandler(AppValidationException.class)
    public ResponseEntity<Object> handleAppValidationException(AppValidationException e, HttpServletRequest httpServletRequest) {
        String errorMessage = BaseUtils.getRootCauseMessage(e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.VALIDATION_ERROR.getHttpStatus(), ErrorCode.VALIDATION_ERROR.name(),
                e.getMessage(), httpServletRequest, e.getErrorDetails());
        return BaseUtils.buildResponseExceptionEntity(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e, HttpServletRequest httpServletRequest) {
        String errorMessage = BaseUtils.getRootCauseMessage(e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), httpServletRequest, null);
        return BaseUtils.buildResponseExceptionEntity(errorResponse);
    }
}
