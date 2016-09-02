package covers1624.asmfixes.asm;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by covers1624 on 1/09/2016.
 */
public class ASMHooksClient {

    public static void preRenderTile(TileEntitySpecialRenderer renderer) {
        //renderProfiler.startSection(renderer.getClass().getSimpleName());
        Minecraft.getMinecraft().mcProfiler.startSection(renderer.getClass().getSimpleName());

    }

    public static void postRenderTile() {
        //renderProfiler.endSection();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }
}
