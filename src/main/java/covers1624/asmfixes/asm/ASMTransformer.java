package covers1624.asmfixes.asm;

import codechicken.lib.asm.ASMBlock;
import codechicken.lib.asm.ASMReader;
import codechicken.lib.asm.ModularASMTransformer;
import codechicken.lib.asm.ModularASMTransformer.MethodInjector;
import codechicken.lib.asm.ModularASMTransformer.MethodReplacer;
import codechicken.lib.asm.ObfMapping;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import java.util.Map;

/**
 * Created by covers1624 on 3/7/2016.
 */
public class ASMTransformer implements IClassTransformer {
    private ModularASMTransformer transformer = new ModularASMTransformer();
    private Map<String, ASMBlock> asmBlocks;
    private static String potionEffectClass = "net/minecraft/potion/PotionEffect";

    public ASMTransformer() {
        Class clazz;
        try {
            clazz = Launch.classLoader.findClass("TempAccessor");
            if (clazz == null) {
                throw new NullPointerException("Failed to find class or class was null!");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        asmBlocks = ASMReader.loadResource(clazz.getResourceAsStream("/blocks.asm"), "/blocks.asm");

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            transformer.add(new MethodInjector(new ObfMapping("net/minecraft/potion/PotionEffect", "func_76455_a", "(Lnet/minecraft/entity/EntityLivingBase;)Z"), asmBlocks.get("potionFix"), true));
        }
        transformer.add(new MethodReplacer(new ObfMapping("net/machinemuse/numina/recipe/SimpleItemMaker", "getRecipeOutput", "()Lnet/minecraft/item/ItemStack;"), asmBlocks.get("n_museLogSpam"), new ASMBlock()));
        transformer.add(new MethodReplacer(new ObfMapping("net/machinemuse/numina/recipe/SimpleItemMatcher", "matchesItem", "(Lnet/minecraft/item/ItemStack;)Z"), asmBlocks.get("n_museLogSpam"), new ASMBlock()));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        bytes = transformer.transform(name, bytes);

        return bytes;
    }
}
