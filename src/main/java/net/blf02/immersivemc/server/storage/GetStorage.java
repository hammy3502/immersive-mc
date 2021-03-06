package net.blf02.immersivemc.server.storage;

import net.blf02.immersivemc.common.storage.AnvilStorage;
import net.blf02.immersivemc.common.storage.ImmersiveStorage;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import java.util.List;

public class GetStorage {

    public static ImmersiveStorage assembleStorage(CompoundNBT nbt, String storageType, WorldSavedData wStorage) {
        // Check storage type, and load storage accordingly
        ImmersiveStorage storage = null;
        if (storageType.equals(ImmersiveStorage.TYPE)) {
            storage = new ImmersiveStorage(wStorage);
            storage.load(nbt);
        } else if (storageType.equals(AnvilStorage.TYPE)) {
            storage = new AnvilStorage(wStorage);
            storage.load(nbt);
        }

        if (storage == null) {
            throw new IllegalArgumentException("Storage type " + storageType + " does not exist!");
        }
        return storage;
    }

    public static int getLastInputIndex(BlockState state) {
        if (state.getBlock() == Blocks.CRAFTING_TABLE) {
            return 8;
        } else if (state.getBlock() instanceof AnvilBlock || state.getBlock() instanceof SmithingTableBlock) {
            return 1;
        } else if (state.getBlock() instanceof EnchantingTableBlock) {
            return 0;
        }
        return -1;
    }

    public static ImmersiveStorage getPlayerStorage(PlayerEntity player, String type) {
        List<ImmersiveStorage> storages = PlayerStorage.getStorages(player);
        for (ImmersiveStorage storage : storages) {
            if (storage.identifier.equals("backpack")) {
                return storage;
            }
        }
        if (type.equals("backpack")) {
            ImmersiveStorage storage = new ImmersiveStorage(PlayerStorage.getPlayerStorage(player)).initIfNotAlready(5);
            storage.identifier = "backpack";
            PlayerStorage.getStorages(player).add(storage);
            PlayerStorage.getPlayerStorage(player).setDirty();
            return storage;
        }
        throw new IllegalArgumentException("Invalid player storage type!");
    }

    public static ImmersiveStorage getStorage(PlayerEntity player, BlockPos pos) {
        BlockState state = player.level.getBlockState(pos);
        if (state.getBlock() == Blocks.CRAFTING_TABLE) {
            return getCraftingStorage(player, pos);
        } else if (state.getBlock() instanceof AnvilBlock || state.getBlock() instanceof SmithingTableBlock) {
            return getAnvilStorage(player, pos);
        } else if (state.getBlock() instanceof EnchantingTableBlock) {
            return getEnchantingStorage(player, pos);
        }
        return null;
    }

    public static ImmersiveStorage getEnchantingStorage(PlayerEntity player, BlockPos pos) {
        return WorldStorage.getStorage(player).getOrCreate(pos).initIfNotAlready(1);
    }

    public static AnvilStorage getAnvilStorage(PlayerEntity player, BlockPos pos) {
        WorldStorage wStorage = WorldStorage.getStorage(player);
        ImmersiveStorage storageOld = wStorage.get(pos);
        AnvilStorage storage;
        if (!(storageOld instanceof AnvilStorage)) {
            storage = new AnvilStorage(wStorage);
            storage.initIfNotAlready(3);
            WorldStorage.getStorage(player).add(pos, storage);
        } else {
            storage = (AnvilStorage) storageOld;
        }
        return storage;
    }

    public static ImmersiveStorage getCraftingStorage(PlayerEntity player, BlockPos pos) {
        return WorldStorage.getStorage(player).getOrCreate(pos).initIfNotAlready(10);
    }
}
