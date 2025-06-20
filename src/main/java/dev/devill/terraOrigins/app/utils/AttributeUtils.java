package dev.devill.terraOrigins.app.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class AttributeUtils {
    public static void applyAttributes(LivingEntity entity, Map<Attribute, AttributeData> attributes) {
        attributes.forEach((attribute, data) -> {
            AttributeInstance instance = entity.getAttribute(attribute);
            if (instance != null) {
                instance.addModifier(data.createModifier());
            }
        });
    }

    public static void removeAttributes(LivingEntity entity, Map<Attribute, AttributeData> attributes) {
        attributes.forEach((attribute, data) -> {
            AttributeInstance instance = entity.getAttribute(attribute);
            if (instance != null) {
                instance.getModifiers().stream()
                    .filter(modifier -> modifier.getKey().equals(data.getKey()))
                    .forEach(modifier -> instance.removeModifier(modifier.getKey()));
            }
        });
    }

    public static void removeAttribute(LivingEntity entity, Attribute attribute, String key) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.getModifiers().stream()
                .filter(modifier -> modifier.getKey().getKey().equals(key))
                .forEach(modifier -> instance.removeModifier(modifier.getKey()));
        }
    }

    public static boolean hasAttribute(LivingEntity entity, Attribute attribute, String key) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return false;
        
        return instance.getModifiers().stream()
            .anyMatch(modifier -> modifier.getKey().getKey().equals(key));
    }

    public static boolean hasAttributes(LivingEntity entity, Map<Attribute, AttributeData> attributes) {
        return attributes.entrySet().stream()
            .allMatch(entry -> hasAttribute(entity, entry.getKey(), entry.getValue().getKey().getKey()));
    }
}
