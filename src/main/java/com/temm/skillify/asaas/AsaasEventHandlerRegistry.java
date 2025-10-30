package com.temm.skillify.asaas;

import com.temm.skillify.model.enums.AsaasEvent;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AsaasEventHandlerRegistry {

    private final Map<AsaasEvent, AsaasEventHandler> handlers = new EnumMap<>(AsaasEvent.class);

    public AsaasEventHandlerRegistry(List<AsaasEventHandler> handlerList) {
        for (AsaasEventHandler h : handlerList) {
            EventHandler ann = h.getClass().getAnnotation(EventHandler.class);
            if (ann != null) {
                handlers.put(ann.value(), h);
            }
        }
    }

    public Optional<AsaasEventHandler> get(AsaasEvent event) {
        return Optional.ofNullable(handlers.get(event));
    }
}