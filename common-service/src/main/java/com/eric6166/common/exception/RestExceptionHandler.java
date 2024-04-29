package com.eric6166.common.exception;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import com.eric6166.base.exception.ErrorDetail;
import com.eric6166.base.exception.ErrorResponse;
import com.eric6166.base.utils.BaseUtils;
import com.eric6166.common.utils.Const;
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
    BaseUtils baseUtils;
    Tracer tracer;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleResponseStatusException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorMessage = BaseUtils.getRootCauseMessage(e);
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
            var errorResponse = baseUtils.buildErrorResponse(HttpStatus.valueOf(e.getStatusCode().value()), e.getMessage());
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
            var errorMessage = BaseUtils.getRootCauseMessage(e);
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
            var errorResponse = baseUtils.buildErrorResponse(e.getHttpStatus(), e.getMessage());
            span.tag("handleAppException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e, HandlerMethod handlerMethod) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleBindException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorMessage = BaseUtils.getRootCauseMessage(e);
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
            List<ErrorDetail> errorDetails = new ArrayList<>();
            for (var error : e.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    errorDetails.add(buildErrorDetail(handlerMethod, fieldError));
                }
            }
            var errorResponse = baseUtils.buildErrorResponse(ErrorCode.VALIDATION_ERROR.getHttpStatus(),
                    ErrorCode.VALIDATION_ERROR.name(), ErrorCode.VALIDATION_ERROR.getReasonPhrase(), errorDetails);
            span.tag("handleBindException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }

    }

    private ErrorDetail buildErrorDetail(HandlerMethod handlerMethod, FieldError fieldError) {
        var messageTemplate = buildMessageTemplate(fieldError);
        var msg = messageTemplate;
        var apiClassName = handlerMethod.getBeanType().getSimpleName();
        var keyField = String.format(KEY_FIELD_TEMPLATE, apiClassName, fieldError.getObjectName(), fieldError.getField());
        if (messageTemplate.contains(Const.PLACEHOLDER_0)) {
            var keyCommonField = String.format(KEY_COMMON_FIELD, Const.COMMON, fieldError.getField());
            var model = buildModel(keyField, keyCommonField);
            msg = formatMsg(messageTemplate, model);
        }
        return new ValidationErrorDetail(keyField, fieldError.getField(), null, null, fieldError.getRejectedValue(), StringUtils.capitalize(msg));
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
    public ResponseEntity<Object> handleAppValidationException(AppValidationException e) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleAppValidationException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorMessage = BaseUtils.getRootCauseMessage(e);
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
            var errorResponse = baseUtils.buildErrorResponse(ErrorCode.VALIDATION_ERROR.getHttpStatus(),
                    ErrorCode.VALIDATION_ERROR.name(), ErrorCode.VALIDATION_ERROR.getReasonPhrase(), e.getErrorDetails());
            span.tag("handleAppValidationException errorResponse", errorResponse.toString());
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException exception) {
            span.error(exception);
            throw exception;
        } finally {
            span.finish();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("handleException").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(e);
            var errorMessage = BaseUtils.getRootCauseMessage(e);
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
            var errorResponse = baseUtils.buildInternalServerErrorResponse(e.getMessage());
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
