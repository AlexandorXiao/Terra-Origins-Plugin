package dev.devill.terraOrigins.app.utils;

import org.bukkit.attribute.AttributeModifier;
import org.bukkit.NamespacedKey;
import dev.devill.terraOrigins.TerraOrigins;

public class AttributeData {
    private final double amount;
    private final AttributeModifier.Operation operation;
    private final NamespacedKey key;

    public AttributeData(double amount, AttributeModifier.Operation operation, String key) {
        this.amount = amount;
        this.operation = operation;
        this.key = new NamespacedKey(TerraOrigins.getPlugin(), key);
    }

    public double getAmount() {
        return amount;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public AttributeModifier createModifier() {
        return new AttributeModifier(
            key,
            amount,
            operation
        );
    }
}
