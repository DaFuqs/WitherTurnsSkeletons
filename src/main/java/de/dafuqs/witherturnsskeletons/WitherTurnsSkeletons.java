package de.dafuqs.witherturnsskeletons;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;

public class WitherTurnsSkeletons {

    public static final String MOD_ID = "witherturnsskeletons";
    
    public static final SoundEvent CONVERTING_SOUND = registerSoundEvent("subtitles.skeleton.converted_to_wither_skeleton");
    
    private static SoundEvent registerSoundEvent(String name) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

}
