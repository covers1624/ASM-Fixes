package covers1624.asmfixes.asm;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by covers1624 on 3/7/2016.
 */
public class CoreMod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        FMLLog.info("ASM-Fixes Says Hello!");
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
