package covers1624.asmfixes.asm;

import codechicken.lib.asm.*;
import codechicken.lib.asm.ModularASMTransformer.MethodInjector;
import codechicken.lib.asm.ModularASMTransformer.MethodReplacer;
import covers1624.asmfixes.config.Config;
import covers1624.asmfixes.util.LogHelper;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
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
        asmBlocks = ASMReader.loadResource("/assets/asmfixes/asm/blocks.asm");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (!hasInit) {
            initCommonTransformers();
            if (FMLLaunchHandler.side().isClient()) {
                initClientTransformers();
            }
            hasInit = true;
        }

        if (!ObfMapping.obfuscated) {
            if (name.equals("net.minecraft.world.World")) {
                ClassNode classNode = new ClassNode();
                ClassReader classReader = new ClassReader(bytes);
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                classReader.accept(classNode, 0);
                FieldNode fieldNode = null;
                for (FieldNode node : classNode.fields) {
                    if (node.name.equals("collidingBoundingBoxes")) {
                        fieldNode = node;
                        break;
                    }
                }
                fieldNode.access = Opcodes.ACC_PUBLIC;

                classNode.accept(classWriter);
                bytes = classWriter.toByteArray();
            }
        }

        //debug(name, bytes);
        if (FMLLaunchHandler.side().isClient()) {
            try {
                bytes = clientTransformer.transform(name, bytes);
            } catch (Throwable t) {
                LogHelper.bigFatal("Unable to transform class [%s] using client transformer.", name);
                t.printStackTrace();
            }
        }
        try {
            bytes = commonTransformer.transform(name, bytes);
        } catch (Throwable t) {
            LogHelper.bigFatal("Unable to transform class [%s] using common transformer", name);
            t.printStackTrace();
        }
        //debug(name, bytes);
        bytes = injectProfilerData(name, bytes);
        return bytes;
    }

    private void initClientTransformers() {
        LogHelper.info("Loading Client Transformers.");
        if (Config.enableInvalidPotionFix) {
            clientTransformer.add(new MethodInjector(new ObfMapping("net/minecraft/potion/PotionEffect", "func_76455_a", "(Lnet/minecraft/entity/EntityLivingBase;)Z"), asmBlocks.get("potionFix"), true));
        }
    }

    private void initCommonTransformers() {
        LogHelper.info("Loading Common Transformers.");
        if (Config.enableNuminaConsoleSpamFix) {
            commonTransformer.add(new MethodReplacer(new ObfMapping("net/machinemuse/numina/recipe/SimpleItemMaker", "getRecipeOutput", "()Lnet/minecraft/item/ItemStack;"), asmBlocks.get("n_numinaLogSpam"), new ASMBlock()));
            commonTransformer.add(new MethodReplacer(new ObfMapping("net/machinemuse/numina/recipe/SimpleItemMatcher", "matchesItem", "(Lnet/minecraft/item/ItemStack;)Z"), asmBlocks.get("n_numinaLogSpam"), new ASMBlock()));
        }
    }

    private ArrayList<String> superTileRenderClasses = new ArrayList<String>();

    private byte[] injectProfilerData(String className, byte[] bytes) {
        if (superTileRenderClasses.isEmpty()) {
            superTileRenderClasses.add("net/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer");
        }
        try {
            ObfMapping mapping = new ObfMapping(className.replace(".", "/"), "func_147500_a", "(Lnet/minecraft/tileentity/TileEntity;DDDF)V").toRuntime();
            ClassNode classNode = ASMHelper.createClassNode(bytes);
            if ("net/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer".equals(classNode.superName)) {
                MethodNode methodNode = ASMHelper.findMethod(mapping, classNode);
                if (methodNode == null) {
                    superTileRenderClasses.add(className.replace(".", "/"));
                    LogHelper.info("Found class that extends TileEntitySpecialRenderer but does not implement renderTileEntityAt.. [%s]", className);
                } else {
                    injectProfilerNodes(methodNode);
                    bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                }
            } else if (superTileRenderClasses.contains(classNode.superName)) {
                MethodNode methodNode = ASMHelper.findMethod(mapping, classNode);
                if (methodNode == null){
                    LogHelper.info("Found a class that extends a suspected super tile renderer that does not implement renderTileEntityAt.. [%s]", className);
                    superTileRenderClasses.add(className.replace(".", "/"));
                } else {
                    injectProfilerNodes(methodNode);
                    bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.fatal("Unable to inject nodes!");
        }
        return bytes;
    }

    private void injectProfilerNodes(MethodNode methodNode){
        methodNode.instructions.insert(asmBlocks.get("i_preRenderTile").rawListCopy());

        for (AbstractInsnNode node : methodNode.instructions.toArray()) {
            if (node.getOpcode() == Opcodes.RETURN) {
                methodNode.instructions.insertBefore(node, asmBlocks.get("i_postRenderTile").rawListCopy());
            }
        }
    }

    private static void debug(String name, byte[] bytes) {
        ObfMapping mapping = new ObfMapping("ExAstris/Block/TileEntity/TileEntitySieveAutomatic", "spawnFX", "(Lnet/minecraft/block/Block;I)V").toRuntime();
        if (name.equals("ExAstris.Block.TileEntity.TileEntitySieveAutomatic")) {
            LogHelper.info("\n");
            LogHelper.info(name);
            ClassNode classNode = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            reader.accept(classNode, 8);
            for (MethodNode node : classNode.methods) {
                LogHelper.info("Name: [%s], Desc: [%s].", node.name, node.desc);
            }
            MethodNode methodNode = ASMHelper.findMethod(mapping, classNode);
            if (methodNode == null) {
                LogHelper.info("Unable to find method!");
            } else {
                LogHelper.info("\n Instructions: \n");
                LogHelper.info(ASMHelper.toString(methodNode.instructions));
            }
            LogHelper.info("\n");
        }
    }

}
