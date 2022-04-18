package net.blf02.immersivemc.client.config;

public class ClientConstants {

    // How long the overlay should be displayed
    public static final int ticksToRenderFurnace = 80;
    public static final int ticksToRenderBrewing = 120;
    public static final int ticksToRenderCrafting = 120;
    public static final int ticksToHandleJukebox = 100;
    public static final int ticksToRenderChest = Integer.MAX_VALUE; // You manually close a chest
    public static final int ticksToRenderAnvil = 120;

    // Size of items when rendered in front of something immersive
    public static final float itemScaleSizeFurnace = 0.5f;
    public static final float itemScaleSizeBrewing = 1f/3f;
    public static final float itemScaleSizeCrafting = 3f/16f; // Chosen for the texture of the table itself
    public static final float itemScaleSizeChest = 0.25f;
    public static final float itemScaleSizeAnvil = itemScaleSizeBrewing;

    // Time to transition in ticks
    public static final int transitionTime = 20;

    // Amount of ticks between syncs (sync every `inventorySyncTime` ticks)
    public static final int inventorySyncTime = 2;

    // For Debugging Only!!!!one!
    public static final boolean vrInteractionsOutsideVR = false; // Doesn't apply for all VR-only interactions!
}
