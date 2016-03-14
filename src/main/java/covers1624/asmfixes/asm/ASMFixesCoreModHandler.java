package covers1624.asmfixes.asm;

import covers1624.asmfixes.config.Config;
import covers1624.asmfixes.util.LogHelper;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.util.Map;

/**
 * Created by covers1624 on 3/7/2016.
 */
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class ASMFixesCoreModHandler implements IFMLLoadingPlugin {
    private static File configFolder = new File(Launch.minecraftHome, "config");
    public ASMFixesCoreModHandler() {
        LogHelper.info("ASM-Fixes Says Hello!");
        Config.init(new File(configFolder, "ASM-Fixes.cfg"));
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { ASMTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        //new SimpleItemMaker();
        //new SimpleItemMatcher();
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
