package online.calamitycraft.serverchat.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLightningBolt;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.EntityTNT;
import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.entity.monster.*;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.entity.projectile.EntityArrowGolden;
import net.minecraft.core.entity.projectile.EntityCannonball;
import net.minecraft.core.entity.projectile.EntityFireball;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.ServerChatMod;

import java.util.concurrent.Future;

public class DeathMessageUtil {
    private static String generateDeathMessage(EntityPlayer player, Entity killer) {
        I18n trans = I18n.getInstance();
        try {
            if (killer instanceof EntityZombie) {
                String name = "Zombie";
                if (killer instanceof EntityPigZombie) {
                    name += " Pigman";
                }
                EntityZombie z = (EntityZombie) killer;
                if (ServerChatMod.isHoldingItem(z)) {
                    return formatDeathMessage(player.getDisplayName(), name, trans.translateKey(z.getHeldItem().getItemTranslateKey()), "[victim] fell victim to [killer]'s [weapon].");
                } else {
                    return formatDeathMessage(player.getDisplayName(), name, "[victim]'s life was ended by [killer].");
                }
            }
            if (killer instanceof EntitySpider) {
                EntitySpider z = (EntitySpider) killer;
                return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), "[victim] was bitten by [killer].");
            }
            if (killer instanceof EntitySlime) {
                EntitySlime z = (EntitySlime) killer;
                return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), "[victim] was drowned inside of a gelatin [killer].");
            }
            if (killer instanceof EntitySkeleton) {
                EntitySkeleton z = (EntitySkeleton) killer;
                if (ServerChatMod.isHoldingItem(z)) {
                    return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), trans.translateKey(z.getHeldItem().getItemTranslateKey()), "[victim] was shot by [killer]'s [weapon].");
                } else {
                    return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), "[victim]'s life was ended by [killer].");
                }
            }
            if (killer instanceof EntityGhast) {
                EntityGhast z = (EntityGhast) killer;
                return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), "[victim] was blown to bits by [killer].");
            }
            if (killer instanceof EntityWolf) {
                EntityWolf z = (EntityWolf) killer;
                return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), "[victim] was relentlessly mauled by [killer].");
            }
            if (killer instanceof EntityFireball) {
                return formatDeathMessage(player.getDisplayName(), "Ghast's", "Fireball", "[killer] [weapon] blew [victim] to bits.");
            }
            if (killer instanceof EntityGiant) {
                EntityGiant z = (EntityGiant) killer;
                return formatDeathMessage(player.getDisplayName(), killer.getClass().getSimpleName().replace("Entity", ""), "[victim] was crushed on by huge [killer].");
            }
            if (killer instanceof EntityLightningBolt) {
                return formatDeathMessage(player.getDisplayName(), "Lightning Bolt", "[killer] sent 3,000 kV through [victim]'s brain.");
            }
            if (killer instanceof EntityTNT) {
                return formatDeathMessage(player.getDisplayName(), "TNT", "[victim] was blown to bits by [killer].");
            }
            if (killer instanceof EntityPlayerMP) {
                EntityPlayerMP z = (EntityPlayerMP) killer;
                if (z.getDisplayName().equals(player.getDisplayName())) {
                    if (ServerChatMod.isHoldingItem(z)) {
                        return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), trans.translateKey(z.getHeldItem().getItemTranslateKey()), "[victim] put themselves out of their misery with [weapon].");
                    } else {
                        return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), "[victim] put themselves out of their misery");
                    }
                }
                if (ServerChatMod.isHoldingItem(z)) {
                    return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), trans.translateKey(z.getHeldItem().getItemTranslateKey()), "[killer] obliterated [victim] with [weapon].");
                } else {
                    return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), "[victim] was beat to death by [killer].");
                }
            }


            if (killer instanceof EntityArrow) {
                if (((EntityArrowGolden) killer).owner != null) {
                    EntityLiving z = ((EntityArrowGolden) killer).owner;
                    if (z.getDisplayName().equals(player.username)) {
                        if (ServerChatMod.isHoldingItem(player)) {
                            return formatDeathMessage(player.getDisplayName(), player.getDisplayName(), trans.translateKey(player.getHeldItem().getItemTranslateKey()), "[victim] shot themselves with [weapon]."); // suicide with named item
                        }
                        return formatDeathMessage(player.getDisplayName(), "[victim] shot themselves."); // suicide
                    }
                    if (ServerChatMod.isHoldingItem(z)) {
                        return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), trans.translateKey(z.getHeldItem().getItemTranslateKey()), "[victim] was shot by [killer]'s [weapon]."); // shot by another player
                    }
                    return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), "[victim] was shot by [killer]."); // shot by another player

                }
                return formatDeathMessage(player.getDisplayName(), "[victim] was silently assassinated."); // don't know who shot the arrow
            }

            if (killer instanceof EntityCannonball) {
                if (((EntityCannonball) killer).owner instanceof EntityPlayerMP) {
                    EntityLiving z = ((EntityCannonball) killer).owner;
                    if (z.getDisplayName().equals(player.username)) {
                        if (ServerChatMod.isHoldingItem(player)) {
                            return formatDeathMessage(player.getDisplayName(), player.getDisplayName(), trans.translateKey(player.getHeldItem().getItemTranslateKey()), "[victim] blew themselves up with [weapon]."); // suicide with named item
                        }
                        return formatDeathMessage(player.getDisplayName(), "[victim] blew themselves up."); // suicide
                    }
                    if (ServerChatMod.isHoldingItem(z)) {
                        return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), trans.translateKey(z.getHeldItem().getItemTranslateKey()), "[victim] was blown up by [killer]'s [weapon]."); // shot by another player
                    }
                    return formatDeathMessage(player.getDisplayName(), z.getDisplayName(), "[victim] was blown up by [killer]."); // shot by another entity

                }
                return formatDeathMessage(player.getDisplayName(), "[victim] was blown up."); // don't know who shot the cannonball
            }


            if (player.world.getBlockMaterial(MathHelper.floor_double(player.x), MathHelper.floor_double(player.y), MathHelper.floor_double(player.z)) == Material.lava) {
                return formatDeathMessage(player.getDisplayName(), "Lava", "[victim] was fried alive in [killer].");
            }
            if (player.world.getBlockId(MathHelper.floor_double(player.x), MathHelper.floor_double(player.y), MathHelper.floor_double(player.z)) == Block.spikes.id) {
                return formatDeathMessage(player.getDisplayName(), "Spike Trap", "[victim] was penetrated to death by [killer].");
            }
            if (player.fallDistance > 0f && player.fallDistance < 5f) {
                return formatDeathMessage(player.getDisplayName(), "[victim] fell face-flat onto the ground.");
            }
            if (player.fallDistance > 5f) {
                return formatDeathMessage(player.getDisplayName(), "[victim] fell from extreme heights.");
            }
            if (player.airSupply <= 0) {
                return formatDeathMessage(player.getDisplayName(), "[victim] is sleeping with the fishes.");
            }
            if (player.remainingFireTicks > 0) {
                return formatDeathMessage(player.getDisplayName(), "[victim] was charred to a crisp.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatDeathMessage(player.getDisplayName(), "[victim] suffered an average British death.");
    }

    public static Future<String> generateDeathMessageAsync(EntityPlayer player, Entity killer) {
        return Threads.deathMessageExecutor.submit(() -> generateDeathMessage(player, killer));
    }

    public static String formatDeathMessage(String victim, String killer, String weapon, String format) {
        TextFormatting red = TextFormatting.RED;
        TextFormatting aqua = TextFormatting.LIGHT_BLUE;
        TextFormatting gold = TextFormatting.ORANGE;
        String message = (red + format).trim();
        message = message.replace("[victim]", aqua + victim + red);
        if (killer != null) {
            message = message.replace("[killer]", aqua + killer + red);
            if (weapon != null) {
                message = message.replace("[weapon]", gold + weapon + red);
            }
        }
        return message;
    }

    public static String formatDeathMessage(String victim, String killer, String format) {
        return formatDeathMessage(victim, killer, null, format);
    }

    public static String formatDeathMessage(String victim, String format) {
        return formatDeathMessage(victim, null, format);
    }
}
