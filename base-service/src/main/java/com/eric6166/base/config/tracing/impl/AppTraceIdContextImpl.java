package com.eric6166.base.config.tracing.impl;

import brave.Tracer;
import com.eric6166.base.config.tracing.AppTraceIdContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(value = Tracer.class)
public class AppTraceIdContextImpl implements AppTraceIdContext {

    private final Tracer tracer;

    @Override
    public String getTraceId() {
        var span = Objects.requireNonNull(tracer.currentSpan());
        return span.context().traceIdString();
    }
}
