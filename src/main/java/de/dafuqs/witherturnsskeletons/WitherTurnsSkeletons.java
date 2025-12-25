package de.dafuqs.witherturnsskeletons;

import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import org.apache.logging.log4j.*;


public class WitherTurnsSkeletons implements ModInitializer {

    public static final String MOD_ID = "witherturnsskeletons";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    public static final SoundEvent CONVERTING_SOUND = registerSoundEvent("subtitles.skeleton.converted_to_wither_skeleton");
    
    @Override
    public void onInitialize() {
        LOGGER.info("Startup Finished!");
    }
    
    private static SoundEvent registerSoundEvent(String name) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

}
