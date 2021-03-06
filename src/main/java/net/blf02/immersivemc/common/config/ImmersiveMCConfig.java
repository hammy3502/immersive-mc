package net.blf02.immersivemc.common.config;

import net.blf02.immersivemc.common.network.Network;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.ForgeConfigSpec;

public class ImmersiveMCConfig {

    public static final int CONFIG_VERSION = 1; // Increment post-release whenever the config changes

    public static final ForgeConfigSpec GENERAL_SPEC;

    // Synced values
    public static ForgeConfigSpec.BooleanValue useAnvilImmersion;
    public static ForgeConfigSpec.BooleanValue useBrewingImmersion;
    public static ForgeConfigSpec.BooleanValue useChestImmersion;
    public static ForgeConfigSpec.BooleanValue useCraftingImmersion;
    public static ForgeConfigSpec.BooleanValue useFurnaceImmersion;
    public static ForgeConfigSpec.BooleanValue useJukeboxImmersion;
    public static ForgeConfigSpec.BooleanValue useRangedGrab;
    public static ForgeConfigSpec.BooleanValue useButton;
    public static ForgeConfigSpec.BooleanValue useETableImmersion;
    public static ForgeConfigSpec.BooleanValue useCampfireImmersion;
    public static ForgeConfigSpec.BooleanValue useLever;
    public static ForgeConfigSpec.BooleanValue useBackpack;
    public static ForgeConfigSpec.BooleanValue useRepeaterImmersion;

    //Non-synced values
    public static ForgeConfigSpec.IntValue backpackColor;
    public static ForgeConfigSpec.BooleanValue leftHandedBackpack;
    public static ForgeConfigSpec.BooleanValue rightClickChest;
    public static ForgeConfigSpec.BooleanValue autoCenterFurnace;
    public static ForgeConfigSpec.BooleanValue autoCenterBrewing;
    public static ForgeConfigSpec.BooleanValue useLowDetailBackpack;
    public static ForgeConfigSpec.BooleanValue showPlacementGuide;
    public static ForgeConfigSpec.IntValue itemPlacementMode;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    protected static void setupConfig(ForgeConfigSpec.Builder builder) {
        // Synced Values
        useAnvilImmersion = builder
                .comment("Whether immersives on anvils and smithing tables should be allowed")
                .define("anvil_immersion", true);
        useBrewingImmersion = builder
                .comment("Whether immersives on brewing stands should be allowed")
                .define("brewing_immersion", true);
        useChestImmersion = builder
                .comment("Whether immersives on all types of chests should be allowed. Unless users enable right_click_chest, this is VR only.")
                .define("chest_immersion", true);
        useCraftingImmersion = builder
                .comment("Whether immersives on crafting tables should be allowed")
                .define("crafting_immersion", true);
        useFurnaceImmersion = builder
                .comment("Whether immersives on furnaces should be allowed")
                .define("furnace_immersion", true);
        useJukeboxImmersion = builder
                .comment("Whether immersives on jukeboxes should be allowed (VR only)")
                .define("jukebox_immersion", true);
        useRangedGrab = builder
                .comment("Allow VR users to grab items at a distance.")
                .define("ranged_grab", true);
        useButton = builder
                .comment("Whether VR users can physically push buttons")
                .define("button_immersion", true);
        useETableImmersion = builder
                .comment("Whether immersives on Enchanting Tables should be allowed")
                .define("enchant_table_immersion", true);
        useCampfireImmersion = builder
                .comment("Whether VR users can hold items above a campfire to cook them")
                .define("campfire_immersion", true);
        useLever = builder
                .comment("Whether VR users can physically toggle levers")
                .define("lever_immersion", true);
        useBackpack = builder
                .comment("Allow VR players to use a bag to manage their inventory")
                .define("bag_inventory", true);
        useRepeaterImmersion = builder
                .comment("Whether VR users can adjust the delay of repeaters using their hands")
                .define("repeater_immersion", true);

        // Non-synced Values
        backpackColor = builder
                .comment("Color for the bag as a base-10 RGB number.")
                .defineInRange("bag_color", 11901820, 0, 0xFFFFFF);
        leftHandedBackpack = builder
                .comment("Puts the bag on the other side of your arm. Set to true if you're left-handed.")
                .define("left_handed_bag", false);
        rightClickChest = builder
                .comment("Allows for right-clicking chests to use their immersive. Works outside of VR!")
                .define("right_click_chest", false);
        autoCenterFurnace = builder
                .comment("Makes the furnace immersive more centered instead of similar to the vanilla GUI")
                .define("center_furnace", false);
        autoCenterBrewing = builder
                .comment("Makes the brewing stand more centered instead of similar to the vanilla GUI")
                .define("center_brewing", false);
        useLowDetailBackpack = builder
                .comment("Use lower-detailed bag")
                .define("low_detail_bag", false);
        showPlacementGuide = builder
                .comment("Whether to show a particle for where to place items")
                .define("show_placement_guide", true);
        itemPlacementMode = builder
                .comment("Integer representation for the mode to use when placing items using ImmersiveMC")
                .defineInRange("placement_mode", 0, 0, PlacementMode.values().length - 1);
    }

    public static void encode(PacketBuffer buffer) {
        buffer.writeInt(Network.PROTOCOL_VERSION).writeInt(CONFIG_VERSION)
                .writeBoolean(useAnvilImmersion.get())
                .writeBoolean(useBrewingImmersion.get())
                .writeBoolean(useChestImmersion.get()).writeBoolean(useCraftingImmersion.get())
                .writeBoolean(useFurnaceImmersion.get()).writeBoolean(useJukeboxImmersion.get())
                .writeBoolean(useRangedGrab.get())
                .writeBoolean(useButton.get())
                .writeBoolean(useETableImmersion.get())
                .writeBoolean(useCampfireImmersion.get())
                .writeBoolean(useLever.get())
                .writeBoolean(useBackpack.get())
                .writeBoolean(useRepeaterImmersion.get());
    }

    public static void resetToDefault() {
        // Synced defaults
        useAnvilImmersion.set(true);
        useBrewingImmersion.set(true);
        useChestImmersion.set(true);
        useCraftingImmersion.set(true);
        useFurnaceImmersion.set(true);
        useJukeboxImmersion.set(true);
        useRangedGrab.set(true);
        useButton.set(true);
        useETableImmersion.set(true);
        useCampfireImmersion.set(true);
        useLever.set(true);
        useBackpack.set(true);
        useRepeaterImmersion.set(true);

        // Non-synced defaults
        backpackColor.set(11901820);
        leftHandedBackpack.set(false);
        rightClickChest.set(false);
        autoCenterFurnace.set(false);
        autoCenterBrewing.set(false);
        useLowDetailBackpack.set(false);
        showPlacementGuide.set(true);
        itemPlacementMode.set(0);

    }


}
