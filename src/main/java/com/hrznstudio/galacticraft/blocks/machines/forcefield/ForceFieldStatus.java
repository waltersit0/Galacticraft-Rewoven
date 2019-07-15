package com.hrznstudio.galacticraft.blocks.machines.forcefield;

import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public enum ForceFieldStatus {
    /**
     * Force Field has no power.
     */
    INSUFFICIENT_POWER(new TranslatableText("ui.galacticraft-rewoven.machinestatus.insufficient_power").setStyle(new Style().setColor(Formatting.DARK_RED)).asFormattedString()),
    /**
     * Fabricator is not processing.
     */
    PROTECTED(new TranslatableText("ui.galacticraft-rewoven.machinestatus.protected").setStyle(new Style().setColor(Formatting.GOLD)).asFormattedString());

    private String name;

    ForceFieldStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ForceFieldStatus get(int index) {
        switch (index) {
            case 0: return INSUFFICIENT_POWER;
            case 1: return PROTECTED;
        }
        return INSUFFICIENT_POWER;
    }
}