package de.dafuqs.witherturnsskeletons;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class WitherTurnsSkeletons implements ModInitializer {

    public static final String MOD_ID = "witherturnsskeletons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void log(Level logLevel, String message) {
        LOGGER.log(logLevel, "[WitherTurnsSkeletons] " +  message);
    }

    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Starting up...");

        log(Level.INFO, "Startup Finished!");
    }

}
