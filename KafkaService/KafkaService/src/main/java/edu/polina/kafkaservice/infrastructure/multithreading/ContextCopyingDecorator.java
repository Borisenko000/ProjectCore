package edu.polina.kafkaservice.infrastructure.multithreading;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } else {
                    MDC.clear();
                    runnable.run();
                }
            } finally {
                MDC.clear();
            }
        };
    }
}
