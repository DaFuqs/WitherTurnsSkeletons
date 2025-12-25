package de.dafuqs.witherturnsskeletons;

import com.mojang.serialization.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.registries.*;
import org.jspecify.annotations.*;

import java.util.function.*;

@Mod(WitherTurnsSkeletons.MOD_ID)
public class WitherTurnsSkeletons {
	
	public static final String MOD_ID = "witherturnsskeletons";
	
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);
	private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);
	
	public static final SoundEvent CONVERTING_SOUND = registerSoundEvent("subtitles.skeleton.converted_to_wither_skeleton");
	
	public static final Supplier<AttachmentType<Integer>> WITHER_SKELETON_CONVERSION_TIME = ATTACHMENT_TYPES.register("wither_skeleton_conversion_time", () -> AttachmentType.builder(() -> 0)
			.sync(new AttachmentSyncHandler<>() {
				@Override
				public void write(@NonNull RegistryFriendlyByteBuf buf, Integer attachment, boolean initialSync) {
					buf.writeInt(attachment);
				}
				
				@Override
				public @NonNull Integer read(@NonNull IAttachmentHolder holder, @NonNull RegistryFriendlyByteBuf buf, @Nullable Integer previousValue) {
					return buf.readInt();
				}
			})
			.serialize(Codec.INT.fieldOf("wither_skeleton_conversion_time"))
			.build()
	);
	
	public WitherTurnsSkeletons(IEventBus modBus) {
		ATTACHMENT_TYPES.register(modBus);
		SOUND_EVENTS.register(modBus);
	}
	
	private static SoundEvent registerSoundEvent(String name) {
		SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, name));
		SOUND_EVENTS.register(name, () -> soundEvent);
		return soundEvent;
	}
	
}
