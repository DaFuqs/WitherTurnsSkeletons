package de.dafuqs.witherturnsskeletons;

import net.fabricmc.api.*;
import org.apache.logging.log4j.*;


public class WitherTurnsSkeletons implements ModInitializer {

    public static final String MOD_ID = "witherturnsskeletons";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    @Override
    public void onInitialize() {
        LOGGER.info("Startup Finished!");
    }

}
