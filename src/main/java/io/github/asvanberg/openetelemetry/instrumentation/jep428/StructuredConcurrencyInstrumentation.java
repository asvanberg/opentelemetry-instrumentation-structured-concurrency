package io.github.asvanberg.openetelemetry.instrumentation.jep428;

import io.opentelemetry.javaagent.bootstrap.executors.PropagatedContext;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import io.opentelemetry.javaagent.instrumentation.executors.JavaExecutorInstrumentation;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class StructuredConcurrencyInstrumentation implements TypeInstrumentation {

    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return hasSuperClass(named("jdk.incubator.concurrent.StructuredTaskScope"));
    }

    @Override
    public void transform(final TypeTransformer transformer) {
        transformer.applyAdviceToMethod(named("fork").and(takesArgument(0, Callable.class)),
                StructuredConcurrencyInstrumentation.class.getName() + "$ScopeAdvice");
    }

    public static class ScopeAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static PropagatedContext enter(@Advice.Argument(value = 0, readOnly = false) Callable<?> task) {
            return JavaExecutorInstrumentation.SetCallableStateAdvice.enterJobSubmit(task);
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
        public static void exit(
                @Advice.Enter PropagatedContext propagatedContext,
                @Advice.Thrown Throwable throwable,
                @Advice.Return Future<?> future)
        {
            JavaExecutorInstrumentation.SetCallableStateAdvice.exitJobSubmit(propagatedContext, throwable, future);
        }
    }
}
