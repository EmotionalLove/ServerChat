package online.calamitycraft.serverchat.mixin;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.save.SaveHandlerBase;
import net.minecraft.core.world.save.SaveHandlerServer;
import online.calamitycraft.serverchat.util.UuidUtil;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

@Mixin(value = SaveHandlerServer.class, remap = false)
public abstract class SaveHandlerServerMixin extends SaveHandlerBase {


    @Shadow @Final private static Logger logger;

    public SaveHandlerServerMixin(ISaveFormat saveFormat, File savesDir, String worldDirName, boolean isMultiplayer) {
        super(saveFormat, savesDir, worldDirName, isMultiplayer);
    }

    /**
     * @author CalamityCraft
     * @reason Use UUID to save playerdata instead of username
     */
    @Overwrite
    private CompoundTag getPlayerData(String s) {
        try {
            String uid = UuidUtil.getUUID(s);
            File file = new File(new File(this.getSaveDirectory(), "players"), uid + ".dat");
            if (file.exists()) {
                return NbtIo.readCompressed(Files.newInputStream(file.toPath()));
            } else {
                File legacy = new File(new File(this.getSaveDirectory(), "players"), s + ".dat");
                if (legacy.exists()) {
                    CompoundTag nbt = NbtIo.readCompressed(Files.newInputStream(legacy.toPath()));
                    boolean converted = legacy.renameTo(file);
                    if (converted) {
                        logger.info("converted " + s + ".dat to " + uid + ".dat");
                    } else {
                        logger.warn("could not convert " + s + ".dat");
                    }
                    return nbt;
                }
            }
        } catch (Exception exception) {
            logger.warn("Failed to load player data for " + s);
        }
        return null;
    }

    /**
     * @author CalamityCraft
     * @reason Use UUID to save playerdata instead of username
     */
    @Overwrite
    public void writePlayerData(EntityPlayer entityplayer) {
        File playersDirectory = new File(this.getSaveDirectory(), "players");
        String uuid = UuidUtil.getUUID(entityplayer.getDisplayName());
        try {
            CompoundTag nbttagcompound = new CompoundTag();
            entityplayer.saveWithoutId(nbttagcompound);
            File file = new File(playersDirectory, "_tmp_.dat");
            File file1 = new File(playersDirectory, uuid + ".dat");
            NbtIo.writeCompressed(nbttagcompound, Files.newOutputStream(file.toPath()));
            if (file1.exists()) {
                file1.delete();
            }
            file.renameTo(file1);
        } catch (Exception exception) {
            logger.warn("Failed to save player data for " + entityplayer.username);
        }
    }

}
