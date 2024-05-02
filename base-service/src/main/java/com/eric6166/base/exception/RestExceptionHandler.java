package com.eric6166.base.exception;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseUtils;
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
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
    private static final String KEY_OBJECT_TEMPLATE = "%s.%s";
    private static final String KEY_FIELD_TEMPLATE_PROPERTY_PATH = "%s.%s";
    private static final String KEY_COMMON_GENERAL_FIELD = String.format(KEY_COMMON_FIELD, BaseConst.COMMON, BaseConst.GENERAL_FIELD);

    MessageSource messageSource;
    BaseUtils baseUtils;
    AppExceptionUtils appExceptionUtils;
    Tracer tracer;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleResponseStatusException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.valueOf(e.getStatusCode().value()), e);
            log.debug("e: {}", e.getClass().getName()); // comment // for local testing
            span.tag("handleResponseStatusException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException e) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleAppException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorResponse = appExceptionUtils.buildErrorResponse(e.getHttpStatus(), e.getError(), e.getMessage(), e.getRootCause());
            log.debug("e: {}", e.getClass().getName());
            span.tag("handleAppException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, HandlerMethod handlerMethod) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleConstraintViolationException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            List<ErrorDetail> errorDetails = new ArrayList<>();
            var apiClassName = handlerMethod.getBeanType().getSimpleName();
            for (var constraintViolation : e.getConstraintViolations()) {
                errorDetails.add(buildErrorDetail(constraintViolation, apiClassName));
            }
            var errorResponse = appExceptionUtils.buildErrorResponse(ErrorCode.VALIDATION_ERROR, errorDetails);
            log.debug("e: {}", e.getClass().getName()); // comment // for local testing
            span.tag("handleConstraintViolationException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
    }

    private ErrorDetail buildErrorDetail(ConstraintViolation<?> constraintViolation, String apiClassName) {
        var propertyPath = constraintViolation.getPropertyPath().toString();
        var keyField = String.format(KEY_FIELD_TEMPLATE_PROPERTY_PATH, apiClassName, propertyPath);
        var parts = propertyPath.split(BaseConst.SPLIT_REGEX_DOT);
        var field = parts.length >= 1 ? parts[parts.length - 1] : BaseConst.GENERAL_FIELD;
        var messageTemplate = constraintViolation.getMessage();
        var msg = messageTemplate;
        if (messageTemplate.contains(BaseConst.PLACEHOLDER_0)) {
            var keyCommonField = String.format(KEY_COMMON_FIELD, BaseConst.COMMON, field);
            var model = buildModel(keyField, keyCommonField);
            msg = formatMsg(messageTemplate, model);
        }
        return new ValidationErrorDetail(keyField, field, null, constraintViolation.getInvalidValue(), StringUtils.capitalize(msg));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e, HandlerMethod handlerMethod) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleBindException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            List<ErrorDetail> errorDetails = new ArrayList<>();
            var apiClassName = handlerMethod.getBeanType().getSimpleName();
            for (var error : e.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    errorDetails.add(buildErrorDetail(fieldError, apiClassName));
                } else {
                    errorDetails.add(buildErrorDetail(error, apiClassName));
                }
            }
            var errorResponse = appExceptionUtils.buildErrorResponse(ErrorCode.VALIDATION_ERROR, errorDetails);
            log.debug("e: {}", e.getClass().getName()); // comment // for local testing
            span.tag("handleBindException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
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
            var keyCommonField = String.format(KEY_COMMON_FIELD, BaseConst.COMMON, field);
            var model = buildModel(keyField, keyCommonField);
            msg = formatMsg(messageTemplate, model);
        }
        return new ValidationErrorDetail(keyField, field, null, fieldError.getRejectedValue(), StringUtils.capitalize(msg));
    }

    private String buildMessageTemplate(ObjectError objectError) {
        var messageTemplate = StringUtils.EMPTY;
        if (ObjectUtils.isNotEmpty(objectError.getCodes())) {
            for (var code : objectError.getCodes()) {
                try {
                    messageTemplate = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
                    break;
                } catch (NoSuchMessageException ignored) {
                    //
                }
            }
        }
        if (StringUtils.isBlank(messageTemplate)) {
            messageTemplate = objectError.getDefaultMessage();
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
                //
            }
        }
        if (StringUtils.isBlank(model)) {
            model = messageSource.getMessage(KEY_COMMON_GENERAL_FIELD, null, LocaleContextHolder.getLocale()); // uncomment
        }
        return model;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
            log.debug("e: {}", e.getClass().getName()); // comment // for local testing
            span.tag("handleException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
    }

}
