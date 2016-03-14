package covers1624.asmfixes.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by covers1624 on 3/13/2016.
 */
public class Config {

    /**
     * Client
     */
    public static boolean enableInvalidPotionFix;
    //public static boolean enableBuildcraftBuilderDumpButton;

    /**
     * Common.
     */
    public static boolean enableNuminaConsoleSpamFix;

    private static Configuration configuration;

    public static void init(File config) {
        if (configuration == null) {
            configuration = new Configuration(config);
        }
        loadConfig();
    }

    public static void loadConfig() {
        enableInvalidPotionFix = configuration.getBoolean("invalidClientPotionFix", "client", true, "Your client will not crash if an invalid potion is applied to your player.");
        enableNuminaConsoleSpamFix = configuration.getBoolean("numinaConsoleSpamFix", "common", true, "Your console will not be spammed with: [WARNING: unlocalizedName is deprecated; please use registryName or itemStackName instead!].");

        configuration.save();
    }

}
