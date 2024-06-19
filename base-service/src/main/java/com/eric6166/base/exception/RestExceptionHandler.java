package com.eric6166.base.exception;

import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseUtils;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionException;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RestExceptionHandler {

    private static final String KEY_COMMON_FIELD_TEMPLATE = "%s.%s";
    private static final String KEY_FIELD_TEMPLATE = "%s.%s.%s";
    private static final String KEY_OBJECT_TEMPLATE = "%s.%s";
    private static final String KEY_FIELD_TEMPLATE_PROPERTY_PATH = "%s.%s";

    MessageSource messageSource;
    BaseUtils baseUtils;
    AppExceptionUtils appExceptionUtils;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.valueOf(e.getStatusCode().value()), e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }


    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException e) {
        var errorResponse = appExceptionUtils.buildErrorResponse(e.getHttpStatus(), e.getError(),
                StringUtils.capitalize(e.getMessage()), e.getRootCause());
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            ServletRequestBindingException.class,
//            InvalidDataAccessApiUsageException.class
    })
    public ResponseEntity<Object> handleBadRequestException(Exception e) {
        var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, HandlerMethod handlerMethod) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        var apiClassName = handlerMethod.getBeanType().getSimpleName();
        e.getConstraintViolations().forEach(constraintViolation ->
                errorDetails.add(buildErrorDetail(constraintViolation, apiClassName))
        );
        var errorResponse = appExceptionUtils.buildErrorResponse(ErrorCode.VALIDATION_ERROR, errorDetails);
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }

    private ErrorDetail buildErrorDetail(ConstraintViolation<?> constraintViolation, String apiClassName) {
        var propertyPath = constraintViolation.getPropertyPath().toString();
        var keyField = String.format(KEY_FIELD_TEMPLATE_PROPERTY_PATH, apiClassName, propertyPath);
        var parts = propertyPath.split(BaseConst.SPLIT_REGEX_DOT);
        var field = parts.length >= 1 ? parts[parts.length - 1] : BaseConst.GENERAL_FIELD;
        var messageTemplate = constraintViolation.getMessage();
        var msg = messageTemplate;
        if (messageTemplate.contains(BaseConst.PLACEHOLDER_0)) {
            var keyCommonField = String.format(KEY_COMMON_FIELD_TEMPLATE, BaseConst.COMMON, field);
            var model = buildModel(keyField, keyCommonField);
            msg = formatMsg(messageTemplate, model);
        }
        return new ValidationErrorDetail(keyField, field, null, constraintViolation.getInvalidValue(), StringUtils.capitalize(msg));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e, HandlerMethod handlerMethod) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        var apiClassName = handlerMethod.getBeanType().getSimpleName();
        e.getAllErrors().forEach(objectError -> {
            if (objectError instanceof FieldError fieldError) {
                errorDetails.add(buildErrorDetail(fieldError, apiClassName));
            } else {
                errorDetails.add(buildErrorDetail(objectError, apiClassName));
            }
        });
        var errorResponse = appExceptionUtils.buildErrorResponse(ErrorCode.VALIDATION_ERROR, errorDetails);
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }

    private ErrorDetail buildErrorDetail(ObjectError objectError, String apiClassName) {
        var messageTemplate = buildMessageTemplate(objectError);
        var object = objectError.getObjectName();
        var keyObject = String.format(KEY_OBJECT_TEMPLATE, apiClassName, object);
        var msg = messageTemplate;
        if (messageTemplate.contains(BaseConst.PLACEHOLDER_0)) {
            var keyCommonObject = String.format(KEY_OBJECT_TEMPLATE, BaseConst.COMMON, object);
            var model = buildModel(keyObject, keyCommonObject);
            msg = formatMsg(messageTemplate, model);
        }
        return new ValidationErrorDetail(null, null, keyObject, object, null, StringUtils.capitalize(msg));

    }

    private ErrorDetail buildErrorDetail(FieldError fieldError, String apiClassName) {
        var messageTemplate = buildMessageTemplate(fieldError);
        var field = fieldError.getField();
        var keyField = String.format(KEY_FIELD_TEMPLATE, apiClassName, fieldError.getObjectName(), field);
        var msg = messageTemplate;
        if (messageTemplate.contains(BaseConst.PLACEHOLDER_0)) {
            var keyCommonField = String.format(KEY_COMMON_FIELD_TEMPLATE, BaseConst.COMMON, field);
            var model = buildModel(keyField, keyCommonField);
            msg = formatMsg(messageTemplate, model);
        }
        var rejectedValue = fieldError.getRejectedValue() instanceof MultipartFile ? null : fieldError.getRejectedValue();
        return new ValidationErrorDetail(keyField, field, null, rejectedValue, StringUtils.capitalize(msg));
    }

    private String buildMessageTemplate(ObjectError objectError) {
        var messageTemplate = StringUtils.EMPTY;
        if (ObjectUtils.isNotEmpty(objectError.getCodes())) {
            messageTemplate = Arrays.stream(objectError.getCodes())
                    .map(code -> {
                        try {
                            return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
                        } catch (NoSuchMessageException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(StringUtils.EMPTY);
        }
        return StringUtils.defaultIfBlank(messageTemplate, objectError.getDefaultMessage());
    }

    private String formatMsg(String messageTemplate, String model) {
        var formattedMsg = new MessageFormat(messageTemplate);
        return formattedMsg.format(new Object[]{model});
    }

    private String buildModel(String keyField, String keyCommonField) {
        var keyFieldList = List.of(keyField, keyCommonField);
        return keyFieldList.stream()
                .map(key -> {
                    try {
                        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                    } catch (NoSuchMessageException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(messageSource.getMessage(String.format(KEY_COMMON_FIELD_TEMPLATE,
                        BaseConst.COMMON, BaseConst.GENERAL_FIELD), null, LocaleContextHolder.getLocale()));
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Object> handleCallNotPermittedException(CallNotPermittedException e) {
        var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, e);
        log.info("handle the exception when the CircuitBreaker is open, e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(Throwable e) {
        var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<Object> handleCompletionException(CompletionException e) {
        if (e.getCause() instanceof AppException appException) {
            return handleAppException(appException);
        }
        return handleThrowable(e.getCause());
    }

}
