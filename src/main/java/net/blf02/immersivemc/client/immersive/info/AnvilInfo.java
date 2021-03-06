package net.blf02.immersivemc.client.immersive.info;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Arrays;

public class AnvilInfo extends AbstractWorldStorageInfo {
    public Direction renderDirection;
    public boolean isReallyAnvil;

    public Vector3d textPos = null;
    public Direction lastDir = null;

    public int anvilCost = 0;

    public AnvilInfo(BlockPos pos, int ticksToExist) {
        super(pos, ticksToExist, 2);
    }

    @Override
    public void setInputSlots() {
        this.inputHitboxes = Arrays.copyOfRange(hitboxes, 0, 2);
    }

    @Override
    public boolean readyToRender() {
        return this.hasPositions() && this.hasHitboxes()
                 && this.renderDirection != null;
    }
}
