package covers1624.asmfixes.asm;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by covers1624 on 3/7/2016.
 */
public class ASMHooks {

    public static boolean potionOnUpdateHook(PotionEffect effect, EntityLivingBase entityLivingBase) {
        if (effect.duration > 0) {
            try {
                if (Potion.potionTypes[effect.potionID].isReady(effect.duration, effect.amplifier)){
                    effect.performEffect(entityLivingBase);
                }
            } catch (Throwable ignored) {
                FMLLog.bigWarning("Found null effect, Removing from player.");
                Minecraft.getMinecraft().thePlayer.removePotionEffectClient(effect.getPotionID());
            }
            effect.deincrementDuration();
        }
        return effect.duration > 0;
    }

}
