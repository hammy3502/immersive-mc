package net.blf02.immersivemc.client.immersive;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blf02.immersivemc.client.config.ClientConfig;
import net.blf02.immersivemc.client.immersive.info.AbstractImmersiveInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract immersive anything
 * @param <I> Info type
 */
public abstract class AbstractImmersive<I extends AbstractImmersiveInfo> {

    protected List<I> infos = new LinkedList<>();

    public abstract boolean shouldHandleImmersion(I info);

    protected void handleImmersion(I info, MatrixStack stack) {
        // Set the cooldown (transition time) based on how long we've existed or until we stop existing
        if (info.getCountdown() > 1 && info.getTicksLeft() > 20) {
            info.changeCountdown(-1);
        } else if (info.getCountdown() < ClientConfig.transitionTime && info.getTicksLeft() <= 20) {
            info.changeCountdown(1);
        }
    }

    // Below this line are utility functions. Everything above MUST be overwritten, and have super() called!

    /**
     * Handle immersion.
     *
     * This is the method that should be called by outside functions.
     */
    public void doImmersion(I info, MatrixStack stack) {
        if (shouldHandleImmersion(info)) {
            handleImmersion(info, stack);
        }
    }

    public List<I> getTrackedObjects() {
        return infos;
    }

    /**
     * Renders an item at the specified position
     * @param item Item to render
     * @param stack MatrixStack in
     * @param pos Position to render at
     * @param size Size to render at
     * @param facing Direction to face (should be the direction of the block)
     * @param hitbox Hitbox for debug rendering
     */
    public void renderItem(ItemStack item, MatrixStack stack, Vector3d pos, float size, Direction facing,
                           AxisAlignedBB hitbox) {
        if (item != ItemStack.EMPTY) {
            stack.pushPose();

            // Move the stack to be relative to the camera
            ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getMainCamera();
            stack.translate(-renderInfo.getPosition().x + pos.x,
                    -renderInfo.getPosition().y + pos.y,
                    -renderInfo.getPosition().z + pos.z);

            // Scale the item to be a good size
            stack.scale(size, size, size);

            // Rotate the item to face the player properly
            int degreesRotation = 0; // If North, we're already good
            if (facing == Direction.WEST) {
                degreesRotation = 90;
            } else if (facing == Direction.SOUTH) {
                degreesRotation = 180;
            } else if (facing == Direction.EAST) {
                degreesRotation = 270;
            }

            stack.mulPose(Vector3f.YP.rotationDegrees(degreesRotation));

            Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemCameraTransforms.TransformType.FIXED,
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    stack, Minecraft.getInstance().renderBuffers().bufferSource());

            stack.popPose();

            if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes() &&
                    hitbox != null) {
                // Use a new stack here, so we don't conflict with the stack.scale() for the item itself
                stack.pushPose();
                stack.translate(-renderInfo.getPosition().x + pos.x,
                        -renderInfo.getPosition().y + pos.y,
                        -renderInfo.getPosition().z + pos.z);
                OutlineLayerBuffer buffer = Minecraft.getInstance().renderBuffers().outlineBufferSource();
                buffer.setColor(255, 255, 255, 255);
                WorldRenderer.renderLineBox(stack, buffer.getBuffer(RenderType.LINES),
                        hitbox.move(-pos.x, -pos.y, -pos.z),
                        1, 1, 1, 1);
                stack.popPose();
            }
        }
    }

    /**
     * Gets the direction to the left from the Direction's perspective, assuming the Direction is
     * looking at the player. This makes it to the right for the player.
     * @param forward Forward direction of the block
     * @return The aforementioned left
     */
    public Direction getLeftOfDirection(Direction forward) {
        if (forward == Direction.UP || forward == Direction.DOWN) {
            throw new IllegalArgumentException("Direction cannot be up or down!");
        }
        if (forward == Direction.NORTH) {
            return Direction.WEST;
        } else if (forward == Direction.WEST) {
            return Direction.SOUTH;
        } else if (forward == Direction.SOUTH) {
            return Direction.EAST;
        }
        return Direction.NORTH;
    }

    /**
     * Gets the position precisely to the front-right of pos given the direction forward.
     *
     * This is effectively the bottom right of the front face from the block's perspective, or the bottom left
     * from the player's perspective facing the block.
     *
     * @param forwardFromBlock Direction forward from block. Can also be opposite direction of player
     * @param pos BlockPos of block
     * @return Vector3d of the front-right of the block face from the block's perspective (front left from the player's)
     */
    public Vector3d getDirectlyInFront(Direction forwardFromBlock, BlockPos pos) {
        // This mess sets pos to always be directly in front of the face of the tile entity
        if (forwardFromBlock == Direction.SOUTH) {
            BlockPos front = pos.relative(forwardFromBlock);
            return new Vector3d(front.getX(), front.getY(), front.getZ());
        } else if (forwardFromBlock == Direction.WEST) {
            BlockPos front = pos;
            return new Vector3d(front.getX(), front.getY(), front.getZ());
        } else if (forwardFromBlock == Direction.NORTH) {
            BlockPos front = pos.relative(Direction.EAST);
            return new Vector3d(front.getX(), front.getY(), front.getZ());
        } else if (forwardFromBlock == Direction.EAST) {
            BlockPos front = pos.relative(Direction.SOUTH).relative(Direction.EAST);
            return new Vector3d(front.getX(), front.getY(), front.getZ());
        } else {
            throw new IllegalArgumentException("Furnaces can't point up or down?!?!");
        }
    }

    /**
     * Gets the forward direction of the block based on the player
     *
     * Put simply, this returns the opposite of the Direction the player is currently facing (only N/S/E/W)
     * @param player Player to get forward from
     * @return The forward direction of a block to use.
     */
    public Direction getForwardFromPlayer(PlayerEntity player) {
        return player.getDirection().getOpposite();
    }

    /**
     * Creates the hitbox for an item.
     *
     * Practically identical to how hitboxes are created manually in ImmersiveFurnace
     * @param pos Position for center of hitbox
     * @param size Size of hitbox
     * @return
     */
    public AxisAlignedBB createHitbox(Vector3d pos, float size) {
        return new AxisAlignedBB(
                pos.x - size,
                pos.y - size,
                pos.z - size,
                pos.x + size,
                pos.y + size,
                pos.z + size);
    }

}