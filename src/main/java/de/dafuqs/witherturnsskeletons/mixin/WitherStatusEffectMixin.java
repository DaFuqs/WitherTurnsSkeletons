package de.dafuqs.witherturnsskeletons.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(WitherStatusEffect.class)
public abstract class WitherStatusEffectMixin {
	
	@Inject(method = "applyUpdateEffect(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;I)Z",
			at = @At("HEAD"),
			cancellable = true)
	public void isInvulnerableTo(ServerWorld world, LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof SkeletonEntity) {
			cir.setReturnValue(true);
		}
	}
	
}
