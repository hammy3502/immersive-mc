package net.blf02.immersivemc.common.config;

import net.minecraft.network.PacketBuffer;

public class ServerPlayerConfig {

    public static final ServerPlayerConfig EMPTY_CONFIG = new ServerPlayerConfig(false,
            false, false, false);

    public boolean useButtons;
    public boolean useCampfire;
    public boolean useLevers;
    public boolean useRangedGrab;

    public ServerPlayerConfig(boolean useButtons, boolean useCampfire, boolean useLevers,
                              boolean useRangedGrab) {
        this.useButtons = useButtons;
        this.useCampfire = useCampfire;
        this.useLevers = useLevers;
        this.useRangedGrab = useRangedGrab;
    }

    public ServerPlayerConfig(PacketBuffer buffer) {
        this(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }
}
