package covers1624.asmfixes.asm;

import buildcraft.core.DefaultProps;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by covers1624 on 3/7/2016.
 */
public class CoreMod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        FMLLog.info("Hello!!");
        return new String[] { ASMTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        FMLLog.info("%s", DefaultProps.MARKER_RANGE);
        /*try {
            Class clazz = Class.forName("buildcraft.core.DefaultProps");
            clazz.newInstance();
            Field field = clazz.getField("MARKER_RANGE");
            field.set(null, 256);
        } catch (Throwable t) {
            t.printStackTrace();
        }*/
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
