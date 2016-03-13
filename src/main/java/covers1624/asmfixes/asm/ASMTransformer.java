package covers1624.asmfixes.asm;

import codechicken.lib.asm.ASMBlock;
import codechicken.lib.asm.ASMReader;
import codechicken.lib.asm.ModularASMTransformer;
import codechicken.lib.asm.ModularASMTransformer.MethodInjector;
import codechicken.lib.asm.ModularASMTransformer.MethodReplacer;
import codechicken.lib.asm.ObfMapping;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import java.util.Map;

/**
 * Created by covers1624 on 3/7/2016.
 */
public class ASMTransformer implements IClassTransformer {
    private ModularASMTransformer commonTransformer = new ModularASMTransformer();
    private ModularASMTransformer clientTransformer = new ModularASMTransformer();
    private boolean hasInit = false;
    private Map<String, ASMBlock> asmBlocks;

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
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!hasInit) {
            initCommonTransformers();
            if (FMLLaunchHandler.side().isClient()) {
                initClientTransformers();
            }
            hasInit = true;
        }

        if (FMLLaunchHandler.side().isClient()) {
            bytes = clientTransformer.transform(name, bytes);
        }

        bytes = commonTransformer.transform(name, bytes);

        return bytes;
    }

    private void initClientTransformers() {
        clientTransformer.add(new MethodInjector(new ObfMapping("net/minecraft/potion/PotionEffect", "func_76455_a", "(Lnet/minecraft/entity/EntityLivingBase;)Z"), asmBlocks.get("potionFix"), true));
        FMLLog.info("Loading Client Transformers.");
    }

    private void initCommonTransformers() {
        commonTransformer.add(new MethodReplacer(new ObfMapping("net/machinemuse/numina/recipe/SimpleItemMaker", "getRecipeOutput", "()Lnet/minecraft/item/ItemStack;"), asmBlocks.get("n_museLogSpam"), new ASMBlock()));
        commonTransformer.add(new MethodReplacer(new ObfMapping("net/machinemuse/numina/recipe/SimpleItemMatcher", "matchesItem", "(Lnet/minecraft/item/ItemStack;)Z"), asmBlocks.get("n_museLogSpam"), new ASMBlock()));
        FMLLog.info("Loading Common Transformers.");
    }

}
