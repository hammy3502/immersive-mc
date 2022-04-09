package net.blf02.immersivemc.client.immersive;

import net.blf02.immersivemc.client.config.ClientConfig;
import net.blf02.immersivemc.client.immersive.info.AbstractTileEntityImmersiveInfo;
import net.blf02.immersivemc.common.network.Network;
import net.blf02.immersivemc.common.network.packet.FetchInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractTileEntityImmersive<T extends TileEntity, I extends AbstractTileEntityImmersiveInfo<T>>
    extends AbstractImmersive<I> {

    /**
     * Get a new instance of info to track.
     *
     * @param tileEnt Tile Entity that the info contains
     * @return The instance
     */
    public abstract I getNewInfo(T tileEnt);

    public abstract int getTickTime();

    public abstract boolean shouldRender(I info, boolean isInVR);

    @Override
    public void tick(I info, boolean isInVR) {
        super.tick(info, isInVR);
        if (info.getTileEntity() instanceof IInventory) {
            if (ThreadLocalRandom.current().nextInt(ClientConfig.inventorySyncTime) == 0) {
                Network.INSTANCE.sendToServer(new FetchInventoryPacket(info.getBlockPosition()));
            }
        }

        if (Minecraft.getInstance().player != null && info.getTileEntity() != null &&
                Minecraft.getInstance().player.distanceToSqr(Vector3d.atCenterOf(info.getTileEntity().getBlockPos())) >
                ClientConfig.distanceSquaredToRemoveImmersive) {
            info.remove();
        }

    }

    // EVERYTHING ABOVE MUST BE OVERRIDEN, AND HAVE SUPER() CALLED IF APPLICABLE!

    /**
     * Can be overriden as a final check before tracking an object.
     * @param tileEnt Tile entity to check one last time before possibly tracking
     * @return Whether or not we should track this
     */
    public boolean shouldTrack(T tileEnt) {
        return true;
    }

    public void trackObject(T tileEnt) {
        for (I info : getTrackedObjects()) {
            if (info.getTileEntity() == tileEnt) {
                info.setTicksLeft(getTickTime());
                return;
            }
        }
        if (shouldTrack(tileEnt)) infos.add(getNewInfo(tileEnt));
    }
}
