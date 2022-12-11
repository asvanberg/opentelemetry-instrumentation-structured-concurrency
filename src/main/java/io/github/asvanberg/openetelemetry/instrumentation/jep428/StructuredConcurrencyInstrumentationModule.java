package io.github.asvanberg.openetelemetry.instrumentation.jep428;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.ignore.IgnoredTypesBuilder;
import io.opentelemetry.javaagent.extension.ignore.IgnoredTypesConfigurer;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;

@AutoService({InstrumentationModule.class, IgnoredTypesConfigurer.class})
public class StructuredConcurrencyInstrumentationModule extends InstrumentationModule implements IgnoredTypesConfigurer {
    public StructuredConcurrencyInstrumentationModule() {
        super("structured-concurrency");
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return List.of(new StructuredConcurrencyInstrumentation());
    }

    @Override
    public void configure(final IgnoredTypesBuilder builder, final ConfigProperties config) {
        builder.allowClass("jdk.incubator.concurrent.");
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
