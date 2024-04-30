package com.eric6166.base.config.tracing.impl;

import brave.Tracer;
import com.eric6166.base.config.tracing.AppTraceIdContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(value = Tracer.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppTraceIdContextImpl implements AppTraceIdContext {

    Tracer tracer;

    @Override
    public String getTraceId() {
        var span = Objects.requireNonNull(tracer.currentSpan());
        return span.context().traceIdString();
    }
}
