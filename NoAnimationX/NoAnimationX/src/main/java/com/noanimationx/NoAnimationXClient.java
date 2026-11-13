package com.noanimationx;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public final class NoAnimationXClient implements ClientModInitializer {
    public static final String MOD_ID = "noanimationx";
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("noanimationx.properties");

    private static final Set<String> DEFAULT_ITEMS = new LinkedHashSet<>(Arrays.asList(
            "minecraft:wind_charge",
            "minecraft:ender_pearl",
            "minecraft:end_crystal"
    ));

    private static volatile Set<String> disabledItems = new LinkedHashSet<>(DEFAULT_ITEMS);
    private static volatile boolean enabled = true;
    private static volatile boolean lockItemPosition = true;
    private static volatile boolean lockHandPosition = true;

    @Override
    public void onInitializeClient() {
        loadConfig();
    }

    public static boolean isTargetItem(net.minecraft.item.ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        String id = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).toString();
        return disabledItems.contains(id);
    }

    public static boolean shouldLockHand() {
        return enabled && lockHandPosition;
    }

    public static boolean shouldLockItem() {
        return enabled && lockItemPosition;
    }

    public static void loadConfig() {
        Properties properties = new Properties();

        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                properties.load(reader);
            } catch (IOException exception) {
                System.err.println("[NoAnimationX] Could not read config: " + exception.getMessage());
            }
        }

        enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));
        lockItemPosition = Boolean.parseBoolean(properties.getProperty("lockItemPosition", "true"));
        lockHandPosition = Boolean.parseBoolean(properties.getProperty("lockHandPosition", "true"));

        String configuredItems = properties.getProperty(
                "disabledAnimations",
                String.join(",", DEFAULT_ITEMS)
        );

        disabledItems = Arrays.stream(configuredItems.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                properties.setProperty("enabled", Boolean.toString(enabled));
                properties.setProperty("lockItemPosition", Boolean.toString(lockItemPosition));
                properties.setProperty("lockHandPosition", Boolean.toString(lockHandPosition));
                properties.setProperty("disabledAnimations", String.join(",", disabledItems));
                properties.store(writer, "NoAnimationX client settings");
            }
        } catch (IOException exception) {
            System.err.println("[NoAnimationX] Could not write config: " + exception.getMessage());
        }
    }
}
