# NoAnimationX

Client-side Fabric mod for Minecraft 1.21.11.

## Features in this prototype

- Default target items: Wind Charge, Ender Pearl, End Crystal.
- Sets first-person swing progress to zero for a target item.
- Sets equip progress to its fully equipped value while a target item is held, preventing the equip transition from moving the rendered hand/item.
- Target item IDs and the two lock toggles are configured in `config/noanimationx.properties`.

## Development environment

- JDK 21
- Minecraft 1.21.11
- Fabric Loader 0.18.1
- Fabric Loom Remap 1.14.10
- Yarn mappings 1.21.11+build.4
- Gradle 9.2.1 or a compatible Gradle 9.x release

Open the folder containing `build.gradle` in IntelliJ IDEA as a Gradle project. Set the Gradle JVM to JDK 21. If IntelliJ asks for a Gradle installation, select Gradle 9.2.1.

Run the Gradle `build` task. The distributable remapped mod JAR is produced under `build/libs/`.

## Configuration

The mod creates `config/noanimationx.properties` on first launch:

```properties
enabled=true
lockItemPosition=true
lockHandPosition=true
disabledAnimations=minecraft:wind_charge,minecraft:ender_pearl,minecraft:end_crystal
```

Add valid item registry IDs to `disabledAnimations`, separated by commas. Restart the game after editing the file.

## Current limitations

This is an initial source build, not a verified release. The renderer mixin uses the 1.21.11 Yarn `HeldItemRenderer.renderFirstPersonItem` descriptor. Its argument ordinals and in-game behavior still need to be verified in a local Gradle build and client launch. Target detection currently checks both hands, so when a target item is in either hand, the swing/equip progress changes can affect both first-person hand render calls.
